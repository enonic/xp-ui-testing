package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentDetailsPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentItemVersionsPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentVersion
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_ContentDetails_VersionHistorySpec
    extends BaseContentSpec
{
    @Shared
    Content folderContent;

    @Shared
    Integer FIRST_NUMBER_OF_VERSIONS = 2;


    def "GIVEN content selected  WHEN 'Version History' option selected THEN panel with all versions for the content is loaded"()
    {
        given: "content selected and details panel opened"
        folderContent = buildFolderContent( "version_h_", "version_history_test" );
        addContent( folderContent );
        and: "'Content Details Panel' is shown"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "'Version History' option selected'"
        ContentItemVersionsPanel contentItemVersionsPanel = contentDetailsPanel.openVersionHistory();

        then: "panel with all versions for the content is loaded"
        contentItemVersionsPanel.isLoaded();
    }

    def "GIVEN content selected  WHEN 'Version History' option selected THEN three versions are present in the versions panel"()
    {
        given: "content selected and details panel opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "'Version History' option selected'"
        ContentItemVersionsPanel contentItemVersionsPanel = contentDetailsPanel.openVersionHistory();
        LinkedList<ContentVersion> allVersions = contentItemVersionsPanel.getAllContentVersions();

        then: "three versions are present in the panel"
        allVersions.size() == FIRST_NUMBER_OF_VERSIONS;

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
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentItemVersionsPanel contentItemVersionsPanel = contentDetailsPanel.openVersionHistory();

        when: "content published and 'Active versions'  button clicked"
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();
        TestUtils.saveScreenshot( getTestSession(), "vh_online" )
        LinkedList<ContentVersion> contentVersions = contentItemVersionsPanel.getAllContentVersions();

        then: "the number of versions not increased"
        contentVersions.size() == FIRST_NUMBER_OF_VERSIONS;
        and: "latest version has status 'online'"
        contentVersions.getFirst().getStatus().equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
    }

    def "GIVEN content with the a published version that is later changed WHEN versions panel opened THEN three versions are listed. The older one with green 'online' badge and the newer one with a gray 'Modified' badge."()
    {
        given: "content with 'online' status was changed and content has a 'Modified' status"
        findAndSelectContent( folderContent.getName() ).clickToolbarEdit().typeDisplayName( "newDisplayName" ).save().close(
            "newDisplayName" );

        when: "'Version Panel' opened"
        ContentItemVersionsPanel versionPanel = openVersionPanel();
        LinkedList<ContentVersion> contentVersions = versionPanel.getAllContentVersions();

        then: "number of versions increased"
        contentVersions.size() - FIRST_NUMBER_OF_VERSIONS == 1;

        and: "the latest  version has a 'modified' badge"
        contentVersions.poll().getStatus().equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );

        and: "previous version has a 'online' badge"
        contentVersions.peek().getStatus().equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
    }

    private ContentItemVersionsPanel openVersionPanel()
    {
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentDetailsPanel contentDetailsPanel = contentBrowsePanel.getContentDetailsPanel();
        ContentItemVersionsPanel contentItemVersionsPanel = contentDetailsPanel.openVersionHistory();
        return contentItemVersionsPanel;
    }
}
