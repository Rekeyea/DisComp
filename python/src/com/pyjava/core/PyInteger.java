package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyTypeError;
import com.pyjava.core.exceptions.PyZeroDivisionError;

/**
 * Created by Cristiano on 17/06/2015.
 */
public class PyInteger extends PyObject {

    public static final String __name__ = "int";


    public int value = 0;

    public PyInteger() {
    }

    public PyInteger(int value) {
        this.value = value;
    }

    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {

        if(kwargs.getCount() > 0){
            throw new PyTypeError(String.format("constructor de '%s' no debe recibir ningun argumento por nombre.", __name__));
        }

        //Si no me pasaron argumentos, retorna int por defecto
        if(args.length == 0){
            return new PyInteger(0);
        }
        else if (args.length == 1){
            // se delega a la funcion __int__
            return args[0].__int__();
        }
        else {
            throw new PyTypeError(String.format("constructor de '%s' debe recibir 0 o 1 argumentos, '%s' recibidos.", __name__, args.length));
        }
    }

    @Override
    public PyString __repr__() throws PyException{
        return new PyString(String.valueOf(this.value));
    }

    //__str__ se delega a object.

    @Override
    public PyObject __int__() throws PyException {
        return this;
    }

    @Override
    public int __getint__() throws PyException{
        return this.value;
    }

    @Override
    public PyObject __long__() throws PyException {
        //int es siempre convertible a long, no chequeo nada.
        return new PyLong((long)this.value);
    }

    @Override
    public PyObject __float__() throws PyException{
        //int es siempre convertible a double, no chequeo nada.
        return new PyFloat((double)this.value);
    }

    /**
     * 0 evalua en False, todo lo demas en true
     * @return
     * @throws PyException
     */
    @Override
    public PyBool __bool__() throws PyException{
        if(this.value == 0){
            return PySingletons.False;
        }
        return PySingletons.True;

    }


