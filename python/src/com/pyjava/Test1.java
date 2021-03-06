package com.pyjava;

import com.pyjava.core.*;
import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyStopIteration;
import com.pyjava.core.runtime.Code;
import com.pyjava.core.runtime.Instruccion;
import com.pyjava.core.runtime.OpCode;

import java.util.ArrayList;

public class Test1 {

    public static void main(String[] args){
	// write your code here

        PyType type = PySingletons.type;
        PyType object = PySingletons.object;
        PyType string = PySingletons.string;
        PyType bultin = PySingletons.builtinFunc;
        PyType integer = PySingletons.integer;
        PyType longint = PySingletons.longint;
        PyType pfloat = PySingletons.pfloat;
        PyType methodWrapper = PySingletons.methodWrapper;
        PyType bool = PySingletons.bool;
        PyType noneType = PySingletons.noneType;


        PyObject o = null;
        try {
            o = object.__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null);
            System.out.println(o.print());
            System.out.println(type.__call__(new PyObject[]{o}, PySingletons.kwargsVacios, null).print());

            o.__call__(null, null, null);
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }

        try{
            //Deberia dar error ya que intento construir un object con argumentos.
            o = object.__call__(new PyObject[]{object}, PySingletons.kwargsVacios, null);
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }

        PyObject s = null;
        try {
            s = string.__call__(new PyObject[]{o},PySingletons.kwargsVacios, null);
            System.out.println(s.print());
            System.out.println(s.__repr__().print());
            System.out.println(type.__call__(new PyObject[]{s}, PySingletons.kwargsVacios, null).print());

            s.__call__(null, null, null);
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }

        try {
            //Deberia explotar, ya que le estoy pasando mas de un argumento
            s = string.__call__(new PyObject[]{o, object},PySingletons.kwargsVacios, null);
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }

        try {
            //Deberia explotar, ya que le estoy pasando argumentos por clave
            AttrDict kwargs = new AttrDict();
            kwargs.put("TEST", o);

            s = string.__call__(PySingletons.argsVacios,kwargs, null);
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }



        try {
            System.out.println(type.__call__(new PyObject[]{type}, PySingletons.kwargsVacios, null).print());
            System.out.println(type.__call__(new PyObject[]{object}, PySingletons.kwargsVacios, null).print());
            System.out.println(type.__call__(new PyObject[]{string}, PySingletons.kwargsVacios, null).print());
            System.out.println(type.__call__(new PyObject[]{bultin}, PySingletons.kwargsVacios, null).print());
            System.out.println(type.__call__(new PyObject[]{methodWrapper}, PySingletons.kwargsVacios, null).print());
            System.out.println(type.print());
            System.out.println(object.print());
            System.out.println(string.print());
            System.out.println(bultin.print());
            System.out.println(methodWrapper.print());
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }

        System.out.println("------------------strings count -------------------");

        try {
            //Lo llamo desde la clase
            System.out.println(string.__getattr__("count").__call__(new PyObject[]{string.__call__(new PyObject[]{o}, PySingletons.kwargsVacios, null), string.__call__(new PyObject[]{o}, PySingletons.kwargsVacios, null)}, PySingletons.kwargsVacios,null).print());

            //Lo llamo desde la instancia
            System.out.println(s.__getattr__("count").__call__(new PyObject[]{s}, PySingletons.kwargsVacios, null).print());

            //llamo desde la clase
            System.out.println(object.__getattr__("__hash__").__call__(new PyObject[]{o}, PySingletons.kwargsVacios, null).print());

            //llamo desde la instancia
            System.out.println(o.__getattr__("__hash__").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());


            //Veo el print y tipo de hash al llamarlo desde la clase
            System.out.println(object.__getattr__("__hash__").print());
            System.out.println(object.__getattr__("__hash__").getType().print());


            //Veo el print y tipo de hash al llamarlo desde instancia
            System.out.println(o.__getattr__("__hash__").print());
            System.out.println(o.__getattr__("__hash__").getType().print());

            //veo el nombre de la funcion count desde instancia y desde clase
            System.out.println(s.__getattr__("count").print());
            System.out.println(string.__getattr__("count").print());

        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }

        System.out.println("-------------------------------------");

