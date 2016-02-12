package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContextWindowPageInspectionPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContextWindow_InspectionPanel_Spec
    extends BaseContentSpec
{
    @Shared
    String SITE_NAME = NameHelper.uniqueName( "inspection" );

    @Shared
    Content SITE

    def "setup: add a site"()
    {
        when: "site based on 'My First App'"
        SITE = buildMyFirstAppSite( SITE_NAME );
        addSiteBasedOnFirstApp( SITE );

        TestUtils.saveScreenshot( getSession(), "inspection-site" );
        then: "it present in browse panel"
        contentBrowsePanel.exists( SITE_NAME );
    }

    def "GIVEN 'Page Editor' opened WHEN 'Inspect' link clicked THEN inspection panel is displayed"()
    {
        given: "'Page Editor' for the existing site opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit()
        wizardPanel.showPageEditor();

        when: "'Inspect' link clicked"
        ContextWindowPageInspectionPanel inspectPanel = new ContextWindowPageInspectionPanel( getSession() );
        wizardPanel.showContextWindow().clickOnInspectLink();
        TestUtils.saveScreenshot( getSession(), "inspect-opened" );

        then: "'inspect panel' is displayed"
        inspectPanel.isDisplayed()
    }

    def "GIVEN 'Inspect' panel opened WHEN 'Custom' renderer selected THEN 'Page Controller' selector appears"()
    {
        given: "'Inspect' panel opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit()
        wizardPanel.showPageEditor().showContextWindow().clickOnInspectLink();

        when: "'Custom' renderer selected"
        ContextWindowPageInspectionPanel inspectPanel = new ContextWindowPageInspectionPanel( getSession() );
        inspectPanel.selectRenderer( "Custom" )
        TestUtils.saveScreenshot( getSession(), "renderer-appears" );

        then: "'Page Controller' selector appears"
        inspectPanel.isPageControllerSelectorDisplayed()
    }

    def "GIVEN 'Custom' renderer selected WHEN region selected THEN correct page controller displayed in the selector"()
    {
        given: "'Inspect' panel opened ana 'Custom' renderer selected"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit()
        wizardPanel.showPageEditor().showContextWindow().clickOnInspectLink();
        ContextWindowPageInspectionPanel inspectPanel = new ContextWindowPageInspectionPanel( getSession() );
        inspectPanel.selectRenderer( "Custom" )

        when: "'Country' page controller selected"
        inspectPanel.selectPageController( COUNTRY_REGION_PAGE_CONTROLLER );
        TestUtils.saveScreenshot( getSession(), "controller-is-country" );

        then: "correct page controller displayed in the selector"
        inspectPanel.getSelectedPageController() == COUNTRY_REGION_PAGE_CONTROLLER;
    }

    def "GIVEN the site with 'page controller' selected  WHEN 'Preview' button pressed THEN page-sources are correct and correct header present as well"()
    {
        given: "the site with 'page controller' selected"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit();

        when: "'Preview' button pressed"
        wizardPanel.clickToolbarPreview();

        then: "page-sources are correct and correct html-header present as well"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_PAGE_CONTROLLER );
        source.contains( COUNTRY_SITE_HTML_HEADER );
    }
}
