<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:math="http://www.w3.org/2005/xpath-functions/math" xmlns:tei="http://www.tei-c.org/ns/1.0"
    exclude-result-prefixes="xs math" version="3.0">
    <xsl:template match="/">
        <html>
            <head>
                <style type="text/css">
                    td, th { border: 1px solid gray;}
                    td.empty { background: gray}
                </style>
            </head>
            <body>
                <xsl:apply-templates select="//tei:annotationBlock"/>                
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="tei:annotationBlock">
        <!-- <xsl:variable name="ABCOPY" select="."/>
        
        <xsl:variable name="SYNCPOINTS">
            <syncPoints>
                <xsl:for-each select="$ABCOPY/descendant::*/@synch | $ABCOPY/descendant::*/@xml:id">
                    <xsl:variable name="CURRENT" select="."/>
                    <xsl:if test="name()='xml:id' or not(parent::*/preceding::*[@synch=$CURRENT])">
                        <xsl:element name="syncPoint">
                            <xsl:attribute name="type">
                                <xsl:choose>
                                    <xsl:when test="parent::*[self::tei:anchor]">anchor</xsl:when>
                                    <xsl:otherwise>id</xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>
                            <xsl:attribute name="element" select="current()/name()"/>
                            <xsl:attribute name="id" select="$CURRENT"/>
                        </xsl:element>
                    </xsl:if>
                </xsl:for-each>
            </syncPoints>
        </xsl:variable> -->
        
        <xsl:variable name="ABCOPY" select="."/>
        
        <xsl:variable name="SYNCPOINTS">
            <syncPoints>
                <xsl:for-each select="$ABCOPY/descendant::*/@synch | $ABCOPY/descendant::*/@xml:id">
                    
                    <xsl:variable name="CURRENT" select="."/>
                    <xsl:variable name="block" select="ancestor::annotationBlock[1]"/>
                    
                    <xsl:if test="
                        name() = 'xml:id'
                        or
                        not($block//@synch[. = $CURRENT][.. &lt;&lt; current()/..])
                        ">
                        
                        <syncPoint>
                            <xsl:attribute name="type">
                                <xsl:choose>
                                    <xsl:when test="parent::*[self::tei:anchor]">anchor</xsl:when>
                                    <xsl:otherwise>id</xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>
                            
                            <xsl:attribute name="element" select="parent::*/name()"/>
                            <xsl:attribute name="id" select="$CURRENT"/>
                        </syncPoint>                        
                    </xsl:if>                    
                </xsl:for-each>
            </syncPoints>
        </xsl:variable>        
        
        <xsl:variable name="IDs">
            <ids>
                <xsl:for-each select="$ABCOPY/descendant::*[@xml:id]">
                    <xsl:element name="id">
                        <xsl:attribute name="id" select="@xml:id"/>
                        <xsl:attribute name="element" select="parent::*/name()"/>
                        <xsl:attribute name="level" select="count(ancestor::*)"/>
                        <xsl:attribute name="type">
                            <xsl:choose>
                                <xsl:when test="not(descendant::*[@xml:id])">leaf</xsl:when>
                                <xsl:otherwise>branch</xsl:otherwise>
                            </xsl:choose>                            
                        </xsl:attribute>
                        <xsl:attribute name="start">
                            <xsl:choose>
                                <xsl:when test="not(descendant::*[@xml:id])">
                                    <xsl:value-of select="@xml:id"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="descendant::*[@xml:id and not (descendant::*[@xml:id])][1]/@xml:id"/>                                    
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <xsl:attribute name="end">
                            <xsl:choose>
                                <xsl:when test="not(descendant::*[@xml:id])">
                                    <xsl:value-of select="@xml:id"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="descendant::*[@xml:id and not (descendant::*[@xml:id])][last()]/@xml:id"/>                                    
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                    </xsl:element>
                </xsl:for-each>                    
            </ids>
        </xsl:variable>
        
        <xsl:message select="$IDs"/>
        

        <table>
            <thead>
                <tr>
                    <th> </th>
                    <xsl:for-each select="$IDs/descendant::id[@type='leaf']">
                        <th>
                            <xsl:attribute name="title">
                                <xsl:value-of select="@id"/> / <xsl:value-of select="@element"/>
                            </xsl:attribute>
                        </th>                    
                    </xsl:for-each>
                </tr>
            </thead>
            <tbody>
                
                <!-- text from leaves -->
                <tr>
                    <td> </td>
                    <xsl:for-each select="tei:u/descendant::*[@xml:id and not(descendant::*[@xml:id])]">
                        <td>
                            <xsl:choose>
                                <xsl:when test="@rend"><xsl:value-of select="@rend"/></xsl:when>
                                <xsl:otherwise><xsl:value-of select="text()"/></xsl:otherwise>
                            </xsl:choose>                            
                        </td>                        
                    </xsl:for-each>
                </tr>


                <!-- attributes from leaves -->
                <!-- but exclude xml:id, type and rend -->
                <xsl:for-each-group select="tei:u/descendant::*[@xml:id and not(descendant::*[@xml:id])]/@*
                    [not(name()='xml:id' or name()='type' or name()='rend')]" 
                    group-by="name()">
                    <xsl:variable name="ATTRIBUTE_NAME" select="current-grouping-key()"/>                    
                    <tr>
                        <td><xsl:value-of select="$ATTRIBUTE_NAME"/></td>
                        <xsl:for-each select="$ABCOPY/descendant::*[@xml:id and not(descendant::*[@xml:id])]">
                            <td><xsl:value-of select="@*[name()=$ATTRIBUTE_NAME]"/></td>
                        </xsl:for-each>
                    </tr>                        
                </xsl:for-each-group>
                
                
                <!-- attributes from non-leaves -->
                <xsl:for-each-group select="tei:u/descendant::*[@xml:id and descendant::*[@xml:id]
                    and @*[not(name()='xml:id' or name()='type')]]" group-by="count(ancestor::*)">
                    
                    <xsl:variable name="THIS_ID" select="@xml:id"/>
                    <xsl:variable name="THIS_GROUP" select="current-group()"/>
                    
                    <xsl:for-each select="$THIS_GROUP[1]/@*[not(name()='xml:id' or name()='type')]">
                        <xsl:variable name="ATTRIBUTE_NAME" select="name()"/>
                        <xsl:variable name="ATTRIBUTE_VALUE" select="."/>
                        <tr>
                            <td>
                                <xsl:value-of select="$THIS_GROUP[1]/name()"/>
                                <xsl:text>:</xsl:text>
                                <xsl:value-of select="$ATTRIBUTE_NAME"/>
                            </td>
                            <xsl:for-each select="$THIS_GROUP">
                                <xsl:variable name="START_ID" select="$IDs/descendant::id[@id=$THIS_ID]/@start"/>
                                <xsl:variable name="END_ID" select="$IDs/descendant::id[@id=$THIS_ID]/@end"/>
                                <xsl:variable name="START_POSITION" select="$ABCOPY/descendant::*[@xml:id=$START_ID]/count(preceding-sibling::*[@xml:id])"/>
                                <xsl:variable name="END_POSITION" select="$ABCOPY/descendant::*[@xml:id=$END_ID]/count(preceding-sibling::*[@xml:id])"/>
                                <td>
                                    <xsl:attribute name="colspan" select="$END_POSITION - $START_POSITION + 1"/>
                                        <xsl:value-of select="$ATTRIBUTE_VALUE"/>
                                </td>
                            </xsl:for-each>
                        </tr>                                                
                    </xsl:for-each>
                </xsl:for-each-group>
                
                <xsl:variable name="FIRST_TOKEN_ID" select="descendant::*[@xml:id and not(descendant::*[@xml:id])][1]/@xml:id"/>
                <xsl:variable name="LAST_TOKEN_ID" select="descendant::*[@xml:id and not(descendant::*[@xml:id])][last()]/@xml:id"/>
                
                <!-- spanGrp annotations -->
                <xsl:for-each select="descendant::tei:spanGrp[tei:span]">
                    <xsl:variable name="FIRST_FROM" select="tei:span[1]/@from"/>
                    <xsl:variable name="LAST_TO" select="tei:span[last()]/@to"/>
                    
                    <tr>
                        <td>
                            <xsl:value-of select="@type"/>
                        </td>
                        
                        <xsl:if test="not($FIRST_FROM=$FIRST_TOKEN_ID)">
                            <td class="empty">
                                <xsl:attribute name="colspan"
                                    select="$ABCOPY/descendant::*[@xml:id=$FIRST_FROM]/count(preceding-sibling::*[@xml:id])">
                                </xsl:attribute>
                            </td>
                        </xsl:if>
                        
                        <xsl:for-each select="tei:span">
                            <xsl:variable name="FROM_ID" select="@from"/>
                            <xsl:variable name="TO_ID" select="@to"/>
                            <xsl:variable name="FROM_POSITION" select="$ABCOPY/descendant::*[@xml:id=$FROM_ID]/count(preceding-sibling::*[@xml:id])"/>
                            <xsl:variable name="TO_POSITION" select="$ABCOPY/descendant::*[@xml:id=$TO_ID]/count(preceding-sibling::*[@xml:id])"/>
                            <td>
                                <xsl:attribute name="colspan" select="$TO_POSITION - $FROM_POSITION + 1"/>
                                <xsl:value-of select="text()"/>
                            </td>
                            
                            <xsl:if test="following-sibling::tei:span">
                                <xsl:variable name="NEXT_FROM_ID" select="following-sibling::tei:span[1]/@from"/>
                                <xsl:variable name="NEXT_FROM_POSITION" select="$ABCOPY/descendant::*[@xml:id=$NEXT_FROM_ID]/count(preceding-sibling::*[@xml:id])"/>
                                <!-- 
                                        <pause type="medium" xml:id="p353"/>
                                        <incident rend="breathes in" xml:id="i224">
                                            <desc>breathes in</desc>
                                        </incident>
                                        
                                        <anchor synch="T4"/>
                                        
                                        <w xml:id="w6249" norm="before" pos="IN" lemma="before" phon="b I . f ' O:">Before</w>
                                        <w xml:id="w6250" norm="the" pos="DT" lemma="the" phon="D ' @">the</w>
                                        <w xml:id="w6251" norm="word" pos="NN" lemma="word" phon="w ' 3: d">word</w>
                                        ...
                                        <w xml:id="w6257" norm="life" pos="NN" lemma="life" phon="l ' aI f">life</w>
                                        <pc xml:id="pc1348">,</pc>
                                
                                        <span from="p353" to="i224">• • ((atmet ein)) </span>
                                        <span from="w6249" to="pc1348">Bevor das Wort "Beatles" in dein Leben trat, </span>
                                
                                -->
                                
                                <!-- NO! Need to check if the last to and the next from are the same OR ADJACENT -->
                                <!-- <xsl:if test="not($NEXT_FROM_POSITION = $TO_POSITION)"> -->
                                <xsl:if test="$NEXT_FROM_POSITION - $TO_POSITION &gt; 1">
                                    <td class="empty">
                                            <xsl:attribute name="colspan" select="$NEXT_FROM_POSITION - $TO_POSITION + 1"/>
                                    </td>
                                </xsl:if>
                            </xsl:if>
                            
                        </xsl:for-each>
                        
                        <xsl:if test="not($LAST_TO=$LAST_TOKEN_ID)">
                            <td class="empty">
                                <xsl:attribute name="colspan"
                                    select="$ABCOPY/descendant::*[@xml:id=$LAST_TO]/count(following-sibling::*[@xml:id])">
                                </xsl:attribute>
                            </td>
                        </xsl:if>
                        
                    </tr>
                </xsl:for-each>
                
            </tbody>
        </table>
            
