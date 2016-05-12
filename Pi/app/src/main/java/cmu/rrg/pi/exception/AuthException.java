package cmu.rrg.pi.exception;

/**
 * Created by Yang on 5/3/16.
 */
public class AuthException extends Exception {

    private static final long serialVersionUID = 1L;

    protected AuthException() {
        super();
    }

    public AuthException(String detailMessage) {
        super(detailMessage);
    }

    public AuthException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AuthException(Throwable throwable) {
        super(throwable);
    }
}
