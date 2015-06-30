package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseItemsSelectionPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentItemVersionsPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentVersion
import com.enonic.autotests.vo.contentmanager.WorkspaceName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_ItemsSelectionPanel_VersionHistorySpec
    extends BaseContentSpec
{
    @Shared
    Content folderContent;

    def "WHEN content selected THEN TabMenu button appears"()
    {
        setup:
        itemsSelectionPanel = contentBrowsePanel.getItemSelectionPanel();
        folderContent = buildFolderContent( "v_history", "version_history_test" );

        and:
        addContent( folderContent );

        when: "when one content selected in the 'Browse Panel'"
        findAndSelectContent( folderContent.getName() );

        then: "the tab menu button with text equals 'Preview' appears in the 'Items Selection Panel'"
        itemsSelectionPanel.waitTabMenuButtonVisible();
    }

    def "GIVEN content selected WHEN TabMenu clicked THEN two menu items showed"()
    {
        given: "content selected"
        findAndSelectContent( folderContent.getName() );

        when: "TabMenu button clicked'"
        itemsSelectionPanel.clickOnTabMenuButton();

        then: "two menu items:'Preview' and 'Version History' appears"
        itemsSelectionPanel.isPresentMenuItem( ContentBrowseItemsSelectionPanel.PREVIEW );
        and:
        itemsSelectionPanel.isPresentMenuItem( ContentBrowseItemsSelectionPanel.VERSION_HISTORY );
    }

    def "GIVEN content selected AND TabMenu clicked WHEN 'Version History' menu item clicked THEN the 'ContentItemVersionsPanel' appears"()
    {
        given: "content selected"
        findAndSelectContent( folderContent.getName() );

        when: "'Version History' item selected'"
        ContentItemVersionsPanel versionPanel = itemsSelectionPanel.openVersionHistory();

        then: "version panel for the content is loaded"
        versionPanel.waitUntilLoaded();
        and: "'all versions' button present"
        versionPanel.isAllVersionsTabBarItemPresent();
        and: "'active versions' button present"
        versionPanel.isActiveVersionsTabBarItemPresent();
    }

    def "GIVEN version history opened WHEN 'preview' menu item clicked THEN the 'ContentItemPreviewPanel' showed"()
    {
        given: "content selected and the 'Version History' opened"
        findAndSelectContent( folderContent.getName() );
        ContentItemVersionsPanel versionPanel = itemsSelectionPanel.openVersionHistory();

        when: "the 'Preview' button clicked "
        versionPanel.openItemsSelectionPanel();

        then: "items-selection panel showed and version panel not displayed"
        versionPanel.waitUntilPanelNotVisible();
    }

    def "GIVEN content with two versions AND none are published WHEN All Versions listed THEN the latest version has a light blue 'draft' badge by it."()
    {
        given: "content selected"
        findAndSelectContent( folderContent.getName() );

        when: "'Active versions'  button clicked"
        ContentItemVersionsPanel versionPanel = itemsSelectionPanel.openVersionHistory();
        versionPanel.clickOnAllVersionsButton()
        LinkedList<String> versionsInfo = versionPanel.getAllContentVersionsInfo();

        then: "date time and displayName of content showed"
        versionsInfo.getFirst().contains( folderContent.getDisplayName() )
        and: "one active version with 'draft' status showed"
        versionPanel.getAllContentVersions().getFirst().getStatus().contains( WorkspaceName.DRAFT.getValue() )
    }

    def "GIVEN content with two versions WHEN published AND All Versions listed THEN the latest versions has a green 'master'"()
    {
        given: "content selected"
        findAndSelectContent( folderContent.getName() );

        when: "content published and 'Active versions'  button clicked"
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();
        ContentItemVersionsPanel versionPanel = itemsSelectionPanel.openVersionHistory();
        versionPanel.clickOnActiveVersionsButton();
        LinkedList<ContentVersion> contentVersions = versionPanel.getActiveContentVersions();

        then: "version has status 'master'"
        contentVersions.getFirst().getStatus().contains( WorkspaceName.MASTER.getValue() );
    }

    def "GIVEN content with the a published version that is later changed WHEN Active versions listed THEN two versions are listed. The older one with green 'master' badge and the newer one with a light blue 'draft' badge."()
    {
        given: "content with 'online' status was changed and content has a 'Modified' status"
        findAndSelectContent( folderContent.getName() ).clickToolbarEdit().typeDisplayName( "newDisplayName" ).save().close(
            "newDisplayName" );

        when: "'Active versions'  button clicked and active versions showed"
        ContentItemVersionsPanel versionPanel = itemsSelectionPanel.openVersionHistory();
        versionPanel.clickOnActiveVersionsButton();
        LinkedList<ContentVersion> contentVersions = versionPanel.getActiveContentVersions();

        then: "the latest active version has a 'draft' badge"
        contentVersions.poll().getStatus() == WorkspaceName.DRAFT.getValue();
        and: "previous version has a master badge"
        contentVersions.peek().getStatus().contains( WorkspaceName.MASTER.getValue() )
    }

    def "GIVEN content with the a published version that is later changed WHEN all versions listed THEN two versions are listed. The older one with green 'master' badge and the newer one with a light blue 'draft' badge."()
    {
        given: "content with 'online' status was changed and content has a 'Modified' status"
        findAndSelectContent( folderContent.getName() );

        when: "'Active versions'  button clicked and active versions showed"
        ContentItemVersionsPanel versionPanel = itemsSelectionPanel.openVersionHistory();
        versionPanel.clickOnAllVersionsButton();
        LinkedList<ContentVersion> contentVersions = versionPanel.getAllContentVersions();

        then: "the latest active version has a 'draft' badge"
        contentVersions.poll().getStatus() == WorkspaceName.DRAFT.getValue();
        and: "previous version has a master badge"
        contentVersions.peek().getStatus().contains( WorkspaceName.MASTER.getValue() )
    }
}
