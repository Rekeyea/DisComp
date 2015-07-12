package com.pyjava.parser.codegen;

import com.pyjava.core.runtime.Instruccion;
import com.pyjava.parser.sym1;
import jdk.nashorn.internal.parser.Lexer;

import java.util.Objects;

/**
 * Created by rekeyea on 7/12/15.
 */
public class RuleGenerator {

    public static ParseResult generateConstant(Object t){
        LexerToken token = (LexerToken)t;
        int numLine = token.NumeroFila+1;
        Object val = null;
        Generador gen = ParserStatus.StackGenerador.peek();
        switch (token.TokenType){
            case sym1.INTEGER:
                val =  gen.createOrGetConst(ConstCreator.createPyInt(token.TokenValue));
            case sym1.LONG:
                val =  gen.createOrGetConst(ConstCreator.createPyLong(token.TokenValue));
            case sym1.FLOAT:
                val =  gen.createOrGetConst(ConstCreator.createPyFloat(token.TokenValue));
            case sym1.STRING:
                val =  gen.createOrGetConst(ConstCreator.createPyString(token.TokenValue));
            case sym1.STRING3:
                val =  gen.createOrGetConst(ConstCreator.createPyString(token.TokenValue));
            case sym1.NONE:
                val =  gen.createOrGetConst(ConstCreator.createPyNone());
            case sym1.TRUE:
                val =  gen.createOrGetConst(ConstCreator.createPyTrue());
            case sym1.FALSE:
                val =  gen.createOrGetConst(ConstCreator.createPyFalse());
            default:
                val =  null;
        }
        return new ParseResult(numLine,val);
    }

    public static ParseResult generateName(Object t){
        LexerToken token = (LexerToken)t;
        int numLine = token.NumeroFila+1;
        Object val = ParserStatus.StackGenerador.peek().createOrGetName((token).TokenValue);
        return new ParseResult(numLine,val);
    }

    public static Instruccion loadName(Name n){
        return null;
    }

    public static Instruccion loadConst(Const c){
        return null;
    }
}
