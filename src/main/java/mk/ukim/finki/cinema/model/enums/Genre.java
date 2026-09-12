package mk.ukim.finki.cinema.model.enums;

public enum Genre {

    ACTION("Action"),
    ADVENTURE("Adventure"),
    ANIMATION("Animation"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DOCUMENTARY("Documentary"),
    DRAMA("Drama"),
    FANTASY("Fantasy"),
    HORROR("Horror"),
    ROMANCE("Romance"),
    SCI_FI("Science fiction"),
    THRILLER("Thriller");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
