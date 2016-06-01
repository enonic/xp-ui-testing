package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentVersionInfoView
import com.enonic.autotests.utils.TestUtils
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
        contentBrowsePanel.clickToolbarEdit().typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );

        when: "the folder has been published"
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );

        and: "one of the previous versions was restored"
        versionItem.doRestoreVersion( versionItem.getId() );

        and: "the version view has been expanded"
        allContentVersionsView.clickOnVersionAndExpand( 1 );
        TestUtils.saveScreenshot( getSession(), "versions_out_of_date" );

        then: "'out-of-date' status displayed in the version view"
        versionItem.getContentStatus( versionItem.getId() ) == ContentStatus.OUT_OF_DATE.getValue();

        and: "'out-of-date' status displayed in the Browse panel"
        contentBrowsePanel.getContentStatus( FOLDER_CONTENT.getName() ) == ContentStatus.OUT_OF_DATE.getValue();
    }
}