package mk.ukim.finki.cinema.web.form;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import mk.ukim.finki.cinema.model.Screening;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ScreeningForm {

    @NotNull(message = "Pick a movie")
    private Long movieId;

    @NotNull(message = "Pick a hall")
    private Long hallId;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startsAt;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private Integer price;

    public static ScreeningForm from(Screening screening) {
        ScreeningForm form = new ScreeningForm();
        form.setMovieId(screening.getMovie().getId());
        form.setHallId(screening.getHall().getId());
        form.setStartsAt(screening.getStartsAt());
        form.setPrice(screening.getPrice());
        return form;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
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
}
