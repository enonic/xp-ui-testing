package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.context_window.PageInspectionPanel
import com.enonic.autotests.pages.form.liveedit.ContextWindow
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


    def "GIVEN creating of new site with selected controller WHEN Context window opened and 'Inspect' link clicked THEN Inspection tab should be opened"()
    {
        given: "creating of new site"
        TEST_SITE = buildMyFirstAppSite( "test-inspection-panel" );
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_SITE.getContentTypeName() ).typeData(
            TEST_SITE ).selectPageDescriptor( COUNTRY_REGION_PAGE_CONTROLLER );

        when: "Context window opened and 'Inspect' link was pressed"
        ContextWindow contextWindow = wizardPanel.showContextWindow();
        contextWindow.clickOnTabBarItem( "Page" );
        PageInspectionPanel inspectionPanel = new PageInspectionPanel( getSession() );
        saveScreenshot( "test-inspection-tab-activated" );

        then: "Inspection tab is opened"
        inspectionPanel.isDisplayed();

        and: "page-template drop down menu is displayed "
        inspectionPanel.isPageTemplateSelectorDisplayed();

        and: "page controller is displayed, because page controller was selected"
        inspectionPanel.isPageControllerSelectorDisplayed();

        and: "'Save as Template' button should be present"
        inspectionPanel.isSaveAsTemplateButtonDisplayed();
    }

    def "GIVEN the site with 'page controller' is opened WHEN 'Preview' button pressed THEN page-sources are correct and correct header present as well"()
    {
        given: "the site with a 'page controller' is opened"
        ContentWizardPanel wizardPanel = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();

        when: "'Preview' button pressed"
        wizardPanel.clickToolbarPreview();

        then: "page source of new opened tab in a browser is not empty"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_PAGE_CONTROLLER );
        source != null;

        and: "page-sources are correct and correct html-header present as well"
        source.contains( COUNTRY_SITE_HTML_HEADER );
    }

    def "GIVEN 'Custom' renderer is selected WHEN Page controller has been changed to 'Country list' THEN correct page controller displayed in the selector"()
    {
        given: "existing site with the selected page controller is opened"
        ContentWizardPanel wizardPanel = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();

        and: "'Page' tab bar item has been clicked"
        ContextWindow contextWindow = wizardPanel.showContextWindow();
        contextWindow.clickOnTabBarItem( "Page" );
        PageInspectionPanel inspectPanel = new PageInspectionPanel( getSession() );

        when: "Page controller has been changed to 'Country list'"
        inspectPanel.changePageController( COUNTRY_LIST_PAGE_CONTROLLER );

        and: "'Yes' button on the Confirmation dialog has been pressed"
        ConfirmationDialog dialog = new ConfirmationDialog( getSession() );
        dialog.pressYesButton();
        saveScreenshot( "test-inspection-new-controller-selected" );

        then: "correct page controller displayed on the Inspect panel"
        inspectPanel.getSelectedPageController() == COUNTRY_LIST_PAGE_CONTROLLER;
    }

    def "GIVEN 'Inspect' panel is opened WHEN 'Automatic' renderer was selected THEN 'Confirmation Dialog' should be displayed"()
    {
        given: "'Inspect' panel is opened"
        ContentWizardPanel wizardPanel = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();
        ContextWindow contextWindow = wizardPanel.showContextWindow();
        contextWindow.clickOnTabBarItem( "Page" );

        when: "'Automatic' page-template has been selected"
        PageInspectionPanel inspectionPanel = new PageInspectionPanel( getSession() );
        inspectionPanel.selectRenderer( "Automatic" );

        then: "'Confirmation Dialog' should be displayed "
        ConfirmationDialog dialog = new ConfirmationDialog( getSession() );
        dialog.isOpened();

        and: "correct question should be displayed"
        dialog.getQuestion() == "Switching to a page template will discard all of the custom changes made to the page. Are you sure?"
    }
    //verifies :XP-3993 Inspection Panel should be closed, when 'Page Controller' was removed (Automatic)
    def "GIVEN 'Inspect' panel is opened WHEN 'Automatic' renderer was selected THEN 'Context window' should be closed"()
    {
        given: "'Inspect' panel is opened"
        ContentWizardPanel wizardPanel = findAndSelectContent( TEST_SITE.getName() ).clickToolbarEdit();
        ContextWindow contextWindow = wizardPanel.showContextWindow();
        contextWindow.clickOnTabBarItem( "Page" )

        when: "'Automatic' page-template has been selected"
        PageInspectionPanel inspectionPanel = new PageInspectionPanel( getSession() );
        inspectionPanel.selectRenderer( "Automatic" );

        and: "'Confirmation Dialog' is displayed "
        ConfirmationDialog dialog = new ConfirmationDialog( getSession() );
        dialog.isOpened();

        and: "Yes' button has been pressed"
        dialog.pressYesButton();
        inspectionPanel.waitUntilPanelClosed( Application.EXPLICIT_NORMAL );

        then: "'Context window should be closed "
        !inspectionPanel.isDisplayed();
    }
}
