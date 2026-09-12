package mk.ukim.finki.cinema.web.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import mk.ukim.finki.cinema.model.Movie;
import mk.ukim.finki.cinema.model.enums.AgeRating;
import mk.ukim.finki.cinema.model.enums.Genre;

public class MovieForm {

    @NotBlank(message = "Title is required")
    @Size(max = 120)
    private String title;

    @NotNull(message = "Genre is required")
    private Genre genre;

    @NotNull(message = "Age rating is required")
    private AgeRating ageRating;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 600, message = "Duration must be at most 600 minutes")
    private Integer durationMinutes;

    @NotNull(message = "Release year is required")
    @Min(value = 1888, message = "Release year must be 1888 or later")
    @Max(value = 2100, message = "Release year must be 2100 or earlier")
    private Integer releaseYear;

    @Size(max = 80)
    private String director;

    @Size(max = 500)
    private String description;

    public static MovieForm from(Movie movie) {
        MovieForm form = new MovieForm();
        form.setTitle(movie.getTitle());
        form.setGenre(movie.getGenre());
        form.setAgeRating(movie.getAgeRating());
        form.setDurationMinutes(movie.getDurationMinutes());
        form.setReleaseYear(movie.getReleaseYear());
        form.setDirector(movie.getDirector());
        form.setDescription(movie.getDescription());
        return form;
    }

    public void applyTo(Movie movie) {
        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setAgeRating(ageRating);
        movie.setDurationMinutes(durationMinutes);
        movie.setReleaseYear(releaseYear);
        movie.setDirector(director);
        movie.setDescription(description);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public AgeRating getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(AgeRating ageRating) {
        this.ageRating = ageRating;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
