package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
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
        BaseAbstractContent content = FolderContent.builder().
            withParent( ContentPath.ROOT ).
            withName( REPONAME ).
            withDisplayName( REPONAME ).
            build();

        when:
        contentBrowsePanel.doAddContent( content, true );

        then:
        contentBrowsePanel.exists( content.getPath() );
    }


    def "GIVEN creating new Content beneath an existing unexpanded WHEN saved and wizard closed THEN parent should still be unexpanded"()
    {
        given:
        BaseAbstractContent content = FolderContent.builder().
            withParent( ContentPath.from( REPONAME ) ).
            withName( "foldercontent" ).
            withDisplayName( "folder" ).
            build();

        when:
        contentBrowsePanel.doAddContent( content, true );

        then:
        !contentBrowsePanel.exists( content.getPath() )
    }

    def "GIVEN creating new Content beneath an existing expanded WHEN saved THEN new Content should be listed beneath parent"()
    {
        given:
        String name = NameHelper.unqiueName( "folder" );
        BaseAbstractContent content = FolderContent.builder().
            withName( name ).
            withDisplayName( "folder" ).
            withParent( ContentPath.from( REPONAME ) ).build()

        when:
        contentBrowsePanel.expandContent( content.getParent().getParentPath() )
        contentBrowsePanel.doAddContent( content, true );
        contentBrowsePanel.expandContent( content.getParent() )

        then:
        contentBrowsePanel.exists( content.getPath() )
    }

    def "GIVEN changing name of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new name"()
    {
        given:
        StructuredContent contentToEdit = StructuredContent.builder().
            withParent( ContentPath.from( REPONAME ) ).
            withName( "editname" ).
            withDisplayName( "editnametest" ).
            build();
        contentBrowsePanel.doAddContent( contentToEdit, true );

        when:
        StructuredContent newcontent = cloneContentWithNewName( contentToEdit );
        contentService.doOpenContentAndEdit( getTestSession(), contentToEdit, newcontent )
        contentBrowsePanel.expandContent( newcontent.getParent() );

        then:
        contentBrowsePanel.exists( newcontent.getPath() );

    }

    def "GIVEN changing displayName of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new displayName"()
    {
        given:
        StructuredContent contentToEdit = StructuredContent.builder().
            withParent( ContentPath.from( REPONAME ) ).
            withName( "editdisplayname" ).
            withDisplayName( "editdisplayname" ).
            build()
        contentBrowsePanel.doAddContent( contentToEdit, true );
        when:
        StructuredContent newcontent = cloneContentWithNewDispalyName( contentToEdit )
        contentService.doOpenContentAndEdit( getTestSession(), contentToEdit, newcontent )
        contentBrowsePanel.expandContent( newcontent.getParent() );

        then:
        contentBrowsePanel.exists( newcontent.getPath() );
    }


    StructuredContent cloneContentWithNewDispalyName( StructuredContent contentToedit )
    {
        String newDisplayName = NameHelper.unqiueName( "displaynamechanged" )
        return StructuredContent.builder().
            withName( contentToedit.getName() ).
            withDisplayName( newDisplayName ).
            withParent( contentToedit.getParent() ).
            build();
    }

    StructuredContent cloneContentWithNewName( StructuredContent source )
    {
        String newName = NameHelper.unqiueName( "newname" )
        return StructuredContent.builder().
            withName( newName ).
            withDisplayName( source.getDisplayName() ).
            withParent( source.getParent() ).
            build();

    }
}
