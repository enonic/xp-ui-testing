package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.PageTemplateWidgetItemView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

/**
 * Create on 8/3/2017.
 *
 * Tasks:xp-ui-testing#72 Add Selenium tests for PageTemplateWidgetItemView(Det. Panel)
 * */
class DetailsPanel_PageTemplateWidgetItemView_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    String COUNTRY_REGION_TITLE = "Country Region";

    def "WHEN image content is selected and details panel opened THEN PageTemplateWidgetItemView is displayed and 'Page Template is not used' should be present"()
    {
        when: "image content is selected"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );
        contentBrowsePanel.clickOnDetailsToggleButton();
        PageTemplateWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPageTemplateWidgetItemView();

        then: "'Page Template Widget' should be displayed"
        view.isDisplayed();
        and: "'Page Template is not used' message should be displayed"
        view.isTemplateNotUsed();
        and:
        view.getTemplateNotUsedMessage() == PageTemplateWidgetItemView.TEMPLATE_NOT_USED_MESSAGE;
    }

    def "GIVEN existing site with a controller WHEN the site is selected AND Details Panel opened THEN 'Custom' template should be displayed on the widget"()
    {
        given: "existing site with a controller"
        SITE = buildMyFirstAppSite( "site" );
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() )
        wizardPanel.typeData( SITE ).selectPageDescriptor( COUNTRY_REGION_TITLE ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "when the site has been selected and the details panel opened"
        findAndSelectContent( SITE.getName() ).clickOnDetailsToggleButton();
        PageTemplateWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPageTemplateWidgetItemView();

        then: "correct controller should be displayed"
        view.getControllerName() == COUNTRY_REGION_TITLE;

        and: "'Custom' template should be displayed"
        view.getTemplateType() == "Custom";
    }
}