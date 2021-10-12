package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.*
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 05.09.2017.
 *
 * */
@Stepwise
class PageTemplate_Fragments_Spec
    extends BaseSiteSpec
{
    @Shared
    Content SITE;

    @Shared
    Content PAGE_TEMPLATE;

    @Shared
    String PAGE_TEMPLATE_FRAGMENT_TEXT = "text2";

    String SITE_FRAGMENT_TEXT = "text1";


    def "Preconditions: new site and new page template should be added"()
    {
        given: "site with a controller has been added"
        SITE = buildMyFirstAppSite( "site" );
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() )
        wizardPanel.typeData( SITE ).close( SITE.getDisplayName() );

        and: "the site is expanded"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );

        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_TITLE, SUPPORT_SITE, "test template",
                                           SITE.getName() );
        and: "new page template has been added"
        contentBrowsePanel.clickOnRowByName( "_templates" ).clickToolbarNew().selectContentType(
            PAGE_TEMPLATE.getContentTypeName() ).showPageEditor().typeData( PAGE_TEMPLATE ).close( PAGE_TEMPLATE.getDisplayName() );

        when: "the name of the page template has been typed"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );

        then: "the template should be filtered"
        contentBrowsePanel.exists( PAGE_TEMPLATE.getName() );
    }

    def "GIVEN existing site is opened WHEN new fragment has been created from text-component THEN new fragment wizard should be loaded in new browser tab"()
    {
        given: "existing site is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        sleep( 2000 );

        ItemViewContextMenu itemViewContextMenu = wizard.showItemViewContextMenu();
        itemViewContextMenu.clickOnCustomizeMenuItem();
        wizard.switchToDefaultWindow();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "image component has been inserted"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Text" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.typeTextInTextComponent( SITE_FRAGMENT_TEXT );
        wizard.switchToDefaultWindow().save();
        wizard.waitForNotificationMessage();
        and: "new fragment has been created"
        wizard.showComponentView();
        pageComponentsView.openMenu( SITE_FRAGMENT_TEXT ).selectMenuItem( ComponentMenuItems.SAVE_AS_FRAGMENT.getValue() );
        sleep( 2000 )
        wizard = contentBrowsePanel.switchToBrowserTabByTitle( SITE_FRAGMENT_TEXT );

        then: "new fragment should be loaded in new browser tab:"
        wizard.getNameInputValue() == buildFragmentName( SITE_FRAGMENT_TEXT );
    }

    def "GIVEN existing page template is opened WHEN existing fragment has been inserted THEN the fragment should be present in the template"()
    {
        given: "existing page template is opened"
        ContentWizardPanel wizard = findAndSelectContent( PAGE_TEMPLATE.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "Fragment-component has been inserted"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Fragment" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();

        and: "existing fragment has been selected from the options"
        FragmentComponentView fragmentComponentView = new FragmentComponentView( getSession() );
        fragmentComponentView.selectFragment( SITE_FRAGMENT_TEXT );

        then: "template should be automatically saved"
        !wizard.isSaveButtonEnabled();
    }

    def "GIVEN existing page template is opened WHEN context menu for a text component has been opened THEN 'Create Fragment' menu item should not be present"()
    {
        given: "existing page template is opened"
        ContentWizardPanel wizard = findAndSelectContent( PAGE_TEMPLATE.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();
        and: "Image-component has been inserted"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Text" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();

        and: "image has been selected in options"
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.typeTextInTextComponent( PAGE_TEMPLATE_FRAGMENT_TEXT );
        wizard.switchToDefaultWindow().save();
        wizard.waitForNotificationMessage();

        when: "the image has been clicked in the PageComponentView and context menu opened"
        wizard.showComponentView();
        pageComponentsView.openMenu( PAGE_TEMPLATE_FRAGMENT_TEXT );

        then: "'Save as Fragment' menu item should not be displayed, because it is the page-template"
        !pageComponentsView.isMenuItemPresent( ComponentMenuItems.SAVE_AS_FRAGMENT.getValue() );
    }
}
