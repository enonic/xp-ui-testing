package com.enonic.wem.uitest.content

import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Ignore
import spock.lang.Shared

class ContentBrowsePanel_DetailsPanel_ContentDetails_Spec
    extends BaseContentSpec
{
    @Shared
    Content folderContent;

    @Shared
    String UNNAMED_SITE_DISPLAY_NAME = "<Unnamed Site>";

    @Ignore
    def "GIVEN site wizard opened AND and HomeButton clicked WHEN unnamed content selected in greed THEN correct display name is shown"()
    {
        given: "site wizard opened AND and HomeButton clicked"
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.site() );
        contentBrowsePanel.pressAppHomeButton();

        when: "unnamed content selected in greed"
        filterPanel.typeSearchText( "unnamed" )
        contentBrowsePanel.selectRowByDisplayName( UNNAMED_SITE_DISPLAY_NAME );
        contentBrowsePanel.clickOnDetailsToggleButton();

        then: "correct display name is shown"
        contentDetailsPanel.getContentDisplayName() == UNNAMED_SITE_DISPLAY_NAME;
    }

    def "WHEN no one content selected THEN 'Details Panel Toggle' button is displayed AND details panel not displayed"()
    {
        when: "no one content selected"
        int numberOfSelectedItems = contentBrowsePanel.getNumberFromClearSelectionLink();

        then: "'Details Panel Toggle' button is displayed"
        contentBrowsePanel.isDetailsPanelToggleButtonDisplayed();

        and: "number of selected items is 0"
        numberOfSelectedItems == 0;

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
        TestUtils.saveScreenshot( getSession(), "detail-panel-opened" );

        when: "'Toggle' button clicked"
        contentBrowsePanel.clickOnDetailsToggleButton();
        TestUtils.saveScreenshot( getSession(), "detail-panel-closed" );

        then: "'Content Details Panel' not displayed"
        !contentBrowsePanel.getContentDetailsPanel().isOpened( folderContent.getDisplayName() );
    }
}
