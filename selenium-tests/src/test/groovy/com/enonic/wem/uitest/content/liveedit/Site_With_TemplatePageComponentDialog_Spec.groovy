package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ItemViewContextMenu
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.PageComponent
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Shared

class Site_With_TemplatePageComponentDialog_Spec
    extends BaseContentSpec
{

    @Shared
    Content PAGE_TEMPLATE;

    @Shared
    String SITE_NAME = NameHelper.uniqueName( "site" );

    @Shared
    Content SITE;

    def "add a site with a page template"()
    {
        given: "site based on test application added"
        SITE = buildMyFirstAppSite( SITE_NAME );
        addSiteBasedOnFirstApp( SITE );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, TEMPLATE_SUPPORTS_SITE, "test template", SITE_NAME );

        when: "page template added"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( SITE_NAME ) );
        contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            PAGE_TEMPLATE.getContentTypeName() ).showPageEditor().typeData( PAGE_TEMPLATE ).save().close( PAGE_TEMPLATE.getDisplayName() );
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );

        then: "template listed in a grid"
        contentBrowsePanel.exists( PAGE_TEMPLATE.getName() );
    }

    def "GIVEN existing site with a template WHEN site opened for edit and 'Show Component View' on wizard-toolbar clicked THEN 'Page Component dialog appears'"()
    {
        given: "existing Site based on 'My First App'"
        filterPanel.typeSearchText( SITE.getName() );

        when: "site opened for edit"
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();

        and: "'Show Component View' button pressed"
        wizard.showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );
        TestUtils.saveScreenshot( getSession(), "page-comp-dialog-templ" );
        List<PageComponent> components = dialog.getPageComponents();
        saveScreenshot( "page-comp-view-opened-templ" );

        then: "'Page Components View' opened"
        dialog.isOpened();

        and: "correct title displayed on view"
        dialog.getTextFromHeader().equals( PageComponentsViewDialog.DIALOG_HEADER );

        and: "one component displayed"
        components.size() == 2;
        and: "correct page template displayed"
        components.get( 0 ).getName().equals( PAGE_TEMPLATE.getDisplayName() );
        and: "correct component displayed"
        components.get( 1 ).getName().equals( "country" )
    }

    def "GIVEN opened a existing site WHEN 'Page Component View' shown AND menu-button clicked THEN context menu should be present"()
    {
        given: "opened a existing site"
        filterPanel.typeSearchText( SITE_NAME )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit();

        and: "'Page Components View' shown"
        wizard.unlockPageEditorAndSwitchToContentStudio().showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );

        when: "menu-button clicked"
        dialog.openMenu( PAGE_TEMPLATE.getDisplayName() );
        ItemViewContextMenu contextMenu = new ItemViewContextMenu( getSession() );

        then: "context menu is displayed"
        contextMenu.isOpened();
    }

    def "GIVEN opened a existing site and 'customize' menu item selected WHEN wizard closed THEN 'save before close dialog' displayed, because renderer was changed"()
    {
        given: "opened a existing site"
        filterPanel.typeSearchText( SITE_NAME )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit();

        and: "'Page Components View' shown"
        wizard.unlockPageEditorAndSwitchToContentStudio().showComponentView();


        when: "wizard closed"
        SaveBeforeCloseDialog dialog = wizard.close( SITE.getDisplayName() );

        then: "save before close dialog displayed, because renderer was changed"
        dialog != null;
    }

    def "GIVEN 'Page Component View' shown AND context menu displayed WHEN wizard closed THEN context menu closed as well "()
    {
        given: "existing site is opened"
        filterPanel.typeSearchText( SITE_NAME )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit();

        and: "page components view shown "
        wizard.unlockPageEditorAndSwitchToContentStudio().showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );

        and: "context-menu opened"
        dialog.openMenu( PAGE_TEMPLATE.getDisplayName() );
        ItemViewContextMenu contextMenu = new ItemViewContextMenu( getSession() );

        when: "site-wizard have been closed"
        wizard.save().close( SITE.getDisplayName() );
        saveScreenshot( "context-menu-closed-templ" );

        then: "context menu is not displayed"
        !contextMenu.isOpened();
    }

    def "GIVEN 'Page Component View' shown AND context menu displayed WHEN 'HomeButton' pressed THEN context menu is not displayed "()
    {
        given: "existing site have been opened"
        filterPanel.typeSearchText( SITE_NAME )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit();
        wizard.unlockPageEditorAndSwitchToContentStudio().showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );
        dialog.openMenu( PAGE_TEMPLATE.getDisplayName() );
        ItemViewContextMenu contextMenu = new ItemViewContextMenu( getSession() );

        when: "HomeButton clicked"
        contentBrowsePanel.pressAppHomeButton();
        saveScreenshot( "context-menu-home-template" );

        then: "context menu is not displayed"
        !contextMenu.isOpened();
    }

    def "GIVEN 'Page Components' view opened WHEN button 'close' clicked THEN dialog not displayed"()
    {
        given: "'Page Components' view opened"
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit().showPageEditor().showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );

        when: "button 'close' clicked"
        dialog.doCloseDialog();
        saveScreenshot( "page-comp-dialog-closed" );

        then: "'page component view' is not displayed"
        !dialog.isOpened()
    }
}
