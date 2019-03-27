package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.PageTemplateWidgetItemView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.context_window.PageInspectionPanel
import com.enonic.autotests.pages.form.liveedit.ContextWindow
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Create on 8/3/2017.
 * */
@Stepwise
class DetailsPanel_PageTemplateWidgetItemView_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    Content PAGE_TEMPLATE;

    @Shared
    String COUNTRY_LIST_CONTROLLER = "Country List";

    @Shared
    String COUNTRY_REGION_TITLE = "Country Region";

    @Shared
    String TEMPLATE_DISPLAY_NAME = "widget template";

    @Shared
    String SUPPORT_SITE = "site";


    def "WHEN image content is selected and details panel opened THEN PageTemplateWidget should be displayed AND 'Page Template is not used' should be present"()
    {
        when: "image content is selected"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );
        contentBrowsePanel.openContentDetailsPanel();
        PageTemplateWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPageTemplateWidgetItemView();

        then: "'Page Template Widget' should be displayed"
        view.isDisplayed();
        and: "'Page Template is not used' message should be displayed"
        view.isTemplateNotUsed();
        and:
        view.getTemplateNotUsedMessage() == PageTemplateWidgetItemView.TEMPLATE_NOT_USED_MESSAGE;
    }

    def "GIVEN existing site with a controller WHEN the site has been selected AND Details Panel opened THEN 'Custom' template should be displayed on the widget"()
    {
        given: "existing site with a controller"
        SITE = buildMyFirstAppSite( "site" );
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() )
        wizardPanel.typeData( SITE ).selectPageDescriptor( COUNTRY_REGION_TITLE ).closeBrowserTab().switchToBrowsePanelTab();

        when: "when the site has been selected and the details panel opened"
        findAndSelectContent( SITE.getName() ).openContentDetailsPanel();
        PageTemplateWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPageTemplateWidgetItemView();

        then: "correct controller should be displayed"
        view.getControllerName() == COUNTRY_REGION_TITLE;

        and: "'Custom' template should be displayed"
        view.getTemplateType() == "Custom";
    }

    def "GIVEN existing site with a controller AND new page template is added AND the template has been selected on the Inspection panel WHEN the site has been selected THEN new controller should be present on the widget"()
    {
        given: "existing site with a controller"
        findAndSelectContent( SITE.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_LIST_CONTROLLER, SUPPORT_SITE, TEMPLATE_DISPLAY_NAME,
                                           SITE.getName() );
        and: "new page template has been added"
        contentBrowsePanel.clickOnRowByName( "_templates" ).clickToolbarNew().selectContentType(
            PAGE_TEMPLATE.getContentTypeName() ).showPageEditor().typeData( PAGE_TEMPLATE ).close( PAGE_TEMPLATE.getDisplayName() );
        sleep(500);

        and: "Page tab-bar item has been clicked"
        ContentWizardPanel siteWizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        ContextWindow contextWindow = siteWizard.showContextWindow();
        contextWindow.clickOnTabBarItem( "Page" );
        PageInspectionPanel inspectionPanel = new PageInspectionPanel( getSession() );
        saveScreenshot( "page_templ_widget1" );

        and: "new added page template has been selected in the dropdown-options"
        inspectionPanel.selectTemplateOrController( TEMPLATE_DISPLAY_NAME );
        ConfirmationDialog confirmationDialog = new ConfirmationDialog( getSession() );

        and: "changes has been confirmed"
        confirmationDialog.pressYesButton();
        siteWizard.close( SITE.getDisplayName() );

        when: "details panel has been opened"
        contentBrowsePanel.openContentDetailsPanel();
        PageTemplateWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPageTemplateWidgetItemView();
        saveScreenshot( "page_templ_widget2" );

        then: "expected controller should be displayed"
        view.getControllerTextLink() == TEMPLATE_DISPLAY_NAME;

        and:
        view.getTemplateType() == "Page Template";
    }
}