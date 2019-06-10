package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ItemViewContextMenu
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
    Content SITE;

    def "add a site with a page template"()
    {
        given: "site based on test application added"
        SITE = buildMyFirstAppSite( "site" );
        addSite( SITE );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, TEMPLATE_SUPPORTS_SITE, "test template", SITE.getName() );

        when: "page template was added"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );
        contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            PAGE_TEMPLATE.getContentTypeName() ).showPageEditor().typeData( PAGE_TEMPLATE ).save().close( PAGE_TEMPLATE.getDisplayName() );
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );

        then: "template should be listed in the grid"
        contentBrowsePanel.exists( PAGE_TEMPLATE.getName() );
    }

    def "GIVEN existing site with a template WHEN site is opened and 'Show Component View' on wizard-toolbar was clicked THEN 'Page Component dialog appears'"()
    {
        given: "existing Site based on 'My First App'"
        filterPanel.typeSearchText( SITE.getName() );

        when: "the site is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();

        and: "'Show Component View' button was pressed"
        wizard.showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );
        saveScreenshot( "page-comp-dialog-templ" );
        List<PageComponent> components = dialog.getPageComponents();
        saveScreenshot( "page-comp-view-opened-templ" );

        then: "'Page Components View' should be opened"
        dialog.isOpened();

        and: "correct title should be displayed on the view"
        dialog.getTextFromHeader().equals( PageComponentsViewDialog.DIALOG_HEADER );

        and: "one component should be displayed"
        components.size() == 2;
        and: "correct 'page template' should be displayed"
        components.get( 0 ).getName().equals( PAGE_TEMPLATE.getDisplayName() );
        and: "correct component's name should be displayed"
        components.get( 1 ).getName().equals( "country" )
    }

    def "GIVEN existing site is opened WHEN 'Page Component View' shown AND menu-button clicked THEN context menu should be present"()
    {
        given: "existing site is opened"
        filterPanel.typeSearchText( SITE.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();

        and: "'Page Components View' is shown"
        wizard.unlockPageEditorAndSwitchToContentStudio().showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );

        when: "'Country Region' item has been clicked and the context menu was shown "
        dialog.openMenu( COUNTRY_REGION_PAGE_CONTROLLER );
        ItemViewContextMenu contextMenu = new ItemViewContextMenu( getSession() );

        then: "context menu should be displayed"
        contextMenu.isOpened();
    }

    def "GIVEN existing site is opened and 'customize' menu item was selected WHEN wizard has been closed THEN 'save before close dialog' should be displayed"()
    {
        given: "existing site is opened"
        filterPanel.typeSearchText( SITE.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit(); ;

        and: "'Page Components View' is shown"
        wizard.unlockPageEditorAndSwitchToContentStudio().showComponentView();

        when: "wizard has been closed"
        wizard.executeCloseWizardScript();

        then: "Alert dialog should appear, because renderer was changed"
        wizard.waitIsAlertDisplayed();
    }

    def "GIVEN 'Page Components' view is opened WHEN button 'close' was clicked THEN dialog should not be present"()
    {
        given: "'Page Components' view opened"
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit().showPageEditor().showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );

        when: "button 'close' was clicked"
        dialog.doCloseDialog();
        saveScreenshot( "page-comp-dialog-closed" );

        then: "'page component view' dialog should not be present"
        !dialog.isOpened()
    }
}
