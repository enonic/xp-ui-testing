package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.ContentPathHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
import com.enonic.autotests.vo.contentmanager.FolderContent
import com.enonic.autotests.vo.contentmanager.StructuredContent
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

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }


    def "GIVEN creating new Content on root WHEN saved THEN new Content should be listed"()
    {
        given:
        ContentPath cpath = ContentPathHelper.buildContentPath( null, REPONAME )
        BaseAbstractContent content = FolderContent.builder().withName( REPONAME ).withDisplayName( REPONAME ).withContentPath(
            cpath ).build();

        when:
        contentBrowsePanel.doAddContent( content, true );

        then:
        contentBrowsePanel.exists( content.getContentPath() );
    }


    def "GIVEN creating new Content beneath an existing unexpanded WHEN saved THEN new Content should be listed beneath parent"()
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

    def "GIVEN creating new Content beneath an existing expanded WHEN saved THEN new Content should be listed beneath parent"()
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

    def "GIVEN a Content on root WHEN deleted THEN deleted content is no longer listed at root"()
    {
        given:
        BaseAbstractContent content = addRootContentToBeDeleted();
        List<BaseAbstractContent> contents = new ArrayList<>();
        contents.add( content );
        ContentBrowsePanel browsePanel = new ContentBrowsePanel( getTestSession() )
        browsePanel.doClearSelection();

        when:
        browsePanel.selectContent( content.getContentPath() )
        browsePanel.deleteSelected();

        then:
        !browsePanel.exists( content.getContentPath() );
    }

    def "GIVEN changing name of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new name"()
    {
        String displayName = "editnametest"
        given:
        String name = "editname"
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        StructuredContent contentToEdit = StructuredContent.builder().withName( name ).withDisplayName( displayName ).withContentPath(
            cpath ).build();
        contentService.addContent( getTestSession(), contentToEdit, true )

        when:
        StructuredContent newcontent = cloneContentWithNewName( contentToEdit );
        contentService.doOpenContentAndEdit( getTestSession(), contentToEdit, newcontent )
        contentBrowsePanel.expandContent( newcontent.getContentPath().getParentPath() );

        then:
        contentBrowsePanel.exists( newcontent.getContentPath() );

    }

    def "GIVEN changing displayName of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new displayName"()
    {
        given:
        String name = "editdisplayname"
        ContentPath cpath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, name )
        StructuredContent contentToEdit = StructuredContent.builder().withName( name ).withDisplayName( name ).withContentPath(
            cpath ).build()
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
        StructuredContent newcontent = StructuredContent.builder().withName( contentToedit.getName() ).withDisplayName(
            newDisplayName ).withContentPath( contentToedit.getContentPath() ).build();
        return newcontent;
    }

    StructuredContent cloneContentWithNewName( StructuredContent contentToedit )
    {
        String newName = NameHelper.unqiueName( "newname" )
        ContentPath newContentPath = ContentPathHelper.buildContentPath( PARENT_FOLDER_ARR, newName );
        StructuredContent newcontent = StructuredContent.builder().withName( newName ).withDisplayName(
            contentToedit.getDisplayName() ).withContentPath( newContentPath ).build();

    }
}
