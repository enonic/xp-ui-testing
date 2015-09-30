package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentDetailsPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentItemVersionsPanel
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

    def "WHEN no one content selected THEN 'Details Panel Toggle' button is enabled"()
    {
        when: "no one content selected"
        int numberOfSelectedItems = contentBrowsePanel.getNumberFromClearSelectionLink();

        then: "'Details Panel Toggle' button is disabled"
        !contentBrowsePanel.isDetailsPanelToggleButtonDisabled();
        and: "number of selected items is 0"
        numberOfSelectedItems == 0;
    }

    def "WHEN content selected THEN 'Details Panel Toggle' button is enabled"()
    {
        given:
        itemsSelectionPanel = contentBrowsePanel.getItemSelectionPanel();
        folderContent = buildFolderContent( "v_history", "version_history_test" );
        addContent( folderContent );

        when: "when one content selected in the 'Browse Panel'"
        findAndSelectContent( folderContent.getName() );
        int numberOfSelectedItems = contentBrowsePanel.getNumberFromClearSelectionLink();

        then: "'Details Panel Toggle' button is enabled"
        !contentBrowsePanel.isDetailsPanelToggleButtonDisabled();
        and: "number of selected items is 1"
        numberOfSelectedItems == 1;
    }

    def "GIVEN content selected WHEN TabMenu clicked THEN two menu items showed"()
    {
        given: "content selected"
        findAndSelectContent( folderContent.getName() );

        when: "'Details Panel Toggle' button clicked'"
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentDetailsPanel contentDetailsPanel = contentBrowseItemPanel.getContentDetailsPanel();

        then: "Content Details Panel opened"
        contentDetailsPanel.isOpened();
        and: "correct content-displayName shown"
        contentDetailsPanel.getContentDisplayName() == folderContent.getDisplayName();
    }

    def "GIVEN content selected AND 'Details Toggle' button clicked WHEN 'Version History' option selected THEN the 'ContentItemVersionsPanel' appears"()
    {
        given: "content selected"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentDetailsPanel contentDetailsPanel = contentBrowseItemPanel.getContentDetailsPanel();

        when: "'Version History' option selected'"
        ContentItemVersionsPanel contentItemVersionsPanel = contentDetailsPanel.selectVersionHistoryOptionItem();


        then: "version panel for the content is loaded"
        contentItemVersionsPanel.isOpened();
        and: "'all versions' button present"
        contentItemVersionsPanel.isAllVersionsTabBarItemPresent();
        and: "'active versions' button present"
        contentItemVersionsPanel.isActiveVersionsTabBarItemPresent();
    }

    def "GIVEN 'Content Details Panel' opened WHEN Toggle Content Details button clicked THEN 'Content Details Panel' hidden"()
    {
        given: "content selected and the 'Version History' opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        boolean isPanelOpened = contentBrowseItemPanel.getContentDetailsPanel().isOpened();

        when: "'Toggle' button clicked again "
        contentBrowsePanel.clickOnDetailsToggleButton();
        TestUtils.saveScreenshot( getSession(), "detail-panel-closed" )

        then: "'Content Details Panel' not displayed"
        !contentBrowseItemPanel.getContentDetailsPanel().isOpened();
        and: "but it was opened"
        isPanelOpened;
    }


    def "GIVEN existing content AND it not published WHEN All Versions opened THEN the latest version has a light blue 'draft' badge by it."()
    {
        given: "content selected and the 'Version History' opened"
        findAndSelectContent( folderContent.getName() );
        ContentItemVersionsPanel contentItemVersionsPanel = openVersionPanel();

        when: "'All versions'  button clicked"
        contentItemVersionsPanel.clickOnAllVersionsButton()
        LinkedList<String> versionsInfo = contentItemVersionsPanel.getAllContentVersionsInfo();

        then: "displayName of content showed"
        versionsInfo.getFirst().contains( folderContent.getDisplayName() )
        and: "one active version with 'draft' status showed"
        contentItemVersionsPanel.getAllContentVersions().getFirst().getStatus().contains( WorkspaceName.DRAFT.getValue() )
    }

    @Ignore
    def "GIVEN content with two versions WHEN published AND All Versions listed THEN the latest versions has a green 'master'"()
    {
        given: "content selected"
        findAndSelectContent( folderContent.getName() );

        when: "content published and 'Active versions'  button clicked"
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();
        TestUtils.saveScreenshot( getTestSession(), "vh_tab" )
        ContentItemVersionsPanel versionPanel = openVersionPanel();
        versionPanel.clickOnActiveVersionsButton();
        LinkedList<ContentVersion> contentVersions = versionPanel.getActiveContentVersions();

        then: "version has status 'master'"
        contentVersions.getFirst().getStatus().contains( WorkspaceName.MASTER.getValue() );
    }

    @Ignore
    def "GIVEN content with the a published version that is later changed WHEN Active versions listed THEN two versions are listed. The older one with green 'master' badge and the newer one with a light blue 'draft' badge."()
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
        ContentDetailsPanel contentDetailsPanel = contentBrowseItemPanel.getContentDetailsPanel();
        ContentItemVersionsPanel contentItemVersionsPanel = contentDetailsPanel.selectVersionHistoryOptionItem();
        return contentItemVersionsPanel;

    }
}
