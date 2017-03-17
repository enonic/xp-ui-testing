package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.schema.content.ContentTypeName

class ContentWizardPanel_Toolbar_Spec
    extends BaseContentSpec
{
    def "WHEN content wizard is opened AND all inputs are empty THEN all buttons on toolbar have correct state"()
    {
        when: "content wizard is opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );

        then: "'Delete' button should be enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save draft' button should be enabled"
        wizardPanel.isSaveButtonEnabled();

        and: "'Publish' button should be disabled"
        !wizardPanel.isPublishButtonEnabled();

        and: "'Duplicate' button should be enabled"
        wizardPanel.isDuplicateButtonEnabled();

        and: "content status should be 'New' because the folder just created"
        wizardPanel.getStatus() == ContentStatus.NEW.getValue();
    }

    def "GIVEN content wizard is opened WHEN data was typed but not saved yet THEN all buttons on toolbar have correct state"()
    {
        given: "content wizard is opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );

        when: "display name is typed"
        wizardPanel.typeDisplayName( NameHelper.uniqueName( "toolbar" ) );

        then: "'Delete' button should be enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save draft' button should be enabled"
        wizardPanel.isSaveButtonEnabled();

        and: "'Publish' button should be disabled"
        wizardPanel.isPublishButtonEnabled();

        and: "'Duplicate' button should be enabled"
        wizardPanel.isDuplicateButtonEnabled();

        and: "content status should be 'New'"
        wizardPanel.getStatus() == ContentStatus.NEW.getValue();
    }

    def "GIVEN content wizard opened WHEN data typed and content saved THEN all buttons on toolbar have correct state"()
    {
        given: "content wizard is opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );

        when: "display name was typed and saved"
        wizardPanel.typeDisplayName( NameHelper.uniqueName( "toolbar" ) ).save();

        then: "'Delete' button should be enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save draft' button should be enabled"
        wizardPanel.isSaveButtonEnabled();

        and: "'Publish' button should be enabled"
        wizardPanel.isPublishButtonEnabled();

        and: "'Duplicate' button should be enabled"
        wizardPanel.isDuplicateButtonEnabled();

        and: "content status should be 'New'"
        wizardPanel.getStatus() == ContentStatus.NEW.getValue();
    }

    def "GIVEN content wizard opened WHEN content saved AND published THEN 'online' status appears in the wizard AND publish-button should be disabled now"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        wizardPanel.typeDisplayName( NameHelper.uniqueName( "toolbar" ) ).save();

        when: "content has been published"
        wizardPanel.clickOnWizardPublishButton().clickOnPublishNowButton();
        saveScreenshot( "folder_published_in_wizard" );

        then: "'Delete' button should be enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save draft' button should be enabled"
        wizardPanel.isSaveButtonEnabled();

        and: "'Publish' button should be disabled"
        !wizardPanel.isPublishButtonEnabled();

        and: "content status should be 'online'"
        wizardPanel.getStatus() == ContentStatus.ONLINE.getValue();
    }

    def "GIVEN existing content is opened WHEN delete button pressed and deleting was confirmed THEN wizard closes and content should not be present in the grid"()
    {
        given: "existing content is opened"
        Content folderContent = buildFolderContent( "folder", "wizard_toolbar" );
        addContent( folderContent );
        ContentWizardPanel wizard = findAndSelectContent( folderContent.getName() ).clickToolbarEdit();

        when: "'delete' button was pressed and deleting is confirmed"
        wizard.clickToolbarDelete().doDeleteAndSwitchToBrowsePanel();

        then: "content should not be present in the grid"
        filterPanel.typeSearchText( folderContent.getName() );
        !contentBrowsePanel.exists( folderContent.getName() )
    }
}
