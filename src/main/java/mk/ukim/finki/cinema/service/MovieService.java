package mk.ukim.finki.cinema.service;

import mk.ukim.finki.cinema.model.Movie;
import mk.ukim.finki.cinema.model.Screening;
import mk.ukim.finki.cinema.model.enums.Genre;
import mk.ukim.finki.cinema.repository.MovieRepository;
import mk.ukim.finki.cinema.repository.ScreeningRepository;
import mk.ukim.finki.cinema.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    private final TicketRepository ticketRepository;

    public MovieService(MovieRepository movieRepository,
                        ScreeningRepository screeningRepository,
                        TicketRepository ticketRepository) {
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<Movie> search(Genre genre) {
        return genre == null
                ? movieRepository.findAllByOrderByTitleAsc()
                : movieRepository.findByGenreOrderByTitleAsc(genre);
    }

    public Movie findById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", id));
    }

    @Transactional
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    public void delete(Long id) {
        Movie movie = findById(id);
        for (Screening screening : screeningRepository.findByMovie(movie)) {
            ticketRepository.deleteByScreening(screening);
        }
        screeningRepository.deleteByMovie(movie);
        movieRepository.delete(movie);
    }

    public long count() {
        return movieRepository.count();
    }
}
