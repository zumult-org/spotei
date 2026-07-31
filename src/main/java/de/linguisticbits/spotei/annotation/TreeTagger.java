/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.linguisticbits.spotei.annotation;

import de.linguisticbits.spotei.utils.ConfigurationReader;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.exmaralda.tagging.GeneralizedISOTEITagger;
import org.exmaralda.tagging.PostProcessingRules;
import org.exmaralda.tagging.TreeTaggableISOTEITranscription;
import org.jdom.JDOMException;

/**
 *
 * @author bernd
 */
public class TreeTagger {
    
    static String[] OPT = {"-token","-lemma","-sgml","-no-unknown"};
    
    static String[][] lang2PostProcessFile = {
        {"de", PostProcessingRules.FOLK_RULES}
    };
    

    
    public static void treeTag(File inFile, File outFile, File configurationFile) throws IOException, JDOMException{
        ConfigurationReader configurationReader = new ConfigurationReader(configurationFile);

        String TT = configurationReader.getTreeTaggerDirectory();
        Map<String, String> treeTaggerParameterFiles = configurationReader.getTreeTaggerParameterFiles();
        String[][] lang2ParamFile = new String[treeTaggerParameterFiles.keySet().size()][3];
        int i = 0;
        for (String language : treeTaggerParameterFiles.keySet()){
            String pfPath = treeTaggerParameterFiles.get(language);
            String[] entry = {language, pfPath, "UTF-8"};
            lang2ParamFile[i] = entry;
            i++;
        }


        GeneralizedISOTEITagger tagger = new GeneralizedISOTEITagger(TT, TreeTaggableISOTEITranscription.XPATH_ALL_WORDS_AND_PUNCTUATION,
                true, OPT, lang2ParamFile, lang2PostProcessFile);

        tagger.tagFile(inFile, outFile);

            
    }
    
}
