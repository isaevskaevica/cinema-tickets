package mk.ukim.finki.cinema.service;

import mk.ukim.finki.cinema.model.Hall;
import mk.ukim.finki.cinema.repository.HallRepository;
import mk.ukim.finki.cinema.repository.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HallService {

    private final HallRepository hallRepository;
    private final ScreeningRepository screeningRepository;

    public HallService(HallRepository hallRepository, ScreeningRepository screeningRepository) {
        this.hallRepository = hallRepository;
        this.screeningRepository = screeningRepository;
    }

    public List<Hall> findAll() {
        return hallRepository.findAllByOrderByNameAsc();
    }

    public Hall findById(Long id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hall", id));
    }

    public long countUpcomingScreenings(Hall hall) {
        return screeningRepository.countByHallAndStartsAtAfter(hall, LocalDateTime.now());
    }
}
