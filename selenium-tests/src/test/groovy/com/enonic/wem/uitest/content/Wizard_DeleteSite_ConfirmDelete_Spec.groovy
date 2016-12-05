package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ConfirmContentDeleteDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Wizard_DeleteSite_ConfirmDelete_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    int INITIAL_NUMBER_TO_DELETE = 2;

    def "GIVEN creating of site WHEN data typed saved  AND 'Delete' on the wizard-toolbar pressed AND 'Delete' on the dialog clicked THEN 'Confirm delete' dialog appears with correct control elements"()
    {
        given: "existing site selected AND delete button pressed"
        SITE = buildSiteWithNameAndDispalyNameAndDescription( "site", "confirm delete", "description" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData(
            SITE ).save();

        when: "ConfirmContentDelete Dialog has been opened"
        DeleteContentDialog deleteContentDialog = wizard.clickToolbarDelete();
        deleteContentDialog.waitForOpened();

        and: "Delete button on the dialog was pressed"
        deleteContentDialog.clickOnDeleteButton();
        ConfirmContentDeleteDialog confirmDialog = new ConfirmContentDeleteDialog( getSession() );
        confirmDialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        saveScreenshot( "test_wizard_confirm_delete_site" );

        then: "'Confirm delete' dialog appears with correct control elements"
        confirmDialog.isCancelButtonBottomDisplayed();

        and:
        confirmDialog.isCancelButtonTopDisplayed();

        and:
        !confirmDialog.isConfirmButtonButtonEnabled();

        and:
        confirmDialog.getNumberOfContentToDelete() == INITIAL_NUMBER_TO_DELETE;
    }

    def "GIVEN existing site is opened WHEN 'Delete' on the toolbar clicked and correct number is confirmed THEN wizard has been closed and site not listed in the browse panel"()
    {
        given: "existing site is opened "
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();

        when: "Delete on the wizard toolbar clicked"
        DeleteContentDialog deleteDialog = wizard.clickToolbarDelete();
        deleteDialog.waitForOpened();

        and: "Delete button on the dialog clicked"
        deleteDialog.clickOnDeleteButton();
        ConfirmContentDeleteDialog confirm = new ConfirmContentDeleteDialog( getSession() );
        confirm.waitUntilDialogShown( Application.EXPLICIT_NORMAL );

        and: "correct number typed"
        confirm.typeNumber( "2" );
        saveScreenshot( "confirm_delete_site_number_typed" );

        and: "'Confirm' button clicked"
        confirm.clickOnConfirmButton();
        wizard.switchToBrowsePanelTab();


        then: "site not listed in the grid"
        !contentBrowsePanel.exists( SITE.getName() );
    }
}
