/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package de.linguisticbits.spotei;

import de.linguisticbits.spotei.utils.XSLTHelper;
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
    
    static String TEST_EXB = "C:\\exmaralda-demo-corpus\\src\\main\\java\\data\\corpora\\EXMARaLDA-DemoKorpus\\RudiVoellerWutausbruch\\RudiVoellerWutausbruch.exb";

    static String TEST_XSL_1 = "/de/linguisticbits/spotei/xml/xslt/conversion/exmaralda2isotei.xsl";
    static String TEST_XSL_2 = "/de/linguisticbits/spotei/xml/xslt/processing/normalize.xsl";
    static String TEST_XSL_3 = "/de/linguisticbits/spotei/xml/xslt/processing/tokenize.xsl";
    static String TEST_OUT = "C:\\Users\\bernd\\OneDrive\\Desktop\\TEST.xml";

    public static void main(String[] args) {
        try {
            XSLTHelperFactory.newXSLTHelper().transformXSLT(new File(TEST_XSL_1), new File(TEST_EXB), new File(TEST_OUT), null);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Spotei.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Spotei() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        xsltHelper = XSLTHelperFactory.newXSLTHelper();
    }
    
    
    XSLTHelper xsltHelper;
    
    //******************************************************
    //********             CONVERSION              *********
    //******************************************************
    
    public void exmaralda2isotei(File inFile, File outFile, String[][] parameters) throws IOException {
        xsltHelper.transformXSLT(SpoteiConstants.EXMARALDA2ISOTEI_XSLT, inFile, outFile, parameters);        
    }
    
    public void exmaralda2isotei_eventToken(File inFile, File outFile, String[][] parameters) throws IOException {
        xsltHelper.transformXSLT(SpoteiConstants.EXMARALDA2ISOTEI_EVENTTOKEN_XSLT, inFile, outFile, parameters);        
    }

    public void folker2isotei(File inFile, File outFile, String[][] parameters) throws IOException {
        xsltHelper.transformXSLT(SpoteiConstants.FOLKER2ISOTEI_XSLT, inFile, outFile, parameters);        
    }

    public void isotei2exmaralda(File inFile, File outFile, String[][] parameters) throws IOException {
        xsltHelper.transformXSLT(SpoteiConstants.ISOTEI2EXMARALDA_XSLT, inFile, outFile, parameters);        
    }

    public void isotei2exmaralda_keepTokens(File inFile, File outFile, String[][] parameters) throws IOException {
        xsltHelper.transformXSLT(SpoteiConstants.ISOTEI2EXMARALDA_KEEPTOKENS_XSLT, inFile, outFile, parameters);        
    }

    public void isotei2eaf(File inFile, File outFile) throws IOException {
        xsltHelper.transformXSLT(SpoteiConstants.ISOTEI2EAF_XSLT, inFile, outFile);        
    }

    public void isotei2folker(File inFile, File outFile, String[][] parameters) throws IOException {
        xsltHelper.transformXSLT(SpoteiConstants.ISOTEI2EXMARALDA_XSLT, inFile, outFile, parameters);        
    }

    public void isotei2tcf(File inFile, File outFile) throws IOException {
        xsltHelper.transformXSLT(SpoteiConstants.ISOTEI2EXMARALDA_XSLT, inFile, outFile);        
    }

    public void isotei2vtt(File inFile, File outFile) throws IOException {
        xsltHelper.transformXSLT(SpoteiConstants.ISOTEI2EXMARALDA_XSLT, inFile, outFile);        
    }

    //******************************************************
    //********             PROCESSING              *********
    //******************************************************

    public void addLowLevelAnchors(File inFile, File outFile) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.ADDLOWLEVELANCHORS_XSLT, inFile, outFile);
    }

    public void attributes2spans(File inFile, File outFile) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.ATTRIBUTES2SPANS_XSLT, inFile, outFile);
    }

    public void desegment(File inFile, File outFile, String[][] parameters) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.DESEGMENT_XSLT, inFile, outFile, parameters);
    }

    public void detokenize(File inFile, File outFile, String[][] parameters) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.DETOKENIZE_XSLT, inFile, outFile, parameters);
    }

    public void flattenSegHierarchy(File inFile, File outFile) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.FLATTENSEGHIAERARCHY_XSLT, inFile, outFile);
    }

    public void insertSentenceLayer(File inFile, File outFile, String[][] parameters) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.INSERTSENTENCELAYER_XSLT, inFile, outFile, parameters);
    }

    public void interpolate(File inFile, File outFile) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.INTERPOLATE_XSLT, inFile, outFile);
    }

    public void normalize(File inFile, File outFile) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.NORMALIZE_XSLT, inFile, outFile);
    }
    
    public void removeStrayAnchors(File inFile, File outFile) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.REMOVESTRAYANCHORS_XSLT, inFile, outFile);
    }

    public void removeTimepointsWithoutAbsolute(File inFile, File outFile) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.REMOVETIMEPOINTSWITHOUTABSOLUTE_XSLT, inFile, outFile);
    }

    public void segment(File inFile, File outFile, String[][] parameters) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.SEGMENT_XSLT, inFile, outFile, parameters);
    }

    public void spans2attributes(File inFile, File outFile) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.SPANS2ATTRIBUTES_XSLT, inFile, outFile);
    }

    public void time2tokenSpanReference(File inFile, File outFile, String[][] parameters) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.TIME2TOKENSPANREFERENCES_XSLT, inFile, outFile, parameters);
    }

    public void token2timeSpanReference(File inFile, File outFile, String[][] parameters) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.TOKEN2TIMESPANREFERENCES_XSLT, inFile, outFile, parameters);
    }

    public void tokenize(File inFile, File outFile, String[][] parameters) throws IOException{
        xsltHelper.transformXSLT(SpoteiConstants.TOKENIZE_XSLT, inFile, outFile, parameters);
    }
    
    //******************************************************
    //********             OUTPUT                  *********
    //******************************************************
    

}
