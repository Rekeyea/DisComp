package com.pyjava.parser.codegen;

import com.pyjava.core.runtime.Instruccion;

import java.util.LinkedList;

/**
 * Created by Cristiano on 11/07/2015.
 */
public class Bloque {

    public LinkedList<Instruccion> instrucciones;       //Listas de instrucciones de este bloque

    public Bloque(){
        instrucciones = new LinkedList<>();
    }

}
