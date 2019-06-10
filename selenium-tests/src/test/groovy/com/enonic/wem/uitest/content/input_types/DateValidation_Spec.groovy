package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.date.TimePickerPopup
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.pages.form.TimeFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class DateValidation_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    String WRONG_TIME = "191:01";

    @Shared
    String CORRECT_TIME = "16:10";

    @Shared
    String CORRECT_DATE_TIME = "2015-02-28 19:01";

    @Shared
    String WRONG_DATE_TIME = "015-02-28 19:01";

    def "GIVEN wizard for adding a Time opened WHEN time input was clicked THEN 'time picker popup' dialog is displayed"()
    {
        given: "Time-content wizard is opened"
        Content dateContent = buildTime0_0_Content( CORRECT_TIME );
        selectSitePressNew( dateContent.getContentTypeName() );

        when: "Time input has been clicked"
        TimeFormViewPanel formViewPanel = new TimeFormViewPanel( getSession() );
        TimePickerPopup picker = formViewPanel.clickOnInputAndShowPicker();
        saveScreenshot( "time-picker-popup" );

        then: "'time picker' popup dialog should be displayed"
        picker.isDisplayed();

        and: "time zone should not be displayed"
        !picker.isTimeZoneDisplayed();
    }

    def "GIVEN saving of content with type 'Time 0:0' WHEN value of time is wrong THEN 'Publish' button should be enabled, because input is not required"()
    {
        given: "start to add a content with type 'Time 0:0'"
        Content timeContent = buildTime0_0_Content( WRONG_TIME );
        ContentWizardPanel wizard = selectSitePressNew( timeContent.getContentTypeName() );
        TimeFormViewPanel formViewPanel = new TimeFormViewPanel( getSession() );

        when: "time with wrong format was typed "
        wizard.typeData( timeContent );
        saveScreenshot( "test_wrong_time" );

        then: "'Publish' button should be enabled, because input is not required"
        wizard.isPublishButtonEnabled();

        and: "time input should be displayed with the red border"
        formViewPanel.isTimeInvalid();
    }

    def "GIVEN saving of content with type 'Time 0:0' WHEN value of time is correct THEN 'Publish' button enabled"()
    {
        given: "start to add a content with type 'Time 0:0'"
        Content timeContent = buildTime0_0_Content( CORRECT_TIME );
        ContentWizardPanel wizard = selectSitePressNew( timeContent.getContentTypeName() );
        TimeFormViewPanel formViewPanel = new TimeFormViewPanel( getSession() );

        when: "correct time typed "
        wizard.typeData( timeContent );

        then: "'Publish' button should be enabled"
        wizard.isPublishButtonEnabled();

        and: "time input should not be with red border"
        !formViewPanel.isTimeInvalid();
    }

    def "GIVEN saving of content with type 'Time 1:1' WHEN time was not typed THEN 'Publish' button should be disabled"()
    {
        given: "start to add a content with type 'Time 1:1'"
        Content timeContent = buildTime1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( timeContent.getContentTypeName() );

        when: "correct time has been typed "
        wizard.typeDisplayName( timeContent.getDisplayName() );
        saveScreenshot( "required-time-publish-disabled" );

        then: "'Publish' button should be disabled, because required input not filled"
        !wizard.isPublishButtonEnabled();
    }

    def "GIVEN saving of content with type 'DateTime 1:1' and value of datetime is wrong WHEN invalid datetime THEN 'Publish' button should be disabled, because wrong datetime typed in the required input"()
    {
        given: "saving of content with type 'DateTime 1:1'"
        Content dateTimeContent = buildDateTime1_1_Content( WRONG_DATE_TIME );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() );
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        when: "date time with wrong format has been typed "
        wizard.typeData( dateTimeContent );
        saveScreenshot( "wrong-date-time" );

        then: "'Publish' button should be disabled, because wrong datetime typed in the required input"
        !wizard.isPublishButtonEnabled();

        and: "time input should be with the red border"
        formViewPanel.isDateTimeInvalid();
    }

    def "GIVEN wizard for adding a required DateTime(1:1) opened WHEN correct date-time has been typed THEN 'Publish' button is enabled"()
    {
        given: "start to add a content with type 'DateTime(1:1)'"
        Content dateTimeContent = buildDateTime1_1_Content( CORRECT_DATE_TIME );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() );
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        when: "only the name typed and dateTime not typed and 'Publish' button clicked"
        wizard.typeData( dateTimeContent );
        saveScreenshot( "correct-date-time" );

        then: "'Publish' button should be enabled"
        wizard.isPublishButtonEnabled();

        and: "time input is not red"
        !formViewPanel.isDateTimeInvalid();
    }

    def "GIVEN wizard for adding a required DateTime(2:4) opened WHEN name typed but dateTime not typed  THEN 'Publish' button is disabled"()
    {
        given: "start to add a content with type 'DateTime(2:4)'"
        Content dateTimeContent = buildDateTime2_4_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() );

        when: "only the 'name' was typed and datetime not typed"
        wizard.typeDisplayName( dateTimeContent.getDisplayName() );
        saveScreenshot( "required-date-time" );

        then: "'Publish' button should be disabled"
        !wizard.isPublishButtonEnabled();
    }
}
