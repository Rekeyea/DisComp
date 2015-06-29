package com.pyjava.core.exceptions;

/**
 * Created by Cristiano on 18/06/2015.
 */
public class PyZeroDivisionError extends PyException {

    public PyZeroDivisionError() {
        super("Division por cero.");
    }

    public PyZeroDivisionError(String message) {
        super(message);
    }

    public PyZeroDivisionError(String message, Throwable cause) {
        super(message, cause);
    }

    public PyZeroDivisionError(Throwable cause) {
        super(cause);
    }

    public PyZeroDivisionError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getName(){
        return "ZeroDivisionError";
    }
}
