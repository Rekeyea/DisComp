package com.pyjava;

import com.pyjava.core.PyInteger;
import com.pyjava.core.PyString;
import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyFinEjecucion;
import com.pyjava.core.runtime.*;

/**
 * Created by Cristiano on 28/06/2015.
 */
public class Test2 {

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
         * for a in "Menor o igual a 0":
         *     if a == "a":
         *         break
         *     print a
         * print None
         * print type(sumaRes)
         * print var3 //Esto deberia explotar y dar un error acorde.
         */

        //Frame inicial
        Frame frameInicial = new Frame();

        Code codigo = new Code("modulo<test.py>","C:/test/test.py");
        frameInicial.f_code = codigo;

        //Cargo algunas constantes...
        codigo.co_consts.add(new PyInteger(1));                     //0
        codigo.co_consts.add(new PyString("10"));                   //1
        codigo.co_consts.add(new PyString("La suma es: "));         //2
        codigo.co_consts.add(new PyInteger(0));                     //3
        codigo.co_consts.add(new PyString("Mayor a 0"));            //4
        codigo.co_consts.add(new PyString("Menor o igual a 0"));    //5
        codigo.co_consts.add(new PyString("a"));                    //6


        //Cargo nombres que voy a utilizar
        //Si fueran a nivel de funciones, deberian ir en co__varnames ya que van a ser variables locales
        //Podrian ir en co_varnames tambien, pero por alguna razon python decide poner variables de modulos en co_names, seguramente porque los modulos no reciben argumentos
        codigo.co_names.add("var1");            //0
        codigo.co_names.add("var2");            //1
        codigo.co_names.add("sumaRes");         //2
        codigo.co_names.add("int");             //3
        codigo.co_names.add("None");            //4
        codigo.co_names.add("type");            //5
        codigo.co_names.add("var3");            //6
        codigo.co_names.add("a");               //7



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
        codigo.co_code.add(new Instruccion(5, OpCode.POP_JUMP_IF_FALSE, 5));                 //18
        codigo.co_code.add(new Instruccion(6, OpCode.LOAD_CONST, 4));                       //19: print 'Mayor a 0'
        codigo.co_code.add(new Instruccion(6, OpCode.PRINT_ITEM,0));                        //20
        codigo.co_code.add(new Instruccion(6, OpCode.PRINT_NEWLINE,0));                     //21
        codigo.co_code.add(new Instruccion(6, OpCode.JUMP_FORWARD,4));                      //22 Si llegue aca, salto 4 lugares para no entrar en el else.
        codigo.co_code.add(new Instruccion(8, OpCode.LOAD_CONST, 5));                       //23
        codigo.co_code.add(new Instruccion(8, OpCode.PRINT_ITEM,0));                        //24
        codigo.co_code.add(new Instruccion(8, OpCode.PRINT_NEWLINE,0));                     //25


        //9: for a in "Menor o igual a 0":
        //10:     if a == "a":
        //11:         break
        //12:     print a
        codigo.co_code.add(new Instruccion(9, OpCode.LOAD_CONST, 5));                           //26    : carga "Menor o igual a 0"
        codigo.co_code.add(new Instruccion(9, OpCode.GET_ITER, 0));                             //27    : obtiene iterador
        codigo.co_code.add(new Instruccion(9, OpCode.CREATE_LOOP, 12));                         //28    : obtiene iterador
        codigo.co_code.add(new Instruccion(9, OpCode.FOR_ITER, 11));                            //29    : obtiene proximo elemento o salta 11 posiciones si no tiene
        codigo.co_code.add(new Instruccion(9, OpCode.STORE_NAME, 7));                           //30    : guarda el valor del proximo elemento en la variable a
        codigo.co_code.add(new Instruccion(10, OpCode.LOAD_NAME, 7));                           //31    : carga la variable a en el stack para realizar comparacion
        codigo.co_code.add(new Instruccion(10, OpCode.LOAD_CONST, 6));                          //32    : carga "a"
        codigo.co_code.add(new Instruccion(10, OpCode.COMPARE_OP, OpCode.CompareCode.EQ));      //33    : compara a == "a"
        codigo.co_code.add(new Instruccion(10, OpCode.POP_JUMP_IF_FALSE,2));                    //34    : si no es igual salta para continuar y evitar el break -- se podria optimizar
        codigo.co_code.add(new Instruccion(11, OpCode.BREAK_LOOP, 0));                          //35    : break...
        codigo.co_code.add(new Instruccion(12, OpCode.LOAD_NAME, 7));                           //36    : carga la variable a en el stack para imprimirla
        codigo.co_code.add(new Instruccion(12, OpCode.PRINT_ITEM,0));                           //37    : imprime
        codigo.co_code.add(new Instruccion(12, OpCode.PRINT_NEWLINE,0));                        //38    : imprime salto de linea
        codigo.co_code.add(new Instruccion(12, OpCode.CONTINUE_LOOP,0));                        //39    : vuelve a la iteracion, esto se podria optimizar.
        codigo.co_code.add(new Instruccion(12, OpCode.DESTROY_LOOP,1));                         //40    : Destruye el loop y hace pop de 1 elemento, o sea, del iterador.


        //print None -- Python en realidad optimiza un poco esto, y agrega siempre a None como constante para no irlo a buscar al namespace global.
        codigo.co_code.add(new Instruccion(13, OpCode.LOAD_NAME, 4));
        codigo.co_code.add(new Instruccion(13, OpCode.PRINT_ITEM,0));
        codigo.co_code.add(new Instruccion(13, OpCode.PRINT_NEWLINE,0));

        //print type(sumaRes)
        codigo.co_code.add(new Instruccion(14, OpCode.LOAD_NAME, 5));
        codigo.co_code.add(new Instruccion(14, OpCode.LOAD_NAME, 2));
        codigo.co_code.add(new Instruccion(14, OpCode.CALL_FUNCTION,1));
        codigo.co_code.add(new Instruccion(14, OpCode.PRINT_ITEM,0));
        codigo.co_code.add(new Instruccion(14, OpCode.PRINT_NEWLINE,0));


        //print var3
        codigo.co_code.add(new Instruccion(15, OpCode.LOAD_NAME, 6));
        codigo.co_code.add(new Instruccion(15, OpCode.PRINT_ITEM,0));
        codigo.co_code.add(new Instruccion(15, OpCode.PRINT_NEWLINE,0));

        //instruccion especial de fin de ejecucion
        codigo.co_code.add(new Instruccion(15, OpCode.FIN_EJECUCION, 0));


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
