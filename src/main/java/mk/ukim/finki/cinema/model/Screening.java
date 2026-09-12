package mk.ukim.finki.cinema.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import mk.ukim.finki.cinema.model.enums.Availability;

import java.time.LocalDateTime;

@Entity
@Table(name = "screenings")
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private int seatsSold = 0;

    public Screening() {
    }

    public Screening(Movie movie, Hall hall, LocalDateTime startsAt, Integer price) {
        this.movie = movie;
        this.hall = hall;
        this.startsAt = startsAt;
        this.price = price;
    }

    public LocalDateTime getEndsAt() {
        return startsAt.plusMinutes(movie.getDurationMinutes());
    }

    public int getSeatsLeft() {
        return hall.getCapacity() - seatsSold;
    }

    public boolean isPast() {
        return startsAt.isBefore(LocalDateTime.now());
    }

    public Availability getAvailability() {
        if (isPast()) {
            return Availability.FINISHED;
        }
        int left = getSeatsLeft();
        if (left <= 0) {
            return Availability.SOLD_OUT;
        }
        if (left <= Math.max(5, hall.getCapacity() / 10)) {
            return Availability.FEW_LEFT;
        }
        return Availability.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public int getSeatsSold() {
        return seatsSold;
    }

    public void setSeatsSold(int seatsSold) {
        this.seatsSold = seatsSold;
    }
}
