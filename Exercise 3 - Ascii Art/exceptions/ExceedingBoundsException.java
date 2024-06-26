package exceptions;

/**
 * Represents exception caused by the attempt of getting higher/lower resolution then the image limit.
 */
public class ExceedingBoundsException extends Exception {

    private final String type;

    /**
     * Constructor.
     *
     * @param type The command where the exception error occurred.
     */
    public ExceedingBoundsException(String type){
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
