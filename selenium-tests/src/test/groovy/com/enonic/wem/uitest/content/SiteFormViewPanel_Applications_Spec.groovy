package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
class SiteFormViewPanel_Applications_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    def "GIVEN creating of new site with two applications WHEN site saved and wizard closed THEN new site should be present"()
    {
        given: "creating of new site with two applications"
        SITE = buildSiteWithApps( SIMPLE_SITE_APP, MY_FIRST_APP );

        when: "data saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData(
            SITE ).closeBrowserTab().switchToBrowsePanelTab();

        then: "new site should be present"
        contentBrowsePanel.getFilterPanel().typeSearchText( SITE.getName() );
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN site with two applications is opened WHEN reordering of applications applied AND site was saved THEN new order of applications should be displayed on the form-panel"()
    {
        given: "site with two applications is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        LinkedList<String> namesBefore = formViewPanel.getAppDisplayNames();

        when: "reordering of applications applied, site was saved"
        formViewPanel.swapApplications( SIMPLE_SITE_APP, MY_FIRST_APP );
        saveScreenshot( "app_swapped1" );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "site is opened again"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        LinkedList<String> namesAfter = formViewPanel.getAppDisplayNames();
        saveScreenshot( "app_swapped2" );

        then: "new order of applications should be present on the form-panel"
        namesBefore.getFirst() == namesAfter.getLast();
    }

    def "GIVEN site with two applications is opened WHEN one application was removed THEN only one application should be displayed"()
    {
        given: "site with two application opened "
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        LinkedList<String> namesBefore = formViewPanel.getAppDisplayNames();

        when: "one application was removed"
        formViewPanel.removeApp( MY_FIRST_APP );
        and: "site was saved and closed"
        wizard.closeBrowserTab().switchToBrowsePanelTab();

        and: "site is opened"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        LinkedList<String> namesAfter = formViewPanel.getAppDisplayNames();

        then: "only one application should be present on the  form-panel"
        namesAfter.getLast() == SIMPLE_SITE_APP;
        and: "number of applications is reduced"
        namesBefore.size() - namesAfter.size() == 1;
    }

    def "GIVEN site with selected application is opened WHEN an application has been filtered AND app-checkbox has been clicked and 'Apply' button pressed THEN two application should be present on the form-panel"()
    {
        given: "site with one application is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        LinkedList<String> namesBefore = formViewPanel.getAppDisplayNames();

        when: "name of an application has been typed in the options=filter and app-checkbox has been clicked and 'Apply' button pressed"
        formViewPanel.clickOnAppCheckBoxAndDoApply( MY_FIRST_APP )
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        LinkedList<String> namesAfter = formViewPanel.getAppDisplayNames();

        then: "new application should appear on the form"
        namesAfter.getLast() == MY_FIRST_APP;

        and: "number of application should increase"
        namesAfter.size() - namesBefore.size() == 1;
    }
}
