package com.pyjava.core.runtime;

import com.pyjava.core.AttrDict;
import com.pyjava.core.PyObject;
import com.pyjava.core.PySingletons;
import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyTypeError;

import java.util.ArrayList;

/**
 * Created by Cristiano on 28/06/2015.
 *
 * Almacena informacion de codigo a ejecutar, ya sea de modulo o de funcion definida por usuario.
 *
 * Necesito que sea PyObject para poder utilizarlo en el stack u otras estructuras.
 */
public class Code extends PyObject{

    public static final String __name__ = "code";

    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {
        throw new PyTypeError(String.format("No se pueden crear instancias de '%s'", __name__));
    }

    /**
     * Solo para que pueda ser un pyobject
     */
    public static class Builtins{
        private static AttrDict builtins = null;
        public static AttrDict getBuiltins() {
            if (builtins == null){
                builtins = new AttrDict();
            }
            return builtins;

        }

    }


    /**
     * Nombre de funcion/modulo
     */
    public String co_name;

    /**
     * Nombre del archivo donde se encuentra el codigo
     */
    public String co_filename;

    /**
     * Nombres de argumentos, en el orden en que fueron definidos. Deberan estar tambien en co_names para poder ser accedidos.
     */
    public ArrayList<String> co_arguments = new ArrayList<String>();

    /**
     * Nombres utilizados en el codigo.
     * Incluye atributos, metodos, variables globales, etc.
     */
    public ArrayList<String> co_names = new ArrayList<String>();

    /**
     * Constantes definidas en el codigo.
     */
    public ArrayList<PyObject> co_consts = new ArrayList<PyObject>();

    /**
     * Instrucciones de este codigo. El indice de la lista es utilizado como numero de instruccion.
     */
    public ArrayList<Instruccion> co_code = new ArrayList<Instruccion>();


    /**
     * Constructor, los demas argumentos deben ser asignados luego de construir el objeto.
     */
    public Code(String name, String filename){
        this.co_name = name;
        this.co_filename = filename;
    }

}
