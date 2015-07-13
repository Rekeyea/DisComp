package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyTypeError;
import com.pyjava.core.runtime.Code;
import com.pyjava.core.runtime.Estado;
import com.pyjava.core.runtime.Frame;

/**
 * Created by Cristiano on 13/07/2015.
 * Funciones definidas por usuario
 */
public class PyUserFunction  extends PyObject{

    public static final String __name__ = "userfunction";

    public Code func_code = null;

    public PyUserFunction(Code code){
        this.func_code = code;
    }

    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {
        throw new PyTypeError(String.format("No se pueden crear instancias de '%s'", __name__));
    }

    @Override
    public PyString __repr__() throws PyException{
        return new PyString(String.format("<%s '%s'>", __name__, func_code.co_name));
    }

    @Override
    public PyObject __call__(PyObject[] args, AttrDict kwargs, Estado estado) throws PyException {
        //aca se pone lindo... Tiene que ejecutar y manipular el estado para cambiar de frame y demas.

        Frame newFrame = new Frame();
        newFrame.f_back = estado.frameActual;
        newFrame.f_instr = 0;
        newFrame.f_code = this.func_code;

        //Valido/asigno argumentos en variables locales
        int argsEncontrados = args.length+ kwargs.getCount();
        if(argsEncontrados != func_code.co_arguments.size()){
            throw new PyTypeError(String.format("'%s' necesita %s argumentos, se encontraron %s",func_code.co_name, func_code.co_arguments.size(), argsEncontrados));
        }


        //Agrego primero argumentos posicionales, que deben corresponderse con co_arguments
        int i = 0;
        for(; i < args.length; i++){
            newFrame.f_locals.put(func_code.co_arguments.get(i), args[i]);
        }


        //Agrego los kwargs, debo validar que el nombre corresponda con nombres de variables y ademas no sea de variables ya pasadas.
        //Al igual que python, los argumentos por nombre deberan venir luego de todos los posicionales,
        //Por lo que solo busco dentro de la lista de argumentos, aquellos > i
        for(; i < func_code.co_arguments.size(); i++){
            String argName = func_code.co_arguments.get(i);
            PyObject argument = kwargs.get(argName);
            if(argument == null){
                throw new PyTypeError(String.format("Argumento no encontrado: '%s'", argName));
            }
            newFrame.f_locals.put(argName, argument);
        }


        //Como la implementacion de python no maneja modulos, las variables globales de la funcion,
        //van a ser las variables globales del frame actual.
        //Por otro lado, la creacion del modulo principal debe setear f_globals = f_locals
        newFrame.f_globals = estado.frameActual.f_globals;



        //Cambio el frame
        estado.frameActual = newFrame;

        //Y retornar null para indicar que no retorna nada y que en vez deja el resultado en el stack.
        return  null;
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
