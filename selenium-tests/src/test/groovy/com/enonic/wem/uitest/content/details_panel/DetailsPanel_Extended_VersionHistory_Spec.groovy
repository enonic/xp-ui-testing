package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentVersionInfoView
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

    def "GIVEN existing content is selected AND 'Version History' opened  WHEN latest 'version item' clicked THEN 'version info' appears"()
    {
        given: "content added selected and version history opened"
        folderContent = buildFolderContent( "version_info", "version_info_test" );
        addContent( folderContent );
        and: "the content selected and details panel opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "latest 'version item' clicked"
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );

        then: "'version info' is shown"
        versionItem.isVersionInfoDisplayed();
    }

    def "GIVEN existing content selected AND 'Version History' opened  WHEN latest 'version item' clicked THEN correct 'version info' displayed"()
    {
        given: "existing content selected and version history opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "'Version History' option opened and first item expanded"
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        String versionId = versionItem.getId();

        then: "correct display name is shown"
        versionItem.getDisplayName() == folderContent.getDisplayName();

        and: "version id is displayed"
        !versionId.isEmpty();

        and: "close button for the version view is displayed"
        versionItem.isCloseButtonDisplayed( versionId );

        and: "message 'this version is active' is present"
        versionItem.getVersionStatus( versionId ) == ContentVersionInfoView.ACTIVE_VERSION;

        and: "timestamp displayed on the info-view"
        String timestamp = versionItem.getTimeStamp( versionId );
        timestamp != null;
        and: "string successfully parsed to date time"
        LocalDateTime ldt = LocalDateTime.parse( timestamp, DateTimeFormatter.ofPattern( "yyy-MM-dd HH:mm:ss" ) );
    }


    def "GIVEN content selected AND 'Version History' option selected  WHEN first version item clicked THEN version info displayed"()
    {
        given: "content added selected and version history opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "'Version History' option opened and first item expanded"
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        String versionId = versionItem.getId();


        then: "correct display name is shown"
        versionItem.getDisplayName() == "";

        and: "version id is displayed"
        !versionId.isEmpty();

        and: "close button for the version view is displayed"
        versionItem.isCloseButtonDisplayed( versionId );

        and:
        versionItem.getVersionStatus( versionId ) == ContentVersionInfoView.RESTORE_THIS;
    }

    def "GIVEN content selected AND version info expanded  WHEN 'close info' button clicked THEN 'version info' is hidden"()
    {
        given: "content added selected and version history opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        String versionId = versionItem.getId();

        when: "'close info' button clicked"
        versionItem.doCloseVersionInfo( versionId );

        then: "'version info' is hidden"
        !versionItem.isVersionInfoDisplayed();
    }


    def "GIVEN existing content AND version info opened WHEN content changed THEN new display name present in the 'version info'"()
    {
        when: "content added selected and version history opened"
        findAndSelectContent( folderContent.getName() ).clickToolbarEdit().typeDisplayName( NEW_DISPLAY_NAME ).save().close(
            NEW_DISPLAY_NAME );
        contentBrowsePanel.clickOnDetailsToggleButton();

        then: "new display name shown in the 'version info'"
        AllContentVersionsView allContentVersionsView = contentDetailsPanel.openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        versionItem.getDisplayName() == NEW_DISPLAY_NAME;
    }

}
