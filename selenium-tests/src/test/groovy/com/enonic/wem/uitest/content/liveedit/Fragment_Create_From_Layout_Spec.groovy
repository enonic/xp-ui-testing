package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.LayoutInspectionPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ComponentMenuItems
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.pages.form.liveedit.LayoutComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 12.01.2017.
 * */
@Stepwise
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
            SITE ).selectPageDescriptor( MAIN_REGION_PAGE_DESCRIPTOR_NAME );
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        and: "image component is inserted"
        pageComponentsView.openMenu( "main" ).selectMenuItem( "Insert", "Layout" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();
        LayoutComponentView layoutComponentView = new LayoutComponentView( getSession() );
        layoutComponentView.selectLayout( LAYOUT_3_COL_DISPLAY_NAME );
        wizard.switchToDefaultWindow();

        when: "click on the image-component and 'create fragment' menu item is selected"
        wizard.showComponentView();
        pageComponentsView.openMenu( LAYOUT_3_COL_DISPLAY_NAME ).selectMenuItem( ComponentMenuItems.SAVE_AS_FRAGMENT.getValue() );
        sleep( 3000 );
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        sleep( 4000 );
        wizard = contentBrowsePanel.switchToBrowserTabByTitle( LAYOUT_3_COL_DISPLAY_NAME );
        saveScreenshot( "fragment_wizard" );

        then: "fragment-wizard should be opened in the new browser tab"
        wizard.getNameInputValue() == buildFragmentName( LAYOUT_3_COL_DISPLAY_NAME );

        and: "'Fragment' wizard's step should be present"
        wizard.isWizardStepPresent( "Fragment" );

        and: "Preview button should be enabled"
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
        wizard.showComponentView().openMenu( LAYOUT_3_COL_DISPLAY_NAME ).selectMenuItem( "Inspect" );
        saveScreenshot( "layout_fragment_wizard" );

        when: "the layout with 3 columns has been replaced with the '2-col' layout"
        LayoutInspectionPanel inspectionPanel = new LayoutInspectionPanel( getSession() );
        inspectionPanel.setLayout( LAYOUT_2_COL_DISPLAY_NAME );
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );

        then: "number of columns in the layout should be changed"
        liveFormPanel.getLayoutColumnNumber() == INITIAL_NUMBER_OF_COLUMN - 1;
    }

    def "GIVEN fragment wizard is opened WHEN the initial layout was set on the fragment-wizard THEN initial layout should be present on the site-wizard as well"()
    {
        given: "existing site with the fragment is opened "
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        and: "PageComponentsView has been opened"
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();
        and: "the fragment has been opened in the new tab"
        pageComponentsView.openMenu( LAYOUT_3_COL_DISPLAY_NAME ).selectMenuItem( ComponentMenuItems.EDIT_IN_NEW_TAB.getValue() );

        and: "switched to the fragment-wizard"
        wizard = contentBrowsePanel.switchToBrowserTabByTitle( LAYOUT_3_COL_DISPLAY_NAME );
        and: "open the 'Component View' and click on the layout's display name"
        wizard.showComponentView().openMenu( LAYOUT_2_COL_DISPLAY_NAME ).selectMenuItem( "Inspect" );

        when: "'Layout Inspection' panel is opened, 2-col has been replaced with 3-col"
        LayoutInspectionPanel inspectionPanel = new LayoutInspectionPanel( getSession() );
        inspectionPanel.setLayout( LAYOUT_3_COL_DISPLAY_NAME );

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

    def "GIVEN fragment(layout) is opened WHEN an image has been inserted into the fragment THEN one image should be displayed on the fragment"()
    {
        given: "existing fragment is opened "
        ContentWizardPanel fragmentWizard = findAndSelectContent( LAYOUT_3_COL_DISPLAY_NAME ).clickToolbarEdit();
        and: "PageComponentsView has been opened"
        PageComponentsViewDialog pageComponentsView = fragmentWizard.showComponentView();
        pageComponentsView.expandItem( LAYOUT_3_COL_DISPLAY_NAME );

        when: "Insert Image menu item has been clicked"
        pageComponentsView.openMenu( "left" ).selectMenuItem( "Insert", "Image" );
        pageComponentsView.doCloseDialog();
        fragmentWizard.switchToLiveEditFrame();
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );

        and: "drop-down handler has been clicked"
        imageComponentView.clickOnDropDownHandler();
        imageComponentView.clickOnDropDownModeToggler().clickOnExpanderInDropDownList( "imagearchive" ).clickOnOption( "enterprise" );

        and: "the fragment should be automatically saved"
        fragmentWizard.switchToLiveEditFrame();
        saveScreenshot( "image_in_fragment_inserted" );

        then: "new image should be present in the left-region "
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.getNumberImagesInLayout() == 1;
    }
}
