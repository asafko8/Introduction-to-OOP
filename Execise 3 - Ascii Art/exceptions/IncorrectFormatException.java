package exceptions;

/**
 * Represents exception caused by incorrect format issues.
 */
public class IncorrectFormatException extends Exception {

    private final String type;

    /**
     * Constructor.
     *
     * @param type The command where the exception error occurred.
     */
    public IncorrectFormatException(String type) {
        super(type);
        this.type = type;
    }

    /**
     * Getter.
     *
     * @return Where the exception error occurred.
     */
    public String getType() {
        return this.type;
    }
}
