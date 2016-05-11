package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.utils.TestUtils
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

    @Shared
    Integer INITIAL_NUMBER_OF_VERSIONS = 2;

    def "GIVEN content selected  WHEN 'Version History' option selected THEN panel with all versions for the content is loaded"()
    {
        given: "content added"
        folderContent = buildFolderContent( "version_h_", "version_history_test" );
        addContent( folderContent );
        and: "the content selected"
        findAndSelectContent( folderContent.getName() );

        when: "'Version History' option selected'"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        then: "panel with all versions for the content is loaded"
        allContentVersionsView.isLoaded();
    }

    def "GIVEN just added content selected WHEN 'Version History' option selected THEN two versions are present in the versions panel"()
    {
        given: "content selected and details panel opened"
        findAndSelectContent( folderContent.getName() );

        when: "'Version History' option selected'"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        LinkedList<ContentVersion> allVersions = allContentVersionsView.getAllVersions();
        TestUtils.saveScreenshot( getSession(), "two-versions" )

        then: "two versions are present in the panel"
        allVersions.size() == INITIAL_NUMBER_OF_VERSIONS;

        and: "first version has a 'offline' status"
        allVersions.getFirst().getStatus() == ContentStatus.OFFLINE.getValue();

        and: "super user is modifier"
        allVersions.getFirst().getModifier() == "Super User";

        and: "correct time is described"
        allVersions.getFirst().getModified().contains( "minute ago" );

    }

    def "GIVEN a existing content WHEN content published THEN the latest versions has a 'online' badge"()
    {
        given: "content selected"
        findAndSelectContent( folderContent.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel()

        when: "content published"
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();
        TestUtils.saveScreenshot( getTestSession(), "vh_online" )
        LinkedList<ContentVersion> contentVersions = allContentVersionsView.getAllVersions();

        then: "the number of versions not increased"
        contentVersions.size() == INITIAL_NUMBER_OF_VERSIONS;

        and: "latest version has status 'online'"
        contentVersions.getFirst().getStatus().equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
    }

    def "GIVEN 'published'-content WHEN content's display name changed AND versions panel opened THEN three versions are listed. The older one with green 'online' badge and the newer one with a gray 'Modified' badge."()
    {
        given: "content with 'online' status was changed and content has got a 'Modified' status"
        findAndSelectContent( folderContent.getName() ).clickToolbarEdit().typeDisplayName( "newDisplayName" ).save().close(
            "newDisplayName" );

        when: "'Version Panel' opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        TestUtils.saveScreenshot( getSession(), "online-modified" )
        LinkedList<ContentVersion> contentVersions = allContentVersionsView.getAllVersions();

        then: "number of versions increased to 3"
        contentVersions.size() - INITIAL_NUMBER_OF_VERSIONS == 1;

        and: "the latest version has a 'modified' badge"
        contentVersions.poll().getStatus().equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );

        and: "previous version has a 'online' badge"
        contentVersions.peek().getStatus().equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
    }

    def "GIVEN 'modified'-content selected WHEN 'Delete' button on the toolbar pressed THEN the newer one with a gray 'Pending delete' badge."()
    {
        given: "'modified' content selected "
        findAndSelectContent( folderContent.getName() ).clickToolbarDelete().doDelete();

        when: "'Delete' button on the toolbar pressed"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        TestUtils.saveScreenshot( getSession(), "online-modified" )
        LinkedList<ContentVersion> contentVersions = allContentVersionsView.getAllVersions();

        then: "the latest version has a 'pending delete' badge"
        contentVersions.poll().getStatus().equalsIgnoreCase( ContentStatus.PENDING_DELETE.getValue() );
    }
}
