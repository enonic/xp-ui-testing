package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.BaseContentType
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.SettingsWizardStepForm
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentSettings
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
class ContentWizardPanel_Settings_Spec
    extends BaseContentSpec
{

    @Shared
    Content content;

    def "WHEN new wizard is opened THEN language should not be selected AND language 'option filter' should be present"()
    {
        when: "content wizard is opened"
        contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName() );
        SettingsWizardStepForm form = new SettingsWizardStepForm( getSession() );

        then: "language 'option filter' is present"
        form.isLanguageInputFilterPresent();

        and: "language should not be selected"
        form.getLanguage() == null;

        and: "Owner should be selected by default:"
        form.getOwner() == SUPER_USER;
    }

    def "GIVEN new content with a language is added WHEN content has been reopened THEN expected language should be present in settings"()
    {
        given: "new content with a language is added"
        ContentSettings settings = ContentSettings.builder().language( NORSK_LANGUAGE ).build();
        content = buildFolderWithSettingsContent( "folder", "content settings", settings );
        addContent( content );

        when: "when content has been opened"
        SettingsWizardStepForm form = findAndSelectContent(
            content.getName() ).clickToolbarEditAndSwitchToWizardTab().clickOnSettingsTabLink();
        saveScreenshot( "norsk-lang" );

        then: "correct language present in settings"
        form.getLanguage() == NORSK_LANGUAGE;
    }

    def "GIVEN existing content with language is opened WHEN language has been removed AND content saved  THEN language should not be selected in the settings"()
    {
        given: "when content opened for edit"
        ContentWizardPanel wizard = findAndSelectContent( content.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SettingsWizardStepForm form = wizard.clickOnSettingsTabLink();

        when: "language removed AND content saved"
        form.removeLanguage( NORSK_LANGUAGE );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        sleep( 700 );
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab().clickOnSettingsTabLink();

        then: "language should not be not present in settings"
        form.getLanguage() == null;
    }

    def "GIVEN existing content with owner is opened WHEN owner has been updated THEN new owner gets visible in settings tab"()
    {
        given: "when existing content opened for edit"
        ContentWizardPanel wizard = findAndSelectContent( content.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SettingsWizardStepForm form = wizard.clickOnSettingsTabLink();

        when: "owner has been changed AND the content saved"
        form.removeOwner( SUPER_USER ).selectOwner( ANONYMOUS_USER );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        and: "the content has been opened"
        findAndSelectContent( content.getName() ).clickToolbarEditAndSwitchToWizardTab().clickOnSettingsTabLink()
        saveScreenshot( "norsk-lang-owner-anonym" )

        then: "new owner should be present in settings tab"
        form.getOwner() == ANONYMOUS_USER;
    }
}
