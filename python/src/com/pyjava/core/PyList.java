package com.pyjava.core;

import com.pyjava.core.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
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

                //Para evitar recursion infinita, no permitimos imprimir listas, dicts o tuplas anidados.
                if(e instanceof PyList) {
                    res.append("[...], ");
                }
                else if (e instanceof PyTuple){
                    res.append("(...), ");
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

            res.append("]");
            return new PyString(res.toString());
        }
        else {
            return new PyString("[]");
        }

    }

    @Override
    public PyObject __add__(PyObject obj) throws PyException{

        if(obj instanceof PyList){
            ArrayList<PyObject> res = new ArrayList<>(this.lista);
            res.addAll(((PyList)obj).lista);
            return new PyList(res);
        }

        throw AritmeticaHelper.getErrorBinary("+", this, obj);
    }

    @Override
    public boolean __hasheable__(){
        return false;
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
    public PyObject __tuple__() throws PyException{
        return new PyTuple(new ArrayList<PyObject>(this.lista));
    }

    @Override
    public PyObject __iter__() throws PyException{
        return new PyIterator(this, this.lista.iterator());
    }

    @Override
    public PyObject __dict__()throws PyException{
        //Construye un dict a partir de la lista.
        //La lista debe tener objetos iterables, donde el primer valor es la clave y el segundo el valor
        //Si no tiene exactamente 2 iterables, error.

        PyDict res = new PyDict();
        PyObject iterador;
        PyObject k;
        PyObject v;

        for(PyObject ele : this.lista){
            try {
                iterador = ele.__iter__();
            }
            catch (PyException e){
                throw new PyValueError(String.format("Conversion de %s a %s: Los elementos deben ser iterables con exactamente 2 elementos.", PyList.__name__, PyDict.__name__));
            }

            try{
                k = iterador.__next__();
                v = iterador.__next__();

                try{
                    iterador.__next__();
                    //hay elementos, error.
                    throw new PyTypeError();
                }
                catch (PyStopIteration e){
                    //ok, no hago nada
                }
            }
            catch (PyException e){
                throw new PyValueError(String.format("Conversion de %s a %s: Los elementos deben ser iterables con exactamente 2 elementos.", PyList.__name__, PyDict.__name__));
            }
            res.__set_index__(k, v);

        }
        return res;
    }


    @Override
    public PyObject[] __unpack__(int c) throws PyException{
        if(lista.size() < c){
            throw new PyValueError("No hay suficientes valores para iterar.");
        }
        if(lista.size() > c){
            throw new PyValueError("Hay demasiados valores para iterar.");
        }
        PyObject[] res = lista.toArray(new PyObject[c]);

        return res;
    }


    @Override
    public PyObject __get_index__(PyObject i) throws PyException{

        if(!(i instanceof PySlice)) {

            try {
                int index = i.__getint__();
                if (index < 0) {
                    index = lista.size() + index;
                }
                return this.lista.get(index);
            } catch (PyException e) {
                throw new PyTypeError(String.format("'%s' no es un indice valido para listas", i.getType().getClassName()));
            } catch (IndexOutOfBoundsException e) {
                throw new PyIndexError();
            }
        }
        else {
            int listaSize = lista.size();

            PySlice slice = (PySlice)i;
            int start = slice.start != null ? slice.start : 0;
            int end = slice.end != null ? slice.end : listaSize;
            int step = slice.step != null ? slice.step : 1;


            if(start < 0){
                start = listaSize + start;
            }

            if(end < 0 ){
                end = listaSize + end;
            }


            ArrayList<PyObject> res = new ArrayList<>();
            if(step > 0){
                int maxIndex = listaSize - 1;
                for(int iter = start; iter < end; iter+=step){
                    if(iter > maxIndex){
                        break;
                    }
                    res.add(lista.get(iter));
                }

            }
            else {
                throw new PyValueError("Slice step debe ser mayor a 0");

            }

            return new PyList(res);


        }
    }

    @Override
    public void __set_index__(PyObject i, PyObject v) throws PyException{

        if(!(i instanceof PySlice)) {

            try {
                int index = i.__getint__();
                if (index < 0) {
                    index = lista.size() + index;
                }
                this.lista.set(index, v);
            } catch (PyException e) {
                throw new PyTypeError(String.format("'%s' no es un indice valido para listas", i.getType().getClassName()));
            } catch (IndexOutOfBoundsException e) {
                throw new PyIndexError();
            }
        }
        else {

            //si estamos asignando con un slice
            //el valor debe ser un iterable
            int listaSize = lista.size();

            PySlice slice = (PySlice)i;
            int start = slice.start != null ? slice.start : 0;
            int end = slice.end != null ? slice.end : listaSize;
            int step = slice.step != null ? slice.step : 1;


            if(start < 0){
                start = listaSize + start;
            }

            if(end < 0 ){
                end = listaSize + end;
            }


            ArrayList<PyObject> res = new ArrayList<>(this.lista);
            PyObject iterador = v.__iter__();
            if(step > 0){

                //En cualquier caso, no hago mas nada una vez consumi todo el iterador, simplemente retorno.
                try {

                    //Primero asigna elementos reemplazando.

                    int maxIndex = listaSize - 1;
                    int iter = start;
                    for (; iter < end; iter += step) {
                        if (iter > maxIndex) {
                            break;
                        }
                        res.set(iter,iterador.__next__());
                    }

                    //Despues, al igual que python, segun donde se quedo iter, inserto lo que sobro.
                    while (true){
                        if (iter > maxIndex) {
                            res.add(iterador.__next__());
                        }
                        else {
                            res.add(iter, iterador.__next__());
                        }
                        iter++;
                    }
                }
                catch (PyStopIteration e){

                }

            }
            else {
                throw new PyValueError("Slice step debe ser mayor a 0");

            }

            this.lista = res;


        }
    }

    public PyObject popIndex(PyObject i) throws PyException{

        try{
            int index = i.__getint__();
            if(index < 0){
                index = lista.size() + index;
            }
            return this.lista.remove(index);
        }
        catch (PyException e){
            throw new PyTypeError(String.format("'%s' no es un indice valido para listas", i.getType().getClassName()));
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

                //Agrega el objeto al final del primer arugmento, siendo este una PyList.
                PyNativeFunction append = new PyNativeFunction("append", clase,
                        new PyCallable() {
                            @Override
                            public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                                if (args.length != 2) {
                                    throw new PyTypeError(String.format("append necesita 2 argumentos, %s encontrados.", args.length));
                                }
                                if (!(args[0] instanceof PyList)) {
                                    throw new PyTypeError(String.format("El primer argumento de append debe ser de tipo '%s'.", PyList.__name__));
                                }

                                PyList lista = (PyList) args[0];
                                lista.lista.add((PyObject) args[1]);

                                return PySingletons.None;


                            }
                        }
                );
                builtins.put(append.funcionNativaNombre, append);


                //cuenta las ocurrencias del objeto en args[1] en arg[0], usando el comparador de igualdad.
                PyNativeFunction count = new PyNativeFunction("count", clase,
                        new PyCallable() {
                            @Override
                            public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                                if (args.length != 2) {
                                    throw new PyTypeError(String.format("count necesita 2 argumentos, %s encontrados.", args.length));
                                }
                                if (!(args[0] instanceof PyList)) {
                                    throw new PyTypeError(String.format("El primer argumento de count debe ser de tipo '%s'.", PyList.__name__));
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




                PyNativeFunction extend = new PyNativeFunction("extend", clase,
                        new PyCallable() {
                            @Override
                            public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                                if (args.length != 2) {
                                    throw new PyTypeError(String.format("extend necesita 2 argumentos, %s encontrados.", args.length));
                                }
                                if (!(args[0] instanceof PyList)) {
                                    throw new PyTypeError(String.format("El primer argumento de extend debe ser de tipo '%s'.", PyList.__name__));
                                }

                                PyList lista = (PyList) args[0];

                                //El segundo argumento debe ser iterable
                                PyObject iterador = null;
                                try{
                                    iterador = args[1].__iter__();
                                }
                                catch (PyTypeError e){
                                    throw new PyTypeError(String.format("El segundo argumento de extend debe ser un iterable."));
                                }

                                ArrayList<PyObject> res = new ArrayList<>();
                                try{
                                    while (true) {
                                        res.add(iterador.__next__());
                                    }
                                }
                                catch (PyStopIteration e){
                                }

                                lista.lista.addAll(res);
                                return PySingletons.None;


                            }
                        }
                );
                builtins.put(extend.funcionNativaNombre, extend);


                PyNativeFunction index = new PyNativeFunction("index", clase,
                        new PyCallable() {
                            @Override
                            public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                                if (args.length != 2 && args.length != 3) {
                                    throw new PyTypeError(String.format("index necesita 2 or 3 argumentos, %s encontrados.", args.length));
                                }
                                if (!(args[0] instanceof PyList)) {
                                    throw new PyTypeError(String.format("El primer argumento de index debe ser de tipo '%s'.", PyList.__name__));
                                }

                                PyList lista = (PyList) args[0];
                                if(args.length == 2) {
                                    int res = lista.lista.indexOf(args[1]);
                                    if(res == -1){
                                        throw new PyValueError(String.format("El valor %s no se encuentra en la lista.", args[1].__repr__().value));
                                    }
                                    return new PyInteger(res);
                                }
                                else {
                                    int index = 0;
                                    try{
                                        index = args[2].__getint__();
                                    }
                                    catch (PyException e){
                                        throw new PyTypeError(String.format("El argumento 3 debe ser de tipo '%s'.", PyInteger.__name__));
                                    }

                                    int res = -1;
                                    try {
                                        res = lista.lista.subList(index, lista.lista.size()).indexOf(args[1]);
                                    }
                                    catch (IndexOutOfBoundsException e){
                                    }
                                    catch (IllegalArgumentException e){
                                    }

                                    if(res == -1){
                                        throw new PyValueError(String.format("El valor %s no se encuentra en la lista.", args[1].__repr__().value));
                                    }

                                    return new PyInteger(res + index);
                                }


                            }
                        }
                );
                builtins.put(index.funcionNativaNombre, index);


                PyNativeFunction insert = new PyNativeFunction("insert", clase,
                        new PyCallable() {
                            @Override
                            public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                                if (args.length != 3) {
                                    throw new PyTypeError(String.format("insert necesita 3 argumentos, %s encontrados.", args.length));
                                }
                                if (!(args[0] instanceof PyList)) {
                                    throw new PyTypeError(String.format("El primer argumento de insert debe ser de tipo '%s'.", PyList.__name__));
                                }

                                PyList lista = (PyList) args[0];

                                lista.__set_index__(args[1], args[2]);

                                return PySingletons.None;

                            }
                        }
                );
                builtins.put(insert.funcionNativaNombre, insert);


                PyNativeFunction pop = new PyNativeFunction("pop", clase,
                        new PyCallable() {
                            @Override
                            public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                                if (args.length != 2) {
                                    throw new PyTypeError(String.format("pop necesita 2 argumentos, %s encontrados.", args.length));
                                }
                                if (!(args[0] instanceof PyList)) {
                                    throw new PyTypeError(String.format("El primer argumento de pop debe ser de tipo '%s'.", PyList.__name__));
                                }

                                PyList lista = (PyList) args[0];

                                return lista.popIndex(args[1]);

                            }
                        }
                );
                builtins.put(pop.funcionNativaNombre, pop);



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
