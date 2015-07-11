package com.pyjava.core.exceptions;

/**
 * Created by Cristiano on 18/06/2015.
 */
public class PyKeyError extends PyException {

    public PyKeyError() {
        super("Clave no encontrada.");
    }

    public PyKeyError(String message) {
        super(message);
    }

    public PyKeyError(String message, Throwable cause) {
        super(message, cause);
    }

    public PyKeyError(Throwable cause) {
        super(cause);
    }

    public PyKeyError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getName(){
        return "KeyError";
    }
}
