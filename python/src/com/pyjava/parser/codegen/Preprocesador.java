package com.pyjava.parser.codegen;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rekeyea on 7/17/15.
 */
public class Preprocesador {
    public static InputStream Preprocesar(String filePath) throws IOException{
        List<String> lines = Files.readAllLines(Paths.get(filePath),Charset.defaultCharset());
        StringBuilder builder = new StringBuilder();
        boolean estadoComillaTriple = false;
        for(String line : lines){
            //hay que tener en cuenta tambien los strings con comillas triples
            if(estadoComillaTriple){
                if(line.contains("\"\"\"")){
                    estadoComillaTriple = false;
                }
                builder.append(line);
                builder.append(System.lineSeparator());
            }else{
                if(!line.trim().isEmpty()){
                    if(line.contains("\"\"\"")){
                        estadoComillaTriple = true;
                    }
                    builder.append(line);
                    builder.append(System.lineSeparator());
                }
            }
        }
        return new ByteArrayInputStream(builder.toString().getBytes("UTF-8"));

    }
}
