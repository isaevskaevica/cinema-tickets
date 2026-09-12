package mk.ukim.finki.cinema.repository;

import mk.ukim.finki.cinema.model.Movie;
import mk.ukim.finki.cinema.model.enums.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAllByOrderByTitleAsc();

    List<Movie> findByGenreOrderByTitleAsc(Genre genre);
}
