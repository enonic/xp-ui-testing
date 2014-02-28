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
        contentBrowsePanel.exists( content.getParent() );
    }


    def "GIVEN creating new Content beneath an existing unexpanded AND saved WHEN wizard closed THEN new Content should be listed beneath parent"()
    {
        given:
        BaseAbstractContent content = FolderContent.builder().
            withParent( ContentPath.from( REPONAME ) ).
            withName( "foldercontent" ).
            withDisplayName( "folder" ).
            build();

        when:
        contentService.addContent( getTestSession(), content, true )
        contentBrowsePanel.expandContent( content.getParent().getParentPath() )

        then:
        contentBrowsePanel.exists( content.getParent() )
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
        contentService.addContent( getTestSession(), content, true )
        contentBrowsePanel.expandContent( content.getParent().getParentPath() )

        then:
        contentBrowsePanel.exists( content.getParent() )
    }

    def "GIVEN changing name of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new name"()
    {
        given:
        StructuredContent contentToEdit = StructuredContent.builder().
            withParent( ContentPath.from( REPONAME ) ).
            withName( "editname" ).
            withDisplayName( "editnametest" ).
            build();
        contentService.addContent( getTestSession(), contentToEdit, true )

        when:
        StructuredContent newcontent = cloneContentWithNewName( contentToEdit );
        contentService.doOpenContentAndEdit( getTestSession(), contentToEdit, newcontent )
        contentBrowsePanel.expandContent( newcontent.getParent().getParentPath() );

        then:
        contentBrowsePanel.exists( newcontent.getParent() );

    }

    def "GIVEN changing displayName of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new displayName"()
    {
        given:
        StructuredContent contentToEdit = StructuredContent.builder().
            withParent( ContentPath.from( REPONAME ) ).
            withName( "editdisplayname" ).
            withDisplayName( "editdisplayname" ).
            build()
        contentService.addContent( getTestSession(), contentToEdit, true )

        when:
        StructuredContent newcontent = cloneContentWithNewDispalyName( contentToEdit )
        contentService.doOpenContentAndEdit( getTestSession(), contentToEdit, newcontent )
        contentBrowsePanel.expandContent( newcontent.getParent().getParentPath() );

        then:
        contentBrowsePanel.exists( newcontent.getParent() );
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
