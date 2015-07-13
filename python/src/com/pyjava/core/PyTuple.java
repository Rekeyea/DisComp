package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyIndexError;
import com.pyjava.core.exceptions.PyTypeError;
import com.pyjava.core.exceptions.PyValueError;

import java.util.ArrayList;

/**
 * Created by Cristiano on 10/07/2015.
 */
public class PyTuple extends PyObject {

    public static final String __name__ = "tuple";

    public ArrayList<PyObject> tupla = new ArrayList<>();

    public PyTuple(){

    }

    public PyTuple(ArrayList<PyObject> t){
        this.tupla = t;
    }



    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {

        //Sin argumentos, construye tupla vacia
        if(args.length == 0){
            return new PyTuple();
        }
        //con argumentos, se delega al objeto argumento
        else if (args.length == 1){
            return args[0].__tuple__();
        }
        else {
            throw new PyTypeError(String.format("constructor de '%s' debe recibir 0 o 1 argumentos, '%s' recibidos.", __name__, args.length));
        }
    }

    @Override
    public PyString __repr__() throws PyException{

        if(this.tupla.size() > 0) {
            StringBuilder res = new StringBuilder("(");
            for (PyObject e : this.tupla) {

                if(e instanceof PyTuple) {
                    res.append("(...), ");
                }
                else if(e instanceof PyList) {
                    res.append("[...], ");
                }
                else if(e instanceof PyDict){
                    res.append("{...}, ");
                }
                else {
                    res.append(e.__repr__().value + ", ");
                }
            }

            res.deleteCharAt(res.length() - 1);
            res.deleteCharAt(res.length() - 1);

            res.append(")");
            return new PyString(res.toString());
        }
        else {
            return new PyString("()");
        }

    }

    @Override
    public PyObject __add__(PyObject obj) throws PyException{

        if(obj instanceof PyTuple){
            ArrayList<PyObject> res = new ArrayList<>(this.tupla);
            res.addAll(((PyTuple)obj).tupla);
            return new PyTuple(res);
        }

        throw AritmeticaHelper.getErrorBinary("+", this, obj);
    }



    @Override
    public PyBool __bool__() throws PyException{
        return this.tupla.size() > 0 ? PySingletons.True : PySingletons.False;
    }

    @Override
    public PyObject __list__() throws PyException{
        return new PyList(new ArrayList<PyObject>(this.tupla));
    }

    /**
     * Al igual que python, la tuple(tupla) retorna el mismo objeto ya que es inmutable.
     * @return
     * @throws PyException
     */
    @Override
    public PyObject __tuple__() throws PyException{
        return this;
    }

    @Override
    public PyObject __iter__() throws PyException{
        return new PyIterator(this, this.tupla.iterator());
    }

    @Override
    public PyObject[] __unpack__(int c) throws PyException{
        if(tupla.size() < c){
            throw new PyValueError("No hay suficientes valores para iterar.");
        }
        if(tupla.size() > c){
            throw new PyValueError("Hay demasiados valores para iterar.");
        }
        PyObject[] res = tupla.toArray(new PyObject[c]);

        return res;
    }

    @Override
    public PyObject __get_index__(PyObject i) throws PyException{

        try{
            return this.tupla.get(i.__getint__());
        }
        catch (PyException e){
            throw new PyTypeError(String.format("%s no es un indice valido", i.getType().getClassName()));
        }
        catch (IndexOutOfBoundsException e){
            throw new PyIndexError();
        }
    }





    public static class Builtins {
        private static AttrDict builtins = null;

        public static AttrDict getBuiltins() {

            if (builtins == null) {
                builtins = new AttrDict();

                PyType clase = PySingletons.types.get(__name__);

                PyNativeFunction size = new PyNativeFunction("size", clase,
                        new PyCallable() {
                            @Override
                            public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                                if (args.length != 1) {
                                    throw new PyTypeError(String.format("size necesita 1 argumento, %s encontrados.", args.length));
                                }
                                if (!(args[0] instanceof PyTuple)) {
                                    throw new PyTypeError(String.format("El primer argumento de size deben ser de tipo '%s'.", PyTuple.__name__));
                                }

                                PyTuple tupla = (PyTuple) args[0];


                                return new PyInteger(tupla.tupla.size());


                            }
                        }
                );
                builtins.put(size.funcionNativaNombre, size);
            }
            return builtins;

        }

    }


}
