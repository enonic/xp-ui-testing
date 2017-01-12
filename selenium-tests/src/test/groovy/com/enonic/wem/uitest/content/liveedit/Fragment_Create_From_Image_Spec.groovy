package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.DependenciesWidgetItemView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ComponentMenuItems
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 05.01.2017.
 *
 * Tasks:
 *   XP-4814 Add selenium tests for creating of fragment from an image
 *   XP-4851 Add selenium tests for checking of outbound, inbound dependencies (Image Fragment)
 * */
@Stepwise
class Fragment_Create_From_Image_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    String FRAGMENT_DISPLAY_NAME = HAND_IMAGE_DISPLAY_NAME;

    def "GIVEN Page Component View is opened AND image component is inserted WHEN click on the image-component and 'create fragment' menu item is selected THEN fragment-wizard is opened in the new browser tab"()
    {
        given: "Page Components View is opened"
        SITE = buildSimpleSiteApp();
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData(
            SITE ).selectPageDescriptor( MAIN_REGION_PAGE_DESCRIPTOR_NAME ).save();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        and: "image component is inserted"
        pageComponentsView.openMenu( "main" ).selectMenuItem( "Insert", "Image" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageItemFromList( HAND_IMAGE_DISPLAY_NAME );
        wizard.save();

        when: "click on the image-component and 'create fragment' menu item is selected"
        wizard.showComponentView();
        pageComponentsView.openMenu( HAND_IMAGE_DISPLAY_NAME ).selectMenuItem( "Create Fragment" );
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        sleep( 1000 );
        wizard = contentBrowsePanel.switchToBrowserTabByTitle( HAND_IMAGE_DISPLAY_NAME );
        saveScreenshot( "fragment_wizard" );

        then: "fragment-wizard is opened in the new browser tab"
        wizard.getNameInputValue() == buildFragmentName( HAND_IMAGE_DISPLAY_NAME );

        and:
        wizard.isWizardStepPresent( "Fragment" );

        and: "Preview button is enabled"
        wizard.isPreviewButtonEnabled();
    }

    def "GIVEN existing site with the fragment is opened WHEN page component view opened THEN fragment with correct display name should be present"()
    {
        given: "existing site with the fragment is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();

        when: "page component view opened"
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();
        List<String> fragments = pageComponentsView.getFragmentDisplayNames();

        then: "fragment with correct display name should be present"
        fragments.size() == 1;

        and:
        fragments.get( 0 ) == HAND_IMAGE_DISPLAY_NAME;
    }

    def "GIVEN existing site with the fragment is opened WHEN page component view is opened AND context menu for the fragment has been opened THEN all menu items shoule be present"()
    {
        given: "existing site with the fragment is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();

        when: "page component view opened"
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();
        pageComponentsView.openMenu( HAND_IMAGE_DISPLAY_NAME );
        saveScreenshot( "fragment_context_menu" );

        then: "fragment with correct display name is present"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.SELECT_PARENT.getValue() );

        and: "'Edit in new tab' should be displayed"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.EDIT_IN_NEW_TAB.getValue() );

        and: "'Insert' should be displayed"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.INSERT.getValue() );

        and: "'Insert' should be displayed"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.INSPECT.getValue() );

        and: "'Remove' should be displayed"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.REMOVE.getValue() );

        and: "'Reset' should be displayed"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.RESET.getValue() );

        and: "'Duplicate' should be displayed"
        pageComponentsView.isMenuItemPresent( ComponentMenuItems.DUPLICATE.getValue() );
    }

    def "GIVEN page component view is opened WHEN the fragment is selected AND 'Edit in new tab' menu item has been selected THEN the fragment should be opened in the separated tab"()
    {
        given: "existing site with the fragment is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();

        when: "page component view opened"
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();
        pageComponentsView.openMenu( HAND_IMAGE_DISPLAY_NAME ).selectMenuItem( ComponentMenuItems.EDIT_IN_NEW_TAB.getValue() );
        sleep( 500 );
        wizard = contentBrowsePanel.switchToBrowserTabByTitle( HAND_IMAGE_DISPLAY_NAME );
        saveScreenshot( "fragment_edit_in_new_tab" );

        then: "fragment-wizard should be opened in the new browser tab with the correct display name"
        wizard.getNameInputValue() == buildFragmentName( HAND_IMAGE_DISPLAY_NAME );

        and: "Fragment step should be present on the wizard-page"
        wizard.isWizardStepPresent( "Fragment" );
    }

    def "GIVEN existing image-fragment is selected WHEN Dependencies Section is opened AND Inbound button has been pressed THEN the parent site should be filtered in the grid"()
    {
        given: "existing image-fragment is filtered"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectContentTypeInAggregationView( "Fragment" );

        and: "the fragment has been selected"
        contentBrowsePanel.clickCheckboxAndSelectRow( FRAGMENT_DISPLAY_NAME );

        when: "Dependencies Section is opened"
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();

        and: "'Show Inbound' button has been pressed"
        dependencies.clickOnShowInboundButton();
        List<String> names = contentBrowsePanel.getContentNamesFromGrid();

        then: "only one inbound dependency should be filtered"
        names.size() == 1;

        and: "correct name of the inbound dependency should be displayed"
        names.get( 0 ).contains( SITE.getName() );
    }

    def "GIVEN existing image-fragment is selected WHEN Dependencies Section is opened AND Outbound button has been pressed THEN the correct image should be filtered in the grid"()
    {
        given: "existing image-fragment is filtered"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectContentTypeInAggregationView( "Fragment" );

        and: "the fragment has been selected"
        contentBrowsePanel.clickCheckboxAndSelectRow( FRAGMENT_DISPLAY_NAME );

        when: "Dependencies Section is opened"
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();

        and: "'Show Inbound' button has been pressed"
        dependencies.clickOnShowOutboundButton();
        List<String> names = contentBrowsePanel.getContentNamesFromGrid();

        then: "only one inbound dependency should be filtered"
        names.size() == 1;

        and: "correct name of the outbound dependency should be displayed"
        names.get( 0 ).contains( HAND_IMAGE_DISPLAY_NAME );
    }

    def "GIVEN page component view is opened WHEN the fragment is selected AND 'Remove' menu item has been selected THEN the fragment should be removed from the Component View"()
    {
        given: "existing site with the fragment is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();

        when: "page component view is opened"
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        and: "context menu was shown and the fragment has been removed"
        pageComponentsView.openMenu( HAND_IMAGE_DISPLAY_NAME ).selectMenuItem( ComponentMenuItems.REMOVE.getValue() );
        List<String> fragments = pageComponentsView.getFragmentDisplayNames();
        saveScreenshot( "fragment_was_removed" );
        wizard.save();

        then: "the fragment should not be present on the Component View"
        fragments.size() == 0;
    }

    def "GIVEN page component view is opened WHEN 'main' region is selected AND fragment component has been inserted THEN the fragment should be present on the the page"()
    {
        given: "existing site with the fragment, that was removed, is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();

        and: "Page Component View is opened"
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();


        when: "root component in the  view is selected AND Insert Fragment menu item has been clicked"
        pageComponentsView.openMenu( "main" ).selectMenuItem( "Insert", "Fragment" );
        pageComponentsView.doCloseDialog();
        saveScreenshot( "fragment_inserted_in_component_view" );
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );

        and: "existing fragment has been selected in the options menu"
        liveFormPanel.selectFragment( FRAGMENT_DISPLAY_NAME );

        then: "new added fragment is present on the page"
        liveFormPanel.getNumberOfFragments() == 1;
    }


    private String buildFragmentName( String resourceDisplayName )
    {
        return "fragment-" + resourceDisplayName;
    }

}
