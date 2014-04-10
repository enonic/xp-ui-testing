<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:portal="wem:portal"
                xmlns:wem="http://enonic.com/wem"
                exclude-result-prefixes="wem portal">

  <xsl:output method="html" omit-xml-declaration="yes"/>

  <xsl:include href="portal-lib.xsl"/>
  <xsl:param name="editable" select="false()"/>
  <xsl:param name="title" select="''"/>

  <xsl:template match="/">
    <html lang="en">
    <head>
      <meta charset="utf-8"/>
      <title><xsl:value-of select="$title"/></title>
      <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
      <meta name="description" content=""/>
      <meta name="author" content=""/>

      <link href="{portal:createResourceUrl('css/bootstrap.css')}" rel="stylesheet"/>
      <link href="{portal:createResourceUrl('css/main.css')}" rel="stylesheet"/>

      <link href="{portal:createResourceUrl('css/bootstrap-responsive.css')}" rel="stylesheet"/>
    </head>

    <body data-live-edit-type="page" data-live-edit-key="{{path}}">
    <script src="{portal:createResourceUrl('js/jquery.js')}"></script>

    <xsl:copy-of select="portal:includeComponent('xslt-header')"/>

      <div class="container">

    <div class="masthead">

      <h3 class="muted"><xsl:value-of select="$title"/></h3>

      <div class="navbar">
        <div class="navbar-inner">
          <div class="container">
            <ul class="nav">
              <li><a href="#">Forside</a></li>
              <li class="active"><a href="#">Trampoline</a></li>
              <li class="active"><a href="#">Trampoline</a></li>
              <li><a href="#">Kjøpeguide</a></li>
              <li><a href="#">Kundeservice</a></li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <xsl:call-template name="portal:renderRegion">
      <xsl:with-param name="name" select="'main'"/>
    </xsl:call-template>

      <hr/>

        <div class="footer">
          <small class="pull-right">Demo site made by Enonic 2014</small>
        </div>

      </div>


      <script src="{portal:createResourceUrl('js/bootstrap.js')}"></script>
    </body>
    </html>
  </xsl:template>

</xsl:stylesheet>
