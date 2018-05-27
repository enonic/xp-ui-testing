package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentVersion
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DetailsPanels_VersionHistory_Spec
    extends BaseVersionHistorySpec
{
    @Shared
    Content folderContent;

    def "GIVEN content was added and selected  WHEN 'Version History' option is selected THEN panel with all versions of the content should be loaded"()
    {
        given: "content was added"
        folderContent = buildFolderContent( "version_h_", "version_history_test" );
        addContent( folderContent );

        and: "the content is selected"
        findAndSelectContent( folderContent.getName() );

        when: "'Version History' option has been selected'"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        then: "panel with all versions of the content should be loaded"
        allContentVersionsView.isLoaded();
    }

    def "GIVEN existing content is selected WHEN 'Version History' option is selected THEN two versions should be present on the versions panel"()
    {
        given: "content selected and details panel opened"
        findAndSelectContent( folderContent.getName() );

        when: "'Version History' option selected'"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        LinkedList<ContentVersion> allVersions = allContentVersionsView.getAllVersions();
        saveScreenshot( "default_number_of_versions" )

        then: "two versions should be present on the versions panel"
        allVersions.size() == INITIAL_NUMBER_OF_VERSIONS;

        and: "first version should has 'New' status"
        allVersions.getFirst().getStatus() == ContentStatus.NEW.getValue();

        and: "super user should be 'modifier'"
        allVersions.getFirst().getModifier() == "Super User";

        and: "correct 'modified time' should be displayed"
        allVersions.getFirst().getModified().contains( "minute ago" );
    }

    def "GIVEN existing content is selected WHEN the content has been published THEN the latest versions should has 'online' badge"()
    {
        given: "existing content is selected"
        findAndSelectContent( folderContent.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel()

        when: "content has been published"
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();
        saveScreenshot( "history_panel_content_was_published" )
        LinkedList<ContentVersion> contentVersions = allContentVersionsView.getAllVersions();

        then: "the number of versions should be the same"
        contentVersions.size() == INITIAL_NUMBER_OF_VERSIONS ;

        and: "latest version should has 'published' status"
        contentVersions.getFirst().getStatus().equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }

    def "GIVEN existing 'online' content was changed and content is getting 'Modified' WHEN versions panel is opened THEN three versions should be present. The older one with green 'online' badge and the newer one with a gray 'Modified' badge."()
    {
        given: "existing 'online' content was changed and content is getting 'Modified'"
        findAndSelectContent( folderContent.getName() ).clickToolbarEditAndSwitchToWizardTab().typeDisplayName(
            "newDisplayName" ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "'Version Panel' is opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        saveScreenshot( "version_history_content-modified" )
        LinkedList<ContentVersion> contentVersions = allContentVersionsView.getAllVersions();

        then: "number of versions should be increased by 1"
        contentVersions.size() == INITIAL_NUMBER_OF_VERSIONS + 1;

        and: "the latest version has a 'modified' badge"
        contentVersions.poll().getStatus().equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );

        and: "previous version has a 'published' badge"
        contentVersions.peek().getStatus().equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }

    def "GIVEN 'modified'-content was selected WHEN 'Delete' button on the toolbar was pressed THEN the newer one with a 'deleted' badge"()
    {
        given: "'modified' content was selected "
        findAndSelectContent( folderContent.getName() ).clickToolbarDelete().doDelete();

        when: "'Delete' button on the toolbar was pressed"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        saveScreenshot( "version_panel_pending" )
        LinkedList<ContentVersion> contentVersions = allContentVersionsView.getAllVersions();

        then: "the latest version has a 'deleted' badge"
        contentVersions.poll().getStatus().equalsIgnoreCase( ContentStatus.DELETED.getValue() );
    }
}
