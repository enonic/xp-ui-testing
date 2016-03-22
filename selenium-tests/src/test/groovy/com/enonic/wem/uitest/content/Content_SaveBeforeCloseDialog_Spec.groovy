package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.xp.schema.content.ContentTypeName

class Content_SaveBeforeCloseDialog_Spec
    extends BaseContentSpec
{

    def "GIVEN content-wizard opened AND data typed and content not saved WHEN confirmation dialog opened AND 'Yes' pressed THEN wizard closed and content not present in grid"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        String displayName = NameHelper.uniqueName( "confirmation" );

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
        String displayName = NameHelper.uniqueName( "confirmation" );

        when: "display name typed and Delete button pressed"
        ConfirmationDialog confirmationDialog = wizardPanel.typeDisplayName( displayName ).clickToolbarDelete();
        confirmationDialog.pressNoButton();

        then: "wizard still opened"
        wizardPanel.isOpened();

        and: "confirmation dialog closed"
        !confirmationDialog.isOpened();
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

    def "GIVEN closing of not saved content WHEN 'Y' key pressed THEN new content listed in the grid"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        String name = NameHelper.uniqueName( "folder" );
        SaveBeforeCloseDialog saveBeforeCloseDialog = wizardPanel.typeDisplayName( name ).close( name );

        when: "'Y' key pressed"
        saveBeforeCloseDialog.press_Y_key();
        String expectedMessage = String.format( Application.CONTENT_SAVED, name );
        boolean isMessageDisplayed = contentBrowsePanel.waitExpectedNotificationMessage( expectedMessage, Application.EXPLICIT_NORMAL );

        then: "modal dialog closed"
        !saveBeforeCloseDialog.isDisplayed();

        and: "correct notification message appears"
        isMessageDisplayed;

        and: "new content listed in the grid"
        filterPanel.typeSearchText( name );
        contentBrowsePanel.exists( name )
    }

    def "GIVEN closing of not saved content WHEN 'N' key pressed THEN new content not listed in the grid"()
    {
        given: "closing of not saved content"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        String name = NameHelper.uniqueName( "folder" );
        SaveBeforeCloseDialog saveBeforeCloseDialog = wizardPanel.typeDisplayName( name ).close( name );

        when: "'N' key pressed"
        saveBeforeCloseDialog.press_N_key();

        then: "modal dialog closed"
        !saveBeforeCloseDialog.isDisplayed();

        and: "new content not listed in the grid"
        filterPanel.typeSearchText( name );
        !contentBrowsePanel.exists( name )
    }
}
