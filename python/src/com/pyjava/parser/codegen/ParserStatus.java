package com.pyjava.parser.codegen;
import com.pyjava.core.runtime.Code;
import com.pyjava.parser.parser;
import java_cup.runtime.Symbol;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Created by rekeyea on 7/11/15.
 */
public class ParserStatus {
    public static Deque<Generador> StackGenerador = new ArrayDeque<Generador>();

    public static boolean parsingWasSuccessfull = true;
    public static List<String> parsingUnsuccessfullMessage = new LinkedList<>();
    public static String fileToParse = "";
    public static HashMap<Integer,Integer> mapeoDeLineas = new HashMap<>();

    public static Code ParseFile(String filePath) throws Exception {
        fileToParse = filePath;
        Lexer lexer = new Lexer( Preprocesador.Preprocesar(fileToParse));
        parser Analizador = new parser(lexer);
        try {
            Symbol s = Analizador.parse();
            if (parsingWasSuccessfull) {
                Code codigo = (Code) s.value;
                return codigo;
            } else {
                throw new UnsupportedOperationException(String.join(", ", parsingUnsuccessfullMessage));
            }
        }
        catch (Throwable t){
            if (parsingWasSuccessfull) {
                throw new Exception(t.getMessage());
            } else {
                throw new UnsupportedOperationException(String.join(", ", parsingUnsuccessfullMessage));
            }
        }
    }

}
