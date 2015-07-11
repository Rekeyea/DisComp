package com.pyjava.parser; /**
 * Created by rekeyea on 6/17/15.
 */
import java_cup.runtime.Symbol;
import jflex.anttask.JFlexTask;
import jflex.sym;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws IOException{
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

            parser Analizador = new parser(new Lexer(new FileReader("/home/rekeyea/Documents/python.py")));
            Analizador.parse();
            /*Lexer lexer = new Lexer(new FileReader("/home/rekeyea/Documents/python.py"));
            Symbol s = lexer.next_token();
            while(s.sym!=sym1.EOF){
                int symbol = s.sym;
                if(symbol==sym1.INDENT){
                    System.out.println("INDENT");
                }else if (symbol==sym1.DEDENT){
                    System.out.println("DEDENT");
                }else if (symbol==sym1.NEWLINE){
                    System.out.println("NEW LINE");
                }else{
                    String texto = (String)s.value;
                    if(!texto.isEmpty()){
                        System.out.println(texto);
                    }
                }
                s = lexer.next_token();
            }*/
            System.out.println("El archivo esta bien");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            /*System.out.println("El archivo tiene errores:");
            System.out.println(e.getMessage());*/
        }


    }
}
