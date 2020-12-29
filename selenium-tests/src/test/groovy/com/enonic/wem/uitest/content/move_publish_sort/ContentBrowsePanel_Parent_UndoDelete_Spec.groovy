package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ConfirmValueDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentVersion
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

/**
 * Created on 3/28/2017.
 * */
class ContentBrowsePanel_Parent_UndoDelete_Spec
    extends BaseContentSpec
{
    @Shared
    Content PARENT_FOLDER

    @Shared
    Content CHILD_FOLDER

    def "WHEN existing parent folder with a child has been 'Marked for deletion' THEN parent and child folder should be 'Marked for deletion'"()
    {
        given: "parent folder has been added"
        PARENT_FOLDER = buildFolderContent( "folder", "undo delete parent" );
        CHILD_FOLDER = buildFolderContent( "folder", "undo delete child" );
        addReadyContent( PARENT_FOLDER );
        findAndSelectContent( PARENT_FOLDER.getName() );
        and: "child folder has been added"
        addReadyContent( CHILD_FOLDER );
        and: "both contents are published"
        contentBrowsePanel.clickToolbarPublish().includeChildren( true ).clickOnPublishNowButton();

        when: "Delete button has been pressed and the deleting confirmed"
        contentBrowsePanel.clickToolbarDelete().clickOnMarkAsDeletedMenuItem();
        ConfirmValueDialog confirmContentDeleteDialog = new ConfirmValueDialog( getSession() );
        confirmContentDeleteDialog.typeNumber( "2" ).clickOnConfirmButton();
        saveScreenshot( "parent_child_deleted" );

        then: "parent folder should be 'Deleted'"
        contentBrowsePanel.getContentStatus( PARENT_FOLDER.getName() ) == ContentStatus.MARKED_FOR_DELETION.getValue();

        and: "child folder should be 'Deleted'"
        findAndSelectContent( CHILD_FOLDER.getName() ).getContentStatus( CHILD_FOLDER.getName() ) ==
            ContentStatus.MARKED_FOR_DELETION.getValue();
    }
    // verifies bug - https://github.com/enonic/app-contentstudio/issues/1080
    def "GIVEN existing 'Marked for deletion' folder is selected WHEN versions widget has been opened THEN Marked for deletion should be in Versions Widget"()
    {
        given: "published then 'deleted' folder is selected "
        findAndSelectContent( PARENT_FOLDER.getName() );

        when:
        VersionHistoryWidget versionHistoryWidget = openVersionPanel();
        saveScreenshot( "version_panel_deleted" )
        LinkedList<ContentVersion> contentVersions = versionHistoryWidget.getAllVersions();

        then: "'Marked for deletion' status should be in the widget"
        versionHistoryWidget.getContentStatus().equalsIgnoreCase( ContentStatus.MARKED_FOR_DELETION.getValue() );
    }

    def "WHEN existing 'Marked for deletion' folder has been selected THEN Duplicate, Move, Edit buttons should not be displayed"()
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

    def "GIVEN parent 'Deleted' folder is selected WHEN 'Undo delete' button has been pressed THEN both folders get 'Published'"()
    {
        given: "parent folder with a child are 'Marked for deletion' AND parent folder is selected"
        findAndSelectContent( PARENT_FOLDER.getName() );

        when: "'Undo delete' button has been pressed"
        contentBrowsePanel.clickToolbarUndodelete();
        saveScreenshot( "parent_child_undodeleted" );

        and: "wait the notification message"
        String message = contentBrowsePanel.waitForNotificationMessage();

        then: "expected notification should be present"
        message == Application.ITEMS_IS_UNDELETED;

        and: "parent folder should be 'Published'"
        contentBrowsePanel.getContentStatus( PARENT_FOLDER.getName() ) == ContentStatus.PUBLISHED.getValue();

        and: "child folder should be 'Published'"
        findAndSelectContent( CHILD_FOLDER.getName() ).getContentStatus( CHILD_FOLDER.getName() ) == ContentStatus.PUBLISHED.getValue();
    }

    def "GIVEN parent folder and child are 'Deleted' WHEN child folder has been 'Undo deleted' THEN both folders get 'Published'"()
    {
        given: "both contents are published"
        findAndSelectContent( PARENT_FOLDER.getName() );
        contentBrowsePanel.clickToolbarDelete().clickOnMarkAsDeletedMenuItemAndConfirm( "2" )

        when: "child is selected AND 'Undo delete' button has been pressed"
        findAndSelectContent( CHILD_FOLDER.getName() ).clickToolbarUndodelete();

        and: "wait for the notification message"
        String message = contentBrowsePanel.waitForNotificationMessage();
        sleep(1000);

        then: "expected notification should be present"
        message == Application.ITEMS_IS_UNDELETED;

        and: "child folder should be 'Published'"
        contentBrowsePanel.getContentStatus( CHILD_FOLDER.getName() ) == ContentStatus.PUBLISHED.getValue();

        and: "parent folder should be 'Published'"
        findAndSelectContent( PARENT_FOLDER.getName() ).getContentStatus( PARENT_FOLDER.getName() ) == ContentStatus.PUBLISHED.getValue();
    }
}
