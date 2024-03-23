package model;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{

    final private int statusCode;
    public DataAccessException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return String.format("Error: %d. %s", statusCode, getMessage());
    }

}
