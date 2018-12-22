package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.BaseContentType
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper

/**
 * TASK: XP-4570 Add selenium tests for Alert, that appears where there are unsaved changes in the wizard
 **/
class ContentWizard_Leave_Stay_Alert_Spec
    extends BaseContentSpec
{
    def "GIVEN folder-wizard is opened AND data typed but the folder is not saved WHEN 'delete content' dialog opened AND 'Delete' pressed THEN wizard closed and content not listed in the grid"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ) );
        String displayName = NameHelper.uniqueName( "delete-dialog" );

        when: "display name typed and Delete button pressed"
        DeleteContentDialog deleteContentDialog = wizardPanel.typeDisplayName( displayName ).clickToolbarDelete();
        deleteContentDialog.doDeleteAndSwitchToBrowsePanel();

        then: "content not listed in the grid"
        filterPanel.typeSearchText( displayName );
        !contentBrowsePanel.exists( displayName );
    }

    def "GIVEN content-wizard opened AND data typed and content not saved WHEN 'delete content' dialog opened AND 'Cancel' pressed THEN wizard still present AND modal dialog closed"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ) );
        String displayName = NameHelper.uniqueName( "delete-dialog" );

        when: "display name typed and Delete button pressed"
        DeleteContentDialog deleteContentDialog = wizardPanel.typeDisplayName( displayName ).clickToolbarDelete();
        saveScreenshot( "save_before_close_delete" )
        deleteContentDialog.clickOnCancelButton();

        then: "wizard still opened"
        wizardPanel.isOpened();

        and: "'delete content' dialog closed"
        !deleteContentDialog.isOpened();
    }

    def "GIVEN content-wizard opened AND data typed and content not saved WHEN delete button pressed THEN 'delete content dialog' appears"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ) );

        when: "display name typed and Delete button pressed"
        DeleteContentDialog deleteContentDialog = wizardPanel.typeDisplayName( NameHelper.uniqueName( "toolbar" ) ).clickToolbarDelete();

        then: "confirmation dialog appears"
        deleteContentDialog.isOpened();

        and: "correct title displayed"
        deleteContentDialog.getTitle() == "Delete item"
    }

    def "GIVEN content wizard is opened AND typed data was not saved AND wizard is closing WHEN 'Leave' has been pressed THEN content with the name not listed in the grid"()
    {
        given: "content wizard is opened AND typed data was not saved"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ) );
        String name = NameHelper.uniqueName( "folder" );
        and: "try to close the wizard with unsaved changes"
        wizardPanel.typeDisplayName( name ).executeCloseWizardScript();

        when: "'Leave' is selected"
        wizardPanel.acceptAlertAndLeavePage();

        then: "wizard closed and content with the name not listed in the grid"
        filterPanel.typeSearchText( name );
        !contentBrowsePanel.exists( name )
    }

    def "GIVEN  content wizard is opened AND typed data was not saved AND wizard is closing WHEN 'Stay' is selected THEN wizard has not been closed"()
    {
        given: "content wizard is opened AND typed data was not saved"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ) );
        String name = NameHelper.uniqueName( "folder" );
        wizardPanel.typeDisplayName( name ).executeCloseWizardScript();

        when: "'Stay' is selected"
        wizardPanel.dismissAlertAndStayOnPage();

        then: "wizard has not been closed"
        wizardPanel.isOpened();
    }
}
