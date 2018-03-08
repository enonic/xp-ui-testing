package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.*
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created by  on 05.09.2017.
 *
 * Tasks: xp-ui-testing#50 Add Selenium tests for inserting a fragment in a page-template
 * */
@Stepwise
class PageTemplate_Fragments_Spec
    extends BaseSiteSpec
{
    @Shared
    Content SITE;

    @Shared
    Content PAGE_TEMPLATE;


    def "GIVEN site with a controller has been added WHEN new page template has been added THEN the template should be present beneath the site"()
    {
        given: "site with a controller has been added"
        SITE = buildMyFirstAppSite( "site" );
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() )
        wizardPanel.typeData( SITE ).save().close( SITE.getDisplayName() );

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

    def "GIVEN existing site is opened WHEN new fragment created in the site THEN new fragment should be listed"()
    {
        given: "existing site is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        sleep( 1000 );

        ItemViewContextMenu itemViewContextMenu = wizard.showItemViewContextMenu();
        itemViewContextMenu.clickOnCustomizeMenuItem();
        wizard.switchToDefaultWindow();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "image component is inserted"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Image" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageFromOptions( ELEPHANT_IMAGE_DISPLAY_NAME );
        wizard.switchToDefaultWindow();
        and: "and new fragment has been created"
        wizard.showComponentView();
        pageComponentsView.openMenu( ELEPHANT_IMAGE_DISPLAY_NAME ).selectMenuItem( ComponentMenuItems.SAVE_AS_FRAGMENT.getValue() );
        sleep( 2000 )
        wizard = contentBrowsePanel.switchToBrowserTabByTitle( ELEPHANT_IMAGE_DISPLAY_NAME );

        then: "new fragment should be present on the page"
        wizard.getNameInputValue() == buildFragmentName( ELEPHANT_IMAGE_DISPLAY_NAME );
    }

    def "GIVEN existing page template is opened WHEN existing fragment has been inserted THEN it should be present "()
    {
        given: "existing page template is opened"
        ContentWizardPanel wizard = findAndSelectContent( PAGE_TEMPLATE.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "Fragment-component has been inserted"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Fragment" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );

        and: "existing fragment has been selected from the options"
        FragmentComponentView fragmentComponentView = new FragmentComponentView( getSession() );
        fragmentComponentView.selectFragment( ELEPHANT_IMAGE_DISPLAY_NAME );

        then: "new fragment should be added on the page-template"
        liveFormPanel.getNumberOfFragments() == 1;
    }

    def "GIVEN existing page template is opened WHEN image has been clicked on the PageComponentView and context menu opened THEN 'Create Fragment' menu item should not be present "()
    {
        given: "existing page template is opened"
        ContentWizardPanel wizard = findAndSelectContent( PAGE_TEMPLATE.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();
        and: "Image-component has been inserted"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Image" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();

        and: "image has been selected in options"
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageFromOptions( ELEPHANT_IMAGE_DISPLAY_NAME );
        wizard.save();

        when: "the image has been clicked on the PageComponentView and context menu opened"
        wizard.switchToDefaultWindow().showComponentView();
        pageComponentsView.openMenu( ELEPHANT_IMAGE_DISPLAY_NAME );

        then: "'Save as Fragment' menu item should not be displayed, because it is the page-template"
        !pageComponentsView.isMenuItemPresent( ComponentMenuItems.SAVE_AS_FRAGMENT.getValue() );
    }
}
