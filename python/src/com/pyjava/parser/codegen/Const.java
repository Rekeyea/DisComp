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


    @Override
    public boolean equals(Object o){
        if( o instanceof Const){
            Const k = (Const) o;
            return this.value.equals(k.value) && this.value.getType() == k.value.getType();     //ademas de comparar valores, comparo tipo, puedo comparar la instancia ya que mismos objetos apuntan a la misma clase
        }
        return false;
    };

    @Override
    public int hashCode(){
        return this.value.hashCode() + this.value.getType().hashCode();
    }

}
