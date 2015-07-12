package com.pyjava.core;

import com.pyjava.core.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cristiano on 10/07/2015.
 */
public class PyDict extends PyObject {

    public static final String __name__ = "dict";
    public HashMap<PyObject, PyObject> dict = new HashMap<>();

    public PyDict(){

    }

    public PyDict(HashMap<PyObject, PyObject> d){
        this.dict = d;
    }

    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {


        if(args.length == 0){
            return new PyDict();
        }
        else if (args.length == 1){
            return args[0].__dict__();
        }
        else {
            throw new PyTypeError(String.format("constructor de '%s' debe recibir 0 o 1 argumentos, '%s' recibidos.", __name__, args.length));
        }
    }

    @Override
    public PyString __repr__() throws PyException{

        if(this.dict.size() > 0) {
            StringBuilder res = new StringBuilder("{");
            for (Map.Entry<PyObject, PyObject> cursor : this.dict.entrySet()) {

                PyObject clave = cursor.getKey();
                PyObject valor = cursor.getValue();

                String k = "";
                String v = "";

                if(clave instanceof PyDict){
                    k = "{...}";
                }
                else if(clave instanceof PyTuple) {
                    k = "(...)";
                }
                else if(clave instanceof PyList) {
                    k = "(...)";
                }
                else {
                    k = clave.__repr__().value;
                }


                if(valor instanceof PyDict){
                    v = "{...}";
                }
                else if(valor instanceof PyTuple) {
                    v = "(...)";
                }
                else if(valor instanceof PyList) {
                    v = "[...]";
                }
                else {
                    v = valor.__repr__().value;
                }

                res.append(String.format("%s:%s, ",k, v));
            }

            res.deleteCharAt(res.length() - 1);
            res.deleteCharAt(res.length() - 1);

            res.append("}");
            return new PyString(res.toString());
        }
        else {
            return new PyString("{}");
        }

    }


    @Override
    public PyBool __bool__() throws PyException{
        return this.dict.size() > 0 ? PySingletons.True : PySingletons.False;
    }

    @Override
    public PyObject __list__() throws PyException{
        //al igual que python devuelve una lista de las claves

        return new PyList(new ArrayList<PyObject>(this.dict.keySet()));
    }

    @Override
    public PyObject __tuple__() throws PyException{
        return new PyTuple(new ArrayList<PyObject>(this.dict.keySet()));
    }

    @Override
    public PyObject __dict__() throws PyException{
        //Al igual que python, devuelve un dict nuevo

        return new PyDict(new HashMap<PyObject, PyObject>(this.dict));
    }

    @Override
    public PyObject __iter__() throws PyException{
        //como en python el iterador es por claves
        return new PyIterator(this, this.dict.keySet().iterator());
    }

    @Override
    public PyObject[] __unpack__(int c) throws PyException{
        if(dict.size() < c){
            throw new PyValueError("No hay suficientes valores para iterar.");
        }
        if(dict.size() > c){
            throw new PyValueError("Hay demasiados valores para iterar.");
        }
        PyObject[] res = new PyObject[c];
        int i = 0;
        for(PyObject v : dict.values()){
            res[i] = v;
            i++;
        }
        return res;
    }

    @Override
    public boolean __hasheable__(){
        return false;
    }

    @Override
    public PyObject __get_index__(PyObject i) throws PyException{

        if (!i.__hasheable__()) {
            throw new PyTypeError(String.format("'%s' no es hasheable y no es clave valida", i.getType().getClassName()));
        }

        PyObject res = this.dict.get(i);
        if (res == null){
            throw new PyKeyError(String.format("Clave no encontrada: %s", i.__repr__().value));
        }
        return res;
    }

    @Override
    public PyObject __set_index__(PyObject i, PyObject v) throws PyException{

        if (!i.__hasheable__()) {
            throw new PyTypeError(String.format("'%s' no es hasheable y no es clave valida", i.getType().getClassName()));
        }

        return this.dict.put(i, v);

    }



    public static class Builtins {
        private static AttrDict builtins = null;

        public static AttrDict getBuiltins() {

            if (builtins == null) {
                builtins = new AttrDict();

                PyType clase = PySingletons.types.get(__name__);

            }
            return builtins;

        }

    }
}
