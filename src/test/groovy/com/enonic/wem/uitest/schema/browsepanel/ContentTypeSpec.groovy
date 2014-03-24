package com.enonic.wem.uitest.schema.browsepanel

import com.enonic.autotests.pages.schemamanager.SchemaType
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.schemamanger.ContentType
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.FolderContentTypeCfg
import com.enonic.wem.uitest.schema.cfg.TextLineContentTypeCfg
import spock.lang.Shared

class ContentTypeSpec
    extends BaseGebSpec
{

    @Shared
    SchemaBrowsePanel schemaBrowsePanel

    def setup()
    {
        go "admin"
        schemaBrowsePanel = NavigatorHelper.openSchemaManager( getTestSession() );
    }

    def "GIVEN BrowsePanel WHEN adding Folder 'Content type' Then the new content type should be listed in the table"()
    {
        given:
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctype = ContentType.with().name( "folderctype" ).schemaType( SchemaType.CONTENT_TYPE ).configuration(
            folderCFG ).build();

        when:
        schemaBrowsePanel.clickToolbarNew().selectKind( SchemaType.CONTENT_TYPE.getValue() ).typeData( ctype ).save().close()
		schemaBrowsePanel.expandSuperTypeFolder(ctype.getSuperTypeNameFromConfig())

        then:
        schemaBrowsePanel.exists( ctype )

    }

    def "GIVEN schema BrowsePanel and exist 'Content type'  WHEN 'Content type' edited, display-name changed  Then the Content type with new display-name should be listed in the table"()
    {
        given:
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctype = ContentType.with().name( "editdisplaynametest" ).schemaType( SchemaType.CONTENT_TYPE ).configuration(
            folderCFG ).build();
        schemaBrowsePanel.clickToolbarNew().selectKind( SchemaType.CONTENT_TYPE.getValue() ).typeData( ctype ).save().close()

        when:
        ContentType newContentType = cloneContentTypeWithNewDisplayName( ctype )
        schemaBrowsePanel.expandSuperTypeFolder( ctype.getSuperTypeNameFromConfig() ).selectRowWithContentType( ctype.getName(),
                                                                                                                ctype.getDisplayNameFromConfig() ).clickToolbarEdit().typeData(
            newContentType ).save().close()

        then:
        schemaBrowsePanel.exists( newContentType )
    }

    def "GIVEN schema BrowsePanel and exist 'Content type'  WHEN Content type edited, name changed  THEN the 'Content type' with new name should be listed in the table"()
    {
        given:
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctype = ContentType.with().name( "editnametest" ).schemaType( SchemaType.CONTENT_TYPE ).configuration(
            folderCFG ).build();
        schemaBrowsePanel.clickToolbarNew().selectKind( SchemaType.CONTENT_TYPE.getValue() ).typeData( ctype ).save().close()

        when:
        ContentType newContentType = cloneContentTypeWithNewName( ctype )
        schemaBrowsePanel.expandSuperTypeFolder( ctype.getSuperTypeNameFromConfig() ).selectRowWithContentType( ctype.getName(),
                                                                                                                ctype.getDisplayNameFromConfig() ).clickToolbarEdit().typeData(
            newContentType ).save().close()

        then:
        schemaBrowsePanel.exists( newContentType )

    }

    def "GIVEN BrowsePanel WHEN adding 'TextLine - Content type' THEN the new 'Content type' should be listed in the table"()
    {
        given:
        String textLineCFG = TextLineContentTypeCfg.CFG
        ContentType ctype = ContentType.with().name( "textlinectype" ).schemaType( SchemaType.CONTENT_TYPE ).configuration(
            textLineCFG ).build();

        when:
        schemaBrowsePanel.clickToolbarNew().selectKind( SchemaType.CONTENT_TYPE.getValue() ).typeData( ctype ).save().close()
		schemaBrowsePanel.expandSuperTypeFolder(ctype.getSuperTypeNameFromConfig())
		
        then:
        schemaBrowsePanel.exists( ctype )

    }

    def "GIVEN BrowsePanel and created a 'Content type' WHEN 'Content type' deleted THEN the it should not be listed in the table"()
    {
        given:
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctypeToDelete = ContentType.with().name( "ctypetodelete" ).schemaType( SchemaType.CONTENT_TYPE ).configuration(
            folderCFG ).build();
        schemaBrowsePanel.clickToolbarNew().selectKind( SchemaType.CONTENT_TYPE.getValue() ).typeData(
            ctypeToDelete ).save().close()
        when:
        schemaBrowsePanel.expandSuperTypeFolder( ctypeToDelete.getSuperTypeNameFromConfig() ).selectRowWithContentType(
            ctypeToDelete.getName(), ctypeToDelete.getDisplayNameFromConfig() ).clickToolbarDelete().doDelete()

        then:
        !schemaBrowsePanel.exists( ctypeToDelete )

    }

    ContentType cloneContentTypeWithNewName( ContentType contenTypeToEdit )
    {
        ContentType newContenttype = contenTypeToEdit.cloneContentType()
        String name = NameHelper.uniqueName( "edited" )
        newContenttype.setName( name )
        return newContenttype
    }

    ContentType cloneContentTypeWithNewDisplayName( ContentType contenTypeToEdit )
    {
        ContentType newContentType = contenTypeToEdit.cloneContentType()
        String displayName = NameHelper.uniqueName( "edited" )
        newContentType.setDisplayNameInConfig( displayName )
        return newContentType
    }

}
	