    @Override
    public PyObject __add__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            return AritmeticaHelper.__add__int_int(this, (PyInteger)obj);
        }

        if(obj instanceof PyLong){
            return AritmeticaHelper.__add__long_int((PyLong) obj, this);
        }

        if(obj instanceof PyFloat){
            return AritmeticaHelper.__add__float_int((PyFloat)obj, this);
        }

        if(obj instanceof PyBool){
            return AritmeticaHelper.__add__int_bool(this, (PyBool)obj);
        }

        throw AritmeticaHelper.getErrorBinary("+", this, obj);
    }

    @Override
    public PyObject __neg__() throws PyException{
        return new PyInteger(this.value * -1);
    }

    //__sub__ se hace automaticamente como add y neg.

    @Override
    public PyObject __mul__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            return AritmeticaHelper.__mul__int_int(this, (PyInteger) obj);
        }

        if(obj instanceof PyLong){
            return AritmeticaHelper.__mul__long_int((PyLong) obj, this);
        }

        if(obj instanceof PyFloat){
            return AritmeticaHelper.__mul__float_int((PyFloat) obj, this);
        }

        if(obj instanceof PyBool){
            return AritmeticaHelper.__mul__int_bool(this, (PyBool) obj);
        }

        if(obj instanceof PyString){
            return obj.__mul__(this);
        }

        throw AritmeticaHelper.getErrorBinary("*", this, obj);
    }

    @Override
    public PyObject __pow__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            double r = Math.pow ((double)this.value, (double)((PyInteger)obj).value);

            if((r != Math.floor(r)) || Double.isInfinite(r)){
                return new PyFloat(r);
            }

            if( r > (double)Integer.MAX_VALUE || r < (double)Integer.MIN_VALUE){
                return new PyLong((long)r);
            }

            return new PyInteger((int)r);
        }

        if(obj instanceof PyLong){
            double r = Math.pow ((double)this.value, (double)((PyLong)obj).value);
            if((r != Math.floor(r)) || Double.isInfinite(r)){
                return new PyFloat(r);
            }
            return new PyLong((long)r);
        }

        if(obj instanceof PyFloat){
            return new PyFloat(Math.pow ((double)this.value, ((PyFloat)obj).value));
        }

        if(obj instanceof PyBool){
            if(!((PyBool)obj).value){
                return new PyInteger(1);
            }
            return new PyInteger(this.value);
        }


        throw AritmeticaHelper.getErrorBinary("**", this, obj);
    }


    @Override
    public PyObject __div__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            //division entera nunca va a dar un numero mas grande, asi que no hago chequeos por int o long.
            int div = ((PyInteger)obj).value;
            if(div == 0){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyInteger(this.value / div);
        }


        if(obj instanceof PyLong){
            long div = ((PyLong)obj).value;
            if(div == 0L){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyLong(this.value / div);

        }

        if(obj instanceof PyFloat){
            double div = ((PyFloat)obj).value;
            if(div == 0.0){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyFloat(this.value / div);
        }

        if(obj instanceof PyBool){
            if(!((PyBool)obj).value){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyInteger(this.value);

        }

        throw AritmeticaHelper.getErrorBinary("/", this, obj);
    }

    //division entera se encarga object.

    /**
     * Copy paste de div, pero con modulo...
     */
    @Override
    public PyObject __mod__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            //division entera nunca va a dar un numero mas grande, asi que no hago chequeos por int o long.
            int div = ((PyInteger)obj).value;
            if(div == 0){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyInteger(this.value % div);
        }


        if(obj instanceof PyLong){
            long div = ((PyLong)obj).value;
            if(div == 0L){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyLong(this.value % div);

        }

        if(obj instanceof PyFloat){
            double div = ((PyFloat)obj).value;
            if(div == 0.0){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyFloat(this.value % div);
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
            return AritmeticaHelper.__band__int_int(this, (PyInteger) obj);
        }

        if(obj instanceof PyLong){
            return AritmeticaHelper.__band__long_int((PyLong) obj, this);
        }

        if(obj instanceof PyBool){
            return AritmeticaHelper.__band__int_bool(this, (PyBool) obj);
        }

        throw AritmeticaHelper.getErrorBinary("&", this, obj);
    }

    @Override
    public PyObject __bnot__() throws PyException{
        return new PyInteger(~this.value);
    }


    @Override
    public PyObject __bor__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            return AritmeticaHelper.__bor__int_int(this, (PyInteger) obj);
        }

        if(obj instanceof PyLong){
            return AritmeticaHelper.__bor__long_int((PyLong) obj, this);
        }

        if(obj instanceof PyBool){
            return AritmeticaHelper.__bor__int_bool(this, (PyBool) obj);
        }

        throw AritmeticaHelper.getErrorBinary("|", this, obj);
    }

    @Override
    public PyObject __bxor__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            return AritmeticaHelper.__bxor__int_int(this, (PyInteger) obj);
        }

        if(obj instanceof PyLong){
            return AritmeticaHelper.__bxor__long_int((PyLong) obj, this);
        }

        if(obj instanceof PyBool){
            return AritmeticaHelper.__bxor__int_bool(this, (PyBool) obj);
        }

        throw AritmeticaHelper.getErrorBinary("^", this, obj);
    }

    @Override
    public PyObject __sleft__(PyObject obj) throws PyException{

        //Shift se puede ir de rango de ints
        if(obj instanceof PyInteger){
            long r = this.value << ((PyInteger)obj).value;
            if( r > (long)Integer.MAX_VALUE || r < (long)Integer.MIN_VALUE){
                return new PyLong(r);
            }

            return new PyInteger((int)r);
        }

        if(obj instanceof PyLong){
            return new PyLong(this.value << ((PyLong)obj).value);
        }

        if(obj instanceof PyBool){
            long r = this.value << (((PyBool)obj).value ? 1 : 0);

            if( r > (long)Integer.MAX_VALUE || r < (long)Integer.MIN_VALUE){
                return new PyLong(r);
            }

            return new PyInteger((int)r);
        }

        throw AritmeticaHelper.getErrorBinary("<<", this, obj);
    }


    @Override
    public PyObject __sright__(PyObject obj) throws PyException{

        //Shift right no puede desbordar.
        if(obj instanceof PyInteger){
            return new PyInteger(this.value >> ((PyInteger)obj).value);
        }

        if(obj instanceof PyLong){
            return new PyLong(this.value >> ((PyLong)obj).value);
        }

        if(obj instanceof PyBool){
            return new PyInteger( this.value >> (((PyBool)obj).value ? 1 : 0));
        }

        throw AritmeticaHelper.getErrorBinary(">>", this, obj);
    }

    @Override
    public PyBool __eq__(PyObject obj) throws PyException{

        if( (obj instanceof PyInteger) ) {
            return this.value == ((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return (long)this.value == ((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyFloat){
            return (double)this.value == ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyBool){
            return ((this.value != 0) == ((PyBool)obj).value) ? PySingletons.True : PySingletons.False;
        }

        return PySingletons.False;
    }

    //__not_eq__ implementado por object a partir de __eq__

    @Override
    public PyBool __gt__(PyObject obj) throws PyException{

        if( (obj instanceof PyInteger) ) {
            return this.value > ((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return (long)this.value > ((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyFloat){
            return (double)this.value > ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyBool){
            return (this.value  > ( ( ((PyBool)obj).value) ? 1 : 0 ) ) ? PySingletons.True : PySingletons.False;
        }

        throw AritmeticaHelper.getErrorBinary(">", this, obj);
    }

    @Override
    public PyBool __lt__(PyObject obj) throws PyException{

        if( (obj instanceof PyInteger) ) {
            return this.value < ((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return (long)this.value < ((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyFloat){
            return (double)this.value < ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyBool){
            return (this.value  < ( ( ((PyBool)obj).value) ? 1 : 0 ) ) ? PySingletons.True : PySingletons.False;
        }

        throw AritmeticaHelper.getErrorBinary("<", this, obj);
    }

    @Override
    public PyBool __ge__(PyObject obj) throws PyException{

        if( (obj instanceof PyInteger) ) {
            return this.value >= ((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return (long)this.value >= ((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyFloat){
            return (double)this.value >= ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyBool){
            return (this.value  >= ( ( ((PyBool)obj).value) ? 1 : 0 ) ) ? PySingletons.True : PySingletons.False;
        }

        throw AritmeticaHelper.getErrorBinary(">=", this, obj);
    }

    @Override
    public PyBool __le__(PyObject obj) throws PyException{

        if( (obj instanceof PyInteger) ) {
            return this.value <= ((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return (long)this.value <= ((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyFloat){
            return (double)this.value <= ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyBool){
            return (this.value  <= ( ( ((PyBool)obj).value) ? 1 : 0 ) ) ? PySingletons.True : PySingletons.False;
        }

        throw AritmeticaHelper.getErrorBinary("<=", this, obj);
    }


    @Override
    public int hashCode(){
        return this.value;
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
