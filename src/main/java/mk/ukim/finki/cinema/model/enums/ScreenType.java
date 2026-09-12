package mk.ukim.finki.cinema.model.enums;

public enum ScreenType {

    STANDARD("Standard"),
    THREE_D("3D"),
    IMAX("IMAX"),
    DOLBY_ATMOS("Dolby Atmos");

    private final String displayName;

    ScreenType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
