package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ConfirmValueDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_DeleteSpec
    extends BaseContentSpec
{

    def "GIVEN existing two folders WHEN both folders are selected and delete button pressed THEN both contents should not be listed in the grid"()
    {
        given: "existing two folders"
        Content content1 = buildFolderContent( "deletecontent", "content to delete" );
        addContent( content1 );
        Content content2 = buildFolderContent( "deletecontent", "content to delete" );
        addContent( content2 );
        List<String> contentList = new ArrayList<>();
        sleep( 1000 );

        when: "both content are selected and delete button pressed AND deleting is confirmed"
        findAndSelectContent( content1.getName() );
        findAndSelectContent( content2.getName() );
        DeleteContentDialog deleteContentDialog = contentBrowsePanel.clickToolbarDelete();
        deleteContentDialog.clickOnDeleteNowButton();
        and: "correct number of contents to delete is typed"
        ConfirmValueDialog confirmContentDeleteDialog = new ConfirmValueDialog( getSession() );
        confirmContentDeleteDialog.typeNumber( "2" ).clickOnConfirmButton();

        then: "both folders should not be listed in the grid"
        !contentBrowsePanel.exists( content1.getName() ) && !contentBrowsePanel.exists( content2.getName() );
    }

    def "GIVEN existing folder in the root WHEN the content has been deleted THEN the content is no longer listed in the root"()
    {
        given: "folder content was added on the root"
        Content content = buildFolderContent( "deletecontent", "delete content" );
        addContent( content );

        when: "the folder was selected and 'Delete' button on toolbar  pressed and 'Yes' pressed on confirm dialog "
        findAndSelectContent( content.getName() ).clickToolbarDelete().doDelete();

        then: "deleted folder is no longer listed in the root"
        !contentBrowsePanel.exists( content.getName() );
    }

    def "GIVEN parent folder and child folder are added WHEN parent folder has been expanded and child content deleted THEN child folder should not be displayed"()
    {
        given: "folder content added at the root and added child content to this folder"
        Content parent = buildFolderContent( "parent", "parent" );
        addContent( parent );

        filterPanel.typeSearchText( parent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parent.getName() );
        Content childFolder = buildFolderContent( "folder", "child content to delete" );
        addContent( childFolder );
        List<String> contentList = new ArrayList<>()
        contentList.add( childFolder.getName() );
        and: "parent folder should be unchecked"
        contentBrowsePanel.clickOnRowCheckbox( parent.getName() );
        sleep( 1000 );

        when: "parent folder expanded and child content selected and 'Delete' button on toolbar pressed"
        DeleteContentDialog deleteContentDialog = contentBrowsePanel.expandContent( parent.getPath() ).selectContentInTable(
            contentList ).clickToolbarDelete();
        deleteContentDialog.clickOnDeleteNowButton();

        then: "child folder should not be displayed"
        !contentBrowsePanel.exists( childFolder.getName(), true );
        and: "expand-icon is no longer present for the parent folder"
        !contentBrowsePanel.isExpanderPresent( ContentPath.from( parent.getName() ) );
    }

    def "GIVEN parent folder and child folder are added WHEN parent folder and child folders have been selected and deleted THEN both folders should not be displayed"()
    {
        given: "folder content added at the root and added child content to this folder"
        Content parent = buildFolderContent( "parent", "parent" );
        addContent( parent );

        filterPanel.typeSearchText( parent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parent.getName() );
        Content childFolder = buildFolderContent( "folder", "child content to delete" );
        addContent( childFolder );
        List<String> contentList = new ArrayList<>()
        contentList.add( childFolder.getName() );
        and: "parent folder should be unchecked"
        sleep( 1000 );

        when: "parent folder expanded and child content selected and 'Delete' button on toolbar pressed"
        DeleteContentDialog deleteContentDialog = contentBrowsePanel.expandContent( parent.getPath() ).selectContentInTable(
            contentList ).clickToolbarDelete();
        deleteContentDialog.clickOnDeleteNowButton();
        and: "2 has been typed and deleting confirmed"
        ConfirmValueDialog confirmContentDeleteDialog = new ConfirmValueDialog( getSession() );
        confirmContentDeleteDialog.typeNumber( "2" ).clickOnConfirmButton();

        then: "child folder should not be displayed"
        !contentBrowsePanel.exists( childFolder.getName(), true );
        and: "parent folder should not be displayed"
        !contentBrowsePanel.exists( parent.getName() );
    }


    def "GIVEN existing folder WHEN the folder has been deleted THEN New-button should be enabled"()
    {
        given: "folder content was added in the root"
        Content folder = buildFolderContent( "folder", "folder-to-delete" );
        addContent( folder );

        when: "just created content was deleted"
        findAndSelectContent( folder.getName() ).clickToolbarDelete().doDelete();
        sleep( 1000 );

        then: "New-button should be enabled"
        contentBrowsePanel.isNewButtonEnabled();

        and:
        String mess = String.format( Application.ITEM_IS_DELETED, folder.getName() );
        contentBrowsePanel.waitExpectedNotificationMessage( mess, 1 )
    }


    def "GIVEN existing content is opened WHEN content has been moved to another location AND 'delete' button in the wizard-toolbar pressed THEN content deleted AND wizard closed"()
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
