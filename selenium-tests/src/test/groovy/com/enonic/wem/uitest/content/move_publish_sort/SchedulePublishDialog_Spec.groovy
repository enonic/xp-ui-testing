package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog
import com.enonic.autotests.pages.contentmanager.SchedulePublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.TimeUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 19.12.2016.
 *
 * Tasks:
 *    XP-4682 Add selenium tests for 'Scheduled Publishing Dialog'
 *    XP-4562 Add selenium tests for Time-based publishing
 * */
@Stepwise
class SchedulePublishDialog_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_FOLDER;

    @Shared
    String testTomorrowDateTime;

    @Shared
    String TEST_ONLINE_TO_VALUE = "2116-12-31 00:00";

    def "GIVEN existing folder is selected WHEN show schedule button pressed THEN 'SchedulePublishDialog' dialog should be displayed"()
    {
        given: "existing folder in root"
        TEST_FOLDER = buildFolderContent( "folder", "schedule dialog" );
        addContent( TEST_FOLDER );

        when: "show schedule button pressed"
        findAndSelectContent( TEST_FOLDER.getName() )
        SchedulePublishDialog schedulePublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL ).clickOnShowScheduleButton();

        then: "'SchedulePublishDialog' dialog should be displayed"
        schedulePublishDialog.isDisplayed();

        and: "'Cancel Top' button should be displayed"
        schedulePublishDialog.isCancelTopButtonDisplayed();

        and: "'Back' button should be displayed"
        schedulePublishDialog.isBackButtonDisplayed();

        and: "'Online From' input should be displayed"
        schedulePublishDialog.isOnlineFromInputDisplayed();

        and: "'Online To' input should be displayed"
        schedulePublishDialog.isOnlineToInputDisplayed();
    }

    def "GIVEN SchedulePublish dialog is opened WHEN 'Back' button was pressed THEN the dialog is not displayed"()
    {
        given: "SchedulePublish dialog is opened"
        findAndSelectContent( TEST_FOLDER.getName() )
        SchedulePublishDialog schedulePublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL ).clickOnShowScheduleButton();
        saveScreenshot( "schedule_dlg_displayed" );

        when: "'Back' button was pressed"
        schedulePublishDialog.clickOnBackButton();
        saveScreenshot( "schedule_dlg_back_pressed" );

        then: "schedule dialog should not be displayed"
        !schedulePublishDialog.isDisplayed();
    }

    def "GIVEN SchedulePublish dialog is opened AND 'online from' is empty WHEN 'Schedule' button was pressed THEN validation warning appears"()
    {
        given: "SchedulePublish dialog is opened AND 'online from' is empty"
        findAndSelectContent( TEST_FOLDER.getName() )
        SchedulePublishDialog schedulePublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL ).clickOnShowScheduleButton();
        saveScreenshot( "schedule_dlg_displayed" );

        when: "'Schedule' button was pressed"
        schedulePublishDialog.clickOnScheduleButton();
        saveScreenshot( "schedule_dlg_validation_message" );

        then: "validation warning appears"
        schedulePublishDialog.isValidationMessagePresent();

        and: "correct message should be displayed"
        schedulePublishDialog.getValidationMessage() == Application.REQUIRED_MESSAGE;
    }

    def "GIVEN SchedulePublish dialog is opened AND 'online from' is Tomorrow WHEN 'Schedule' button has been pressed THEN status 'Online (Pending)' is displayed"()
    {
        given: "SchedulePublish dialog is opened AND 'online from' is empty"
        findAndSelectContent( TEST_FOLDER.getName() )
        SchedulePublishDialog schedulePublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL ).clickOnShowScheduleButton();
        saveScreenshot( "schedule_dlg_displayed" );

        when: "'Schedule' button was pressed"
        testTomorrowDateTime = TimeUtils.getTomorrowDateTime();
        schedulePublishDialog.typeOnlineFrom( testTomorrowDateTime ).hideTimePickerPopup().clickOnScheduleButton();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        sleep( 400 );
        saveScreenshot( "schedule_onlinefrom_typed" );

        then: "'Online(Pending)' status should be displayed for the folder"
        contentBrowsePanel.getContentStatus( TEST_FOLDER.getName() ).equals( ContentStatus.ONLINE_PENDING.getValue() );
    }

    def "GIVEN existing 'Online (Pending)' folder WHEN the folder opened THEN 'online from' input is present AND correct date time is displayed"()
    {
        given: "existing 'Online (Pending)' folder"
        findAndSelectContent( TEST_FOLDER.getName() )

        when: "the folder is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();

        then: "correct 'online from' date should be displayed"
        wizard.getOnlineFromDateTime() == testTomorrowDateTime

        and: "'Online(Pending)' status should be displayed on the wizard"
        wizard.getStatus() == ContentStatus.ONLINE_PENDING.getValue();
    }

    def "GIVEN existing 'Online (Pending)' folder WHEN the content opened and 'online to' date time typed AND the content was saved THEN correct 'online to' is displayed"()
    {
        given: "existing 'Online (Pending)' folder"
        findAndSelectContent( TEST_FOLDER.getName() );

        when: "the content is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit()
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        and: "'online to' date time was typed and the content has been saved"
        wizard.typeOnlineTo( TEST_ONLINE_TO_VALUE ).save();

        then: "correct 'online to' should be displayed on the wizard"
        wizard.getOnlineToDateTime() == TEST_ONLINE_TO_VALUE;

        and: "status of content should be 'Modified (Pending)'"
        wizard.getStatus() == ContentStatus.MODIFIED_PENDING
    }

    def "GIVEN existing 'Modified (Pending)' folder WHEN the folder is selected AND Unpublish menu item clicked THEN the folders is getting 'New'"()
    {
        given: "existing 'Modified (Pending)' folder"
        findAndSelectContent( TEST_FOLDER.getName() );

        when: "the folder is selected AND Unpublish menu item clicked"
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();
        contentUnPublishDialog.clickOnUnpublishButton();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "pending_online_is_unpublished" );

        then: "the folders is getting 'New', because the previous state was 'Modified (Pending)' "
        contentBrowsePanel.getContentStatus( TEST_FOLDER.getName() ).equals( ContentStatus.NEW.getValue() );
    }
}
