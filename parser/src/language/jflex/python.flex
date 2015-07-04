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
    return symbol(sym1.DEDENT);
    return symbol(sym1.EOF);
%eofval}

NEWLINE = \r|\n|\r\n
WHITESPACE = [ \t\f]
INPUTCHARACTER = [^\r\n]
TAB = \t
COMMENT     = "#" {INPUTCHARACTER}* {NEWLINE}?
ASSIGN = "="
INTEGER = [0-9]+
LONG = {INTEGER}("L"|"l")
_FRACTION = "."[0-9]+
_POINTFLOAT = {INTEGER}? _FRACTION | {INTEGER}"."
_EXP = ("e"|"E")("+"|"-")?{INTEGER}
_EXPNUM = ({INTEGER}|{_POINTFLOAT}) _EXP
FLOAT = {_POINTFLOAT}|{_EXPNUM}
STRING = \"|\'
TRIPLE_STRING = (\"\"\")|(\'\'\')

NAME = ([:jletter:]|_)([:jletterdigit:]|_)*


%state STRING
%state TRIPLE_STRING

%%

/*REGLAS LEXICAS*/

<YYINITIAL> {

    "del"                     {return symbol(sym1.DEL, yytext());}
    "from"                    {return symbol(sym1.FROM, yytext());}
    "while"                   {return symbol(sym1.WHILE, yytext());}
    "as"                      {return symbol(sym1.AS, yytext());}
    "elif"                    {return symbol(sym1.ELIF, yytext());}
    "global"                  {return symbol(sym1.GLOBAL, yytext());}
    "with"                    {return symbol(sym1.WITH, yytext());}
    "assert"                  {return symbol(sym1.ASSERT, yytext());}
    "else"                    {return symbol(sym1.ELSE, yytext());}
    "if"                      {return symbol(sym1.IF, yytext());}
    "pass"                    {return symbol(sym1.PASS, yytext());}
    "yield"                   {return symbol(sym1.YIELD, yytext());}
    "break"                   {return symbol(sym1.BREAK, yytext());}
    "except"                  {return symbol(sym1.EXCEPT, yytext());}
    "import"                  {return symbol(sym1.IMPORT, yytext());}
    "class"                   {return symbol(sym1.CLASS, yytext());}
    "exec"                    {return symbol(sym1.EXEC, yytext());}
    "in"                      {return symbol(sym1.IN, yytext());}
    "raise"                   {return symbol(sym1.RAISE, yytext());}
    "continue"                {return symbol(sym1.CONTINUE, yytext());}
    "finally"                 {return symbol(sym1.FINALLY, yytext());}
    "is"                      {return symbol(sym1.IS, yytext());}
    "return"                  {return symbol(sym1.RETURN, yytext());}
    "def"                     {return symbol(sym1.DEF, yytext());}
    "for"                     {return symbol(sym1.FOR, yytext());}
    "lambda"                  {return symbol(sym1.LAMBDA, yytext());}
    "try"                     {return symbol(sym1.TRY, yytext());}
    "type"                    {return symbol(sym1.TYPE, yytext());}
    "print"                   {return symbol(sym1.PRINT, yytext());}

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

    "+"                       {return symbol(sym1.PLUS, yytext());}
    "-"                       {return symbol(sym1.MINUS, yytext());}
    "**"                      {return symbol(sym1.EXP, yytext());}
    "*"                       {return symbol(sym1.MULT, yytext());}
    "/"                       {return symbol(sym1.DIV, yytext());}
    "//"                      {return symbol(sym1.DIVE, yytext());}
    "%"                       {return symbol(sym1.MOD, yytext());}

    "&"                       {return symbol(sym1.ANDB, yytext());}
    "|"                       {return symbol(sym1.ORB, yytext());}
    "^"                       {return symbol(sym1.XORB, yytext());}
    "~"                       {return symbol(sym1.NOTB, yytext());}
    "<<"                      {return symbol(sym1.SHIFTL, yytext());}
    ">>"                      {return symbol(sym1.SHIFTR, yytext());}

    "and"                     {return symbol(sym1.AND, yytext());}
    "or"                      {return symbol(sym1.OR, yytext());}
    "not"                     {return symbol(sym1.NOT, yytext());}
    "=="                      {return symbol(sym1.EQUALS, yytext());}
    "!="                      {return symbol(sym1.DIFF, yytext());}
    "<"                       {return symbol(sym1.MINOR, yytext());}
    ">"                       {return symbol(sym1.MAJOR, yytext());}
    "<="                      {return symbol(sym1.MINOREQ, yytext());}
    ">="                      {return symbol(sym1.MAJOREQ, yytext());}
    "="                       {return symbol(sym1.ASSIGN, yytext());}

    {COMMENT}                 { }
    {NEWLINE}                 {return symbol(sym1.NEWLINE, yytext());}
    {WHITESPACE}{WHITESPACE}{WHITESPACE}{WHITESPACE} {return symbol(sym1.INDENT);}
    {WHITESPACE}              {}
    {TAB}                     {return symbol(sym1.TAB, yytext());}
    {NAME}                    {return symbol(sym1.NAME, yytext());}
    {ASSIGN}                  {return symbol(sym1.NAME, yytext());}
    {LONG}                    {return symbol(sym1.LONG, yytext());}
    {INTEGER}                 {return symbol(sym1.INTEGER, yytext());}
    {FLOAT}                   {return symbol(sym1.FLOAT, yytext());}
    {STRING}                  {string.setLength(0); yybegin(STRING);}
    {TRIPLE_STRING}           {string.setLength(0); yybegin(TRIPLE_STRING);}

}

<STRING> {
  \"|\'                       {yybegin(YYINITIAL); return symbol(sym1.STRING, string.toString());}
  [^\r\n\"\\]+                {string.append( yytext() );}
  \\t                         {string.append('\t');}
  \\n                         {string.append('\n');}
  \\r                         {string.append('\r');}
  \\\"                        {string.append('\"');}
  \\                          {string.append('\\');}
}

<TRIPLE_STRING> {
  (\"\"\")|(\'\'\')           {yybegin(YYINITIAL); return symbol(sym1.STRING3, string.toString());}
  [^((\"\"\")|(\'\'\'))]+     {string.append(yytext());}
}

[^]                           {throw new Error("Illegal Character < "+yytext()+" >");}