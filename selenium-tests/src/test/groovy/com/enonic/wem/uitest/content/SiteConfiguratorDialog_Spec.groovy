package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.SiteConfiguratorDialog
import com.enonic.autotests.pages.form.PageTemplateFormViewPanel
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

/**
 *
 **/
class SiteConfiguratorDialog_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    String BACKGROUND_COLOR = "Red"

    @Shared
    String BACKGROUND_RED_COLOR_VALUE = "213, 147, 146"

    def "GIVEN creating of new Site with 2 applications WHEN site was saved and wizard closed THEN new site should be present"()
    {
        given: "creating of new Site with 2 applications"
        SITE = buildSiteWithApps( SIMPLE_SITE_APP, MY_FIRST_APP );

        when: "data saved and wizard closed"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData( SITE );
        PageTemplateFormViewPanel pageTemplateFormViewPanel = new PageTemplateFormViewPanel( getSession() );

        and: "controller selected"
        pageTemplateFormViewPanel.selectPageController( COUNTRY_REGION_PAGE_CONTROLLER );

        and: "wizard was closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        then: "new site should be present"
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN existing site is opened WHEN the 'edit-button' on the selected application was clicked THEN configurator dialog with correct title should appear"()
    {
        given: "site is opened"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );

        when: "edit button was clicked"
        SiteConfiguratorDialog dialog = formViewPanel.openSiteConfigurationDialog( SIMPLE_SITE_APP );

        then: "dialog is not null"
        dialog != null;

        and: "dialog should be opened"
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
        given: "site was opened and configurator dialog is opened"
        filterPanel.typeSearchText( SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog dialog = formViewPanel.openSiteConfigurationDialog( SIMPLE_SITE_APP );

        when: "red color for background was selected "
        dialog.selectBackgroundColor( BACKGROUND_COLOR )
        saveScreenshot( "page-background-selected" );

        and: "changes were applied"
        dialog.doApply();
        saveScreenshot( "page-background-applied" );

        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        wizard.switchToLiveEditFrame();

        then: "correct background color present in the page-editor"
        liveFormPanel.getBackgroundColor().contains( BACKGROUND_RED_COLOR_VALUE );
    }

    def "GIVEN configurator dialog is opened WHEN if something is changed and 'Cancel' button has been pressed THEN 'Confirmation Dialog' should not be present"()
    {
        given: "site was opened and configurator dialog is opened"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog dialog = formViewPanel.openSiteConfigurationDialog( SIMPLE_SITE_APP );

        when: "red color for background was selected "
        dialog.selectBackgroundColor( "Blue" );

        and: "Cancel button has been pressed"
        dialog.clickOnCancelButtonTop();
        saveScreenshot( "site_config_confirm_dlg" );
        ConfirmationDialog confirmationDialog = new ConfirmationDialog( getSession() );

        then: "'Confirmation Dialog' should not be present"
        !confirmationDialog.isOpened();
    }
}
