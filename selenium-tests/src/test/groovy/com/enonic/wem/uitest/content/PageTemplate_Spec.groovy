package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.PageTemplateFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 4/5/2017.
 *
 * Verifies:
 * Page Template Wizard- button 'Edit' in the support-selected option does not open the content in new wizard tab #4745
 * */
@Stepwise
class PageTemplate_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_SITE;

    @Shared
    Content TEST_TEMPLATE;

    def "GIVEN site has been added WHEN 'page template' wizard is opened THEN ContentTypeFilter(support) should be present on the page"()
    {
        given: "site has been added"
        TEST_SITE = buildMyFirstAppSite( "test-site" );
        addSite( TEST_SITE );

        and: "the site is expanded"
        filterPanel.typeSearchText( TEST_SITE.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( TEST_SITE.getName() ) );
        and: "_templates folder is selected and 'New' button pressed"
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            "Page Template" );
        PageTemplateFormViewPanel formViewPanel = new PageTemplateFormViewPanel( getSession() );

        when: "'Support' combobox should be displayed"
        formViewPanel.isSupportOptionFilterDisplayed();

        then: "Live Edit frame should be displayed on the wizard page"
        wizard.isLiveEditFrameDisplayed();
        and: "Page-template step should be present"
        wizard.isWizardStepPresent( "Page Template" );
    }

    def "GIVEN existing site WHEN 'page template' wizard is opened AND display name typed AND Save button pressed THEN red icon should be displayed on the wizard"()
    {
        given: "site has been added"
        filterPanel.typeSearchText( TEST_SITE.getName() );
        and: "the site is expanded"

        contentBrowsePanel.expandContent( ContentPath.from( TEST_SITE.getName() ) );

        and: "_templates folder is selected and 'New' button pressed"
        TEST_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, null, "not-valid",
                                           TEST_SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            "Page Template" );

        when: "display name has been typed"
        wizard.typeData( TEST_TEMPLATE );
        //wizard.clickOnMinimizeEditIcon();
        sleep( 500 );
        saveScreenshot( "support_not_selected" );

        then: "red icon should be displayed on the wizard, the content is not valid"
        wizard.isContentInvalid();

        and:"Save button should be disabled, because auto-saving should be performed when the controller has been selected"
        !wizard.isSaveButtonEnabled(  );
    }

    def "GIVEN existing page-template('support' is not selected) EXPECT the template should be displayed in the grid as not valid"()
    {
        given: "existing page-template('support' is not selected)"
        findAndSelectContent( TEST_TEMPLATE.getName() );

        expect: "the template should be displayed in the grid as not valid"
        contentBrowsePanel.isContentInvalid( TEST_TEMPLATE.getName() );
    }

    def "GIVEN existing site WHEN the page-template is opened AND 'site' has been selected from the support options THEN the content should be displayed as valid in the grid"()
    {
        given: "existing not valid template"
        ContentWizardPanel wizard = findAndSelectContent( TEST_TEMPLATE.getName() ).clickToolbarEdit();
        PageTemplateFormViewPanel formViewPanel = new PageTemplateFormViewPanel( getSession() );

        when: "'site' has been selected from the support options"
        formViewPanel.selectSupportOption( TEMPLATE_SUPPORTS_SITE );
        wizard.save().switchToBrowsePanelTab().waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        sleep( 1000 );

        then: "the template should be displayed in the grid as valid"
        !contentBrowsePanel.isContentInvalid( TEST_TEMPLATE.getName() );
    }

    def "GIVEN existing site WHEN page-template wizard is opened AND name and 'support' has been typed THEN the content should be displayed as valid in the wizard"()
    {
        given: "site has been added"
        filterPanel.typeSearchText( TEST_SITE.getName() );
        and: "the site is expanded"
        contentBrowsePanel.expandContent( ContentPath.from( TEST_SITE.getName() ) );

        and: "_templates folder is selected and 'New' button pressed"
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            "Page Template" );
        PageTemplateFormViewPanel formViewPanel = new PageTemplateFormViewPanel( getSession() );

        when: "display name has been typed"
        wizard.typeDisplayName( "test display name" );

        and: "'support' has been selected"
        formViewPanel.selectSupportOption( TEMPLATE_SUPPORTS_SITE );
        saveScreenshot( "support_is_selected" )

        then: "red icon should not be displayed on the wizard, the content is valid"
        !wizard.isContentInvalid();
    }

    def "GIVEN existing page-template is opened WHEN 'support' has been removed THEN the content is getting not valid"()
    {
        given: "existing page-template is opened"
        ContentWizardPanel wizard = findAndSelectContent( TEST_TEMPLATE.getName() ).clickToolbarEdit();
        PageTemplateFormViewPanel formViewPanel = new PageTemplateFormViewPanel( getSession() );

        when: "'support'-option has been removed"
        formViewPanel.removeSupportOption( "Site" );
        wizard.save();
        saveScreenshot( "support_option_removed" )

        then: "red icon should be displayed on the wizard, the content is invalid"
        wizard.isContentInvalid();
    }

    def "GIVEN existing page template with several versions WHEN the version with the selected 'support option' is restored THEN 'Site' content type should be selected"()
    {
        given: "existing page template with several versions"
        ContentWizardPanel wizard = findAndSelectContent( TEST_TEMPLATE.getName() ).clickToolbarEdit();
        PageTemplateFormViewPanel formViewPanel = new PageTemplateFormViewPanel( getSession() );
        wizard.switchToBrowsePanelTab();

        when: "version panel is opened"
        VersionHistoryWidget allContentVersionsView = openVersionPanel();
        and: "the version with selected 'support' option has been expanded"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        and: "the version is reverted"
        versionItem.doRevertVersion();
        contentBrowsePanel.switchToBrowserTabByTitle( TEST_TEMPLATE.getDisplayName() );

        then: "'support' option filter should be displayed"
        formViewPanel.isSupportOptionFilterDisplayed();

        and: "'Site' content type should be selected"
        formViewPanel.getSelectedContentTypes().contains( "Site" );
    }
}
