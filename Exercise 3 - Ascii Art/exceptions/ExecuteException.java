package exceptions;

/**
 * Represents exception caused by execute issues.
 */
public class ExecuteException extends Exception {

    private final String type;

    /**
     * Constructor.
     *
     * @param type The command where the exception error occurred.
     */
    public ExecuteException(String type) {
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
