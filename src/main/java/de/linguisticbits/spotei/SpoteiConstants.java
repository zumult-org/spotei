/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.linguisticbits.spotei;

/**
 *
 * @author bernd
 */
public class SpoteiConstants {
    
    public static final String[][] EMPTY_PARAMETERS = {};
    
    public static final String BASE_XSLT_PATH = "/de/linguisticbits/spotei/xml/xslt";
    
    public static final String CONVERSION_XSLT_PATH = BASE_XSLT_PATH + "/conversion";
    public static final String PROCESSING_XSLT_PATH = BASE_XSLT_PATH + "/processing";
    public static final String OUTPUT_XSLT_PATH = BASE_XSLT_PATH + "/output";
    
    public static final String EXMARALDA2ISOTEI_XSLT = CONVERSION_XSLT_PATH + "/exmaralda2isotei.xsl";
    public static final String EXMARALDA2ISOTEI_EVENTTOKEN_XSLT = CONVERSION_XSLT_PATH + "/exmaralda2isotei_eventToken.xsl";
    public static final String FOLKER2ISOTEI_XSLT = CONVERSION_XSLT_PATH + "/folker2isotei.xsl";
    public static final String ISOTEI2EXMARALDA_XSLT = CONVERSION_XSLT_PATH + "/isotei2exmaralda.xsl";
    public static final String ISOTEI2EXMARALDA_KEEPTOKENS_XSLT = CONVERSION_XSLT_PATH + "/isotei2exmaralda_keepTokens.xsl";
    public static final String ISOTEI2EAF_XSLT = CONVERSION_XSLT_PATH + "/isotei2eaf.xsl";
    public static final String ISOTEI2FOLKER_XSLT = CONVERSION_XSLT_PATH + "/isotei2folker.xsl";
    public static final String ISOTEI2TCF_XSLT = CONVERSION_XSLT_PATH + "/isotei2tcf.xsl";
    public static final String ISOTEI2VTT_XSLT = CONVERSION_XSLT_PATH + "/isotei2vtt.xsl";
    
    public static final String ADDLOWLEVELANCHORS_XSLT = PROCESSING_XSLT_PATH + "/addLowLevelAnchors.xsl";
    public static final String ATTRIBUTES2SPANS_XSLT = PROCESSING_XSLT_PATH + "/attributes2spans.xsl";
    public static final String DESEGMENT_XSLT = PROCESSING_XSLT_PATH + "/desegment.xsl";
    public static final String DETOKENIZE_XSLT = PROCESSING_XSLT_PATH + "/detokenize.xsl";
    public static final String FLATTENSEGHIAERARCHY_XSLT = PROCESSING_XSLT_PATH + "/flattenSegHierarchy.xsl";
    public static final String INSERTSENTENCELAYER_XSLT = PROCESSING_XSLT_PATH + "/insertSentenceLayer.xsl";
    public static final String INTERPOLATE_XSLT = PROCESSING_XSLT_PATH + "/interpolate.xsl";
    public static final String NORMALIZE_XSLT = PROCESSING_XSLT_PATH + "/normalize.xsl";
    public static final String REMOVESTRAYANCHORS_XSLT = PROCESSING_XSLT_PATH + "/removeStrayAnchors.xsl";
    public static final String REMOVETIMEPOINTSWITHOUTABSOLUTE_XSLT = PROCESSING_XSLT_PATH + "/removeTimepointsWithoutAbsolute.xsl";
    public static final String SEGMENT_XSLT = PROCESSING_XSLT_PATH + "/segment.xsl";
    public static final String SPANS2ATTRIBUTES_XSLT = PROCESSING_XSLT_PATH + "/spans2attributes.xsl";
    public static final String TIME2TOKENSPANREFERENCES_XSLT = PROCESSING_XSLT_PATH + "/time2tokenSpanReferences.xsl";
    public static final String TOKEN2TIMESPANREFERENCES_XSLT = PROCESSING_XSLT_PATH + "/token2timeSpanReferences.xsl";
    public static final String TOKENIZE_XSLT = PROCESSING_XSLT_PATH + "/tokenize.xsl";
    
    
    
}
