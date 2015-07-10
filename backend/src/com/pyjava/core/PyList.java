package com.pyjava.core;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyIndexError;
import com.pyjava.core.exceptions.PyTypeError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Cristiano on 09/07/2015.
 */
public class PyList extends PyObject {

    public static final String __name__ = "list";

    public ArrayList<PyObject> lista = new ArrayList<>();


    public PyList() {

    }

    public PyList(ArrayList<PyObject> l){
        this.lista = l;
    }

    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {

        //Sin argumentos, construye lista vacia
        if(args.length == 0){
            return new PyList();
        }
        //con argumentos, se delega al objeto argumento
        else if (args.length == 1){
            return args[0].__list__();
        }
        else {
            throw new PyTypeError(String.format("constructor de '%s' debe recibir 0 o 1 argumentos, '%s' recibidos.", __name__, args.length));
        }
    }

    @Override
    public PyString __repr__() throws PyException{

        if(this.lista.size() > 0) {
            StringBuilder res = new StringBuilder("[");
            for (PyObject e : this.lista) {
                res.append(e.__repr__().value + ", ");
            }

            res.deleteCharAt(res.length() - 1);
            res.deleteCharAt(res.length() - 2);

            res.append("]");
            return new PyString(res.toString());
        }
        else {
            return new PyString("[]");
        }

    }

    @Override
    public PyBool __bool__() throws PyException{
        return this.lista.size() > 0 ? PySingletons.True : PySingletons.False;
    }

    @Override
    public PyObject __list__() throws PyException{
        //Al igual que python, copia la lista, retorna una nueva referencia, aunque los elementos de adentro no son copiados
        // osea, hace una copia debil

        return new PyList(new ArrayList<PyObject>(this.lista));
    }

    @Override
    public PyObject __iter__() throws PyException{
        return new PyIterator(this, this.lista.iterator());
    }

    @Override
    public PyObject __get_index__(PyObject i) throws PyException{

        try{
            int index = ((PyInteger)i.__int__()).value;
            return this.lista.get(index);
        }
        catch (PyException e){
            throw new PyTypeError(String.format("%s no es un indice valido", i.getType().getClassName()));
        }
        catch (IndexOutOfBoundsException e){
            throw new PyIndexError();
        }
    }

    @Override
    public PyObject __set_index__(PyObject i, PyObject v) throws PyException{
        try{
            int index = ((PyInteger)i.__int__()).value;
            return this.lista.set(index, v);
        }
        catch (PyException e){
            throw new PyTypeError(String.format("%s no es un indice valido", i.getType().getClassName()));
        }
        catch (IndexOutOfBoundsException e){
            throw new PyIndexError();
        }
    }


    public static class Builtins{

        private static AttrDict builtins = null; //Sera singleton, para no instanciar builtins de esta clase muchas veces

        public static AttrDict getBuiltins(){

            if(builtins == null) {

                builtins = new AttrDict();

                PyType clase = PySingletons.types.get(__name__);

                //cuenta las ocurrencias del objeto en args[1] en arg[0], usando el comparador de igualdad.
                PyNativeFunction count = new PyNativeFunction("count", clase,
                        new PyCallable() {
                            @Override
                            public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                                if (args.length != 2) {
                                    throw new PyTypeError(String.format("count necesita 2 argumentos, %s encontrados.", args.length));
                                }
                                if (!(args[0] instanceof PyList)) {
                                    throw new PyTypeError(String.format("El primer argumento de count deben ser de tipo '%s'.", PyList.__name__));
                                }

                                PyList lista = (PyList) args[0];
                                PyObject obj = (PyObject) args[1];

                                int count = 0;
                                for(PyObject o : lista.lista){
                                    if(o.__eq__(obj).value){
                                        count++;
                                    }
                                }

                                return new PyInteger(count);


                            }
                        }
                );
                builtins.put(count.funcionNativaNombre, count);

                //Agrega el objeto al final del primer arugmento, siendo este una PyList.
                PyNativeFunction append = new PyNativeFunction("append", clase,
                        new PyCallable() {
                            @Override
                            public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                                if (args.length != 2) {
                                    throw new PyTypeError(String.format("append necesita 2 argumentos, %s encontrados.", args.length));
                                }
                                if (!(args[0] instanceof PyList)) {
                                    throw new PyTypeError(String.format("El primer argumento de append deben ser de tipo '%s'.", PyList.__name__));
                                }

                                PyList lista = (PyList) args[0];
                                lista.lista.add((PyObject) args[1]);

                                return PySingletons.None;


                            }
                        }
                );
                builtins.put(append.funcionNativaNombre, append);

                //Devuelve el tamano del a lista
                PyNativeFunction size = new PyNativeFunction("size", clase,
                        new PyCallable() {
                            @Override
                            public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                                if (args.length != 1) {
                                    throw new PyTypeError(String.format("size necesita 1 argumento, %s encontrados.", args.length));
                                }
                                if (!(args[0] instanceof PyList)) {
                                    throw new PyTypeError(String.format("El primer argumento de size deben ser de tipo '%s'.", PyList.__name__));
                                }

                                PyList lista = (PyList) args[0];


                                return new PyInteger(lista.lista.size());


                            }
                        }
                );
                builtins.put(size.funcionNativaNombre, size);
            }




            return builtins;


        }
    }

}
