package com.pyjava.core.exceptions;

/**
 * Created by Cristiano on 17/06/2015.
 */
public class PyAttributeError extends PyException {

    public PyAttributeError() {
        super("Error de atributo.");
    }

    public PyAttributeError(String message) {
        super(message);
    }

    public PyAttributeError(String message, Throwable cause) {
        super(message, cause);
    }

    public PyAttributeError(Throwable cause) {
        super(cause);
    }

    public PyAttributeError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getName(){
        return "Attribute Error";
    }
}
