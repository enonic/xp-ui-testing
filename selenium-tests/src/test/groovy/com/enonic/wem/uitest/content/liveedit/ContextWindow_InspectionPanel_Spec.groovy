package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContextWindowPageInspectionPanel
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
    Content TEST_SITE


    def "GIVEN creating of new site with selected controller WHEN Context window opened and 'Inspect' link clicked THEN Inspection tab activated"()
    {
        given: "creating of new site"
        TEST_SITE = buildMyFirstAppSite( "test-inspection-panel" );
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_SITE.getContentTypeName() ).typeData(
            TEST_SITE ).selectPageDescriptor( COUNTRY_REGION_PAGE_CONTROLLER ).save();

        when: "Context window opened and 'Inspect' link was pressed"
        wizardPanel.showContextWindow().clickOnInspectLink();
        ContextWindowPageInspectionPanel inspectionPanel = new ContextWindowPageInspectionPanel( getSession() );
        saveScreenshot( "test-inspection-tab-activated" );

        then: "Inspection tab activated"
        inspectionPanel.isDisplayed();

        and: "template selector displayed "
        inspectionPanel.isPageTemplateSelectorDisplayed();

        and: "page controller is displayed, because page controller was selected"
        inspectionPanel.isPageControllerSelectorDisplayed()
    }

    def "GIVEN the site with 'page controller' selected  WHEN 'Preview' button pressed THEN page-sources are correct and correct header present as well"()
    {
        given: "the site with 'page controller' selected"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow( TEST_SITE.getName() ).clickToolbarEdit();

        when: "'Preview' button pressed"
        wizardPanel.clickToolbarPreview();

        then: "page source of new opened tab in a browser is not empty"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_PAGE_CONTROLLER );
        source != null;

        and: "page-sources are correct and correct html-header present as well"
        source.contains( COUNTRY_SITE_HTML_HEADER );
    }

    def "GIVEN 'Custom' renderer selected WHEN region selected THEN correct page controller displayed in the selector"()
    {
        given: "'Inspect' panel opened ana 'Custom' renderer selected"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow( TEST_SITE.getName() ).clickToolbarEdit();
        wizardPanel.showContextWindow().clickOnInspectLink();
        ContextWindowPageInspectionPanel inspectPanel = new ContextWindowPageInspectionPanel( getSession() );

        when: "'Country' page controller selected"
        inspectPanel.changePageController( COUNTRY_LIST_PAGE_CONTROLLER );
        saveScreenshot( "test-inspection-new-controller-selected" );

        then: "correct page controller displayed in the selector"
        inspectPanel.getSelectedPageController() == COUNTRY_LIST_PAGE_CONTROLLER;
    }
    //verifies :XP-3993 Inspection Panel should be closed, when 'Page Controller' was removed (Automatic)
    def "GIVEN 'Inspect' panel opened WHEN 'Automatic' renderer selected THEN 'Page Controller' selector appears"()
    {
        given: "'Inspect' panel opened"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow( TEST_SITE.getName() ).clickToolbarEdit()
        wizardPanel.showContextWindow().clickOnInspectLink();

        when: "'Automatic' renderer selected"
        ContextWindowPageInspectionPanel inspectionPanel = new ContextWindowPageInspectionPanel( getSession() );
        inspectionPanel.selectRenderer( "Automatic" );
        inspectionPanel.waitUntilPanelClosed( Application.EXPLICIT_NORMAL );

        then: "'Context window has been closed "
        !inspectionPanel.isDisplayed();
    }
}
