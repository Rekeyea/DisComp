package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyTypeError;
import com.pyjava.core.runtime.Estado;

/**
 * Created by Cristiano on 17/06/2015.
 *
 * Clase de funciones nativas. Esta clase debe ser creada, pero no permite realizar instanciaciones desde afuera (__new__ e __init__ tiraran error si se intenta instanciar)
 *
 * Se debera instanciar desde adentro, con su respectiva funcion nativa (funcionNativa) que sera la que realmente se llame al llamar a __call__
 *
 */
public class PyNativeFunction extends PyObject {

    public static final String __name__ = "bultin_function";

    /**
     * Es la funcion nativa real a la cual llama este callable.
     */
    public PyCallable funcionNativa = null;
    public String funcionNativaNombre = null;

    /**
     * Guarda una referencia a la clase en que fue definida esta funcion, o null si no pertenece a ninguna clase.
     * Esto es distinto a la clase actual del objeto, que va a ser __name__ ...
     */
    public PyType definidaEn = null;

    public PyNativeFunction() {
    }

    /**
     * Construye una instancia con su respectiva funcion nativa, y asigna ademas su respectiva clase.
     */
    public PyNativeFunction(String nombre, PyType definidaEn, PyCallable funcionNativa){
        this.funcionNativa = funcionNativa;
        this.funcionNativaNombre = nombre;
        this.definidaEn = definidaEn;
    }

    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {
        throw new PyTypeError(String.format("No se puede crear instancias de '%s'",__name__));
    }

    @Override
    public PyObject __call__(PyObject[] args, AttrDict kwargs, Estado estado) throws PyException {
        return this.funcionNativa.invoke(args,kwargs);
    }

    @Override
    public PyString __repr__() throws PyException{
        if(definidaEn != null) {
            return new PyString(String.format("<%s '%s' definida en '%s'>", __name__, funcionNativaNombre, definidaEn.getClassName()));
        }
        else{
            return new PyString(String.format("<%s '%s'>", __name__, funcionNativaNombre));
        }
    }

    //__str__ se delega a object


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
