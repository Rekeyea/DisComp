package com.pyjava.core.runtime;

/**
 * Created by Cristiano on 12/07/2015.
 */
public class LoopBlock {

    public int instr_inicio = 0;
    public int instr_fin = 0;

    public LoopBlock(int inicio, int fin){
        this.instr_inicio = inicio;
        this.instr_fin = fin;
    }
}
