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

NEWLINE = \r|\n|\r\n
WHITESPACE = [ \t\f]
INPUTCHARACTER = [^\r\n]
COMMENT     = "#" {INPUTCHARACTER}* {NEWLINE}?
KEYWORD = "and"|"del"|"from"|"not"|"while"|"as"|"elif"|"global"|"or"|"with"|"assert"|"else"|"if"|"pass"|"yield"|"break"|"except"|"import"|"print"|"class"|"exec"|"in"|"raise"|"continue"|"finally"|"is"|"return"|"def"|"for"|"lambda"|"try"
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
    {COMMENT}                 {   }
    {INTEGER}    {return symbol(sym.DIGITCLASS,yytext());}
    {WHITESPACE}    {return symbol(sym.WHITESPACECLASS);}
    {PLUS}                 {return symbol(sym.PLUS);}
    {MINUS}                 {return symbol(sym.DIFFERENCE);}
    {MULT}                 {return symbol(sym.STAR);}
    {DIV}                 {return symbol(sym.BAR);}
    {EQUALS}                 {return symbol(sym.EQUALS);}
    {ASSIGN}                 {return symbol(sym.EQUALS);}
    {KEYWORD}      {return symbol(sym.ACTION, yytext());}
    (\"\"\")|(\'\'\')              { string.setLength(0); yybegin(TRIPLE_STRING);}
    \"|\'              { string.setLength(0); yybegin(STRING); }
    {NAME}        {return symbol(sym.IDENT,yytext());}
    {NEWLINE}       {return symbol(sym.NEWLINE);}
}

<STRING> {
  \"|\'                             { yybegin(YYINITIAL);
                                   return symbol(sym.STRING,
                                   string.toString()); }
  [^\r\n\"\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}

<TRIPLE_STRING> {
  (\"\"\")|(\'\'\')                  { yybegin(YYINITIAL);
                                   return symbol(sym.STRING_I,
                                string.toString()); }
  [^((\"\"\")|(\'\'\'))]+                          { string.append(yytext()); }
}

[^]                     {throw new Error("Illegal Character < "+yytext()+" >");}