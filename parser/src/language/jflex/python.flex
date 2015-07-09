/*SECCIÓN DE CÓDIGO DE USUARIO*/
import java_cup.runtime.*;
import jflex.sym;
import java.util.Deque;
import java.util.LinkedList;

%%
/*SECCIÓN DE DEFINICIONES*/

%class Lexer
%unicode
%cup
%line
%column

%{
    Deque<Integer> Stack = new LinkedList<Integer>();

    StringBuffer string = new StringBuffer();

    private Symbol symbol(int type){
        return new Symbol(type,yyline,yycolumn);
    }

    private Symbol symbol(int type,Object value){
        return new Symbol(type,yyline,yycolumn,value);
    }


    public int indentLevel = 0;
    public int indentSentence = 0;

%}

%eofval{
    if(Stack.size()==0){
        return symbol(sym1.EOF);
    }else{
        yypushback(1);
        return symbol(sym1.DEDENT);
    }
%eofval}

NEWLINE = \r|\n|\r\n
WHITESPACE = [ \t\f]
INDENT_TOKEN = ("    ")*
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
INDENT = [ \t\f]{3}

NAME = ([:jletter:]|_)([:jletterdigit:]|_)*


%state SENTENCE
%state STRING
%state TRIPLE_STRING
%state INDENTATION

%%

/*REGLAS LEXICAS*/

<YYINITIAL> {
    {INDENT}                  {indentSentence++; if(indentSentence > indentLevel) return symbol(sym1.INDENT, yytext());}

    "del"                     {yybegin(SENTENCE);return symbol(sym1.DEL, yytext());}
    "from"                    {yybegin(SENTENCE);return symbol(sym1.FROM, yytext());}
    "while"                   {yybegin(SENTENCE);return symbol(sym1.WHILE, yytext());}
    "as"                      {yybegin(SENTENCE);return symbol(sym1.AS, yytext());}
    "elif"                    {yybegin(SENTENCE);return symbol(sym1.ELIF, yytext());}
    "global"                  {yybegin(SENTENCE);return symbol(sym1.GLOBAL, yytext());}
    "with"                    {yybegin(SENTENCE);return symbol(sym1.WITH, yytext());}
    "assert"                  {yybegin(SENTENCE);return symbol(sym1.ASSERT, yytext());}
    "else"                    {yybegin(SENTENCE);return symbol(sym1.ELSE, yytext());}
    "if"                      {yybegin(SENTENCE);return symbol(sym1.IF, yytext());}
    "pass"                    {yybegin(SENTENCE);return symbol(sym1.PASS, yytext());}
    "yield"                   {yybegin(SENTENCE);return symbol(sym1.YIELD, yytext());}
    "break"                   {yybegin(SENTENCE);return symbol(sym1.BREAK, yytext());}
    "except"                  {yybegin(SENTENCE);return symbol(sym1.EXCEPT, yytext());}
    "import"                  {yybegin(SENTENCE);return symbol(sym1.IMPORT, yytext());}
    "class"                   {yybegin(SENTENCE);return symbol(sym1.CLASS, yytext());}
    "exec"                    {yybegin(SENTENCE);return symbol(sym1.EXEC, yytext());}
    "in"                      {yybegin(SENTENCE);return symbol(sym1.IN, yytext());}
    "raise"                   {yybegin(SENTENCE);return symbol(sym1.RAISE, yytext());}
    "continue"                {yybegin(SENTENCE);return symbol(sym1.CONTINUE, yytext());}
    "finally"                 {yybegin(SENTENCE);return symbol(sym1.FINALLY, yytext());}
    "is"                      {yybegin(SENTENCE);return symbol(sym1.IS, yytext());}
    "return"                  {yybegin(SENTENCE);return symbol(sym1.RETURN, yytext());}
    "def"                     {yybegin(SENTENCE);return symbol(sym1.DEF, yytext());}
    "for"                     {yybegin(SENTENCE);return symbol(sym1.FOR, yytext());}
    "lambda"                  {yybegin(SENTENCE);return symbol(sym1.LAMBDA, yytext());}
    "try"                     {yybegin(SENTENCE);return symbol(sym1.TRY, yytext());}
    "type"                    {yybegin(SENTENCE);return symbol(sym1.TYPE, yytext());}
    "print"                   {yybegin(SENTENCE);return symbol(sym1.PRINT, yytext());}

    ","                       {yybegin(SENTENCE);return symbol(sym1.COMA, yytext());}
    "."                       {yybegin(SENTENCE);return symbol(sym1.DOT, yytext());}
    ":"                       {yybegin(SENTENCE);return symbol(sym1.COLON, yytext());}
    ";"                       {yybegin(SENTENCE);return symbol(sym1.SEMICOLON, yytext());}
    "("                       {yybegin(SENTENCE);return symbol(sym1.LPAREN, yytext());}
    ")"                       {yybegin(SENTENCE);return symbol(sym1.RPAREN, yytext());}
    "["                       {yybegin(SENTENCE);return symbol(sym1.LBRACKET, yytext());}
    "]"                       {yybegin(SENTENCE);return symbol(sym1.RBRACKET, yytext());}
    "{"                       {yybegin(SENTENCE);return symbol(sym1.LCURLY, yytext());}
    "}"                       {yybegin(SENTENCE);return symbol(sym1.RCURLY, yytext());}

    "+"                       {yybegin(SENTENCE);return symbol(sym1.PLUS, yytext());}
    "-"                       {yybegin(SENTENCE);return symbol(sym1.MINUS, yytext());}
    "**"                      {yybegin(SENTENCE);return symbol(sym1.EXP, yytext());}
    "*"                       {yybegin(SENTENCE);return symbol(sym1.MULT, yytext());}
    "/"                       {yybegin(SENTENCE);return symbol(sym1.DIV, yytext());}
    "//"                      {yybegin(SENTENCE);return symbol(sym1.DIVE, yytext());}
    "%"                       {yybegin(SENTENCE);return symbol(sym1.MOD, yytext());}

    "&"                       {yybegin(SENTENCE);return symbol(sym1.ANDB, yytext());}
    "|"                       {yybegin(SENTENCE);return symbol(sym1.ORB, yytext());}
    "^"                       {yybegin(SENTENCE);return symbol(sym1.XORB, yytext());}
    "~"                       {yybegin(SENTENCE);return symbol(sym1.NOTB, yytext());}
    "<<"                      {yybegin(SENTENCE);return symbol(sym1.SHIFTL, yytext());}
    ">>"                      {yybegin(SENTENCE);return symbol(sym1.SHIFTR, yytext());}

    "and"                     {yybegin(SENTENCE);return symbol(sym1.AND, yytext());}
    "or"                      {yybegin(SENTENCE);return symbol(sym1.OR, yytext());}
    "not"                     {yybegin(SENTENCE);return symbol(sym1.NOT, yytext());}
    "=="                      {yybegin(SENTENCE);return symbol(sym1.EQUALS, yytext());}
    "!="                      {yybegin(SENTENCE);return symbol(sym1.DIFF, yytext());}
    "<"                       {yybegin(SENTENCE);return symbol(sym1.MINOR, yytext());}
    ">"                       {yybegin(SENTENCE);return symbol(sym1.MAJOR, yytext());}
    "<="                      {yybegin(SENTENCE);return symbol(sym1.MINOREQ, yytext());}
    ">="                      {yybegin(SENTENCE);return symbol(sym1.MAJOREQ, yytext());}
    "="                       {yybegin(SENTENCE);return symbol(sym1.ASSIGN, yytext());}

    {COMMENT}                 { }
    {WHITESPACE}              { }
    {TAB}                     {yybegin(SENTENCE); return symbol(sym1.TAB, yytext());}
    {NAME}                    {yybegin(SENTENCE); return symbol(sym1.NAME, yytext());}
    {ASSIGN}                  {yybegin(SENTENCE); return symbol(sym1.NAME, yytext());}
    {LONG}                    {yybegin(SENTENCE); return symbol(sym1.LONG, yytext());}
    {INTEGER}                 {yybegin(SENTENCE); return symbol(sym1.INTEGER, yytext());}
    {FLOAT}                   {yybegin(SENTENCE); return symbol(sym1.FLOAT, yytext());}

    {NEWLINE}                 {indentLevel=indentSentence;indentSentence=0;return symbol(sym1.NEWLINE, yytext());}
    {STRING}                  {string.setLength(0); yybegin(STRING);}
    {TRIPLE_STRING}           {string.setLength(0); yybegin(TRIPLE_STRING);}
}

