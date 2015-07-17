package com.pyjava;

import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyFinEjecucion;
import com.pyjava.core.runtime.*;
import com.pyjava.parser.codegen.ParserStatus;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Cristiano on 11/07/2015.
 */
public class Interprete {

    public static void main(String[] args){


        if(args.length == 0){
            System.out.println("ERROR: Se debe invocar con ruta completa del script a ejecutar.");
        }
        else {

            String fileName = args[0];
            Code codigo;
            try {
                codigo = ParserStatus.ParseFile(fileName);
            }
            catch (IOException e) {
                System.out.println(String.format("Error, archivo '%s' no encontrado.", fileName));
                return;
            }
            catch (UnsupportedOperationException e){
                System.out.println("Error de sintaxis:");
                System.out.println(e.getMessage());
                return;
            }
            catch (Exception e){
                System.out.println("Error inesperado: " + e.getMessage());
                return;
            }


            //Inicio frame inicial
            Frame frameInicial = new Frame();

            //Como es el modulo principal, globals = locals para correcto funcionamiento de invocaciones.
            frameInicial.f_globals = frameInicial.f_locals;

            //Cargo codigo
            frameInicial.f_code = codigo;

            //Inicio estado con frame inicial
            Estado estado = new Estado(frameInicial);

            //inicio loop de interpretacion
            //Itera hasta llegar a excepcion de fin de ejecucion.

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
}
