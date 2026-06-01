/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.linguisticbits.spotei.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Xslt30Transformer;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;

/**
 *
 * @author bernd
 */
public class Saxon12XSLTHelper implements XSLTHelper {

    @Override
    public void transformXSLT(File xsltFile, File inputFile, File outputFile, String[][] parameters) throws IOException {
        try {
            Processor proc = new Processor(false);   // false = no schema support
            XsltCompiler compiler = proc.newXsltCompiler();
            
            // Compile stylesheet
            XsltExecutable exec = compiler.compile(new StreamSource(xsltFile));
            
            // Load transformer
            Xslt30Transformer transformer = exec.load30();
            
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
