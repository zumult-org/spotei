<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:math="http://www.w3.org/2005/xpath-functions/math"
    xmlns:tei="http://www.tei-c.org/ns/1.0"     
    exclude-result-prefixes="xs math"
    version="3.0">
    
    <!-- 
    NAME: isotei2eaf.xsl
    INPUT: an ISO/TEI Spoken Transcription 
    PARAMETERS: none
    OUTPUT: an ELAN Annotation file (EAF), an XML file which can be read by the ELAN annotation tools 
    =================================================================
    HISTORY:
    change 21-07-2026: created from a custom XSL from UDE
-->        
    
    <xsl:template match="/">
        <ANNOTATION_DOCUMENT AUTHOR="" FORMAT="3.0" VERSION="3.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://www.mpi.nl/tools/elan/EAFv3.0.xsd">
            <xsl:attribute name="DATE" select="current-dateTime()"/>
            <HEADER MEDIA_FILE="" TIME_UNITS="milliseconds">
                <xsl:for-each select="//tei:media">
                    <MEDIA_DESCRIPTOR>
                        <xsl:attribute name="MEDIA_URL" select="@url"/>
                        <xsl:attribute name="MIME_TYPE" select="@mimeType"/>
                    </MEDIA_DESCRIPTOR>
                </xsl:for-each>
                <PROPERTY NAME="URN">urn:nl-mpi-tools-elan-eaf:6f0b2f66-ae68-469b-a67e-b56b2a4fb021</PROPERTY>
            </HEADER>
            <xsl:apply-templates select="//tei:timeline"/>
            <xsl:for-each-group select="//tei:annotationBlock" group-by="@who">
                <!-- 
                    <TIER LINGUISTIC_TYPE_REF="default-lt" TIER_ID="NFS-01_ver">
                        <ANNOTATION>
                            <ALIGNABLE_ANNOTATION ANNOTATION_ID="a42"
                                TIME_SLOT_REF1="ts4" TIME_SLOT_REF2="ts7">
                                <ANNOTATION_VALUE>haben wir denn eigentlich noch gar keinen zweiten rtw hier</ANNOTATION_VALUE>
                            </ALIGNABLE_ANNOTATION>
                        </ANNOTATION>                
                -->
                <TIER>
                    <xsl:attribute name="LINGUISTIC_TYPE_REF">TRANSCRIPTION</xsl:attribute>
                    <xsl:attribute name="TIER_ID" select="concat(current-grouping-key(), '_ver')"/>
                    <xsl:attribute name="PARTICIPANT" select="current-grouping-key()"/>         
                    <xsl:for-each select="current-group()">
                        <xsl:variable name="ID_BASE" select="concat(current-grouping-key(), '_ver', '_', position())"/>
                        <ANNOTATION>
                            <ALIGNABLE_ANNOTATION>
                                <xsl:attribute name="ANNOTATION_ID" select="$ID_BASE"/>
                                <xsl:attribute name="TIME_SLOT_REF1" select="@start"/>
                                <xsl:attribute name="TIME_SLOT_REF2" select="@end"/>
                                <ANNOTATION_VALUE>
                                    <xsl:apply-templates select="descendant::tei:seg/descendant::*[not(*)]"/>        
                                </ANNOTATION_VALUE>
                            </ALIGNABLE_ANNOTATION>                                
                        </ANNOTATION>
                    </xsl:for-each>                                                        
                </TIER>
                
                
                <!-- 
                    <ANNOTATION>
                        <REF_ANNOTATION ANNOTATION_REF="a1" ANNOTATION_ID="a1_token_1">
                            <ANNOTATION_VALUE>DREIUNZWANISTE</ANNOTATION_VALUE>
                        </REF_ANNOTATION>
                    </ANNOTATION>                
                -->
                <TIER>
                    <xsl:attribute name="LINGUISTIC_TYPE_REF">TOKENIZATION</xsl:attribute>
                    <xsl:attribute name="TIER_ID" select="concat(current-grouping-key(), '_tok')"/>
                    <xsl:attribute name="PARTICIPANT" select="current-grouping-key()"/>
                    <xsl:variable name="PARENT_TIER" select="concat(current-grouping-key(), '_ver')"/>
                    <!-- this one goes through annotationBlocks -->
                    <xsl:for-each select="current-group()">
                        <xsl:variable name="POSITION" select="position()"/>
                        <xsl:variable name="ID_BASE" select="concat(current-grouping-key(), '_ver', '_', position())"/>
                        <xsl:for-each select="descendant::tei:seg/descendant::*[text() and not(*)]">
                            <xsl:variable name="COUNT_ORIGINAL" select="count(ancestor-or-self::*[parent::tei:seg][1]/preceding-sibling::*) + 1"/>
                                <ANNOTATION>
                                    <REF_ANNOTATION>
                                        <xsl:attribute name="ANNOTATION_ID" select="concat($ID_BASE, '_tok_', position())"/>
                                        <xsl:attribute name="ANNOTATION_REF" select="concat($PARENT_TIER, '_', $POSITION)"/>
                                        <xsl:if test="position()&gt;1">
                                            <xsl:attribute name="PREVIOUS_ANNOTATION" select="concat($ID_BASE, '_tok_', position()-1)"/>
                                        </xsl:if>
                                        <ANNOTATION_VALUE>
                                            <xsl:value-of select="text()"/>
                                        </ANNOTATION_VALUE>
                                    </REF_ANNOTATION>
                                </ANNOTATION>                                                                            
                        </xsl:for-each>
                    </xsl:for-each>
                </TIER>
                

                <TIER>
                    <xsl:attribute name="LINGUISTIC_TYPE_REF">NORMALIZATION</xsl:attribute>
                    <xsl:attribute name="TIER_ID" select="concat(current-grouping-key(), '_norm')"/>
                    <xsl:attribute name="PARTICIPANT" select="current-grouping-key()"/>
                    <xsl:variable name="PARENT_TIER" select="concat(current-grouping-key(), '_ver')"/>
                    <xsl:for-each select="current-group()">
                        <xsl:variable name="POSITION" select="position()"/>
                        <xsl:for-each select="descendant::tei:seg/descendant::*[text() and not(*)]">
                                <ANNOTATION>
                                    <REF_ANNOTATION>
                                        <xsl:attribute name="ANNOTATION_ID" select="concat($PARENT_TIER, '_', $POSITION, '_norm_', position())"/>
                                        <xsl:attribute name="ANNOTATION_REF" select="concat($PARENT_TIER, '_', $POSITION,  '_tok_', position())"/>
                                        <xsl:if test="position()&gt;1">
                                            <xsl:attribute name="PREVIOUS_ANNOTATION" select="concat($PARENT_TIER, '_', $POSITION, '_norm_', position()-1)"/>
                                        </xsl:if>
                                        <ANNOTATION_VALUE>
                                            <xsl:value-of select="@norm"/>
                                        </ANNOTATION_VALUE>
                                    </REF_ANNOTATION>
                                </ANNOTATION>                                                                            
                        </xsl:for-each>
                    </xsl:for-each>
                </TIER>
                

                <TIER>
                    <xsl:attribute name="LINGUISTIC_TYPE_REF">LEMMA</xsl:attribute>
                    <xsl:attribute name="TIER_ID" select="concat(current-grouping-key(), '_lemma')"/>
                    <xsl:attribute name="PARTICIPANT" select="current-grouping-key()"/>
                    <xsl:variable name="PARENT_TIER" select="concat(current-grouping-key(), '_ver')"/>
                    <xsl:for-each select="current-group()">
                        <xsl:variable name="POSITION" select="position()"/>
                        <xsl:for-each select="descendant::tei:seg/descendant::*[text() and not(*)]">
                                <ANNOTATION>
                                    <REF_ANNOTATION>
                                        <xsl:attribute name="ANNOTATION_ID" select="concat($PARENT_TIER, '_', $POSITION, '_lemma_', position())"/>
                                        <xsl:attribute name="ANNOTATION_REF" select="concat($PARENT_TIER, '_', $POSITION, '_tok_', position())"/>
                                        <ANNOTATION_VALUE>
                                            <xsl:value-of select="@lemma"/>
                                        </ANNOTATION_VALUE>
                                    </REF_ANNOTATION>
                                </ANNOTATION>                                                
                        </xsl:for-each>
                    </xsl:for-each>
                </TIER>
                
                <TIER>
                    <xsl:attribute name="LINGUISTIC_TYPE_REF">POS</xsl:attribute>
                    <xsl:attribute name="TIER_ID" select="concat(current-grouping-key(), '_pos')"/>
                    <xsl:attribute name="PARTICIPANT" select="current-grouping-key()"/>
                    <xsl:variable name="PARENT_TIER" select="concat(current-grouping-key(), '_ver')"/>
                    <xsl:for-each select="current-group()">
                        <xsl:variable name="POSITION" select="position()"/>
                        <xsl:for-each select="descendant::tei:seg/descendant::*[text() and not(*)]">
                            <ANNOTATION>
                                <REF_ANNOTATION>
                                    <xsl:attribute name="ANNOTATION_ID" select="concat($PARENT_TIER, '_', $POSITION, '_pos_', position())"/>
                                    <xsl:attribute name="ANNOTATION_REF" select="concat($PARENT_TIER, '_', $POSITION, '_tok_', position())"/>
                                    <ANNOTATION_VALUE>
                                        <xsl:value-of select="@pos"/>
                                    </ANNOTATION_VALUE>
                                </REF_ANNOTATION>
                            </ANNOTATION>                                                                            
                        </xsl:for-each>
                    </xsl:for-each>
                </TIER> -->
            </xsl:for-each-group>
            
            <xsl:call-template name="LINGUISTIC_TYPES"/>
        </ANNOTATION_DOCUMENT>
    </xsl:template>
    
    <xsl:template match="tei:timeline">
        <TIME_ORDER>
            <xsl:apply-templates select="tei:when"/>
        </TIME_ORDER>
    </xsl:template>
    
    <xsl:template match="tei:when">
        <!-- <when xml:id="T_START" interval="0.0" since="T_START"/> -->
        <!-- <TIME_SLOT TIME_SLOT_ID="ts1" TIME_VALUE="2482161"/> -->
        <TIME_SLOT>
            <xsl:attribute name="TIME_SLOT_ID" select="@xml:id"/>
            <xsl:attribute name="TIME_VALUE" select="xs:integer(@interval * 1000)"/>
        </TIME_SLOT>
    </xsl:template>
    
    <xsl:template match="tei:u">
        <xsl:param name="ID_BASE"/>
        <xsl:apply-templates select="tei:seg">
            <xsl:with-param name="ID_BASE" select="$ID_BASE"/>
        </xsl:apply-templates>
    </xsl:template>
    
    <xsl:template match="tei:seg[tei:seg]">
        <xsl:param name="ID_BASE"/>
        <xsl:apply-templates select="tei:seg">
            <xsl:with-param name="ID_BASE" select="$ID_BASE"/>
        </xsl:apply-templates>
    </xsl:template>

    
    <xsl:template match="tei:seg[not(tei:seg)]">
        <xsl:param name="ID_BASE"></xsl:param>
        <xsl:variable name="AB_POSITION" select="count(ancestor::tei:annotationBlock/preceding-sibling::tei:annotationBlock) + 1"/>
        <xsl:variable name="SEG_POSITION" select="count(preceding-sibling::tei:seg) + 1"/>
        <xsl:for-each select="tei:anchor[position() != last()]">
            <xsl:variable name="ANCHOR_POSITION" select="count(preceding-sibling::tei:anchor) + 1"/>
            <xsl:variable name="NEXT_ANCHOR"
                select="following-sibling::tei:anchor[1]"/>            
            <ANNOTATION>
                <ALIGNABLE_ANNOTATION>                    
                    <!-- <xsl:attribute name="ANNOTATION_ID" select="concat($ID_BASE, '_', position())"/> -->
                    <xsl:attribute name="ANNOTATION_ID" select="concat($ID_BASE, '_', $AB_POSITION, '_', $SEG_POSITION, '_', $ANCHOR_POSITION)"/>
                    <xsl:attribute name="TIME_SLOT_REF1" select="@synch"/>
                    <xsl:attribute name="TIME_SLOT_REF2" select="following-sibling::tei:anchor[1]/@synch"/>
                    <ANNOTATION_VALUE>                                    
                        <xsl:apply-templates
                            select="following-sibling::node()
                            [. &lt;&lt; $NEXT_ANCHOR]"/>
                    </ANNOTATION_VALUE>
                </ALIGNABLE_ANNOTATION>
            </ANNOTATION>
        </xsl:for-each>
    </xsl:template>
    
    <!-- for the words we need some word-level symbols and to decide whether or not to add space -->
    <xsl:template match="tei:w">
        <xsl:apply-templates/>
        <xsl:choose>
            <!-- no space before pc, utterance end symbols will be inserted later after last <w> -->
            <xsl:when test="following-sibling::*[1][self::tei:pc] or not(following-sibling::*[self::tei:w])"></xsl:when>
            <xsl:otherwise><xsl:text> </xsl:text></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="tei:pc">
        <xsl:apply-templates/>
        <xsl:choose>
            <!-- no space before pc -->
            <xsl:when test="following-sibling::*[1][self::tei:pc]"></xsl:when>
            <xsl:otherwise><xsl:text> </xsl:text></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="tei:seg/tei:pause">
        <xsl:choose>
            <!-- <pause xml:id="p495" rend="(0.71)" dur="PT0.71S" start="TLI_950" end="TLI_951"/>  -->
            <xsl:when test="@rend"><xsl:value-of select="@rend"/></xsl:when>
            <xsl:otherwise>
                <xsl:variable name="DURATION" select="substring-after(substring-before(@dur,'S'), 'PT')"/>
                <xsl:text>(</xsl:text><xsl:value-of select="$DURATION"/><xsl:text>)</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <!-- here we don't want spaces if a <pc> follows (which is really weird, but happens) -->
            <xsl:when test="following-sibling::*[1][self::tei:pc] or (following-sibling::*[1][self::tei:anchor] and following-sibling::*[2][self::tei:pc])"></xsl:when>
            <xsl:otherwise><xsl:text> </xsl:text></xsl:otherwise>
        </xsl:choose>       
    </xsl:template>
    
    <xsl:template match="tei:seg/tei:vocal | tei:seg/tei:incident">
        <xsl:choose>
            <xsl:when test="tei:desc/@rend"><xsl:value-of select="tei:desc/@rend"/></xsl:when>
            <xsl:otherwise>
                <xsl:text>((</xsl:text>
                <xsl:value-of select="tei:desc"/>
                <xsl:text>))</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <!-- here we don't want spaces if a <pc> follows (which is really weird, but happens) -->
            <xsl:when test="following-sibling::*[1][self::tei:pc] or (following-sibling::*[1][self::tei:anchor] and following-sibling::*[2][self::tei:pc])"></xsl:when>
            <xsl:otherwise><xsl:text> </xsl:text></xsl:otherwise>
        </xsl:choose>        
    </xsl:template>
    
    
    
    
    
    
    <xsl:template name="LINGUISTIC_TYPES">
        <LINGUISTIC_TYPE GRAPHIC_REFERENCES="false" LINGUISTIC_TYPE_ID="TRANSCRIPTION"
            TIME_ALIGNABLE="true"/>
        <LINGUISTIC_TYPE CONSTRAINTS="Symbolic_Subdivision" GRAPHIC_REFERENCES="false"
            LINGUISTIC_TYPE_ID="TOKENIZATION" TIME_ALIGNABLE="false"/>
        <LINGUISTIC_TYPE CONSTRAINTS="Symbolic_Subdivision" GRAPHIC_REFERENCES="false"
            LINGUISTIC_TYPE_ID="NORMALIZATION" TIME_ALIGNABLE="false"/>
        <LINGUISTIC_TYPE CONSTRAINTS="Symbolic_Association" GRAPHIC_REFERENCES="false"
            LINGUISTIC_TYPE_ID="LANGUAGE" TIME_ALIGNABLE="false"/>
        <LINGUISTIC_TYPE GRAPHIC_REFERENCES="false" LINGUISTIC_TYPE_ID="TRANSLATION"
            CONSTRAINTS="Symbolic_Association" TIME_ALIGNABLE="true"/>
        <LINGUISTIC_TYPE CONSTRAINTS="Symbolic_Association" GRAPHIC_REFERENCES="false"
            LINGUISTIC_TYPE_ID="LEMMA" TIME_ALIGNABLE="false"/>
        <LINGUISTIC_TYPE CONSTRAINTS="Symbolic_Association" GRAPHIC_REFERENCES="false"
            LINGUISTIC_TYPE_ID="POS" TIME_ALIGNABLE="false"/>
        <LOCALE LANGUAGE_CODE="en"/>
        <LOCALE LANGUAGE_CODE="de"/>
        <CONSTRAINT
            DESCRIPTION="Time subdivision of parent annotation's time interval, no time gaps allowed within this interval"
            STEREOTYPE="Time_Subdivision"/>
        <CONSTRAINT
            DESCRIPTION="Symbolic subdivision of a parent annotation. Annotations refering to the same parent are ordered"
            STEREOTYPE="Symbolic_Subdivision"/>
        <CONSTRAINT DESCRIPTION="1-1 association with a parent annotation"
            STEREOTYPE="Symbolic_Association"/>
        <CONSTRAINT
            DESCRIPTION="Time alignable annotations within the parent annotation's time interval, gaps are allowed"
            STEREOTYPE="Included_In"/>        
    </xsl:template>
    
</xsl:stylesheet>