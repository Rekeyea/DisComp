package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyTypeError;

/**
 * Created by Cristiano on 17/06/2015.
 */
public class PyNone extends PyObject {

    public static final String __name__ = "NoneType";

    public PyNone() {
    }


    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {
        throw new PyTypeError(String.format("No se puede crear instancias de '%s'",__name__));
    }

    @Override
    public PyString __repr__() throws PyException{
        return new PyString("None");
    }

    //__str__ se delega a object.

    /**
     * None siempre evalua en false
     * @return
     * @throws PyException
     */
    @Override
    public PyBool __bool__() throws PyException {
        return PySingletons.False;
    }


    //None compara true unicamente con otra instancia de None
    @Override
    public PyBool __eq__(PyObject obj) throws PyException{
        if(obj instanceof PyNone) {
            return PySingletons.True;
        }
        return PySingletons.False;
    }


    @Override
    public int hashCode(){
        return 0;
    }

    public static class Builtins {
        private static AttrDict builtins = null;

        public static AttrDict getBuiltins() {

            if (builtins == null) {
                builtins = new AttrDict();
            }
            return builtins;

        }

    }

}
