package mk.ukim.finki.cinema.service;

import mk.ukim.finki.cinema.model.Hall;
import mk.ukim.finki.cinema.model.Screening;
import mk.ukim.finki.cinema.repository.ScreeningRepository;
import mk.ukim.finki.cinema.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final TicketRepository ticketRepository;
    private final MovieService movieService;
    private final HallService hallService;

    public ScreeningService(ScreeningRepository screeningRepository,
                            TicketRepository ticketRepository,
                            MovieService movieService,
                            HallService hallService) {
        this.screeningRepository = screeningRepository;
        this.ticketRepository = ticketRepository;
        this.movieService = movieService;
        this.hallService = hallService;
    }

    public List<Screening> search(Long movieId, Long hallId, boolean includePast) {
        LocalDateTime now = LocalDateTime.now();
        return screeningRepository.findAllByOrderByStartsAtAsc().stream()
                .filter(s -> includePast || !s.getStartsAt().isBefore(now))
                .filter(s -> movieId == null || s.getMovie().getId().equals(movieId))
                .filter(s -> hallId == null || s.getHall().getId().equals(hallId))
                .toList();
    }

    public Map<LocalDate, List<Screening>> groupByDay(List<Screening> screenings) {
        return screenings.stream().collect(Collectors.groupingBy(
                s -> s.getStartsAt().toLocalDate(), TreeMap::new, Collectors.toList()));
    }

    public List<Screening> findUpcoming() {
        return screeningRepository.findByStartsAtAfterOrderByStartsAtAsc(LocalDateTime.now());
    }

    public List<Screening> findBookable() {
        return findUpcoming().stream().filter(s -> s.getSeatsLeft() > 0).toList();
    }

    public Screening findById(Long id) {
        return screeningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Screening", id));
    }

    public long countUpcoming() {
        return screeningRepository.countByStartsAtAfter(LocalDateTime.now());
    }

    @Transactional
    public Screening create(Long movieId, Long hallId, LocalDateTime startsAt, Integer price) {
        Screening screening = new Screening(
                movieService.findById(movieId), hallService.findById(hallId), startsAt, price);
        return screeningRepository.save(screening);
    }

    /**
     * A screening can only move to a hall that still fits the seats already sold.
     */
    @Transactional
    public Screening update(Long id, Long movieId, Long hallId, LocalDateTime startsAt, Integer price) {
        Screening screening = findById(id);
        Hall hall = hallService.findById(hallId);

        if (hall.getCapacity() < screening.getSeatsSold()) {
            throw new CinemaRuleViolationException(
                    hall.getName() + " has " + hall.getCapacity() + " seats, but "
                            + screening.getSeatsSold() + " are already sold for this screening.");
        }

        screening.setMovie(movieService.findById(movieId));
        screening.setHall(hall);
        screening.setStartsAt(startsAt);
        screening.setPrice(price);
        return screeningRepository.save(screening);
    }

    @Transactional
    public void delete(Long id) {
        Screening screening = findById(id);
        ticketRepository.deleteByScreening(screening);
        screeningRepository.delete(screening);
    }
}
