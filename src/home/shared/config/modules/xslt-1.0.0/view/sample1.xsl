<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:portal="wem:portal">

  <xsl:output method="html" omit-xml-declaration="yes"/>

  <xsl:include href="portal-lib.xsl"/>

  <xsl:template match="/">
    <div>
      <a href="{portal:createServiceUrl('test', ())}">test</a>
    </div>
  </xsl:template>

</xsl:stylesheet>
