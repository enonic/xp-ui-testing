package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.TimeUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentVersion
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 27.12.2016.
 *
 * */
@Stepwise
class ContentWizard_Publish_Inputs_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_FOLDER;

    def "GIVEN existing folder is opened WHEN the folder has been published THEN 'Online from' and 'Online to' appear in the Schedule step form"()
    {
        given: "existing folder is opened"
        TEST_FOLDER = buildFolderContent( "schedule", "schedule inputs test" );
        addContent( TEST_FOLDER );
        ContentWizardPanel wizard = findAndSelectContent( TEST_FOLDER.getName() ).clickToolbarEdit();

        and: "'Online From' input is not displayed"
        boolean isDisplayedBeforePublish = wizard.isOnlineFromInputDisplayed();

        when: "the folder has been marked as ready"
        wizard.showPublishMenu().clickOnMarkAsReadyMenuItem();

        and: "'Publish' button has been pressed and this content has been published"
        wizard.clickOnPublishButton().clickOnPublishNowButton();
        String nowDate = TimeUtils.getNowDate();

        then: "'Schedule' wizard step appears"
        wizard.isWizardStepPresent( "Schedule" );

        and: "'Online from' is present on the wizard"
        wizard.isOnlineFromInputDisplayed();

        and: "'Online to' is present on the wizard"
        wizard.isOnlineToInputDisplayed();

        and: "'Online from' input contains correct date time"
        wizard.getOnlineFromDateTime().contains( nowDate );

        and: "'Online from' input was not displayed before publish"
        !isDisplayedBeforePublish;
    }

    def "GIVEN existing published folder is opened WHEN 'Online to' is earlier than 'Online from' THEN expected validation message appears"()
    {
        given: "existing published folder"
        ContentWizardPanel wizard = findAndSelectContent( TEST_FOLDER.getName() ).clickToolbarEdit();

        when: "'Online to' is earlier  than 'Online from'"
        wizard.typeOnlineTo( TimeUtils.getYesterdayDateTime() );
        saveScreenshot( "schedule_wizard_validation_message" )

        then: "expected validation messages appears"
        wizard.getScheduleValidationMessage() == wizard.SCHEDULE_VALIDATION_MESSAGE;
    }

    def "GIVEN existing published folder is opened WHEN 'Online from' is cleared and 'Online to' has been set THEN expected notification message appears"()
    {
        given: "existing published folder"
        ContentWizardPanel wizard = findAndSelectContent( TEST_FOLDER.getName() ).clickToolbarEdit();

        when: "'Online from' is empty"
        wizard.typeOnlineFrom( " " );

        and: "but 'Online to' is Yesterday"
        wizard.typeOnlineTo( TimeUtils.getYesterdayDateTime() ).save();
        saveScreenshot( "schedule_wizard_notification_message" )

        then: "expected notification messages appears"
        String notification = wizard.waitNotificationWarning( Application.EXPLICIT_NORMAL );
        notification == wizard.ONLINE_FROM_MISSED_NOTIFICATION_MESSAGE
    }

    def "WHEN existing folder has been unpublished THEN 'Online from' and 'Online to' inputs get hidden"()
    {
        given: "existing published folder"
        ContentWizardPanel wizard = findAndSelectContent( TEST_FOLDER.getName() ).clickToolbarEdit();
        sleep( 1000 );

        when: "the folder has been unpublished"
        wizard.showPublishMenu().selectUnPublishMenuItem().clickOnUnpublishButton();

        then: "status is getting 'Unpublished'"
        wizard.waitStatus( ContentStatus.UNPUBLISHED, Application.EXPLICIT_NORMAL );
        saveScreenshot( "schedule_wizard_unpublished" )

        and: "'Online from' input disappears"
        !wizard.isOnlineFromInputDisplayed();

        and: "'Online to' input disappears"
        !wizard.isOnlineToInputDisplayed();
    }

    def "WHEN 'Online from' has been set in the future AND Publish button pressed THEN folder gets 'Publishing Scheduling'"()
    {
        given: "existing published folder"
        ContentWizardPanel wizard = findAndSelectContent( TEST_FOLDER.getName() ).clickToolbarEditAndSwitchToWizardTab(  );

        when: "the folder has been unpublished"
        ContentPublishDialog publishDialog = wizard.showPublishMenu().clickOnPublishMenuItem();
        saveScreenshot( "schedule_wizard_add_schedule_button" );
        publishDialog.clickOnAddScheduleButton();
        publishDialog.typeOnlineFrom( TimeUtils.getTomorrowDateTime() ).clickOnScheduleButton();

        then: "status gets 'Published(Pending)'"
        saveScreenshot( "schedule_wizard_published_pending" );
        wizard.waitStatus( ContentStatus.PUBLISHED_SCHEDULED, Application.EXPLICIT_NORMAL );
    }
    //Verifies https://github.com/enonic/app-contentstudio/issues/941
    //Incorrect status in version history for content with scheduled publishing #941
    def "WHEN existing 'Publishing Scheduling' folder is selected THEN 'Will be published' status should be in Versions widget"()
    {
        given: "existing Published(Pending) folder is selected"
        findAndSelectContent( TEST_FOLDER.getName() );

        when: "Versions panel has been opened"
        VersionHistoryWidget versionHistoryWidget = openVersionPanel();

        then: "status gets 'Published(Pending)'"
        saveScreenshot( "schedule_wizard_published_pending" );
        versionHistoryWidget.getContentStatus().contains( "Will be published" );
    }
}
