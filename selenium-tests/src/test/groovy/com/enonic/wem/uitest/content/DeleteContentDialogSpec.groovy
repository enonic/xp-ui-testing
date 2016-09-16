package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
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

        then: "delete dialog opened"
        dialog.waitForOpened();
        saveScreenshot( "delete_content_dialog_elements" );

        and: "'Cancel' button is present on the dialog"
        dialog.isCancelButtonPresent();

        and: "'Delete' button is present on the dialog"
        dialog.isDeleteButtonPresent();

        and: "checkbox not displayed, because status of the content is 'offline'"
        !dialog.isCheckboxForDeletePublishedItemsDisplayed();

        and:
        dialog.getContentStatus( CONTENT1.getDisplayName() ) == ContentStatus.OFFLINE.getValue();
    }

    def "GIVEN 'Delete Content' dialog opened WHEN 'Cancel' button pressed THEN dialog closed and content not removed from the grid"()
    {
        given: "content selected and 'Delete' button clicked"
        findAndSelectContent( CONTENT1.getName() );
        DeleteContentDialog dialog = contentBrowsePanel.clickToolbarDelete();
        dialog.waitForOpened();

        when: "'cancel' button pressed"
        dialog.clickOnCancelButton();

        then: "dialog closing"
        !dialog.isOpened();

        and: "content not removed"
        contentBrowsePanel.exists( CONTENT1.getName() );
    }

    def "GIVEN 'Delete Content' dialog opened WHEN 'Cancel' on Top pressed THEN dialog closed and content not removed from the grid"()
    {
        given: "content selected and Delete button clicked"
        findAndSelectContent( CONTENT1.getName() );
        DeleteContentDialog dialog = contentBrowsePanel.clickToolbarDelete();
        dialog.waitForOpened();

        when: "'cancel' button on the top clicked"
        dialog.clickOnCancelTop();

        then: "dialog closing"
        !dialog.isOpened();

        and: "content not removed"
        contentBrowsePanel.exists( CONTENT1.getName() );
    }

    def "GIVEN an existing content WHEN content selected and Delete button clicked THEN delete dialog with one content is displayed"()
    {
        given: "an existing content"
        findAndSelectContent( CONTENT1.getName() );

        when: "'Delete Content' dialog opened"
        DeleteContentDialog dialog = contentBrowsePanel.clickToolbarDelete();
        dialog.waitForOpened();

        then: "only one content is displayed"
        List<String> displayNamesFromUI = dialog.getDisplayNamesToDelete();
        displayNamesFromUI.size() == 1;
        saveScreenshot( "one_content_in_dialog" );

        and: "correct display name of content is displayed"
        CONTENT1.getDisplayName().equals( displayNamesFromUI.get( 0 ) )
    }


    def "GIVEN existing published content WHEN content selected and Delete button clicked THEN modal dialog with the checkbox appears AND the checkbox is not checked"()
    {
        given: "content published"
        findAndSelectContent( CONTENT1.getName() );
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton().waitForDialogClosed();

        when: "modal dialog opened"
        DeleteContentDialog dialog = contentBrowsePanel.clickToolbarDelete();

        then:
        dialog.waitForOpened();
        sleep( 300 );
        saveScreenshot( "delete_dialog_online_status" );

        and: "checkbox appeared in the modal dialog"
        dialog.isCheckboxForDeletePublishedItemsDisplayed();

        and: "the checkbox has correct label"
        dialog.getCheckboxLabelText() == DeleteContentDialog.CHECKBOX_LABEL_TEXT;

        and: "the content has 'online' status"
        dialog.getContentStatus( CONTENT1.getDisplayName() ) == ContentStatus.ONLINE.getValue();

        and: "'Instantly delete published items' checkbox is unchecked"
        !dialog.isInstantlyDeleteCheckboxChecked();
    }

    def "GIVEN two existing content WHEN delete dialog opened THEN two contents are present in the modal dialog"()
    {
        given: "new folder added"
        CONTENT2 = buildFolderContent( NAME_PART, "folder-delete-dialog2" );
        addContent( CONTENT2 );
        filterPanel.typeSearchText( NAME_PART );
        sleep( 1000 );

        when: "two folders are selected and 'Delete' button pressed"
        DeleteContentDialog dialog = contentBrowsePanel.clickOnSelectAll().clickToolbarDelete();
        List<String> displayNames = dialog.getDisplayNamesToDelete();
        saveScreenshot( "two_contents_in_dialog" );

        then: "two correct display names are shown"
        displayNames.contains( CONTENT1.getDisplayName() );

        and:
        displayNames.contains( CONTENT2.getDisplayName() );

        and:
        displayNames.size() == 2;
    }

    def "GIVEN existing published content WHEN content deleting AND 'instantly delete published items' checked THEN published content not listed in the grid"()
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

        then: "dialog closed"
        !dialog.isOpened();

        and: "the content was with 'online' status"
        status == ContentStatus.ONLINE.getValue();

        and: "content not listed in the grid"
        !contentBrowsePanel.exists( CONTENT1.getName() );
    }
}
