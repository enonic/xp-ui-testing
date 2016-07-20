package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_DeleteSpec
    extends BaseContentSpec
{

    def "GIVEN existing two contents, WHEN all content selected and delete button pressed THEN the content should not be listed in the table"()
    {
        given:
        Content content1 = buildFolderContent( "deletecontent", "contenttodelete" );
        addContent( content1 );

        Content content2 = buildFolderContent( "deletecontent", "contenttodelete" );
        addContent( content2 );

        List<String> contentList = new ArrayList<>();
        contentList.add( content1.getName() );
        contentList.add( content2.getName() );

        when:
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then:
        !contentBrowsePanel.exists( content1.getName() ) && !contentBrowsePanel.exists( content2.getName() );
    }

    def "GIVEN a Content on root WHEN deleted THEN deleted content is no longer listed at root"()
    {
        given: "folder content added on the root"
        Content content = buildFolderContent( "deletecontent", "deletecontent" );
        addContent( content );

        when: "just created content selected and 'Delete' button on toolbar  pressed and 'Yes' pressed on confirm dialog "
        contentBrowsePanel.clickCheckboxAndSelectRow( content.getName() ).clickToolbarDelete().doDelete();

        then: "deleted content is no longer listed at root"
        !contentBrowsePanel.exists( content.getName() );
    }

    def "GIVEN a Content beneath an existing WHEN deleted THEN deleted Content is no longer listed beneath parent"()
    {
        given: "folder content added at the root and added child content to this folder"
        Content parent = buildFolderContent( "parent", "parent" );
        addContent( parent );

        contentBrowsePanel.clickCheckboxAndSelectRow( parent.getName() );
        Content contentToDelete = buildFolderContent( "folder", "delete content beneath parent" );
        addContent( contentToDelete );
        List<String> contentList = new ArrayList<>()
        contentList.add( contentToDelete.getName() );

        when: "parent folder expanded and child content selected and 'Delete' button on toolbar pressed"
        contentBrowsePanel.expandContent( parent.getPath() ).selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then: "deleted Content is no longer listed beneath parent"
        !contentBrowsePanel.exists( contentToDelete.getName(), true );
    }

    def "GIVEN a one and only Content beneath an existing WHEN deleted THEN expand icon of parent is no longer shown "()
    {
        given: "folder content added at the root and added a child content to this folder"
        Content parent = buildFolderContent( "parent", "expand-icon-test" );
        addContent( parent );

        contentBrowsePanel.clickCheckboxAndSelectRow( parent.getName() );
        Content content = Content.builder().
            name( NameHelper.uniqueName( "unstructured" ) ).
            displayName( "unstructured" ).
            contentType( ContentTypeName.unstructured() ).
            parent( ContentPath.from( parent.getName() ) ).
            build();
        addContent( content );
        List<String> contentList = new ArrayList<>();
        contentList.add( content.getName() );

        when: "child content deleted"
        contentBrowsePanel.expandContent( parent.getPath() );
        TestUtils.saveScreenshot( getTestSession(), "deletecontentbeneath" )
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then: "expand icon of parent is no longer shown"
        !contentBrowsePanel.isExpanderPresent( ContentPath.from( parent.getName() ) );
    }

    def "GIVEN a Content on root WHEN deleted THEN New-button is enabled"()
    {
        given: "folder content added at the root"
        Content folder = buildFolderContent( "folder", "folder-to-delete" );
        addContent( folder );

        when: "just created content deleted"
        contentBrowsePanel.selectContentInTable( folder.getName() ).clickToolbarDelete().doDelete();

        then: "New-button is enabled"
        contentBrowsePanel.isNewButtonEnabled();
    }

    def "GIVEN two Content on root WHEN both deleted THEN New-button is enabled"()
    {
        given: "two folder added at the root"
        Content content1 = buildFolderContent( "folder", "folder-to-delete1" );
        addContent( content1 );

        Content content2 = buildFolderContent( "folder", "folder-to-delete2" );
        addContent( content2 );

        List<String> contentList = new ArrayList<>();
        contentList.add( content2.getName() );
        contentList.add( content1.getName() );

        when: "both contents selected in the grid and  deleted"
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then: "New-button is enabled"
        contentBrowsePanel.isNewButtonEnabled();
    }

    def "GIVEN existing content is opened WHEN content has been moved to another location AND 'delete' button on the wizard-toolbar pressed THEN content deleted AND wizard closed"()
    {
        given: "existing content is opened"
        Content parent = buildFolderContent( "folder", "destination folder" );
        Content contentToDelete = buildFolderContent( "folder", "move and delete" );
        addContent( parent );
        addContent( contentToDelete );
        ContentWizardPanel wizard = findAndSelectContent( contentToDelete.getName() ).clickToolbarEdit();
        contentBrowsePanel.pressAppHomeButton();

        when: "content has been moved"
        contentBrowsePanel.clickToolbarMove().typeSearchText( parent.getName() ).selectFolderAndClickOnMove( parent.getName() );
        sleep( 1000 );

        and: "content deleted from the wizard"
        contentBrowsePanel.clickOnTab( contentToDelete.getDisplayName() );
        wizard.clickToolbarDelete().doDelete();
        TestUtils.saveScreenshot( getSession(), "test_content_moved_and_deleted" );

        then: "wizard has been closed"
        !wizard.isOpened();

        and: "content not listed in the grid"
        !contentBrowsePanel.exists( contentToDelete.getName() );

    }
}
