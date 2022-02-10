package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog
import com.enonic.autotests.pages.form.PageTemplateFormViewPanel
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentSettings
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry
import com.enonic.autotests.vo.usermanager.SystemUserName
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
class Restore_Version_Site_Spec
    extends BaseVersionHistorySpec
{
    @Shared
    Content SITE;

    @Shared
    String INITIAL_DISPLAY_NAME = "site-initial-name"

    @Shared
    String NEW_DISPLAY_NAME = "site-restore-version";


    def "GIVEN existing site WHEN display name has been changed THEN new 'version history item' appears in the version-view"()
    {
        given: "existing site"
        ContentSettings settings = ContentSettings.builder().language( NORSK_LANGUAGE ).build();
        SITE = buildSiteWithAppsAndSettings( INITIAL_DISPLAY_NAME, settings, MY_FIRST_APP );
        addContent( SITE );
        findAndSelectContent( SITE.getName() );
        VersionHistoryWidget allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "site_versions_1" );

        when: "display name of the site has been changed"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab().typeDisplayName(
            NEW_DISPLAY_NAME ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        and: "navigated to 'Version history' panel again"
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "site_versions_2" );

        then: "new 'version history item' appeared in the version-view"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN site with updated 'display name' is selected WHEN previous version has been reverted THEN display name should be reverted"()
    {
        given: "site with updated 'display name' is selected"
        findAndSelectContent( SITE.getName() );

        and: "version panel is opened"
        VersionHistoryWidget allContentVersionsView = openVersionPanel();

        when: "the previous version has been restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        versionItem.doRevertVersion();
        saveScreenshot( "site_versions_reverted1" );

        then: "previous display name should appear in the grid"
        filterPanel.typeSearchText( INITIAL_DISPLAY_NAME );
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN new acl-entry has been added WHEN previous version has been reverted THEN list of acl entries should not be changed"()
    {
        given: "new acl entry has been added and Save pressed"
        ContentAclEntry anonymousEntry = ContentAclEntry.builder().principalName( SystemUserName.SYSTEM_ANONYMOUS.getValue() ).build();
        findAndSelectContent( SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        EditPermissionsDialog modalDialog = wizard.clickOnEditPermissionsButton();
        modalDialog.setInheritPermissionsCheckbox( false ).addPermission( anonymousEntry );
        List<String> beforeRestoring = modalDialog.getPrincipalsDisplayName();
        modalDialog.clickOnApply();

        wizard.closeBrowserTab().switchToBrowsePanelTab();

        when: "the previous version has been restored"
        VersionHistoryWidget allContentVersionsView = openVersionPanel();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        versionItem.doRevertVersion();
        and:
        modalDialog = contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab().clickOnEditPermissionsButton();

        then: "acl-entry should be displayed after the reverting of previous version"
        modalDialog.getPrincipalsDisplayName().contains( "Anonymous User" );

        and: "the entry was present before the restoring"
        beforeRestoring.contains( "Anonymous User" );
    }

    def "GIVEN the site is selected AND version panel is opened WHEN previous version has been reverted THEN list of acl entries should not be changed"()
    {
        given: "the site is selected AND version panel is opened"
        findAndSelectContent( SITE.getName() );
        VersionHistoryWidget allContentVersionsView = openVersionPanel();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );

        when: "the previous version has been restored"
        versionItem.doRevertVersion();

        and: "navigate to the security tab"
        EditPermissionsDialog editPermissionsDialog = contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab().clickOnEditPermissionsButton();
        saveScreenshot( "version_acl_site_application_restored" );

        then: "the role should appear after the restoring of the latest version"
        editPermissionsDialog.getPrincipalsDisplayName().contains( "Anonymous User" );
    }

    def "GIVEN existing site is opened WHEN application has been removed THEN number of versions should be increased"()
    {
        given: "existing site with selected application opened"
        findAndSelectContent( SITE.getName() );
        VersionHistoryWidget allContentVersionsView = openVersionPanel();
        int before = allContentVersionsView.getAllVersions().size();
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel siteFormViewPanel = new SiteFormViewPanel( getSession() );

        when: "application has been removed"
        siteFormViewPanel.removeApp( MY_FIRST_APP );
        and: "the site automatically saved"
        wizard.waitForNotificationMessage();
        saveScreenshot( "app_removed_from_wizard" );
        sleep( 1000 );

        and: "navigated to the tab with the Grid"
        wizard.switchToBrowsePanelTab();
        saveScreenshot( "version_site_app_removed" );

        then: "number of versions should be increased after the removing"
        allContentVersionsView.getAllVersions().size() - before == 1;
    }

    def "GIVEN version of the site without an application is opened WHEN the version  with application has been restored THEN application should appear in the 'application selector'"()
    {
        given: "version of site without an application is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel siteFormViewPanel = new SiteFormViewPanel( getSession() );
        wizard.switchToBrowsePanelTab();

        and: "version panel is opened"
        VersionHistoryWidget allContentVersionsView = openVersionPanel();

        when: "version with single application has been reverted"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        versionItem.doRevertVersion();
        contentBrowsePanel.switchToContentWizardTabBySelectedContent();
        saveScreenshot( "version_site_application_restored" );

        then: "one application should be present in 'Applications selector'"
        siteFormViewPanel.getAppDisplayNames().size() == 1;

        and: "required application name should be displayed in the 'application selector'"
        siteFormViewPanel.getAppDisplayNames().get( 0 ) == MY_FIRST_APP;
    }

    def "GIVEN existing site WHEN page controller has been selected THEN the number of versions should be increased"()
    {
        given: "existing site with selected application is opened"
        findAndSelectContent( SITE.getName() );
        VersionHistoryWidget allContentVersionsView = openVersionPanel();
        int before = allContentVersionsView.getAllVersions().size();
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        PageTemplateFormViewPanel pageTemplateFormViewPanel = new PageTemplateFormViewPanel( getSession() );

        when: "page controller has been selected"
        pageTemplateFormViewPanel.selectPageController( COUNTRY_REGION_PAGE_CONTROLLER );
        sleep( 500 );
        saveScreenshot( "site_version_controller_added_wizard" );

        and: "home button has been clicked"
        wizard.switchToBrowsePanelTab();
        saveScreenshot( "version_site_controller_added" );

        then: "number of versions increased after adding of controller in the wizard"
        allContentVersionsView.getAllVersions().size() - before == 1;
    }

    def "GIVEN existing site with controller WHEN version of the site in which the controller was not selected has been restored THEN controller selector (dropdown list) gets visible in the wizard page"()
    {
        given: "existing site with selected application opened"
        findAndSelectContent( SITE.getName() );
        VersionHistoryWidget allContentVersionsView = openVersionPanel();
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        wizard.switchToBrowsePanelTab();

        when: "version of the site in which the controller was not selected has been reverted"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 3 );
        versionItem.doRevertVersion();
        sleep( 300 );
        contentBrowsePanel.switchToContentWizardTabBySelectedContent();
        sleep( 700 );
        saveScreenshot( "test_version_without_selected_controller" );

        then: "page controller should be not selected and 'option filter' input should be displayed"
        wizard.isPageDescriptorOptionsFilterDisplayed();
    }

}
