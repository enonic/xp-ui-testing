package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 3/16/2017.
 *
 * */
@Stepwise
class ContentWizard_UndoDelete_Spec
    extends BaseContentSpec
{
    @Shared
    Content CONTENT;

    def "GIVEN existing folder WHEN the folder has been published THEN 'PUBLISHED' status should be displayed"()
    {
        given: "existing folder"
        CONTENT = buildFolderContent( "folder", "unpublish of deleted" )
        addReadyContent( CONTENT );

        when: "the folder has been published"
        findAndSelectContent( CONTENT.getName() ).clickToolbarPublish().clickOnPublishButton();

        then: "'Published' status should be displayed"
        contentBrowsePanel.getContentStatus( CONTENT.getName() ) == ContentStatus.PUBLISHED.getValue();
    }

    def "GIVEN existing 'PUBLISHED' folder is opened  WHEN 'Delete' button has been pressed and confirmed THEN 'Undo delete' button should be present on the toolbar"()
    {
        given: "existing 'PUBLISHED folder is opened"
        ContentWizardPanel wizard = findAndSelectContent( CONTENT.getName() ).clickToolbarEdit();

        boolean isDisplayedBeforeDeleting = wizard.isUndoDeleteButtonDisplayed();

        when: "'Delete' button has been pressed and confirmed"
        wizard.clickToolbarDelete().clickOnMarkAsDeletedMenuItem();
        saveScreenshot( "deleted_status_wizard" );

        then: "'Deleted' status should be displayed on the wizard-page"
        wizard.getStatus() == ContentStatus.DELETED.getValue();

        and: "'Delete' button should not be displayed for the 'deleted' content"
        !wizard.isDeleteButtonDisplayed();

        and: "'Save' button should not be displayed for the 'deleted' content"
        !wizard.isSaveButtonDisplayed();

        and: "'undo delete' button should be displayed"
        wizard.isUndoDeleteButtonDisplayed();

        and: "'undo delete' button was not displayed before the deleting"
        !isDisplayedBeforeDeleting;
    }

    def "GIVEN existing 'Deleted' content WHEN the content selected and 'Undo delete' has been pressed THEN the content should be 'PUBLISHED'"()
    {
        when: "the content selected and 'Undo delete' has been pressed"
        findAndSelectContent( CONTENT.getName() ).clickToolbarUndodelete();
        saveScreenshot( "undo-delete-pressed-online" );

        then: "the content gets 'Published'"
        contentBrowsePanel.getContentStatus( CONTENT.getName() ) == ContentStatus.PUBLISHED.getValue();

        and: "expected notification message should be displayed"
        contentBrowsePanel.waitForNotificationMessage() == Application.ITEM_IS_UNDELETED;
    }
}
