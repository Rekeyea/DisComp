package com.pyjava.core;

import com.pyjava.core.exceptions.*;

import java.util.ArrayList;

/**
 * Created by Cristiano on 09/07/2015.
 */
public class PySlice extends PyObject {

    public static final String __name__ = "slice";


    public Integer start = null;
    public Integer end = null;
    public Integer step = null;


    public PySlice() {
    }

    /**
     * Recibe PyObjects para consturir el slice. Se va a tratar de setear en un int, o null si __is_none__ evalua en True
     * @param start
     * @param end
     * @param step
     */
    public PySlice(PyObject start, PyObject end, PyObject step) throws PyException{
        this.start = start.__is_none__() ? null : start.__getint__();
        this.end = end.__is_none__() ? null : end.__getint__();
        this.step = step.__is_none__() ? null : step.__getint__();
    }



    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {
        //Si bien python permite instanciar slices como llamada a funcion, no lo vamos a permitir.
        throw new PyTypeError(String.format("No se pueden crear instancias de '%s'", __name__));
    }



    @Override
    public boolean __hasheable__(){
        return false;
    }


    @Override
    public PyString __repr__() throws PyException{

        return new PyString(String.format("<slice [%s:%s:%s]>",start != null ? start.toString() : "", end != null ? end.toString() : "", step != null ? step.toString() : ""));


    }

    public static class Builtins{

        private static AttrDict builtins = null; //Sera singleton, para no instanciar builtins de esta clase muchas veces

        public static AttrDict getBuiltins(){

            if(builtins == null) {

                builtins = new AttrDict();

                PyType clase = PySingletons.types.get(__name__);


            }

            return builtins;


        }
    }

}
