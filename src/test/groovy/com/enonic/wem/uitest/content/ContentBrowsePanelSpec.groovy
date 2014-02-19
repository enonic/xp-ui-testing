package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.*
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
    String FULL_REPONAME = "/" + REPONAME;

    @Shared
    String[] CONTENT_PATH = [FULL_REPONAME];

    ContentBrowsePanel contentBrowsePanel;

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "GIVEN new Content on root WHEN saved THEN the Content should be listed"()
    {
        given:
        BaseAbstractContent content = FolderContent.builder().withName( REPONAME ).withDisplayName( REPONAME ).build();

        when:
        contentBrowsePanel.doAddContent( content, true );

        then:
        contentBrowsePanel.exists( content.getContentPath2() );
    }

    def "GIVEN content BrowsePanel and existing content WHEN content deleted THEN the content should not be listed in the table"()
    {
        given:
        BaseAbstractContent content = addContentToBeDeleted();
        List<BaseAbstractContent> contents = new ArrayList<>();
        contents.add( content );
        contentBrowsePanel.doClearSelection();


        when:
        contentBrowsePanel.expandAndSelectContent( content.getContentPath2() );
        contentBrowsePanel.deleteSelected();

        then:
        !contentBrowsePanel.exists( content.getContentPath2() );
    }

    def "GIVEN BrowsePanel WHEN adding Folder-content THEN the content should be listed in the table"()
    {
        given:
        BaseAbstractContent content = FolderContent.builder().withName( "foldercontent" ).withDisplayName( "folder" ).build()
        content.setContentPath( CONTENT_PATH )

        when:
        contentService.addContent( getTestSession(), content, true )

        then:
        ContentBrowsePanel grid = new ContentBrowsePanel( getTestSession() )
        grid.findContentInTable( content, 2l )
    }

    def "GIVEN BrowsePanel WHEN adding Structured-content THEN the content should be listed in the table"()
    {
        given:
        BaseAbstractContent content = StructuredContent.builder().withName( "structuredcontent" ).withDisplayName( "structured" ).build();
        content.setContentPath( CONTENT_PATH )

        when:
        contentService.addContent( getTestSession(), content, true )

        then:
        ContentBrowsePanel grid = new ContentBrowsePanel( getTestSession() )
        grid.findContentInTable( content, 2l )
    }

    def "GIVEN BrowsePanel WHEN adding Unstructured-content THEN the content should be listed in the table"()
    {
        given:
        BaseAbstractContent content = UnstructuredContent.builder().withName( "unstructuredcontent" ).withDisplayName(
            "unstructured" ).build();
        content.setContentPath( CONTENT_PATH )

        when:
        contentService.addContent( getTestSession(), content, true )

        then:
        ContentBrowsePanel grid = new ContentBrowsePanel( getTestSession() )
        grid.findContentInTable( content, 2l )
    }

    def "GIVEN BrowsePanel WHEN adding Archive-content THEN the content should be listed in the table"()
    {
        given:
        BaseAbstractContent content = ArchiveContent.builder().withName( "archivecontent" ).withDisplayName( "archive" ).build();
        content.setContentPath( CONTENT_PATH )

        when:
        contentService.addContent( getTestSession(), content, true )

        then:
        ContentBrowsePanel grid = new ContentBrowsePanel( getTestSession() )
        grid.findContentInTable( content, 2l )
    }

    def "GIVEN BrowsePanel WHEN adding Media-content THEN the content should be listed in the table"()
    {
        given:
        BaseAbstractContent content = MediaContent.builder().withName( "mediacontent" ).withDisplayName( "media" ).build();
        content.setContentPath( CONTENT_PATH )

        when:
        contentService.addContent( getTestSession(), content, true )

        then:
        ContentBrowsePanel grid = new ContentBrowsePanel( getTestSession() )
        grid.findContentInTable( content, 2l )
    }

    def "GIVEN BrowsePanel WHEN adding Data-content THEN the content should be listed in the table"()
    {
        given:
        BaseAbstractContent content = DataContent.builder().withName( "datacontent" ).withDisplayName( "data" ).build();
        content.setContentPath( CONTENT_PATH )

        when:
        contentService.addContent( getTestSession(), content, true )

        then:
        ContentBrowsePanel grid = new ContentBrowsePanel( getTestSession() )
        grid.findContentInTable( content, 2l )
    }


    def "GIVEN BrowsePanel WHEN adding Text-content THEN the content should be listed in the table"()
    {
        given:
        BaseAbstractContent content = TextContent.builder().withName( "textcontent" ).withDisplayName( "text" ).build();
        content.setContentPath( CONTENT_PATH )

        when:
        contentService.addContent( getTestSession(), content, true )

        then:
        ContentBrowsePanel grid = new ContentBrowsePanel( getTestSession() )
        grid.findContentInTable( content, 2l )
    }

    def "GIVEN BrowsePanel WHEN adding Page-content THEN the content should be listed in the table"()
    {
        given:
        BaseAbstractContent content = PageContent.builder().withName( "pagecontent" ).withDisplayName( "page" ).build()
        content.setContentPath( CONTENT_PATH )

        when:
        contentService.addContent( getTestSession(), content, true )

        then:
        ContentBrowsePanel grid = new ContentBrowsePanel( getTestSession() )
        grid.findContentInTable( content, 2l )
    }


    def "GIVEN BrowsePanel WHEN adding Shortcut-content THEN the content should be listed in the table"()
    {
        given:
        BaseAbstractContent content = ShortcutContent.builder().withName( "shortcutcontent" ).withDisplayName( "shortcut" ).build()
        content.setContentPath( CONTENT_PATH )

        when:
        contentService.addContent( getTestSession(), content, true )

        then:
        ContentBrowsePanel grid = new ContentBrowsePanel( getTestSession() )
        grid.findContentInTable( content, 2l )
    }

    def "GIVEN BrowsePanel and existing content  WHEN content editet, name changed  THEN the content with new name should be listed in the table"()
    {
        String displayName = "editnametest"
        given:
        String name = "editname"
        StructuredContent contentToEdit = StructuredContent.builder().withName( name ).withDisplayName( displayName ).withContentPath(
            CONTENT_PATH ).build();
        contentService.addContent( getTestSession(), contentToEdit, true )

        when:
        StructuredContent newcontent = cloneContentWithNewName( contentToEdit );
        contentService.doOpenContentAndEdit( getTestSession(), contentToEdit, newcontent )

        then:
        ContentBrowsePanel grid = new ContentBrowsePanel( getTestSession() )
        grid.findContentInTable( newcontent, 2l )


    }

    def "GIVEN BrowsePanel and existing content  WHEN content editet, display name changed  THEN the content with new display-name should be listed in the table"()
    {
        given:
        String name = "editdisplayname"
        StructuredContent contentToEdit = StructuredContent.builder().withName( name ).withDisplayName( name ).build()
        contentToEdit.setContentPath( CONTENT_PATH );
        contentService.addContent( getTestSession(), contentToEdit, true )

        when:
        StructuredContent newcontent = cloneContentWithNewDispalyName( contentToEdit )
        contentService.doOpenContentAndEdit( getTestSession(), contentToEdit, newcontent )

        then:
        ContentBrowsePanel grid = new ContentBrowsePanel( getTestSession() )
        grid.findContentInTable( newcontent, 2l )
    }


    StructuredContent cloneContentWithNewDispalyName( StructuredContent contentToedit )
    {
        String newDisplayName = NameHelper.unqiueName( "displaynamechanged" )
        StructuredContent newcontent = StructuredContent.builder().withName( contentToedit.getName() ).withDisplayName(
            newDisplayName ).withContentPath( CONTENT_PATH ).build();
        return newcontent;
    }

    StructuredContent cloneContentWithNewName( StructuredContent contentToedit )
    {
        String newName = NameHelper.unqiueName( "newname" )
        StructuredContent newcontent = StructuredContent.builder().withName( newName ).withDisplayName(
            contentToedit.getDisplayName() ).withContentPath( CONTENT_PATH ).build();

    }
}
