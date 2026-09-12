package mk.ukim.finki.cinema.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import mk.ukim.finki.cinema.model.enums.AgeRating;
import mk.ukim.finki.cinema.model.enums.Genre;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Genre genre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AgeRating ageRating;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private Integer releaseYear;

    @Column(length = 80)
    private String director;

    @Column(length = 500)
    private String description;

    public Movie() {
    }

    public Movie(String title, Genre genre, AgeRating ageRating, Integer durationMinutes,
                 Integer releaseYear, String director, String description) {
        this.title = title;
        this.genre = genre;
        this.ageRating = ageRating;
        this.durationMinutes = durationMinutes;
        this.releaseYear = releaseYear;
        this.director = director;
        this.description = description;
    }

    public String getDurationLabel() {
        if (durationMinutes == null) {
            return "";
        }
        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;
        if (hours == 0) {
            return minutes + "m";
        }
        return minutes == 0 ? hours + "h" : hours + "h " + minutes + "m";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
