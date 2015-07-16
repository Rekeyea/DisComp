package com.pyjava.parser.codegen;

import java.util.Objects;

/**
 * Created by rekeyea on 7/12/15.
 */
public class ParseResult {
    public int linea;
    public int argumentos;
    public int argumentos2;
    public Object value;

    public ParseResult(int l, Object o){
        this.linea = l;
        this.value = o;
        this.argumentos = 0;
        this.argumentos2 = 0;
    }

    public static <T> T getAs(Object p){
        return (T)((ParseResult)p).value;
    }
}
