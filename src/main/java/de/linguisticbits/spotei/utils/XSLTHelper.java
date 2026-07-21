/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.linguisticbits.spotei.utils;

import java.io.File;
import java.io.IOException;
import javax.xml.transform.Source;
import net.sf.saxon.s9api.Destination;

/**
 *
 * @author bernd
 */

// making this an interface
// I want XSLT 3.0 support
// Currently, only Saxon 12.4+ will have that
// But I do not want to be dependent on Saxon
// so: abstracting over that

public interface XSLTHelper {
    
    public void transformXSLT(File xsltFile, File inputFile, File outputFile, String[][] parameters) throws IOException;        

    void transformXSLT(String internalPath, File inputFile, File outputFile, String[][] parameters) throws IOException;

    void transformXSLT(File xsltFile, File inputFile, File outputFile) throws IOException;

    void transformXSLT(String internalPath, File inputFile, File outputFile) throws IOException;

    
}
