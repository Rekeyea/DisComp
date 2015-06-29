package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyTypeError;
import com.pyjava.core.exceptions.PyZeroDivisionError;

/**
 * Created by Cristiano on 20/06/2015.
 *
 * Helper para intentar reducir la cantidad de codigo para operaciones aritmeticas con numeros.
 */
public class AritmeticaHelper {

    public static PyException getErrorBinary(String op, PyObject obj1, PyObject obj2){
        return new PyTypeError(String.format("Operador '%s' no es soportado para: '%s' y '%s'", op, obj1.getType().getClassName(), obj2.getType().getClassName()));
    }

    public static PyException getErrorUnary(String op, PyObject obj1){
        return new PyTypeError(String.format("Operador unario '%s' no es soportado para '%s'", op, obj1.getType().getClassName()));
    }

    public static PyException getZeroError(PyObject obj1, PyObject obj2){
        return new PyZeroDivisionError(String.format("Division por 0 entre '%s' y '%s'", obj1.getType().getClassName(), obj2.getType().getClassName()));
    }

    //***************************** SUMA **************************************

    /**
     * Suma entre int e int, escala a long si se sale de rango.
     */
    public static PyObject __add__int_int(PyInteger l, PyInteger obj){
        long r = (long)l.value + (long)obj.value;
        if( r > (long)Integer.MAX_VALUE || r < (long)Integer.MIN_VALUE){
            return new PyLong(r);
        }

        return new PyInteger((int)r);
    }

    public static PyObject __add__long_long(PyLong l, PyLong obj){
        return new PyLong(l.value + obj.value);
    }

    public static PyObject __add__float_float(PyFloat l, PyFloat obj){
        return new PyFloat(l.value + obj.value);
    }

    /**
     * Retorna PyInt
     */
    public static PyObject __add__bool_bool(PyBool l, PyBool obj){
        int v1 = l.value ? 1 : 0;
        int v2 = obj.value ? 1 : 0;

        return new PyInteger(v1 + v2);
    }

    /**
     * Suma entre long e int, retorna siempre long.
     */
    public static PyObject __add__long_int(PyLong l, PyInteger obj) {
        return new PyLong(l.value + (long)obj.value);
    }

    /**
     * Siempre retorna float
     */
    public static PyObject __add__float_int(PyFloat l, PyInteger obj) {
        return new PyFloat(l.value + (double)obj.value);
    }

    /**
     * Siempre retorna float
     */
    public static PyObject __add__float_long(PyFloat l, PyLong obj) {
        return new PyFloat(l.value + (double)obj.value);
    }

    /**
     * Retorna int o long, dependiendo de si pasa o no los limites
     */
    public static PyObject __add__int_bool(PyInteger l, PyBool obj) {

        long r = (long)l.value;
        if (obj.value){
            r+=1;
        }

        if( r > (long)Integer.MAX_VALUE || r < (long)Integer.MIN_VALUE){
            return new PyLong(r);
        }
        return new PyInteger((int)r);

    }


    public static PyObject __add__long_bool(PyLong l, PyBool obj) {
        long r = l.value;
        if (obj.value){
            r+=1;
        }

        return new PyLong(r);
    }

    public static PyObject __add__float_bool(PyFloat l, PyBool obj) {
        double r = l.value;
        if (obj.value){
            r+=1;
        }

        return new PyFloat(r);
    }


    //***************************************** multiplicacion **************************************

    public static PyObject __mul__int_int(PyInteger l, PyInteger obj){
        long r = (long)l.value * (long)obj.value;
        if( r > (long)Integer.MAX_VALUE || r < (long)Integer.MIN_VALUE){
            return new PyLong(r);
        }

        return new PyInteger((int)r);

    }

    public static PyObject __mul__long_long(PyLong l, PyLong obj){
        return new PyLong(l.value * obj.value);
    }

    public static PyObject __mul__float_float(PyFloat l, PyFloat obj){
        return new PyFloat(l.value * obj.value);

    }

    public static PyObject __mul__bool_bool(PyBool l, PyBool obj){
        int a = l.value ? 1 : 0;
        int b = obj.value ? 1 : 0;
        return new PyInteger(a*b);
    }

