package com.pyjava.core.runtime;

import com.pyjava.core.PyObject;

import java.util.ArrayList;

/**
 * Created by Cristiano on 28/06/2015.
 *
 * Almacena informacion de codigo a ejecutar, ya sea de modulo o de funcion definida por usuario.
 */
public class Code {

    /**
     * Nombre de funcion/modulo
     */
    public String co_name;

    /**
     * Nombre del archivo donde se encuentra el codigo
     */
    public String co_filename;

    /**
     * Nombres de parametros y variables locales. Los primeros elementos deben ser los argumentos.
     */
    public ArrayList<String> co_varnames = new ArrayList<String>();

    /**
     * Cantidad de argumentos
     */
    public int co_argcount = 0;

    /**
     * Nombres utilizados en el codigo. No incluye aquellos en co_varnames.
     * Incluye atributos, metodos, variables globales, etc.
     * En el caso especial de que este objeto de codigo corresponda a un modulo, todos los nombres estaran en este objeto, y no en co_varnames.
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
