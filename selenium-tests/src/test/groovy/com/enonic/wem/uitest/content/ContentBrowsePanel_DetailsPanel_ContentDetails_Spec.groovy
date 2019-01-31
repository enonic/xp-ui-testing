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

    def "WHEN a content has been selected THEN expected display name should be shown in the Detail Panel"()
    {
        given: "folder has been added"
        folderContent = buildFolderContent( "details_p", "details_panel_test" );
        addContent( folderContent );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "when the folder is selected in the 'Browse Panel'"
        findAndSelectContent( folderContent.getName() );

        then: "expected display name should be shown on the Details Panel"
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

    def "GIVEN a content is selected AND 'Content Details Panel' is opened WHEN Toggle Content Details button has been clicked THEN 'Content Details Panel' should be closed"()
    {
        given: "content has been selected and the 'Content Details Panel' is opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        saveScreenshot( "detail-panel-opened" );

        when: "'Toggle' button has been clicked"
        contentBrowsePanel.clickOnDetailsToggleButton();
        saveScreenshot( "detail-panel-closed" );

        then: "'Content Details Panel' should be closed"
        !contentBrowsePanel.getContentDetailsPanel().isOpened( folderContent.getDisplayName() );
    }
}
