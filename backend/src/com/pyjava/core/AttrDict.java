package com.pyjava.core;

import com.pyjava.core.exceptions.PyAttributeError;
import com.pyjava.core.exceptions.PyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Cristiano on 17/06/2015.
 *
 * Clase encargada de almacenar el diccionario de atributos de un objeto.
 * La clave del atributo debe ser un string, y el valor un PyObject.
 *
 */
public class AttrDict {

    public HashMap<String, PyObject> attrs = null;


    public AttrDict(){
        this.attrs = new HashMap<String, PyObject>();
    }

    /**
     * Retorna el atributo o null si no existe
     * @param key
     * @return
     * @throws PyException
     */
    public PyObject get(String key){
        return attrs.get(key);
    }

    /**
     * Agrega un nuevo atributo, o reemplaza a alguno existente.
     * @param key
     * @param obj
     */
    public void put(String key, PyObject obj){

        attrs.put(key,obj);
    }


    /**
     * Agrega todos los atributos de dict en este diccionario de atributos.
     */
    public void append(AttrDict dict){

        this.attrs.putAll(dict.attrs);
    }

    /**
     * Devuelve la cantidad de elementos del dict de atributos
     * @return
     */
    public int getCount(){
        return this.attrs.size();
    }

}
