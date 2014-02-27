package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.ContentPathHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.*
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanelSpec
extends BaseGebSpec
{
    @Shared
    String REPONAME = "test-folder";

    @Shared
    String[] PARENT_FOLDER_ARR = [REPONAME];

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    def setup( )
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }


    def "GIVEN BrowsePanel WHEN adding Folder on root THEN the content should be listed in the table"( )
    {
        given:
        ContentPath cpath = ContentPathHelper.buildContentPath( null, REPONAME )
        BaseAbstractContent content = FolderContent.builder().withName( REPONAME ).withDisplayName( REPONAME ).withContentPath( cpath ).build();

        when:
        contentBrowsePanel.doAddContent( content, true );

        then:
        contentBrowsePanel.exists( content.getContentPath() );
    }


    def "GIVEN creating new Folder-content beneath an existing unexpanded WHEN conten saved THEN new Content should be listed beneath parent"( )
    {
        given:
        String name = "foldercontent";
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        BaseAbstractContent content = FolderContent.builder().withName( name ).withDisplayName( "folder" ).withContentPath( cpath ).build()

        when:
        contentService.addContent( getTestSession(), content, true )
        contentBrowsePanel.expandContent( content.getContentPath().getParentPath() )

        then:
        contentBrowsePanel.exists( content.getContentPath() )
    }

    def "GIVEN creating new Structured beneath an existing unexpanded WHEN conten saved THEN the content should be listed beneath parent"( )
    {
        given:
        String name = "structuredcontent";
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        BaseAbstractContent content = StructuredContent.builder().withName( name ).withDisplayName( "structured" ).withContentPath( cpath ).build();


        when:
        contentService.addContent( getTestSession(), content, true )
        contentBrowsePanel.expandContent( content.getContentPath().getParentPath() );

        then:
        contentBrowsePanel.exists( content.getContentPath() );
    }

    def "GIVEN creating new Unstructured beneath an existing unexpanded WHEN conten saved THEN new Content should be listed beneath parent"( )
    {
        given:
        String name = "unstructuredcontent";
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        BaseAbstractContent content = UnstructuredContent.builder().withName( name ).withDisplayName( "unstructured" ).withContentPath( cpath ).build();

        when:
        contentService.addContent( getTestSession(), content, true )
        contentBrowsePanel.expandContent( content.getContentPath().getParentPath() );

        then:
        contentBrowsePanel.exists( content.getContentPath() );
    }

    def "GIVEN creating new Archive beneath an existing unexpanded WHEN conten saved THEN the content should be listed beneath parent"( )
    {
        given:
        String name = "archivecontent";
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        BaseAbstractContent content = ArchiveContent.builder().withName( name ).withDisplayName( "archive" ).withContentPath( cpath ).build()


        when:
        contentService.addContent( getTestSession(), content, true )
        contentBrowsePanel.expandContent( content.getContentPath().getParentPath() );

        then:
        contentBrowsePanel.exists( content.getContentPath() );
    }

    def "GIVEN creating new Media beneath an existing unexpanded  WHEN conten saved THEN the content should be listed beneath parent"( )
    {
        given:
        String name = "mediacontent";
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        BaseAbstractContent content = MediaContent.builder().withName( name ).withDisplayName( "media" ).withContentPath( cpath ).build()

        when:
        contentService.addContent( getTestSession(), content, true )
        contentBrowsePanel.expandContent( content.getContentPath().getParentPath() );

        then:
        contentBrowsePanel.exists( content.getContentPath() );
    }

    def "GIVEN creating new Data beneath an existing unexpanded  WHEN conten saved THEN the content should be listed beneath parent"( )
    {
        given:
        String name = "datacontent";
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        BaseAbstractContent content = DataContent.builder().withName( name ).withDisplayName( "data" ).withContentPath( cpath ).build();

        when:
        contentService.addContent( getTestSession(), content, true )
        contentBrowsePanel.expandContent( content.getContentPath().getParentPath() );

        then:
        contentBrowsePanel.exists( content.getContentPath() );
    }


    def "GIVEN creating new Text beneath an existing unexpanded  WHEN conten saved THEN the content should be listed beneath parent"( )
    {
        given:
        String name = "textcontent";
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        BaseAbstractContent content = TextContent.builder().withName( name ).withDisplayName( "text" ).withContentPath( cpath ).build()

        when:
        contentService.addContent( getTestSession(), content, true )
        contentBrowsePanel.expandContent( content.getContentPath().getParentPath() );

        then:
        contentBrowsePanel.exists( content.getContentPath() );
    }

    def "GIVEN creating new Page beneath an existing unexpanded  WHEN conten saved THEN the content should be listed beneath parent"( )
    {
        given:
        String name = "pagecontent";
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        BaseAbstractContent content = PageContent.builder().withName( "pagecontent" ).withDisplayName( "page" ).withContentPath( cpath ).build()


        when:
        contentService.addContent( getTestSession(), content, true )
        contentBrowsePanel.expandContent( content.getContentPath().getParentPath() );

        then:
        contentBrowsePanel.exists( content.getContentPath() );
    }


    def "GIVEN creating new Shortcut beneath an existing unexpanded  WHEN conten saved THEN the content should be listed beneath parent"( )
    {
        given:
        String name = "shortcutcontent";
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        BaseAbstractContent content = ShortcutContent.builder().withName( name ).withDisplayName( "shortcut" ).withContentPath( cpath ).build()

        when:
        contentService.addContent( getTestSession(), content, true )
        contentBrowsePanel.expandContent( content.getContentPath().getParentPath() );

        then:
        contentBrowsePanel.exists( content.getContentPath() );
    }


    def "GIVEN creating new Content beneath an existing expanded WHEN conten saved THEN new Content should be listed beneath parent"( )
    {
        given:
        String name = NameHelper.unqiueName( "folder" );
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        BaseAbstractContent content = FolderContent.builder().withName( name ).withDisplayName( "folder" ).withContentPath( cpath ).build()

        when:
        contentBrowsePanel.expandContent( content.getContentPath().getParentPath() )
        contentService.addContent( getTestSession(), content, true )
        contentBrowsePanel.expandContent( content.getContentPath().getParentPath() )

        then:
        contentBrowsePanel.exists( content.getContentPath() )
    }

    def "GIVEN  existing content  WHEN content editet, name changed  THEN the content with new name should be listed beneath parent"( )
    {
        String displayName = "editnametest"
        given:
        String name = "editname"
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        StructuredContent contentToEdit = StructuredContent.builder().withName( name ).withDisplayName( displayName ).withContentPath( cpath ).build();
        contentService.addContent( getTestSession(), contentToEdit, true )

        when:
        StructuredContent newcontent = cloneContentWithNewName( contentToEdit );
        contentService.doOpenContentAndEdit( getTestSession(), contentToEdit, newcontent )
        contentBrowsePanel.expandContent( newcontent.getContentPath().getParentPath() );

        then:
        contentBrowsePanel.exists( newcontent.getContentPath() );

    }

    def "GIVEN existing content  WHEN content editet, display name changed  THEN the content with new display-name should be listed beneath parent"( )
    {
        given:
        String name = "editdisplayname"
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        StructuredContent contentToEdit = StructuredContent.builder().withName( name ).withDisplayName( name ).withContentPath( cpath ).build()
        contentService.addContent( getTestSession(), contentToEdit, true )

        when:
        StructuredContent newcontent = cloneContentWithNewDispalyName( contentToEdit )
        contentService.doOpenContentAndEdit( getTestSession(), contentToEdit, newcontent )
        contentBrowsePanel.expandContent( newcontent.getContentPath().getParentPath() );

        then:
        contentBrowsePanel.exists( newcontent.getContentPath() );
    }


    StructuredContent cloneContentWithNewDispalyName( StructuredContent contentToedit )
    {
        String newDisplayName = NameHelper.unqiueName( "displaynamechanged" )
        StructuredContent newcontent = StructuredContent.builder().withName( contentToedit.getName() ).withDisplayName( newDisplayName ).withContentPath( contentToedit.getContentPath() ).build();
        return newcontent;
    }

    StructuredContent cloneContentWithNewName( StructuredContent contentToedit )
    {
        String newName = NameHelper.unqiueName( "newname" )
        ContentPath newContentPath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, newName );
        StructuredContent newcontent = StructuredContent.builder().withName( newName ).withDisplayName( contentToedit.getDisplayName() ).withContentPath( newContentPath ).build();

    }
}
