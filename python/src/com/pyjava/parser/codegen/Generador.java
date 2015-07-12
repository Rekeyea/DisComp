package com.pyjava.parser.codegen;

import com.pyjava.core.PyObject;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Cristiano on 11/07/2015.
 *
 */
public class Generador {


    public HashMap<String, Name> co_names;          //Diccionario de nombres
    public HashMap<PyObject, Const> co_consts;      //Diccionario de constantes


    /**
     * Crea y retorna una nueva constante
     */
    public Const createConst(PyObject c){

        Const res = co_consts.get(c);
        if(res == null){
            res = new Const(co_consts.size()+1,c);
        }

        return res;
    }

    /**
     * Crea un nuevo nombre
     */
    public Name createName(String n){

        Name res = co_names.get(n);
        if(res == null){
            res = new Name(co_names.size()+1,n);
        }

        return res;

    }

}
