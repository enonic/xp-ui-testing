package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Restore_Version_Out_Of_Date_Spec
    extends BaseVersionHistorySpec
{
    @Shared
    Content FOLDER_CONTENT;

    @Shared
    String INITIAL_DISPLAY_NAME = "out-of-date-name";

    @Shared
    String NEW_DISPLAY_NAME = "restore-out-of-date";

    def "GIVEN existing folder with several versions WHEN the folder has been published AND previous version restored THEN 'out-of-date' status displayed in the version view AND in the Browse panel"()
    {
        given: "existing folder with several versions"
        FOLDER_CONTENT = buildFolderContent( "folder", INITIAL_DISPLAY_NAME );
        addContent( FOLDER_CONTENT );
        findAndSelectContent( FOLDER_CONTENT.getName() );
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab().typeDisplayName(
            NEW_DISPLAY_NAME ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the folder has been published"
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );

        and: "one of the previous versions was restored"
        versionItem.doRestoreVersion( versionItem.getId() );

        and: "the version view has been expanded"
        allContentVersionsView.clickOnVersionAndExpand( 1 );
        saveScreenshot( "versions_out_of_date" );

        then: "'out-of-date' status displayed in the version view"
        versionItem.getContentStatus( versionItem.getId() ) == ContentStatus.OUT_OF_DATE.getValue();

        and: "'out-of-date' status displayed in the Browse panel"
        contentBrowsePanel.getContentStatus( FOLDER_CONTENT.getName() ) == ContentStatus.OUT_OF_DATE.getValue();
    }
    //verifies the  XP-4156
    def "GIVEN existing content with 'out-of-date' status  AND version history is opened WHEN the content selected and 'Unpublish' menu item was clicked THEN spinner automatically disappears after a short interval "()
    {
        given: "existing content with 'out-of-date' status"
        findAndSelectContent( FOLDER_CONTENT.getName() );

        and: " version history is opened "
        contentBrowsePanel.openContentDetailsPanel().openVersionHistory();

        when:
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();
        contentUnPublishDialog.clickOnUnpublishButton();
        saveScreenshot( "out_of_date_unpublished" );

        then: "spinner automatically disappears after a short interval"
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
    }
}
