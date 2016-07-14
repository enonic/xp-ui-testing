package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.DependenciesWidgetItemView
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DetailsPanel_DependenciesWidgetItemView_Spec
    extends BaseContentSpec
{

    @Shared
    String TARGET_IMG = "whale.jpg";

    @Shared
    Content SHORTCUT_CONTENT;

    def "WHEN image content selected and details panel opened AND 'Dependencies' option selected THEN Dependencies Widget is displayed and has attachments"()
    {
        when: "image content selected"
        findAndSelectContent( IMPORTED_BOOK_IMAGE );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        TestUtils.saveScreenshot( getSession(), "test_dependencies_widget_opened" );

        then: "Dependencies Widget is displayed"
        dependencies.isDisplayed();
    }

    def "WHEN folder content selected and details panel opened AND 'Dependencies' option selected THEN Dependencies Widget displayed without dependencies"()
    {
        when: "image content selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        TestUtils.saveScreenshot( getSession(), "test_dependencies_widget_no_dependencies" );

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
        SHORTCUT_CONTENT = buildShortcutWithTarget( "shortcut", null, "shortcut display name", TARGET_IMG );
        addContent( SHORTCUT_CONTENT );
        findAndSelectContent( SHORTCUT_CONTENT.getName() );

        when: "display name of the folder changed"
        DependenciesWidgetItemView dependencies = openDependenciesWidgetView();
        TestUtils.saveScreenshot( getSession(), "dependencies_shortcut_selected" );

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
        TestUtils.saveScreenshot( getSession(), "outbound_image_shortcut" );

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
        TestUtils.saveScreenshot( getSession(), "inbound_image_shortcut" );

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

    //TODO add tests for outbound dependencies in site
    // https://youtrack.enonic.net/issue/INBOX-476

}
