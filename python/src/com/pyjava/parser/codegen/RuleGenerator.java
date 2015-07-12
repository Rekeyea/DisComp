package com.pyjava.parser.codegen;

import com.pyjava.parser.sym1;

import java.util.Objects;

/**
 * Created by rekeyea on 7/12/15.
 */
public class RuleGenerator {

    public static Const generateConstant(LexerToken token){
        Generador gen = ParserStatus.StackGenerador.peek();
        switch (token.TokenType){
            case sym1.INTEGER:
                return gen.createOrGetConst(ConstCreator.createPyInt(token.TokenValue));
            case sym1.LONG:
                return gen.createOrGetConst(ConstCreator.createPyLong(token.TokenValue));
            case sym1.FLOAT:
                return gen.createOrGetConst(ConstCreator.createPyFloat(token.TokenValue));
            case sym1.STRING:
                return gen.createOrGetConst(ConstCreator.createPyString(token.TokenValue));
            case sym1.STRING3:
                return gen.createOrGetConst(ConstCreator.createPyString(token.TokenValue));
            case sym1.NONE:
                return gen.createOrGetConst(ConstCreator.createPyNone());
            case sym1.TRUE:
                return gen.createOrGetConst(ConstCreator.createPyTrue());
            case sym1.FALSE:
                return gen.createOrGetConst(ConstCreator.createPyFalse());
            default:
                return null;
        }
    }
    public static Name generateName(LexerToken token){
        return ParserStatus.StackGenerador.peek().createOrGetName((token).TokenValue);
    }
}
