/**
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
    public static void main(String[] args){
        String input = "";
        Scanner reader = new Scanner(System.in);
        InputStream stream;
        Lexer lexer;
        Symbol simbolo;
        while (!input.equals("fin")){
            System.out.println("Ingrese la expresión");
            input = reader.next();
            stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            lexer = new Lexer(stream);

            try{
                simbolo = lexer.next_token();
                while (simbolo.sym != sym.EOF){
                    System.out.println(simbolo.toString());
                    simbolo = lexer.next_token();
                }
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }

        }
    }
}
