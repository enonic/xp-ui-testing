package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.exceptions.TestFrameworkException
import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.date.DateTimePickerPopup
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Occurrences_DateTime_1_1_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    String TEST_DATE_TIME1 = "2015-02-28 19:01";

    def "GIVEN wizard for adding a DateTime with timezone opened WHEN date time input was clicked THEN date time picker popup dialog with timezone is displayed"()
    {
        given: "wizard for adding a DateTime with timezone opened"
        Content dateTimeContent = buildDateTime1_1_Content( TEST_DATE_TIME1 );
        selectSitePressNew( dateTimeContent.getContentTypeName() );

        when: "DateTime input has been clicked"
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );
        DateTimePickerPopup picker = formViewPanel.showPicker();
        TestUtils.saveScreenshot( getSession(), "date-time-picker-with-timezone" );

        then: "'date time picker' popup dialog is displayed"
        picker.isDisplayed();
        and: "time zone is displayed"
        picker.getTimePickerPopup().isTimeZoneDisplayed();
    }

    def "GIVEN wizard for adding a DateTime(1:1) opened WHEN name typed and dateTime was not typed THEN dateTime input is empty and content has a invalid status"()
    {
        given: "start to adding a content with type 'DateTime(1:1)'"
        Content dateTimeContent = buildDateTime1_1_Content( TEST_DATE_TIME1 );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() );

        when: "only a name typed and dateTime was not typed"
        wizard.typeDisplayName( dateTimeContent.getDisplayName() );
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "date time input is present and enabled"
        formViewPanel.isDateTimeInputDisplayed();

        and: "content should be invalid, because required field- datetime1:1 not typed"
        wizard.isContentInvalid( dateTimeContent.getDisplayName() );

        and: "and date time input is empty"
        formViewPanel.getDateTimeValue().isEmpty();
    }

    def "GIVEN 'date time'-wizard opened WHEN content without required 'date time' saved THEN wizard has a red icon"()
    {
        given: "new content with type date time added'"
        Content dateTimeContent = buildDateTime1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() ).typeData( dateTimeContent );

        when: "content without required 'date time' saved"
        wizard.save();

        then: "content should be invalid, because required field is empty"
        wizard.isContentInvalid( dateTimeContent.getDisplayName() );
    }

    def "WHEN content without required 'date time ' saved and wizard closed THEN grid row with it content has a red icon"()
    {
        given: "new content with type date time added'"
        Content dateTimeContent = buildDateTime1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() ).typeData( dateTimeContent );

        when: "content saved and the wizard has been closed"
        wizard.save().close( dateTimeContent.getDisplayName() );
        filterPanel.typeSearchText( dateTimeContent.getName() );
        TestUtils.saveScreenshot( getSession(), "date-time-not-valid-grid" )

        then: "content should be invalid, because required field not filled"
        contentBrowsePanel.isContentInvalid( dateTimeContent.getName() );
    }

    def "GIVEN creating new DateTime1:1 on root WHEN data typed and 'Save' and  'Publish' are pressed THEN new content with status equals 'Online' listed"()
    {
        given: "start to add a content with type 'DateTime1 1:1'"
        Content dateTimeContent = buildDateTime1_1_Content( TEST_DATE_TIME1 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateTimeContent.getContentTypeName() );

        when: "data typed and 'Save' and  'Publish' are pressed"
        contentWizardPanel.typeData( dateTimeContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton();
        String publishMessage = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        contentWizardPanel.refreshInBrowser();
        SaveBeforeCloseDialog modalDialog = contentWizardPanel.close( dateTimeContent.getDisplayName() );
        saveScreenshot( NameHelper.uniqueName( "datetime1_1_close" ) );
        if ( modalDialog != null )
        {
            saveScreenshot( NameHelper.uniqueName( "err-close-wizard" ) );
            throw new TestFrameworkException( "'save before closing' modal dialog present but all changes were saved! " )
        }
        filterPanel.typeSearchText( dateTimeContent.getName() );

        then: "status of content is 'online' now"
        contentBrowsePanel.getContentStatus( dateTimeContent.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );

        and: "correct notification was shown"
        publishMessage == String.format( Application.ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, dateTimeContent.getDisplayName() );
    }
}
