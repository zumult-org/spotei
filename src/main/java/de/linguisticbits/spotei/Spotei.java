/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package de.linguisticbits.spotei;

import de.linguisticbits.spotei.utils.XSLTHelperFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bernd
 */
public class Spotei {
    
    static String TEST_XSL = "C:\\spotei\\spotei\\src\\main\\java\\de\\linguisticbits\\spotei\\xml\\xslt\\conversion\\exmaralda2isotei.xsl";
    static String TEST_EXB = "C:\\exmaralda-demo-corpus\\src\\main\\java\\data\\corpora\\EXMARaLDA-DemoKorpus\\RudiVoellerWutausbruch\\RudiVoellerWutausbruch.exb";
    static String TEST_OUT = "C:\\Users\\bernd\\OneDrive\\Desktop\\TEST.xml";

    public static void main(String[] args) {
        try {
            XSLTHelperFactory.newXSLTHelper().transformXSLT(new File(TEST_XSL), new File(TEST_EXB), new File(TEST_OUT), null);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Spotei.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
