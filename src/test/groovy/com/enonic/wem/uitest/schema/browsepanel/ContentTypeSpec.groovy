package com.enonic.wem.uitest.schema.browsepanel

import com.enonic.autotests.pages.schemamanager.KindOfContentTypes
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

    def "GIVEN BrowsePanel WHEN adding Folder ContentType Then the new contentype should be listed in the table"()
    {
        given:
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctype = ContentType.with().name( "folderctype" ).kind( KindOfContentTypes.CONTENT_TYPE ).configuration(
            folderCFG ).build();

        when:
        schemaBrowsePanel.clickToolbarNew().selectKind( KindOfContentTypes.CONTENT_TYPE.getValue() ).typeData( ctype ).save().close()

        then:
        schemaBrowsePanel.exists( ctype )

    }

    def "GIVEN schema BrowsePanel and exist Contentype  WHEN Contentype edited, display-name changed  Then the Contentype with new display-name should be listed in the table"()
    {
        given:
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctype = ContentType.with().name( "editdisplaynametest" ).kind( KindOfContentTypes.CONTENT_TYPE ).configuration(
            folderCFG ).build();
        schemaBrowsePanel.clickToolbarNew().selectKind( KindOfContentTypes.CONTENT_TYPE.getValue() ).typeData( ctype ).save().close()

        when:
        ContentType newContentType = cloneContentTypeWithNewDisplayName( ctype );
        schemaBrowsePanel.expandSuperTypeFolder( ctype.getSuperTypeNameFromConfig() ).selectRowWithContentType( ctype.getName(),
                                                                                                                ctype.getDisplayNameFromConfig() ).clickToolbarEdit().typeData(
            newContentType ).save().close()

        then:
        schemaBrowsePanel.exists( newContentType );
    }

    def "GIVEN schema BrowsePanel and exist Contentype  WHEN Contentype edited, name changed  THEN the Contentype with new name should be listed in the table"()
    {
        given:
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctype = ContentType.with().name( "editnametest" ).kind( KindOfContentTypes.CONTENT_TYPE ).configuration(
            folderCFG ).build();
        schemaBrowsePanel.clickToolbarNew().selectKind( KindOfContentTypes.CONTENT_TYPE.getValue() ).typeData( ctype ).save().close()

        when:
        ContentType newContentType = cloneContentTypeWithNewName( ctype )
        schemaBrowsePanel.expandSuperTypeFolder( ctype.getSuperTypeNameFromConfig() ).selectRowWithContentType( ctype.getName(),
                                                                                                                ctype.getDisplayNameFromConfig() ).clickToolbarEdit().typeData(
            newContentType ).save().close()

        then:
        schemaBrowsePanel.exists( newContentType );

    }

    def "GIVEN BrowsePanel WHEN adding TextLine ContentType THEN the new Contentype should be listed in the table"()
    {
        given:
        String textLineCFG = TextLineContentTypeCfg.CFG
        ContentType ctype = ContentType.with().name( "textlinectype" ).kind( KindOfContentTypes.CONTENT_TYPE ).configuration(
            textLineCFG ).build();

        when:
        schemaBrowsePanel.clickToolbarNew().selectKind( KindOfContentTypes.CONTENT_TYPE.getValue() ).typeData( ctype ).save().close()

        then:
        SchemaBrowsePanel grid = new SchemaBrowsePanel( getTestSession() )
        grid.exists( ctype )

    }

    def "GIVEN BrowsePanel and created a contentType WHEN Contenttype deleted THEN the Contentype should not be listed in the table"()
    {
        given:
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctypeToDelete = ContentType.with().name( "ctypetodelete" ).kind( KindOfContentTypes.CONTENT_TYPE ).configuration(
            folderCFG ).build();
        schemaBrowsePanel.clickToolbarNew().selectKind( KindOfContentTypes.CONTENT_TYPE.getValue() ).typeData(
            ctypeToDelete ).save().close()
        when:
        schemaBrowsePanel.expandSuperTypeFolder( ctypeToDelete.getSuperTypeNameFromConfig() ) selectRowWithContentType(
            ctypeToDelete.getName(), ctypeToDelete.getDisplayNameFromConfig() ).clickToolbarDelete().doDelete()

        then:
        !schemaBrowsePanel.exists( ctypeToDelete );

    }

    ContentType cloneContentTypeWithNewName( ContentType contenTypeToEdit )
    {
        ContentType newContenttype = contenTypeToEdit.cloneContentType()
        String name = NameHelper.unqiueName( "edited" );
        newContenttype.setName( name )
        return newContenttype
    }

    ContentType cloneContentTypeWithNewDisplayName( ContentType contenTypeToEdit )
    {
        ContentType newContentType = contenTypeToEdit.cloneContentType();
        String displayName = NameHelper.unqiueName( "edited" );
        newContentType.setDisplayNameInConfig( displayName );
        return newContentType
    }

}
	