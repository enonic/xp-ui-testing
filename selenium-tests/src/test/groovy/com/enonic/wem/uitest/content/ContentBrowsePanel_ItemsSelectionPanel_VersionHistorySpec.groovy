package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentDetailsPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentItemVersionsPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentVersion
import com.enonic.autotests.vo.contentmanager.WorkspaceName
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_ItemsSelectionPanel_VersionHistorySpec
    extends BaseContentSpec
{
    @Shared
    Content folderContent;

    @Shared
    Integer FIRST_NUMBER_OF_VERSIONS = 2;

    def "WHEN no one content selected THEN 'Details Panel Toggle' button is enabled"()
    {
        when: "no one content selected"
        int numberOfSelectedItems = contentBrowsePanel.getNumberFromClearSelectionLink();

        then: "'Details Panel Toggle' button is disabled"
        contentBrowsePanel.isDetailsPanelToggleButtonDisplayed();

        and: "number of selected items is 0"
        numberOfSelectedItems == 0;

        and:
        !contentDetailsPanel.isOpened();
    }

    def "WHEN content selected THEN correct display name shown in the Detail Panel"()
    {
        given:
        folderContent = buildFolderContent( "v_history", "version_history_test" );
        addContent( folderContent );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "when one content selected in the 'Browse Panel'"
        findAndSelectContent( folderContent.getName() );

        then: "correct display name shown in the Detail Panel"
        contentDetailsPanel.getContentDisplayName() == folderContent.getDisplayName();
    }

    def "GIVEN 'Content Details Panel' opened WHEN Toggle Content Details button clicked THEN 'Content Details Panel' hidden"()
    {
        given: "content selected and the 'Version History' opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        TestUtils.saveScreenshot( getSession(), "detail-panel-opened" );

        when: "'Toggle' button clicked again "
        contentBrowsePanel.clickOnDetailsToggleButton();
        TestUtils.saveScreenshot( getSession(), "detail-panel-closed" );

        then: "'Content Details Panel' not displayed"
        !contentBrowsePanel.getContentDetailsPanel().isOpened( folderContent.getDisplayName() );
    }

    def "GIVEN content selected  WHEN 'Version History' option selected THEN panel with all versions for the content is loaded"()
    {
        given: "content selected and details panel opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "'Version History' option selected'"
        ContentItemVersionsPanel contentItemVersionsPanel = contentDetailsPanel.selectVersionHistoryOptionItem();

        then: "panel with all versions for the content is loaded"
        contentItemVersionsPanel.isLoaded();
    }

    def "GIVEN content selected  WHEN 'Version History' option selected THEN three versions are present in the versions panel"()
    {
        given: "content selected and details panel opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();

        when: "'Version History' option selected'"
        ContentItemVersionsPanel contentItemVersionsPanel = contentDetailsPanel.selectVersionHistoryOptionItem();
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

    @Ignore
    def "GIVEN a existing content  WHEN content  published THEN the latest versions has a 'online' badge"()
    {
        given: "content selected"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentItemVersionsPanel contentItemVersionsPanel = contentDetailsPanel.selectVersionHistoryOptionItem();


        when: "content published and 'Active versions'  button clicked"
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();
        TestUtils.saveScreenshot( getTestSession(), "vh_online" )
        LinkedList<ContentVersion> contentVersions = contentItemVersionsPanel.getAllContentVersions();

        then: "the number of versions not increased"
        contentVersions.size() == FIRST_NUMBER_OF_VERSIONS;
        and: "latest version has status 'online'"
        contentVersions.getFirst().getStatus().contains( ContentStatus.ONLINE.getValue() );

    }

    @Ignore
    def "GIVEN content with the a published version that is later changed WHEN versions panel opened THEN two versions are listed. The older one with green 'master' badge and the newer one with a light blue 'draft' badge."()
    {
        given: "content with 'online' status was changed and content has a 'Modified' status"
        findAndSelectContent( folderContent.getName() ).clickToolbarEdit().typeDisplayName( "newDisplayName" ).save().close(
            "newDisplayName" );

        when: "'Active versions'  button clicked and active versions showed"
        ContentItemVersionsPanel versionPanel = openVersionPanel();
        versionPanel.clickOnActiveVersionsButton();
        LinkedList<ContentVersion> contentVersions = versionPanel.getActiveContentVersions();

        then: "the latest active version has a 'draft' badge"
        contentVersions.poll().getStatus() == WorkspaceName.DRAFT.getValue();
        and: "previous version has a master badge"
        contentVersions.peek().getStatus().contains( WorkspaceName.MASTER.getValue() )
    }

    @Ignore
    def "GIVEN content with the a published version that is later changed WHEN all versions listed THEN two versions are listed. The older one with green 'master' badge and the newer one with a light blue 'draft' badge."()
    {
        given: "content with 'online' status was changed and content has a 'Modified' status"
        findAndSelectContent( folderContent.getName() );

        when: "'Active versions'  button clicked and active versions showed"
        ContentItemVersionsPanel versionPanel = openVersionPanel();
        versionPanel.clickOnAllVersionsButton();
        LinkedList<ContentVersion> contentVersions = versionPanel.getAllContentVersions();

        then: "the latest active version has a 'draft' badge"
        contentVersions.poll().getStatus() == WorkspaceName.DRAFT.getValue();
        and: "previous version has a master badge"
        contentVersions.peek().getStatus().contains( WorkspaceName.MASTER.getValue() )
    }

    private ContentItemVersionsPanel openVersionPanel()
    {
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentDetailsPanel contentDetailsPanel = contentBrowsePanel.getContentDetailsPanel();
        ContentItemVersionsPanel contentItemVersionsPanel = contentDetailsPanel.selectVersionHistoryOptionItem();
        return contentItemVersionsPanel;

    }
}
