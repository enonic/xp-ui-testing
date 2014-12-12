package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
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


    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "GIVEN existing content, WHEN content opened and delete button pressed THEN the content should not be listed in the table"()
    {
        given:
        Content content = Content.builder().
            name( NameHelper.uniqueName( "deletecontent" ) ).
            displayName( "contenttodelete" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData( content ).save().close();

        when:
        ItemViewPanelPage contentInfoPage = contentBrowsePanel.selectRowByContentPath( content.getPath().toString() ).clickToolbarOpen()
        contentInfoPage.waitUntilOpened( content.getDisplayName() );
        contentInfoPage.openDeleteConfirmationDialog().doConfirm();

        then:
        !contentBrowsePanel.exists( content.getPath(), true );
    }

    def "GIVEN existing two contents, WHEN all content selected and delete button pressed THEN the content should not be listed in the table"()
    {
        given:
        Content content1 = Content.builder().
            name( NameHelper.uniqueName( "deletecontent" ) ).
            displayName( "contenttodelete" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.folder() ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content1.getContentTypeName() ).typeData( content1 ).save().close();

        Content content2 = Content.builder().
            name( NameHelper.uniqueName( "deletecontent" ) ).
            displayName( "contenttodelete" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content2.getContentTypeName() ).waitUntilWizardOpened().typeData(
            content2 ).save().close();

        List<Content> contentList = new ArrayList<>();
        contentList.add( content1 );
        contentList.add( content2 );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        contentBrowsePanel.waitUntilPageLoaded( 2 );
        contentBrowsePanel.clickOnClearSelection();

        when:
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();


        then:
        !contentBrowsePanel.exists( content1.getPath() ) && !contentBrowsePanel.exists( content2.getPath() );
    }

    def "GIVEN a Content on root WHEN deleted THEN deleted content is no longer listed at root"()
    {
        given: "folder content added at the root"
        Content content = Content.builder().
            name( NameHelper.uniqueName( "deletecontent" ) ).
            displayName( "contenttodelete" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();
        List<Content> contents = new ArrayList<>();
        contents.add( content );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        contentBrowsePanel.clickOnClearSelection();

        when: "just created content selected and 'Delete' button on toolbar  pressed and 'Yes' pressed on confirm dialog "
        contentBrowsePanel.clickCheckboxAndSelectRow( content.getPath() );
        contentBrowsePanel.deleteSelected();

        then: "deleted content is no longer listed at root"
        !contentBrowsePanel.exists( content.getPath() );
    }

    def "GIVEN a Content beneath an existing WHEN deleted THEN deleted Content is no longer listed beneath parent"()
    {
        given: "folder content added at the root and added child archive to this folder"
        Content parent = Content.builder().
            parent( ContentPath.ROOT ).
            name( NameHelper.uniqueName( "parent" ) ).
            displayName( "parent" ).
            contentType( ContentTypeName.folder() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( parent.getContentTypeName() ).typeData( parent ).save().close();
        contentBrowsePanel.clickOnParentCheckbox( parent.getPath() )
        Content contentToDelete = Content.builder().
            name( NameHelper.uniqueName( "archive" ) ).
            displayName( "delete content beneath parent" ).
            parent( ContentPath.from( parent.getName() ) ).
            contentType( ContentTypeName.archiveMedia() ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( contentToDelete.getContentTypeName() ).typeData( contentToDelete ).
            save().close();
        contentBrowsePanel.waitUntilPageLoaded( 1 );

        List<Content> contentList = new ArrayList<>()
        contentList.add( contentToDelete );

        when: "parent folder expanded and child archive selected and 'Delete' button on toolbar pressed"
        contentBrowsePanel.expandContent( contentToDelete.getParent() ).selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then: "deleted Content is no longer listed beneath parent"
        !contentBrowsePanel.exists( contentToDelete.getPath(), true );
    }


    def "GIVEN a one and only Content beneath an existing WHEN deleted THEN expand icon of parent is no longer shown "()
    {
        given: "folder content added at the root and added child archive to this folder"
        Content parent = Content.builder().
            parent( ContentPath.ROOT ).
            name( NameHelper.uniqueName( "parent" ) ).
            displayName( "expandicon-test" ).
            contentType( ContentTypeName.folder() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( parent.getContentTypeName() ).typeData( parent ).save().close();

        contentBrowsePanel.clickOnParentCheckbox( parent.getPath() )
        Content content = Content.builder().
            name( NameHelper.uniqueName( "archive" ) ).
            displayName( "archive" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.from( parent.getName() ) ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();

        List<Content> contentList = new ArrayList<>();
        contentList.add( content );

        when: "child content deleted"
        contentBrowsePanel.expandContent( content.getParent() )
        TestUtils.saveScreenshot( getTestSession(), "deletecontentbeneath" )
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then: "expand icon of parent is no longer shown"
        !contentBrowsePanel.isExpanderPresent( ContentPath.from( parent.getName() ) );
    }

    def "GIVEN a Content on root WHEN deleted THEN New-button is enabled"()
    {
        given: "folder content added at the root"
        Content content = Content.builder().
            name( NameHelper.uniqueName( "folder" ) ).
            displayName( "folderToDelete" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();

        when: "just created content deleted"
        contentBrowsePanel.selectContentInTable( content.getPath() ).clickToolbarDelete().doDelete();

        then: "New-button is enabled"
        contentBrowsePanel.isNewButtonEnabled();
    }

    def "GIVEN two Content on root WHEN both deleted THEN New-button is enabled"()
    {
        given: "two folder added at the root"
        Content content1 = Content.builder().
            name( NameHelper.uniqueName( "folder" ) ).
            displayName( "folderToDelete1" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();

        Content content2 = Content.builder().
            name( NameHelper.uniqueName( "folder" ) ).
            displayName( "folderToDelete2" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();
        List<Content> contentList = new ArrayList<>();
        contentList.add( content2 );
        contentList.add( content1 );
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData( content1 ).save().close();
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData( content2 ).save().close();

        when: "both contents selected in the grid and  deleted"
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then: "New-button is enabled"
        contentBrowsePanel.isNewButtonEnabled();
    }
}
