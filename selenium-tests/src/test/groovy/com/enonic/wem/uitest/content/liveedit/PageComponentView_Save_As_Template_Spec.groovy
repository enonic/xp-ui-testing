package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ComponentMenuItems
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 8/15/2017.
 *
 * Tasks: xp-ui-testing#74 Add Selenium tests for 'Save as Template' menu item
 *
 * verifies : #5393 Page Template Wizard - 'Save as Template' menu item should not be displayed
 * */
@Stepwise
class PageComponentView_Save_As_Template_Spec
    extends BaseSiteSpec
{
    @Shared
    Content SITE

    def "GIVEN existing site with a controller AND the site has been opened and PageComponent shown WHEN the controller has been clicked THEN 'Save as Template' menu item should be displayed"()
    {
        given:
        SITE = buildMyFirstAppSite( "site" );
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData( SITE ).selectPageDescriptor(
            COUNTRY_LIST_CONTROLLER ).save().closeBrowserTab().switchToBrowsePanelTab();

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

    def "GIVEN existing site WHEN wizard for page-template has been opened"()
    {
        given:
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );

        and: "new template has been added"
        Content template = buildPageTemplate( COUNTRY_LIST_CONTROLLER, TEMPLATE_SUPPORTS_SITE, "test statistics template",
                                              SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            template.getContentTypeName() ).showPageEditor().typeData( template ).save();

        when: ""
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView().openMenu( COUNTRY_LIST_CONTROLLER );
        saveScreenshot( "context_menu_template" );

        then: "'Save as Template' menu item should not be displayed"
        !pageComponentsView.isMenuItemPresent( ComponentMenuItems.SAVE_AS_TEMPLATE.getValue() );

        and: "'Inspect' menu item should be displayed"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.INSPECT.getValue() );

        and: "'Reset' menu item should be displayed"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.RESET.getValue() );
    }
}
