package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.LayoutInspectionPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ComponentMenuItems
import com.enonic.autotests.pages.form.liveedit.LayoutComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

/**
 * Created on 12.01.2017.
 *
 * Tasks:
 * XP-4817 Add selenium test for creating fragments from a layout
 * XP-4818 Add selenium test for switching between layouts
 * */
class Fragment_Create_From_Layout_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    String LAYOUT_3_COL_DISPLAY_NAME = "3-col"

    @Shared
    Integer INITIAL_NUMBER_OF_COLUMN = 3;

    @Shared
    String LAYOUT_2_COL_DISPLAY_NAME = "25/75"

    def "GIVEN Page Component View is opened AND layout component is inserted WHEN click on the layout-component and 'create fragment' menu item is selected THEN fragment-wizard is opened in the new browser tab"()
    {
        given: "Page Components View is opened"
        SITE = buildSimpleSiteApp();
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData(
            SITE ).selectPageDescriptor( MAIN_REGION_PAGE_DESCRIPTOR_NAME ).save();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        and: "image component is inserted"
        pageComponentsView.openMenu( "main" ).selectMenuItem( "Insert", "Layout" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();
        LayoutComponentView layoutComponentView = new LayoutComponentView( getSession() );
        layoutComponentView.selectLayout( LAYOUT_3_COL_DISPLAY_NAME );
        wizard.save();

        when: "click on the image-component and 'create fragment' menu item is selected"
        wizard.showComponentView();
        pageComponentsView.openMenu( LAYOUT_3_COL_DISPLAY_NAME ).selectMenuItem( "Create Fragment" );
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        sleep( 2000 );
        wizard = contentBrowsePanel.switchToBrowserTabByTitle( LAYOUT_3_COL_DISPLAY_NAME );
        saveScreenshot( "fragment_wizard" );

        then: "fragment-wizard is opened in the new browser tab"
        wizard.getNameInputValue() == buildFragmentName( LAYOUT_3_COL_DISPLAY_NAME );

        and:
        wizard.isWizardStepPresent( "Fragment" );

        and: "Preview button is enabled"
        wizard.isPreviewButtonEnabled();
    }

    def "GIVEN existing layout-fragment WHEN the fragment-content is opened THEN 3 columns should be rendered on the fragment-wizard"()
    {
        given: "the fragment has been selected"
        findAndSelectContent( buildFragmentName( LAYOUT_3_COL_DISPLAY_NAME ) );

        when: "the fragment has been opened"
        contentBrowsePanel.clickToolbarEdit().switchToLiveEditFrame(); ;

        then: "3 columns should be rendered on the fragment-wizard"
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.getLayoutColumnNumber() == INITIAL_NUMBER_OF_COLUMN;
    }

    def "GIVEN existing site AND the layout-fragment was created from the layout WHEN site is opened THEN original page should be rendered with a fragment instead of layout"()
    {
        given: "existing site is selected"
        findAndSelectContent( SITE.getName() );

        when: "the site has been opened"
        contentBrowsePanel.clickToolbarEdit().switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );

        then: "original page should be rendered with a fragment instead of layout"
        !liveFormPanel.isLayoutComponentPresent();

        and: "fragment-layout should be present"
        liveFormPanel.getNumberOfFragments() == 1;
    }

    def "GIVEN existing fragment is opened WHEN initial layout was replaced with another layout THEN new layout should be present in page-sources"()
    {
        given: "existing site has been selected and opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        and: "PageComponentsView has been opened"
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();
        and: "fragment has been clicked and 'Edit in new tab' selected "
        pageComponentsView.openMenu( LAYOUT_3_COL_DISPLAY_NAME ).selectMenuItem( ComponentMenuItems.EDIT_IN_NEW_TAB.getValue() );
        sleep( 1000 );

        and: "switching to the fragment-wizard"
        wizard = contentBrowsePanel.switchToBrowserTabByTitle( LAYOUT_3_COL_DISPLAY_NAME );
        and: "'click on the layout's name and open the Inspection panel"
        wizard.showComponentView().clickOnLayout( LAYOUT_3_COL_DISPLAY_NAME );
        saveScreenshot( "layout_fragment_wizard" );

        when: "the layout with 3 columns has been replaced with the '2-col' layout"
        LayoutInspectionPanel inspectionPanel = new LayoutInspectionPanel( getSession() );
        inspectionPanel.selectNewLayout( LAYOUT_2_COL_DISPLAY_NAME );
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );

        then: "number of columns in the layout should be changed"
        liveFormPanel.getLayoutColumnNumber() == INITIAL_NUMBER_OF_COLUMN - 1;
    }

    def "GIVEN existing site with the fragment is opened WHEN the initial layout was set on the fragment-wizard THEN initial layout should be present on the site-wizard as well"()
    {
        given: "existing site with the fragment is opened "
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        and: "PageComponentsView has been opened"
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();
        and: "the fragment has been opened in the new tab"
        pageComponentsView.openMenu( LAYOUT_2_COL_DISPLAY_NAME ).selectMenuItem( ComponentMenuItems.EDIT_IN_NEW_TAB.getValue() );

        and: "switched to the fragment-wizard"
        wizard = contentBrowsePanel.switchToBrowserTabByTitle( LAYOUT_3_COL_DISPLAY_NAME );
        and: "open the 'Component View' and click on the layout's display name"
        wizard.showComponentView().clickOnLayout( LAYOUT_2_COL_DISPLAY_NAME );

        when: "'Layout Inspection' panel is opened, current layout has been replaced"
        LayoutInspectionPanel inspectionPanel = new LayoutInspectionPanel( getSession() );
        inspectionPanel.selectNewLayout( LAYOUT_3_COL_DISPLAY_NAME );

        and: "switching to the site-wizard"
        wizard = contentBrowsePanel.switchToBrowserTabByTitle( SITE.getDisplayName() );
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );

        then: "layout should not be present on the site-page"
        !liveFormPanel.isLayoutComponentPresent();

        and: "only one fragment is present on the page"
        liveFormPanel.getNumberOfFragments() == 1;

        and: "fragment-component should have 3 columns"
        liveFormPanel.getNumberOfColumnInFragment() == 3;
    }
}
