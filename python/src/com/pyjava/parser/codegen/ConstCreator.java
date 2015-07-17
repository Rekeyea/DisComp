package com.pyjava.parser.codegen;

import com.pyjava.core.*;

/**
 * Created by Cristiano on 11/07/2015.
 */
public class ConstCreator {

    public static PyObject createPyInt(String s){
        try{
            return new PyInteger(Integer.parseInt(s));
        }
        //Si falla parsear int, trato de retornar long.
        catch (NumberFormatException e){
            try{
                return new PyLong(Long.parseLong(s));
            }
            catch (NumberFormatException e2){
                throw new NumberFormatException(String.format("Valor '%s' es demasiado grande para convertir a long.", s));
            }
        }
    }

    public static PyObject createPyLong(String s){

        try {
            return new PyLong(Long.parseLong(s.substring(0, s.length() - 1)));
        }
        catch (NumberFormatException e2) {
            throw new NumberFormatException(String.format("Valor '%s' es demasiado grande para convertir a long.", s));
        }
    }

    public static PyObject createPyFloat(String s){
        try {
            return new PyFloat(Double.parseDouble(s));
        }
        catch (NumberFormatException e2) {
            throw new NumberFormatException(String.format("Valor '%s' es demasiado grande para convertir a float.", s));
        }
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
