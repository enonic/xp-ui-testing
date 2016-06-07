package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.SecurityWizardStepForm
import com.enonic.autotests.pages.form.PageTemplateFormViewPanel
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.utils.TestUtils
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
        TestUtils.saveScreenshot( getSession(), "versions_before_changing_dname_site" );


        when: "display name of the site changed"
        contentBrowsePanel.clickToolbarEdit().typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        TestUtils.saveScreenshot( getSession(), "versions_after_changing_dname_site" );

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
        TestUtils.saveScreenshot( getSession(), "site_display_name_restored" );

        then: "correct display name appears in the grid"
        filterPanel.typeSearchText( INITIAL_DISPLAY_NAME );
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN new acl-entry added for the site WHEN previous version restored THEN acl entry not present in the content wizard"()
    {
        given: "new acl entry added and folder saved"
        ContentAclEntry anonymousEntry = ContentAclEntry.builder().principalName( SystemUserName.SYSTEM_ANONYMOUS.getValue() ).build();
        findAndSelectContent( SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        SecurityWizardStepForm securityForm = wizard.clickOnSecurityTabLink();
        EditPermissionsDialog modalDialog = securityForm.clickOnEditPermissionsButton();
        modalDialog.setCheckedForInheritCheckbox( false ).addPermission( anonymousEntry ).clickOnApply();
        sleep( 1000 );
        List<String> beforeRestoring = securityForm.getAllDisplayNamesOfAclEntries();
        wizard.save().close( SITE.getDisplayName() );

        when: "the previous version was restored"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );

        then: "and this role not present after restoring of version without this role"
        !contentBrowsePanel.clickToolbarEdit().clickOnSecurityTabLink().getAllDisplayNamesOfAclEntries().contains( "Anonymous User" );

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

        then: "new role present after restoring of the latest version"
        contentBrowsePanel.clickToolbarEdit().clickOnSecurityTabLink().getAllDisplayNamesOfAclEntries().contains( "Anonymous User" );
    }

    def "GIVEN existing site with selected application opened WHEN application removed from the wizard THEN number of versions increased"()
    {
        given: "existing site with selected application opened"
        findAndSelectContent( SITE.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int before = allContentVersionsView.getAllVersions().size();
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        SiteFormViewPanel siteFormViewPanel = new SiteFormViewPanel( getSession() );

        when: "application removed"
        siteFormViewPanel.removeApp( MY_FIRST_APP );
        TestUtils.saveScreenshot( getSession(), "app_removed_from_wizard" );
        and: "the site saved"
        wizard.save();
        sleep( 1000 );

        and: "home button clicked"
        contentBrowsePanel.pressAppHomeButton();
        TestUtils.saveScreenshot( getSession(), "version_site_app_removed" );

        then: "number of versions increased after the removing of application in wizard"
        allContentVersionsView.getAllVersions().size() - before == 1;
    }

    def "GIVEN existing site without an application opened WHEN version of site with the application was restored THEN application present in the 'application selector'"()
    {
        given: "existing site without an application opened"
        findAndSelectContent( SITE.getName() );
        contentBrowsePanel.clickToolbarEdit();
        SiteFormViewPanel siteFormViewPanel = new SiteFormViewPanel( getSession() );
        contentBrowsePanel.pressAppHomeButton();

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version with an application restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        contentBrowsePanel.clickOnTab( INITIAL_DISPLAY_NAME );
        TestUtils.saveScreenshot( getSession(), "version_site_application_restored" );


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
        TestUtils.saveScreenshot( getSession(), "site_version_controller_added_wizard" );

        and: "the site saved"
        wizard.save();
        sleep( 1000 );

        and: "home button clicked"
        contentBrowsePanel.pressAppHomeButton();
        TestUtils.saveScreenshot( getSession(), "version_site_controller_added" );

        then: "number of versions increased after adding of controller in the wizard"
        allContentVersionsView.getAllVersions().size() - before == 1;
    }

    def "GIVEN existing site with controller WHEN version without selected controller restored THEN page controller not selected in the page editor"()
    {
        given: "existing site with selected application opened"
        findAndSelectContent( SITE.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        contentBrowsePanel.pressAppHomeButton();

        when: "version without a selected controller was restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 2 );
        versionItem.doRestoreVersion( versionItem.getId() );
        sleep( 1000 );
        contentBrowsePanel.clickOnTab( INITIAL_DISPLAY_NAME );
        sleep( 1000 );
        TestUtils.saveScreenshot( getSession(), "test_version_without_selected_controller" );

        then: "page controller not selected in the page editor and option filter input should be displayed"
        wizard.isPageDescriptorOptionsFilterDisplayed();
    }

}
