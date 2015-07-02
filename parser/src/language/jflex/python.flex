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

%eofval{
    return symbol(sym1.EOF);
%eofval}

NEWLINE = \r|\n|\r\n
WHITESPACE = [ \t\f]
INPUTCHARACTER = [^\r\n]
COMMENT     = "#" {INPUTCHARACTER}* {NEWLINE}?
INTEGER = [0-9]+
LONG = {INTEGER}("L"|"l")
_FRACTION = "."[0-9]+
_POINTFLOAT = {INTEGER}? _FRACTION | {INTEGER}"."
_EXP = ("e"|"E")("+"|"-")?{INTEGER}
_EXPNUM = ({INTEGER}|{_POINTFLOAT}) _EXP
FLOAT = {_POINTFLOAT}|{_EXPNUM}
PLUS = "+"
MINUS = "-"
MULT = "*"
DIV = "/"
EQUALS = "=="
ASSIGN = "="
NAME = ([:jletter:]|_)([:jletterdigit:]|_)*


%state STRING
%state TRIPLE_STRING

%%

/*REGLAS LEXICAS*/

<YYINITIAL> {

    ","                       {return symbol(sym1.COMA, yytext());}
    "."                       {return symbol(sym1.DOT, yytext());}
    ":"                       {return symbol(sym1.COLON, yytext());}
    ";"                       {return symbol(sym1.SEMICOLON, yytext());}
    "("                       {return symbol(sym1.LPAREN, yytext());}
    ")"                       {return symbol(sym1.RPAREN, yytext());}
    "["                       {return symbol(sym1.LBRACKET, yytext());}
    "]"                       {return symbol(sym1.RBRACKET, yytext());}
    "{"                       {return symbol(sym1.LCURLY, yytext());}
    "}"                       {return symbol(sym1.RCURLY, yytext());}

    {COMMENT}                 {   }
    {INTEGER}                 {return symbol(sym1.INTEGER, yytext());}
    {WHITESPACE}              {return symbol(sym1.WHITESPACE, yytext());}
    {PLUS}                    {return symbol(sym1.PLUS, yytext());}
    {MINUS}                   {return symbol(sym1.MINUS, yytext());}
    {MULT}                    {return symbol(sym1.MULT, yytext());}
    {DIV}                     {return symbol(sym1.DIV, yytext());}
    {EQUALS}                  {return symbol(sym1.EQUALS, yytext());}
    {ASSIGN}                  {return symbol(sym1.ASSIGN, yytext());}
    (\"\"\")|(\'\'\')         { string.setLength(0); yybegin(TRIPLE_STRING);}
    \"|\'                     { string.setLength(0); yybegin(STRING);}
    {NAME}                    {return symbol(sym1.NAME, yytext());}
    {NEWLINE}                 {return symbol(sym1.NEWLINE, yytext());}
    <<EOF>>                   { return symbol(sym1.EOF); }
}

<STRING> {
  \"|\'                             { yybegin(YYINITIAL);
                                   return symbol(sym1.STRING, string.toString()); }
  [^\r\n\"\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}

<TRIPLE_STRING> {
  (\"\"\")|(\'\'\')                  { yybegin(YYINITIAL);
                                   return symbol(sym1.STRING3, string.toString()); }
  [^((\"\"\")|(\'\'\'))]+                          { string.append(yytext()); }
}

[^]                     {throw new Error("Illegal Character < "+yytext()+" >");}