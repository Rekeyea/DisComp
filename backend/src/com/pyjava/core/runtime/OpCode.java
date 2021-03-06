package com.pyjava.core.runtime;

/**
 * Created by Cristiano on 28/06/2015.
 * Clase estatica que almacena todas las posibles instrucciones
 */
public class OpCode {

    //------------ Operaciones de llamada/retorno -----------

    /**
     * Nota: TOS = Top of the stack, o sea, tope del stack del frame actual.
     CALL_FUNCTION(argc): Realiza la llamada a una funci�n, donde argc indica la cantidad de argumentos involucrados en la llamada. En un principio, solo cantidad, en un futuro, el valor deber� indicar argumentos tanto posicionales como por nombre.
     En el stack, se deber� encontrar lo siguiente [ argN, argN-1, �, arg1, obj_func ], donde a la izquierda se tiene el tope del stack, y obj_func es el objeto a invocar.
     RETURN_VALUE: Retorna de la funci�n actual, poniendo en el stack de la funci�n que lo invoc� el resultado de esta.
     */

    public final static int CALL_FUNCTION = 1;    //Llamar a funcion, el argumento es la cantidad de argumentos de la funcion
    public final static int RETURN_VALUE = 2;     //Retorna de una funcion. Argumento es ignorado, deja en el tope del stack el resultado.


    //------------ Operaciones de manejo de variables -----------
    /**
     * Se hace referencia a variables tanto de frame y de code, definidas anteriormente.
     NO USADA -- LOAD_GLOBAL(i) : TOS = f_globals [ co_names[i] ] : Esta es a su vez un poco especial, en caso de no encontrar el nombre en f_globals, deber� finalmente buscar en los builtins.
     LOAD_CONST(i) : TOS = co_consts[i]
     LOAD_FAST(i) : TOS = f_locals [ co_varnames[i] ]
     LOAD_NAME(i) : TOS = f_locals [ co_names[i] ] : Si no lo encuentra, busca en f_globals, y si no lo encuentra, en builtins.
     LOAD_ATTR(i) : TOS = TOS.__getattr__(co_names[i]) : llama al __getattr__ del objeto
     STORE_FAST(i) : f_locals [ co_varnames[i] ] = TOS : Reemplaza o crea una nueva entrada en f_locals
     STORE_NAME(i) : f_locals[ co_names[i] ] = TOS : Se usa m�s que nada para c�digo de m�dulo
     */

    //public final static int LOAD_GLOBAL = 3; //Decidido no usarla, que LOAD_NAME busque en globals si no lo encuentra en locals, menos eficiente, pero facilita el parser.
    public final static int LOAD_CONST = 4;
    public final static int LOAD_FAST = 5;
    public final static int LOAD_NAME = 6;
    public final static int LOAD_ATTR = 7;
    public final static int STORE_FAST = 8;
    public final static int STORE_NAME = 9;


    //------------ Operaciones Unarias -----------

    /**
     UNARY_INVERT : TOS = ~TOS (not bitwise, invertir)
     UNARY_NEGATIVE: TOS = -TOS (negativo, mult * -1)
     UNARY_NOT : TOS = not TOS (en nuestro caso, not __bool__())
     */

    public final static int UNARY_INVERT = 10;
    public final static int UNARY_NEGATIVE = 11;
    public final static int UNARY_NOT = 12;

    //------------ Operaciones Binarias -----------

    /**
     * Todas estas, act�an sobre TOS y TOS1 (el siguiente del stack)
     BINARY_POW : TOS = TOS1 ** TOS
     BINARY_MUL : TOS = TOS1 * TOS
     BINARY_DIV : TOS = TOS1 / TOS
     BINARY_FLOOR_DIV : TOS = TOS1 // TOS
     BINARY_MOD : TOS = TOS1 % TOS
     BINARY_ADD : TOS = TOS1 + TOS
     BINARY_SUB : TOS = TOS1 � TOS
     BINARY_LSHIFT : TOS = TOS1 << TOS
     BINARY_RSHIFT : TOS = TOS1 >> TOS
     BINARY_AND : TOS = TOS1 & TOS
     BINARY_XOR : TOS = TOS1 ^ TOS
     BINARY_OR : TOS = TOS1 | TOS
     COMPARE_OP : TOS = TOS1 op TOS : donde op puede ser ==, !=, <, >, <=, >=, definida en CompareCode.
     */

    public final static int BINARY_POW = 13;
    public final static int BINARY_MUL = 14;
    public final static int BINARY_DIV = 15;
    public final static int BINARY_FLOOR_DIV = 16;
    public final static int BINARY_MOD = 17;
    public final static int BINARY_ADD = 18;
    public final static int BINARY_SUB = 19;
    public final static int BINARY_LSHIFT = 20;
    public final static int BINARY_RSHIFT = 21;
    public final static int BINARY_AND = 22;
    public final static int BINARY_XOR = 23;
    public final static int BINARY_OR = 24;
    public final static int COMPARE_OP = 25;


