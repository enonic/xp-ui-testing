package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.PageComponent
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Ignore
import spock.lang.Shared

class PageComponentsDialog_Spec
    extends BaseContentSpec
{

    @Shared
    Content PAGE_TEMPLATE;

    @Shared
    String COUNTRY_REGION_PAGE_CONTROLLER = "Country Region";

    @Shared
    String SITE_WITH_COMPONENTS_NAME = NameHelper.uniqueName( "page-component" );

    @Shared
    String FIRST_SITE_NAME = NameHelper.uniqueName( "my-site" );

    def "GIVEN existing site without a template WHEN site opened for edit and 'Show Component View' on wizard-toolbar clicked THEN 'Page Component dialog appears'"()
    {
        given: "existing Site based on 'My First App'"
        Content site = buildMySite( FIRST_SITE_NAME );
        addSiteBasedOnFirstApp( site );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_SITE_NAME ).clickToolbarEdit();
        wizard.showPageEditor();

        when: "site opened for edit and 'Show Component View' button clicked"
        wizard.showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );
        TestUtils.saveScreenshot( getSession(), "page-comp-dialog" );
        List<PageComponent> components = dialog.getPageComponents();

        then: "'Page Components View' dialog appeared on the wizard page"
        dialog.isOpened();

        and: "correct title displayed on dialog"
        dialog.getTextFromHeader().equals( PageComponentsViewDialog.DIALOG_HEADER );

        and: "correct displayed name of site shown"
        components.size() == 1;
        and: "'Automatic' component displayed, when there are no controllers"
        components.get( 0 ).getName().equals( "Automatic" );
    }

    def "GIVEN opened 'Page Components' Dialog WHEN button 'close' clicked THEN dialog not displayed"()
    {
        given: "opened 'Page Components' Dialog"
        contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_SITE_NAME ).clickToolbarEdit().showPageEditor().showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );

        when: "button 'close' clicked"
        dialog.doCloseDialog();
        TestUtils.saveScreenshot( getSession(), "page-comp-dialog-closed" );

        then: "dialog not displayed"
        !dialog.isOpened()
    }

    @Ignore
    def "test"()
    {
        given: "existing Site based on 'My First App'"
        Content site = buildMySite( SITE_WITH_COMPONENTS_NAME );
        addSiteBasedOnFirstApp( site );
        contentBrowsePanel.expandContent( ContentPath.from( SITE_WITH_COMPONENTS_NAME ) );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, "country", "country template",
                                           SITE_WITH_COMPONENTS_NAME );

        when: "'Templates' folder selected and new page-template added"
        contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            PAGE_TEMPLATE.getContentTypeName() ).showPageEditor().typeData( PAGE_TEMPLATE ).save().close( PAGE_TEMPLATE.getDisplayName() );
        sleep( 500 );

        then: "new page-template listed"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        contentBrowsePanel.exists( PAGE_TEMPLATE.getName() );
    }

    private void addSiteBasedOnFirstApp( Content site )
    {
        contentBrowsePanel.clickToolbarNew().selectContentType( site.getContentTypeName() ).typeData( site ).save().close(
            site.getDisplayName() );
        TestUtils.saveScreenshot( getSession(), site.getName() );
    }
}
