package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Cristiano on 17/06/2015.
 *
 * Clase que mantiene referencias a todos los singletons del entorno.
 * Es fundamental llamar a esta clase antes de realizar cualquier operacion.
 */

public class PySingletons {

    //INSTANCIA FUNDAMENTAL DE TYPE.
    public static PyType type = null;

    //Todos los tipos creados.
    public static ConcurrentHashMap<String, PyType> types = null;


    //INSTANCIAS DE BUILTIN TYPES.
    public static PyType object = null;
    public static PyType string = null;
    public static PyType integer = null;
    public static PyType longint = null;
    public static PyType pfloat = null;
    public static PyType bool = null;
    public static PyType noneType = null;

    public static PyType builtinFunc = null;
    public static PyType methodWrapper = null;

    //INSTANCIAS DE SINGLETONS DE BUILTIN TYPES Y HELPERS.
    public static PyObject[] argsVacios = null;
    public static AttrDict kwargsVacios = null;  //TODO: reemplazar por PyDict
    public static PyString strVacio = null;

    public static PyBool True = null;
    public static PyBool False = null;
    public static PyNone None = null;

    private static boolean inicializado = false;

    public static void init() {

        if(inicializado){
            throw new RuntimeException("ERROR FATAL: Singletons ya fueron inicializados.");
        }

        try {

            //INICIALIZO TYPE Y TYPES
            types = new ConcurrentHashMap<String,PyType>();
            type = new PyType();
            type.__class__ = type;  //Caso especial, es instancia de si mismo.
            type.__dict__ = PyType.Builtins.getBuiltins();  //Caso especial, tanto la instancia como la clase de Type tienen los mismos builtins.
            type.claseAlmacenada = PyType.class;
            type.claseAlmacenadaName = PyType.__name__;
            types.put(PyType.__name__, type);


            //INSTANCIAS DE BUILTIN TYPES
            object = type.crearClase(PyObject.__name__, PyObject.class, null);
            type.__bases__ = Arrays.asList(object); //Caso especial de type, que tambien tiene a object como clase padre.

            string = type.crearClase(PyString.__name__, PyString.class, Arrays.asList(object));
            integer = type.crearClase(PyInteger.__name__, PyInteger.class, Arrays.asList(object));
            longint = type.crearClase(PyLong.__name__, PyLong.class, Arrays.asList(object));
            pfloat = type.crearClase(PyFloat.__name__, PyFloat.class, Arrays.asList(object));
            bool = type.crearClase(PyBool.__name__, PyBool.class, Arrays.asList(object));
            noneType = type.crearClase(PyNone.__name__, PyNone.class, Arrays.asList(object));

            builtinFunc = type.crearClase(PyNativeFunction.__name__, PyNativeFunction.class, Arrays.asList(object));
            methodWrapper = type.crearClase(PyMethodWrapper.__name__, PyMethodWrapper.class, Arrays.asList(object));


            //INSTANCIAS DE SINGLETONS DE BUILTIN TYPES Y HELPERS
            argsVacios = new PyObject[0];
            kwargsVacios = new AttrDict();
            strVacio = new PyString("");
            True = new PyBool(true);
            False = new PyBool(false);
            None = new PyNone();

            //BUILTINS: Cargarlos unicamente cuando todas las clases fueron instanciadas, si no podemos tener objetos que no sean instanciados correctamente.
            object.__dict__ = PyObject.Builtins.getBuiltins();
            string.__dict__ = PyString.Builtins.getBuiltins();
            integer.__dict__ = PyInteger.Builtins.getBuiltins();
            longint.__dict__ = PyLong.Builtins.getBuiltins();
            pfloat.__dict__ = PyFloat.Builtins.getBuiltins();
            bool.__dict__ = PyBool.Builtins.getBuiltins();
            noneType.__dict__ = PyNone.Builtins.getBuiltins();

            builtinFunc.__dict__ = PyNativeFunction.Builtins.getBuiltins();
            methodWrapper.__dict__ = PyMethodWrapper.Builtins.getBuiltins();


            inicializado = true;

        }
        catch (Throwable t){
            t.printStackTrace();
            throw new RuntimeException("ERROR FATAL: No se pudo inicializar singletons: " + t.getMessage());
        }
    }

    static {
        init();
    }

}
