package apsi.team3.backend.exceptions;

public class ApsiException extends Exception {
    public ApsiException(String s) {
        super(s);
    }

    public ApsiException(Throwable e){
        super(e);
    }
}
