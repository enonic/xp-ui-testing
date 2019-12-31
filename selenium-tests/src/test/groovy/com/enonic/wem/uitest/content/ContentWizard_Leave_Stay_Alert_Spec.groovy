package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.BaseContentType
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper


class ContentWizard_Leave_Stay_Alert_Spec
    extends BaseContentSpec
{
    def "GIVEN data typed but the folder is not saved yet WHEN 'delete content' dialog has been opened AND 'Delete' pressed THEN wizard closes and content should be deleted"()
    {
        given: "content wizard is opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ) );
        String displayName = NameHelper.uniqueName( "delete-dialog" );

        when: "display name typed and 'Delete' button  has been pressed"
        DeleteContentDialog deleteContentDialog = wizardPanel.typeDisplayName( displayName ).clickToolbarDelete();
        deleteContentDialog.doDeleteAndSwitchToBrowsePanel();

        then: "content should be deleted:"
        filterPanel.typeSearchText( displayName );
        !contentBrowsePanel.exists( displayName );
    }

    def "GIVEN data typed but the folder is not saved yet WHEN 'delete content' dialog has been opened AND 'Cancel' pressed THEN wizard still present AND modal dialog is closed"()
    {
        given: "content wizard is opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ) );
        String displayName = NameHelper.uniqueName( "delete-dialog" );

        when: "display name has been typed and Delete button pressed"
        DeleteContentDialog deleteContentDialog = wizardPanel.typeDisplayName( displayName ).clickToolbarDelete();
        saveScreenshot( "save_before_close_delete" )
        deleteContentDialog.clickOnCancelButton();

        then: "wizard still is opened"
        wizardPanel.isOpened();

        and: "'delete content' dialog is closed"
        !deleteContentDialog.isOpened();
    }


    def "GIVEN try to close the browser-tab with unsaved changes WHEN 'Live' button has been pressed THEN content should not be updated"()
    {
        given: "content wizard is opened AND typed data was not saved"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ) );
        String name = NameHelper.uniqueName( "folder" );
        and: "try to close the wizard with unsaved changes"
        wizardPanel.typeDisplayName( name ).executeCloseWizardScript();

        when: "'Leave' is selected"
        wizardPanel.acceptAlertAndLeavePage();

        then: "wizard closes and content should not be updated"
        filterPanel.typeSearchText( name );
        !contentBrowsePanel.exists( name )
    }

    def "GIVEN try to close the browser-tab with unsaved changes WHEN 'Stay' button has been clicked THEN wizard has not been closed"()
    {
        given: "content wizard is opened AND typed data was not saved"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ) );
        String name = NameHelper.uniqueName( "folder" );
        wizardPanel.typeDisplayName( name ).executeCloseWizardScript();

        when: "'Stay' button has been clicked"
        wizardPanel.dismissAlertAndStayOnPage();

        then: "wizard has not been closed"
        wizardPanel.isOpened();
    }
}
