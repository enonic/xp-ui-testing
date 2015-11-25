package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.schema.content.ContentTypeName

class ContentWizardPanel_Toolbar_Spec
    extends BaseContentSpec
{
    def "WHEN content wizard opened  THEN all buttons on toolbar have correct state"()
    {
        when: "content wizard opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );

        then: "'Delete' button enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save draft' button enabled"
        wizardPanel.isSaveButtonEnabled();

        and: "'Publish' button disabled"
        !wizardPanel.isPublishButtonEnabled();

        and: "'Duplicate' button disabled"
        !wizardPanel.isDuplicateButtonEnabled();

        and: "content status is offline"
        wizardPanel.getStatus() == "Offline";
    }

    def "GIVEN content wizard opened WHEN data typed THEN all buttons on toolbar have correct state"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );

        when: "display name typed"
        wizardPanel.typeDisplayName( NameHelper.uniqueName( "toolbar" ) );

        then: "'Delete' button enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save draft' button enabled"
        wizardPanel.isSaveButtonEnabled();

        and: "'Publish' button disabled"
        wizardPanel.isPublishButtonEnabled();

        and: "'Duplicate' button disabled"
        !wizardPanel.isDuplicateButtonEnabled();

        and: "content status is offline"
        wizardPanel.getStatus() == "Offline";
    }

    def "GIVEN  content wizard opened WHEN data typed and content saved THEN all buttons on toolbar have correct state"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );

        when: "display name typed"
        wizardPanel.typeDisplayName( NameHelper.uniqueName( "toolbar" ) ).save();

        then: "'Delete' button enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save draft' button enabled"
        wizardPanel.isSaveButtonEnabled();

        and: "'Publish' button disabled"
        wizardPanel.isPublishButtonEnabled();

        and: "'Duplicate' button disabled"
        wizardPanel.isDuplicateButtonEnabled();

        and: "content status is offline"
        wizardPanel.getStatus() == "Offline";
    }

    def "GIVEN content-wizard opened AND data typed and content not saved WHEN delete button pressed THEN 'confirmation dialog' appears"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );

        when: "display name typed and Delete button pressed"
        ConfirmationDialog confirmationDialog = wizardPanel.typeDisplayName( NameHelper.uniqueName( "toolbar" ) ).clickToolbarDelete();

        then: "confirmation dialog appears"
        confirmationDialog.isOpened();

        and: "correct title displayed"
        confirmationDialog.getTitle() == "Confirmation"

        and: "correct question displayed"
        confirmationDialog.getQuestion() == ConfirmationDialog.QUESTION;
    }

    def "GIVEN content-wizard opened AND data typed and content not saved WHEN confirmation dialog opened AND 'Yes' pressed THEN wizard closed and content not present in grid"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        String displayName = NameHelper.uniqueName( "toolbar" );

        when: "display name typed and Delete button pressed"
        ConfirmationDialog confirmationDialog = wizardPanel.typeDisplayName( displayName ).clickToolbarDelete();
        confirmationDialog.pressYesButton();

        then: "wizard closed"
        !wizardPanel.isOpened();

        and: "correct title displayed"
        filterPanel.typeSearchText( displayName );
        !contentBrowsePanel.exists( displayName );
    }

    def "GIVEN content-wizard opened AND data typed and content not saved WHEN confirmation dialog opened AND 'No' pressed THEN wizard still present AND 'confirmation dialog' closed"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        String displayName = NameHelper.uniqueName( "toolbar" );

        when: "display name typed and Delete button pressed"
        ConfirmationDialog confirmationDialog = wizardPanel.typeDisplayName( displayName ).clickToolbarDelete();
        confirmationDialog.pressNoButton();

        then: "wizard still opened"
        wizardPanel.isOpened();

        and: "confirmation dialog closed"
        !confirmationDialog.isOpened();
    }

    def "GIVEN existing content opened  WHEN delete button pressed and deleting confirmed THEN wizard closed and content not present in a grid"()
    {
        given: "existing content opened"
        Content folderContent = buildFolderContent( "folder", "wizard_toolbar" );
        addContent( folderContent );

        when: "delete button pressed and deleting confirmed"
        filterPanel.typeSearchText( folderContent.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( folderContent.getName() ).clickToolbarEdit();
        wizard.clickToolbarDelete().pressYesButton();

        then: "content not present in a grid"
        filterPanel.typeSearchText( folderContent.getName() );
        !contentBrowsePanel.exists( folderContent.getName() )
    }
}
