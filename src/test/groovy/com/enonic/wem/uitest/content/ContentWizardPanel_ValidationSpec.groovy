package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.schemamanager.KindOfContentTypes
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.vo.schemamanger.ContentType
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.TwoTextLineContentTypeCfg
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentWizardPanel_ValidationSpec
    extends BaseGebSpec
{
    @Shared
    String CTYPE_NAME = "twotextline"

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    def setup()
    {
        go "admin"

    }

    def "create a contenttype with two textline"()
    {
        given:
        String twoTextlineCFG = TwoTextLineContentTypeCfg.CFG
        ContentType ctype = ContentType.with().name( CTYPE_NAME ).kind( KindOfContentTypes.CONTENT_TYPE ).configuration(
            twoTextlineCFG ).build();
        SchemaBrowsePanel schemaBrowsePanel = NavigatorHelper.openSchemaManager( getTestSession() );
        schemaBrowsePanel.clickToolbarNew().selectKind( KindOfContentTypes.CONTENT_TYPE.getValue() ).typeData( ctype ).save().close()
    }

    def "GIVEN a ContentType with two TextLine Inputs AND one is required WHEN no inputs are filled out THEN Publish-button is disabled"()
    {
        given:
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() )
        contentBrowsePanel.clickToolbarNew().selectContentType( CTYPE_NAME ).waitUntilWizardOpened()

        when:
        $( "input", name: "requiredTextLine" ) << ''
        $( "input", name: "unrequiredTextLine" ) << ''

        then:
        waitFor { $( "button", text: "Publish", disabled: "true" ).size() == 1 }
    }

    @Ignore
    def "GIVEN a ContentType with two TextLine Inputs AND one is required WHEN only the required input is filled out THEN Publish-button is enabled"()
    {
        given:
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() )
        contentBrowsePanel.clickToolbarNew().selectContentType( CTYPE_NAME ).waitUntilWizardOpened()

        when:
        $( "input", name: "requiredTextLine" ) << 'required line'

        then:
        waitFor { $( "button", text: "Publish", disabled: "true" ).size() == 0 }
    }


    def "GIVEN a ContentType with two TextLine Inputs AND one is required WHEN both inputs are filled out THEN Publish-button is enabled"()
    {
        given:
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
        contentBrowsePanel.clickToolbarNew().selectContentType( CTYPE_NAME ).waitUntilWizardOpened()

        when:
        $( "input", name: "requiredTextLine" ) << 'required line'
        $( "input", name: "unrequiredTextLine" ) << 'unrequired line'

        then:
        waitFor { $( "button", text: "Publish", disabled: "true" ).size() == 0 }

    }

    def "GIVEN a ContentType with two TextLine Inputs AND one is required WHEN only the unrequired input is filled out THEN Publish-button is disabled"()
    {
        given:
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
        contentBrowsePanel.clickToolbarNew().selectContentType( CTYPE_NAME ).waitUntilWizardOpened()

        when:
        $( "input", name: "unrequiredTextLine" ) << 'unrequired line'

        then:
        waitFor { $( "button", text: "Publish", disabled: "true" ).size() == 1 }
    }


}
