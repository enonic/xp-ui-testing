package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.DependenciesWidgetItemView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Tasks:
 * XP-4643 Add selenium tests to verify XP-3893
 * verifies the XP-3893 Dependencies Widget: outbound dependencies not displayed , when site with inserted images was selected
 * */
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

    def "WHEN image is selected and details panel opened AND 'Dependencies' option selected THEN Dependencies Widget is displayed and has attachments"()
    {
        when: "image content is selected"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        saveScreenshot( "test_dependencies_widget_opened" );

        then: "'Dependencies Widget' should be displayed"
        dependencies.isDisplayed();
    }

    def "WHEN folder is selected and details panel opened AND 'Dependencies' option selected THEN Dependencies Widget displayed without dependencies"()
    {
        when: "folder content has been selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        saveScreenshot( "test_dependencies_widget_no_dependencies" );

        then: "Dependencies Widget should be displayed"
        dependencies.isDisplayed();

        and: "'show outbound dependencies' should not be displayed"
        !dependencies.isShowOutboundButtonDisplayed();

        and: "'show inbound dependencies' should not be displayed"
        !dependencies.isShowInboundButtonDisplayed();
    }

    def "GIVEN existing shortcut AND target is an image WHEN Dependencies Widget is opened THEN 'show outbound'- button should be displayed"()
    {
        given: "existing shortcut has been selected"
        SHORTCUT_CONTENT = buildShortcutWithTarget( "shortcut", null, "shortcut display name", WHALE_IMAGE_DISPLAY_NAME );
        addContent( SHORTCUT_CONTENT );
        findAndSelectContent( SHORTCUT_CONTENT.getName() );

        when: "Dependencies Widget is opened"
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        saveScreenshot( "dependencies_shortcut_selected" );

        then: "'show outbound'- button should be displayed"
        dependencies.isShowOutboundButtonDisplayed();
    }

    def "GIVEN existing shortcut AND Dependencies Widget opened WHEN 'show outbound'- button clicked THEN Dependencies Section appears on the filter panel AND correct label for dependencies is displayed"()
    {
        given:
        findAndSelectContent( SHORTCUT_CONTENT.getName() );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();

        when: "'show outbound'- button has been clicked"
        dependencies.clickOnShowOutboundButton();
        NavigatorHelper.switchToNextTab( getTestSession(  ) );
        saveScreenshot( "outbound_image_shortcut" );

        then: "Dependencies Section should appear in the filter panel"
        filterPanel.isDependenciesSectionDisplayed();
        List<String> names = contentBrowsePanel.getContentDisplayNamesFromGrid();

        and: "correct label for dependencies is displayed"
        filterPanel.getDependenciesSectionTitle() == ContentBrowseFilterPanel.DEPENDENCIES_SECTION_OUTBOUND_TITLE;


        and: "close the section button is displayed"
        filterPanel.isCloseDependenciesSectionButtonDisplayed();

        and: "correct target is filtered in the grid"
        names.contains( TARGET_IMG );

        and: "one content should be displayed in the grid"
        names.size() == 1;
    }

    def "GIVEN shortcut is selected AND Dependencies Widget opened WHEN button 'close' on the section pressed THEN Dependencies Section closes"()
    {
        given:
        findAndSelectContent( SHORTCUT_CONTENT.getName() );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        dependencies.clickOnShowOutboundButton();
        NavigatorHelper.switchToNextTab( getTestSession(  ) );

        when: "button 'close' on the section has been pressed"
        filterPanel.doCloseDependenciesSection();

        then: "'Dependencies Section' should be closed"
        !filterPanel.isDependenciesSectionDisplayed();
    }

    def "GIVEN existing image that is target for shortcut was selected AND Dependencies Widget opened WHEN 'show inbound'- button clicked THEN Dependencies Section appears  AND correct label for dependencies is displayed"()
    {
        given:
        findAndSelectContent( TARGET_IMG );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();

        when: "'show inbound'- button has been clicked"
        dependencies.clickOnShowInboundButton();
        sleep( 1000 );
        NavigatorHelper.switchToNextTab( getTestSession(  ) );
        saveScreenshot( "test_inbound_image_shortcut" );

        then: "Dependencies Section appears on the filter panel"
        filterPanel.isDependenciesSectionDisplayed();
        List<String> names = contentBrowsePanel.getContentNamesFromGrid();

        and: "correct label for dependencies is displayed"
        filterPanel.getDependenciesSectionTitle() == ContentBrowseFilterPanel.DEPENDENCIES_SECTION_INBOUND_TITLE;

        and: "one content should be displayed in the grid"
        names.size() == 1;

        and: "correct target is filtered in the grid"
        names.get( 0 ).contains( SHORTCUT_CONTENT.getName() );
    }

    def "GIVEN existing content with an 'image-selector' AND the test image is selected in its content WHEN dependencies for image is opened THEN two correct inbound dependencies are displayed"()
    {
        given: "site has been added"
        addSiteWithAllInputTypes( TEST_SITE_NAME );

        and: "the site is selected and new content with 'image-selector' added"
        Content imageSelector = buildImageSelector1_1_Content( TEST_SITE_NAME, TARGET_IMG )
        findAndSelectContent( TEST_SITE_NAME ).clickToolbarNew().selectContentType( imageSelector.getContentTypeName() ).typeData(
            imageSelector ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "dependencies for image is opened"
        findAndSelectContent( TARGET_IMG );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        dependencies.clickOnShowInboundButton();
        sleep(2000);
        NavigatorHelper.switchToNextTab( getTestSession(  ) );
        List<String> names = contentBrowsePanel.getContentNamesFromGrid();
        saveScreenshot( "test_two_inbound_contents" );

        then: "two contents should be displayed in the grid"
        names.size() == 2;

        and: "required contents should be filtered in the grid"
        TestUtils.isContains( names, SHORTCUT_CONTENT.getName() )

        and: "required contents should be filtered in the grid"
        TestUtils.isContains( names, imageSelector.getName() )
    }
    // verifies the XP-3893 Dependencies Widget: outbound dependencies not displayed, when site with inserted images was selected
    def "GIVEN existing site with inserted Image Component WHEN site selected and dependency widget is opened THEN correct outbound dependency should be displayed"()
    {
        given: "Image Component was added on the site"
        def name = NameHelper.uniqueName( "site" );
        addSiteWithAllInputTypes( name );
        ContentWizardPanel wizard = findAndSelectContent( name ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsViewDialog = wizard.selectPageDescriptor(
            "Page" ).switchToDefaultWindow().showComponentView();
        pageComponentsViewDialog.openMenu( "main" ).selectMenuItem( "Insert", "Image" );
        pageComponentsViewDialog.doCloseDialog();
        wizard.switchToLiveEditFrame();

        and: "image has been selected from the options list"
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageFromOptions( HAND_IMAGE_DISPLAY_NAME );
        wizard.closeBrowserTab().switchToBrowsePanelTab();

        when: "site with the component was selected in the grid and dependency widget is opened"
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        dependencies.clickOnShowOutboundButton();
        NavigatorHelper.switchToNextTab( getTestSession(  ) );
        List<String> names = contentBrowsePanel.getContentNamesFromGrid();
        saveScreenshot( "test_dependencies_site_with_component" );

        then: "correct outbound dependency should be displayed"
        names.get( 0 ).contains( HAND_IMAGE_DISPLAY_NAME );

    }
}
