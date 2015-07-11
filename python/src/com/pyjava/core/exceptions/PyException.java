package com.pyjava.core.exceptions;

/**
 * Created by Cristiano on 16/06/2015.
 */

/***
 * Clase base de todas las excepciones que pueda lanzar el interprete.
 * Como no vamos a manejar try catch de python, no implementamos nada particular para integrar con el lenguaje.
 */
public class PyException extends Throwable {


    public PyException() {
    }

    public PyException(String message) {
        super(message);
    }

    public PyException(String message, Throwable cause) {
        super(message, cause);
    }

    public PyException(Throwable cause) {
        super(cause);
    }

    public PyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Devuelve el nombre de la excepcion, se debera sobreescribir en clases hijas.
     * @return
     */
    public String getName(){
        return "Exception";
    }

    @Override
    public String getMessage(){
        return String.format("%s: %s", this.getName(), super.getMessage());
    }
}
