package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.SecurityWizardStepForm
import com.enonic.autotests.pages.contentmanager.wizardpanel.SettingsWizardStepForm
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentSettings
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry
import com.enonic.autotests.vo.usermanager.SystemUserName
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
        saveScreenshot( "versions_before_changing_dname" );


        when: "display name of the folder changed"
        contentBrowsePanel.clickToolbarEdit().typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_after_changing_dname" );

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
        saveScreenshot( "folder_display_name_restored" );

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
        saveScreenshot( "folder_language_restored" );

        then: "language is restored on the wizard page"
        form.getLanguage() == NORSK_LANGUAGE;
    }

    def "GIVEN new acl-entry added for the folder WHEN previous version restored THEN acl entry not present in the content wizard"()
    {
        given: "new acl entry added and folder saved"
        ContentAclEntry anonymousEntry = ContentAclEntry.builder().principalName( SystemUserName.SYSTEM_ANONYMOUS.getValue() ).build();
        findAndSelectContent( FOLDER_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        SecurityWizardStepForm securityForm = wizard.clickOnSecurityTabLink();
        EditPermissionsDialog modalDialog = securityForm.clickOnEditPermissionsButton();
        modalDialog.setCheckedForInheritCheckbox( false ).addPermission( anonymousEntry ).clickOnApply();
        sleep( 1000 );
        List<String> beforeRestoring = securityForm.getDisplayNamesOfAclEntries();
        wizard.save().close( NEW_DISPLAY_NAME );

        when: "the previous version is restored"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );

        then: "and this role not present after restoring of version without this role"
        !contentBrowsePanel.clickToolbarEdit().clickOnSecurityTabLink().getDisplayNamesOfAclEntries().contains( "Anonymous User" );

        and: "required role was present before restoring"
        beforeRestoring.contains( "Anonymous User" );
    }

    def "GIVEN version panel opened for the folder WHEN version with the acl-entry restored THEN acl-entry present again in the content wizard"()
    {
        given: "new acl entry added and folder saved"
        findAndSelectContent( FOLDER_CONTENT.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );

        when: "the latest version is restored"
        versionItem.doRestoreVersion( versionItem.getId() );

        then: "new role present after restoring of the latest version"
        contentBrowsePanel.clickToolbarEdit().clickOnSecurityTabLink().getDisplayNamesOfAclEntries().contains( "Anonymous User" );
    }

}
