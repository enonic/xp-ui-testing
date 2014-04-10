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
    <div
        data-live-edit-type="{$componentType}"
        data-live-edit-component="{$componentPath}">

      <h3>Tilbehør</h3>

      <div class="media" data-live-edit-type="content" data-live-edit-name="Accessories - Ladder">
        <a href="#" class="pull-left">
          <img src="{portal:createResourceUrl('img/Prod_liten_tillbehor_stege.png')}" height="64px" width="99px"/>
        </a>

        <div class="media-body">
          <h4>Stige</h4>

          <p>Pris
            <br/>
            100,-
          </p>

        </div>
        <hr/>
      </div>
      <div class="media" data-live-edit-type="content" data-live-edit-name="Accessories - Lower safety net">
        <a href="#" class="pull-left">
          <img src="{portal:createResourceUrl('img/Prod_liten_tillbehor_nedre-skyddsnat_1.png')}" height="64px" width="99px"/>
        </a>

        <div class="media-body">
          <h4>Rammenett</h4>

          <p>Pris
            <br/>
            539,-
          </p>

        </div>
        <hr/>
      </div>
      <div class="media" data-live-edit-type="content" data-live-edit-name="Accessories - Cover">
        <a href="#" class="pull-left">
          <img src="{portal:createResourceUrl('img/Prod_liten_tillbehor_overdragsskydd_basic.png')}" height="64px" width="99px"/>
        </a>

        <div class="media-body">
          <h4>Overtrekk</h4>

          <p>Pris
            <br/>
            349,-
          </p>

        </div>
        <hr/>
      </div>
      <div class="media" data-live-edit-type="content" data-live-edit-name="Accessories - Safety net">
        <a href="#" class="pull-left">
          <img src="{portal:createResourceUrl('img/Prod_liten_Champion-med-nat.png')}" height="64px" width="99px"/>
        </a>

        <div class="media-body">
          <h4>Sikkerhetsnett</h4>

          <p>Pris
            <br/>
            1799,-
          </p>

        </div>
        <hr/>
      </div>
    </div>
  </xsl:template>

</xsl:stylesheet>
