package com.pyjava.core.runtime;

import com.pyjava.core.*;
import com.pyjava.core.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Cristiano on 28/06/2015.
 *
 * Clase responsable de almacenar el estado de ejecucion actual y procesar instrucciones.
 */
public class Estado {

    /**
     * Frame que esta siendo interpretado actualmente
     */
    public Frame frameActual;

    /**
     * Funciones/clases/objetos 'builtins' o incorporadas por defecto.
     */
    public HashMap<String, PyObject> builtins = new HashMap<String,PyObject>();

    public Estado(Frame frameActual){

        this.frameActual = frameActual;

        //Cargo datos en builtins.

        //Clases que pueden ser utilizadas
        builtins.put(PyType.__name__, PySingletons.type);
        builtins.put(PyObject.__name__, PySingletons.object);
        builtins.put(PyInteger.__name__, PySingletons.integer);
        builtins.put(PyLong.__name__, PySingletons.longint);
        builtins.put(PyFloat.__name__, PySingletons.pfloat);
        builtins.put(PyBool.__name__, PySingletons.bool);
        builtins.put(PyList.__name__, PySingletons.list);
        builtins.put(PyDict.__name__, PySingletons.dict);
        builtins.put(PyTuple.__name__, PySingletons.tuple);
        builtins.put(PyString.__name__, PySingletons.string);
        builtins.put(PySingletons.raw_input.funcionNativaNombre, PySingletons.raw_input);
        builtins.put(PySingletons.__hash__.funcionNativaNombre, PySingletons.__hash__);

        //singletons de objetos
        builtins.put("True", PySingletons.True);
        builtins.put("False", PySingletons.False);
        builtins.put("None", PySingletons.None);

        //funciones builtins que no son clases... Aca iria raw_input

    }


    /**
     * Retorna la linea actual de ejecucion, NO de instruccion, si no del archivo python.
     * @return
     */
    public int getLineaActual(){
        return frameActual.f_code.co_code.get(frameActual.f_instr).linea;
    }

    /**
     * Retorna el nombre del archivo de ejecucion actual
     * @return
     */
    public String getArchivoActual(){
        return frameActual.f_code.co_filename;
    }

    /**
     * Retorna el nombre de la funcion actual
     * @return
     */
    public String getFuncActual(){
        return frameActual.f_code.co_name;
    }

    /**
     * Imprime el trace
     */
    public void printStacktrace(){

        if(this.frameActual.f_back != null){
            this.printStacktrace();
        }

        System.out.println(String.format("\tArchivo: %s", this.getArchivoActual()));
        System.out.println(String.format("\tFuncion o modulo: %s", this.getFuncActual()));
        System.out.println(String.format("\tLinea: %s", this.getLineaActual()));
        System.out.println("-----");
    }

