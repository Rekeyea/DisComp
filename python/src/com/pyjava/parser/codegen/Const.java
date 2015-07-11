package com.pyjava.parser.codegen;

import com.pyjava.core.PyObject;

/**
 * Created by Cristiano on 11/07/2015.
 */
public class Const {

    public int index;       //Indice en la tabla de constantes
    public PyObject value;  //Valor de la constante

    public Const(int index, PyObject value){
        this.index = index;
        this.value = value;
    }

}
