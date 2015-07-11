package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyTypeError;
import com.pyjava.core.runtime.Estado;

/**
 * Created by Cristiano on 18/06/2015.
 *
 * Esta clase encapsula una funcion, de forma de llamarla siempre incluyendo a "si misma", o sea, al "self".
 * Necesaria para llamar metodos desde una instancia y pasar el self automaticamente.
 */
public class PyMethodWrapper extends PyObject{

    public static final String __name__ = "method_wrapper";

    public PyObject self = null;
    public PyObject wrappedFunc = null;

    public PyMethodWrapper() {
    }

    public PyMethodWrapper(PyObject self, PyObject wrappedFunc) {
        this.self = self;
        this.wrappedFunc = wrappedFunc;

    }

    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {
        throw new PyTypeError(String.format("No se puede crear instancias de '%s'",__name__));
    }


    @Override
    public PyObject __call__(PyObject[] args, AttrDict kwargs, Estado estado) throws PyException {
        if(self == null || wrappedFunc == null){
            throw new PyTypeError(String.format("Error al llamar metodo de instancia, no se le asigno ninguna instancia a self o wrappedFunc."));
        }
        PyObject[] newArgs = new PyObject[args.length + 1];
        newArgs[0] = self;
        System.arraycopy(args, 0, newArgs, 1, args.length);

        return wrappedFunc.__call__(newArgs, kwargs, estado);

    }

    @Override
    public PyString __repr__() throws PyException{
        return new PyString(String.format("<%s de '%s' para instancia de '%s'>", __name__, wrappedFunc.__repr__().value, self.getType().getClassName()));
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
