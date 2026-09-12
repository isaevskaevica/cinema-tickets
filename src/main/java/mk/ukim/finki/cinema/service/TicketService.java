package mk.ukim.finki.cinema.service;

import mk.ukim.finki.cinema.model.Screening;
import mk.ukim.finki.cinema.model.Ticket;
import mk.ukim.finki.cinema.repository.ScreeningRepository;
import mk.ukim.finki.cinema.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TicketService {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("EEE d MMM, HH:mm");

    private final TicketRepository ticketRepository;
    private final ScreeningRepository screeningRepository;
    private final ScreeningService screeningService;

    public TicketService(TicketRepository ticketRepository,
                         ScreeningRepository screeningRepository,
                         ScreeningService screeningService) {
        this.ticketRepository = ticketRepository;
        this.screeningRepository = screeningRepository;
        this.screeningService = screeningService;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAllByOrderByBookedAtDesc();
    }

    public Ticket findById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", id));
    }

    public long count() {
        return ticketRepository.count();
    }

    public long totalSeatsSold() {
        return ticketRepository.sumSeats();
    }

    /**
     * Booking is refused once the screening has started or the hall has fewer seats left than requested.
     */
    @Transactional
    public Ticket book(Long screeningId, String customerName, String customerEmail, int seats) {
        Screening screening = screeningService.findById(screeningId);
        String label = screening.getMovie().getTitle() + " (" + screening.getStartsAt().format(TIME) + ")";

        if (screening.isPast()) {
            throw new CinemaRuleViolationException(label + " has already started.");
        }
        int left = screening.getSeatsLeft();
        if (left <= 0) {
            throw new CinemaRuleViolationException(label + " is sold out.");
        }
        if (seats > left) {
            throw new CinemaRuleViolationException(
                    "Only " + left + (left == 1 ? " seat is" : " seats are") + " left for " + label + ".");
        }

        screening.setSeatsSold(screening.getSeatsSold() + seats);
        screeningRepository.save(screening);

        return ticketRepository.save(
                new Ticket(screening, customerName, customerEmail, seats, seats * screening.getPrice()));
    }

    @Transactional
    public Ticket cancel(Long id) {
        Ticket ticket = findById(id);
        Screening screening = ticket.getScreening();

        screening.setSeatsSold(Math.max(0, screening.getSeatsSold() - ticket.getSeats()));
        screeningRepository.save(screening);
        ticketRepository.delete(ticket);
        return ticket;
    }
}
