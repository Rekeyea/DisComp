package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyTypeError;
import com.pyjava.core.exceptions.PyValueError;

/**
 * Created by Cristiano on 17/06/2015.
 */
public class PyBool extends PyObject {

    public static final String __name__ = "bool";

    public boolean value = false;

    public PyBool() {
    }

    public PyBool(boolean value) {
        this.value = value;
    }

    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {

        if(kwargs.getCount() > 0){
            throw new PyTypeError(String.format("constructor de '%s' no debe recibir ningun argumento por nombre.", __name__));
        }

        //Si no me pasaron argumentos
        if(args.length == 0){
            return PySingletons.False;
        }
        else if (args.length == 1){
            // se delega a la funcion __int__
            return args[0].__bool__();
        }
        else {
            throw new PyTypeError(String.format("constructor de '%s' debe recibir 0 o 1 argumentos, '%s' recibidos.", __name__, args.length));
        }
    }

    @Override
    public PyString __repr__() throws PyException{
        if(this.value) return new PyString("True");
        else return new PyString("False");
    }

    //__str__ se delega a object.

    @Override
    public PyObject __int__() throws PyException {
        if(this.value){
            return new PyInteger(1);
        }
        else {
            return new PyInteger(0);
        }
    }

    @Override
    public int __getint__() throws PyException{
        return this.value ? 1 : 0;
    }


    @Override
    public PyObject __long__() throws PyException {
        if(this.value){
            return new PyLong(1L);
        }
        else {
            return new PyLong(0L);
        }
    }

    @Override
    public PyObject __float__() throws PyException{
        if(this.value){
            return new PyFloat(1.0);
        }
        else {
            return new PyFloat(0.0);
        }
    }

    @Override
    public PyBool __bool__() throws PyException{
        return this;
    }


    public PyObject __add__(PyObject obj) throws PyException{
        if(obj instanceof PyInteger){
            return AritmeticaHelper.__add__int_bool((PyInteger) obj, this);
        }

        if(obj instanceof PyLong){
            return AritmeticaHelper.__add__long_bool((PyLong)obj, this);
        }

        if(obj instanceof PyFloat){
            return AritmeticaHelper.__add__float_bool((PyFloat) obj, this);
        }

        if(obj instanceof PyBool){
            return AritmeticaHelper.__add__bool_bool(this, (PyBool) obj);
        }

        throw AritmeticaHelper.getErrorBinary("+", this, obj);
    }

    @Override
    public PyObject __neg__() throws PyException{
        return new PyInteger(this.value ? -1 : 0);
    }


