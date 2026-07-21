/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.linguisticbits.spotei.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.Xslt30Transformer;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;

/**
 *
 * @author bernd
 */
public class Saxon12XSLTHelper implements XSLTHelper {

    private Map<QName, XdmValue> makeParameterMap(String[][] parameters){
        Map<QName, XdmValue> params = new HashMap<>();
        for (String[] paramPair : parameters){
            params.put(new QName(paramPair[0]), new XdmAtomicValue(paramPair[1]));
        }
        return params;
    }
    
    
    @Override
    public void transformXSLT(File xsltFile, File inputFile, File outputFile) throws IOException {
        String[][] emptyParams = {};
        transformXSLT(xsltFile, inputFile, outputFile, emptyParams);
    }
    
    
    @Override
    public void transformXSLT(File xsltFile, File inputFile, File outputFile, String[][] parameters) throws IOException {
        try {
            Processor proc = new Processor(false);   // false = no schema support
            XsltCompiler compiler = proc.newXsltCompiler();
            // Compile stylesheet
            XsltExecutable exec = compiler.compile(new StreamSource(xsltFile));
            // Load transformer
            Xslt30Transformer transformer = exec.load30();
            // set parameters
            transformer.setStylesheetParameters(makeParameterMap(parameters));
            // Run transform
            transformer.transform(
                    new StreamSource(inputFile),
                    proc.newSerializer(outputFile)
            );
        } catch (SaxonApiException ex) {
            Logger.getLogger(Saxon12XSLTHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex);
        }
    }    
    
    @Override
    public void transformXSLT(String internalPath, File inputFile, File outputFile) throws IOException{
        String[][] emptyParams = {};
        transformXSLT(internalPath, inputFile, outputFile, emptyParams);        
    }

    @Override
    public void transformXSLT(String internalPath, File inputFile, File outputFile, String[][] parameters) throws IOException{
        try {
            Processor proc = new Processor(false);   // false = no schema support
            InputStream xslStream =
                getClass().getResourceAsStream(internalPath);            
            XsltCompiler compiler = proc.newXsltCompiler();
            // Compile stylesheet
            XsltExecutable exec = compiler.compile(new StreamSource(xslStream));
            // Load transformer
            Xslt30Transformer transformer = exec.load30();
            // set parameters
            transformer.setStylesheetParameters(makeParameterMap(parameters));
            // Run transform
            transformer.transform(
                    new StreamSource(inputFile),
                    proc.newSerializer(outputFile)
            );
        } catch (SaxonApiException ex) {
            Logger.getLogger(Saxon12XSLTHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex);
        }        
    }
    
}
