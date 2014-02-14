package com.enonic.wem.uitest.schema.browsepanel

import com.enonic.autotests.pages.schemamanager.KindOfContentTypes
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.services.ContentTypeService
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.schemamanger.ContentType
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.FolderContentTypeCfg
import com.enonic.wem.uitest.schema.cfg.TextLineContentTypeCfg
import spock.lang.Shared

class ContentTypeSpec extends BaseGebSpec
{

    @Shared ContentTypeService contentTypeService = new ContentTypeService();

    def "GIVEN BrowsePanel WHEN adding Folder ContentType Then the new contentype should be listed in the table"( )
    {
        given:
        go "admin"
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctype = ContentType.with().name( "folderctype" ).kind( KindOfContentTypes.CONTENT_TYPE ).configuration( folderCFG ).build();

        when:
        SchemaBrowsePanel grid = contentTypeService.createContentType( getTestSession(), ctype, true )

        then:
        grid.isContentTypePresentInTable( ctype )

    }

    def "GIVEN schema BrowsePanel and exist Contentype  WHEN Contentype editet, display-name changed  Then the Contentype whith new display-name should be listed in the table"( )
    {
        given:
        go "admin"
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctype = ContentType.with().name( "editdisplaynametest" ).kind( KindOfContentTypes.CONTENT_TYPE ).configuration( folderCFG ).build();
        SchemaBrowsePanel grid = contentTypeService.createContentType( getTestSession(), ctype, true );

        when:
        ContentType newContentType = cloneContentTypeWithNewDisplayName( ctype );
        contentTypeService.editContentType( getTestSession(), ctype, newContentType );

        then:
        grid.isContentTypePresentInTable( newContentType );
    }

    def "GIVEN schema BrowsePanel and exist Contentype  WHEN contentype editet, name changed  THEN the contentype whith new name should be listed in the table"( )
    {
        given:
        go "admin"
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctype = ContentType.with().name( "editnametest" ).kind( KindOfContentTypes.CONTENT_TYPE ).configuration( folderCFG ).build();
        SchemaBrowsePanel grid = contentTypeService.createContentType( getTestSession(), ctype, true );

        when:
        ContentType newContentType = cloneContentTypeWithNewName( ctype )
        contentTypeService.editContentType( getTestSession(), ctype, newContentType );

        then:
        grid.isContentTypePresentInTable( newContentType );

    }

    def "GIVEN BrowsePanel WHEN adding TextLine ContentType THEN the new contentype should be listed in the table"( )
    {
        given:
        go "admin"
        String textLineCFG = TextLineContentTypeCfg.CFG
        ContentType ctype = ContentType.with().name( "textlinectype" ).kind( KindOfContentTypes.CONTENT_TYPE ).configuration( textLineCFG ).build();

        when:
        contentTypeService.createContentType( getTestSession(), ctype, true )

        then:
        SchemaBrowsePanel grid = new SchemaBrowsePanel( getTestSession() )
        grid.isContentTypePresentInTable( ctype )

    }

    def "GIVEN BrowsePanel and created a contentType WHEN contenttype deleted THEN the the contentype should not be listed in the table"( )
    {
        given:
        go "admin"
        String folderCFG = FolderContentTypeCfg.FOLDER_CFG
        ContentType ctypeToDelete = ContentType.with().name( "ctypetodelete" ).kind( KindOfContentTypes.CONTENT_TYPE ).configuration( folderCFG ).build();

        when:
        contentTypeService.createContentType( getTestSession(), ctypeToDelete, true )

        then:
        SchemaBrowsePanel schemasPage = contentTypeService.deleteContentType( getTestSession(), ctypeToDelete );
        !schemasPage.isContentTypePresentInTable( ctypeToDelete );

    }

    ContentType cloneContentTypeWithNewName( ContentType contenTypeToEdit )
    {
        ContentType newContenttype = contenTypeToEdit.cloneContentType()
        String name = NameHelper.unqiueContentName( "edited" );
        newContenttype.setName( name )
        return newContenttype
    }

    ContentType cloneContentTypeWithNewDisplayName( ContentType contenTypeToEdit )
    {
        ContentType newContentType = contenTypeToEdit.cloneContentType();
        String displayName = NameHelper.unqiueContentName( "edited" );
        newContentType.setDisplayNameInConfig( displayName );
        return newContentType
    }

}
	