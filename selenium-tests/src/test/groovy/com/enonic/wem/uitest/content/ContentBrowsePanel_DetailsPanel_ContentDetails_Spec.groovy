package com.enonic.wem.uitest.content

import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

/**
 * Tasks:
 * enonic/xp-ui-testing#44  Update tests for 'Details Panel' in order to new changes*/

class ContentBrowsePanel_DetailsPanel_ContentDetails_Spec
    extends BaseContentSpec
{
    @Shared
    Content folderContent;

    def "WHEN no one content is selected THEN 'Details Panel Toggle' button should be displayed AND details panel should not be displayed by default"()
    {
        expect: "'Details Panel Toggle' button is displayed"
        contentBrowsePanel.isDetailsPanelToggleButtonDisplayed();

        and: "details panel should not be displayed by default"
        !contentDetailsPanel.isDisplayed();
    }

    def "WHEN content selected THEN correct display name shown in the Detail Panel"()
    {
        given: "folder has been added"
        folderContent = buildFolderContent( "details_p", "details_panel_test" );
        addContent( folderContent );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "when the folder is selected in the 'Browse Panel'"
        findAndSelectContent( folderContent.getName() );

        then: "correct display name should be shown on the Details Panel"
        contentDetailsPanel.getContentDisplayName() == folderContent.getDisplayName();
        and: "menu options is opened"
        List<String> widgetMenuOptions = contentDetailsPanel.getMenuOptions();
        and: "three items should be present"
        widgetMenuOptions.size() == 3;
        and: "'Details' menu option should be present"
        widgetMenuOptions.contains( "Details" );
        and: "'Dependencies' menu option should be present"
        widgetMenuOptions.contains( "Dependencies" );
        and: "Version history menu option should be present"
        widgetMenuOptions.contains( "Version history" );
    }

    def "GIVEN 'Content Details Panel' opened WHEN Toggle Content Details button clicked THEN 'Content Details Panel' hidden"()
    {
        given: "content selected and the 'Content Details Panel' shown"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        saveScreenshot( "detail-panel-opened" );

        when: "'Toggle' button clicked"
        contentBrowsePanel.clickOnDetailsToggleButton();
        saveScreenshot( "detail-panel-closed" );

        then: "'Content Details Panel' not displayed"
        !contentBrowsePanel.getContentDetailsPanel().isOpened( folderContent.getDisplayName() );
    }
}
