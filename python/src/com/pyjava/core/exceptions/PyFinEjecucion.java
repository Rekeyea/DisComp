package com.pyjava.core.exceptions;

/**
 * Created by Cristiano on 18/06/2015.
 * Excepcion para indicar el fin de ejecucion, no es realmente un error.
 */
public class PyFinEjecucion extends PyException {

    public PyFinEjecucion() {
        super("Fin de ejecucion.");
    }

    public PyFinEjecucion(String message) {
        super(message);
    }

    public PyFinEjecucion(String message, Throwable cause) {
        super(message, cause);
    }

    public PyFinEjecucion(Throwable cause) {
        super(cause);
    }

    public PyFinEjecucion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getName(){
        return "Fin de ejecucion";
    }
}
