package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.SiteConfiguratorDialog
import com.enonic.autotests.pages.form.PageTemplateFormViewPanel
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared

@Ignore
class SiteConfiguratorDialog_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    String BACKGROUND_COLOR = "Red"

    @Shared
    String BACKGROUND_RED_COLOR_VALUE = "213, 147, 146"

    def "WHEN site with 2 applications is added THEN new site should be present"()
    {
        given: "site with 2 applications is added"
        SITE = buildSiteWithApps( SIMPLE_SITE_APP, MY_FIRST_APP );
        filterPanel.typeSearchText( SITE.getName() );

        when: "the site has been opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData( SITE );
        PageTemplateFormViewPanel pageTemplateFormViewPanel = new PageTemplateFormViewPanel( getSession() );

        and: "controller has been selected,(the site should be saved automatically)"
        pageTemplateFormViewPanel.selectPageController( COUNTRY_REGION_PAGE_CONTROLLER );

        and: "wizard has been closed"
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( SITE.getName() );

        then: "new site should be present"
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN existing site is opened WHEN the 'edit-button' in the application has been clicked THEN site configurator dialog should loaded"()
    {
        given: "site is opened"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        saveScreenshot( "site-config-icon2" );

        when: "edit button has been clicked"
        SiteConfiguratorDialog dialog = formViewPanel.openSiteConfigurationDialog( SIMPLE_SITE_APP );

        then: "dialog should be loaded"
        dialog.isOpened();

        and: "correct title is displayed"
        dialog.getTitle() == SIMPLE_SITE_APP;

        and: "Cancel Top button is displayed"
        dialog.isCancelTopButtonPresent();

        and: "'Cancel Bottom' button is displayed"
        dialog.isCancelBottomButtonPresent();

        and: "Apply button is displayed"
        dialog.isApplyButtonPresent();
    }

    def "GIVEN configurator dialog is opened WHEN red color was selected for the background THEN correct background color should be displayed in the page-editor"()
    {
        given: "site configurator dialog is opened"
        filterPanel.typeSearchText( SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog dialog = formViewPanel.openSiteConfigurationDialog( SIMPLE_SITE_APP );

        when: "red color for background has been selected"
        dialog.selectBackgroundColor( BACKGROUND_COLOR )
        saveScreenshot( "page-background-selected" );

        and: "changes were applied"
        dialog.doApply();
        saveScreenshot( "page-background-applied" );

        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        wizard.switchToLiveEditFrame();

        then: "correct background color should be in the page-editor"
        liveFormPanel.getBackgroundColor().contains( BACKGROUND_RED_COLOR_VALUE );
    }

    def "GIVEN configurator dialog is opened WHEN if something is changed and 'Cancel' button has been pressed THEN 'Confirmation Dialog' should not be present"()
    {
        given: "site configurator dialog is opened"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        saveScreenshot( "site-config-icon1" );
        SiteConfiguratorDialog dialog = formViewPanel.openSiteConfigurationDialog( SIMPLE_SITE_APP );

        when: "red color for background has been selected "
        dialog.selectBackgroundColor( "Blue" );

        and: "Cancel button has been pressed"
        dialog.clickOnCancelButtonTop();
        saveScreenshot( "site_config_confirm_dlg" );
        ConfirmationDialog confirmationDialog = new ConfirmationDialog( getSession() );

        then: "'Confirmation Dialog' should not be present"
        !confirmationDialog.isOpened();
    }
}
