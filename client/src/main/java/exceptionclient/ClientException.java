package exceptionclient;

public class ClientException extends Exception {

    final private int statusCode;
    public ClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
