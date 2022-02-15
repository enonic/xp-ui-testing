package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared
@Ignore
class Restore_Version_Published_Folder_Spec
    extends BaseVersionHistorySpec
{
    @Shared
    Content FOLDER_CONTENT;

    @Shared
    String INITIAL_DISPLAY_NAME = "modified-name";

    @Shared
    String NEW_DISPLAY_NAME = "restore-modified";

    def "WHEN existing folder has been published AND previous version restored THEN 'Modified' status should be displayed in history widget AND in the Browse panel"()
    {
        given: "existing folder with several versions"
        FOLDER_CONTENT = buildFolderContent( "folder", INITIAL_DISPLAY_NAME );
        addContent( FOLDER_CONTENT );
        findAndSelectContent( FOLDER_CONTENT.getName() );
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab().typeDisplayName(
            NEW_DISPLAY_NAME ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the folder has been published"
        contentBrowsePanel.showPublishMenu().clickOnMarkAsReadyMenuItem();
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();
        VersionHistoryWidget versionHistoryWidget = openVersionPanel();
        ContentVersionInfoView versionItem = versionHistoryWidget.clickOnVersionItem( 1 );

        and: "one of the previous versions was reverted"
        versionItem.doRevertVersion();

        then: "'Modified' status should be displayed in History Widget"
        versionHistoryWidget.getContentStatus() == ContentStatus.MODIFIED.getValue();

        and: "'Modified' status should be displayed in the Browse panel"
        contentBrowsePanel.getContentStatus( FOLDER_CONTENT.getName() ) == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing content with 'Modified' status  AND version history is opened WHEN the content selected and 'Unpublish' menu item was clicked THEN spinner automatically disappears after a short interval"()
    {
        given: "existing content with 'Modified' status"
        findAndSelectContent( FOLDER_CONTENT.getName() );

        and: "version history is opened "
        VersionHistoryWidget versionHistoryWidget = contentBrowsePanel.openContentDetailsPanel().openVersionHistory();

        when:
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();
        contentUnPublishDialog.clickOnUnpublishButton();
        saveScreenshot( "folder_modified_unpublished" );

        then: "spinner automatically disappears after a short interval"
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        versionHistoryWidget.getContentStatus() == ContentStatus.UNPUBLISHED.getValue();
    }
}
