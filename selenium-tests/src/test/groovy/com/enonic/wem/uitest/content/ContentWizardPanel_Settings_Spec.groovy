package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.SettingsWizardStepForm
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentSettings
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentWizardPanel_Settings_Spec
    extends BaseContentSpec
{

    @Shared
    Content content;

    def "WHEN content wizard opened THEN language not selected AND option input filter is present"()
    {
        when: "content wizard opened"
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        SettingsWizardStepForm form = new SettingsWizardStepForm( getSession() );

        then: "language 'option filter' is present"
        form.isLanguageInputFilterPresent();

        and: "language not selected"
        form.getLanguage() == null;

        and:
        form.getOwner() == SUPER_USER;
    }

    def "GIVEN saving a content with a language WHEN content selected and 'Edit' pressed in toolbar  THEN correct language present in settings"()
    {
        given: "saving a content with a language"
        ContentSettings settings = ContentSettings.builder().language( NORSK_LANGUAGE ).build();
        content = buildFolderWithSettingsContent( "folder", "content settings", settings );
        addContent( content );

        when: "when content opened for edit"
        SettingsWizardStepForm form = findAndSelectContent(
            content.getName() ).clickToolbarEditAndSwitchToWizardTab().clickOnSettingsTabLink();
        saveScreenshot( "norsk-lang" );

        then: "correct language present in settings"
        form.getLanguage() == NORSK_LANGUAGE;
    }

    def "GIVEN existing content with language opened WHEN language removed AND content saved  THEN no one language present in settings"()
    {
        given: "when content opened for edit"
        ContentWizardPanel wizard = findAndSelectContent( content.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SettingsWizardStepForm form = wizard.clickOnSettingsTabLink();

        when: "language removed AND content saved"
        form.removeLanguage( NORSK_LANGUAGE );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        sleep( 700 );
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab().clickOnSettingsTabLink();

        then: "language not present in settings"
        form.getLanguage() == null;
    }

    def "GIVEN existing content with owner opened WHEN owner changed AND content saved  THEN new owner shown in settings"()
    {
        given: "when existing content opened for edit"
        ContentWizardPanel wizard = findAndSelectContent( content.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SettingsWizardStepForm form = wizard.clickOnSettingsTabLink();

        when: "owner changed AND content saved"
        form.removeOwner( SUPER_USER ).selectOwner( ANONYMOUS_USER );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        and: "the content opened in the wizard"
        findAndSelectContent( content.getName() ).clickToolbarEditAndSwitchToWizardTab().clickOnSettingsTabLink()
        saveScreenshot( "norsk-lang-owner-anonym" )

        then: "new owner shown in settings"
        form.getOwner() == ANONYMOUS_USER;
    }
}
