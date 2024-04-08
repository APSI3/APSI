package apsi.team3.backend.exceptions;

public class ApsiValidationException extends ApsiException {
    public String key;

    public ApsiValidationException(String message, String key) {
        super(message);
        this.key = key;
    }

    public ApsiValidationException(Throwable e) {
        super(e);
    }
}
