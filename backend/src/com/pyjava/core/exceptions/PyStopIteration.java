package com.pyjava.core.exceptions;

/**
 * Created by Cristiano on 18/06/2015.
 */
public class PyStopIteration extends PyException {

    public PyStopIteration() {
        super("Iteracion finalizada.");
    }

    public PyStopIteration(String message) {
        super(message);
    }

    public PyStopIteration(String message, Throwable cause) {
        super(message, cause);
    }

    public PyStopIteration(Throwable cause) {
        super(cause);
    }

    public PyStopIteration(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getName(){
        return "StopIteration";
    }
}
