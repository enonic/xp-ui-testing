package com.enonic.wem.uitest.schema.browsepanel

import com.enonic.autotests.pages.schemamanager.NewSchemaDialog
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.pages.schemamanager.wizardpanel.ContentTypeWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.schemamanger.ContentType
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.FolderContentTypeCfg
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class SchemaBrowsePanel_ContextMenuSpec
    extends BaseGebSpec
{
    @Shared
    SchemaBrowsePanel schemaBrowsePanel;

    private String TEST_CONTENTYPE_KEY = "test_ctype";

    def setup()
    {
        go "admin"
        schemaBrowsePanel = NavigatorHelper.openSchemaManager( getTestSession() );
    }

    def "GIVEN a content type WHEN content type selected and 'Edit' selected from the context menu THEN content type wizard opened"()
    {
        given:
        String ctypeToCreate = FolderContentTypeCfg.FOLDER_CFG;
        ContentType ctype = ContentType.newContentType().name( NameHelper.uniqueName( "folderctype" ) ).configData( ctypeToCreate ).build();
        schemaBrowsePanel.clickToolbarNew().selectKind( ctype.getSchemaKindUI().getValue() ).
            typeData( ctype ).save().close();
        schemaBrowsePanel.waituntilPageLoaded( 3 );
        getTestSession().put( TEST_CONTENTYPE_KEY, ctype );

        when:
        schemaBrowsePanel.expandSuperTypeFolder( ctype.getSuperTypeNameFromConfig() );
        ContentTypeWizardPanel wizard = schemaBrowsePanel.selectEditInContextMenu( ctype );

        then:
        wizard.waitUntilWizardOpened();

    }

    def "GIVEN a content type WHEN content type selected and 'New' selected from the context menu THEN content type wizard opened"()
    {
        given:
        ContentType ctype = (ContentType) getTestSession().get( TEST_CONTENTYPE_KEY );
        schemaBrowsePanel.expandSuperTypeFolder( ctype.getSuperTypeNameFromConfig() );

        when:
        NewSchemaDialog newSchemaDialog = schemaBrowsePanel.selectNewInContextMenu( ctype );

        then:
        newSchemaDialog.isOpened( 3 );

    }

    def "GIVEN a content type WHEN content type selected and 'Delete' selected from the context menu THEN content type wizard opened"()
    {
        given:
        ContentType ctype = (ContentType) getTestSession().get( TEST_CONTENTYPE_KEY );
        schemaBrowsePanel.expandSuperTypeFolder( ctype.getSuperTypeNameFromConfig() );

        when:
        schemaBrowsePanel.selectDeleteInContextMenu( ctype ).doDelete();
        schemaBrowsePanel.waituntilPageLoaded( 3 );

        then:
        !schemaBrowsePanel.exists( ctype );

    }

}
