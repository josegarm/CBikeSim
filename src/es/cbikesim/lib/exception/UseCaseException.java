package es.cbikesim.lib.exception;

public class UseCaseException extends Exception {

    public UseCaseException(String message) {
        super(message);
    }

    public UseCaseException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
