<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:math="http://www.w3.org/2005/xpath-functions/math"
    xmlns:tei="http://www.tei-c.org/ns/1.0"            
    exclude-result-prefixes="xs math"
    version="3.0">
    
    <!-- 
    NAME: segment.xsl
    INPUT: a tokenized, normalized ISO/TEI transcription with one level of <seg> underneath <u>
    PARAMETERS:
       - CONVENTION - a string specifying the transcription convention, possible values are
            - HIAT (https://nbn-resolving.org/urn:nbn:de:bsz:mh39-23681)
            - GAT
            - GENERIC (=simple, largely failsafe method, it only distinguishes words, punctuation and incidents in square brackets
    OUTPUT: an ISO/TEI conformant transcription file with a second level of <seg> determined according to punctuation characters
        
    =================================================================
    HISTORY:
       - created     13-01-2026 
    -->        
    
    <xsl:param name="CONVENTION">
        <xsl:choose>
            <xsl:when test="//tei:transcriptionDesc/@ident">
                <xsl:value-of select="//tei:transcriptionDesc/@ident"/>
            </xsl:when>
            <xsl:otherwise>GENERIC</xsl:otherwise>
        </xsl:choose>
    </xsl:param>
    
    <xsl:template match="@*|node()">
        <xsl:copy copy-namespaces="no">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="@count-iu"/>
    
    <xsl:template match="tei:u/tei:seg[not(tei:seg)]">
        <xsl:variable name="id" select="@xml:id"/>
        <xsl:variable name="TOKENS">
            <seg>
                <xsl:for-each select="*">
                    <xsl:copy>
                        <xsl:attribute name="count-iu">
                            <xsl:choose>
                                <xsl:when test="$CONVENTION='HIAT'">
                                    <xsl:value-of select="count(preceding-sibling::tei:pc[matches(text(),'[\.\?!˙…]')]) + 1"/>                                                                                
                                </xsl:when>
                                <xsl:when test="$CONVENTION='GAT'">
                                    <xsl:value-of select="count(preceding-sibling::tei:pc[matches(text(),'[\.\?\-;,]')]) + 1"/>                                                                                
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="count(preceding-sibling::tei:pc[matches(text(),'[\.\?!]')]) + 1"/>                                                                                                                    
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>                             
                        <xsl:apply-templates select="@*|node()"/>
                    </xsl:copy>                
                </xsl:for-each>
            </seg>
        </xsl:variable>
        
        <xsl:variable name="GROUPED">
            <segs>
                <xsl:for-each-group select="$TOKENS/seg/*" group-by="@count-iu">
                    <seg>
                        <xsl:apply-templates select="current-group()"/>
                    </seg>
                </xsl:for-each-group>
            </segs>
        </xsl:variable>
        
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <!-- <input>
                <xsl:copy-of select="$GROUPED"/>
            </input> -->
            
            <xsl:choose>
                <xsl:when test="max($TOKENS/seg/*/@count-iu)=2 and child::*[1][self::tei:anchor] and child::*[last()][self::tei:anchor]">
                    <!-- There is only one iu to begin with, so no need to complicatedly mess with anchors -->
                    <xsl:copy-of select="tei:anchor[1]"/> <!-- okay -->
                    <tei:seg>
                        <xsl:attribute name="xml:id" select="concat($id, '_seg_1')"/>
                        <xsl:attribute name="type">
                            <xsl:choose>
                                <xsl:when test="$CONVENTION='HIAT'">utterance</xsl:when>
                                <xsl:when test="$CONVENTION='GAT'">intonation-unit</xsl:when>
                                <xsl:otherwise>segment</xsl:otherwise>
                            </xsl:choose>    
                        </xsl:attribute>
                        <xsl:attribute name="subtype">
                            <xsl:variable name="LAST_PC" select="descendant::tei:pc[last()]/text()"/>
                            <xsl:choose>
                                <xsl:when test="$CONVENTION='HIAT'">
                                    <xsl:choose>
                                        <xsl:when test="$LAST_PC='.'">declarative</xsl:when>
                                        <xsl:when test="$LAST_PC='?'">interrogative</xsl:when>
                                        <xsl:when test="$LAST_PC='!'">exclamative</xsl:when>
                                        <xsl:when test="$LAST_PC='…'">interrupted</xsl:when>
                                        <xsl:otherwise>not_classified</xsl:otherwise>                                    
                                    </xsl:choose>
                                </xsl:when>
                                <xsl:when test="$CONVENTION='GAT'">
                                    <xsl:choose>
                                        <xsl:when test="$LAST_PC='.'">low-falling</xsl:when>
                                        <xsl:when test="$LAST_PC=';'">mid-falling</xsl:when>
                                        <xsl:when test="$LAST_PC='-'">level</xsl:when>
                                        <xsl:when test="$LAST_PC=','">mid-rising</xsl:when>
                                        <xsl:when test="$LAST_PC='?'">high-rising</xsl:when>
                                        <xsl:otherwise>not_classified</xsl:otherwise>
                                    </xsl:choose>                                    
                                </xsl:when>
                                <xsl:otherwise>not_classified</xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <xsl:apply-templates select="*[position() > 1 and position() != last()]"/>                        
                    </tei:seg>
                    <xsl:copy-of select="tei:anchor[last()]"/>
                </xsl:when>                



                <xsl:otherwise>
                    <xsl:for-each select="$GROUPED/descendant::seg">
                        <xsl:choose>
                            <xsl:when test="child::*[1][self::tei:anchor]">
                                <xsl:apply-templates select="child::*[1]"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <tei:anchor type="seg_add">
                                    <xsl:attribute name="synch" select="concat(preceding::tei:anchor[1]/@synch, '_', following::tei:anchor[1]/@synch)"/>
                                </tei:anchor>
                            </xsl:otherwise>
                        </xsl:choose>
                        <tei:seg>
                            <xsl:attribute name="xml:id" select="concat($id, '_iu_', position())"/>
                            <xsl:attribute name="type">
                                <xsl:choose>
                                    <xsl:when test="$CONVENTION='HIAT'">utterance</xsl:when>
                                    <xsl:when test="$CONVENTION='GAT'">intonation-unit</xsl:when>
                                    <xsl:otherwise>segment</xsl:otherwise>
                                </xsl:choose>    
                            </xsl:attribute>
                            <xsl:attribute name="subtype">
                                <xsl:variable name="LAST_PC" select="descendant::tei:pc[last()]/text()"/>
                                <xsl:choose>
                                    <xsl:when test="$CONVENTION='HIAT'">
                                        <xsl:choose>
                                            <xsl:when test="$LAST_PC='.'">declarative</xsl:when>
                                            <xsl:when test="$LAST_PC='?'">interrogative</xsl:when>
                                            <xsl:when test="$LAST_PC='!'">exclamative</xsl:when>
                                            <xsl:when test="$LAST_PC='…'">interrupted</xsl:when>
                                            <xsl:otherwise>not_classified</xsl:otherwise>                                    
                                        </xsl:choose>
                                    </xsl:when>
                                    <xsl:when test="$CONVENTION='GAT'">
                                        <xsl:choose>
                                            <xsl:when test="$LAST_PC='.'">low-falling</xsl:when>
                                            <xsl:when test="$LAST_PC=';'">mid-falling</xsl:when>
                                            <xsl:when test="$LAST_PC='-'">level</xsl:when>
                                            <xsl:when test="$LAST_PC=','">mid-rising</xsl:when>
                                            <xsl:when test="$LAST_PC='?'">high-rising</xsl:when>
                                            <xsl:otherwise>not_classified</xsl:otherwise>
                                        </xsl:choose>                                    
                                    </xsl:when>
                                    <xsl:otherwise>not_classified</xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>                            
                            <xsl:apply-templates select="*[not(self::tei:anchor and (not(preceding-sibling::*) or not(following-sibling::*)))]"/>                        
                        </tei:seg>
                    </xsl:for-each>   
                    <xsl:apply-templates select="descendant::tei:anchor[last()]"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
        
            
    </xsl:template>
    
    
</xsl:stylesheet>