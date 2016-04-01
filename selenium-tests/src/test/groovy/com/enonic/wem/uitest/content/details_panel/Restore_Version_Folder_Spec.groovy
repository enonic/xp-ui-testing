package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.SettingsWizardStepForm
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentSettings
import spock.lang.Shared

class Restore_Version_Folder_Spec
    extends BaseVersionHistorySpec
{
    @Shared
    Content FOLDER_CONTENT;

    @Shared
    String INITIAL_DISPLAY_NAME = "restore-version-test";

    @Shared
    String NEW_DISPLAY_NAME = NameHelper.uniqueName( "restore-version" )

    def "GIVEN existing folder WHEN display name of the folder changed THEN new 'version history item' appeared in the version-view"()
    {
        given:
        ContentSettings settings = ContentSettings.builder().language( NORSK_LANGUAGE ).build();
        FOLDER_CONTENT = buildFolderWithSettingsContent( "folder", INITIAL_DISPLAY_NAME, settings );
        addContent( FOLDER_CONTENT );
        findAndSelectContent( FOLDER_CONTENT.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();
        TestUtils.saveScreenshot( getSession(), "versions_before_changing_dname" );


        when: "display name of the folder changed"
        contentBrowsePanel.clickToolbarEdit().typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        TestUtils.saveScreenshot( getSession(), "versions_after_changing_dname" );

        then: "new 'version history item' appeared in the version-view"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN existing folder with updated 'display name' WHEN the folder selected AND previous version restored THEN correct display name appears in the grid"()
    {
        given: "existing folder with updated 'display name'"
        findAndSelectContent( FOLDER_CONTENT.getName() );

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "the folder selected AND previous version restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        TestUtils.saveScreenshot( getSession(), "display_name_restored" );

        then: "correct display name appears in the grid"
        filterPanel.typeSearchText( INITIAL_DISPLAY_NAME );
        contentBrowsePanel.exists( FOLDER_CONTENT.getName() );
    }

    def "GIVEN existing folder AND language changed for it WHEN the folder selected AND previous version restored THEN language is restored on the wizard page"()
    {
        given: "existing folder AND language changed for it"
        findAndSelectContent( FOLDER_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        SettingsWizardStepForm form = new SettingsWizardStepForm( getSession() );
        form.removeLanguage( NORSK_LANGUAGE ).selectLanguage( ENGLISH_LANGUAGE );
        TestUtils.saveScreenshot( getSession(), "language_changed" );
        wizard.save().close( INITIAL_DISPLAY_NAME );

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "the folder selected AND previous version restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );

        and: "folder opened in the wizard again"
        contentBrowsePanel.clickToolbarEdit();
        TestUtils.saveScreenshot( getSession(), "language_restored" );

        then: "language is restored on the wizard page"
        form.getLanguage() == NORSK_LANGUAGE;
    }
}
