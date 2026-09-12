package mk.ukim.finki.cinema.model.enums;

public enum AgeRating {

    G("G", "General audiences"),
    PG("PG", "Parental guidance suggested"),
    PG_13("PG-13", "Parents strongly cautioned"),
    R("R", "Restricted, 17 and over");

    private final String label;
    private final String description;

    AgeRating(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
