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
        contentBrowsePanel.isDetailsPanelToggleButtonDisplayed();

        and: "number of selected items is 0"
        numberOfSelectedItems == 0;
        and:
        contentDetailsPanel.isPanelEmpty();
    }

    def "WHEN content selected THEN correct display name shown in the Detail Panel"()
    {
        given:
        folderContent = buildFolderContent( "v_history", "version_history_test" );
        addContent( folderContent );

        when: "when one content selected in the 'Browse Panel'"
        findAndSelectContent( folderContent.getName() );

        then: "correct display name shown in the Detail Panel"
        contentDetailsPanel.getContentDisplayName() == folderContent.getDisplayName();
    }

    def "GIVEN content selected  WHEN 'Version History' option selected THEN panel with all versions for the content is loaded"()
    {
        given: "content selected"
        findAndSelectContent( folderContent.getName() );

        when: "'Version History' option selected'"
        ContentItemVersionsPanel contentItemVersionsPanel = contentDetailsPanel.selectVersionHistoryOptionItem();

        then: "panel with all versions for the content is loaded"
        contentItemVersionsPanel.isLoaded();
    }

    @Ignore
    def "GIVEN 'Content Details Panel' opened WHEN Toggle Content Details button clicked THEN 'Content Details Panel' hidden"()
    {
        given: "content selected and the 'Version History' opened"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        boolean isPanelOpened = contentDetailsPanel.isOpened( folderContent.getDisplayName() );
        TestUtils.saveScreenshot( getSession(), "detail-panel-opened" );

        when: "'Toggle' button clicked again "
        contentBrowsePanel.clickOnDetailsToggleButton();
        TestUtils.saveScreenshot( getSession(), "detail-panel-closed" );

        then: "'Content Details Panel' not displayed"
        !contentBrowsePanel.getContentDetailsPanel().isOpened( folderContent.getDisplayName() );
        and: "but it was opened"
        isPanelOpened;
    }

    @Ignore
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

        ContentItemVersionsPanel versionPanel = openVersionPanel();
        versionPanel.clickOnActiveVersionsButton();
        TestUtils.saveScreenshot( getTestSession(), "vh_active" )
        LinkedList<ContentVersion> contentVersions = versionPanel.getActiveContentVersions();

        then: "version has two statuses"
        contentVersions.size() == 2;
        and: "version has status 'master'"
        contentVersions.getFirst().getStatus().contains( WorkspaceName.MASTER.getValue() );
        and: " version has 'draft' as well"
        contentVersions.getLast().getStatus().contains( WorkspaceName.DRAFT.getValue() );


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
        ContentDetailsPanel contentDetailsPanel = contentBrowsePanel.getContentDetailsPanel();
        ContentItemVersionsPanel contentItemVersionsPanel = contentDetailsPanel.selectVersionHistoryOptionItem();
        return contentItemVersionsPanel;

    }
}
