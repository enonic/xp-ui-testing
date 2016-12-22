package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog
import com.enonic.autotests.pages.contentmanager.SchedulePublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Created on 19.12.2016.
 *
 * Tasks: XP-4682 Add selenium tests for 'Scheduled Publishing Dialog'
 * */
@Stepwise
class SchedulePublishDialog_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_FOLDER;

    @Shared
    String testTomorrowDateTime;

    def "GIVEN existing folder is selcted WHEN show schedule button pressed THEN 'SchedulePublishDialog' dialog displayed"()
    {
        given: "existing folder in root"
        TEST_FOLDER = buildFolderContent( "folder", "schedule dialog" );
        addContent( TEST_FOLDER );

        when: "show schedule button pressed"
        findAndSelectContent( TEST_FOLDER.getName() )
        SchedulePublishDialog schedulePublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL ).clickOnShowScheduleButton();

        then: "'SchedulePublishDialog' dialog is displayed"
        schedulePublishDialog.isDisplayed();

        and: "'Cancel Top' button is displayed"
        schedulePublishDialog.isCancelTopButtonDisplayed();

        and: "'Back' button is displayed"
        schedulePublishDialog.isBackButtonDisplayed();

        and: "'Online From' input is displayed"
        schedulePublishDialog.isOnlineFromInputDisplayed();

        and: "'Online To' input is displayed"
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

        then: "schedule dialog has been closed"
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

        and: "correct message is displayed"
        schedulePublishDialog.getValidationMessage() == Application.REQUIRED_MESSAGE;
    }

    def "GIVEN SchedulePublish dialog is opened AND 'online from' is Tomorrow WHEN 'Schedule' button has been pressed THEN correct status is displayed"()
    {
        given: "SchedulePublish dialog is opened AND 'online from' is empty"
        findAndSelectContent( TEST_FOLDER.getName() )
        SchedulePublishDialog schedulePublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL ).clickOnShowScheduleButton();
        saveScreenshot( "schedule_dlg_displayed" );

        when: "'Schedule' button was pressed"
        testTomorrowDateTime = getTomorrowDateTime();
        schedulePublishDialog.typeOnlineFrom( testTomorrowDateTime ).hideTimePickerPopup().clickOnScheduleButton();
        saveScreenshot( "schedule_onlinefrom_typed" );

        then: "'Online(Pending)' status is displayed for the folder"
        contentBrowsePanel.getContentStatus( TEST_FOLDER.getName() ).equals( ContentStatus.ONLINE_PENDING.getValue() );
    }

    def "GIVEN existing 'Online (Pending)' folder WHEN the folder opened THEN 'online from' input is present AND correct date time is displayed"()
    {
        given: "existing 'Online (Pending)' folder"
        findAndSelectContent( TEST_FOLDER.getName() )

        when: "the folder opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();

        then: "correct 'online from' is displayed"
        wizard.getOnlineFromDateTime() == tomorrowDateTime;

        and: "'Online(Pending)' status is displayed on the wizard"
        wizard.getStatus() == ContentStatus.ONLINE_PENDING.getValue();
    }

    def "GIVEN existing 'Online (Pending)' folder WHEN the folder is slected AND Unpublish menu item clicked THEN the folders is getting 'offline'"()
    {

        given: "existing 'Online (Pending)' folder"
        findAndSelectContent( TEST_FOLDER.getName() );

        when:
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();
        contentUnPublishDialog.clickOnUnpublishButton();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "pending_online_is_unpublished" );

        then: "the folders is getting 'offline'"
        contentBrowsePanel.getContentStatus( TEST_FOLDER.getName() ).equals( ContentStatus.OFFLINE.getValue() );
    }

    private String getTomorrowDateTime()
    {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tomorrow = today.plus( 1, ChronoUnit.DAYS );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm" );
        String formatDateTime = tomorrow.format( formatter );
        return formatDateTime;
    }
}