    //------------ Operaciones Salida -----------

    /**
     *
     PRINT_ITEM : print TOS
     PRINT_NEWLINE : imprime \n
     */

    public final static int PRINT_ITEM = 26;
    public final static int PRINT_NEWLINE = 27;


    //---------- Operaciones de salto --------------------------------
    /** Nota: f_instr es el contador de instruccion del frame actual, que va desde 0 a N-1, siendo N la cantidad de instrucciones del codigo del frame.
     * No se hace ningun chequeo por que la proxima instruccion sea valida, queda como responsabilidad del compilador.
     *
     * JUMP_FORWARD(i) : Aumenta f_instr en i, o sea, f_instr += i
     * POP_JUMP_IF_TRUE(i) : Si TOS evalua en True, f_instr = i. TOS es siempre popeado
     * POP_JUMP_IF_FALSE(i) : Si TOS evalua en False, f_instr = i. TOS es siempre popeado
     * JUMP_IF_TRUE_OR_POP(i) : Si TOS evalua en True, f_instr = i y deja el stack intacto, si no, se popea TOS. (y avanza instruccion)
     * JUMP_IF_FALSE_OR_POP(i) : Si TOS evalua en False, f_instr = i y deja el stack intacto, si no, se popea TOS. (y avanza instruccion)
     * JUMP_ABSOLUTE(i) : f_instr = i
     * POP_JUMP_FORWARD(i) : Hace pop y aumenta f_instr en i, o sea, f_instr += 1
     * POP_JUMP_ABSOLUTE(i) : Hace pop y f_instr = i
     *
     */

    public final static int JUMP_FORWARD = 28;
    public final static int POP_JUMP_IF_TRUE = 29;
    public final static int POP_JUMP_IF_FALSE = 30;
    public final static int JUMP_IF_TRUE_OR_POP = 31;
    public final static int JUMP_IF_FALSE_OR_POP = 32;
    public final static int JUMP_ABSOLUTE = 33;
    public final static int POP_JUMP_FORWARD = 34;
    public final static int POP_JUMP_ABSOLUTE = 35;


    //------------ Operaciones condicionales (and, or, not) -----------

        //El not ya esta implementado: UNARY_NOT
        //Las demas construcciones, las hace el parser, encadenando y evaluaciones JUMPS.


    //------------ Operaciones loops -----------

    /** En todos los casos se va a lanzar excepcion si el objeto no es un iterable.
     *
     * GET_ITER : Hace pop del stack, Llama a __iter__ del objeto popeado, y pushea el resultado en el stack.
     * FOR_ITER(i) : Llama a __next__ del objeto en TOS. Si se llego al final de la iteracion, se avanza la instruccion actual i posiciones y se popea TOS.
     *               En caso contrario, se pushea el valor de la proxima iteracion (no se popea el iterador en este caso)
     *
     * Algunas guias para la implementacion de LOOPS:
     *      - Para los FOR, siempre que haya un BREAK hay que hacer POP del stack (se puede usar POP_JUMP_FORWARD o POP_JUMP_ABSOLUTE
     *              Esto es para eliminar el iterador que queda en el stack.
     *
     *      - Para el caso de los WHILE, no deberia ser necesario hacer POP, a menos que se haya dejado algo en el stack (cosa que no es probable, porque siempre se asigna algo a alguna variable)
     *
     *      - Escenario tipico de un for:
     *          0: GET_ITER
     *          1: FOR_ITER(5) : si no hay mas elementos, hace jump 5 lugares para ir a la instruccion 6
     *          2: ... instr ...
     *          3: ... instr ...
     *          4: ... instr ...
     *          5: JUMP_ABSOLUTE(1) : fin del for, vuelvo arriba.
     *          6: ... instr fuera del for ....
     */


    public final static int GET_ITER = 36;
    public final static int FOR_ITER = 37;




    //------------ Operaciones listas -----------

    //Pendiente

    //------------ Operaciones diccionarios -----------

    //Pendiente


    //------------ Operaciones tuplas -----------

    //Pendiente



    //------------ Operaciones de creacion de funciones -----------

        //Pendiente



    //Fin de ejecucion.
    public final static int FIN_EJECUCION = -1;


    //---------- Codigos de comparaciones ---------

    public static class CompareCode{
        public final static int EQ = 1;       //Equals
        public final static int NEQ = 2;      //Not equals
        public final static int GT = 3;       //Greater
        public final static int LT = 4;       //Lesser
        public final static int GE = 5;       //Greater equals
        public final static int LE = 6;       //Lesser equals

    }

}
