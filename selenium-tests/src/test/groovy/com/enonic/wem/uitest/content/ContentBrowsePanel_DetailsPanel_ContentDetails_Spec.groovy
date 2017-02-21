package com.enonic.wem.uitest.content

import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class ContentBrowsePanel_DetailsPanel_ContentDetails_Spec
    extends BaseContentSpec
{
    @Shared
    Content folderContent;

    def "WHEN no one content is selected THEN 'Details Panel Toggle' button should be displayed AND details panel should not be displayed"()
    {
        expect: "'Details Panel Toggle' button is displayed"
        contentBrowsePanel.isDetailsPanelToggleButtonDisplayed();

        and: "details panel not displayed"
        !contentDetailsPanel.isDisplayed();
    }

    def "WHEN content selected THEN correct display name shown in the Detail Panel"()
    {
        given:
        folderContent = buildFolderContent( "details_p", "details_panel_test" );
        addContent( folderContent );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "when one content selected in the 'Browse Panel'"
        findAndSelectContent( folderContent.getName() );

        then: "correct display name shown in the Detail Panel"
        contentDetailsPanel.getContentDisplayName() == folderContent.getDisplayName();
    }

    def "GIVEN 'Content Details Panel' opened WHEN Toggle Content Details button clicked THEN 'Content Details Panel' hidden"()
    {
        given: "content selected and the 'Content Details Panel' shown"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        saveScreenshot( "detail-panel-opened" );

        when: "'Toggle' button clicked"
        contentBrowsePanel.clickOnDetailsToggleButton();
        TestUtils.saveScreenshot( getSession(), "detail-panel-closed" );

        then: "'Content Details Panel' not displayed"
        !contentBrowsePanel.getContentDetailsPanel().isOpened( folderContent.getDisplayName() );
    }
}