    @Override
    public PyObject __mul__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            return AritmeticaHelper.__mul__int_bool((PyInteger) obj, this);
        }

        if(obj instanceof PyLong){
            return AritmeticaHelper.__mul__long_bool((PyLong) obj, this);
        }

        if(obj instanceof PyFloat){
            return AritmeticaHelper.__mul__float_bool((PyFloat) obj, this);
        }

        if(obj instanceof PyBool){
            return AritmeticaHelper.__mul__bool_bool(this, (PyBool) obj);
        }

        throw AritmeticaHelper.getErrorBinary("*", this, obj);

    }

    @Override
    public PyObject __pow__(PyObject obj) throws PyException{

        //Primero valido tipos, aunque los resultados van a ser siempre dos. El tipo de resultado va a depender del objeto.

        if(obj instanceof PyInteger){
            if(this.value){
                return new PyInteger(1);
            }
            else {
                return new PyInteger(0);
            }
        }

        if(obj instanceof PyLong){
            if(this.value){
                return new PyLong(1L);
            }
            else {
                return new PyLong(0L);
            }
        }

        if(obj instanceof PyFloat){
            if(this.value){
                return new PyFloat(1.0);
            }
            else {
                return new PyFloat(0.0);
            }
        }

        if(obj instanceof PyBool){
            if(this.value){
                return new PyInteger(1);
            }
            else {
                return new PyInteger(0);
            }
        }

        throw AritmeticaHelper.getErrorBinary("**", this, obj);
    }


    @Override
    public PyObject __div__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            int div = ((PyInteger)obj).value;
            if(div == 0){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyInteger(this.value ? 1 : 0);
        }


        if(obj instanceof PyLong){
            long div = ((PyLong)obj).value;
            if(div == 0L){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyLong(this.value ? 1 : 0);

        }

        if(obj instanceof PyFloat){
            double div = ((PyFloat)obj).value;
            if(div == 0.0){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyFloat(this.value ? 1 : 0);
        }

        if(obj instanceof PyBool){
            if(!((PyBool)obj).value){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyInteger(this.value ? 1 : 0);

        }

        throw AritmeticaHelper.getErrorBinary("/", this, obj);
    }


    @Override
    public PyObject __mod__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            int div = ((PyInteger)obj).value;
            if(div == 0){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyInteger( (this.value ? 1 : 0) % div );
        }


        if(obj instanceof PyLong){
            long div = ((PyLong)obj).value;
            if(div == 0L){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyLong((this.value ? 1 : 0) % div);

        }

        if(obj instanceof PyFloat){
            double div = ((PyFloat)obj).value;
            if(div == 0.0){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyFloat((this.value ? 1 : 0) % div);
        }

        if(obj instanceof PyBool){
            if(!((PyBool)obj).value){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyInteger(0);

        }

        throw AritmeticaHelper.getErrorBinary("%", this, obj);
    }

    @Override
    public PyObject __band__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            return AritmeticaHelper.__band__int_bool((PyInteger) obj, this);
        }

        if(obj instanceof PyLong){
            return AritmeticaHelper.__band__long_bool((PyLong) obj, this);
        }

        if(obj instanceof PyBool){
            return AritmeticaHelper.__band__bool_bool(this, (PyBool) obj);
        }

        throw AritmeticaHelper.getErrorBinary("&", this, obj);
    }

    //Estupido python el not a un bool devuelve integer....
    @Override
    public PyObject __bnot__() throws PyException{
        return new PyInteger(~ (this.value ? 1 : 0));
    }

    @Override
    public PyObject __bor__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            return AritmeticaHelper.__bor__int_bool((PyInteger) obj, this);
        }

        if(obj instanceof PyLong){
            return AritmeticaHelper.__bor__long_bool((PyLong) obj, this);
        }

        if(obj instanceof PyBool){
            return AritmeticaHelper.__bor__bool_bool(this, (PyBool) obj);
        }

        throw AritmeticaHelper.getErrorBinary("|", this, obj);
    }


    @Override
    public PyObject __bxor__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            return AritmeticaHelper.__bxor__int_bool((PyInteger) obj, this);
        }

        if(obj instanceof PyLong){
            return AritmeticaHelper.__bxor__long_bool((PyLong) obj, this);
        }

        if(obj instanceof PyBool){
            return AritmeticaHelper.__bxor__bool_bool(this, (PyBool) obj);
        }

        throw AritmeticaHelper.getErrorBinary("^", this, obj);
    }

    @Override
    public PyObject __sleft__(PyObject obj) throws PyException{

        int v = this.value ? 1 : 0;

        if(obj instanceof PyInteger){
            return new PyLong(v << ((PyInteger)obj).value);
        }

        if(obj instanceof PyLong){
            return new PyLong((long)v << ((PyLong)obj).value);
        }

        if(obj instanceof PyBool){
            return new PyInteger(v << ( ((PyBool)obj).value ? 1 : 0) );
        }

        throw AritmeticaHelper.getErrorBinary("<<", this, obj);
    }

    @Override
    public PyObject __sright__(PyObject obj) throws PyException{

        int v = this.value ? 1 : 0;

        if(obj instanceof PyInteger){
            return new PyLong(v >> ((PyInteger)obj).value);
        }

        if(obj instanceof PyLong){
            return new PyLong((long)v >> ((PyLong)obj).value);
        }

        if(obj instanceof PyBool){
            return new PyInteger(v >> ( ((PyBool)obj).value ? 1 : 0) );
        }

        throw AritmeticaHelper.getErrorBinary(">>", this, obj);
    }


    @Override
    public PyBool __eq__(PyObject obj) throws PyException{

        if(obj instanceof PyBool){
            return (this.value == ((PyBool)obj).value) ? PySingletons.True : PySingletons.False;
        }

        int v = this.value ? 1 : 0;
        if( (obj instanceof PyInteger) ) {
            return v == ((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return (long)v == ((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyFloat){
            return (double)v == ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }


        return PySingletons.False;
    }

    //__not_eq__ implementado por object a partir de __eq__


    @Override
    public PyBool __gt__(PyObject obj) throws PyException{

        int v = this.value ? 1 : 0;

        if(obj instanceof PyBool){
            return (v > (((PyBool)obj).value ? 1 : 0)) ? PySingletons.True : PySingletons.False;
        }
        if( (obj instanceof PyInteger) ) {
            return v > ((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return (long)v > ((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyFloat){
            return (double)v > ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }

        throw AritmeticaHelper.getErrorBinary(">", this, obj);
    }

    @Override
    public PyBool __lt__(PyObject obj) throws PyException{

        int v = this.value ? 1 : 0;

        if(obj instanceof PyBool){
            return (v < (((PyBool)obj).value ? 1 : 0)) ? PySingletons.True : PySingletons.False;
        }
        if( (obj instanceof PyInteger) ) {
            return v < ((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return (long)v < ((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyFloat){
            return (double)v < ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }

        throw AritmeticaHelper.getErrorBinary("<", this, obj);
    }

    @Override
    public PyBool __ge__(PyObject obj) throws PyException{

        int v = this.value ? 1 : 0;

        if(obj instanceof PyBool){
            return (v >= (((PyBool)obj).value ? 1 : 0)) ? PySingletons.True : PySingletons.False;
        }
        if( (obj instanceof PyInteger) ) {
            return v >= ((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return (long)v >= ((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyFloat){
            return (double)v >= ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }

        throw AritmeticaHelper.getErrorBinary(">=", this, obj);
    }

    @Override
    public PyBool __le__(PyObject obj) throws PyException{

        int v = this.value ? 1 : 0;

        if(obj instanceof PyBool){
            return (v <= (((PyBool)obj).value ? 1 : 0)) ? PySingletons.True : PySingletons.False;
        }
        if( (obj instanceof PyInteger) ) {
            return v <= ((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return (long)v <= ((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyFloat){
            return (double)v <= ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }

        throw AritmeticaHelper.getErrorBinary("<=", this, obj);
    }

    @Override
    public int hashCode(){
        return this.value ? 1 : 0;
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
