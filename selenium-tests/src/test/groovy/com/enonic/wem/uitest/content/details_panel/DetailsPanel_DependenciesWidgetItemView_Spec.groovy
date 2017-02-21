package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.DependenciesWidgetItemView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Tasks:
 * XP-4643 Add selenium tests to verify XP-3893
 * verifies the XP-3893 Dependencies Widget: outbound dependencies not displayed , when site with inserted images was selected*/
@Stepwise
class DetailsPanel_DependenciesWidgetItemView_Spec
    extends BaseContentSpec
{

    @Shared
    String TARGET_IMG = WHALE_IMAGE_DISPLAY_NAME;

    @Shared
    Content SHORTCUT_CONTENT;

    @Shared
    String TEST_SITE_NAME = NameHelper.uniqueName( "site" );

    def "WHEN image content selected and details panel opened AND 'Dependencies' option selected THEN Dependencies Widget is displayed and has attachments"()
    {
        when: "image content selected"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        saveScreenshot( "test_dependencies_widget_opened" );

        then: "Dependencies Widget is displayed"
        dependencies.isDisplayed();
    }

    def "WHEN folder content selected and details panel opened AND 'Dependencies' option selected THEN Dependencies Widget displayed without dependencies"()
    {
        when: "image content selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        saveScreenshot( "test_dependencies_widget_no_dependencies" );

        then: "Dependencies Widget is displayed"
        dependencies.isDisplayed();

        and: "'show outbound dependencies' not displayed"
        !dependencies.isShowOutboundButtonDisplayed();

        and: "'show inbound dependencies' not displayed"
        !dependencies.isShowInboundButtonDisplayed();
    }

    def "GIVEN existing shortcut AND target is an image WHEN Dependencies Widget opened THEN 'show outbound'- button is displayed"()
    {
        given:
        SHORTCUT_CONTENT = buildShortcutWithTarget( "shortcut", null, "shortcut display name", WHALE_IMAGE_DISPLAY_NAME );
        addContent( SHORTCUT_CONTENT );
        findAndSelectContent( SHORTCUT_CONTENT.getName() );

        when: "display name of the folder changed"
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        saveScreenshot( "dependencies_shortcut_selected" );

        then: "'show outbound'- button is displayed"
        dependencies.isShowOutboundButtonDisplayed();
    }

    def "GIVEN existing shortcut AND Dependencies Widget opened WHEN 'show outbound'- button clicked THEN Dependencies Section appears on the filter panel AND correct label for dependencies is displayed"()
    {
        given:
        findAndSelectContent( SHORTCUT_CONTENT.getName() );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();

        when: "'show outbound'- button has been clicked"
        dependencies.clickOnShowOutboundButton();
        saveScreenshot( "outbound_image_shortcut" );

        then: "Dependencies Section appears on the filter panel"
        filterPanel.isDependenciesSectionDisplayed();
        List<String> names = contentBrowsePanel.getContentDisplayNamesFromGrid();

        and: "correct label for dependencies is displayed"
        filterPanel.getDependenciesSectionTitle() == ContentBrowseFilterPanel.DEPENDENCIES_SECTION_OUTBOUND_TITLE;


        and: "close the section button is displayed"
        filterPanel.isCloseDependenciesSectionButtonDisplayed();

        and: "correct target is filtered in the grid"
        names.contains( TARGET_IMG );

        and: "one content displayed in the grid"
        names.size() == 1;
    }

    def "GIVEN Dependencies Widget opened WHEN button 'close' on the section pressed THEN Dependencies Section closes"()
    {
        given:
        findAndSelectContent( SHORTCUT_CONTENT.getName() );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        dependencies.clickOnShowOutboundButton();

        when: " button 'close' on the section pressed"
        filterPanel.doCloseDependenciesSection();

        then: "Dependencies Section closes"
        !filterPanel.isDependenciesSectionDisplayed();
    }

    def "GIVEN existing image that is target for shortcut was selected AND Dependencies Widget opened WHEN 'show inbound'- button clicked THEN Dependencies Section appears  AND correct label for dependencies is displayed"()
    {
        given:
        findAndSelectContent( TARGET_IMG );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();

        when: "'show inbound'- button has been clicked"
        dependencies.clickOnShowInboundButton();
        saveScreenshot( "test_inbound_image_shortcut" );

        then: "Dependencies Section appears on the filter panel"
        filterPanel.isDependenciesSectionDisplayed();
        List<String> names = contentBrowsePanel.getContentNamesFromGrid();

        and: "correct label for dependencies is displayed"
        filterPanel.getDependenciesSectionTitle() == ContentBrowseFilterPanel.DEPENDENCIES_SECTION_INBOUND_TITLE;

        and: "one content displayed in the grid"
        names.size() == 1;

        and: "correct target is filtered in the grid"
        names.get( 0 ).contains( SHORTCUT_CONTENT.getName() );
    }

    def "GIVEN existing content with an 'image-selector' AND the test image is selected in its content WHEN dependencies for image is opened THEN two correct inbound dependencies are displayed"()
    {
        given: "add a site with all content types"
        addSiteWithAllInputTypes( TEST_SITE_NAME );

        and: "content with 'image-selector' added"
        Content imageSelector = buildImageSelector1_1_Content( TEST_SITE_NAME, TARGET_IMG )
        findAndSelectContent( TEST_SITE_NAME ).clickToolbarNew().selectContentType( imageSelector.getContentTypeName() ).typeData(
            imageSelector ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "dependencies for image is opened"
        findAndSelectContent( TARGET_IMG );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        dependencies.clickOnShowInboundButton();
        List<String> names = contentBrowsePanel.getContentNamesFromGrid();
        saveScreenshot( "test_two_inbound_contents" );

        then: "two contents are displayed in the grid"
        names.size() == 2;

        and: "correct contents are filtered in the grid"
        TestUtils.isContains( names, SHORTCUT_CONTENT.getName() )


        and: "correct contents are filtered in the grid"
        TestUtils.isContains( names, imageSelector.getName() )

    }
    // verifies the XP-3893 Dependencies Widget: outbound dependencies not displayed, when site with inserted images was selected
    def "GIVEN existing site with inserted Image Component WHEN site selected and dependency widget is opened THEN correct outbound dependency should be displayed"()
    {
        given: "Image Component was added on the site"
        def name = NameHelper.uniqueName( "site" );
        addSiteWithAllInputTypes( name );
        ContentWizardPanel wizard = findAndSelectContent( name ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsViewDialog = wizard.selectPageDescriptor( "Page" ).save().showComponentView();
        pageComponentsViewDialog.openMenu( "main" ).selectMenuItem( "Insert", "Image" );
        pageComponentsViewDialog.doCloseDialog();
        wizard.switchToLiveEditFrame();

        and: "image has been selected from the options list"
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageItemFromList( HAND_IMAGE_DISPLAY_NAME );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "site with the component was selected in the grid and dependency widget is opened"
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        dependencies.clickOnShowOutboundButton();
        List<String> names = contentBrowsePanel.getContentNamesFromGrid();
        saveScreenshot( "test_dependencies_site_with_component" );

        then: "correct outbound dependency should be displayed"
        names.get( 0 ).contains( HAND_IMAGE_DISPLAY_NAME );

    }
}
