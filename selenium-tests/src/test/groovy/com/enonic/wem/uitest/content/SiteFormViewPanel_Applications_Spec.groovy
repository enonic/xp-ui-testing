package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class SiteFormViewPanel_Applications_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    def "GIVEN creating new Site with two configurations WHEN site saved and wizard closed THEN new site should be present"()
    {
        given: "creating new Site with two configurations"
        SITE = buildSiteWithApps( SIMPLE_SITE_APP, MY_FIRST_APP );

        when: "data saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData(
            SITE ).save().closeBrowserTab().switchToBrowsePanelTab();

        then: "new site should be present"
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN site with two applications WHEN reordering of applications applied AND site saved THEN new order of applications present in form-panel"()
    {
        given: "site with two applications opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        LinkedList<String> namesBefore = formViewPanel.getAppDisplayNames();

        when: "reordering of applications applied, site saved"
        formViewPanel.swapApplications( SIMPLE_SITE_APP, MY_FIRST_APP );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        saveScreenshot( SITE.getDisplayName() + "_closed" );

        and: "site opened again"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        LinkedList<String> namesAfter = formViewPanel.getAppDisplayNames();

        then: "new order of applications present in form-panel"
        namesBefore.getFirst() == namesAfter.getLast();
    }

    def "GIVEN site with two applications WHEN one application removed THEN only one application present in form-panel"()
    {
        given: "site with two application opened "
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        LinkedList<String> namesBefore = formViewPanel.getAppDisplayNames();

        when: "one application removed"
        formViewPanel.removeApp( MY_FIRST_APP )
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "opened in the wizard again"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        LinkedList<String> namesAfter = formViewPanel.getAppDisplayNames();

        then: "only one application present in form-panel"
        namesAfter.getLast() == SIMPLE_SITE_APP;
        and: "number of applications is reduced"
        namesBefore.size() - namesAfter.size() == 1;
    }

    def "GIVEN site with application WHEN checkbox clicked and 'Apply' button pressed THEN two application present in form-panel"()
    {
        given: "site with one application opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        LinkedList<String> namesBefore = formViewPanel.getAppDisplayNames();

        when: "checkbox for application clicked and 'Apply' button pressed"
        formViewPanel.selectCheckBoxAndApply( MY_FIRST_APP )
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        LinkedList<String> namesAfter = formViewPanel.getAppDisplayNames();

        then: "new application present in form"
        namesAfter.getLast() == MY_FIRST_APP;

        and: "number of application increased"
        namesAfter.size() - namesBefore.size() == 1;
    }
}
