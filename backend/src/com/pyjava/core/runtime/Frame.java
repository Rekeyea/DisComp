package com.pyjava.core.runtime;

import com.pyjava.core.PyObject;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Cristiano on 28/06/2015.
 * Almacena informacion de un frame (stack frame)
 */
public class Frame {

    /**
     * Puntero al frame anterior. O null si no hay (es el modulo principal)
     */
    public Frame f_back = null;

    /**
     * Stack de variables del frame
     */
    public Stack<PyObject> f_stack = new Stack<PyObject>();

    /**
     * Variables globales del frame
     */
    public HashMap<String, PyObject> f_globals = new HashMap<String, PyObject>();

    /**
     * Variables locales del frame
     */
    public HashMap<String, PyObject> f_locals = new HashMap<String, PyObject>();

    /**
     * Proxima instruccion a ejecutar. De 0 a N
     */
    public int f_instr = 0;

    /**
     * Codigo a ejecutar
     */
    public Code f_code;


}
