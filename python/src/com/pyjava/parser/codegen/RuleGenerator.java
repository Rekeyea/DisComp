package com.pyjava.parser.codegen;

import com.pyjava.core.runtime.Instruccion;
import com.pyjava.core.runtime.OpCode;
import com.pyjava.parser.sym1;
import java_cup.parse_action_table;
import jdk.nashorn.internal.parser.Lexer;
import org.omg.CosNaming._NamingContextImplBase;
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

    public static ParseResult storeName(Object n){
        ParseResult pr = (ParseResult)n;
        int line = pr.linea;
        Name name = (Name)pr.value;
        LinkedList<Instruccion> instrucciones = new LinkedList<>();
        instrucciones.add(new Instruccion(line, OpCode.STORE_NAME,name.index));
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

    public static ParseResult generatePrint(Object p,Object coma, Object exp){
        if(coma==null){
            ParseResult pr = (ParseResult)p;
            Bloque bloque = ParseResult.getAs(pr);
            bloque.instrucciones.addLast(new Instruccion(pr.linea, OpCode.PRINT_ITEM,0));
            return new ParseResult(pr.linea,bloque);
        }else{
            int line = ((LexerToken)coma).NumeroFila+1;
            Bloque bFirst = ParseResult.getAs(p);
            Bloque bLast = ParseResult.getAs(exp);
            bFirst.instrucciones.add(new Instruccion(line,OpCode.PRINT_ITEM,0));
            bFirst.instrucciones.addAll(bLast.instrucciones);
            return new ParseResult(line,bFirst);
        }
    }

    public static ParseResult generatePrintNewline(Object p,Object trail){
        int line = ((LexerToken)p).NumeroFila+1;
        Bloque bRes = ParseResult.getAs(trail);
        bRes.instrucciones.add(new Instruccion(line,OpCode.PRINT_NEWLINE,0));
        return new ParseResult(line,bRes);
    }

    public static ParseResult generateFunctionCall(Object fName, Object fTrail){
        ParseResult pr = (ParseResult)fName;
        int line = pr.linea;
        Name name = ParseResult.getAs(pr);
        int cantArguments = 0;
        Instruccion cargarNombreFuncion = new Instruccion(line,OpCode.LOAD_NAME,name.index);
        Bloque trail = null;
        if(fTrail!=null){
            trail = ParseResult.getAs(fTrail);
            cantArguments = ((ParseResult)fTrail).argumentos;
            trail.instrucciones.addFirst(cargarNombreFuncion);
        }else{
            LinkedList<Instruccion> instrucciones = new LinkedList<>();
            instrucciones.add(cargarNombreFuncion);
            trail = ParserStatus.StackGenerador.peek().crearBloque(instrucciones,null,null);
        }
        Instruccion llamarFuncion = new Instruccion(line,OpCode.CALL_FUNCTION,cantArguments);
        trail.instrucciones.addLast(llamarFuncion);
        return new ParseResult(line,ParserStatus.StackGenerador.peek().crearBloque(trail.instrucciones,null,null));
    }

    public static ParseResult generateArguments(Object coma, Object left, Object right){
        if(coma==null){
            ParseResult pr = (ParseResult)left;
            int line = pr.linea;
            Bloque b = ParseResult.getAs(left);
            ParseResult p = new ParseResult(line,b);
            p.argumentos = 1;
            return p;
        }else{
            int line = ((LexerToken)coma).NumeroFila+1;
            Bloque lBloque = ParseResult.getAs(left);
            Bloque rBloque = ParseResult.getAs(right);
            Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(lBloque.instrucciones,rBloque,null);
            ParseResult res = new ParseResult(line,bRes);
            res.argumentos = ((ParseResult)right).argumentos + 1;
            return res;
        }

    }

    public static ParseResult generateListElements(Object head,Object coma, Object tail){
        if(coma==null){
            ParseResult pr = (ParseResult)head;
            pr.argumentos = 1;
            return pr;
        }else {
            ParseResult pTail = (ParseResult)tail;
            int line = ((LexerToken)coma).NumeroFila+1;
            int cantArgs = pTail.argumentos;
            Bloque bHead = ParseResult.getAs(head);
            Bloque bTail = ParseResult.getAs(pTail);
            Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bHead.instrucciones,bTail,null);
            ParseResult res = new ParseResult(line,bRes);
            res.argumentos = pTail.argumentos+1;
            return res;
        }
    }

    public static ParseResult generateListTupleDict(Object bracket, Object elements, int tipoInstruccion){
        int linea = ((LexerToken)bracket).NumeroFila+1;
        int argumentos = 0;
        Bloque b;
        Instruccion instLista = new Instruccion(linea,tipoInstruccion,argumentos);
        if(elements==null){
            LinkedList<Instruccion> instrucciones = new LinkedList<>();
            instrucciones.add(instLista);
            b = ParserStatus.StackGenerador.peek().crearBloque(instrucciones,null,null);
        }else{
            ParseResult pr = (ParseResult)elements;
            argumentos = pr.argumentos;
            instLista.arg = argumentos;
            b = ParseResult.getAs(elements);
            b.instrucciones.addLast(instLista);
        }
        ParseResult res = new ParseResult(linea,b);
        return res;
    }

    public static ParseResult generateDictItem(Object e1, Object coma, Object e2){
        int linea = ((LexerToken)coma).NumeroFila;
        Bloque bE1 = ParseResult.getAs(e1);
        Bloque bE2 = ParseResult.getAs(e2);
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bE1.instrucciones,bE2,null);
        ParseResult res = new ParseResult(linea,bRes);
        res.argumentos=1;
        return res;
    }

    public static ParseResult generateNameFunctionCall(Object exp,Object punto,Object nomF, Object trail){
        int linea = ((LexerToken)punto).NumeroFila+1;
        Bloque b = ParseResult.getAs(exp);
        Name n = ParseResult.getAs(nomF);
        Instruccion nombreAtributo = new Instruccion(linea,OpCode.LOAD_ATTR,n.index);
        ParseResult argumentos = (ParseResult)trail;
        int cantArgumentos = 0;
        Bloque bRes;
        b.instrucciones.addLast(nombreAtributo);
        if(argumentos==null){
            b.instrucciones.addLast(new Instruccion(linea,OpCode.CALL_FUNCTION,cantArgumentos));
            bRes = b;
        }else{
            cantArgumentos = argumentos.argumentos;
            Bloque tail = (Bloque)argumentos.value;
            Instruccion llamaFuncion = new Instruccion(linea,OpCode.CALL_FUNCTION,cantArgumentos);
            tail.instrucciones.addLast(llamaFuncion);
            bRes = ParserStatus.StackGenerador.peek().crearBloque(b.instrucciones,tail,null);
        }
        return new ParseResult(linea,bRes);
    }

    public static ParseResult generateAssignation(Object e, Object au, Object r){
        int lineNumber = ((LexerToken)au).NumeroFila;
        Bloque expression = ParseResult.getAs(r);
        Name b = ParseResult.getAs(e);
        expression.instrucciones.addLast(new Instruccion(lineNumber, OpCode.STORE_NAME, b.index));
        return new ParseResult(lineNumber,expression);
    }

    public static ParseResult joinNames(Object m, Object nc){
        ParseResult pr = (ParseResult)nc;
        int line = pr.linea;
        Name nombre = ParseResult.getAs(m);
        Bloque b = ParseResult.getAs(nc);
        b.instrucciones.addLast(new Instruccion(line,OpCode.STORE_NAME,nombre.index));
        return new ParseResult(line,b);
    }

    public static ParseResult generateUnpackAssignation(Object namelist,Object assign,Object expression){
        int linea = ((LexerToken)assign).NumeroFila+1;
        Bloque bNames = ParseResult.getAs(namelist);
        Instruccion unpack = new Instruccion(linea,OpCode.UNPACK,bNames.instrucciones.size());
        Bloque bTrail = ParseResult.getAs(expression);
        bTrail.instrucciones.addLast(unpack);
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bTrail.instrucciones,bNames,null);
        return new ParseResult(linea,bRes);
    }

    public static ParseResult generateTupleMakerAssignation(Object namelist,Object assign,Object tuplemaker){
        int linea = ((LexerToken)assign).NumeroFila+1;
        Bloque bNames = ParseResult.getAs(namelist);
        ParseResult pTrail = (ParseResult)tuplemaker;
        Bloque bTrail = ParseResult.getAs(tuplemaker);
        Instruccion instLista = new Instruccion(linea,OpCode.CREATE_TUPLE,pTrail.argumentos);
        Instruccion unpack = new Instruccion(linea,OpCode.UNPACK,bNames.instrucciones.size());
        bTrail.instrucciones.addLast(instLista);
        bTrail.instrucciones.addLast(unpack);
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bTrail.instrucciones,bNames,null);
        return new ParseResult(linea,bRes);
    }

    public static ParseResult generateIfStatement(Object exp,Object colon1,Object suite1,Object
            colon2,Object suite2){
        int linea = ((LexerToken)colon1).NumeroFila+1;
        Bloque expBloque = ParseResult.getAs(exp);
        Bloque ifBloque = ParseResult.getAs(suite1);
        Bloque rBloque = null;
        Bloque lBloque = null;
        if (suite2 == null) {
            expBloque.instrucciones.addLast(new Instruccion(linea, OpCode.JUMP_IF_FALSE_OR_POP, ifBloque.instrucciones.size() + 1));
            lBloque = ParserStatus.StackGenerador.peek().crearBloque(expBloque.instrucciones,ifBloque,null);
        } else {
            expBloque.instrucciones.addLast(new Instruccion(linea, OpCode.JUMP_IF_FALSE_OR_POP, ifBloque.instrucciones.size() + 2));
            lBloque = ParserStatus.StackGenerador.peek().crearBloque(expBloque.instrucciones,ifBloque,null);
            int linea2 = ((LexerToken)colon2).NumeroFila+1;
            rBloque = ParseResult.getAs(suite2);
            lBloque.instrucciones.addLast(new Instruccion(linea2, OpCode.JUMP_FORWARD, rBloque.instrucciones.size() + 1));
        }
        Bloque b = ParserStatus.StackGenerador.peek().crearBloque(lBloque.instrucciones,rBloque,null);
        return new ParseResult(linea,b);
    }

}
