package lwi.vision.domain.enumeration;

/**
 * The UpdateType enumeration.
 */
public enum UpdateType {
    FIRMWARE("Firmware"),
    SOFTWARE("Software");

    private final String value;

    UpdateType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
