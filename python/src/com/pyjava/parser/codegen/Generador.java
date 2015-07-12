package com.pyjava.parser.codegen;

import com.pyjava.core.PyObject;
import com.pyjava.core.runtime.Instruccion;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Cristiano on 11/07/2015.
 *
 */
public class Generador {


    public HashMap<String, Name> co_names;          //Diccionario de nombres
    public HashMap<PyObject, Const> co_consts;      //Diccionario de constantes

    public Generador(){
        co_names = new HashMap<>();
        co_consts = new HashMap<>();
    }

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

    /**
     * Crea un nuevo bloque a partir de instrucciones y otro bloque.
     * Usar null para valores que no se quieran utilizar.
     */
    public Bloque crearBloque(LinkedList<Instruccion> preInstr, Bloque bloque, LinkedList<Instruccion> postInstr){

        Bloque res = bloque;
        if(res == null){
            res = new Bloque();
        }

        if (preInstr != null){
            for(Instruccion i : preInstr){
                res.instrucciones.addFirst(i);
            }

        }

        if (postInstr != null){
            for(Instruccion i : preInstr){
                res.instrucciones.addLast(i);
            }

        }

        return res;

    }

}
