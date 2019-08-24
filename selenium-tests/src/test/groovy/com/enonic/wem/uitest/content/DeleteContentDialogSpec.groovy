package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DeleteContentDialogSpec
    extends BaseContentSpec
{
    @Shared
    Content CONTENT1;

    @Shared
    Content CONTENT2;

    @Shared
    String NAME_PART = "deletecontentdialog";

    def "GIVEN an existing content WHEN content selected and 'Delete' button clicked THEN delete dialog with title 'Delete Content' shown"()
    {
        given: "new folder added"
        CONTENT1 = buildFolderContent( NAME_PART, "folder-delete-dialog1" );
        addContent( CONTENT1 );
        findAndSelectContent( CONTENT1.getName() );

        when: "content selected and 'Delete' button clicked"
        DeleteContentDialog dialog = contentBrowsePanel.clickToolbarDelete();

        then: "delete dialog is opened"
        dialog.waitForOpened();
        saveScreenshot( "delete_content_dialog_elements" );

        and: "'Cancel' button should be present on the dialog"
        dialog.isCancelButtonPresent();

        and: "'Delete' button should be present on the dialog"
        dialog.isDeleteButtonPresent();

        and: "checkbox should not be displayed, because the status of the content is 'New'"
        !dialog.isCheckboxForDeletePublishedItemsDisplayed();

        and:
        dialog.getContentStatus( CONTENT1.getDisplayName() ) == ContentStatus.NEW.getValue();
    }

    def "GIVEN 'Delete Content' dialog is opened WHEN 'Cancel' button was pressed THEN dialog closed and content was not removed "()
    {
        given: "content selected and 'Delete' button clicked"
        findAndSelectContent( CONTENT1.getName() );
        DeleteContentDialog dialog = contentBrowsePanel.clickToolbarDelete();
        dialog.waitForOpened();

        when: "'cancel' button was pressed"
        dialog.clickOnCancelButton();

        then: "dialog is closing"
        !dialog.isOpened();

        and: "content was not removed"
        contentBrowsePanel.exists( CONTENT1.getName() );
    }

    def "GIVEN 'Delete Content' dialog is opened WHEN 'Cancel' on Top was pressed THEN dialog closes and content should not be removed from the grid"()
    {
        given: "content is selected and Delete button clicked"
        findAndSelectContent( CONTENT1.getName() );
        DeleteContentDialog dialog = contentBrowsePanel.clickToolbarDelete();
        dialog.waitForOpened();

        when: "'cancel' button on the top has been clicked"
        dialog.clickOnCancelTop();

        then: "dialog should be closed"
        !dialog.isOpened();

        and: "content was not removed"
        contentBrowsePanel.exists( CONTENT1.getName() );
    }

    def "GIVEN existing content WHEN content was selected and 'Delete' button clicked THEN delete dialog with one content should be displayed"()
    {
        given: "existing content is selcted"
        findAndSelectContent( CONTENT1.getName() );

        when: "'Delete Content' dialog is opened"
        DeleteContentDialog dialog = contentBrowsePanel.clickToolbarDelete();
        dialog.waitForOpened();

        then: "only one content should be displayed"
        List<String> displayNamesFromUI = dialog.getDisplayNamesToDelete();
        displayNamesFromUI.size() == 1;
        saveScreenshot( "one_content_in_dialog" );

        and: "correct display name of content is displayed"
        CONTENT1.getDisplayName()== displayNamesFromUI.get( 0 );
    }


    def "GIVEN existing published content WHEN content was selected and Delete button clicked THEN DeleteContentDialog with the checkbox appears AND the checkbox is not checked"()
    {
        given: "existing content is selected"
        findAndSelectContent( CONTENT1.getName() );

        and: "the content has been published"
        contentBrowsePanel.showPublishMenu().clickOnMarkAsReadyMenuItem();
        contentBrowsePanel.clickToolbarPublish().clickOnPublishButton().waitForDialogClosed();

        when: "the content is selected and 'Delete' button has been pressed"
        DeleteContentDialog dialog = contentBrowsePanel.clickToolbarDelete();

        then: "'Delete Content' dialog should appear"
        dialog.waitForOpened();
        saveScreenshot( "delete_dialog_online_status" );

        and: "checkbox 'Instantly delete' should be present on the modal dialog"
        dialog.isCheckboxForDeletePublishedItemsDisplayed();
        and: "'Instantly delete published items' checkbox is unchecked"
        !dialog.isInstantlyDeleteCheckboxChecked();

        and: "the checkbox has correct label"
        dialog.getCheckboxLabelText() == DeleteContentDialog.CHECKBOX_LABEL_TEXT;

        and: "the content has 'published' status"
        dialog.getContentStatus( CONTENT1.getDisplayName() ) == ContentStatus.PUBLISHED.getValue();
    }

    def "GIVEN two existing content WHEN delete dialog opened THEN two contents are present in the modal dialog"()
    {
        given: "new folder is added"
        CONTENT2 = buildFolderContent( NAME_PART, "folder-delete-dialog2" );
        addContent( CONTENT2 );
        filterPanel.typeSearchText( NAME_PART );
        sleep( 1000 );

        when: "two folders are selected and 'Delete' button pressed"
        DeleteContentDialog dialog = contentBrowsePanel.doSelectAll().clickToolbarDelete();
        List<String> displayNames = dialog.getDisplayNamesToDelete();
        saveScreenshot( "two_contents_in_dialog" );

        then: "two correct display names should be present on  the dialog"
        displayNames.contains( CONTENT1.getDisplayName() );

        and:
        displayNames.contains( CONTENT2.getDisplayName() );

        and:
        displayNames.size() == 2;
    }

    def "GIVEN existing published content WHEN content deleting AND 'instantly delete published items' was checked THEN published content should not be listed in the grid"()
    {
        given: "existing published content"
        findAndSelectContent( CONTENT1.getName() );

        when: "'Instantly delete published items'  selected"
        DeleteContentDialog dialog = contentBrowsePanel.clickToolbarDelete();
        String status = dialog.getContentStatus( CONTENT1.getDisplayName() );
        dialog.clickOnInstantlyCheckbox();

        and: "Delete button was clicked "
        dialog.doDelete();
        saveScreenshot( "delete_dialog_content_deleted" );

        then: "dialog closes"
        !dialog.isOpened();

        and: "the content was with 'published' status"
        status == ContentStatus.PUBLISHED.getValue();

        and: "content should not be listed in the grid"
        !contentBrowsePanel.exists( CONTENT1.getName() );

        and:"Delete button disabled after the deleting"
        !contentBrowsePanel.isDeleteButtonEnabled(  );
    }
}
