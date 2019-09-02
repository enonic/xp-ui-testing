package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.SettingsWizardStepForm
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 17.01.2017.
 * */
@Stepwise
class ContentWizard_Keyboard_Shortcuts_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_FOLDER;

    @Shared
    String NEW_DISPLAY_NAME = NameHelper.uniqueName( "test" );

    //Ctrl(Cmd)+S  This test is reimplemented in JS
    @Ignore
    def "GIVEN existing folder is opened WHEN shortcut to 'Save' has been pressed THEN correct notification message should be displayed"()
    {
        given: "existing folder"
        TEST_FOLDER = buildFolderContent( "folder", "wizard keyboard shortcuts" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType(
            TEST_FOLDER.getContentTypeName() ).waitUntilWizardOpened();
        wizard.typeData( TEST_FOLDER );

        when: "shortcut to 'Save' has been pressed"
        wizard.pressSaveKeyboardShortcut();
        String expectedMessage = String.format( Application.CONTENT_SAVED, TEST_FOLDER.getName() );
        saveScreenshot( "wizard_keyboard_shortcut_save" );

        then: "correct notification message should be displayed"
        wizard.waitExpectedNotificationMessage( expectedMessage, Application.EXPLICIT_NORMAL );
    }
    // Save content and close wizard tab  Ctrl+Enter This test is reimplemented in JS
    @Ignore
    def "GIVEN existing folder is opened AND the display name is changed WHEN shortcut to 'Save and Close' has been pressed THEN the wizard should be closed AND content should be listed with the new display name"()
    {
        given: "existing folder"
        ContentWizardPanel wizard = findAndSelectContent( TEST_FOLDER.getName() ).clickToolbarEdit();
        wizard.typeDisplayName( NEW_DISPLAY_NAME );

        when: "shortcut to 'Save' has been pressed"
        wizard.pressSaveAndCloseKeyboardShortcut().switchToBrowsePanelTab();
        and: "the new display name has been typed in the search input"
        filterPanel.typeSearchText( NEW_DISPLAY_NAME );

        then: "the wizard should be closed AND content should be listed with the new display name"
        contentBrowsePanel.exists( TEST_FOLDER.getName() );
    }
    //Close wizard tab Alt+W  This test is reimplemented in JS
    @Ignore
    def "GIVEN existing folder is opened AND the display name is changed WHEN shortcut to 'Close wizard' has been pressed THEN 'Alert' dialog with warning message should appear"()
    {
        given: "existing folder is opened"
        ContentWizardPanel wizard = findAndSelectContent( TEST_FOLDER.getName() ).clickToolbarEdit();
        and: "display name updated"
        SettingsWizardStepForm form = new SettingsWizardStepForm( getSession() );
        form.selectLanguage( ENGLISH_LANGUAGE );

        when: "shortcut to 'Close' has been pressed"
        wizard.pressCloseKeyboardShortcut();

        then: "'Alert' dialog with warning message should appear"
        wizard.waitIsAlertDisplayed();
    }
}
