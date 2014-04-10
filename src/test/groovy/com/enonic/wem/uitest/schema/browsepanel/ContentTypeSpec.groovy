package com.enonic.wem.uitest.schema.browsepanel

import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.pages.schemamanager.wizardpanel.ContentTypeWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.SchemaCfgHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.schemamanger.ContentType
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.FolderContentTypeCfg
import com.enonic.wem.uitest.schema.cfg.TextLineContentTypeCfg
import spock.lang.Shared

class ContentTypeSpec
    extends BaseGebSpec
{

    @Shared
    SchemaBrowsePanel schemaBrowsePanel;

    def setup()
    {
        go "admin"
        schemaBrowsePanel = NavigatorHelper.openSchemaManager( getTestSession() );
    }

    def "GIVEN BrowsePanel WHEN adding Folder 'Content type' Then the new content type should be listed in the table"()
    {
        given:
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG;
        ContentType ctype = ContentType.newContentType().name( "folderctype" ).configData( folderCFG ).build();

        when:
        schemaBrowsePanel.clickToolbarNew().selectKind( ctype.getSchemaKindUI().getValue() ).
            typeData( ctype ).save().close();
        schemaBrowsePanel.waituntilPageLoaded( 3 );
        schemaBrowsePanel.expandSuperTypeFolder( ctype.getSuperTypeNameFromConfig() );

        then:
        schemaBrowsePanel.exists( ctype );

    }

    def "GIVEN schema BrowsePanel and exist 'Content type'  WHEN 'Content type' edited, display-name changed  Then the Content type with new display-name should be listed in the table"()
    {
        given:
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctype = ContentType.newContentType().name( "editdisplaynametest" ).configData( folderCFG ).build();
        schemaBrowsePanel.clickToolbarNew().selectKind( ctype.getSchemaKindUI().getValue() ).typeData( ctype ).save().close()
        schemaBrowsePanel.waituntilPageLoaded( 3 );

        when:
        ContentType newContentType = cloneContentTypeWithNewDisplayName( ctype )
        schemaBrowsePanel.expandSuperTypeFolder( ctype.getSuperTypeNameFromConfig() ).selectRowWithContentType( ctype.getName(),
                                                                                                                ctype.getDisplayNameFromConfig() );
        ContentTypeWizardPanel wizard = schemaBrowsePanel.clickToolbarEdit().typeData( newContentType );
        wizard.save();
        TestUtils.saveScreenshot( getSession(), newContentType.getName() );
        wizard.waitNotificationMessage();
        wizard.close();
        TestUtils.saveScreenshot( getSession(), newContentType.getName() );

        then:
        schemaBrowsePanel.exists( newContentType );
    }

    def "GIVEN schema BrowsePanel and exist 'Content type'  WHEN Content type edited, name changed  THEN the 'Content type' with new name should be listed in the table"()
    {
        given:
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctype = ContentType.newContentType().name( "editnametest" ).configData( folderCFG ).build();
        schemaBrowsePanel.clickToolbarNew().selectKind( ctype.getSchemaKindUI().getValue() ).typeData( ctype ).save().close();
        schemaBrowsePanel.waituntilPageLoaded( 3 );

        when:
        ContentType newContentType = cloneContentTypeWithNewName( ctype );
        schemaBrowsePanel.expandSuperTypeFolder( ctype.getSuperTypeNameFromConfig() ).selectRowWithContentType( ctype.getName(),
                                                                                                                ctype.getDisplayNameFromConfig() ).
            clickToolbarEdit().typeData( newContentType ).save().close();

        then:
        schemaBrowsePanel.exists( newContentType );

    }

    def "GIVEN BrowsePanel WHEN adding 'TextLine - Content type' THEN the new 'Content type' should be listed in the table"()
    {
        given:
        String textLineCFG = TextLineContentTypeCfg.CFG
        ContentType ctype = ContentType.newContentType().name( "textlinectype" ).configData( textLineCFG ).build();

        when:
        schemaBrowsePanel.clickToolbarNew().selectKind( ctype.getSchemaKindUI().getValue() ).
            typeData( ctype ).save().close();
        schemaBrowsePanel.waituntilPageLoaded( 3 );
        schemaBrowsePanel.expandSuperTypeFolder( ctype.getSuperTypeNameFromConfig() );

        then:
        schemaBrowsePanel.exists( ctype );

    }

    def "GIVEN BrowsePanel and created a 'Content type' WHEN 'Content type' deleted THEN the it should not be listed in the table"()
    {
        given:
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG;
        ContentType ctypeToDelete = ContentType.newContentType().name( "ctypetodelete" ).configData( folderCFG ).build();
        schemaBrowsePanel.clickToolbarNew().selectKind( ctypeToDelete.getSchemaKindUI().getValue() ).typeData( ctypeToDelete ).
            save().close();
        schemaBrowsePanel.waituntilPageLoaded( 3 );

        when:
        schemaBrowsePanel.expandSuperTypeFolder( ctypeToDelete.getSuperTypeNameFromConfig() ).
            selectRowWithContentType( ctypeToDelete.getName(), ctypeToDelete.getDisplayNameFromConfig() ).
            clickToolbarDelete().doDelete();

        then:
        !schemaBrowsePanel.exists( ctypeToDelete );

    }


    ContentType cloneContentTypeWithNewDisplayName( ContentType source )
    {
        String newDisplayName = NameHelper.uniqueName( "newdisplayname" );
        String newconfigData = SchemaCfgHelper.changeDisplayName( newDisplayName, source.getConfigData() );
        return ContentType.newContentType().name( source.getName() ).configData( newconfigData ).build();
    }

    ContentType cloneContentTypeWithNewName( ContentType source )
    {
        String newName = NameHelper.uniqueName( "newname" );
        return ContentType.newContentType().name( newName ).configData( source.getConfigData() ).build();
    }

}
	