/*SECCIÓN DE CÓDIGO DE USUARIO*/
import java_cup.runtime.*;

%%
/*SECCIÓN DE DEFINICIONES*/

%class Lexer
%unicode
%cup
%line
%column

%{
    StringBuffer string = new StringBuffer();

    private Symbol symbol(int type){
        return new Symbol(type,yyline,yycolumn);
    }

    private  Symbol symbol(int type,Object value){
        return new Symbol(type,yyline,yycolumn,value);
    }
%}


LineTerminator = \r|\n|\r\n
WhiteSpace = \t+

IntegralNumber = 0|[1-9][0-9]*
Operator = "+"|"-"|"*"|"/"

Identifier = [:jletter:][:jletterdigit:]*

%%

/*REGLAS LEXICAS*/

<YYINITIAL> {
    {Identifier}        {return symbol(sym.IDENTIFIER);}
    {IntegralNumber}    {return symbol(sym.INTEGER_LITERAL);}
    "+"                 {return symbol(sym.PLUS);}
    "-"                 {return symbol(sym.MINUS);}
    "*"                 {return symbol(sym.STAR);}
    "/"                 {return symbol(sym.DIV);}
}

[^]                     {throw new Error("Illegal Character < "+yytext()+" >");}