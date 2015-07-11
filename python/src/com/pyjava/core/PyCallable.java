package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;

/**
 * Created by Cristiano on 17/06/2015.
 *
 * Interfaz que define como deben ser todos los callables de objetos que son invocables y no esten implementados directamente en el __call__ del objeto.
 */
public interface PyCallable {

    /**
     * Todos los metodos deberan recibir posiblemente argumentos y argumentos por clave.
     * Ademas todos deben retornar un PyObject.
     * @param args
     * @param kwargs
     * @return
     * @throws PyException
     */
    PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException;

}
