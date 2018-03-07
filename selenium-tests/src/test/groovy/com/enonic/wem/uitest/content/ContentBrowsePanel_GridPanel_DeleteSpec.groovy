package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ConfirmContentDeleteDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_DeleteSpec
    extends BaseContentSpec
{

    def "GIVEN existing two folders WHEN both folders are selected and delete button pressed THEN both contents should not be listed in the table"()
    {
        given: "existing two folders"
        Content content1 = buildFolderContent( "deletecontent", "content to delete" );
        addContent( content1 );
        Content content2 = buildFolderContent( "deletecontent", "content to delete" );
        addContent( content2 );
        List<String> contentList = new ArrayList<>();
        contentList.add( content1.getName() );
        contentList.add( content2.getName() );

        when: "both content are selected and delete button pressed AND deleting is confirmed"
        DeleteContentDialog deleteContentDialog = contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete();
        deleteContentDialog.clickOnDeleteButton();
        and: "correct number of contents to delete is typed"
        ConfirmContentDeleteDialog confirmContentDeleteDialog = new ConfirmContentDeleteDialog( getSession() );
        confirmContentDeleteDialog.typeNumber( "2" ).clickOnConfirmButton();

        then: "both folders should not be listed in the grid"
        !contentBrowsePanel.exists( content1.getName() ) && !contentBrowsePanel.exists( content2.getName() );
    }

    def "GIVEN existing content at the root WHEN the content is deleted THEN the content is no longer listed at the root"()
    {
        given: "folder content was added on the root"
        Content content = buildFolderContent( "deletecontent", "delete content" );
        addContent( content );

        when: "the folder was selected and 'Delete' button on toolbar  pressed and 'Yes' pressed on confirm dialog "
        findAndSelectContent( content.getName() ).clickToolbarDelete().doDelete();

        then: "deleted folder is no longer listed in the root"
        !contentBrowsePanel.exists( content.getName() );
    }

    def "GIVEN parent folder with a child were added WHEN parent folder is expanded and child content has been deleted THEN child is no longer listed beneath parent"()
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
        DeleteContentDialog deleteContentDialog = contentBrowsePanel.expandContent( parent.getPath() ).selectContentInTable(
            contentList ).clickToolbarDelete();
        deleteContentDialog.clickOnDeleteButton();
        and: "correct number of contents to delete is typed"
        ConfirmContentDeleteDialog confirmContentDeleteDialog = new ConfirmContentDeleteDialog( getSession() );
        confirmContentDeleteDialog.typeNumber( "2" ).clickOnConfirmButton();

        then: "child Content is no longer listed beneath the parent"
        !contentBrowsePanel.exists( contentToDelete.getName(), true );
        and: "expand-icon is no longer shown for the parent folder"
        !contentBrowsePanel.isExpanderPresent( ContentPath.from( parent.getName() ) );
    }

    def "GIVEN existing folder WHEN the folder is selected and deleted THEN New-button should be enabled"()
    {
        given: "folder content was added in the root"
        Content folder = buildFolderContent( "folder", "folder-to-delete" );
        addContent( folder );

        when: "just created content was deleted"
        contentBrowsePanel.selectContentInTable( folder.getName() ).clickToolbarDelete().doDelete();

        then: "New-button should be enabled"
        contentBrowsePanel.isNewButtonEnabled();

        and:
        contentBrowsePanel.waitForNotificationMessage() == Application.CONTENT_DELETED_MESSAGE;
    }

    def "GIVEN two existing folders in the root WHEN both folders were deleted THEN New-button should be enabled"()
    {
        given: "two folder were added at the root"
        Content content1 = buildFolderContent( "folder", "folder-to-delete1" );
        addContent( content1 );

        Content content2 = buildFolderContent( "folder", "folder-to-delete2" );
        addContent( content2 );

        List<String> contentList = new ArrayList<>();
        contentList.add( content2.getName() );
        contentList.add( content1.getName() );

        when: "both contents selected in the grid and  deleted"
        DeleteContentDialog deleteContentDialog = contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete();
        deleteContentDialog.clickOnDeleteButton();
        ConfirmContentDeleteDialog confirmContentDeleteDialog = new ConfirmContentDeleteDialog( getSession() );
        confirmContentDeleteDialog.typeNumber( "2" ).clickOnConfirmButton();

        then: "New-button should be enabled"
        contentBrowsePanel.isNewButtonEnabled();
    }

    def "GIVEN existing content is opened WHEN content has been moved to another location AND 'delete' button on the wizard-toolbar pressed THEN content deleted AND wizard closed"()
    {
        given: "existing content is opened"
        Content parent = buildFolderContent( "folder", "destination folder" );
        Content contentToDelete = buildFolderContent( "folder", "move and delete" );
        addContent( parent );
        addContent( contentToDelete );
        ContentWizardPanel wizard = findAndSelectContent( contentToDelete.getName() ).clickToolbarEditAndSwitchToWizardTab();
        wizard.switchToBrowsePanelTab();

        when: "content has been moved to the 'parent' folder"
        contentBrowsePanel.clickToolbarMove().typeSearchText( parent.getName() ).selectDestinationAndClickOnMove( parent.getName() );
        sleep( 1000 );

        and: "content was deleted from the wizard"
        contentBrowsePanel.switchToBrowserTabByTitle( contentToDelete.getDisplayName() );
        wizard.clickToolbarDelete().doDeleteAndSwitchToBrowsePanel();

        then: "content should not be listed in the grid"
        saveScreenshot( "test_content_moved_and_deleted" );
        !contentBrowsePanel.exists( contentToDelete.getName() );
    }
}
