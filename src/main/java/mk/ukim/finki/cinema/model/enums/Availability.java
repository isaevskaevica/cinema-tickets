package mk.ukim.finki.cinema.model.enums;

public enum Availability {

    AVAILABLE("Available"),
    FEW_LEFT("Few seats left"),
    SOLD_OUT("Sold out"),
    FINISHED("Finished");

    private final String displayName;

    Availability(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