<!--
        <table>
            <thead>
                <tr>
                    <xsl:for-each select="$SYNCPOINTS/descendant::syncPoint">
                        <th>
                            <xsl:value-of select="@id"/><br/>
                            <xsl:value-of select="@type"/><br/>
                            <xsl:value-of select="@element"/><br/>
                            <xsl:value-of select="count(preceding-sibling::*)"/><br/>
                        </th>                    
                    </xsl:for-each>
                </tr>
            </thead>
            <tbody>
                <xsl:for-each-group select="tei:u/descendant::*[@xml:id and string-length(normalize-space())&gt;0]" group-by="count(ancestor::*)">
                    <tr>
                        <xsl:variable name="FIRST_ID" select="current-group()[1]/@xml:id"/>
                        <xsl:message select="$SYNCPOINTS"/>
                        <xsl:variable name="FIRST_START" select="$SYNCPOINTS/descendant::syncPoint[@id=$FIRST_ID]/count(preceding-sibling::*)"/>
                        <xsl:if test="$FIRST_START &gt; 0">
                            <td class="empty">
                                <xsl:attribute name="colspan" select="$FIRST_START"/>
                            </td>
                        </xsl:if>
                        
                        <xsl:for-each select="current-group()">
                            <xsl:variable name="ID" select="@xml:id"/>
                            <xsl:variable name="index" select="position()"/>
                            <xsl:variable name="START" select="$SYNCPOINTS/descendant::syncPoint[@id=$ID]/count(preceding-sibling::*)"/>
                            <td>
                                <xsl:value-of select="text()"/><br/>
                                <xsl:for-each select="@*">
                                    <xsl:value-of select="."/><br/>
                                </xsl:for-each>
                            </td>
                            
                            <xsl:variable name="NEXT_ID" select="current-group()[$index+1]/@xml:id"/>
                            <xsl:variable name="NEXT_START" select="$SYNCPOINTS/descendant::syncPoint[@id=$NEXT_ID]/count(preceding-sibling::*)"/>
                            <xsl:if test="($NEXT_START - $START) &gt; 1">
                                <td class="empty">
                                    <xsl:attribute name="colspan" select="($NEXT_START - $START) - 1"/>
                                </td>                                
                            </xsl:if>
                            
                        </xsl:for-each>
                    </tr>
                </xsl:for-each-group>
            </tbody>
        </table>-->
        
        <hr style="margin-top:20px; margin-bottom:10px"/>
        
       
        <xsl:choose>
            
            
            <!-- This is the default case: we do not have <seg> beneath <w> -->
            <xsl:when test="not($ABCOPY/descendant::tei:w/tei:seg)"></xsl:when>
            <!-- This is the special case for the  -->
            <xsl:otherwise>
                
            </xsl:otherwise>
        </xsl:choose>
        
    </xsl:template>
</xsl:stylesheet>