        PyObject i = null ;
        try {
            i = integer.__call__(new PyObject[]{new PyString("312323")}, PySingletons.kwargsVacios, null);
            System.out.println(i.print());

            System.out.println("Hash de instancia de int: " + i.__getattr__("__hash__").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());

            System.out.println("Instancia de int creada de int: "+integer.__call__(new PyObject[]{i}, PySingletons.kwargsVacios, null).print());

            System.out.println(string.__call__(new PyObject[]{i}, PySingletons.kwargsVacios, null).print());
            System.out.println(type.__call__(new PyObject[]{i}, PySingletons.kwargsVacios, null).print());

            i.__call__(null, null, null);
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }

        try {
            //deberia explotar porque le paso mas de un argumento
            i = integer.__call__(new PyObject[]{new PyString("312323"), i}, PySingletons.kwargsVacios, null);

        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }

        System.out.println("-------------------------------------");

        PyObject l = null ;
        try {
            //Pruebo construir long a partir de int
            l = longint.__call__(new PyObject[]{i}, PySingletons.kwargsVacios, null);
            System.out.println(l.print());
            System.out.println("Hash de instancia de long: " + l.__getattr__("__hash__").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());

            l = longint.__call__(new PyObject[]{new PyString("2147483648232")}, PySingletons.kwargsVacios, null);
            System.out.println(l.print());
            System.out.println("Hash de instancia de long: " + l.__getattr__("__hash__").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());

            System.out.println(l.__repr__().print());

            //pruebo construir un int a partir del long gigante, esto deberia crearme un long en vez de int.
            i = integer.__call__(new PyObject[]{l}, PySingletons.kwargsVacios, null);
            System.out.println(i.__repr__().print());

            //Trato con un int de un string grande, deberia construir un long tambien
            i = integer.__call__(new PyObject[]{new PyString("2147483123123648232")}, PySingletons.kwargsVacios, null);
            System.out.println(i.__repr__().print());
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("-------------------------------------");

        PyObject f = null ;
        try {
            //Pruebo construir float a partir de long
            f = pfloat.__call__(new PyObject[]{l}, PySingletons.kwargsVacios, null);
            System.out.println(f.print());
            System.out.println("Hash de instancia de float: " + f.__getattr__("__hash__").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());

            f = pfloat.__call__(new PyObject[]{new PyString("2147483648232")}, PySingletons.kwargsVacios, null);
            System.out.println(f.print());
            System.out.println("Hash de instancia de float: " + f.__getattr__("__hash__").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());



            l = longint.__call__(new PyObject[]{f}, PySingletons.kwargsVacios, null);
            System.out.println(l.print());

        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("-------------------------------------");

        PyObject b = null ;
        try {
            //Pruebo construir float a partir de long
            b = bool.__call__(new PyObject[]{l}, PySingletons.kwargsVacios, null);
            System.out.println(b.print());
            System.out.println("Hash de instancia de bool: " + b.__getattr__("__hash__").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());

            //bool por defecto
            b = bool.__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null);
            System.out.println(b.print());
            System.out.println("Hash de instancia de bool: " + b.__getattr__("__hash__").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());

            //Bool a partir de string vacio
            b = bool.__call__(new PyObject[]{PySingletons.strVacio}, PySingletons.kwargsVacios, null);
            System.out.println(b.print());
            System.out.println("Hash de instancia de bool: " + b.__getattr__("__hash__").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());

            //Bool a partir de algun string
            b = bool.__call__(new PyObject[]{s}, PySingletons.kwargsVacios, null);
            System.out.println(b.print());
            System.out.println("Hash de instancia de bool: " + b.__getattr__("__hash__").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());

            System.out.println(b.getType().print());

            //construyo string a partir de bool.
            s = string.__call__(new PyObject[]{b},PySingletons.kwargsVacios, null);
            System.out.println(s.getType().print());
            System.out.println(s.print());

            //Pruebo imprimir sus method wrappers para ver que muestren el tipo de instancia correcto
            System.out.println(b.__getattr__("__hash__").print());
            System.out.println(bool.__getattr__("__hash__").print());
            System.out.println(bool.__getattr__("__hash__").getType().print());
            System.out.println(bool.__getattr__("__hash__").getType().__getattr__("__hash__").print());

            //intento llamar a instancia bool, esto deberia explotar

            b.__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null);
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("-------------------------------------");

        PyObject n = null ;
        try {

            System.out.println(PySingletons.None.print());
            System.out.println(PySingletons.None.getType().print());
            b = bool.__call__(new PyObject[]{PySingletons.None}, PySingletons.kwargsVacios, null);

            System.out.println("Valor bool de None: " + b.print());


            //Valido que no pueda instanciar None
            n = noneType.__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null);

        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        try{
            integer.__call__(new PyObject[]{PySingletons.None}, PySingletons.kwargsVacios, null);
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("-------------------------------------");

        PyObject raw_input = null;
        try {
            raw_input = new PyNativeFunction("raw_input", null,
                    new PyCallable() {
                        @Override
                        public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                            return new PyString("raw_input prueba");
                        }
                    }
            );

            System.out.println(raw_input.print());
            System.out.println(type.__call__(new PyObject[]{raw_input}, PySingletons.kwargsVacios, null).print());

            System.out.println(raw_input.__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());

            raw_input = bultin.__call__(PySingletons.argsVacios,PySingletons.kwargsVacios, null);
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }

        System.out.println("-------------------------------------");


        try {
            System.out.println(raw_input.print());
            System.out.println(raw_input.__repr__().print());
            System.out.println(raw_input.__getattr__("__repr__").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());
            System.out.println(i.__getattr__("__repr__").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("-------------------------------------");


        try {
            PyObject s2 = s.__add__(s);
            System.out.println(s2.__repr__().print());

            s2 = s.__add__(i);
            System.out.println(s2.__repr__().print());
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("-------------------------------------");


        try {
            PyObject int1 = new PyInteger(Integer.MAX_VALUE);
            System.out.println(int1.__repr__().print());
            PyObject int2  = new PyInteger(Integer.MAX_VALUE);
            System.out.println(int2.__repr__().print());

            System.out.println(int1.__add__(int2).__repr__().print());

            PyObject int3 = int1.__add__(PySingletons.False);
            System.out.println(int3.__repr__().print());

            int3 = int1.__add__(PySingletons.True);
            System.out.println(int3.__repr__().print());

        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("-------------------------------------");


        try {
            PyObject int1 = new PyInteger(Integer.MAX_VALUE);
            System.out.println(int1.__repr__().print());
            PyObject int2  = new PyInteger(Integer.MAX_VALUE);
            System.out.println(int2.__repr__().print());


            System.out.println(int1.__sub__(int2).__repr__().print());

            System.out.println(int1.__add__(int2).__sub__(int2).__repr__().print());



        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }

        System.out.println("-------------------------------------");


        try {
            PyObject int1 = new PyInteger(465);
            System.out.println(int1.__repr__().print());
            PyObject long1 = new PyLong(46546546546546545L);
            System.out.println(long1.__repr__().print());
            PyFloat float1 = new PyFloat(54654.3545);
            System.out.println(float1.__repr__().print());


            System.out.println(int1.__add__(float1).__repr__().print());
            System.out.println(int1.__sub__(float1).__repr__().print());
            System.out.println(long1.__add__(float1).__repr__().print());
            System.out.println(long1.__sub__(float1).__repr__().print());

            PyObject bool1 = new PyBool(true);
            System.out.println(bool1.__add__(float1).__repr__().print());
            System.out.println(bool1.__sub__(float1).__repr__().print());


            System.out.println(float1.__mul__(long1).print());
            System.out.println(new PyString("hola").__mul__(new PyLong(10L)).print());

            PyObject bool2 = new PyBool(false);
            System.out.println(bool2.__mul__(float1).__repr__().print());

            System.out.println(bool1.__pow__(long1).__repr__().print());
            System.out.println(bool2.__pow__(float1).__repr__().print());
            System.out.println(bool2.__pow__(int1).__repr__().print());
            System.out.println(long1.__pow__(float1).__repr__().print());
            System.out.println(long1.__pow__(int1).__repr__().print());
            System.out.println(long1.__pow__(long1).__pow__(int1).__repr__().print());  //si se pasa del largo de long, se estanca en el maximo

            System.out.println(long1.__pow__(float1).__add__(long1).__repr__().print());    //si se pasa del largo de double, se convierte en infinity.

            System.out.println(long1.__div__(bool1).__repr__().print());
            System.out.println(long1.__div__(float1).__repr__().print());
            System.out.println(long1.__div__(int1).__repr__().print());

            PyObject int3 = new PyInteger(3);
            PyObject int4 = new PyInteger(4);
            PyObject float4 = new PyFloat(4.0);

            System.out.println(int3.__div__(int4).__repr__().print());
            System.out.println(int3.__div__(float4).__repr__().print());

            System.out.println(int3.__int_div__(int4).__repr__().print());
            System.out.println(int3.__int_div__(long1).__repr__().print());
            System.out.println(int3.__int_div__(float4).__repr__().print());

            try{
                System.out.println(int3.__int_div__(new PyString("0")).__repr__().print());
            }
            catch (Throwable t){
                System.out.println(t.getMessage());
            }

            try{
                System.out.println(int3.__int_div__(new PyFloat(0.0)).__repr__().print());
            }
            catch (Throwable t){
                System.out.println(t.getMessage());
            }


            System.out.println(int3.__mod__(int4).__repr__().print());
            System.out.println(int3.__mod__(long1).__repr__().print());
            System.out.println(int3.__mod__(float4).__repr__().print());


            try{
                System.out.println(new PyString("123123").__mod__(new PyInteger(3)).__repr__().print());
            }
            catch (Throwable t){
                System.out.println(t.getMessage());
            }

            System.out.println(int3.__band__(int4).__repr__().print());
            System.out.println(int3.__band__(long1).__repr__().print());

            System.out.println(PySingletons.True.__band__(PySingletons.False).__repr__().print());
            System.out.println(PySingletons.True.__band__(PySingletons.True).__repr__().print());
            System.out.println(PySingletons.False.__band__(PySingletons.False).__repr__().print());

            System.out.println(PySingletons.False.__bnot__().__repr__().print());
            System.out.println(PySingletons.True.__bnot__().__repr__().print());
            System.out.println(int3.__bnot__().__repr__().print());

            System.out.println(int3.__bor__(int4).__repr__().print());
            System.out.println(int3.__bor__(long1).__repr__().print());
            System.out.println(PySingletons.True.__bor__(PySingletons.False).__repr__().print());
            System.out.println(PySingletons.True.__bor__(PySingletons.True).__repr__().print());
            System.out.println(PySingletons.False.__bor__(PySingletons.False).__repr__().print());

            System.out.println(int3.__bxor__(int4).__repr__().print());
            System.out.println(int3.__bxor__(long1).__repr__().print());
            System.out.println(PySingletons.True.__bxor__(PySingletons.False).__repr__().print());
            System.out.println(PySingletons.True.__bxor__(PySingletons.True).__repr__().print());
            System.out.println(PySingletons.False.__bxor__(PySingletons.False).__repr__().print());

            System.out.println(int3.__sleft__(int4).__repr__().print());
            System.out.println(int3.__sleft__(long1).__repr__().print());
            System.out.println(PySingletons.True.__sleft__(PySingletons.False).__repr__().print());
            System.out.println(PySingletons.True.__sleft__(PySingletons.True).__repr__().print());
            System.out.println(PySingletons.False.__sleft__(PySingletons.False).__repr__().print());

            System.out.println(int3.__sright__(int4).__repr__().print());
            System.out.println(int3.__sright__(long1).__repr__().print());
            System.out.println(PySingletons.True.__sright__(PySingletons.False).__repr__().print());
            System.out.println(PySingletons.True.__sright__(PySingletons.True).__repr__().print());
            System.out.println(PySingletons.False.__sright__(PySingletons.False).__repr__().print());


            System.out.println("-------------------------------------");

            System.out.println(int3.__eq__(int4).__repr__().print());
            System.out.println(int3.__eq__(int3).__repr__().print());
            System.out.println(PySingletons.True.__eq__(PySingletons.False).__repr__().print());
            System.out.println(PySingletons.True.__eq__(PySingletons.True).__repr__().print());
            System.out.println(PySingletons.False.__eq__(PySingletons.False).__repr__().print());
            System.out.println(PySingletons.None.__eq__(PySingletons.None).__repr__().print());
            System.out.println(int3.__eq__(new PyFloat(3.0)).__repr__().print());
            System.out.println(int3.__not_eq__(new PyFloat(3.0)).__repr__().print());


            System.out.println("-------------------------------------");

            System.out.println(int3.__gt__(int4).__repr__().print());
            System.out.println(int3.__gt__(int3).__repr__().print());
            System.out.println(int4.__gt__(int3).__repr__().print());
            System.out.println(PySingletons.True.__gt__(PySingletons.False).__repr__().print());
            System.out.println(PySingletons.True.__gt__(PySingletons.True).__repr__().print());
            System.out.println(PySingletons.False.__gt__(PySingletons.False).__repr__().print());
            System.out.println(int3.__gt__(new PyFloat(3.0)).__repr__().print());
            System.out.println(int4.__gt__(new PyFloat(3.0)).__repr__().print());

            System.out.println("-------------------------------------");

            System.out.println(int3.__lt__(int4).__repr__().print());
            System.out.println(int3.__lt__(int3).__repr__().print());
            System.out.println(int4.__lt__(int3).__repr__().print());
            System.out.println(PySingletons.True.__lt__(PySingletons.False).__repr__().print());
            System.out.println(PySingletons.True.__lt__(PySingletons.True).__repr__().print());
            System.out.println(PySingletons.False.__lt__(PySingletons.False).__repr__().print());
            System.out.println(int3.__lt__(new PyFloat(3.0)).__repr__().print());
            System.out.println(int4.__lt__(new PyFloat(3.0)).__repr__().print());

            System.out.println("-------------------------------------");

            System.out.println(int3.__ge__(int4).__repr__().print());
            System.out.println(int3.__ge__(int3).__repr__().print());
            System.out.println(int4.__ge__(int3).__repr__().print());
            System.out.println(PySingletons.True.__ge__(PySingletons.False).__repr__().print());
            System.out.println(PySingletons.True.__ge__(PySingletons.True).__repr__().print());
            System.out.println(PySingletons.False.__ge__(PySingletons.False).__repr__().print());
            System.out.println(int3.__ge__(new PyFloat(3.0)).__repr__().print());
            System.out.println(int4.__ge__(new PyFloat(3.0)).__repr__().print());

            System.out.println("-------------------------------------");

            System.out.println(int3.__le__(int4).__repr__().print());
            System.out.println(int3.__le__(int3).__repr__().print());
            System.out.println(int4.__le__(int3).__repr__().print());
            System.out.println(PySingletons.True.__le__(PySingletons.False).__repr__().print());
            System.out.println(PySingletons.True.__le__(PySingletons.True).__repr__().print());
            System.out.println(PySingletons.False.__le__(PySingletons.False).__repr__().print());
            System.out.println(int3.__le__(new PyFloat(3.0)).__repr__().print());
            System.out.println(int4.__le__(new PyFloat(3.0)).__repr__().print());

        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("-------------------------------------");


        try {
            PyObject pp = new PyString("String de prueba para iterator");

            PyObject iterador = pp.__iter__();
            System.out.println(iterador.print());
            while (true){
                try{
                    System.out.println(iterador.__next__().print());
                }
                catch (PyStopIteration e){
                    System.out.println("----- Iteracion finalizada -----");
                    break;
                }
            }



        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("--------------- TEST LISTA ----------------------");


        try {
            PyList lista = new PyList();


            //Le agrego algunos elementos llamando la funcion append
            lista.__getattr__("append").__call__(new PyObject[]{s}, PySingletons.kwargsVacios, null);

            lista.__getattr__("append").__call__(new PyObject[]{s}, PySingletons.kwargsVacios, null);
            lista.__getattr__("append").__call__(new PyObject[]{o}, PySingletons.kwargsVacios, null);
            lista.__getattr__("append").__call__(new PyObject[]{lista}, PySingletons.kwargsVacios, null); //agrego a si misma a la lista
            PySingletons.list.__getattr__("append").__call__(new PyObject[]{lista, PySingletons.True}, PySingletons.kwargsVacios, null);   //agrego uno llamando al metodo de clase en vez de instancia

            //Agrego elemento por indice
            lista.__set_index__(new PyInteger(2), lista);
            lista.__set_index__(new PyLong(2), lista);
            lista.__set_index__(PySingletons.True, lista);

            //Imprimo size
            System.out.println(lista.__getattr__("size").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());

            //imprimo count
            System.out.println(lista.__getattr__("count").__call__(new PyObject[]{lista}, PySingletons.kwargsVacios, null).print());

            //imprimo elemento por indice
            System.out.println(lista.__get_index__(new PyInteger(0)).print());
            System.out.println(lista.__get_index__(new PyLong(2)).print());
            System.out.println(lista.__get_index__(PySingletons.True).print());
            System.out.println(lista.__get_index__(new PyInteger(4)).print());

            System.out.println("\t---");
            //pruebo iteracion
            PyIterator iter = (PyIterator) lista.__iter__();

            while(iter.iterador.hasNext()){
                System.out.println(iter.iterador.next().print());
            }

            PyList lista2 = new PyList();

            //Le agrego algunos elementos llamando la funcion append
            lista2.__getattr__("append").__call__(new PyObject[]{s}, PySingletons.kwargsVacios, null);
            lista2.__getattr__("append").__call__(new PyObject[]{s}, PySingletons.kwargsVacios, null);
            lista2.__getattr__("append").__call__(new PyObject[]{lista}, PySingletons.kwargsVacios, null);
            lista.__getattr__("append").__call__(new PyObject[]{lista2}, PySingletons.kwargsVacios, null);

            System.out.println(lista2.print());


        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("--------------- TEST TUPLAS ----------------------");


        try {
            ArrayList<PyObject> datosTupla = new ArrayList<>();
            datosTupla.add(s);
            datosTupla.add(o);
            datosTupla.add(l);
            datosTupla.add(raw_input);
            datosTupla.add(new PyList());
            datosTupla.add(new PyTuple());
            PyTuple tupla = new PyTuple(datosTupla);



            //Imprimo size
            System.out.println(tupla.__getattr__("size").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios, null).print());


            //imprimo elemento por indice
            System.out.println(tupla.__get_index__(new PyInteger(0)).print());
            System.out.println(tupla.__get_index__(new PyLong(2)).print());
            System.out.println(tupla.__get_index__(PySingletons.True).print());
            System.out.println(tupla.__get_index__(new PyLong(-1)).print());
            System.out.println(tupla.print());

            System.out.println("\t---");
            //pruebo iteracion
            PyIterator iter = (PyIterator) tupla.__iter__();

            while(iter.iterador.hasNext()){
                System.out.println(iter.iterador.next().print());
            }



        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("--------------- TEST DICT ----------------------");


        try {
            PyDict dict = new PyDict();

            dict.__set_index__(s,s);
            dict.__set_index__(o,o);
            dict.__set_index__(raw_input,raw_input);
            dict.__set_index__(new PyString("Clave de prueba"),new PyList());
            dict.__set_index__(new PyString("Clave de prueba2"),new PyTuple());
            dict.__set_index__(new PyString("Clave de prueba3"),new PyDict());
            dict.__set_index__(new PyString("booleano"),PySingletons.True);
            dict.__set_index__(PySingletons.False,PySingletons.True);
            dict.__set_index__(PySingletons.False,PySingletons.False);
            dict.__set_index__(PySingletons.None,new PyString("None como clave"));


            //imprimo elemento por indice
            System.out.println(dict.__get_index__(s).print());
            System.out.println(dict.__get_index__(o).print());
            System.out.println(dict.__get_index__(raw_input).print());
            System.out.println(dict.__get_index__(new PyString("Clave de prueba")).print());
            System.out.println(dict.__get_index__(new PyString("Clave de prueba2")).print());
            System.out.println(dict.__get_index__(new PyString("Clave de prueba3")).print());
            System.out.println(dict.__get_index__(PySingletons.False).print());
            System.out.println(dict.__get_index__(PySingletons.None).print());
            System.out.println(dict.print());

            System.out.println("\t---");
            //pruebo iteracion
            PyIterator iter = (PyIterator) dict.__iter__();

            while(iter.iterador.hasNext()){
                System.out.println(iter.iterador.next().print());
            }


            //deberia explotar
            try{
                System.out.println(dict.__get_index__(new PyString("Clave que no existe")).print());
            }
            catch (Throwable t){
                System.out.println(t.getMessage());
            }

            //deberia explotar
            try{
                dict.__set_index__(dict,PySingletons.False);
            }
            catch (Throwable t){
                System.out.println(t.getMessage());
            }
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("--------------- TEST FUNCIONES STRING ----------------------");


        try {

            PyString str1 = new PyString("holaholahola");
            PyString str2 = new PyString("la");
            PyString str3 = new PyString("ho");
            PyString sep = new PyString(", ");



            System.out.println(str1.__getattr__("find").__call__(new PyObject[]{str2}, PySingletons.kwargsVacios,null).print());
            System.out.println(str1.__getattr__("find").__call__(new PyObject[]{str2, new PyInteger(2)}, PySingletons.kwargsVacios,null).print());


            PyList lista = new PyList();
            lista.lista.add(new PyString("str1"));
            lista.lista.add(new PyString("str2"));
            lista.lista.add(new PyString("str3"));
            lista.lista.add(new PyString("str4"));
            lista.lista.add(new PyString("str5"));
            lista.lista.add(new PyString("str6"));
            //lista.lista.add(new PyInteger(2));

            System.out.println(sep.__getattr__("join").__call__(new PyObject[]{lista}, PySingletons.kwargsVacios, null).print());

            System.out.println(str1.__getattr__("split").__call__(new PyObject[]{str2}, PySingletons.kwargsVacios,null).print());

            System.out.println(str1.__getattr__("replace").__call__(new PyObject[]{str3, str2}, PySingletons.kwargsVacios,null).print());

            System.out.println(str1.__getattr__("length").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios,null).print());


        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }



        System.out.println("--------------- TEST FUNCIONES DICT ----------------------");


        try {

            PyDict dict = new PyDict();

            dict.__set_index__(s,s);
            dict.__set_index__(o,o);
            dict.__set_index__(raw_input,raw_input);
            dict.__set_index__(new PyString("Clave de prueba"),new PyList());
            dict.__set_index__(new PyString("Clave de prueba2"),new PyTuple());
            dict.__set_index__(new PyString("Clave de prueba3"),new PyDict());
            dict.__set_index__(new PyString("booleano"),PySingletons.True);

            PyString claveNoValida = new PyString("caca");

            System.out.println(dict.__getattr__("has_key").__call__(new PyObject[]{s}, PySingletons.kwargsVacios,null).print());
            System.out.println(dict.__getattr__("has_key").__call__(new PyObject[]{PySingletons.None}, PySingletons.kwargsVacios,null).print());
            System.out.println(dict.__getattr__("has_key").__call__(new PyObject[]{claveNoValida}, PySingletons.kwargsVacios,null).print());

            System.out.println(dict.__getattr__("items").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios,null).print());
            System.out.println(dict.__getattr__("items").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios,null).__get_index__(new PyInteger(3)).print());

            System.out.println(dict.__getattr__("keys").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios,null).print());
            System.out.println(dict.__getattr__("values").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios,null).print());

            System.out.println(dict.__getattr__("length").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios,null).print());

            System.out.println(dict.__getattr__("pop").__call__(new PyObject[]{new PyString("Clave de prueba2")}, PySingletons.kwargsVacios,null).print());

            //Deberia explotar pq ya le hice pop
            try{
                System.out.println(dict.__getattr__("pop").__call__(new PyObject[]{new PyString("Clave de prueba2")}, PySingletons.kwargsVacios,null).print());
            }
            catch (Throwable t){
                System.out.println(t.getMessage());
            }
            System.out.println(dict.__getattr__("length").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios,null).print());

        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }



        System.out.println("--------------- TEST FUNCIONES LISTA ----------------------");


        try {

            PyList lista = new PyList();

            //Le agrego algunos elementos llamando la funcion append
            lista.__getattr__("append").__call__(new PyObject[]{s}, PySingletons.kwargsVacios, null);
            lista.__getattr__("append").__call__(new PyObject[]{s}, PySingletons.kwargsVacios, null);
            lista.__getattr__("append").__call__(new PyObject[]{o}, PySingletons.kwargsVacios, null);
            lista.__getattr__("append").__call__(new PyObject[]{lista}, PySingletons.kwargsVacios, null);
            PySingletons.list.__getattr__("append").__call__(new PyObject[]{lista, PySingletons.True}, PySingletons.kwargsVacios, null);



            System.out.println(lista.__getattr__("count").__call__(new PyObject[]{lista}, PySingletons.kwargsVacios,null).print());
            System.out.println(lista.__getattr__("size").__call__(new PyObject[]{}, PySingletons.kwargsVacios,null).print());
            System.out.println(lista.__getattr__("extend").__call__(new PyObject[]{lista}, PySingletons.kwargsVacios,null).print());
            System.out.println(lista.__getattr__("size").__call__(new PyObject[]{}, PySingletons.kwargsVacios,null).print());
            System.out.println(lista.print());

            System.out.println(lista.__getattr__("index").__call__(new PyObject[]{lista}, PySingletons.kwargsVacios,null).print());
            System.out.println(lista.__getattr__("index").__call__(new PyObject[]{s}, PySingletons.kwargsVacios,null).print());
            System.out.println(lista.__getattr__("index").__call__(new PyObject[]{s, new PyInteger(6)}, PySingletons.kwargsVacios,null).print());

            //deberia explotar
            try{
                System.out.println(lista.__getattr__("index").__call__(new PyObject[]{s, new PyInteger(7)}, PySingletons.kwargsVacios,null).print());
            }
            catch (Throwable t){
                System.out.println(t.getMessage());
            }

            //deberia explotar
            try{
                System.out.println(lista.__getattr__("index").__call__(new PyObject[]{s, new PyInteger(546)}, PySingletons.kwargsVacios,null).print());
            }
            catch (Throwable t){
                System.out.println(t.getMessage());
            }


            System.out.println(lista.__getattr__("insert").__call__(new PyObject[]{new PyInteger(0), new PyString("ZERO")}, PySingletons.kwargsVacios,null).print());
            System.out.println(lista.__get_index__(new PyInteger(0)).print());

            //deberia explotar
            try{
                System.out.println(lista.__getattr__("insert").__call__(new PyObject[]{ new PyString("ZERO"), new PyString("ZERO")}, PySingletons.kwargsVacios,null).print());
            }
            catch (Throwable t){
                System.out.println(t.getMessage());
            }

            System.out.println(lista.__getattr__("size").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios,null).print());
            System.out.println(lista.__getattr__("pop").__call__(new PyObject[]{new PyInteger(0)}, PySingletons.kwargsVacios, null).print());
            System.out.println(lista.__getattr__("pop").__call__(new PyObject[]{new PyInteger(5)}, PySingletons.kwargsVacios,null).print());
            System.out.println(lista.__getattr__("size").__call__(PySingletons.argsVacios, PySingletons.kwargsVacios,null).print());

            System.out.println(lista.print());

            System.out.println(lista.__get_index__(new PyLong(-1)).print());
            System.out.println(lista.__get_index__(new PyInteger(-2)).print());
            System.out.println(lista.__get_index__(new PyInteger(-3)).print());
            System.out.println(lista.__get_index__(new PyInteger(-4)).print());
            System.out.println(lista.__get_index__(new PyInteger(-5)).print());
            System.out.println(lista.__get_index__(new PyInteger(-6)).print());
            System.out.println(lista.__get_index__(new PyInteger(-7)).print());
            System.out.println(lista.__get_index__(new PyInteger(-8)).print());

            //deberia explotar
            try{
                System.out.println(lista.__get_index__(new PyInteger(-9)).print());
            }
            catch (Throwable t){
                System.out.println(t.getMessage());
            }

        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }



        System.out.println("--------------- BUILTINS TEST ----------------------");
        try {

            PyObject result = PySingletons.raw_input.__call__(new PyObject[]{new PyString("Prueba de entrada: ")}, PySingletons.kwargsVacios,null);
            System.out.println(result.print());
            System.out.println(PySingletons.__hash__.__call__(new PyObject[]{result}, PySingletons.kwargsVacios,null).print());
            System.out.println(PySingletons.__hash__.__call__(new PyObject[]{new PyInteger(2)}, PySingletons.kwargsVacios,null).print());
            System.out.println(PySingletons.__hash__.__call__(new PyObject[]{new PyString("Prueba de entrada: ")}, PySingletons.kwargsVacios,null).print());
            System.out.println(PySingletons.__hash__.__call__(new PyObject[]{new PyString("Prueba de entrada: ")}, PySingletons.kwargsVacios,null).print());
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("--------------- TEST STRING ----------------------");
        try {

            PyObject str = new PyString("hola\nmundo");
            System.out.println(str.__repr__().value);
            System.out.println(str.print());
        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


        System.out.println("--------------- TEST SLICE ----------------------");
        try {

            System.out.println(new PySlice().print());
            System.out.println(new PySlice(new PyInteger(2), PySingletons.None, PySingletons.None).print());
            System.out.println(new PySlice(PySingletons.None, new PyInteger(2), PySingletons.None).print());
            System.out.println(new PySlice(PySingletons.None, PySingletons.None, new PyInteger(2)).print());
            System.out.println(new PySlice(new PyInteger(2), new PyInteger(2), new PyInteger(2)).print());

            System.out.println(new PyString("Hola carola").__get_index__(new PySlice(new PyInteger(1), PySingletons.None, new PyInteger(2))).print());

            ArrayList<PyObject> testLista = new ArrayList<>();
            testLista.add(new PyInteger(1));
            testLista.add(new PyInteger(2));
            testLista.add(new PyInteger(3));
            testLista.add(new PyInteger(4));

            System.out.println(new PyList(testLista).__get_index__(new PySlice(new PyInteger(1), PySingletons.None, new PyInteger(1))).print());

            ArrayList<PyObject> testLista2 = new ArrayList<>();
            testLista2.add(new PyString("a"));
            testLista2.add(new PyString("b"));
            testLista2.add(new PyString("c"));
            testLista2.add(new PyString("d"));

            System.out.println("\t---- asignacion ----");
            PyList plist = new PyList(testLista);
            System.out.println(plist.print());
            plist.__set_index__(new PySlice(new PyInteger(0), PySingletons.None, new PyInteger(1)), new PyTuple(testLista2));
            System.out.println(plist.print());

        }
        catch (Throwable t){
            System.out.println(t.getMessage());
        }


    }
}
