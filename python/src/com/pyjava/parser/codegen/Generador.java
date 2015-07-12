package com.pyjava.parser.codegen;

import com.pyjava.core.PyObject;
import com.pyjava.core.runtime.Code;
import com.pyjava.core.runtime.Instruccion;
import com.pyjava.core.runtime.OpCode;

import java.util.*;

/**
 * Created by Cristiano on 11/07/2015.
 *
 */
public class Generador {


    public HashMap<String, Name> co_names;          //Diccionario de nombres
    public HashMap<PyObject, Const> co_consts;      //Diccionario de constantes

    public Generador(){
        co_names = new HashMap<>();
        co_consts = new HashMap<>();
    }

    /**
     * Crea y retorna una nueva constante
     */
    public Const createConst(PyObject c){

        Const res = co_consts.get(c);
        if(res == null){
            res = new Const(co_consts.size(),c);
        }

        return res;
    }



    /**
     * Crea un nuevo nombre
     */
    public Name createName(String n){

        Name res = co_names.get(n);
        if(res == null){
            res = new Name(co_names.size(),n);
        }

        return res;

    }

    /**
     * Crea un nuevo bloque a partir de instrucciones y otro bloque.
     * Usar null para valores que no se quieran utilizar.
     */
    public Bloque crearBloque(LinkedList<Instruccion> preInstr, Bloque bloque, LinkedList<Instruccion> postInstr){

        Bloque res = bloque;
        if(res == null){
            res = new Bloque();
        }

        if (preInstr != null){
            for(Instruccion i : preInstr){
                res.instrucciones.addFirst(i);
            }

        }

        if (postInstr != null){
            for(Instruccion i : preInstr){
                res.instrucciones.addLast(i);
            }

        }

        return res;

    }

    /**
     * Devuelve una instancia de Code lista para usar por el interprete
     * @param name nombre del modulo
     * @param fileName ruta del archivo
     * @return
     */
    public Code crearCodigo(String name, String fileName, Bloque bloque){
        Code res = new Code(name, fileName);

        //Crear co_names
        String[] names = new String[co_names.size()];
        for(Name n : co_names.values()){
            names[n.index] = n.value;
        }

        PyObject[] consts = new PyObject[co_consts.size()];
        for(Const c : co_consts.values()){
            consts[c.index] = c.value;
        }

        res.co_names = new ArrayList<String>(Arrays.asList(names));
        res.co_consts = new ArrayList<PyObject>(Arrays.asList(consts));
        res.co_code = new ArrayList<Instruccion>(bloque.instrucciones);
        res.co_code.add(new Instruccion(res.co_code.size()+1, OpCode.FIN_EJECUCION, 0));

        return res;

    }

}
