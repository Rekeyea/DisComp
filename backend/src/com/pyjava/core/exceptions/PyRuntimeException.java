package com.pyjava.core.exceptions;

/**
 * Created by Cristiano on 18/06/2015.
 */
public class PyRuntimeException extends PyException {

    public PyRuntimeException() {
        super("Error de ejecucion.");
    }

    public PyRuntimeException(String message) {
        super(message);
    }

    public PyRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PyRuntimeException(Throwable cause) {
        super(cause);
    }

    public PyRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getName(){
        return "RuntimeException";
    }
}
