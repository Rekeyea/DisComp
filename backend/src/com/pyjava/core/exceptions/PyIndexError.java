package com.pyjava.core.exceptions;

/**
 * Created by Cristiano on 18/06/2015.
 */
public class PyIndexError extends PyException {

    public PyIndexError() {
        super("Indice fuera de rango.");
    }

    public PyIndexError(String message) {
        super(message);
    }

    public PyIndexError(String message, Throwable cause) {
        super(message, cause);
    }

    public PyIndexError(Throwable cause) {
        super(cause);
    }

    public PyIndexError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getName(){
        return "IndexError";
    }
}
