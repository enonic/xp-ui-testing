package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.TimeUtils
import com.enonic.autotests.vo.contentmanager.Content
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

    def "GIVEN existing folder is opened WHEN 'Publish' button has been pressed and the content published THEN 'Online from' and 'Online to' appears on the Schedule step form"()
    {
        given: "existing folder is opened"
        TEST_FOLDER = buildFolderContent( "schedule", "schedule inputs test" );
        addContent( TEST_FOLDER );
        ContentWizardPanel wizard = findAndSelectContent( TEST_FOLDER.getName() ).clickToolbarEdit();

        and: "'Online From' input is not displayed"
        boolean isDisplayedBeforePublish = wizard.isOnlineFromInputDisplayed();

        when: "the folder has been marked as ready"
        wizard.showPublishMenu().clickOnMarkAsReadyMenuItem();

        and: "'Publish' button has been pressed and the content published"
        wizard.clickOnWizardPublishButton().clickOnPublishButton();
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

    def "GIVEN existing published folder WHEN the folder is  opened and 'Online to' is earlier  than 'Online from' THEN correct validation message appears"()
    {
        given: "existing published folder"
        ContentWizardPanel wizard = findAndSelectContent( TEST_FOLDER.getName() ).clickToolbarEdit();

        when: "'Online to' is earlier  than 'Online from'"
        wizard.typeOnlineTo( TimeUtils.getYesterdayDateTime() );
        saveScreenshot( "schedule_wizard_validation_message" )

        then: "expected validation messages appears"
        wizard.getScheduleValidationMessage() == wizard.SCHEDULE_VALIDATION_MESSAGE;

    }

    def "GIVEN existing published folder WHEN the folder is opened 'Online from' is cleared and 'Online to' has been set THEN correct notification message appears"()
    {
        given: "existing published folder"
        ContentWizardPanel wizard = findAndSelectContent( TEST_FOLDER.getName() ).clickToolbarEdit();

        when: "'Online from' is empty"
        wizard.typeOnlineFrom( " " );

        and: "but 'Online to' is Yesterday"
        wizard.typeOnlineTo( TimeUtils.getYesterdayDateTime() ).save();
        saveScreenshot( "schedule_wizard_notification_message" )

        then: "correct notification messages appears"
        String notification = wizard.waitNotificationWarning( Application.EXPLICIT_NORMAL );
        notification == wizard.ONLINE_FROM_MISSED_NOTIFICATION_MESSAGE
    }

    def "GIVEN existing published folder WHEN the folder has been unpublished THEN 'Online from'  and 'Online to'  inputs are getting hidden"()
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

    def "GIVEN existing Unpublished folder WHEN 'Online from' set in the future AND Publish button pressed THEN folder is getting 'Online Pending'"()
    {
        given: "existing published folder"
        ContentWizardPanel wizard = findAndSelectContent( TEST_FOLDER.getName() ).clickToolbarEdit();

        when: "the folder has been unpublished"
        ContentPublishDialog publishDialog = wizard.showPublishMenu().clickOnPublishMenuItem();
        publishDialog.clickOnAddScheduleButton();
        publishDialog.typeOnlineFrom( TimeUtils.getTomorrowDateTime() ).clickOnScheduleButton();

        then: "status gets 'Published(Pending)'"
        saveScreenshot( "schedule_wizard_online_pending" );
        wizard.waitStatus( ContentStatus.PUBLISHED_PENDING, Application.EXPLICIT_NORMAL );

    }
}
