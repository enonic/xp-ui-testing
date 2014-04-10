<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:portal="wem:portal"
                xmlns:wem="http://enonic.com/wem"
                exclude-result-prefixes="wem portal">

  <xsl:output method="html" omit-xml-declaration="yes"/>

  <xsl:include href="portal-lib.xsl"/>

  <xsl:param name="editable" select="false()"/>
  <xsl:param name="title" select="''"/>
  <xsl:param name="contentPath" select="''"/>
  <xsl:param name="componentType" select="''"/>
  <xsl:param name="componentPath" select="''"/>
  <xsl:param name="imageUrl" select="''"/>

  <xsl:template match="/">
    <div data-live-edit-type="{$componentType}"
         data-live-edit-component="{$componentPath}">

      <img src="{$imageUrl}"/>

    </div>
  </xsl:template>

</xsl:stylesheet>
