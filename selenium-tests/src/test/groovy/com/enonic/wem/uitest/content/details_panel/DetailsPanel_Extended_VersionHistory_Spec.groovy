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

    def "GIVEN existing content is selected AND 'Version History' is opened WHEN latest 'version item' clicked THEN 'version info' appears"()
    {
        given: "saving of new folder content"
        folderContent = buildFolderContent( "version_info", "version_info_test" );
        addContent( folderContent );
        and: "the content was selected and details panel opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "latest 'version item' has been clicked"
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );

        then: "'version info' panel should be shown"
        versionItem.isVersionInfoDisplayed();
    }

    def "GIVEN existing content was selected AND 'Version History' is opened WHEN latest 'version item' was clicked THEN correct 'version info' should be displayed"()
    {
        given: "existing content was selected and version history opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "'Version History' option opened and first item expanded"
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        String versionId = versionItem.getId();
        saveScreenshot( "version-history-expanded" );

        then: "correct display name should be shown"
        versionItem.getDisplayName() == folderContent.getDisplayName();

        and: "version-id should be present"
        !versionId.isEmpty();

        and: "close button for the version view should be displayed"
        versionItem.isCloseButtonDisplayed( versionId );

        and: "message 'this version is active' should be present"
        versionItem.getVersionStatus( versionId ) == ContentVersionInfoView.ACTIVE_VERSION;

        and: "timestamp should be displayed on the info-view"
        String timestamp = versionItem.getTimeStamp( versionId );
        timestamp != null;

        and: "string successfully parsed to date time"
        LocalDateTime ldt = LocalDateTime.parse( timestamp, DateTimeFormatter.ofPattern( "yyy-MM-dd HH:mm:ss" ) );
    }


    def "GIVEN existing content is selected and version history opened  WHEN first version item was expanded THEN required version-info should be displayed"()
    {
        given: "existing content is selected and version history opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "'Version History' option opened and first item expanded"
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        String versionId = versionItem.getId();
        saveScreenshot( "version-history-expanded-no-display-name" );


        then: "correct display name should be empty"
        versionItem.getDisplayName() == "";

        and: "correct version id should be displayed"
        !versionId.isEmpty();

        and: "close button for the version view should be displayed"
        versionItem.isCloseButtonDisplayed( versionId );

        and:
        versionItem.getVersionStatus( versionId ) == ContentVersionInfoView.RESTORE_THIS;
    }

    def "GIVEN content was selected AND version info expanded  WHEN 'close info' button has been clicked THEN 'version info' is getting hidden"()
    {
        given: "content is selected and version history is opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();

        and: "version history panel has been opened "
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        String versionId = versionItem.getId();

        when: "'close version info' button was clicked"
        versionItem.doCloseVersionInfo( versionId );
        saveScreenshot( "version-history-collapsed" );

        then: "'version info' is getting hidden"
        !versionItem.isVersionInfoDisplayed();
    }

    def "GIVEN existing content AND version info is opened WHEN content was changed THEN new display name should be present on the 'version info'"()
    {
        when: "existing content is opened new display name typed"
        findAndSelectContent( folderContent.getName() ).clickToolbarEditAndSwitchToWizardTab().typeDisplayName(
            NEW_DISPLAY_NAME ).save().closeBrowserTab().switchToBrowsePanelTab();
        and: "details panel has been opened"
        contentBrowsePanel.clickOnDetailsToggleButton();

        then: "new display name should be present on the 'version info"
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        saveScreenshot( "version-history-new-display-name" );
        versionItem.getDisplayName() == NEW_DISPLAY_NAME;
    }
}
