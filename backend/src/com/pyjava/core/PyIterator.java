package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyStopIteration;
import com.pyjava.core.exceptions.PyTypeError;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Cristiano on 03/07/2015.
 */
public class PyIterator extends PyObject {

    public static final String __name__ = "iterator";

    //Objeto del cual se genero este iterador
    public PyObject iteradorDe;
    public Iterator<PyObject> iterador;

    public PyIterator() {

    }

    public PyIterator(PyObject iteradorDe, Iterator<PyObject> iterador){
        this.iteradorDe = iteradorDe;
        this.iterador = iterador;
    }

    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {
        throw new PyTypeError(String.format("No se puede crear instancias de '%s'",__name__));
    }


    @Override
    public PyString __repr__() throws PyException {
        return new PyString(String.format("<%s de '%s'>", __name__, iteradorDe.getType().getClassName()));
    }

    @Override
    public PyObject __next__() throws PyException {
        try{
            return iterador.next();
        }
        catch (NoSuchElementException e){
            throw new PyStopIteration();
        }

    }



    public static class Builtins{
        private static AttrDict builtins = null;

        public static AttrDict getBuiltins() {

            if (builtins == null){
                builtins = new AttrDict();
            }
            return builtins;

        }

    }

}
