package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewContextMenu
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.PageComponent
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Site_Without_PageTemplate_PageComponentsDialog_Spec
    extends BaseContentSpec
{

    @Shared
    String SITE_NAME = NameHelper.uniqueName( "my-site" );

    @Shared
    Content SITE;


    def "GIVEN existing site without a template WHEN site opened for edit and 'Show Component View' on wizard-toolbar clicked THEN 'Page Component dialog appears'"()
    {
        given: "existing Site without a template"
        SITE = buildMyFirstAppSite( SITE_NAME );
        addSiteBasedOnFirstApp( SITE );

        and: "site opened for edit"
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit();
        and: "'Show Page Editor' button pressed"
        wizard.showPageEditor();

        when: "'Show Component View' button pressed"
        wizard.showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );
        TestUtils.saveScreenshot( getSession(), "page-comp-dialog" );
        List<PageComponent> components = dialog.getPageComponents();
        TestUtils.saveScreenshot( getSession(), "page-comp-view-opened" );

        then: "'Page Components View' opened"
        dialog.isOpened();

        and: "correct title displayed on view"
        dialog.getTextFromHeader().equals( PageComponentsViewDialog.DIALOG_HEADER );

        and: "one component displayed"
        components.size() == 1;
        and: "'Automatic' component displayed, when there are no controllers"
        components.get( 0 ).getName().equals( "Automatic" );
    }

    @Ignore
    def "GIVEN opened a existing site WHEN 'Page Component View' shown AND menu-button clicked THEN context menu should be present"()
    {
        given: "opened a existing site"
        filterPanel.typeSearchText( SITE_NAME )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit();

        and: "'Page Components View' shown"
        wizard.showPageEditor().showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );

        when: "menu-button clicked"
        dialog.openMenu( "Automatic" );
        ItemViewContextMenu contextMenu = new ItemViewContextMenu( getSession() );

        then: "context menu is displayed"
        contextMenu.isDisplayed();
    }

    @Ignore
    def "GIVEN 'Page Component View' shown AND context menu displayed WHEN wizard closed THEN context menu closed as well "()
    {
        given: "existing site is opened"
        filterPanel.typeSearchText( SITE_NAME )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit();

        and: "page components view shown "
        wizard.showPageEditor().showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );

        and: "context-menu opened"
        dialog.openMenu( "Automatic" );
        ItemViewContextMenu contextMenu = new ItemViewContextMenu( getSession() );

        when: "site-wizard have been closed"
        wizard.close( SITE.getDisplayName() );
        TestUtils.saveScreenshot( getSession(), "context-menu-closed" );

        then: "context menu is not displayed"
        !contextMenu.isDisplayed();
    }

    @Ignore
    def "GIVEN 'Page Component View' shown AND context menu displayed WHEN 'HomeButton' pressed THEN context menu is not displayed "()
    {
        given: "existing site have been opened"
        filterPanel.typeSearchText( SITE_NAME )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit();
        wizard.showPageEditor().showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );
        dialog.openMenu( "Automatic" );
        ItemViewContextMenu contextMenu = new ItemViewContextMenu( getSession() );

        when: "HomeButton clicked"
        contentBrowsePanel.goToAppHome();
        TestUtils.saveScreenshot( getSession(), "context-menu-home" );

        then: "context menu is not displayed"
        !contextMenu.isDisplayed();
    }

    def "GIVEN 'Page Components' view opened WHEN button 'close' clicked THEN dialog not displayed"()
    {
        given: "'Page Components' view opened"
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarEdit().showPageEditor().showComponentView();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );

        when: "button 'close' clicked"
        dialog.doCloseDialog();
        TestUtils.saveScreenshot( getSession(), "page-comp-dialog-closed" );

        then: "'page component view' is not displayed"
        !dialog.isOpened()
    }
}