    public static PyObject __mul__long_int(PyLong l, PyInteger obj){
        return new PyLong(l.value * (long)obj.value);
    }

    public static PyObject __mul__float_int(PyFloat l, PyInteger obj){
        return new PyFloat(l.value * (double)obj.value);
    }

    public static PyObject __mul__float_long(PyFloat l, PyLong obj){
        return new PyFloat(l.value * (double)obj.value);
    }

    public static PyObject __mul__int_bool(PyInteger l, PyBool obj){
        int v = obj.value ? 1 : 0;
        return new PyInteger(l.value * v);

    }

    public static PyObject __mul__long_bool(PyLong l, PyBool obj){
        long v = obj.value ? 1L : 0L;
        return new PyLong(l.value * v);
    }

    public static PyObject __mul__float_bool(PyFloat l, PyBool obj){
        double v = obj.value ? 1.0 : 0.0;
        return new PyFloat(l.value * v);

    }

    //***************************************** potencia ********************************************

    //No se implementa ninguna aca porque la potencia no es conmutativa asi que no ahorro nada de codigo.


    //***************************************** division ********************************************

    //idem a lo anterior

    //***************************************** modulo **********************************************

    //idem a lo anterior

    //***************************************** binarios ********************************************


    //*** and

    public static PyObject __band__int_int(PyInteger l, PyInteger obj){
        return new PyInteger(l.value & obj.value);
    }

    public static PyObject __band__long_long(PyLong l, PyLong obj){
        return new PyLong(l.value & obj.value);
    }

    /**
     * Bool con Bool, retorna Bool.
     */
    public static PyObject __band__bool_bool(PyBool l, PyBool obj){
        boolean res = l.value & obj.value;
        if(res){
            return PySingletons.True;
        }
        else{
            return PySingletons.False;
        }
    }

    public static PyObject __band__int_bool(PyInteger l, PyBool obj){
        return new PyLong(l.value & (obj.value ? 1 : 0));
    }

    public static PyObject __band__long_int(PyLong l, PyInteger obj){
        return new PyLong(l.value & (long)obj.value);
    }

    public static PyObject __band__long_bool(PyLong l, PyBool obj){
        return new PyLong(l.value & (obj.value ? 1L : 0L));
    }

    //*** or

    public static PyObject __bor__int_int(PyInteger l, PyInteger obj){
        return new PyInteger(l.value | obj.value);
    }

    public static PyObject __bor__long_long(PyLong l, PyLong obj){
        return new PyLong(l.value | obj.value);
    }

    /**
     * Bool con Bool, retorna Bool.
     */
    public static PyObject __bor__bool_bool(PyBool l, PyBool obj){
        boolean res = l.value | obj.value;
        if(res){
            return PySingletons.True;
        }
        else{
            return PySingletons.False;
        }
    }

    public static PyObject __bor__int_bool(PyInteger l, PyBool obj){
        return new PyLong(l.value | (obj.value ? 1 : 0));
    }

    public static PyObject __bor__long_int(PyLong l, PyInteger obj){
        return new PyLong(l.value | (long)obj.value);
    }

    public static PyObject __bor__long_bool(PyLong l, PyBool obj){
        return new PyLong(l.value | (obj.value ? 1L : 0L));
    }


    //*** xor

    public static PyObject __bxor__int_int(PyInteger l, PyInteger obj){
        return new PyInteger(l.value ^ obj.value);
    }

    public static PyObject __bxor__long_long(PyLong l, PyLong obj){
        return new PyLong(l.value ^ obj.value);
    }

    /**
     * Bool con Bool, retorna Bool.
     */
    public static PyObject __bxor__bool_bool(PyBool l, PyBool obj){
        boolean res = l.value ^ obj.value;
        if(res){
            return PySingletons.True;
        }
        else{
            return PySingletons.False;
        }
    }

    public static PyObject __bxor__int_bool(PyInteger l, PyBool obj){
        return new PyLong(l.value ^ (obj.value ? 1 : 0));
    }

    public static PyObject __bxor__long_int(PyLong l, PyInteger obj){
        return new PyLong(l.value ^ (long)obj.value);
    }

    public static PyObject __bxor__long_bool(PyLong l, PyBool obj){
        return new PyLong(l.value ^ (obj.value ? 1L : 0L));
    }

}
