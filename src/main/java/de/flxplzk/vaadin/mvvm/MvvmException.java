package de.flxplzk.vaadin.mvvm;

class MvvmException extends RuntimeException {
    MvvmException() {
    }

    MvvmException(String message) {
        super(message);
    }

    MvvmException(String message, Throwable cause) {
        super(message, cause);
    }

    MvvmException(NoSuchFieldException e) {
        super(e);
    }

    MvvmException(Exception e) {
        super(e);
    }
}
