package de.flxplzk.vaadin.mvvm;

public class BindingException extends MvvmException {

    public BindingException(String message) {
        super(message);
    }

    public BindingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BindingException(NoSuchFieldException e) {
        super(e);
    }

    public BindingException(Exception e) {
        super(e);
    }
}

