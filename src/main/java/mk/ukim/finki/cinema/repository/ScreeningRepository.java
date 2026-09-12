package mk.ukim.finki.cinema.repository;

import mk.ukim.finki.cinema.model.Hall;
import mk.ukim.finki.cinema.model.Movie;
import mk.ukim.finki.cinema.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findAllByOrderByStartsAtAsc();

    List<Screening> findByStartsAtAfterOrderByStartsAtAsc(LocalDateTime after);

    List<Screening> findByMovie(Movie movie);

    long countByStartsAtAfter(LocalDateTime after);

    long countByHallAndStartsAtAfter(Hall hall, LocalDateTime after);

    void deleteByMovie(Movie movie);
}
