package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.ArchiveContent
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
import com.enonic.autotests.vo.contentmanager.FolderContent
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_DeleteSpec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel

    @Shared
    String DELETE_CONTENT_KEY = "deletecontent_test"

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "GIVEN existing content, WHEN content opened and delete button pressed THEN the content should not be listed in the table"()
    {
        given:
        BaseAbstractContent content = FolderContent.builder().
            withName( NameHelper.uniqueName( "deletecontent" ) ).
            withDisplayName( "contenttodelete" ).
            withParent( ContentPath.ROOT ).build();
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData( content ).save().close()

        ItemViewPanelPage contentInfoPage = contentBrowsePanel.selectRowByContentPath( content.getPath().toString() ).clickToolbarOpen()
        contentInfoPage.waitUntilOpened( content.getDisplayName(), 1 )

        when:
        contentInfoPage.openDeleteConfirmationDialog().doConfirm()

        then:
        !contentBrowsePanel.exists( content.getPath() )
    }

    def "GIVEN existing two contents, WHEN all content selected and delete button pressed THEN the content should not be listed in the table"()
    {
        given:
        BaseAbstractContent content1 = FolderContent.builder().
	    withName( NameHelper.uniqueName( "deletecontent" ) ).
            withDisplayName( "contenttodelete" ).
            withParent( ContentPath.ROOT ).build();
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData( content1 ).save().close()


        BaseAbstractContent content2 = FolderContent.builder().
	    withName( NameHelper.uniqueName( "deletecontent" ) ).
            withDisplayName( "contenttodelete" ).
            withParent( ContentPath.ROOT ).build();
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData( content2 ).save().close()
        List<BaseAbstractContent> contentList = new ArrayList<>()
        contentList.add( content1 )
        contentList.add( content2 )

        when:
        contentBrowsePanel.doClearSelection()
        contentBrowsePanel.expandContent( content1.getParent() ).selectContentInTable( contentList ).clickToolbarDelete().doDelete()


        then:
        !contentBrowsePanel.exists( content1.getPath() ) && !contentBrowsePanel.exists( content2.getPath() )
    }

    def "GIVEN a Content on root WHEN deleted THEN deleted content is no longer listed at root"()
    {
        given:
        BaseAbstractContent content = FolderContent.builder().
			withName(  NameHelper.uniqueName( "deletecontent" ) ).
            withDisplayName( "contenttodelete" ).
            withParent( ContentPath.ROOT ).build();

        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData( content ).save().close()
        List<BaseAbstractContent> contents = new ArrayList<>()
        contents.add( content )
        ContentBrowsePanel browsePanel = new ContentBrowsePanel( getTestSession() )
        browsePanel.doClearSelection()

        when:
        contentBrowsePanel.selectRowByCheckbox( content.getPath() )
        contentBrowsePanel.deleteSelected();

        then:
        !contentBrowsePanel.exists( content.getPath() )
    }

    def "GIVEN a Content beneath an existing WHEN deleted THEN deleted Content is no longer listed beneath parent"()
    {
        given:
        BaseAbstractContent parent = FolderContent.builder().
            withParent( ContentPath.ROOT ).
            withName( NameHelper.uniqueName( "parent" ) ).
            withDisplayName( "parent" ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( parent.getContentTypeName() ).typeData( parent ).save().close()

        contentBrowsePanel.clickByParentCheckbox( parent.getPath() )
        BaseAbstractContent contentToDelete = ArchiveContent.builder().
            withName( NameHelper.uniqueName( "archive" ) ).
            withDisplayName( "delete content beneath parent" ).
            withParent( ContentPath.from( parent.getName() ) ).build();
        contentBrowsePanel.clickToolbarNew().selectContentType( contentToDelete.getContentTypeName() ).typeData(
            contentToDelete ).save().close();


        List<BaseAbstractContent> contentList = new ArrayList<>()
        contentList.add( contentToDelete )

        when:
        contentBrowsePanel.expandContent( contentToDelete.getParent() ).selectContentInTable( contentList ).clickToolbarDelete().doDelete()

        then:
        !contentBrowsePanel.exists( contentToDelete.getPath() )
    }


    def "GIVEN a one and only Content beneath an existing WHEN deleted THEN expand icon of parent is no longer shown "()
    {
        given:
        BaseAbstractContent parent = FolderContent.builder().
            withParent( ContentPath.ROOT ).
            withName( NameHelper.uniqueName( "parent" ) ).
            withDisplayName( "expandicon-test" ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( parent.getContentTypeName() ).typeData( parent ).save().close()

        contentBrowsePanel.clickByParentCheckbox( parent.getPath() )
        BaseAbstractContent content = ArchiveContent.builder().
            withName( NameHelper.uniqueName( "archive" ) ).
            withDisplayName( "archive" ).
            withParent( ContentPath.from( parent.getName() ) ).build();
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close()

        List<BaseAbstractContent> contentList = new ArrayList<>()
        contentList.add( content )

        when:
        contentBrowsePanel.expandContent( content.getParent() ).selectContentInTable( contentList ).clickToolbarDelete().doDelete()

        then:
        !contentBrowsePanel.isExpanderPresent( parent.getName() )
    }
}
