package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyTypeError;
import com.pyjava.core.exceptions.PyValueError;

/**
 * Created by Cristiano on 17/06/2015.
 */
public class PyFloat extends PyObject {

    public static final String __name__ = "float";

    public double value = 0.0;

    public PyFloat() {
    }

    public PyFloat(double value) {
        this.value = value;
    }

    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {

        if(kwargs.getCount() > 0){
            throw new PyTypeError(String.format("constructor de '%s' no debe recibir ningun argumento por nombre.", __name__));
        }

        //Si no me pasaron argumentos
        if(args.length == 0){
            return new PyFloat(0.0);
        }
        else if (args.length == 1){
            // se delega a la funcion __long__
            return args[0].__float__();
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

    /**
     * Si es muy grande, retorna un PyLong
     * @return
     * @throws PyException
     */
    @Override
    public PyObject __int__() throws PyException {
        //Veo que se pueda convertir a int.
        try{
            if(this.value > (double)Integer.MAX_VALUE || this.value < (double)Integer.MIN_VALUE){
                if(this.value > (double)Long.MAX_VALUE || this.value < (double)Long.MIN_VALUE){
                    throw new Exception();
                }

                return new PyLong((long)this.value);

            }
            return new PyInteger((int)this.value);
        }
        catch (Throwable t){
            throw new PyValueError(String.format("el valor '%s' no es convertible a %s", this.value, PyInteger.__name__));
        }
    }

    @Override
    public PyObject __long__() throws PyException {

        try{
            if(this.value > (double)Long.MAX_VALUE || this.value < (double)Long.MIN_VALUE){
                throw new Exception();
            }
            return new PyLong((long)this.value);
        }
        catch (Throwable t){
            throw new PyValueError(String.format("el valor '%s' no es convertible a %s", this.value, PyLong.__name__));
        }
    }

    @Override
    public PyObject __float__() throws PyException{
        return this;
    }

    /**
     * 0 evalua en False, todo lo demas en true
     * @return
     * @throws PyException
     */
    @Override
    public PyBool __bool__() throws PyException{
        if(this.value == 0.0){
            return PySingletons.False;
        }
        return PySingletons.True;

    }


    @Override
    public PyObject __add__(PyObject obj) throws PyException{
        if(obj instanceof PyInteger){
            return AritmeticaHelper.__add__float_int(this, (PyInteger)obj);
        }

        if(obj instanceof PyLong){
            return AritmeticaHelper.__add__float_long(this,(PyLong)obj);
        }

        if(obj instanceof PyFloat){
            return AritmeticaHelper.__add__float_float(this, (PyFloat) obj);
        }

        if(obj instanceof PyBool){
            return AritmeticaHelper.__add__float_bool(this, (PyBool) obj);
        }

        throw AritmeticaHelper.getErrorBinary("+", this, obj);
    }

    @Override
    public PyObject __neg__() throws PyException{
        return new PyFloat(this.value * -1);
    }

    @Override
    public PyObject __mul__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            return AritmeticaHelper.__mul__float_int(this, (PyInteger) obj);
        }

        if(obj instanceof PyLong){
            return AritmeticaHelper.__mul__float_long(this, (PyLong) obj);
        }

        if(obj instanceof PyFloat){
            return AritmeticaHelper.__mul__float_float(this, (PyFloat) obj);
        }

        if(obj instanceof PyBool){
            return AritmeticaHelper.__mul__float_bool(this, (PyBool) obj);
        }

        throw AritmeticaHelper.getErrorBinary("*", this, obj);


    }


    @Override
    public PyObject __pow__(PyObject obj) throws PyException{

        if(obj instanceof PyInteger){
            return new PyFloat((long)Math.pow (this.value, (double)((PyInteger)obj).value));
        }

        if(obj instanceof PyLong){
            return new PyFloat((long)Math.pow (this.value, (double)((PyLong)obj).value));
        }

        if(obj instanceof PyFloat){
            return new PyFloat(Math.pow (this.value, ((PyFloat)obj).value));
        }

        if(obj instanceof PyBool){
            if(!((PyBool)obj).value){
                return new PyFloat(1.0);
            }
            return new PyFloat(this.value);
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
            return new PyFloat(this.value / div);
        }


        if(obj instanceof PyLong){
            long div = ((PyLong)obj).value;
            if(div == 0L){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyFloat(this.value / div);

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
            return new PyFloat(this.value);

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
            return new PyFloat(this.value % div);
        }


        if(obj instanceof PyLong){
            long div = ((PyLong)obj).value;
            if(div == 0L){
                throw AritmeticaHelper.getZeroError(this,obj);
            }
            return new PyFloat(this.value % div);

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
            return new PyFloat(this.value % 1);

        }

        throw AritmeticaHelper.getErrorBinary("%", this, obj);
    }


    @Override
    public PyBool __eq__(PyObject obj) throws PyException{

        if(obj instanceof PyFloat){
            return this.value == ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }
        if( (obj instanceof PyInteger) ) {
            return this.value == (double)((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return this.value == (double)((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyBool){
            return ((this.value != 0.0) == ((PyBool)obj).value) ? PySingletons.True : PySingletons.False;
        }

        return PySingletons.False;
    }

    //__not_eq__ implementado por object a partir de __eq__

    @Override
    public PyBool __gt__(PyObject obj) throws PyException{

        if(obj instanceof PyFloat){
            return this.value > ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }
        if( (obj instanceof PyInteger) ) {
            return this.value > (double)((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return this.value > (double)((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyBool){
            return (this.value  > ( ( ((PyBool)obj).value) ? 1.0 : 0.0 ) ) ? PySingletons.True : PySingletons.False;
        }

        throw AritmeticaHelper.getErrorBinary(">", this, obj);
    }

    @Override
    public PyBool __lt__(PyObject obj) throws PyException{

        if(obj instanceof PyFloat){
            return this.value < ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }
        if( (obj instanceof PyInteger) ) {
            return this.value < (double)((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return this.value < (double)((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyBool){
            return (this.value  < ( ( ((PyBool)obj).value) ? 1.0 : 0.0 ) ) ? PySingletons.True : PySingletons.False;
        }

        throw AritmeticaHelper.getErrorBinary("<", this, obj);
    }


    @Override
    public PyBool __ge__(PyObject obj) throws PyException{

        if(obj instanceof PyFloat){
            return this.value >= ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }
        if( (obj instanceof PyInteger) ) {
            return this.value >= (double)((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return this.value >= (double)((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyBool){
            return (this.value  >= ( ( ((PyBool)obj).value) ? 1.0 : 0.0 ) ) ? PySingletons.True : PySingletons.False;
        }

        throw AritmeticaHelper.getErrorBinary(">=", this, obj);
    }

    @Override
    public PyBool __le__(PyObject obj) throws PyException{

        if(obj instanceof PyFloat){
            return this.value <= ((PyFloat)obj).value ? PySingletons.True : PySingletons.False;
        }
        if( (obj instanceof PyInteger) ) {
            return this.value <= (double)((PyInteger)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyLong){
            return this.value <= (double)((PyLong)obj).value ? PySingletons.True : PySingletons.False;
        }
        if(obj instanceof PyBool){
            return (this.value  <= ( ( ((PyBool)obj).value) ? 1.0 : 0.0 ) ) ? PySingletons.True : PySingletons.False;
        }

        throw AritmeticaHelper.getErrorBinary("<=", this, obj);
    }


    @Override
    public int hashCode(){
        return (int)this.value;
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
