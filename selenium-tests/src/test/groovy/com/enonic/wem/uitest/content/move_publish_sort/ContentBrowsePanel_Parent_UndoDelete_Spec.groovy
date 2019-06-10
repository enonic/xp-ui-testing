package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ConfirmContentDeleteDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

/**
 * Created on 3/28/2017.
 *
 * Tasks:
 * enonic/xp-ui-testing#32  Add Selenium tests for Undo Delete action for parent and child contents
 * */
class ContentBrowsePanel_Parent_UndoDelete_Spec
    extends BaseContentSpec
{
    @Shared
    Content PARENT_FOLDER

    @Shared
    Content CHILD_FOLDER

    def "GIVEN existing parent folder with a child WHEN the parent is selected AND 'Delete' button has been pressed THEN confirmation dialog with input for number of contents to delete should be displayed"()
    {
        given: "existing parent folder"
        PARENT_FOLDER = buildFolderContent( "folder", "undo delete parent with child" );
        CHILD_FOLDER = buildFolderContent( "folder", "undo delete child content" );
        addContent( PARENT_FOLDER );
        findAndSelectContent( PARENT_FOLDER.getName() );
        and: "child folder has been added"
        addContent( CHILD_FOLDER );
        and: "both contents are published"
        contentBrowsePanel.clickToolbarPublish().includeChildren( true ).clickOnPublishNowButton();

        when: "Delete button has been pressed and the deleting confirmed"
        contentBrowsePanel.clickToolbarDelete().clickOnDeleteButton();
        ConfirmContentDeleteDialog confirmContentDeleteDialog = new ConfirmContentDeleteDialog( getSession() );
        confirmContentDeleteDialog.typeNumber( "2" ).clickOnConfirmButton();
        saveScreenshot( "parent_child_deleted" );

        then: "parent folder should be 'Deleted'"
        contentBrowsePanel.getContentStatus( PARENT_FOLDER.getName() ) == ContentStatus.DELETED.getValue();

        and: "child folder should be 'Deleted'"
        findAndSelectContent( CHILD_FOLDER.getName() ).getContentStatus( CHILD_FOLDER.getName() ) == ContentStatus.DELETED.getValue();
    }

    def "GIVEN existing 'deleted' folder WHEN the content has been selected THEN only two toolbar's items should be displayed"()
    {
        when: "existing 'deleted' folder is selected"
        findAndSelectContent( PARENT_FOLDER.getName() );
        saveScreenshot( "deleted_content_selected" )

        then: "Delete button should not be displayed"
        !contentBrowsePanel.isDeleteButtonDisplayed();

        and: "Edit button should not be displayed"
        !contentBrowsePanel.isEditButtonDisplayed();

        and: "Move button should not be displayed"
        !contentBrowsePanel.isMoveButtonDisplayed();

        and: "Duplicate button should not be displayed"
        !contentBrowsePanel.isDuplicateButtonDisplayed();
    }

    def "GIVEN parent 'Deleted' folder is selected WHEN 'Undo delete' button was pressed THEN both folders are getting 'Online'"()
    {
        given: "parent folder with a child are 'Deleted' AND parent is selected"
        findAndSelectContent( PARENT_FOLDER.getName() );

        when: "'Undo delete' button has been pressed"
        contentBrowsePanel.clickToolbarUndodelete();
        saveScreenshot( "parent_child_undodeleted" );

        and: "wait the notification message"
        String message = contentBrowsePanel.waitForNotificationMessage(  );

        then: "correct notification should be present"
        message == Application.ITEMS_IS_UNDELETED;

        and: "parent folder should be 'Published'"
        contentBrowsePanel.getContentStatus( PARENT_FOLDER.getName() ) == ContentStatus.PUBLISHED.getValue();

        and: "child folder should be 'online'"
        findAndSelectContent( CHILD_FOLDER.getName() ).getContentStatus( CHILD_FOLDER.getName() ) == ContentStatus.PUBLISHED.getValue();
    }

    def "GIVEN parent folder and child are 'Deleted' WHEN child folder is selected and 'Undo delete' pressed THEN both folders are getting 'Online'"()
    {
        given: "both contents are published"
        findAndSelectContent( PARENT_FOLDER.getName() );
        contentBrowsePanel.clickToolbarDelete().clickOnDeleteButtonAndConfirm( "2" )

        when: "child is selected AND 'Undo delete' button has been pressed"
        findAndSelectContent( CHILD_FOLDER.getName() ).clickToolbarUndodelete();

        and: "wait the notification message"
        String message = contentBrowsePanel.waitForNotificationMessage(  );

        then: "correct notification should be present"
        message == Application.ITEMS_IS_UNDELETED;

        and: "child folder should be 'Published'"
        contentBrowsePanel.getContentStatus( CHILD_FOLDER.getName() ) == ContentStatus.PUBLISHED.getValue();

        and: "parent folder should be 'online'"
        findAndSelectContent( PARENT_FOLDER.getName() ).getContentStatus( PARENT_FOLDER.getName() ) == ContentStatus.PUBLISHED.getValue();
    }
}
