/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.linguisticbits.spotei.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author bernd
 */
public class NameSpaceCleanup {
    
    public static void CleanupNameSpaces(File file) throws UnsupportedEncodingException, IOException{
        StringBuilder result = new StringBuilder();
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String nextLine="";
        while ((nextLine = br.readLine()) != null){
            String cleanLine = nextLine
                    .replaceAll("tei:", "")
                    .replaceAll("xmlns:exmaralda=\"http://www.exmaralda.org\"", "")
                    .replaceAll("xmlns:tei=\"http://www.tei-c.org/ns/1.0\" ", "");
            result.append(cleanLine);
        }
        br.close();
        
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(result.toString());
        bw.close();        
        
    }
    
}
