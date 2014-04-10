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
    <div class="row-fluid"
         data-live-edit-type="{$componentType}"
         data-live-edit-component="{$componentPath}">

      <div class="span8">
        <img id="bounce-image" src="{portal:createResourceUrl('img/Prod_stor_studsmattor_Elite_rod.jpg')}" alt=""/>
      </div>
      <div class="span4">
        <h3>Jumping Jack - Big Bounce</h3>

        <select id="bounce-selector">
          <option value="Prod_stor_studsmattor_Elite_rod.jpg" selected="selected">4,3m</option>
          <option value="Prod_stor_studsmattor_Elite-tattoo_rod.jpg">4,3m m/trykk</option>
        </select>
        <br/>
        <br/>

        <p>
          <i class="icon-remove-sign"></i>
          Ikke på lager
          <br/>
          <i class="icon-ok-sign"></i>
          Leveres
        </p>
        <br/>
        <br/>

        <div class="well well-small">
          <div class="row-fluid">
            <div class="span4">
              <h3>7990,-
                <small>Inkl. mva</small>
              </h3>

            </div>
            <div class="span6 pull-right">
              <br/>
              <button class="btn btn-large btn-warning">Kjøp nå</button>
            </div>
          </div>
          <hr/>
          <p>Kjøp nå, og få Enonic WEM med på kjøpet</p>
        </div>
        <script type="text/javascript">
          $(function () {
          $('#bounce-selector').on('change', function () {
          var src = 'img/' + this.value;
          $('#bounce-image').attr('src', src);
          });
          });
        </script>
      </div>
    </div>
  </xsl:template>

</xsl:stylesheet>
