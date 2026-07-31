/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.linguisticbits.spotei.annotation;

import de.linguisticbits.spotei.SpoteiConstants;
import de.linguisticbits.spotei.utils.LanguageUtility;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exmaralda.exakt.utilities.FileIO;
import org.exmaralda.orthonormal.lexicon.LexiconException;
import org.exmaralda.orthonormal.lexicon.XMLLexicon;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 *
 * @author bernd
 */
public class OrthoNormalizer {
    
    
    
    public static void orthoNormalize(File inFile, File outFile) throws JDOMException, IOException {
        XMLLexicon FOLK_Lexicon = new XMLLexicon();        
        FOLK_Lexicon.read(null);
        
        Document doc = FileIO.readDocumentFromLocalFile(inFile);
        String language = LanguageUtility.getLanguageFromTEI(doc);
        XPath xp = XPath.newInstance("//tei:w");
        xp.addNamespace(SpoteiConstants.TEI_NAMESPACE); 
        List l = xp.selectNodes(doc);

        for (Object o : l){
            try {
                Element w = (Element)o;
                String transcribedForm = w.getText();
                if (language.equals("de")){
                    String cleanForm = transcribedForm.toLowerCase().replaceAll(":", "");
                    String normalisedForm = cleanForm;
                    List<String> candidateForms = FOLK_Lexicon.getCandidateForms(cleanForm);
                    if (!candidateForms.isEmpty()){
                        normalisedForm = candidateForms.get(0);
                    }
                    if (normalisedForm.length()==0){
                        normalisedForm = transcribedForm;
                    }
                    w.setAttribute("norm", normalisedForm);
                } else {
                    // we don't have other normalisation lexicons, so simply throwing out all
                    // non-alphabet characters
                    String cleanForm = transcribedForm.toLowerCase().replaceAll("[^\\p{L}\\p{N}]", "");
                    String normalisedForm = cleanForm;
                    if (normalisedForm.length()==0){
                        normalisedForm = transcribedForm;
                    }
                    w.setAttribute("norm", normalisedForm);
                }
            } catch (LexiconException ex) {
                Logger.getLogger(OrthoNormalizer.class.getName()).log(Level.SEVERE, null, ex);
                throw new IOException(ex);
            }

        }
        
        FileIO.writeDocumentToLocalFile(outFile, doc);
        
    }
    
    
}
