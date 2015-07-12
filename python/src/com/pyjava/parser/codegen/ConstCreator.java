package com.pyjava.parser.codegen;

import com.pyjava.core.*;

/**
 * Created by Cristiano on 11/07/2015.
 */
public class ConstCreator {

    public static PyObject createPyInt(String s){
        return new PyInteger(Integer.parseInt(s));
    }

    public static PyObject createPyLong(String s){

        return new PyLong(Long.parseLong(s.substring(0,s.length()-1)));
    }

    public static PyObject createPyFloat(String s){
        return new PyFloat(Double.parseDouble(s));
    }

    public static PyObject createPyString(String s){
        return new PyString(s);
    }

    public static PyObject createPyNone(){
        return PySingletons.None;
    }

    public static PyObject createPyTrue(){
        return PySingletons.True;
    }

    public static PyObject createPyFalse(){
        return PySingletons.False;
    }


}
