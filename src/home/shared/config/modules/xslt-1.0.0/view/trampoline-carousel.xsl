<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:portal="wem:portal"
                xmlns:wem="http://enonic.com/wem"
                exclude-result-prefixes="wem portal">

  <xsl:output method="html" omit-xml-declaration="yes"/>

  <xsl:include href="portal-lib.xsl"/>
  <xsl:param name="editable" select="false()"/>
  <xsl:param name="title" select="''"/>
  <xsl:param name="componentType" select="''"/>
  <xsl:param name="componentPath" select="''"/>

  <xsl:template match="/">

    <div id="myCarousel"
         class="carousel slide"
         data-live-edit-type="{$componentType}"
         data-live-edit-component="{$componentPath}">

      <div class="carousel-inner">
        <div class="item active">
          <img src="{portal:createResourceUrl('img/Elite_01.jpg')}" alt=""/>
        </div>
        <div class="item">
          <img src="{portal:createResourceUrl('img/Elite_02.jpg')}" alt=""/>
        </div>
        <div class="item">
          <img src="{portal:createResourceUrl('img/Elite_03.jpg')}" alt=""/>
        </div>
        <div class="item">
          <img src="{portal:createResourceUrl('img/Elite_05.jpg')}" alt=""/>
        </div>
      </div>
      <a class="left carousel-control" href="#myCarousel" data-slide="prev">&#171;</a>
      <a class="right carousel-control" href="#myCarousel" data-slide="next">&#187;</a>
      <script>
        !function ($) {
        $(function () {
        // carousel demo
        $('#myCarousel').carousel()
        })
        }(window.jQuery)
      </script>
    </div>
  </xsl:template>

</xsl:stylesheet>
