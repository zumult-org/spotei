<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:math="http://www.w3.org/2005/xpath-functions/math"
    xmlns:tei="http://www.tei-c.org/ns/1.0"      
    xmlns:exmaralda="http://www.exmaralda.org"
    exclude-result-prefixes="xs math"
    version="3.0">
    
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- 
            <spanGrp type="sentenceId">
                <span from="Artur-J-Gvecg-P500001.tok4502" to="Artur-J-Gvecg-P500001.tok4511"
                    >Artur-J-Gvecg-P500001.s686-s687_reseg.2</span>
                <span from="Artur-J-Gvecg-P500001.tok4512" to="Artur-J-Gvecg-P500001.tok4526"
                    >Artur-J-Gvecg-P500001.s688_reseg.3</span>
            </spanGrp>
    -->
    
    <xsl:template match="tei:seg[@type='contribution']">
        <xsl:variable name="SEG_COPY">
            <xsl:copy-of select="."/>
        </xsl:variable>
        <xsl:variable name="START" select="descendant::tei:anchor[1]/@synch"/>
        <xsl:variable name="FIRST_FROM" select="ancestor::tei:annotationBlock/descendant::tei:spanGrp[@type='sentenceId']/tei:span[1]/@from"/>
        <xsl:variable name="END" select="descendant::tei:anchor[last()]/@synch"/>
        <xsl:variable name="LAST_TO" select="ancestor::tei:annotationBlock/descendant::tei:spanGrp[@type='sentenceId']/tei:span[last()]/@to"/>
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            
            <xsl:if test="not($START=$FIRST_FROM)">
                <!-- build an initial sentence seg -->
                <xsl:if test="$SEG_COPY/descendant::*[
                    @xml:id
                    and
                    (following-sibling::*/@xml:id=$FIRST_FROM)                        
                    ]">
                    <tei:seg type="sentence" subtype="filler">
                        <xsl:attribute name="xml:id" select="concat(ancestor::tei:annotationBlock/descendant::tei:spanGrp[@type='sentenceId']/tei:span[1], '_PREFIX')"/>
                        <xsl:apply-templates select="$SEG_COPY/descendant::*[
                            (following-sibling::*/@xml:id=$FIRST_FROM)                        
                            ]"/>                            
                    </tei:seg>
                </xsl:if>
            </xsl:if>
                
             <!-- ********************* -->
                
             <xsl:for-each select="ancestor::tei:annotationBlock/descendant::tei:spanGrp[@type='sentenceId']/tei:span">
                <xsl:variable name="ID" select="text()"/>
                <xsl:variable name="FROM" select="@from"/>
                <xsl:variable name="TO" select="@to"/>
                <xsl:variable name="NEXT_FROM" select="following-sibling::tei:span[1]/@from"/>
                <tei:seg type="sentence" subtype="regular">
                    <xsl:attribute name="xml:id" select="$ID"/>
                    <xsl:apply-templates select="$SEG_COPY/descendant::*[
                        ((self::*/@xml:id=$FROM
                        or preceding-sibling::*/@xml:id=$FROM)
                        and
                        (self::*/@xml:id=$TO
                        or following-sibling::*/@xml:id=$TO))
                        or (
                            self::tei:anchor
                            and following-sibling::*[1]/@xml:id=$FROM
                        )                        
                        ]"/>
                </tei:seg>
                <xsl:if test="string-length($NEXT_FROM)&gt;0 and not($NEXT_FROM=$TO)">
                    <!-- build an intervening sentence seg -->
                    <xsl:if test="$SEG_COPY/descendant::*[
                        @xml:id
                        and
                        (preceding-sibling::*/@xml:id=$TO)
                        and
                        (following-sibling::*/@xml:id=$NEXT_FROM)                        
                        ]">
                        <tei:seg type="sentence" subtype="filler">
                            <xsl:attribute name="xml:id" select="concat($ID, '_', $NEXT_FROM)"/>
                            <xsl:apply-templates select="$SEG_COPY/descendant::*[
                                (preceding-sibling::*/@xml:id=$TO)
                                and
                                (following-sibling::*/@xml:id=$NEXT_FROM)                        
                                ]"/>                            
                        </tei:seg>
                    </xsl:if>
                        
                </xsl:if>
            </xsl:for-each>
            
            <!-- ********************* -->
            
            <xsl:if test="not($END=$LAST_TO)">
                <!-- build an initial sentence seg -->
                <xsl:if test="$SEG_COPY/descendant::*[
                    @xml:id
                    and
                    (preceding-sibling::*/@xml:id=$LAST_TO)                        
                    ]">
                    <tei:seg type="sentence" subtype="filler">
                        <xsl:attribute name="xml:id" select="concat(ancestor::tei:annotationBlock/descendant::tei:spanGrp[@type='sentenceId']/tei:span[last()], '_SUFFIX')"/>
                        <xsl:apply-templates select="$SEG_COPY/descendant::*[
                            (preceding-sibling::*/@xml:id=$LAST_TO)                        
                            ]"/>                            
                    </tei:seg>
                </xsl:if>
            </xsl:if>
            
        </xsl:copy>
        
    </xsl:template>
    
    
    <xsl:template match="tei:spanGrp[@type='sentenceId']"/>
    
</xsl:stylesheet>