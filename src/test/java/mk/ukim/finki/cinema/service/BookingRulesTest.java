package mk.ukim.finki.cinema.service;

import mk.ukim.finki.cinema.model.Hall;
import mk.ukim.finki.cinema.model.Movie;
import mk.ukim.finki.cinema.model.Screening;
import mk.ukim.finki.cinema.model.Ticket;
import mk.ukim.finki.cinema.model.enums.AgeRating;
import mk.ukim.finki.cinema.model.enums.Genre;
import mk.ukim.finki.cinema.model.enums.ScreenType;
import mk.ukim.finki.cinema.repository.HallRepository;
import mk.ukim.finki.cinema.repository.MovieRepository;
import mk.ukim.finki.cinema.repository.ScreeningRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BookingRulesTest {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private ScreeningService screeningService;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private ScreeningRepository screeningRepository;

    private Screening screening(String hallName, int capacity, LocalDateTime startsAt) {
        Movie movie = movieRepository.save(
                new Movie("Test Movie", Genre.DRAMA, AgeRating.PG, 100, 2020, null, null));
        Hall hall = hallRepository.save(new Hall(hallName, ScreenType.STANDARD, capacity));
        return screeningRepository.save(new Screening(movie, hall, startsAt, 250));
    }

    @Test
    void bookingReducesSeatsLeftAndComputesTotal() {
        Screening s = screening("Rules Hall A", 10, LocalDateTime.now().plusDays(1));

        Ticket ticket = ticketService.book(s.getId(), "Ana", "ana@test.mk", 3);

        assertEquals(750, ticket.getTotalPrice());
        assertEquals(7, screeningRepository.findById(s.getId()).orElseThrow().getSeatsLeft());
    }

    @Test
    void cannotBookMoreSeatsThanAreLeft() {
        Screening s = screening("Rules Hall B", 10, LocalDateTime.now().plusDays(1));
        ticketService.book(s.getId(), "Marko", "marko@test.mk", 8);

        assertThrows(CinemaRuleViolationException.class,
                () -> ticketService.book(s.getId(), "Elena", "elena@test.mk", 3));
        assertEquals(2, screeningRepository.findById(s.getId()).orElseThrow().getSeatsLeft());
    }

    @Test
    void cannotBookAScreeningThatAlreadyStarted() {
        Screening s = screening("Rules Hall C", 10, LocalDateTime.now().minusHours(1));

        assertThrows(CinemaRuleViolationException.class,
                () -> ticketService.book(s.getId(), "Filip", "filip@test.mk", 1));
    }

    @Test
    void cancellingABookingReleasesItsSeats() {
        Screening s = screening("Rules Hall D", 10, LocalDateTime.now().plusDays(1));
        Ticket ticket = ticketService.book(s.getId(), "Sara", "sara@test.mk", 4);

        ticketService.cancel(ticket.getId());

        assertEquals(10, screeningRepository.findById(s.getId()).orElseThrow().getSeatsLeft());
    }

    @Test
    void cannotMoveAScreeningToAHallSmallerThanSeatsSold() {
        Screening s = screening("Rules Hall E", 50, LocalDateTime.now().plusDays(1));
        ticketService.book(s.getId(), "Nikola", "nikola@test.mk", 20);
        ticketService.book(s.getId(), "Ana", "ana2@test.mk", 20);
        Hall smaller = hallRepository.save(new Hall("Rules Hall F", ScreenType.STANDARD, 30));

        assertThrows(CinemaRuleViolationException.class, () -> screeningService.update(
                s.getId(), s.getMovie().getId(), smaller.getId(), s.getStartsAt(), s.getPrice()));
    }
}