    /**
     * Interpreta la proxima instruccion, segun el frame actual y su f_instr.
     * Lanza excepciones. El llamador debera ser encargado de agarrarlas, y reportar el error, y lineas del error.
     */
    public void interpretarInstruccion() throws PyException {

        //Si frameActual es null, hay un error de implementacion, no lo controlo ya que se supone no debe pasar.
        try {
            Instruccion instr = frameActual.f_code.co_code.get(frameActual.f_instr);
            int instrCode = instr.instruccion;
            int instrArg = instr.arg;

            Stack<PyObject> stack = frameActual.f_stack;

            switch (instrCode) {

                case OpCode.CALL_FUNCTION: {

                    //Por ahora solo argumentos posicionales son validos, los kwargs se ignoran/no obtienen.

                    //Obtengo argumentos. Estan en el stack de derecha a izquierda.
                    PyObject[] args = new PyObject[instrArg];
                    AttrDict kwargs = new AttrDict();

                    for (int i = instrArg - 1; i >= 0; i--) {
                        args[i] = stack.pop();
                    }

                    //En este caso no importa el orden en que los inserto
                    for(int i = 0; i < instr.arg2; i++ ){
                        PyObject val = stack.pop();
                        PyObject key = stack.pop();
                        try{
                            kwargs.put(((PyString)key).value,val);
                        }
                        catch (ClassCastException e){
                            throw new PyRuntimeException("kwargs debe ser un PyString");
                        }

                    }

                    //Objeto a llamar esta ultimo en el stack.

                    PyObject callable = stack.pop();

                    PyObject res = callable.__call__(args, kwargs, this);

                    //Si obtuve resultado, quiere decir que se llamo a una funcion nativa o que no modifica el frame,
                    //Por lo que hago un push del resultado.
                    if (res != null) {
                        stack.push(res);
                        frameActual.f_instr += 1;
                    } else {
                        //En caso contrario, deberia continuar sin hacer nada, el __call__ de la funcion se encargo de modificar el frame.
                        //No implementado por ahora.
                    }

                    break;
                }

                case OpCode.RETURN_VALUE: {

                    //Obtengo el resultado del top del stack actual, y restauro estado del frame anterior.
                    //Si f_back del frame actual es null, hay un error critico del parser, no lo controlo ya que el parser deberia controlar que no haya returns en cosas que no sean funciones.
                    //Por otro lado, si el stack esta vacio, tambien es un error del parser, ya que siempre debe retornar algo, al menos None.

                    PyObject res = stack.pop();

                    //Restaura el frame anterior
                    frameActual = frameActual.f_back;

                    //Hace push del resultado
                    frameActual.f_stack.push(res);

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.LOAD_CONST: {

                    //Es un error del parser si se intenta obtener una constante que no existe, asi que no se chequea.
                    stack.push(frameActual.f_code.co_consts.get(instrArg));

                    frameActual.f_instr += 1;
                    break;
                }


                case OpCode.LOAD_NAME: {

                    //Es un error del parser si se intenta obtener un nombre fuera de rango, asi que no se chequea.
                    String name = frameActual.f_code.co_names.get(instrArg);

                    PyObject valor = frameActual.f_locals.get(name);

                    //si no lo encuentro en locals, busco en globals
                    if (valor == null) {
                        valor = frameActual.f_globals.get(name);
                    }

                    //Si no lo encuentro en globals, lo busco en builtins.
                    if (valor == null) {
                        valor = builtins.get(name);
                    }

                    //Si no lo encontre en ningun lado, error.
                    if (valor == null) {
                        throw new PyNameError(String.format("nombre '%s' no esta definido.", name));
                    }

                    stack.push(valor);

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.LOAD_ATTR: {

                    //Es un error del parser si se intenta obtener un nombre fuera de rango, asi que no se chequea.
                    String name = frameActual.f_code.co_names.get(instrArg);

                    //Lanza excepcion acorde si no existe el atributo.
                    PyObject valor = stack.pop().__getattr__(name);

                    stack.push(valor);

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.STORE_NAME: {
                    //Es un error del parser si se intenta obtener un nombre fuera de rango, asi que no se chequea.
                    String name = frameActual.f_code.co_names.get(instrArg);

                    frameActual.f_locals.put(name, stack.pop());

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.UNARY_INVERT: {

                    stack.push(stack.pop().__bnot__());

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.UNARY_NEGATIVE: {

                    stack.push(stack.pop().__neg__());

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.UNARY_NOT: {

                    PyBool val = PySingletons.True;
                    if (stack.pop().__bool__().value) {
                        val = PySingletons.False;
                    }
                    stack.push(val);

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.BINARY_POW: {

                    PyObject v2 = stack.pop();
                    PyObject v1 = stack.pop();

                    stack.push(v1.__pow__(v2));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.BINARY_MUL: {

                    PyObject v2 = stack.pop();
                    PyObject v1 = stack.pop();

                    stack.push(v1.__mul__(v2));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.BINARY_DIV: {

                    PyObject v2 = stack.pop();
                    PyObject v1 = stack.pop();

                    stack.push(v1.__div__(v2));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.BINARY_FLOOR_DIV: {

                    PyObject v2 = stack.pop();
                    PyObject v1 = stack.pop();

                    stack.push(v1.__int_div__(v2));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.BINARY_MOD: {

                    PyObject v2 = stack.pop();
                    PyObject v1 = stack.pop();

                    stack.push(v1.__mod__(v2));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.BINARY_ADD: {

                    PyObject v2 = stack.pop();
                    PyObject v1 = stack.pop();

                    stack.push(v1.__add__(v2));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.BINARY_SUB: {

                    PyObject v2 = stack.pop();
                    PyObject v1 = stack.pop();

                    stack.push(v1.__sub__(v2));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.BINARY_LSHIFT: {

                    PyObject v2 = stack.pop();
                    PyObject v1 = stack.pop();

                    stack.push(v1.__sleft__(v2));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.BINARY_RSHIFT: {

                    PyObject v2 = stack.pop();
                    PyObject v1 = stack.pop();

                    stack.push(v1.__sright__(v2));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.BINARY_AND: {

                    PyObject v2 = stack.pop();
                    PyObject v1 = stack.pop();

                    stack.push(v1.__band__(v2));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.BINARY_XOR: {

                    PyObject v2 = stack.pop();
                    PyObject v1 = stack.pop();

                    stack.push(v1.__bxor__(v2));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.BINARY_OR: {

                    PyObject v2 = stack.pop();
                    PyObject v1 = stack.pop();

                    stack.push(v1.__bor__(v2));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.COMPARE_OP: {

                    PyObject v2 = stack.pop();
                    PyObject v1 = stack.pop();


                    switch (instrArg) {
                        case OpCode.CompareCode.EQ: {
                            stack.push(v1.__eq__(v2));
                            break;
                        }
                        case OpCode.CompareCode.NEQ: {
                            stack.push(v1.__not_eq__(v2));
                            break;
                        }
                        case OpCode.CompareCode.GT: {
                            stack.push(v1.__gt__(v2));
                            break;
                        }
                        case OpCode.CompareCode.LT: {
                            stack.push(v1.__lt__(v2));
                            break;
                        }
                        case OpCode.CompareCode.GE: {
                            stack.push(v1.__ge__(v2));
                            break;
                        }
                        case OpCode.CompareCode.LE: {
                            stack.push(v1.__le__(v2));
                            break;
                        }
                    }

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.PRINT_ITEM: {
                    System.out.print(stack.pop().print());

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.PRINT_NEWLINE: {

                    System.out.println();

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.JUMP_FORWARD: {

                    frameActual.f_instr += instrArg;
                    break;
                }

                case OpCode.POP_JUMP_IF_TRUE: {

                    if (stack.pop().__bool__().value) {
                        frameActual.f_instr += instrArg;
                    } else {
                        frameActual.f_instr += 1;
                    }

                    break;
                }

                case OpCode.POP_JUMP_IF_FALSE: {


                    if (!stack.pop().__bool__().value) {
                        frameActual.f_instr += instrArg;
                    } else {
                        frameActual.f_instr += 1;
                    }


                    break;
                }

                case OpCode.JUMP_IF_TRUE_OR_POP: {

                    if (stack.peek().__bool__().value) {
                        frameActual.f_instr += instrArg;
                    } else {
                        stack.pop();
                        frameActual.f_instr += 1;
                    }

                    break;
                }

                case OpCode.JUMP_IF_FALSE_OR_POP: {

                    if (!stack.peek().__bool__().value) {
                        frameActual.f_instr += instrArg;
                    } else {
                        stack.pop();
                        frameActual.f_instr += 1;
                    }

                    break;
                }

                case OpCode.JUMP_ABSOLUTE: {

                    frameActual.f_instr = instrArg;
                    break;
                }

                case OpCode.POP_JUMP_FORWARD: {

                    stack.pop();
                    frameActual.f_instr += instrArg;

                    break;
                }

                case OpCode.POP_JUMP_ABSOLUTE: {
                    stack.pop();
                    frameActual.f_instr = instrArg;

                    break;
                }


                /** Iteracioens **/

                case OpCode.GET_ITER: {
                    stack.push(stack.pop().__iter__());
                    frameActual.f_instr += 1;

                    break;
                }

                case OpCode.FOR_ITER: {
                    try{
                        stack.push(stack.peek().__next__());
                        frameActual.f_instr += 1;
                    }
                    catch (PyStopIteration e){
                        frameActual.f_instr += instrArg;
                    }

                    break;
                }


                case OpCode.CREATE_LOOP: {
                    frameActual.f_loopStack.push(new LoopBlock(frameActual.f_instr+1, frameActual.f_instr + instrArg));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.CONTINUE_LOOP: {
                    if(frameActual.f_loopStack.empty()){
                        throw new PyRuntimeException("Continue llamado fuera de un loop.");
                    }

                    frameActual.f_instr = frameActual.f_loopStack.peek().instr_inicio;

                    break;
                }

                case OpCode.BREAK_LOOP: {
                    if(frameActual.f_loopStack.empty()){
                        throw new PyRuntimeException("Break llamado fuera de un loop.");
                    }

                    frameActual.f_instr = frameActual.f_loopStack.peek().instr_fin;

                    break;
                }

                case OpCode.DESTROY_LOOP: {
                    frameActual.f_loopStack.pop();

                    if(instrArg > 0) {
                        for (int i = 0; i < instrArg; i++) {
                            stack.pop();
                        }
                    }

                    frameActual.f_instr += 1;
                    break;
                }


                //***************************** Listas dicts y tuplas **************************************

                case OpCode.CREATE_LIST: {

                    PyList res = new PyList();
                    if(instrArg > 0) {
                        ArrayList<PyObject> datos = new ArrayList<>(instrArg);

                        for (int i = instrArg -1 ; i >= 0; i--) {
                            datos.add(0, stack.pop());
                        }
                        res.lista = datos;
                    }

                    stack.push(res);

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.CREATE_DICT: {

                    PyDict res = new PyDict();
                    if(instrArg > 0) {

                        for(int i = 0; i < instrArg; i++){
                            PyObject valor = stack.pop();
                            PyObject clave = stack.pop();

                            res.dict.put(clave,valor);
                        }
                    }

                    stack.push(res);

                    frameActual.f_instr += 1;
                    break;
                }


                case OpCode.CREATE_TUPLE: {

                    PyTuple res = new PyTuple();
                    if(instrArg > 0) {
                        ArrayList<PyObject> datos = new ArrayList<>(instrArg);

                        for (int i = instrArg -1 ; i >= 0; i--) {
                            datos.add(0, stack.pop());
                        }
                        res.tupla = datos;
                    }

                    stack.push(res);

                    frameActual.f_instr += 1;
                    break;
                }


                case OpCode.GET_INDEX: {

                    PyObject index = stack.pop();
                    PyObject ele = stack.pop();
                    stack.push(ele.__get_index__(index));

                    frameActual.f_instr += 1;
                    break;
                }

                case OpCode.SET_INDEX: {

                    PyObject index = stack.pop();
                    PyObject valor = stack.pop();
                    PyObject ele = stack.pop();
                    ele.__set_index__(index,valor);


                    frameActual.f_instr += 1;
                    break;
                }


                case OpCode.UNPACK: {

                    PyObject[] unpacked = stack.pop().__unpack__(instrArg);
                    for(PyObject v : unpacked){
                        stack.push(v);
                    }


                    frameActual.f_instr += 1;
                    break;
                }
                



                case OpCode.POP_TOP:{

                    stack.pop();
                    frameActual.f_instr+=1;
                    break;
                }


                case OpCode.FIN_EJECUCION: {
                    System.out.println(String.format("[DEBUG] Ejecutando instruccion de FIN: Numero de instruccion = %s, tamanio del stack del frame actual = %s", frameActual.f_instr, stack.size()));
                    throw new PyFinEjecucion("Fin de ejecucicion.");
                }

                default: {
                    throw new RuntimeException("Error fatal, instruccion desconocida.");
                }
            }
        }
        catch (PyFinEjecucion e){
            throw  e;
        }
        catch (PyException e){
            System.out.println(String.format("[DEBUG] Error en instruccion numero: %s de la linea %s, con tamano de stack en frame actual %s", frameActual.f_instr, this.getLineaActual(), frameActual.f_stack.size()));
            throw  e;
        }
        catch (Throwable t) {
            //Por ahora para debug
            System.out.println(String.format("[DEBUG] Error en instruccion numero: %s de la linea %s, con tamano de stack en frame actual %s", frameActual.f_instr, this.getLineaActual(), frameActual.f_stack.size()));
            throw new PyRuntimeException(t.getMessage());
        }
    }

}
