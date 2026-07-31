/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.linguisticbits.spotei.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;

/**
 *
 * @author bernd
 */
public class LanguageUtility {
    
    
    public static String mapLanguageCode3To2(String languageCode) {
        switch (languageCode) {
            case "deu" : return "de";
            case "fra" : return "fr";
            case "eng" : return "en";
            case "ita" : return "it";
            case "spa" : return "es";
            case "por" : return "pt";
            case "rus" : return "ru";
            case "pol" : return "pl";
            default : return "xx";
        }
    }


    public static String mapLanguageCode2To3(String languageCode) {
        switch (languageCode) {
            case "de" : return "deu";
            case "fr" : return "fra";
            case "en" : return "eng";
            case "it" : return "ita";
            case "es" : return "spa";
            case "pt" : return "por";
            case "ru" : return "rus";
            case "pl" : return "pol";
            default : return "xxx";
        }
    }
    
    public static String mapLanguageNameToCode2(String languageName){
        switch (languageName) {
            case "German" : return "de";
            case "English" : return "en";
            default : return "xx";
        }
    }

    public static String mapLanguageCode2ToName(String languageCode){
        switch (languageCode) {
            case "de" : return "German";
            case "en" : return "English";
            default : return "Xxxxx";
        }
    }

    public static String getLanguageFromTEI(Document teiDoc) {
        try {
            String xp = "//tei:text";
            XPath xpath = XPath.newInstance(xp);
            xpath.addNamespace("tei", "http://www.tei-c.org/ns/1.0");
            Element e = (Element) xpath.selectSingleNode(teiDoc);
            return e.getAttributeValue("lang", Namespace.XML_NAMESPACE);
        } catch (JDOMException ex) {
            Logger.getLogger(LanguageUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "de";
    }

    public static String mapLanguageCode2ToG2P(String languageCode) {
        switch (languageCode) {
            case "fr" : return "fra-FR";
            case "es" : return "spa-ES";
            case "pt" : return "spa-ES"; // BAS doesn't cater for Portuguese !?
            case "ru" : return "rus-RU";
            default : return mapLanguageCode2To3(languageCode);
        }
    }
    
}
