package de.flxplzk.vaddon.exception;

public class CommonAddOnException extends RuntimeException {

    public CommonAddOnException() {
    }

    public CommonAddOnException(String message) {
        super(message);
    }

    public CommonAddOnException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonAddOnException(Throwable cause) {
        super(cause);
    }

    public CommonAddOnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static class AsyncTaskException extends RuntimeException {
        public AsyncTaskException(String message) {
            super(message);
        }
    }
}
