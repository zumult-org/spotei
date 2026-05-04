<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:tei="http://www.tei-c.org/ns/1.0"
    xmlns:exmaralda="http://www.exmaralda.org"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:variable name="TIMELINE_COPY">
        <timeline>
            <xsl:for-each select="//tei:timeline/tei:when">
                <when>
                    <xsl:copy-of select="@*"/>
                    <xsl:attribute name="position" select="count(preceding-sibling::*)"/>                    
                </when>
            </xsl:for-each>
        </timeline>    
    </xsl:variable>
    
    <xsl:variable name="TOKEN_COPY">
        <tokens>
            <xsl:for-each select="//tei:seg[not(tei:seg)]/descendant::*[@xml:id]">
                <token>
                    <xsl:copy-of select="@xml:id"/>
                    <xsl:attribute name="position" select="count(preceding::*[@xml:id])"/>                    
                </token>
            </xsl:for-each>
        </tokens>
    </xsl:variable>


    <xsl:function name="exmaralda:position" as="xs:integer">
        <xsl:param name="ID"/>
        <xsl:choose>
            <xsl:when test="$TIMELINE_COPY/descendant::*[@xml:id=$ID]">
                <xsl:sequence select="$TIMELINE_COPY/descendant::*[@xml:id=$ID]/@position" />                
            </xsl:when>
            <xsl:otherwise>
                <xsl:sequence select="$TOKEN_COPY/descendant::*[@xml:id=$ID]/@position" />                                
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>
    
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="tei:body">
        <xsl:copy>
            <xsl:apply-templates select="//tei:seg[not(tei:seg)]">
                <xsl:sort select="exmaralda:position(descendant::tei:anchor[@synch][1]/@synch)" data-type="number"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="tei:seg">
        <xsl:variable name="SEG_START" select="descendant::tei:anchor[@synch][1]/@synch"/>
        <xsl:variable name="SEG_END" select="descendant::tei:anchor[@synch][last()]/@synch"/>
        <xsl:variable name="FIRST_TOKEN" select="descendant::*[@xml:id][1]/@xml:id"/>
        <xsl:variable name="LAST_TOKEN" select="descendant::*[@xml:id][last()]/@xml:id"/>
        <tei:annotationBlock>
            <xsl:attribute name="who" select="ancestor::tei:annotationBlock/@who"/>
            <xsl:attribute name="start" select="$SEG_START"/>
            <xsl:attribute name="end" select="$SEG_END"/>
            <xsl:attribute name="xml:id" select="concat(ancestor::tei:annotationBlock/@xml:id, '_', @xml:id)"/>
            <tei:u>
                <xsl:attribute name="xml:id" select="concat(ancestor::tei:annotationBlock/@xml:id, '_u_', @xml:id)"/>
                <tei:seg>
                    <xsl:copy-of select="@*"/>
                    <xsl:apply-templates/>
                </tei:seg>
            </tei:u>
            <xsl:apply-templates 
                select="ancestor::tei:annotationBlock/descendant::tei:spanGrp">
                <xsl:with-param name="START" select="$SEG_START"/>
                <xsl:with-param name="END" select="$SEG_END"/>
                <xsl:with-param name="FROM" select="$FIRST_TOKEN"/>
                <xsl:with-param name="TO" select="$LAST_TOKEN"/>
            </xsl:apply-templates>
        </tei:annotationBlock>        
    </xsl:template>
    
    <xsl:template match="tei:spanGrp">
        <xsl:param name="START"/>
        <xsl:param name="END"/>
        <xsl:param name="FROM"/>
        <xsl:param name="TO"/>
        <xsl:choose>
            <xsl:when test="id(@from)[self::tei:when]">
                <xsl:if test="descendant::tei:span[exmaralda:position(@from) &gt;= exmaralda:position($START) and exmaralda:position(@to) &lt;=exmaralda:position($END)]">
                    <xsl:copy>
                        <xsl:apply-templates select="@*"/>
                        <xsl:apply-templates select="descendant::tei:span[exmaralda:position(@from) &gt;= exmaralda:position($START) and exmaralda:position(@to) &lt;= exmaralda:position($END)]"/>                
                    </xsl:copy>
                </xsl:if>                
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="descendant::tei:span[exmaralda:position(@from) &gt;= exmaralda:position($FROM) and exmaralda:position(@to) &lt;=exmaralda:position($TO)]">
                    <xsl:copy>
                        <xsl:apply-templates select="@*"/>
                        <xsl:apply-templates select="descendant::tei:span[exmaralda:position(@from) &gt;= exmaralda:position($FROM) and exmaralda:position(@to) &lt;= exmaralda:position($TO)]"/>                
                    </xsl:copy>
                </xsl:if>                
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
</xsl:stylesheet>