package com.pyjava.core.runtime;

/**
 * Created by Cristiano on 28/06/2015.
 * Objetos que almacenan una instruccion a ejecutar, como su argumento y linea a la que corresponde.
 */
public class Instruccion {

    public int linea;           //Linea a la que corresponde esta instruccion, usualmente varias instrucciones van a corresponder a una misma linea. De 1 a N
    public int instruccion;     //El valor es el definido en OpCode.java
    public int arg;             //El valor es un int que va a depender de la instruccion
    public int arg2;            //Argumento extra para ciertas instrucciones

    public Instruccion(int linea, int instruccion, int arg){
        this.linea = linea;
        this.instruccion = instruccion;
        this.arg = arg;
        this.arg2 = 0;
    }

    public Instruccion(int linea, int instruccion, int arg, int arg2){
        this.linea = linea;
        this.instruccion = instruccion;
        this.arg = arg;
        this.arg2 = arg2;

    }

}
