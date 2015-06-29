package com.pyjava.core;

import com.pyjava.core.exceptions.PyAttributeError;
import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyTypeError;
import com.pyjava.core.runtime.Estado;

/**
 * Created by Cristiano on 16/06/2015.
 *
 * Clase raiz de todas las posibles clases del lenguaje.
 * Todas las clases hijas deberan definir __name__ y __new__ y opcionalmente hacer override de otros metodos.
 * Todas las clases hijas deberian definir su respectiva clase con builtins (ver abajo del todo)
 *  *
 *
 * Todas las instancias creadas deberan setear correctamente su referencia a __class__ y opcionalmente metodos en __dict__
 * __class__ se setea automaticamente si no se llama al constructor no por defecto de object.
 *
 * Por otro lado, todo lo que reciba args o kwargs, en caso de mandar vacio, debera utilizar PySingletons.argsVacios y PySingletons.kwargsVacios
 * para evitar comparar siempre por null.
 */
public class PyObject {

    public static final String __name__ = "object";


    public PyType __class__ = null;
    public AttrDict __dict__ = new AttrDict();

    public PyObject(){
        try {
            String className = (String)getClass().getField("__name__").get(null); //obtiene el nombre de la clase, __name__ static, con reflection.
            this.__class__ = PySingletons.types.get(className);
            if (this.__class__ == null){
                throw new Exception();
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("ERROR FATAL: Clase instanciada, pero no se encontro su 'clase' python.");
        }
    }

    /**
     * Construcotr especial el cual asigna la clase en vez de obtenerla automaticamente del singleton.
     */
    public PyObject(PyType clase){
        this.__class__ = clase;
    }



    /**
     * Constructor que debe utilizarse, setea automaticamente la clase del objeto o la crea en caso de no existir.
     * name es el nombre de la clase a utilizar.
     */
    /**
    public PyObject(String name){
        this.__class__ = PySingletons.types.get(name);

        //Si no se encuentra la clase pasada, error fatal.
        if (this.__class__ == null){
            throw new RuntimeException("ERROR FATAL: Clase instanciada, pero no se encontro su 'clase' python: " + name);
        }
    }
     **/


    /**
     * Devuelve la instancia de la clase de este objeto. O sea, su meta clase.
     * @return
     * @throws PyException
     */
    public PyType getType(){
        if(this.__class__ == null){
            throw new RuntimeException("ERROR FATAL: Clase sin tipo o clase python: " + getClass().toString());
        }
        return this.__class__;
    }

    /**
     * Construye una instancia y la retorna.
     *
     */
    public static PyObject __new__(PyObject[] args, AttrDict kwargs) throws PyException {

        //Constructor de object no debe recibir ningun argumento
        if(args.length > 0 || kwargs.getCount() > 0){
            throw new PyTypeError(String.format("constructor de '%s' no debe recibir ningun parametro.", __name__));
        }

        return new PyObject();
    }


    /**
     * Initializer que segun argumentos, realiza acciones.
     * @param args
     * @param kwargs
     */
    public void __init__(PyObject[] args, AttrDict kwargs) throws PyException{
    }

    /**
     * Funcion a llamar al invocar un objeto. Algunos podran ser invocados, y otros no.
     * Debera retornar null si es una funcion que modifica el frame actual.
     * Si es una funcion que necesita acceso al frame actual, la instancia de estado debera ser pasada.
     */
    public PyObject __call__(PyObject[] args, AttrDict kwargs, Estado estado) throws PyException{
        throw new PyTypeError( String.format("instancia de '%s' no es llamable.", getType().getClassName()));
    }


    /**
     * Metodo de conveniencia para imprimir un objeto: llama a __str__ del objeto y retorna su valor.
     * @return
     */
    public String print() throws PyException{
        //print por defecto
        return this.__str__().value;
    }


    /**
     * Retorna un atributo del objeto. Si no lo encuentra, lo busca en su clase, y sus clases 'padre' (clase python, o tipo). Si no error.
     * Si el resultado es una instancia de PyNativeFunction, en vez de retornarla, retorna un PyMethodWrapper para que pueda ser llamada con el 'this' pasado automaticamente.
     *      Esto unicamente, si el resultado se obtuvo de la clase o padres, y no de la instancia misma.
     *
     * @param key
     * @return
     */
    public PyObject __getattr__(String key) throws PyException {
        PyObject r = __dict__.get(key);
        if(r != null){
            return r;
        }

        PyType clase = getType();
        r = clase.__dict__.get(key);
        if(r == null){
            for(PyType padre : clase.__bases__){
                r = padre.__dict__.get(key);
                if(r != null){
                    break;
                }
            }
        }

        if(r == null) {
            throw new PyAttributeError(String.format("instancia de '%s' no tiene atributo '%s'", clase.getClassName(), key));
        }

        if(r instanceof PyNativeFunction){
            r = new PyMethodWrapper(this,r);
        }

        return r;

    }


    //__setattr__ no se define, no es necesaria ya que no vamos a permitir crear clases ni asignar atributos.

    //**** Definimos __repr__ y __str__ para que retornen sus clases reales (PyString) ya que siempre deben devolver eso.
    //**** Los otros metodos, devuelven PyObject ya que pueden o no retornar el tipo de su operacion, por ej, __int__ podria devolver PyInteger o PyLong
    //**** Como estas funciones son implementadas por nosotros, estamos seguros (o nos comprometemos) a retornar siempre tipos correctos.
    //**** Y funciones que necesiten de un tipo especifico debera castearlo al que lo necesite y validarlo.

    /**
     * Retora un PyString con la "representacion" de un objeto. Es similar a __str__ pero se supone que devuelve una representacion
     * no ambigua de un objeto, orientada al programador y no al usuario final.
     * Por defecto esto es lo que se utiliza para obtener el PyString de un objeto, sub clases pueden optar por implementar este, __str__ o ninguno.
     * Utiliza getType() y no __name__ para poder ser llamada por clases hijas y poder obtener su clase correcta.
     * @return
     */
    public PyString __repr__() throws PyException{
        return new PyString(String.format("<instancia de '%s'>", getType().getClassName()));
    }

    /**
     * Crea una instancia de PyStr, puede o no ser modificada en clases hijo.
     * Por defecto crea un string a partir del resultado de this.print()
     * @return
     * @throws PyException
     */
    public PyString __str__() throws PyException{
        //Por defecto el string de un objeto es su representacion, clases pueden optar o no por modificar la representacion del objeto.
        return this.__repr__();

    }

    /**
     * Devuelve un hash del objeto. Utilizado para las claves de diccionarios y demas.
     * IMPORTANTE: Para clases que no se quiera permitir como clave de diccionarios, LANZAR EXCEPCION. No permitir hash.
     * @return
     */
    public PyInteger __hash__() throws PyException{
        //POR AHORA DEVUELVO EL HASH DE JAVA... Sera suficiente?
        return new PyInteger(this.hashCode());
    }


    //************* Operadores de conversion ****************

    /**
     * Castea un objeto a int, object lanza error, pero puede ser implementado por otras clases.
     * Puede devolver PyInteger o PyLong dependiendo del tamano del valor.
     * @return
     */
    public PyObject __int__() throws PyException{
        throw new PyTypeError(String.format("'%s' no se puede convertir a %s", getType().getClassName(), PyInteger.__name__));
    }

    /**
     * Castea un objeto a long, object lanza error, pero puede ser implementado por otras clases.
     * @return
     */
    public PyObject __long__() throws PyException{
        throw new PyTypeError(String.format("'%s' no se puede convertir a %s", getType().getClassName(), PyLong.__name__));
    }

    /**
     * Castea un objeto a float, object lanza error, pero puede ser implementado por otras clases.
     * @return
     */
    public PyObject __float__() throws PyException{
        throw new PyTypeError(String.format("'%s' no se puede convertir a %s", getType().getClassName(), PyFloat.__name__));
    }

    /**
     * Castea un objeto a bool, por defecto evalua en TRUE, debe re implementarse en clase hijas que puedan evaluar en FALSE.
     * Es importante que todas las funciones retornen el singleton de True o False de bool.
     * @return
     */
    public PyBool __bool__() throws PyException{
        return PySingletons.True;
    }


    //********** Operadores Aritmeticos *************
    //**** Todas deberan promover el resultado a otro tipo, en caso de tener tipos distintos:
    // int > long > float, bool es siempre int.

    /**
     * suma
     */
    public PyObject __add__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary("+", this, obj);
    }

