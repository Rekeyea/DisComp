/*SECCIÓN DE CÓDIGO DE USUARIO*/
import java_cup.runtime.*;
import jflex.sym;

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

/*
preciso saber:

    NAME
    NEWLINE
    ENDMARKER
    INDENT
    DEDENT
    NUMBER
    STRING
*/

NAME = ([:jletter:]|_)([:jletterdigit:]|_)*
KEYWORD = "and"|"del"|"from"|"not"|"while"|"as"|"elif"|"global"|"or"|"with"|"assert"|"else"|"if"|"pass"|"yield"|"break"|"except"|"import"|"print"|"class"|"exec"|"in"|"raise"|"continue"|"finally"|"is"|"return"|"def"|"for"|"lambda"|"try"
INTEGER = [0-9]+
LONG = {INTEGER}("L"|"l")
_FRACTION = "."[0-9]+
_POINTFLOAT = {INTEGER}? _FRACTION | {INTEGER}"."
_EXP = ("e"|"E")("+"|"-")?{INTEGER}
_EXPNUM = ({INTEGER}|{_POINTFLOAT}) _EXP
FLOAT = {_POINTFLOAT}|{_EXPNUM}
NEWLINE = \r|\n|\r\n
WHITESPACE = \s*|\t*
PLUS = "+"
MINUS = "-"
MULT = "*"
DIV = "/"
EQUALS = "="

%state STRING

%%

/*REGLAS LEXICAS*/

<YYINITIAL> {
    {Identifier}        {return symbol(sym.IDENT,yytext());}
    {IntegralNumber}    {return symbol(sym.DIGITCLASS,yytext());}
    "+"                 {return symbol(sym.PLUS);}
    "-"                 {return symbol(sym.DIFFERENCE);}
    "*"                 {return symbol(sym.STAR);}
    "/"                 {return symbol(sym.BAR);}
}

[^]                     {throw new Error("Illegal Character < "+yytext()+" >");}