package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.BaseContentType
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore

@Ignore
class ContentWizardPanel_Toolbar_Spec
    extends BaseContentSpec
{
    def "WHEN folder-wizard is opened AND all inputs are empty THEN all buttons in the toolbar have correct state"()
    {
        when: "content wizard is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName() );

        then: "'Delete' button should be enabled"
        wizard.isDeleteButtonEnabled();

        and: "'Save' button should be disabled, because name input is empty"
        !wizard.isSaveButtonEnabled();

        and: "'Publish' button should be disabled"
        !wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );

        and: "'Duplicate' button should be enabled"
        wizard.isDuplicateButtonEnabled();

        and: "content status should be 'New' because the folder just created"
        wizard.getStatus() == ContentStatus.NEW.getValue();
    }

    def "GIVEN content wizard is opened WHEN name has been typed but not saved yet THEN all buttons in toolbar have correct state"()
    {
        given: "new folder wizard is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ));
        when: "display name has been filled"
        wizard.typeDisplayName( NameHelper.uniqueName( "toolbar" ) );

        then: "'Archive...' button should be enabled"
        wizard.isArchiveButtonEnabled(  );

        and: "'Save' button gets enabled"
        wizard.isSaveButtonEnabled();

        and: "'Publish' menu item gets enabled"
        wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );

        and: "'Duplicate...' button should be enabled"
        wizard.isDuplicateButtonEnabled();

        and: "content status should be 'New'"
        wizard.getStatus() == ContentStatus.NEW.getValue();
    }

    def "GIVEN new folder-wizard opened WHEN a name has been typed and content saved THEN all buttons in toolbar have correct state"()
    {
        given: "wizard is opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ) );

        when: "display name has been typed and saved"
        wizardPanel.typeDisplayName( NameHelper.uniqueName( "toolbar" ) ).save();

        then: "'Archiv...' button should be enabled"
        wizardPanel.isArchiveButtonEnabled();

        and: "'Saved' button should be disabled"
        wizardPanel.isSavedButtonDisplayed();

        and: "'Publish' menu item should be enabled"
        wizardPanel.showPublishMenu(  ).isPublishMenuItemEnabled(  );

        and: "'Duplicate...' button should be enabled"
        wizardPanel.isDuplicateButtonEnabled();

        and: "content status should be 'New'"
        wizardPanel.getStatus() == ContentStatus.NEW.getValue();
    }

    def "GIVEN new folder wizard is opened WHEN folder has been published THEN 'Published' status appears in the wizard AND publish menu item gets disabled now"()
    {
        given: "new wizard is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName(  ) );
        wizard.typeDisplayName( NameHelper.uniqueName( "folder" ) ).save();

        when: "content has been published"
        wizard.showPublishMenu(  ).clickOnMarkAsReadyMenuItem(  );

        wizard.clickOnPublishButton().clickOnPublishNowButton();
        saveScreenshot( "folder_published_in_wizard" );

        then: "'Saved' button should be disabled"
        !wizard.isSavedButtonEnabled();

        and: "'Publish' menu item should be disabled"
        !wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );

        and: "content status should be 'PUBLISHED'"
        wizard.getStatus() == ContentStatus.PUBLISHED.getValue();
    }

    def "GIVEN existing folder is opened WHEN delete button has been pressed and deleting confirmed THEN wizard closes and content should not be present in the grid"()
    {
        given: "existing content is opened"
        Content folderContent = buildFolderContent( "folder", "wizard_toolbar" );
        addContent( folderContent );
        ContentWizardPanel wizard = findAndSelectContent( folderContent.getName() ).clickToolbarEdit();

        when: "'Archive...' button has been pressed and deleting is confirmed"
        wizard.clickToolbarArchive().clickOnDeleteAndWaitForClosed();

        then: "content should not be present in the grid"
        filterPanel.typeSearchText( folderContent.getName() );
        !contentBrowsePanel.exists( folderContent.getName() );
    }
}
