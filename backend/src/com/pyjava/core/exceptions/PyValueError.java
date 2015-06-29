package com.pyjava.core.exceptions;

/**
 * Created by Cristiano on 18/06/2015.
 */
public class PyValueError extends PyException {

    public PyValueError() {
        super("Error de valor.");
    }

    public PyValueError(String message) {
        super(message);
    }

    public PyValueError(String message, Throwable cause) {
        super(message, cause);
    }

    public PyValueError(Throwable cause) {
        super(cause);
    }

    public PyValueError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getName(){
        return "Value Error";
    }
}
