package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.context_window.PageInspectionPanel
import com.enonic.autotests.pages.form.liveedit.ComponentMenuItems
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 8/15/2017.
 *
 * verifies : #5393 Page Template Wizard - 'Save as Template' menu item should not be displayed
 * */
@Stepwise
class PageComponentView_Save_As_Template_Spec
    extends BaseSiteSpec
{
    @Shared
    Content SITE

    def "GIVEN existing site with a controller is opened AND PageComponent is opened WHEN the controller in PageComponentView has been clicked THEN 'Save as Template' menu item should be displayed"()
    {
        given:
        SITE = buildMyFirstAppSite( "site" );
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData( SITE ).selectPageDescriptor(
            COUNTRY_LIST_CONTROLLER ).closeBrowserTab().switchToBrowsePanelTab();

        when: "Page Component View has been opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();
        and: "context menu for the controller has been opened"
        pageComponentsView.openMenu( COUNTRY_LIST_CONTROLLER );
        saveScreenshot( "save_as_template_menu_item" )

        then: "'Save as Template' menu item should be displayed"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.SAVE_AS_TEMPLATE.getValue() );

        and: "'Inspect' menu item should be displayed"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.INSPECT.getValue() );

        and: "'Reset' menu item should be displayed"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.RESET.getValue() );
    }

    def "GIVEN wizard for page-template is opened WHEN root item in PageComponentView has been clicked  THEN 'Save as Template' menu item should not be displayed"()
    {
        given: "existing site"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );

        and: "new template has been added"
        Content template = buildPageTemplate( COUNTRY_LIST_CONTROLLER, TEMPLATE_SUPPORTS_SITE, "test statistics template",
                                              SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            template.getContentTypeName() ).showPageEditor().typeData( template );

        when: "Page Component View has been opened"
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView().openMenu( COUNTRY_LIST_CONTROLLER );
        saveScreenshot( "context_menu_template" );

        then: "'Save as Template' menu item should be displayed"
        !pageComponentsView.isMenuItemPresent( ComponentMenuItems.SAVE_AS_TEMPLATE.getValue() );

        and: "'Inspect' menu item should be displayed"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.INSPECT.getValue() );

        and: "'Reset' menu item should be displayed"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.RESET.getValue() );
    }

    def "GIVEN existing site is opened AND PageComponentView is opened WHEN 'Save as Template' menu item has been clicked THEN new template should be added"()
    {
        given: "existing site is opened AND PageComponentView is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();
        and: "context menu for the controller has been opened"
        pageComponentsView.openMenu( COUNTRY_LIST_CONTROLLER );

        when: "'the 'country list' component has been selected and  Save as Template' menu-item clicked"
        pageComponentsView.selectMenuItem( ComponentMenuItems.SAVE_AS_TEMPLATE.getValue() );
        and: "go to the browse panel"
        wizard.switchToBrowsePanelTab();

        then: "new template should be listed in the grid"
        saveScreenshot( "saved_template" );
        contentBrowsePanel.exists( "template-" + SITE.getName() );
    }

    def "GIVEN existing site is opened AND PageComponentView is opened AND component is selected AND Inspection Panel is opened WHEN 'Save as Template' button has been clicked THEN new template should be added"()
    {
        given: "existing site is opened AND PageComponentView is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();
        and: "select the controller on the PageComponentView"
        pageComponentsView.clickOnComponent( COUNTRY_LIST_CONTROLLER );

        when: "PageInspectionPanel is loaded"
        PageInspectionPanel inspectionPanel = new PageInspectionPanel( getSession() );
        inspectionPanel.waitForLoaded();
        and: "'Save as Template' button has been clicked"
        inspectionPanel.clickOnSaveAsTemplateButton();
        and: "go to the browse panel"
        wizard.switchToBrowsePanelTab();

        then: "new template should be listed in the grid"
        saveScreenshot( "saved_template2" );
        contentBrowsePanel.exists( "template-" + SITE.getName() + "-1" );
    }
}
