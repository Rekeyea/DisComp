package com.pyjava.parser.codegen;

import com.pyjava.core.runtime.Instruccion;
import com.pyjava.core.runtime.OpCode;
import com.pyjava.parser.sym1;
import jdk.nashorn.internal.parser.Lexer;
import sun.util.locale.ParseStatus;

import java.util.LinkedList;
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
                break;
            case sym1.LONG:
                val =  gen.createOrGetConst(ConstCreator.createPyLong(token.TokenValue));
                break;
            case sym1.FLOAT:
                val =  gen.createOrGetConst(ConstCreator.createPyFloat(token.TokenValue));
                break;
            case sym1.STRING:
                val =  gen.createOrGetConst(ConstCreator.createPyString(token.TokenValue));
                break;
            case sym1.STRING3:
                val =  gen.createOrGetConst(ConstCreator.createPyString(token.TokenValue));
                break;
            case sym1.NONE:
                val =  gen.createOrGetConst(ConstCreator.createPyNone());
                break;
            case sym1.TRUE:
                val =  gen.createOrGetConst(ConstCreator.createPyTrue());
                break;
            case sym1.FALSE:
                val =  gen.createOrGetConst(ConstCreator.createPyFalse());
                break;
            default:
                val =  null;
        }
        return new ParseResult(numLine,val);
    }

    public static ParseResult generateMinusConstant(Object t){
        LexerToken token = (LexerToken)t;
        token.TokenValue = "-"+token.TokenValue;
        return generateConstant(token);
    }

    public static ParseResult generateName(Object t){
        LexerToken token = (LexerToken)t;
        int numLine = token.NumeroFila+1;
        Object val = ParserStatus.StackGenerador.peek().createOrGetName((token).TokenValue);
        return new ParseResult(numLine,val);
    }

    public static ParseResult loadName(Object n){
        ParseResult pr = (ParseResult)n;
        int line = pr.linea;
        Name name = (Name)pr.value;
        LinkedList<Instruccion> instrucciones = new LinkedList<>();
        instrucciones.add(new Instruccion(line, OpCode.LOAD_NAME,name.index));
        return new ParseResult(line,ParserStatus.StackGenerador.peek().crearBloque(instrucciones,null,null));
    }

    public static ParseResult loadConst(Object c){
        ParseResult pr = (ParseResult)c;
        int line = pr.linea;
        Const constante = (Const)pr.value;
        LinkedList<Instruccion> instrucciones = new LinkedList<>();
        instrucciones.add(new Instruccion(line, OpCode.LOAD_CONST,constante.index));
        return new ParseResult(line,ParserStatus.StackGenerador.peek().crearBloque(instrucciones,null,null));
    }

    public static ParseResult loadNameOrConst(Object c){
        ParseResult pr = (ParseResult)c;
        if(pr.value instanceof Name){
            return loadName(c);
        }else {
            return loadConst(c);
        }
    }

    public static ParseResult generateBinaryOperation(Object op, Object left, Object right){
        LexerToken operador = (LexerToken)op;
        int linea = operador.NumeroFila+1;
        Bloque lBloque = ParseResult.getAs(left);
        Bloque rBloque = ParseResult.getAs(right);
        Instruccion instOp = null;
        Bloque b = null;
        if(operador.TokenType == sym1.AND || operador.TokenType == sym1.OR ){
            if(sym1.OR==operador.TokenType){
                lBloque.instrucciones.add(new Instruccion(linea,OpCode.JUMP_IF_TRUE_OR_POP,rBloque.instrucciones.size()+1));
                b = ParserStatus.StackGenerador.peek().crearBloque(lBloque.instrucciones,rBloque,null);
            }else {
                lBloque.instrucciones.add(new Instruccion(linea,OpCode.JUMP_IF_FALSE_OR_POP,rBloque.instrucciones.size()+1));
                b = ParserStatus.StackGenerador.peek().crearBloque(lBloque.instrucciones,rBloque,null);
            }
        }else{
            switch (operador.TokenType){
                case sym1.MINOR:
                    instOp = new Instruccion(linea,OpCode.COMPARE_OP,OpCode.CompareCode.LT);
                    break;
                case sym1.MAJOR:
                    instOp = new Instruccion(linea,OpCode.COMPARE_OP,OpCode.CompareCode.GT);
                    break;
                case sym1.MINOREQ:
                    instOp = new Instruccion(linea,OpCode.COMPARE_OP,OpCode.CompareCode.LE);
                    break;
                case sym1.MAJOREQ:
                    instOp = new Instruccion(linea,OpCode.COMPARE_OP,OpCode.CompareCode.GE);
                    break;
                case sym1.DIFF:
                    instOp = new Instruccion(linea,OpCode.COMPARE_OP,OpCode.CompareCode.NEQ);
                    break;
                case sym1.EQUALS:
                    instOp = new Instruccion(linea,OpCode.COMPARE_OP,OpCode.CompareCode.EQ);
                    break;
                case sym1.ORB:
                    instOp = new Instruccion(linea,OpCode.BINARY_OR,0);
                    break;
                case sym1.XORB:
                    instOp = new Instruccion(linea,OpCode.BINARY_XOR,0);
                    break;
                case sym1.ANDB:
                    instOp = new Instruccion(linea,OpCode.BINARY_AND,0);
                    break;
                case sym1.SHIFTL:
                    instOp = new Instruccion(linea,OpCode.BINARY_LSHIFT,0);
                    break;
                case sym1.SHIFTR:
                    instOp = new Instruccion(linea,OpCode.BINARY_RSHIFT,0);
                    break;
                case sym1.PLUS:
                    instOp = new Instruccion(linea,OpCode.BINARY_ADD,0);
                    break;
                case sym1.MINUS:
                    instOp = new Instruccion(linea,OpCode.BINARY_SUB,0);
                    break;
                case sym1.MULT:
                    instOp = new Instruccion(linea,OpCode.BINARY_MUL,0);
                    break;
                case sym1.DIV:
                    instOp = new Instruccion(linea,OpCode.BINARY_DIV,0);
                    break;
                case sym1.MOD:
                    instOp = new Instruccion(linea,OpCode.BINARY_MOD,0);
                    break;
                case sym1.EXP:
                    instOp = new Instruccion(linea,OpCode.BINARY_POW,0);
                    break;
                case sym1.DIVE:
                    instOp = new Instruccion(linea,OpCode.BINARY_FLOOR_DIV,0);
                    break;
            }
            rBloque.instrucciones.add(instOp);
            b = ParserStatus.StackGenerador.peek().crearBloque(lBloque.instrucciones,rBloque,null);
        }
        return new ParseResult(linea,b);
    }

    public static ParseResult generateUnaryOperation(Object p, Object exp){
        LexerToken operator = (LexerToken)p;
        int linea = operator.NumeroFila+1;
        ParseResult expresion = (ParseResult)exp;
        Instruccion instOp = null;
        switch (operator.TokenType){
            case sym1.NOT:
                instOp = new Instruccion(linea,OpCode.UNARY_NOT,0);
                break;
            case sym1.NOTB:
                instOp = new Instruccion(linea,OpCode.UNARY_INVERT,0);
                break;
        }
        ((Bloque)ParseResult.getAs(expresion)).instrucciones.add(instOp);
        return expresion;
    }

    public static ParseResult generatePrint(Object p, Object exp){
        int lineNumber = ((LexerToken)p).NumeroFila;
        ParseResult pr = (ParseResult)exp;
        Bloque b = ParseResult.getAs(pr);
        b.instrucciones.add(new Instruccion(lineNumber, OpCode.PRINT_ITEM,0));
        b.instrucciones.add(new Instruccion(lineNumber, OpCode.PRINT_NEWLINE,0));
        return new ParseResult(lineNumber,b);
    }
}
