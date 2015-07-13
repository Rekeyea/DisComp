package com.pyjava;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyFinEjecucion;
import com.pyjava.core.runtime.*;

/**
 * Created by Cristiano on 11/07/2015.
 */
public class Interprete {

    public static void main(String[] args){


        //aca deberia leer archivo de entrada

        //deberia parsear y retornar un Code

        //Code codigoModulo = null; // aca iria el code retornado por el parser

        //Por ahora uso uno que no hace nada
        Code codigoModulo = new Code("modulo<test.py>","C:/test/test.py");
        codigoModulo.co_code.add(new Instruccion(0, OpCode.FIN_EJECUCION, 0));

        Frame frameInicial = new Frame();
        frameInicial.f_globals = frameInicial.f_locals;

        frameInicial.f_code = codigoModulo;


        Estado estado = new Estado(frameInicial);


        //Inicio loop de interpretacion

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
