package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.SecurityWizardStepForm
import com.enonic.autotests.pages.form.PageTemplateFormViewPanel
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentSettings
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry
import com.enonic.autotests.vo.usermanager.SystemUserName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Restore_Version_Site_Spec
    extends BaseVersionHistorySpec
{
    @Shared
    Content SITE;

    @Shared
    String INITIAL_DISPLAY_NAME = "site-initial-name"

    @Shared
    String NEW_DISPLAY_NAME = "site-restore-version";


    def "GIVEN existing site WHEN display name of the site changed THEN new 'version history item' appeared in the version-view"()
    {
        given: "existing site"
        ContentSettings settings = ContentSettings.builder().language( NORSK_LANGUAGE ).build();
        SITE = buildSiteWithAppsAndSettings( INITIAL_DISPLAY_NAME, settings, MY_FIRST_APP );
        addContent( SITE );
        findAndSelectContent( SITE.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_before_changing_dname_site" );


        when: "display name of the site changed"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab().typeDisplayName(
            NEW_DISPLAY_NAME ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        and: "navigated to 'Version history' panel again"
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_after_changing_dname_site" );

        then: "new 'version history item' appeared in the version-view"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN existing site with updated 'display name' WHEN the site selected AND previous version restored THEN correct display name appears in the grid"()
    {
        given: "existing site with updated 'display name'"
        findAndSelectContent( SITE.getName() );

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "the site selected AND previous version restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        saveScreenshot( "site_display_name_restored" );

        then: "correct display name appears in the grid"
        filterPanel.typeSearchText( INITIAL_DISPLAY_NAME );
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN new acl-entry added for the site WHEN previous version restored THEN acl entry not present in the content wizard"()
    {
        given: "new acl entry added and folder saved"
        ContentAclEntry anonymousEntry = ContentAclEntry.builder().principalName( SystemUserName.SYSTEM_ANONYMOUS.getValue() ).build();
        findAndSelectContent( SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        SecurityWizardStepForm securityForm = wizard.clickOnAccessTabLink();
        EditPermissionsDialog modalDialog = securityForm.clickOnEditPermissionsButton();
        modalDialog.setInheritPermissionsCheckbox( false ).addPermission( anonymousEntry ).clickOnApply();
        sleep( 1000 );
        List<String> beforeRestoring = securityForm.getDisplayNamesOfAclEntries();
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the previous version was restored"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );

        then: "and this role not present after restoring of version without this role"
        !contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab().clickOnAccessTabLink().getDisplayNamesOfAclEntries().contains(
            "Anonymous User" );

        and: "required role was present before restoring"
        beforeRestoring.contains( "Anonymous User" );
    }

    def "GIVEN version panel opened for the folder WHEN version with the acl-entry restored THEN acl-entry present again in the content wizard"()
    {
        given: "new acl entry added and folder saved"
        findAndSelectContent( SITE.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );

        when: "the latest version is restored"
        versionItem.doRestoreVersion( versionItem.getId() );

        and: "security tab opened"
        SecurityWizardStepForm form = contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab().clickOnAccessTabLink();
        saveScreenshot( "version_acl_site_application_restored" );

        then: "new role present after restoring of the latest version"
        form.getDisplayNamesOfAclEntries().contains( "Anonymous User" );
    }

    def "GIVEN existing site with selected application opened WHEN application removed from the wizard THEN number of versions increased"()
    {
        given: "existing site with selected application opened"
        findAndSelectContent( SITE.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int before = allContentVersionsView.getAllVersions().size();
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel siteFormViewPanel = new SiteFormViewPanel( getSession() );

        when: "application removed"
        siteFormViewPanel.removeApp( MY_FIRST_APP );
        saveScreenshot( "app_removed_from_wizard" );
        and: "the site saved"
        wizard.save();
        sleep( 1000 );

        and: "navigated to the tab with the Grid"
        wizard.switchToBrowsePanelTab();
        saveScreenshot( "version_site_app_removed" );

        then: "number of versions increased after the removing of application in wizard"
        allContentVersionsView.getAllVersions().size() - before == 1;
    }

    def "GIVEN existing site without an application opened WHEN version of site with the application was restored THEN application present in the 'application selector'"()
    {
        given: "existing site without an application opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel siteFormViewPanel = new SiteFormViewPanel( getSession() );
        wizard.switchToBrowsePanelTab();

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version with an application restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        contentBrowsePanel.switchToContentWizardTabBySelectedContent();
        saveScreenshot( "version_site_application_restored" );

        then: "application present in the 'application selector'"
        siteFormViewPanel.getAppDisplayNames().get( 0 ) == MY_FIRST_APP;

        and: "one application displayed"
        siteFormViewPanel.getAppDisplayNames().size() == 1;
    }

    def "GIVEN existing site WHEN page controller selected THEN a number of versions increased"()
    {
        given: "existing site with selected application opened"
        findAndSelectContent( SITE.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int before = allContentVersionsView.getAllVersions().size();
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        PageTemplateFormViewPanel pageTemplateFormViewPanel = new PageTemplateFormViewPanel( getSession() );

        when: "page controller selected from the combobox-options"
        pageTemplateFormViewPanel.selectPageController( COUNTRY_REGION_PAGE_CONTROLLER );
        sleep( 500 );
        saveScreenshot( "site_version_controller_added_wizard" );


        and: "home button clicked"
        wizard.switchToBrowsePanelTab();
        saveScreenshot( "version_site_controller_added" );

        then: "number of versions increased after adding of controller in the wizard"
        allContentVersionsView.getAllVersions().size() - before == 1;
    }

    def "GIVEN existing site with controller WHEN version without selected controller restored THEN page controller not selected in the page editor"()
    {
        given: "existing site with selected application opened"
        findAndSelectContent( SITE.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        wizard.switchToBrowsePanelTab();

        when: "version without a selected controller was restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 2 );
        versionItem.doRestoreVersion( versionItem.getId() );
        sleep( 300 );
        contentBrowsePanel.switchToContentWizardTabBySelectedContent();
        sleep( 700 );
        saveScreenshot( "test_version_without_selected_controller" );

        then: "page controller not selected in the page editor and option filter input should be displayed"
        wizard.isPageDescriptorOptionsFilterDisplayed();
    }

}
