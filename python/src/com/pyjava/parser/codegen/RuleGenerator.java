package com.pyjava.parser.codegen;

import com.pyjava.core.PyObject;
import com.pyjava.core.runtime.Code;
import com.pyjava.core.runtime.Instruccion;
import com.pyjava.core.runtime.OpCode;
import com.pyjava.parser.sym1;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx;
import java_cup.parse_action_table;
import jdk.nashorn.internal.parser.Lexer;
import org.omg.CosNaming._NamingContextImplBase;
import sun.util.locale.ParseStatus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
        int cantArguments2 = 0;
        Instruccion cargarNombreFuncion = new Instruccion(line,OpCode.LOAD_NAME,name.index);
        Bloque trail = null;
        if(fTrail!=null){
            trail = ParseResult.getAs(fTrail);
            cantArguments = ((ParseResult)fTrail).argumentos;
            cantArguments2 = ((ParseResult)fTrail).argumentos2;
            trail.instrucciones.addFirst(cargarNombreFuncion);
        }else{
            LinkedList<Instruccion> instrucciones = new LinkedList<>();
            instrucciones.add(cargarNombreFuncion);
            trail = ParserStatus.StackGenerador.peek().crearBloque(instrucciones,null,null);
        }
        Instruccion llamarFuncion = new Instruccion(line,OpCode.CALL_FUNCTION,cantArguments,cantArguments2);
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
            ParseResult prRight = (ParseResult)right;
            int line = ((LexerToken)coma).NumeroFila+1;
            Bloque lBloque = ParseResult.getAs(left);
            Bloque rBloque = ParseResult.getAs(right);
            Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(lBloque.instrucciones,rBloque,null);
            ParseResult res = new ParseResult(line,bRes);
            res.argumentos = ((ParseResult)right).argumentos + 1;
            res.argumentos2 = ((ParseResult)right).argumentos2;
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
        int cantArgumentos2 = 0;
        Bloque bRes;
        b.instrucciones.addLast(nombreAtributo);
        if(argumentos==null){
            b.instrucciones.addLast(new Instruccion(linea,OpCode.CALL_FUNCTION,cantArgumentos));
            bRes = b;
        }else{
            cantArgumentos = argumentos.argumentos;
            cantArgumentos2 = argumentos.argumentos2;
            Bloque tail = (Bloque)argumentos.value;
            Instruccion llamaFuncion = new Instruccion(linea,OpCode.CALL_FUNCTION,cantArgumentos,cantArgumentos2);
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
        ParseResult prM = (ParseResult)m;
        ParseResult pr = (ParseResult)nc;
        int line = pr.linea;
        Bloque b = ParseResult.getAs(nc);
        ParseResult nPr = (ParseResult)nc;
        int argumentos = nPr.argumentos+1;
        if(prM.value instanceof Name){
            Name nombre = ParseResult.getAs(m);
            b.instrucciones.addLast(new Instruccion(line,OpCode.STORE_NAME,nombre.index));
        }else{
            Bloque mBloque = ParseResult.getAs(m);
            b.instrucciones.addAll(mBloque.instrucciones);
            b.instrucciones.addLast(new Instruccion(line,OpCode.SET_INDEX,0));
        }
        ParseResult prRes = new ParseResult(line,b);
        prRes.argumentos = argumentos;
        return prRes;
    }

    public static ParseResult generateUnpackAssignation(Object namelist,Object assign,Object expression){
        int linea = ((LexerToken)assign).NumeroFila+1;
        ParseResult prNamelist = (ParseResult)namelist;
        Bloque bNames = ParseResult.getAs(namelist);
        Instruccion unpack = new Instruccion(linea,OpCode.UNPACK,prNamelist.argumentos);
        Bloque bTrail = ParseResult.getAs(expression);
        bTrail.instrucciones.addLast(unpack);
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bTrail.instrucciones,bNames,null);
        return new ParseResult(linea,bRes);
    }

    public static ParseResult generateTupleMakerAssignation(Object namelist,Object assign,Object tuplemaker){
        int linea = ((LexerToken)assign).NumeroFila+1;
        ParseResult prNamelist = (ParseResult)namelist;
        Bloque bNames = ParseResult.getAs(namelist);
        ParseResult pTrail = (ParseResult)tuplemaker;
        Bloque bTrail = ParseResult.getAs(tuplemaker);
        /*
        Instruccion instLista = new Instruccion(linea,OpCode.CREATE_TUPLE,pTrail.argumentos);
        Instruccion unpack = new Instruccion(linea,OpCode.UNPACK,prNamelist.argumentos);
        bTrail.instrucciones.addLast(instLista);
        bTrail.instrucciones.addLast(unpack);
        */
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bTrail.instrucciones,bNames,null);
        return new ParseResult(linea,bRes);
    }

    public static ParseResult generateTupleMakerSingleAssignation(Object name,Object assign,Object tuplemaker){
        int linea = ((LexerToken)assign).NumeroFila+1;

        ParseResult pTrail = (ParseResult)tuplemaker;
        Bloque bTrail = ParseResult.getAs(tuplemaker);
        Instruccion instLista = new Instruccion(linea,OpCode.CREATE_TUPLE,pTrail.argumentos);
        bTrail.instrucciones.addLast(instLista);
        Name b = ParseResult.getAs(name);
        bTrail.instrucciones.addLast(new Instruccion(linea, OpCode.STORE_NAME, b.index));

        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bTrail.instrucciones,null,null);
        return new ParseResult(linea,bRes);
    }


    public static ParseResult generateForStatement(Object name, Object expression, Object body){
        ParseResult pName = (ParseResult)name;
        ParseResult pExp = (ParseResult)expression;
        ParseResult pBody = (ParseResult)body;
        Bloque bExp = ParseResult.getAs(pExp);
        Bloque bBody = ParseResult.getAs(body);
        Name nombre = ParseResult.getAs(pName);

        LinkedList<Instruccion> instrucionesComienzoIteracion = new LinkedList<>();
        instrucionesComienzoIteracion.addLast(new Instruccion(pExp.linea, OpCode.GET_ITER, 0));
        instrucionesComienzoIteracion.addLast(new Instruccion(pExp.linea, OpCode.CREATE_LOOP, bBody.instrucciones.size() + 4));
        instrucionesComienzoIteracion.addLast(new Instruccion(pExp.linea, OpCode.FOR_ITER, bBody.instrucciones.size() + 3));
        instrucionesComienzoIteracion.addLast(new Instruccion(pExp.linea, OpCode.STORE_NAME, nombre.index));

        bExp.instrucciones.addAll(instrucionesComienzoIteracion);

        LinkedList<Instruccion> instruccionesFinIteracion = new LinkedList<>();
        instruccionesFinIteracion.add(new Instruccion(bBody.instrucciones.getLast().linea,OpCode.CONTINUE_LOOP,0));
        instruccionesFinIteracion.add(new Instruccion(bBody.instrucciones.getLast().linea,OpCode.DESTROY_LOOP,1));

        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bExp.instrucciones,bBody,instruccionesFinIteracion);
        return new ParseResult(bBody.instrucciones.getLast().linea,bRes);
        //primero cargo la expresion
        //get iter
        //create loop N
        //for iter N-1
        //bloque (N-3 instrucciones)
        //continue loop
        //destroy loop 1

    }

    public static ParseResult joinBloques(Object stmt, Object stmtlist){
        Bloque bStmt = ParseResult.getAs(stmt);
        Bloque bList = ParseResult.getAs(stmtlist);
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bStmt.instrucciones,bList,null);
        return new ParseResult(bStmt.instrucciones.getLast().linea,bRes);
    }

    public static ParseResult generateSubscript(Object br, Object exp, Object rest){
        int line = ((LexerToken)br).NumeroFila+1;
        if(rest==null){
            return (ParseResult)exp;
        }else{
            Bloque bExp = ParseResult.getAs(exp);
            bExp.instrucciones.addLast(new Instruccion(line, OpCode.GET_INDEX, 0));
            Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(null,bExp,ParseResult.getAs(rest));
            return new ParseResult(line,bExp);
        }
    }

    public static ParseResult generateIfStatement(Object exp,Object colon1,Object suite1,Object
            colon2,Object suite2){
        int linea = ((LexerToken)colon1).NumeroFila+1;
        Bloque expBloque = ParseResult.getAs(exp);
        Bloque ifBloque = ParseResult.getAs(suite1);
        Bloque rBloque = null;
        Bloque lBloque = null;
        if (suite2 == null) {
            expBloque.instrucciones.addLast(new Instruccion(linea, OpCode.POP_JUMP_IF_FALSE, ifBloque.instrucciones.size() + 1));
            lBloque = ParserStatus.StackGenerador.peek().crearBloque(expBloque.instrucciones,ifBloque,null);
        } else {
            expBloque.instrucciones.addLast(new Instruccion(linea, OpCode.POP_JUMP_IF_FALSE, ifBloque.instrucciones.size() + 2));
            lBloque = ParserStatus.StackGenerador.peek().crearBloque(expBloque.instrucciones,ifBloque,null);
            int linea2 = ((LexerToken)colon2).NumeroFila+1;
            rBloque = ParseResult.getAs(suite2);
            lBloque.instrucciones.addLast(new Instruccion(linea2, OpCode.JUMP_FORWARD, rBloque.instrucciones.size() + 1));
        }
        Bloque b = ParserStatus.StackGenerador.peek().crearBloque(lBloque.instrucciones,rBloque,null);
        return new ParseResult(linea,b);
    }

    public static ParseResult generateWhileStatement(Object exp,Object colon,Object suite){
        int linea = ((LexerToken)colon).NumeroFila+1;
        Bloque expBloque = ParseResult.getAs(exp);
        Bloque whileBloque = ParseResult.getAs(suite);

        expBloque.instrucciones.addFirst(new Instruccion(linea, OpCode.CREATE_LOOP, expBloque.instrucciones.size() + whileBloque.instrucciones.size() + 3));
        expBloque.instrucciones.addLast(new Instruccion(linea, OpCode.POP_JUMP_IF_FALSE, whileBloque.instrucciones.size() + 2));

        whileBloque.instrucciones.addLast(new Instruccion(linea, OpCode.CONTINUE_LOOP ,0));
        whileBloque.instrucciones.addLast(new Instruccion(linea, OpCode.DESTROY_LOOP ,0));

        Bloque b = ParserStatus.StackGenerador.peek().crearBloque(expBloque.instrucciones,whileBloque,null);

        return new ParseResult(linea,b);
    }

    public static ParseResult generateFullSubscript(Object exp1, Object sub){
        int line = ((ParseResult)exp1).linea;
        Bloque bExp = ParseResult.getAs(exp1);
        Bloque bSub = ParseResult.getAs(sub);
        bSub.instrucciones.add(new Instruccion(line,OpCode.GET_INDEX,0));
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bExp.instrucciones,bSub,null);
        return new ParseResult(line,bRes);
    }

    public static ParseResult generateSubscriptAssignation(Object m, Object au,Object nc){
        int line = ((LexerToken)au).NumeroFila+1;
        Bloque bExpSub = ParseResult.getAs(m);
        Bloque bAs = ParseResult.getAs(nc);
        bExpSub.instrucciones.add(new Instruccion(line,OpCode.SET_INDEX,0));
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bAs.instrucciones,bExpSub,null);
        return new ParseResult(line,bRes);
    }

    public static ParseResult generateSubscriptTupleMakerSingleAssignation(Object m,Object assign,Object tuplemaker){
        int linea = ((LexerToken)assign).NumeroFila+1;

        Bloque bExpSub = ParseResult.getAs(m);

        ParseResult pTrail = (ParseResult)tuplemaker;
        Bloque bTrail = ParseResult.getAs(tuplemaker);

        bTrail.instrucciones.addLast(new Instruccion(linea,OpCode.CREATE_TUPLE,pTrail.argumentos));
        bExpSub.instrucciones.addLast(new Instruccion(linea,OpCode.SET_INDEX,0));

        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bTrail.instrucciones,bExpSub,null);
        return new ParseResult(linea,bRes);
    }

    public static ParseResult generateSubscriptForAssign(Object exp1, Object sub){
        int line = ((ParseResult)exp1).linea;
        Bloque bExp = ParseResult.getAs(exp1);
        Bloque bSub = ParseResult.getAs(sub);
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bExp.instrucciones,bSub,null);
        return new ParseResult(line,bRes);
    }

    public static ParseResult addSetIndex(Object n){
        ParseResult pr = (ParseResult)n;
        int line = pr.linea;
        Bloque b = ParseResult.getAs(pr);
        b.instrucciones.add(new Instruccion(line,OpCode.SET_INDEX,0));
        ParseResult res = new ParseResult(line,b);
        res.argumentos=1;
        return res;
    }

    public static ParseResult generateFunParams(Object name, Object rest){
        LexerToken l = (LexerToken)name;
        int numeroFila = l.NumeroFila+1;
        if(rest==null){
            LinkedList<LexerToken> lista = new LinkedList<>();
            lista.add(l);
            return new ParseResult(numeroFila,lista);
        }else{
            LinkedList<LexerToken> lista = ParseResult.getAs(rest);
            lista.addFirst(l);
            return new ParseResult(numeroFila,lista);
        }
    }

    public static void generateNewScope(){
        ParserStatus.StackGenerador.push(new Generador());
    }

    public static ParseResult generateFunctionDef(Object def, Object fName, Object fParams, Object fCode ){
        LexerToken l = (LexerToken)def;
        int numeroLinea = l.NumeroFila+1;
        Generador genFunc = ParserStatus.StackGenerador.pop();
        int numLineLastInst = ((Bloque)ParseResult.getAs(fCode)).instrucciones.getLast().linea;
        Const none = genFunc.createOrGetConst(ConstCreator.createPyNone());

        Instruccion cargarNone = new Instruccion(numeroLinea+numLineLastInst,OpCode.LOAD_CONST,none.index);
        Instruccion returnNone = new Instruccion(numeroLinea+numLineLastInst,OpCode.RETURN_VALUE,0);
        Bloque bCode = ParseResult.getAs(fCode);
        bCode.instrucciones.addLast(cargarNone);
        bCode.instrucciones.addLast(returnNone);

        Name nombreFuncion = ParseResult.getAs(fName);
        Code codigoFunc = genFunc.crearCodigo(nombreFuncion.value,"lala",bCode);
        codigoFunc.co_arguments = new ArrayList<>();
        if(fParams!=null){
            LinkedList<LexerToken> params = ParseResult.getAs(fParams);
            for(LexerToken t :params ){
                codigoFunc.co_arguments.add(t.TokenValue);
            }
        }
        Const constCodigo = ParserStatus.StackGenerador.peek().createOrGetConst(codigoFunc);
        Instruccion loadFunc = new Instruccion(numeroLinea,OpCode.LOAD_CONST,constCodigo.index);
        Instruccion createFunc = new Instruccion(numeroLinea,OpCode.CREATE_FUNC,0);
        LinkedList<Instruccion> instruccionesFuncion = new LinkedList<>();
        instruccionesFuncion.add(loadFunc);
        instruccionesFuncion.add(createFunc);
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(instruccionesFuncion,null,null);
        return new ParseResult(numeroLinea,bRes);
    }

    public static ParseResult generateReturnEmpty(Object ret){
        LexerToken lRet = (LexerToken)ret;
        int linea = lRet.NumeroFila+1;
        Instruccion returnNone = new Instruccion(linea,OpCode.RETURN_VALUE,0);
        Const none = ParserStatus.StackGenerador.peek().createOrGetConst(ConstCreator.createPyNone());
        Instruccion cargarNone = new Instruccion(linea,OpCode.LOAD_CONST,none.index);
        LinkedList<Instruccion> instrucciones = new LinkedList<>();
        instrucciones.add(cargarNone);
        instrucciones.add(returnNone);
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(instrucciones,null,null);
        return new ParseResult(linea,bRes);
    }

    public static ParseResult generateReturnTup(Object ret, Object exp){
        LexerToken lRet = (LexerToken)ret;
        int linea = lRet.NumeroFila+1;
        Instruccion returnTuple = new Instruccion(linea,OpCode.RETURN_VALUE,0);
        ParseResult prRes = (ParseResult)exp;
        Bloque bExp = ParseResult.getAs(exp);
        Instruccion crearTupla = new Instruccion(linea,OpCode.CREATE_TUPLE,prRes.argumentos);
        bExp.instrucciones.addLast(crearTupla);
        bExp.instrucciones.addLast(returnTuple);
        return new ParseResult(linea,bExp);
    }

    public static ParseResult generateReturnExp(Object ret,Object exp){
        LexerToken lRet = (LexerToken)ret;
        int linea = lRet.NumeroFila+1;
        Instruccion returnExp = new Instruccion(linea,OpCode.RETURN_VALUE,0);
        Bloque bRes = ParseResult.getAs(exp);
        bRes.instrucciones.addLast(returnExp);
        return new ParseResult(linea,bRes);
    }

    public static ParseResult generateNamedArgument(Object name, Object exp){
        LexerToken lName = (LexerToken)name;
        int line = lName.NumeroFila+1;
        Const pName = ParserStatus.StackGenerador.peek().createOrGetConst(ConstCreator.createPyString(lName.TokenValue));
        Instruccion loadParamName = new Instruccion(line,OpCode.LOAD_CONST,pName.index);
        Bloque bRes = ParseResult.getAs(exp);
        bRes.instrucciones.addFirst(loadParamName);
        ParseResult pRes = new ParseResult(line,bRes);
        pRes.argumentos2=1;
        return pRes;
    }

    public static ParseResult generateNamedArgumentList(Object namedArg,Object coma,Object list){
        LexerToken lComa = (LexerToken)coma;
        ParseResult prList = (ParseResult)list;
        int line = lComa.NumeroFila+1;
        Bloque bNA = ParseResult.getAs(namedArg);
        Bloque bL = ParseResult.getAs(list);
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(bNA.instrucciones,bL,null);
        ParseResult pRes = new ParseResult(line,bRes);
        pRes.argumentos2 = prList.argumentos2+1;
        return pRes;
    }

    public static ParseResult generateJoinedArguments(Object args,Object br){
        ParseResult prArgs =(ParseResult)args;
        LexerToken token = (LexerToken)br;
        int line = token.NumeroFila+1;
        Bloque bArgs = ParseResult.getAs(prArgs);
        ParseResult res = new ParseResult(line,bArgs);
        res.argumentos = prArgs.argumentos;
        res.argumentos2 = prArgs.argumentos2;
        return res;
    }

    public static ParseResult generateBreak(Object b){
        int linea = ((LexerToken)b).NumeroFila;
        LinkedList<Instruccion> instrucciones = new LinkedList<>();
        instrucciones.add(new Instruccion(linea,OpCode.BREAK_LOOP,0));
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(instrucciones,null,null);
        return new ParseResult(linea,bRes);
    }

    public static ParseResult generateContinue(Object c){
        int linea = ((LexerToken)c).NumeroFila;
        LinkedList<Instruccion> instrucciones = new LinkedList<>();
        instrucciones.add(new Instruccion(linea,OpCode.CONTINUE_LOOP,0));
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(instrucciones,null,null);
        return new ParseResult(linea,bRes);
    }

    public static ParseResult addPop(Object n){
        ParseResult pr = (ParseResult)n;
        int line = pr.linea;
        Bloque b = ParseResult.getAs(pr);
        b.instrucciones.add(new Instruccion(line,OpCode.POP_TOP,0));
        ParseResult res = new ParseResult(line,b);
        res.argumentos=1;
        return res;
    }

    public static ParseResult generateEmptyBlock(Object n){
        int linea = ((LexerToken)n).NumeroFila;
        Bloque bRes = ParserStatus.StackGenerador.peek().crearBloque(null,null,null);
        return new ParseResult(linea,bRes);
    }

}


