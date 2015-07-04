package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyTypeError;
import com.pyjava.core.exceptions.PyValueError;
import org.w3c.dom.Attr;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Cristiano on 16/06/2015.
 */
public class PyString extends PyObject{

    public static final String __name__ = "str";


    public String value = "";

    public PyString() {
    }


    public PyString(String value){
        this.value = value;
    }

    /**
     * Construye una instancia y la retorna.
     *
     */
    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {

        if(kwargs.getCount() > 0){
            throw new PyTypeError(String.format("constructor de '%s' no debe recibir ningun argumento por nombre.", __name__));
        }

        //Si no me pasaron argumentos, construye string vacio.
        if(args.length == 0){
            return PySingletons.strVacio;
        }
        else if (args.length == 1){
            return args[0].__str__();
        }
        else {
            throw new PyTypeError(String.format("constructor de '%s' debe recibir 0 o 1 argumentos, '%s' recibidos.", __name__, args.length));
        }

    }

    @Override
    public PyString __repr__() throws PyException{
        return new PyString(String.format("'%s'",this.value));
    }

    @Override
    public PyString __str__() throws PyException{
        return this;
    }


    @Override
    public PyObject __int__() throws PyException{

        try{
            try{
                return new PyInteger(Integer.parseInt(this.value));
            }
            catch (Throwable t){
                return new PyLong(Long.parseLong(this.value));
            }
        }
        catch (NumberFormatException e){
            throw new PyValueError(String.format("el valor '%s' no es convertible a %s", this.value, PyInteger.__name__));
        }
    }

    @Override
    public PyObject __long__() throws PyException{

        try{
            return new PyLong(Long.parseLong(this.value));
        }
        catch (NumberFormatException e){
            throw new PyValueError(String.format("el valor '%s' no es convertible a %s", this.value, PyLong.__name__));
        }
    }

    @Override
    public PyObject __float__() throws PyException{
        try{
            return new PyFloat(Double.parseDouble(this.value));
        }
        catch (NumberFormatException e){
            throw new PyValueError(String.format("el valor '%s' no es convertible a %s", this.value, PyFloat.__name__));
        }
    }

    /**
     * String vacio evalua en False, todo lo demas en True
     * @return
     * @throws PyException
     */
    @Override
    public PyBool __bool__() throws PyException{
        if(this.value.equals("")){
            return PySingletons.False;
        }
        return PySingletons.True;

    }

    @Override
    public PyObject __add__(PyObject obj) throws PyException{
        //Sumar string con objetos, solo permite sumar con otro string.
        if(! (obj instanceof PyString) ){
            throw new PyTypeError(String.format("No se puede concatenar '%s' con '%s'", this.getType().getClassName(), obj.getType().getClassName()));
        }
        else {
            return new PyString(this.value + ((PyString) obj).value);
        }
    }

    @Override
    public PyObject __mul__(PyObject obj) throws PyException{
        if(obj instanceof PyInteger){
            int val = ((PyInteger)obj).value;
            String res = this.value;
            if(val <= 0){
                res = "";
            }
            else {
                for (int i = 0; i < val; i++) {
                    res += this.value;
                }
            }
            return new PyString(res);
        }

        if(obj instanceof PyLong){
            long val = ((PyLong)obj).value;
            String res = this.value;
            if(val <= 0){
                res = "";
            }
            else {
                for (int i = 0; i < val; i++) {
                    res += this.value;
                }
            }
            return new PyString(res);

        }

        if(obj instanceof PyBool){
            boolean val = ((PyBool)obj).value;
            String res = this.value;

            if(!val){
                res = "";
            }
            return new PyString(res);

        }

        throw AritmeticaHelper.getErrorBinary("*", this, obj);
    }

    @Override
    public PyBool __eq__(PyObject obj) throws PyException{
        if( (obj instanceof PyString) ) {
            return this.value.equals( ((PyString)obj).value) ? PySingletons.True : PySingletons.False;

        }
        return PySingletons.False;
    }

    //__not_eq__ implementado por object a partir de __eq__



    @Override
    public PyObject __iter__() throws PyException{

        return new PyIterator(this,

                new Iterator<PyObject>() {
                    private int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return pos < PyString.this.value.length();
                    }

                    @Override
                    public PyObject next() {
                        if(pos < PyString.this.value.length()){

                            PyObject res = new PyString(String.valueOf(PyString.this.value.charAt(pos)));
                            pos++;
                            return res;
                        }
                        throw new NoSuchElementException();
                    }
                }
        );
    }



    public static class Builtins{

        private static AttrDict builtins = null; //Sera singleton, para no instanciar builtins de esta clase muchas veces

        public static AttrDict getBuiltins(){

            if(builtins == null) {

                builtins = new AttrDict();

                PyType clase = PySingletons.types.get(__name__);

                //cuenta las ocurrencias de la sub cadena 'sub' en S
                //El primer argumento debe ser una instancia de PyString, y el segundo tambien.
                //Retorna una instancia de PyInt, o PyTypeError si los datos son invalidos.
                PyNativeFunction count = new PyNativeFunction("count", clase,
                        new PyCallable() {
                            @Override
                            public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                                if (args.length == 0) {
                                    throw new PyTypeError(String.format("count necesita 2 argumentos, 0 encontrados."));
                                }
                                if (args.length < 2) {
                                    throw new PyTypeError(String.format("count necesita 2 argumentos, %s encontrados.", args.length));
                                }
                                if (!(args[0] instanceof PyString) || !(args[1] instanceof PyString)) {
                                    throw new PyTypeError(String.format("Los argumentos de count deben ser de tipo '%s'.", PyString.__name__));
                                }

                                PyString str1 = (PyString) args[0];
                                PyString str2 = (PyString) args[1];

                                //POR AHORA COMO NO TENGO INT, RETORNO STR.

                                //No tengo funcion facil para hacer el conteo... estupido java.
                                int count = 0;
                                int idx = 0;

                                while ((idx = str1.value.indexOf(str2.value, idx)) != -1) {
                                    idx++;
                                    count++;
                                }

                                return new PyString(String.valueOf(count));


                            }
                        }
                );

                builtins.put(count.funcionNativaNombre, count);
            }


            return builtins;


        }
    }

}

