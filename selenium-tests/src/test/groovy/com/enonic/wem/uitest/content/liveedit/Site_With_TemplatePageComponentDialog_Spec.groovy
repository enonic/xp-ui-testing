package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ItemViewContextMenu
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.PageComponent
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Site_With_TemplatePageComponentDialog_Spec
    extends BaseContentSpec
{

    @Shared
    Content PAGE_TEMPLATE;

    @Shared
    Content SITE;

    def "Preconditions :add a site with a page template"()
    {
        given: "site based on test application added"
        SITE = buildMyFirstAppSite( "site" );
        addSite( SITE );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, TEMPLATE_SUPPORTS_SITE, "test template", SITE.getName() );

        when: "page template was added"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );
        contentBrowsePanel.selectContentInGrid( "_templates" ).clickToolbarNew().selectContentType(
            PAGE_TEMPLATE.getContentTypeName() ).showPageEditor().typeData( PAGE_TEMPLATE ).close( PAGE_TEMPLATE.getDisplayName() );
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );

        then: "template should be listed in the grid"
        contentBrowsePanel.exists( PAGE_TEMPLATE.getName() );
    }

    def "GIVEN existing site with a template is opened WHEN 'Show Component View' button has been clicked THEN 'Page Component dialog appears'"()
    {
        given: "existing site is filtered"
        filterPanel.typeSearchText( SITE.getName() );

        when: "the site is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();

        and: "'Show Component View' button has been pressed"
        wizard.showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );
        saveScreenshot( "page-comp-dialog-templ" );
        List<PageComponent> components = dialog.getPageComponents();
        saveScreenshot( "page-comp-view-opened-templ" );

        then: "'Page Components View' should be opened"
        dialog.isOpened();

        and: "correct title should be displayed in the dialog"
        dialog.getTextFromHeader() == PageComponentsViewDialog.DIALOG_HEADER;

        and: "one component should be displayed"
        components.size() == 2;
        and: "correct 'page template' should be displayed"
        components.get( 0 ).getName().equals( PAGE_TEMPLATE.getDisplayName() );
        and: "correct component's name should be displayed"
        components.get( 1 ).getName().equals( "country" );
    }

    def "GIVEN existing site is opened WHEN 'Page Component View' shown AND menu-button clicked THEN context menu should be present"()
    {
        given: "existing site is opened"
        filterPanel.typeSearchText( SITE.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        sleep( 2000 );

        and: "'Page Components View' is shown"
        wizard.unlockPageEditorAndSwitchToContentStudio().showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );

        when: "'Country Region' item has been clicked and the context menu was shown "
        dialog.openMenu( COUNTRY_REGION_PAGE_CONTROLLER );
        ItemViewContextMenu contextMenu = new ItemViewContextMenu( getSession() );

        then: "context menu should be displayed"
        contextMenu.isOpened();
    }

    def "GIVEN 'Page Components' view is opened WHEN button 'close' has been clicked THEN dialog should be closed"()
    {
        given: "'Page Components' view opened"
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit().showPageEditor().showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );

        when: "button 'close' has been clicked"
        dialog.doCloseDialog();
        saveScreenshot( "page-comp-dialog-closed" );

        then: "'page component view' dialog should be closed"
        !dialog.isOpened()
    }
}
