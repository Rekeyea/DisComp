package com.pyjava.core.exceptions;

/**
 * Created by Cristiano on 18/06/2015.
 */
public class PyNameError extends PyException {

    public PyNameError() {
        super("Error de nombre.");
    }

    public PyNameError(String message) {
        super(message);
    }

    public PyNameError(String message, Throwable cause) {
        super(message, cause);
    }

    public PyNameError(Throwable cause) {
        super(cause);
    }

    public PyNameError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getName(){
        return "NameError";
    }
}
