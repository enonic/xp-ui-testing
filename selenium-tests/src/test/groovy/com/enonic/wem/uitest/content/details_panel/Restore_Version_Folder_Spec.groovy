package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.SecurityWizardStepForm
import com.enonic.autotests.pages.contentmanager.wizardpanel.SettingsWizardStepForm
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentSettings
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry
import com.enonic.autotests.vo.usermanager.SystemUserName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Restore_Version_Folder_Spec
    extends BaseVersionHistorySpec
{
    @Shared
    Content FOLDER_CONTENT;

    @Shared
    String INITIAL_DISPLAY_NAME = "restore-version-test";

    @Shared
    String NEW_DISPLAY_NAME = NameHelper.uniqueName( "restore-version" )

    def "GIVEN existing folder WHEN display name of the folder was changed THEN new 'version history item' should appear in the version-view"()
    {
        given: "new folder was added"
        ContentSettings settings = ContentSettings.builder().language( NORSK_LANGUAGE ).build();
        FOLDER_CONTENT = buildFolderWithSettingsContent( "folder", INITIAL_DISPLAY_NAME, settings );
        addContent( FOLDER_CONTENT );
        findAndSelectContent( FOLDER_CONTENT.getName() );
        and: "version panel for the content is opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_before_changing_dname" );

        when: "display name of the folder was changed"
        contentBrowsePanel.clickToolbarEdit().typeDisplayName( NEW_DISPLAY_NAME ).save().closeBrowserTab().switchToBrowsePanelTab();
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_after_changing_dname" );

        then: "new 'version history item' should appear in the version-view"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN existing folder with updated 'display name' WHEN the folder was selected AND previous version restored THEN content with original display name should be present"()
    {
        given: "existing folder with updated 'display name'"
        findAndSelectContent( FOLDER_CONTENT.getName() );

        and: "version panel was opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "the folder selected AND previous version is restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        saveScreenshot( "folder_display_name_restored" );

        then: "content with original display name should be present"
        filterPanel.typeSearchText( INITIAL_DISPLAY_NAME );
        contentBrowsePanel.exists( FOLDER_CONTENT.getName() );
    }

    def "GIVEN existing folder is opened AND language was changed WHEN  previous version restored THEN original language should be restored on the wizard page"()
    {
        given: "existing folder is opened"
        findAndSelectContent( FOLDER_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        SettingsWizardStepForm form = new SettingsWizardStepForm( getSession() );
        and: "language is changed"
        form.removeLanguage( NORSK_LANGUAGE ).selectLanguage( ENGLISH_LANGUAGE );
        saveScreenshot( "folder_language_changed" );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "version panel is opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "the folder selected AND previous version of the folder was restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );

        and: "folder is opened"
        contentBrowsePanel.clickToolbarEdit();
        saveScreenshot( "folder_language_restored" );

        then: "original language should be restored on the wizard page"
        form.getLanguage() == NORSK_LANGUAGE;
    }

    def "GIVEN new acl-entry is  added in the folder wizard WHEN versions widget has been opened THEN number of versions should not be changed"()
    {
        given: "new acl entry is added and folder saved"
        ContentAclEntry anonymousEntry = ContentAclEntry.builder().principalName( SystemUserName.SYSTEM_ANONYMOUS.getValue() ).build();
        findAndSelectContent( FOLDER_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        SecurityWizardStepForm securityForm = wizard.clickOnAccessTabLink();
        EditPermissionsDialog modalDialog = securityForm.clickOnEditPermissionsButton();
        modalDialog.setInheritPermissionsCheckbox( false ).addPermission( anonymousEntry ).clickOnApply();
        sleep( 500 );
        List<String> aclNames = wizard.clickOnAccessTabLink().getDisplayNamesOfAclEntries();
        and: "wizard closes"
        wizard.closeBrowserTab().switchToBrowsePanelTab();

        when: "version panel for the content is opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int numberOfVersions = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_number_permissions_updated" );

        then: "number of versions should not be changed, because only permissions were changed"
        numberOfVersions == 4;

        and: "new ACL entry is present"
        aclNames.contains( "Anonymous User" );

    }
}
