package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.BaseContentType
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content

class ContentWizardPanel_Toolbar_Spec
    extends BaseContentSpec
{
    def "WHEN folder-wizard is opened AND all inputs are empty THEN all buttons on toolbar have correct state"()
    {
        when: "content wizard is opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName() );

        then: "'Delete' button should be enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button should be disabled, because name input is empty"
        !wizardPanel.isSaveButtonEnabled();

        and: "'Publish' button should be disabled"
        !wizardPanel.isPublishButtonEnabled();

        and: "'Duplicate' button should be enabled"
        wizardPanel.isDuplicateButtonEnabled();

        and: "content status should be 'New' because the folder just created"
        wizardPanel.getStatus() == ContentStatus.NEW.getValue();
    }

    def "GIVEN content wizard is opened WHEN name has been typed but not saved yet THEN all buttons on toolbar have correct state"()
    {
        given: "content wizard is opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ));
        when: "display name is typed"
        wizardPanel.typeDisplayName( NameHelper.uniqueName( "toolbar" ) );

        then: "'Delete' button should be enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button should be enabled"
        wizardPanel.isSaveButtonEnabled();

        and: "'Publish' button should be enabled"
        wizardPanel.isPublishButtonEnabled();

        and: "'Duplicate' button should be enabled"
        wizardPanel.isDuplicateButtonEnabled();

        and: "content status should be 'New'"
        wizardPanel.getStatus() == ContentStatus.NEW.getValue();
    }

    def "GIVEN folder-wizard opened WHEN a name has been typed and content saved THEN all buttons on toolbar have correct state"()
    {
        given: "wizard is opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ) );

        when: "display name has been typed and saved"
        wizardPanel.typeDisplayName( NameHelper.uniqueName( "toolbar" ) ).save();

        then: "'Delete' button should be enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Saved' button should be disabled"
        wizardPanel.isSavedButtonDisplayed();

        and: "'Publish' button should be enabled"
        wizardPanel.isPublishButtonEnabled();

        and: "'Duplicate' button should be enabled"
        wizardPanel.isDuplicateButtonEnabled();

        and: "content status should be 'New'"
        wizardPanel.getStatus() == ContentStatus.NEW.getValue();
    }

    def "GIVEN folder wizard is opened WHEN content saved AND published THEN 'online' status appears in the wizard AND publish-button should be disabled now"()
    {
        given: "wizard is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ) );
        wizard.typeDisplayName( NameHelper.uniqueName( "toolbar" ) ).save();

        when: "content has been published"
        ConfirmationDialog confirm = wizard.showPublishMenu(  ).clickOnMarkAsReadyMenuItem(  );
        confirm.pressYesButton();
        wizard.clickOnWizardPublishButton().clickOnPublishButton();
        saveScreenshot( "folder_published_in_wizard" );

        then: "'Delete' button should be enabled"
        wizard.isDeleteButtonEnabled();

        and: "'Saved' button should be disabled"
        !wizard.isSavedButtonEnabled();

        and: "'Publish' button should be disabled"
        !wizard.isPublishButtonEnabled();

        and: "content status should be 'online'"
        wizard.getStatus() == ContentStatus.PUBLISHED.getValue();
    }

    def "GIVEN existing folder is opened WHEN delete button has been pressed and deleting confirmed THEN wizard closes and content should not be present in the grid"()
    {
        given: "existing content is opened"
        Content folderContent = buildFolderContent( "folder", "wizard_toolbar" );
        addContent( folderContent );
        ContentWizardPanel wizard = findAndSelectContent( folderContent.getName() ).clickToolbarEdit();

        when: "'delete' button was pressed and deleting is confirmed"
        wizard.clickToolbarDelete().doDeleteAndSwitchToBrowsePanel();

        then: "content should not be present in the grid"
        filterPanel.typeSearchText( folderContent.getName() );
        !contentBrowsePanel.exists( folderContent.getName() );
    }
}
