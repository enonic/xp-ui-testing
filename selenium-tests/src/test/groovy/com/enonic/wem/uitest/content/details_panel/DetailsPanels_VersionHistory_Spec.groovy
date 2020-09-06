package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentVersion
import spock.lang.Shared
import spock.lang.Stepwise
import com.enonic.autotests.utils.TimeUtils

@Stepwise
class DetailsPanels_VersionHistory_Spec
    extends BaseVersionHistorySpec
{
    @Shared
    Content folderContent;

    def "GIVEN content is selected WHEN 'Version History' has been opened THEN panel with versions should be loaded"()
    {
        given: "content is added"
        folderContent = buildFolderContent( "version_h_", "version_history_test" );
        addContent( folderContent );

        and: "the content is selected"
        findAndSelectContent( folderContent.getName() );

        when: "'Version History' option has been selected'"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        then: "panel with all versions should be loaded"
        allContentVersionsView.waitUntilLoaded();
    }

    def "GIVEN existing content is selected WHEN 'Version History' has been opened THEN two versions should be present in the panel"()
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

        and: "expected 'modified time' should be displayed"
        allVersions.getFirst().getModified().contains( TimeUtils.getNowDate() );
    }

    def "GIVEN existing content is selected WHEN the content has been published THEN the latest versions should be with 'Published' badge"()
    {
        given: "existing content is selected"
        findAndSelectContent( folderContent.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        contentBrowsePanel.showPublishMenu().clickOnMarkAsReadyMenuItem();

        when: "content has been published"
        contentBrowsePanel.clickToolbarPublish().clickOnPublishButton();
        saveScreenshot( "history_panel_content_was_published" )
        LinkedList<ContentVersion> contentVersions = allContentVersionsView.getAllVersions();

        then: "the number of versions should be increased, because this content was marked as ready"
        contentVersions.size() == INITIAL_NUMBER_OF_VERSIONS+ 1;

        and: "latest version should has 'published' status"
        contentVersions.getFirst().getStatus().equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }

    def "GIVEN existing 'Published' content has been changed and content gets 'Modified' WHEN versions panel has been opened THEN three versions should be present. The older one with green 'published' badge and the newer one with a gray 'Modified' badge."()
    {
        given: "existing 'Published' content has been changed and content is 'Modified'"
        findAndSelectContent( folderContent.getName() ).clickToolbarEditAndSwitchToWizardTab().typeDisplayName(
            "newDisplayName" ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "'Version Panel' is opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        saveScreenshot( "version_history_content-modified" )
        LinkedList<ContentVersion> contentVersions = allContentVersionsView.getAllVersions();

        then: "number of versions should be increased by 1"
        contentVersions.size() == INITIAL_NUMBER_OF_VERSIONS + 2;

        and: "the latest version has a 'modified' badge"
        contentVersions.poll().getStatus().equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );

        and: "previous version has a 'published' badge"
        contentVersions.peek().getStatus().equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }

    def "GIVEN 'modified'-content is selected WHEN 'Mark as Deleted' menu item in the Delete Dialog has been pressed THEN 'Deleted' status get Deleted in the Details Panel"()
    {
        given: "the content is selected and Delete content dialog is opened "
        findAndSelectContent( folderContent.getName() ).clickToolbarDelete().clickOnMarkAsDeletedMenuItem();

        when: "Versions panel has been opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        saveScreenshot( "version_panel_pending" )
        LinkedList<ContentVersion> contentVersions = allContentVersionsView.getAllVersions();

        then: "the latest version has 'deleted' badge"
        contentVersions.poll().getStatus().equalsIgnoreCase( ContentStatus.DELETED.getValue() );

        and: "previous version has a 'published' badge"
        contentVersions.peek().getStatus().equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }
}
