package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 3/16/2017.
 *
 * Tasks:
 * xp-ui-testing#24 Add selenium tests for 'Undo delete' menu item
 * */
@Stepwise
class ContentWizard_UndoDelete_Spec
    extends BaseContentSpec
{
    @Shared
    Content CONTENT;

    def "GIVEN existing folder WHEN the folder has been published THEN 'Online' status should be displayed"()
    {
        given: "existing folder"
        CONTENT = buildFolderContent( "folder", "unpublish of deleted" )
        addContent( CONTENT );

        when: "the folder has been published"
        findAndSelectContent( CONTENT.getName() ).clickToolbarPublish().clickOnPublishNowButton();

        then: " 'Online' status should be displayed"
        contentBrowsePanel.getContentStatus( CONTENT.getName() ) == ContentStatus.ONLINE.getValue();
    }

    def "GIVEN existing 'online' folder is opened  WHEN 'Delete' button has been pressed and confirmed THEN 'Undo delete' button should be present on the toolbar"()
    {
        given: "existing 'online' folder is opened"
        ContentWizardPanel wizard = findAndSelectContent( CONTENT.getName() ).clickToolbarEdit();

        boolean isDisplayedBeforeDeleting = wizard.isUndoDeleteButtonDisplayed();

        when: "'Delete' button has been pressed and confirmed"
        wizard.clickToolbarDelete().doDelete();
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
}
