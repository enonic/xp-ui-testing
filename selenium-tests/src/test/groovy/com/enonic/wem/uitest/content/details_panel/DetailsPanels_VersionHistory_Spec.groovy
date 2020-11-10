package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentVersion
import spock.lang.Shared
import spock.lang.Stepwise
import com.enonic.autotests.utils.TimeUtils

import java.time.LocalDateTime
import java.time.LocalTime

@Stepwise
class DetailsPanels_VersionHistory_Spec
    extends BaseVersionHistorySpec
{
    @Shared
    Content folderContent;

    def "Precondition: test folder should be created"()
    {
        given: "content is added"
        folderContent = buildFolderContent( "version_h_", "version_history_test" );
        addContent( folderContent );
    }

    def "GIVEN existing folder is selected WHEN 'Version History' has been opened THEN two versions should be present in the widget"()
    {
        given: "content selected and details panel opened"
        findAndSelectContent( folderContent.getName() );

        when: "'Version History' option selected'"
        VersionHistoryWidget versionHistoryWidget = openVersionPanel();
        LinkedList<ContentVersion> allVersions = versionHistoryWidget.getAllVersions();
        saveScreenshot( "default_number_of_versions" )

        then: "two versions should be present in the versions panel"
        allVersions.size() == INITIAL_NUMBER_OF_VERSIONS;

        and: "first version should has 'Created' action"
        allVersions.getLast().getAction() == 'Created';

        and: "super user should be 'modifier'"
        allVersions.getLast().getModifier() == "Super User";

        and: "expected 'modified time' should be displayed"
        LocalTime actualTime = LocalTime.parse( allVersions.getLast().getModified() );
        LocalTime.now().isAfter( actualTime );
    }

    def "GIVEN existing folder is selected WHEN the content has been published THEN 'Published' status should be present in the widget"()
    {
        given: "existing content is selected"
        findAndSelectContent( folderContent.getName() );
        VersionHistoryWidget versionHistoryWidget = openVersionPanel();
        contentBrowsePanel.clickOnMarkAsReadySingleContent();
        contentBrowsePanel.waitForNotificationMessage();

        when: "content has been published"
        contentBrowsePanel.clickToolbarPublish().clickOnPublishButton();
        saveScreenshot( "history_panel_content_was_published" )
        LinkedList<ContentVersion> contentVersions = versionHistoryWidget.getAllVersions();

        then: "the number of versions should be increased, because this content was marked as ready"
        contentVersions.size() == INITIAL_NUMBER_OF_VERSIONS + 1;

        and: "'Published' status should be present in the widget:"
        versionHistoryWidget.getContentStatus().equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }

    def "GIVEN existing 'Published' content has been updated WHEN versions panel has been opened THEN three versions should be present. 'Modified' status should be in the widget."()
    {
        given: "existing 'Published' content has been updated"
        findAndSelectContent( folderContent.getName() ).clickToolbarEditAndSwitchToWizardTab().typeDisplayName(
            "newDisplayName" ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "'Version Panel' is opened"
        VersionHistoryWidget versionHistoryWidget = openVersionPanel();
        saveScreenshot( "version_history_content-modified" )
        LinkedList<ContentVersion> contentVersions = versionHistoryWidget.getAllVersions();

        then: "number of versions should be increased by 1"
        contentVersions.size() == INITIAL_NUMBER_OF_VERSIONS + 2;

        and: "'Modified' status should be in the widget"
        versionHistoryWidget.getContentStatus().equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );
    }

    def "GIVEN 'modified'-content is selected WHEN the folder has been 'Mark as Deleted' THEN 'Marked for deletion' status should be in the widget"()
    {
        given: "the content is selected and Delete content dialog is opened "
        findAndSelectContent( folderContent.getName() ).clickToolbarDelete().clickOnMarkAsDeletedMenuItem();

        when: "Versions panel has been opened"
        VersionHistoryWidget versionHistoryWidget = openVersionPanel();
        LinkedList<ContentVersion> contentVersions = versionHistoryWidget.getAllVersions();
        saveScreenshot( "version_panel_deleted" );

        then: "number of versions should not be increased"
        contentVersions.size() == INITIAL_NUMBER_OF_VERSIONS + 2;

        and: "'Marked for deletion' status should be in the widget"
        versionHistoryWidget.getContentStatus().equalsIgnoreCase( ContentStatus.MARKED_FOR_DELETION.getValue() );
    }
}
