package mk.ukim.finki.cinema.config;

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
import mk.ukim.finki.cinema.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "cinema.seed-data", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final ScreeningRepository screeningRepository;
    private final TicketRepository ticketRepository;

    private final List<Ticket> tickets = new ArrayList<>();

    public DataSeeder(MovieRepository movieRepository,
                      HallRepository hallRepository,
                      ScreeningRepository screeningRepository,
                      TicketRepository ticketRepository) {
        this.movieRepository = movieRepository;
        this.hallRepository = hallRepository;
        this.screeningRepository = screeningRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (movieRepository.count() > 0) {
            log.info("Database already contains data, skipping seed.");
            return;
        }
        log.info("Seeding sample movies, halls, screenings and tickets.");

        Movie inception = new Movie("Inception", Genre.SCI_FI, AgeRating.PG_13, 148, 2010, "Christopher Nolan",
                "A thief who steals secrets through dreams is offered a chance to have his past erased.");
        Movie beforeTheRain = new Movie("Before the Rain", Genre.DRAMA, AgeRating.R, 113, 1994, "Milčo Mančevski",
                "Three intertwined stories set in Macedonia and London.");
        Movie honeyland = new Movie("Honeyland", Genre.DOCUMENTARY, AgeRating.PG, 89, 2019,
                "Tamara Kotevska, Ljubomir Stefanov",
                "The last female wild beekeeper in Europe and the family that moves in next door.");
        Movie spiritedAway = new Movie("Spirited Away", Genre.ANIMATION, AgeRating.PG, 125, 2001, "Hayao Miyazaki",
                "A girl wanders into a world of spirits and must work to free her parents.");
        Movie parasite = new Movie("Parasite", Genre.THRILLER, AgeRating.R, 132, 2019, "Bong Joon-ho",
                "A poor family schemes to become employed by a wealthy household.");
        Movie grandBudapest = new Movie("The Grand Budapest Hotel", Genre.COMEDY, AgeRating.R, 99, 2014, "Wes Anderson",
                "A concierge and his lobby boy get caught up in the theft of a priceless painting.");
        Movie madMax = new Movie("Mad Max: Fury Road", Genre.ACTION, AgeRating.R, 120, 2015, "George Miller",
                "A woman rebels against a tyrannical ruler in a post-apocalyptic wasteland.");
        Movie toyStory = new Movie("Toy Story", Genre.ANIMATION, AgeRating.G, 81, 1995, "John Lasseter",
                "A cowboy doll feels threatened when a new spaceman figure becomes the favourite toy.");
        Movie getOut = new Movie("Get Out", Genre.HORROR, AgeRating.R, 104, 2017, "Jordan Peele",
                "A young man uncovers a disturbing secret while visiting his girlfriend's family.");
        Movie amelie = new Movie("Amélie", Genre.ROMANCE, AgeRating.R, 122, 2001, "Jean-Pierre Jeunet",
                "A shy waitress decides to change the lives of those around her for the better.");
        movieRepository.saveAll(List.of(inception, beforeTheRain, honeyland, spiritedAway, parasite,
                grandBudapest, madMax, toyStory, getOut, amelie));

        Hall hall1 = new Hall("Hall 1", ScreenType.STANDARD, 120);
        Hall hall2 = new Hall("Hall 2", ScreenType.STANDARD, 60);
        Hall hall3 = new Hall("Hall 3", ScreenType.THREE_D, 100);
        Hall imax = new Hall("IMAX", ScreenType.IMAX, 200);
        Hall vip = new Hall("VIP Lounge", ScreenType.DOLBY_ATMOS, 30);
        hallRepository.saveAll(List.of(hall1, hall2, hall3, imax, vip));

        LocalDate today = LocalDate.now();
        Screening inceptionToday = new Screening(inception, imax, today.atTime(20, 30), 350);
        Screening toyStoryToday = new Screening(toyStory, hall1, today.atTime(16, 0), 200);
        Screening parasiteToday = new Screening(parasite, hall2, today.atTime(21, 0), 250);
        Screening beforeTheRainToday = new Screening(beforeTheRain, vip, today.atTime(19, 0), 500);
        Screening spiritedTomorrow = new Screening(spiritedAway, hall3, today.plusDays(1).atTime(17, 30), 300);
        Screening honeylandTomorrow = new Screening(honeyland, hall2, today.plusDays(1).atTime(18, 0), 250);
        Screening madMaxTomorrow = new Screening(madMax, imax, today.plusDays(1).atTime(21, 0), 350);
        Screening getOutTomorrow = new Screening(getOut, hall1, today.plusDays(1).atTime(22, 30), 250);
        Screening amelieDay2 = new Screening(amelie, vip, today.plusDays(2).atTime(20, 0), 500);
        Screening budapestDay2 = new Screening(grandBudapest, hall1, today.plusDays(2).atTime(19, 0), 250);
        Screening inceptionDay2 = new Screening(inception, imax, today.plusDays(2).atTime(20, 30), 350);
        Screening parasiteDay3 = new Screening(parasite, hall2, today.plusDays(3).atTime(20, 0), 250);
        Screening toyStoryDay3 = new Screening(toyStory, hall3, today.plusDays(3).atTime(15, 0), 300);
        Screening getOutYesterday = new Screening(getOut, hall2, today.minusDays(1).atTime(21, 0), 250);
        List<Screening> screenings = List.of(inceptionToday, toyStoryToday, parasiteToday, beforeTheRainToday,
                spiritedTomorrow, honeylandTomorrow, madMaxTomorrow, getOutTomorrow, amelieDay2, budapestDay2,
                inceptionDay2, parasiteDay3, toyStoryDay3, getOutYesterday);
        screeningRepository.saveAll(screenings);

        LocalDateTime now = LocalDateTime.now();
        book(inceptionToday, "Ana Petrovska", "ana.petrovska@example.com", 2, now.minusDays(3));
        book(inceptionToday, "Marko Stojanov", "marko.stojanov@example.com", 4, now.minusDays(2));
        book(beforeTheRainToday, "Elena Nikolova", "elena.nikolova@example.com", 12, now.minusDays(5));
        book(beforeTheRainToday, "Filip Dimitrov", "filip.dimitrov@example.com", 10, now.minusDays(4));
        book(beforeTheRainToday, "Sara Trajkova", "sara.trajkova@example.com", 8, now.minusDays(1));
        book(parasiteToday, "Nikola Ristov", "nikola.ristov@example.com", 20, now.minusDays(4));
        book(parasiteToday, "Ana Petrovska", "ana.petrovska@example.com", 20, now.minusDays(2));
        book(parasiteToday, "Marko Stojanov", "marko.stojanov@example.com", 14, now.minusHours(6));
        book(toyStoryToday, "Sara Trajkova", "sara.trajkova@example.com", 3, now.minusDays(1));
        book(spiritedTomorrow, "Marko Stojanov", "marko.stojanov@example.com", 2, now.minusHours(20));
        book(madMaxTomorrow, "Elena Nikolova", "elena.nikolova@example.com", 5, now.minusHours(3));
        book(getOutYesterday, "Filip Dimitrov", "filip.dimitrov@example.com", 2, now.minusDays(3));
        ticketRepository.saveAll(tickets);
        screeningRepository.saveAll(screenings);

        log.info("Seed data loaded.");
    }

    private void book(Screening screening, String name, String email, int seats, LocalDateTime bookedAt) {
        Ticket ticket = new Ticket(screening, name, email, seats, seats * screening.getPrice());
        ticket.setBookedAt(bookedAt);
        screening.setSeatsSold(screening.getSeatsSold() + seats);
        tickets.add(ticket);
    }
}
