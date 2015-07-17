java -jar libs\jflex-1.6.1.jar --inputstreamctor -d src\com\pyjava\parser\codegen src\com\pyjava\parser\language\jflex\python.flex

java -jar libs\java-cup-11b.jar -destdir src\com\pyjava\parser -symbols sym1 src\com\pyjava\parser\language\cup\parser.cup

ant -buildfile build.xml