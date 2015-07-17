package com.pyjava.parser; /**
 * Created by rekeyea on 6/17/15.
 */
import com.pyjava.core.exceptions.PyException;
import com.pyjava.core.exceptions.PyFinEjecucion;
import com.pyjava.core.runtime.*;
import com.pyjava.parser.codegen.LexerToken;
import java_cup.runtime.Symbol;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws Exception {
//        String input = "";
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        InputStream stream;
//        Lexer lexer;
//        Symbol simbolo;
//        StringBuffer string = new StringBuffer();
//        input = reader.readLine();
//        while (!input.equals("fin")){
//            string.append(input);
//            string.append(System.getProperty("line.separator"));
//            input = reader.readLine();
//        }
//        System.out.println(string.toString());
//        System.out.println("-------------------");
//        stream = new ByteArrayInputStream(string.toString().getBytes(StandardCharsets.UTF_8));
//        lexer = new Lexer(stream);
//        try{
//            simbolo = lexer.next_token();
//            while (simbolo.sym != sym.EOF){
//                System.out.println(simbolo.toString());
//                System.out.println(simbolo.value);
//                simbolo = lexer.next_token();
//            }
//        }catch (Exception ex){
//            System.out.println(ex.getMessage());
//        }

        try{

            parser Analizador = new parser(new Lexer(new FileReader("C:\\Users\\Cristiano\\Desktop\\test.py")));
            Symbol s = Analizador.parse();

            Code codigoModulo = (Code)s.value;

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


//            Lexer lexer = new Lexer(new FileReader("/home/rafael/proyects/DisComp/python/src/com/pyjava/parser/python.py"));
//            Symbol s = lexer.next_token();
//            while(s.sym!=sym1.EOF){
//                int symbol = s.sym;
//                if(symbol==sym1.INDENT){
//                    System.out.println("INDENT");
//                }else if (symbol==sym1.DEDENT){
//                    System.out.println("DEDENT");
//                }else if (symbol==sym1.NEWLINE){
//                    System.out.println("NEW LINE");
//                }else{
//                    String texto = ((LexerToken)s.value).TokenValue;
//                    if(!texto.isEmpty()){
//                        System.out.println(texto);
//                    }
//                }
//                s = lexer.next_token();
//            }
        }
        catch (Exception e){
            throw e;
            /*System.out.println("El archivo tiene errores:");
            System.out.println(e.getMessage());*/
        }


    }
}
