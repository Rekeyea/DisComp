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
        for(String line : lines){
            if(!line.trim().isEmpty()){
                builder.append(line);
                builder.append(System.lineSeparator());
            }
        }
        return new ByteArrayInputStream(builder.toString().getBytes("UTF-8"));

    }
}
