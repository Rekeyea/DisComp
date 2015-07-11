package com.pyjava.core;

import com.pyjava.core.exceptions.PyAttributeError;
import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyTypeError;
import com.pyjava.core.runtime.Estado;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristiano on 16/06/2015.
 *
 * Clase muy especial, siguiendo la implementacion de CPython, en realidad una meta clase. Todas las clases existentes deberan ser instancia de esta clase.
 * Esta clase a su vez almacenara una referencia a la clase que almacena, y sera la encargada de instanciar esas clases cuando se llama al metodo __call__ desde "afuera"
 * A su vez mantiene un diccionario de todas las clases creadas por esta, para poder acceder a ellas por su nombre cuando sea necesario.
 *
 * Esta clase no debe ser instanciada manualmente, se instancia a si misma al referenciarla por primera vez. Y luego define un metodo para crear otras clases
 * a las cuales se les asignara esta instancia como su clase, ya que todas las clases definidas tendran a esta como su meta clase.
 */

public class PyType extends PyObject {

    public static final String __name__ = "type"; //Mantengo nombres en minuscula para estas clases como python


    /**
     * Clase que almacena esta 'metaclase' type. La cual va a utilizar para crear instancias y demas.
     */
    public Class claseAlmacenada = null;
    public String claseAlmacenadaName = null;
    protected Method claseAlmacenadaNew = null;

    //Lista de clases base. Solo las metaclases tienen esta informacion.
    public List<PyType> __bases__ = new ArrayList<PyType>();

    /**
     * El constructor de type por defecto, crea un objeto sin su respectiva clase, es responsabilidad del que lo construye asignarle la clase correcta.
     * Si no explota todo. Solo deberia llamarse desde PySingletons para instanciarlo por primera vez.
     */
    public PyType(){
        super(null);
    }

    /**
     * Constructor de instancias PyType con clase asignada, solo para uso interno.
     */
    private PyType(PyType clase) {
        super(clase);
    }


    /**
     * Crea una nueva clase, tal que es instancia de este type. Y su clase almacenada es clase.
     * Esto es, crea una nueva clase, donde su meta clase es .this
     * Ademas, agrega la nueva clase al mapa de clases creadas por esta metaclase.
     * La clase a agregar debe implementar __new__ e __init__
     * Los builtins de la clase deben cargarse luego de que todas las clases hayan sido creadas.
     */
    public PyType crearClase(String nombre, Class clase, List<PyType> bases) throws PyException {
        PyType r =  new PyType(this);
        r.claseAlmacenada = clase;
        r.claseAlmacenadaName = nombre;

        try {
            r.claseAlmacenadaNew = clase.getMethod("__new__", PyObject[].class, AttrDict.class);
        } catch (NoSuchMethodException e) {
            throw new PyTypeError(String.format("Error al crear clase '%s'",nombre));
        }

        if(bases != null) {
            for (PyType t : bases) {
                r.__bases__.add(t);
            }
        }

        PySingletons.types.put(nombre, r);

        return r;
    }

    /**
     * Devuelve una instancia de la clase almacenada. Creando el objeto llamando a __new__ e __init__ de la clase creada.
     */
    public PyObject instanciarClase(PyObject[] args, AttrDict kwargs) throws PyException {

        //Por ahora no se permite instanciar clases de type.
        if(claseAlmacenada == PyType.class) {
            throw new PyTypeError("No se puede instanciar clases de este tipo.");
        }

        try {
            PyObject r = (PyObject)claseAlmacenadaNew.invoke(null,args, kwargs);
            r.__init__(args,kwargs);

            return  r;
        }

        //Como uso reflection solo puedo obtener la excepcion real de esta forma.
        catch (InvocationTargetException e){
            try {
                throw e.getTargetException();
            }
            catch (PyException er) {
                throw er;
            }
            catch (Throwable t){
                throw new PyException(String.format("Error al instanciar clase '%s'.", claseAlmacenadaName));
            }
        }
        catch(Throwable t){
            System.out.println(t.getMessage());
            throw new PyException(String.format("Error al instanciar clase '%s'.", claseAlmacenadaName));
        }
    }

    /**
     * Devuelve el nombre 'string' de la clase a la que hace referencia este type. Se obtiene del campo __name__ de la clase.
     */
    public String getClassName() {
        return claseAlmacenadaName;
    }


    /**
     * Se llama a la funcion. Se utiliza principalmente para instanciar clases.
     * Sin embargo, si la clase de este type es justamente type, actua como funcion y retorna el objeto type del argumento.
     */
    @Override
    public PyObject __call__(PyObject[] args, AttrDict kwargs, Estado estado) throws PyException{

        //si es una instancia de Type, actua como funcion y devuelve el tipo del objeto.
        //Si no, crea una instancia de la clase almacenada.

        if(claseAlmacenada == PyType.class) {
            if ( args.length < 1) {
                throw new PyTypeError(String.format("'%s' necesita 1 argumento.", getClassName()));
            } else {
                return args[0].getType();
            }
        }
        else{

            //Instancio clase
            return this.instanciarClase(args,kwargs);

        }
    }

    @Override
    public PyString __repr__() throws PyException{
        //print por defecto
        return new PyString(String.format("<%s '%s'>", __name__, getClassName()));
    }

    /**
     * Sobreescribe para cambiar el mensaje de error.
     * Ademas en este caso, no crea PyMethodWrapper ya que si se llama desde aca quiere decir que se llamo desde una clase y no una instancia.
     */
    @Override
    public PyObject __getattr__(String key) throws PyException {

        PyObject r = __dict__.get(key);
        if(r != null){
            return r;
        }

        PyType clase = getType();

        r = clase.__dict__.get(key);
        if(r != null){
            return r;
        }

        for(PyType padre : clase.__bases__){
            r = padre.__dict__.get(key);
            if(r != null){
                return r;
            }
        }

        throw new PyAttributeError(String.format("%s '%s' no tiene atributo '%s'", __name__, getClassName(), key));
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
            }
            return builtins;

        }

    }

}
