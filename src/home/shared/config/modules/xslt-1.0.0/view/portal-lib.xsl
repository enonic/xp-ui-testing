<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:portal="wem:portal"
                xmlns:wem="http://enonic.com/wem"
                exclude-result-prefixes="wem portal xsd">

  <xsl:param name="_" as="node()" required="yes"/>

  <xsl:function name="portal:includeComponent" as="node()*">
    <xsl:param name="componentName" as="xsd:string"/>
    <xsl:comment>
      <xsl:text># COMPONENT module:</xsl:text><xsl:value-of select="$componentName"/>
    </xsl:comment>
  </xsl:function>

  <xsl:function name="portal:renderComponent" as="node()*">
    <xsl:param name="path" as="xsd:string"/>
    <xsl:comment>
      <xsl:text># COMPONENT </xsl:text><xsl:value-of select="$path"/>
    </xsl:comment>
  </xsl:function>

  <xsl:template name="portal:renderRegion">
    <xsl:param name="name"/>
    <xsl:variable name="regionPath" select="$_/context/regions/region[@name=$name]/@path"/>
    <div data-live-edit-type="region"
         data-live-edit-region="{$regionPath}">
      <xsl:for-each select="$_/context/regions/region[@name=$name]/components/component">
        <xsl:copy-of select="portal:renderComponent(@path)"/>
      </xsl:for-each>
    </div>
  </xsl:template>

  <xsl:function name="portal:createUrl" as="xsd:string">
    <xsl:param name="path" as="xsd:string"/>
    <xsl:value-of select="portal:createUrl($path, ())"/>
  </xsl:function>

  <xsl:function name="portal:createUrl" as="xsd:string">
    <xsl:param name="path" as="xsd:string"/>
    <xsl:param name="params" as="xsd:anyAtomicType*"/>
    <xsl:variable name="args">
      <xsl:for-each select="$params">
        <xsl:choose>
          <xsl:when test="(position() mod 2) != 0">
            <xsl:choose>
              <xsl:when test="position() = 1">
                <xsl:text>?</xsl:text>
              </xsl:when>
              <xsl:otherwise>
                <xsl:text><![CDATA[&]]></xsl:text>
              </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="concat(., '=')"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="."/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
    </xsl:variable>
    <xsl:value-of select="concat($_/context/baseUrl, '/', $path, $args)"/>
  </xsl:function>

  <xsl:function name="portal:createServiceUrl" as="xsd:string">
    <xsl:param name="name" as="xsd:string"/>
    <xsl:param name="params" as="xsd:anyAtomicType*"/>
    <xsl:value-of select="portal:createUrl(concat('_/service/', $name), $params)"/>
  </xsl:function>

  <xsl:function name="portal:createResourceUrl" as="xsd:string">
    <xsl:param name="path" as="xsd:string"/>
    <xsl:value-of select="concat($_/context/baseUrl, '/', $path)"/>
  </xsl:function>

</xsl:stylesheet>
