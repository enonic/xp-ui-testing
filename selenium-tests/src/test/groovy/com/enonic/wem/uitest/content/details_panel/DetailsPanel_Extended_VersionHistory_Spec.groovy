package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Stepwise
class DetailsPanel_Extended_VersionHistory_Spec
    extends BaseContentSpec
{

    @Shared
    Content folderContent;

    @Shared
    String NEW_DISPLAY_NAME = "version-info-changed"

    def "GIVEN existing content is selected AND 'Version History' is opened WHEN latest 'version item' has been clicked THEN 'version info' should appear"()
    {
        given: "new folder has been added"
        folderContent = buildFolderContent( "version_info", "version_info_test" );
        addContent( folderContent );
        and: "the content is selected and details panel opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.openContentDetailsPanel();

        when: "latest 'version item' has been clicked"
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        allContentVersionsView.clickOnVersionAndExpand( 0 );

        then: "'version item' should be expanded or active"
        allContentVersionsView.isVersionInfoExpanded( 0 );
    }

    def "GIVEN existing content is selected AND 'Version History' is opened WHEN latest 'version item' has been expanded THEN expected 'version info' should be displayed"()
    {
        given: "existing content was selected and version history opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.openContentDetailsPanel();

        when: "'Version History' option opened and first item expanded"
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        saveScreenshot( "version-history-expanded" );

        then: "Super User should be owner in the vew"
        versionItem.getOwnerName(0) == "Super User";

        and: "'this version should be active"
        allContentVersionsView.isVersionActive( 0 );
    }

    def "GIVEN existing content is selected and version history is opened WHEN first version item has been expanded THEN version-info should be expanded AND Restore button gets visible"()
    {
        given: "existing content is selected and version history opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.openContentDetailsPanel();

        when: "'Version History' option opened and first item expanded"
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        saveScreenshot( "version-history-expanded-no-display-name" );

        then: "two versions should be displayed on the panel"
        allContentVersionsView.getAllVersions().size() == 2;

        and: "correct version id should be displayed"
        allContentVersionsView.isVersionInfoExpanded( 1 );

        and: "Revert button should be displayed in the expanded view"
        versionItem.isRevertButtonDisplayed();
    }

    def "GIVEN folder is selected AND a version info is expanded  WHEN this version item has been clicked THEN 'version item' gets collapsed"()
    {
        given: "content is selected and version history is opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.openContentDetailsPanel();

        and: "version history panel has been opened "
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );

        when: "'close version info' button was clicked"
        allContentVersionsView.clickOnVersionAndCloseView( 1 );
        saveScreenshot( "version-history-item-collapsed" );

        then: "'Revert' button gets  hidden"
        !versionItem.isRevertButtonDisplayed();

        and: "version item gets collapsed"
        !allContentVersionsView.isVersionInfoExpanded( 1 );
    }
}