    /**
     * negacion, o sea multiplicar * -1. No es negacion logica.
     */
    public PyObject __neg__() throws PyException{
        throw AritmeticaHelper.getErrorUnary("-", this);
    }

    /**
     * resta, utiliza suma y negacion.
     */
    public PyObject __sub__(PyObject obj) throws PyException{
        try{
            return this.__add__(obj.__neg__());
        }
        catch (PyTypeError e ){
            throw AritmeticaHelper.getErrorBinary("-", this, obj);
        }
    }


    /**
     * Multiplicacion
     */
    public PyObject __mul__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary("*", this, obj);
    }

    /**
     * exponente o potencia
     */
    public PyObject __pow__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary("**", this, obj);
    }

    /**
     * division
     */
    public PyObject __div__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary("/", this, obj);
    }

    /**
     * division entera. Funciona igual que el anterior, pero siempre se trunca el valor en caso de ser float.
     * Se define aca ya que todos los numeros van a hacer lo mismo.
     */
    public PyObject __int_div__(PyObject obj) throws PyException{

        try {
            //Para facilitarla, simplemente hago la division, y trunco el resultado en caso de que no sea entero.
            PyObject r = this.__div__(obj);

            if (r instanceof PyFloat) {
                PyFloat f = (PyFloat) r;

                f.value = Math.floor(f.value);

            }

            return r;
        }
        catch (PyTypeError e){
            throw AritmeticaHelper.getErrorBinary("//", this, obj);
        }
    }

    /**
     * Modulo
     */
    public PyObject __mod__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary("%", this, obj);
    }


    /*********** Operadores bit wise ***********
     * Solo para int, long y bool. No se permite float.
     * Tambien se promueve de int a long.
     */

    /**
     * Binary and ( & )
     */
    public PyObject __band__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary("&", this, obj);
    }

    /**
     * Binary not ( ~ )
     */
    public PyObject __bnot__() throws PyException{
        throw AritmeticaHelper.getErrorUnary("~", this);
    }


    /**
     * Binary or ( | )
     */
    public PyObject __bor__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary("|", this, obj);
    }


    /**
     * Binary xor ( ^ )
     */
    public PyObject __bxor__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary("^", this, obj);
    }



    /**
     * Shift left ( << )
     */
    public PyObject __sleft__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary("<<", this, obj);
    }

    /**
     * Shift right ( >> )
     */
    public PyObject __sright__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary(">>", this, obj);
    }


    //****** Operadores booleanos , retornan PyBool ********
    //El de igualdad por defecto, comparara referencia de objetos. Queda a implementacion de cada clase implementar la igualdad correcta.

    /**
     * igualdad
     */
    public PyBool __eq__(PyObject obj) throws PyException{
        if(this == obj){
            return PySingletons.True;
        }
        return PySingletons.False;
    }

    /**
     * Igualdad por referencia, no deberia ser necesario porque no estamos implementando el "is". Pero puede ser util.     *
     */
    public PyBool __ref_eq__(PyObject obj) throws PyException{
        if(this == obj){
            return PySingletons.True;
        }
        return PySingletons.False;
    }


    /**
     * desigualdad, compara con not eq
     */
    public PyBool __not_eq__(PyObject obj) throws PyException{
        if(this.__eq__(obj).value){
            return PySingletons.False;
        }
        return PySingletons.True;
    }

    /**
     * Mayor
     */
    public PyBool __gt__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary(">", this, obj);
    }

    /**
     * Menor
     */
    public PyBool __lt__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary("<", this, obj);

    }

    /**
     * Mayor igual
     */
    public PyBool __ge__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary(">=", this, obj);
    }

    /**
     * Menor igual
     */
    public PyBool __le__(PyObject obj) throws PyException{
        throw AritmeticaHelper.getErrorBinary("<=", this, obj);
    }





    /**
     * Clase interna para manejar definicion de builtins.
     */
    public static class Builtins{
        private static AttrDict builtins = null; //Sera singleton, para no instanciar builtins de esta clase muchas veces

        /**
         * Devuelve todos los builtin que va a tener esta clase.
         * NOTA: Estos son los builtin de la clase, y no instancia.
         */
        public static AttrDict getBuiltins() {

            if (builtins == null){
                builtins = new AttrDict();

                //Obtengo referencia a la clase, razon por la cual esto debe llamarse solo una vez instanciado todas las clases.
                PyType clase = PySingletons.types.get(__name__);

                //Devuelve el valor del hash de la instancia parametro, invocando a la funcion __hash__ del objeto.
                //Mas que nada para probar.
                PyNativeFunction hash = new PyNativeFunction("__hash__", clase,
                        new PyCallable() {
                            @Override
                            public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                                if (args.length == 0) {
                                    throw new PyTypeError(String.format("__hash__ necesita 1 argumento, 0 encontrados."));
                                }
                                else if(args.length == 1) {
                                    return args[0].__hash__();
                                }
                                else {
                                    throw new PyTypeError(String.format("__hash__ necesita 1 argumento, %s encontrados.", args.length));
                                }
                            }
                        }
                );

                builtins.put(hash.funcionNativaNombre, hash);

                //Llama a la funcion __repr__ de los objetos.
                PyNativeFunction repr = new PyNativeFunction("__repr__", clase,
                        new PyCallable() {
                            @Override
                            public PyObject invoke(PyObject[] args, AttrDict kwargs) throws PyException {
                                if (args.length == 0) {
                                    throw new PyTypeError(String.format("__repr__ necesita 1 argumento, 0 encontrados."));
                                }
                                else if(args.length == 1) {
                                    return args[0].__repr__();
                                }
                                else {
                                    throw new PyTypeError(String.format("__repr__ necesita 1 argumento, %s encontrados.", args.length));
                                }
                            }
                        }
                );

                builtins.put(repr.funcionNativaNombre, repr);

            }
            return builtins;

        }

    }
}


