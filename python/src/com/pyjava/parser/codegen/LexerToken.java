package com.pyjava.parser.codegen;

/**
 * Created by rekeyea on 7/11/15.
 */
public class LexerToken {
    public int NumeroColumna;
    public int NumeroFila;
    public int TokenType;
    public String TokenValue;

    public LexerToken(int ncol, int nrow, int type, String tokValue){
        this.NumeroColumna = ncol;
        this.NumeroFila = nrow;
        this.TokenType = type;
        this.TokenValue = tokValue;
    }
}
