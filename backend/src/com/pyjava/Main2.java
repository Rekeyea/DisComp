package com.pyjava;

import com.pyjava.core.PyInteger;
import com.pyjava.core.PyString;
import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyFinEjecucion;
import com.pyjava.core.runtime.*;

/**
 * Created by Cristiano on 28/06/2015.
 */
public class Main2 {

    /**
     * Pruebo interprete.
     * @param args
     */
    public static void main(String[] args){


        /**
         * Vamos a compilar y ejecutar el siguiente codigo python: test.py (a nivel de modulo)
         * var1 = 1
         * var2 = int("10")
         * sumaRes = var1 + var2
         * print 'La suma es: ', sumaRes
         * if sumaRes > 0:
         *     print 'Mayor a 0'
         * else:
         *     print 'Menor o igual a 0'
         * print None
         * print type(sumaRes)
         * print var3 //Esto deberia explotar y dar un error acorde.
         */

        //Frame inicial
        Frame frameInicial = new Frame();

        Code codigo = new Code("modulo<test.py>","C:/test/test.py");
        frameInicial.f_code = codigo;

        //Cargo algunas constantes...
        codigo.co_consts.add(new PyInteger(1));
        codigo.co_consts.add(new PyString("10"));
        codigo.co_consts.add(new PyString("La suma es: "));
        codigo.co_consts.add(new PyInteger(0));
        codigo.co_consts.add(new PyString("Mayor a 0"));
        codigo.co_consts.add(new PyString("Menor o igual a 0"));


        //Cargo nombres que voy a utilizar
        //Si fueran a nivel de funciones, deberian ir en co__varnames ya que van a ser variables locales
        //Podrian ir en co_varnames tambien, pero por alguna razon python decide poner variables de modulos en co_names, seguramente porque los modulos no reciben argumentos
        codigo.co_names.add("var1");
        codigo.co_names.add("var2");
        codigo.co_names.add("sumaRes");
        codigo.co_names.add("int");
        codigo.co_names.add("None");
        codigo.co_names.add("type");
        codigo.co_names.add("var3");



        //Cargo codigo, instrucciones responsables de asignar esas constantes a variables.

        //var1 = 1
        codigo.co_code.add(new Instruccion(1, OpCode.LOAD_CONST, 0));   //Carga la primer constante en el stack
        codigo.co_code.add(new Instruccion(1, OpCode.STORE_NAME, 0));   //Guarda la constante en una variable local, a nivel modulo, por eso usamos co_names y no co_varnames

        //var2 = int("10")
        codigo.co_code.add(new Instruccion(2, OpCode.LOAD_NAME, 3));    //cargo 'int' en el stack
        codigo.co_code.add(new Instruccion(2, OpCode.LOAD_CONST, 1));   //Carga la segunda constante en el stack
        codigo.co_code.add(new Instruccion(2, OpCode.CALL_FUNCTION, 1));   //llamo a la funcion int, con 1 argumento.
        codigo.co_code.add(new Instruccion(2, OpCode.STORE_NAME, 1));       //guardo el resultado en var2

        //sumaRes = var1 + var2
        //Pusheo los dos valores al stack y los sumo
        codigo.co_code.add(new Instruccion(3, OpCode.LOAD_NAME, 0));
        codigo.co_code.add(new Instruccion(3, OpCode.LOAD_NAME, 1));
        codigo.co_code.add(new Instruccion(3, OpCode.BINARY_ADD, 0));
        codigo.co_code.add(new Instruccion(3, OpCode.STORE_NAME, 2));

        //print 'La suma es: ', sumaRes
        //imprimo resultado
        codigo.co_code.add(new Instruccion(4, OpCode.LOAD_CONST, 2));
        codigo.co_code.add(new Instruccion(4, OpCode.PRINT_ITEM,0));
        codigo.co_code.add(new Instruccion(4, OpCode.LOAD_NAME, 2));
        codigo.co_code.add(new Instruccion(4, OpCode.PRINT_ITEM,0));
        codigo.co_code.add(new Instruccion(4, OpCode.PRINT_NEWLINE,0));


        //5: if sumaRes > 0:
        //6:      print 'Mayor a 0'
        //7: else:
        //8:     print 'Menor o igual a 0'
        codigo.co_code.add(new Instruccion(5, OpCode.LOAD_NAME, 2));
        codigo.co_code.add(new Instruccion(5, OpCode.LOAD_CONST, 3));
        codigo.co_code.add(new Instruccion(5, OpCode.COMPARE_OP, OpCode.CompareCode.GE));   //esta es la instruccion 17. (de 0 a N-1)
        codigo.co_code.add(new Instruccion(5, OpCode.POP_JUMP_IF_FALSE, 23));                 //18
        codigo.co_code.add(new Instruccion(6, OpCode.LOAD_CONST, 4));                       //19: print 'Mayor a 0'
        codigo.co_code.add(new Instruccion(6, OpCode.PRINT_ITEM,0));                        //20
        codigo.co_code.add(new Instruccion(6, OpCode.PRINT_NEWLINE,0));                     //21
        codigo.co_code.add(new Instruccion(6, OpCode.JUMP_FORWARD,4));                      //22 Si llegue aca, salto 4 lugares para no entrar en el else.
        codigo.co_code.add(new Instruccion(8, OpCode.LOAD_CONST, 5));                       //23
        codigo.co_code.add(new Instruccion(8, OpCode.PRINT_ITEM,0));                        //24
        codigo.co_code.add(new Instruccion(8, OpCode.PRINT_NEWLINE,0));                     //25




        //print None -- Python en realidad optimiza un poco esto, y agrega siempre a None como constante para no irlo a buscar al namespace global.
        codigo.co_code.add(new Instruccion(9, OpCode.LOAD_NAME, 4));
        codigo.co_code.add(new Instruccion(9, OpCode.PRINT_ITEM,0));
        codigo.co_code.add(new Instruccion(9, OpCode.PRINT_NEWLINE,0));

        //print type(sumaRes)
        codigo.co_code.add(new Instruccion(10, OpCode.LOAD_NAME, 5));
        codigo.co_code.add(new Instruccion(10, OpCode.LOAD_NAME, 2));
        codigo.co_code.add(new Instruccion(10, OpCode.CALL_FUNCTION,1));
        codigo.co_code.add(new Instruccion(10, OpCode.PRINT_ITEM,0));
        codigo.co_code.add(new Instruccion(10, OpCode.PRINT_NEWLINE,0));

        //print var3
        codigo.co_code.add(new Instruccion(11, OpCode.LOAD_NAME, 6));
        codigo.co_code.add(new Instruccion(11, OpCode.PRINT_ITEM,0));
        codigo.co_code.add(new Instruccion(11, OpCode.PRINT_NEWLINE,0));

        //instruccion especial de fin de ejecucion
        codigo.co_code.add(new Instruccion(12, OpCode.FIN_EJECUCION, 0));


        Estado estado = new Estado(frameInicial);

        //inicio loop de interpretacion
        while(true){
            try{
                estado.interpretarInstruccion();
            }
            catch (PyFinEjecucion e){
                System.out.println("-------- Fin de ejecucion ------");
                return;
            }
            catch (PyException e){
                System.out.println("Error: Stack trace:");
                estado.printStacktrace();
                System.out.println(e.getMessage());

                //Finalizo la ejecucion ante error.
                return;
            }
        }

    }
}