<SENTENCE> {
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

    {NEWLINE}"    "{INDENT_TOKEN}   {yypushback(yylength()); yybegin(INDENTATION);}
    {NEWLINE}                 {return symbol(sym1.NEWLINE, yytext());}
    {WHITESPACE}              {}

    {TAB}                     {return symbol(sym1.TAB, yytext());}
    {ASSIGN}                  {return symbol(sym1.NAME, yytext());}
    {LONG}                    {return symbol(sym1.LONG, yytext());}
    {INTEGER}                 {return symbol(sym1.INTEGER, yytext());}
    {FLOAT}                   {return symbol(sym1.FLOAT, yytext());}
    {NEWLINE}                 {indentLevel=indentSentence;indentSentence=0;yybegin(YYINITIAL); return symbol(sym1.NEWLINE, yytext());}
    {STRING}                  {string.setLength(0); yybegin(STRING);}
    {TRIPLE_STRING}           {string.setLength(0); yybegin(TRIPLE_STRING);}
    {NAME}                    {return symbol(sym1.NAME, yytext());}
}

<STRING> {
  \"|\'                       {yybegin(SENTENCE); return symbol(sym1.STRING, string.toString());}
  [^\r\n\"\\]+                {string.append( yytext() );}
  \\t                         {string.append('\t');}
  \\n                         {string.append('\n');}
  \\r                         {string.append('\r');}
  \\\"                        {string.append('\"');}
  \\                          {string.append('\\');}
}

<TRIPLE_STRING> {
  (\"\"\")|(\'\'\')           {yybegin(SENTENCE); return symbol(sym1.STRING3, string.toString());}
  [^((\"\"\")|(\'\'\'))]+     {string.append(yytext());}
}


<INDENTATION>{
    {NEWLINE} {
        return symbol(sym1.NEWLINE,yytext());
    }
    {INDENT_TOKEN}
    {
      //HAY QUE VER BIEN COMO FUNCIONA EL yypushback PERO ESE ES EL CAMINO
      int indentLevel = yylength() / 4;
      int nivelStack = Stack.size() == 0 ? 0 : Stack.peek();
      if(indentLevel == nivelStack){
          yybegin(YYINITIAL);
      }else if(indentLevel < nivelStack){
          //tengo que emitir tokens DEDENT hasta llegar al nivel del stack
          Stack.pop();
          return symbol(sym1.DEDENT,yytext());
      }else{
          //aumento el nivel de indentacion
          nivelStack+=1;
          yypushback(4*(indentLevel-nivelStack));
          Stack.push(nivelStack);
          return symbol(sym1.INDENT,yytext());
      }
      //return symbol(sym1.INDENT,"");

    }


}

[^]                           {System.out.println("Illegal Character < "+yytext()+" > in state "+yystate());}
