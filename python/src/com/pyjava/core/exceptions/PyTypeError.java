package com.pyjava.core.exceptions;

/**
 * Created by Cristiano on 16/06/2015.
 */
public class PyTypeError extends PyException {


    public PyTypeError() {
        super("Error de tipos.");
    }

    public PyTypeError(String message) {
        super(message);
    }

    public PyTypeError(String message, Throwable cause) {
        super(message, cause);
    }

    public PyTypeError(Throwable cause) {
        super(cause);
    }

    public PyTypeError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getName(){
        return "TypeError";
    }
}
