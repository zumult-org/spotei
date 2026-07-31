/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.linguisticbits.spotei.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.exmaralda.exakt.utilities.FileIO;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author bernd
 */
public class ConfigurationReader {
    
    Document configurationDoc = new Document(new Element("spotei-configuration"));
    
    public ConfigurationReader(File configurationFile) throws JDOMException, IOException{
        System.out.println("[Spotei]  Reading configuration from "+ configurationFile.getAbsolutePath());
        configurationDoc = FileIO.readDocumentFromLocalFile(configurationFile);
    }

    public String getTreeTaggerDirectory() {
        return configurationDoc.getRootElement().getChildText("tree-tagger-directory");
    }
    
    public String getFFMPEGDirectory() {
        return configurationDoc.getRootElement().getChildText("ffmpeg-directory");
    }

    public String getFFProbeDirectory() {
        return configurationDoc.getRootElement().getChildText("ffprobe-directory");
    }

    public String getDeepLAPIKey(){
        return configurationDoc.getRootElement().getChildText("deepl-api-key");
    }

    public Map<String, String> getTreeTaggerParameterFiles(){
        Map<String, String> result = new HashMap<>();
        for (Object o : configurationDoc.getRootElement().getChildren("tree-tagger-parameter-file")){
            Element e = (Element)o;
            result.put(e.getAttributeValue("lang"), e.getText());
        }
        return result;
    }

    public Map<String, String> getPhoneticLexicons() {
        Map<String, String> result = new HashMap<>();
        for (Object o : configurationDoc.getRootElement().getChildren("phonetic-lexicon")){
            Element e = (Element)o;
            result.put(e.getAttributeValue("lang"), e.getText());
        }
        return result;
    }
    
    public Map<String, String> getTranslationMemories() {
        Map<String, String> result = new HashMap<>();
        for (Object o : configurationDoc.getRootElement().getChildren("translation-memory")){
            Element e = (Element)o;
            result.put(e.getAttributeValue("lang"), e.getText());
        }
        return result;
    }
    
    public String[] getLanguageNames() {
        Set<String> allLanguageCodes = new HashSet<>();
        for (Object o : configurationDoc.getRootElement().getChildren("phonetic-lexicon")){
            allLanguageCodes.add(((Element)o).getAttributeValue("lang"));
        }
        for (Object o : configurationDoc.getRootElement().getChildren("tree-tagger-parameter-file")){
            allLanguageCodes.add(((Element)o).getAttributeValue("lang"));
        }
        
        String[] result = new String[allLanguageCodes.size()];
        int i=0;
        for (String languageCode : allLanguageCodes){
            String languageName = LanguageUtility.mapLanguageCode2ToName(languageCode);
            result[i] = languageName;
            i++;
        }
        return result;
    }
    
    public String[] getLanguageCodes() {
        Set<String> allLanguageCodes = new HashSet<>();
        for (Object o : configurationDoc.getRootElement().getChildren("phonetic-lexicon")){
            allLanguageCodes.add(((Element)o).getAttributeValue("lang"));
        }
        for (Object o : configurationDoc.getRootElement().getChildren("tree-tagger-parameter-file")){
            allLanguageCodes.add(((Element)o).getAttributeValue("lang"));
        }
        
        String[] result = new String[allLanguageCodes.size()];
        int i=0;
        for (String languageCode : allLanguageCodes){
            result[i] = languageCode;
            i++;
        }
        return result;
    }


}
