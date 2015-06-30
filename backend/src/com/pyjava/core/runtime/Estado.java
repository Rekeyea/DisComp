package com.pyjava.core.runtime;

import com.pyjava.core.*;
import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyFinEjecucion;
import com.pyjava.core.exceptions.PyNameError;

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

                    for (int i = instrArg - 1; i >= 0; i--) {
                        args[i] = stack.pop();
                    }

                    //Objeto a llamar esta ultimo en el stack.

                    PyObject callable = stack.pop();

                    PyObject res = callable.__call__(args, PySingletons.kwargsVacios, this);

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

                case OpCode.LOAD_FAST: {

                    //Es un error del parser si se intenta obtener un nombre fuera de rango, asi que no se chequea.
                    String name = frameActual.f_code.co_varnames.get(instrArg);

                    PyObject valor = frameActual.f_locals.get(name);

                    if (valor == null) {
                        throw new PyNameError(String.format("nombre local '%s' no esta definido.", name));
                    }

                    stack.push(valor);

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


                case OpCode.STORE_FAST: {

                    //Es un error del parser si se intenta obtener un nombre fuera de rango, asi que no se chequea.
                    String name = frameActual.f_code.co_varnames.get(instrArg);

                    frameActual.f_locals.put(name, stack.pop());

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

                    if (stack.peek().__bool__().value) {
                        stack.pop();
                        frameActual.f_instr = instrArg;
                    } else {
                        frameActual.f_instr += 1;
                    }

                    break;
                }

                case OpCode.POP_JUMP_IF_FALSE: {

                    if (!stack.peek().__bool__().value) {
                        stack.pop();
                        frameActual.f_instr = instrArg;
                    } else {
                        frameActual.f_instr += 1;
                    }


                    break;
                }

                case OpCode.JUMP_IF_TRUE_OR_POP: {

                    if (stack.peek().__bool__().value) {
                        frameActual.f_instr = instrArg;
                    } else {
                        stack.pop();
                        frameActual.f_instr += 1;
                    }

                    break;
                }

                case OpCode.JUMP_IF_FALSE_OR_POP: {

                    if (!stack.peek().__bool__().value) {
                        frameActual.f_instr = instrArg;
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


                case OpCode.FIN_EJECUCION: {
                    throw new PyFinEjecucion("Fin de ejecucicion.");
                }

                default: {
                    throw new RuntimeException("Error fatal, instruccion desconocida.");
                }
            }
        }
        catch (Throwable t) {
            //Por ahora para debug
            System.out.println(String.format("Error en instruccion numero: %s de la linea %s", frameActual.f_instr, this.getLineaActual()));
            throw t;
        }
    }

}
