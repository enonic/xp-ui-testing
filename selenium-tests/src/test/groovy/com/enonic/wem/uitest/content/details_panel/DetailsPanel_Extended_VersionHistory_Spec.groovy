package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DetailsPanel_Extended_VersionHistory_Spec
    extends BaseContentSpec
{

    @Shared
    Content FOLDER;

    @Shared
    String NEW_DISPLAY_NAME = "version-info-changed"

    def "GIVEN existing content is selected AND 'Version History' is opened WHEN current 'version item' has been clicked THEN the item should be expanded"()
    {
        given: "new folder has been added"
        FOLDER = buildFolderContent( "version_info", "version_info_test" );
        addContent( FOLDER );
        and: "the content is selected and details panel opened"
        findAndSelectContent( FOLDER.getName() );
        contentBrowsePanel.openContentDetailsPanel();

        when: "latest 'version item' has been clicked"
        VersionHistoryWidget allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 0 );

        then: "the 'version item' should be expanded"
        allContentVersionsView.isVersionInfoExpanded( 0 );
        and: "this version should be active"
        allContentVersionsView.isVersionActive( 0 );
        and:
        versionItem.getOwnerName( 0 ) == "by Super User";
    }

    def "GIVEN existing content is selected and version history is opened WHEN penultimate version item has been expanded THEN version-info should be expanded AND Revert button gets visible"()
    {
        given: "existing content is selected and version history opened"
        findAndSelectContent( FOLDER.getName() );
        contentBrowsePanel.openContentDetailsPanel();

        when: "'Version History' widget has been opened and first item has been expanded"
        VersionHistoryWidget allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        saveScreenshot( "version-history-expanded-no-display-name" );

        then: "two versions should be displayed in the panel"
        allContentVersionsView.getAllVersions().size() == 2;

        and: "the version item should be expanded"
        allContentVersionsView.isVersionInfoExpanded( 1 );

        and: "Revert button should be displayed in the expanded view"
        versionItem.isRevertButtonDisplayed();
    }

    def "GIVEN the version item is expanded WHEN the version item has been clicked THEN 'version item' gets collapsed"()
    {
        given: "content is selected and version history is opened"
        findAndSelectContent( FOLDER.getName() );
        contentBrowsePanel.openContentDetailsPanel();

        and: "version history panel has been expanded"
        VersionHistoryWidget allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );

        when: "the version item has been clicked"
        allContentVersionsView.clickOnVersionItem( 1 );
        saveScreenshot( "version-history-item-collapsed" );

        then: "'Revert' button gets hidden"
        !versionItem.isRevertButtonDisplayed();

        and: "version item gets collapsed"
        !allContentVersionsView.isVersionInfoExpanded( 1 );
    }
}
