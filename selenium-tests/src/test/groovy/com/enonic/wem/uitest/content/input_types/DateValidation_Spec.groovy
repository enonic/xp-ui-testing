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

    def "GIVEN new Time wizard is opened WHEN time input has been clicked THEN 'time picker popup' dialog should appear"()
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

    def "GIVEN wizard for not required 'Time 0:0' is opened WHEN time with wrong format has been typed THEN 'Publish' button should be enabled, because the input is not required"()
    {
        given: "wizard for not required 'Time 0:0' is opened"
        Content timeContent = buildTime0_0_Content( WRONG_TIME );
        ContentWizardPanel wizard = selectSitePressNew( timeContent.getContentTypeName() );
        TimeFormViewPanel formViewPanel = new TimeFormViewPanel( getSession() );

        when: "time in wrong format has been typed"
        wizard.typeData( timeContent );
        saveScreenshot( "test_wrong_time" );
        wizard.showPublishMenu();

        then: "'Publish...' menu item should be enabled, because input is not required"
        wizard.isPublishMenuItemEnabled(  );

        and: "time input should be displayed with the red border"
        formViewPanel.isTimeInvalid();
    }

    def "GIVEN wizard with not required 'Time 0:0' is opened WHEN correct time has been typed THEN 'Publish' button gets enabled"()
    {
        given: "wizard with not required 'Time 0:0' is opened"
        Content timeContent = buildTime0_0_Content( CORRECT_TIME );
        ContentWizardPanel wizard = selectSitePressNew( timeContent.getContentTypeName() );
        TimeFormViewPanel formViewPanel = new TimeFormViewPanel( getSession() );

        when: "correct time has been typed"
        wizard.typeData( timeContent );

        then: "'Publish' menu item should be enabled"
        wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );

        and: "time input should not be with red border"
        !formViewPanel.isTimeInvalid();
    }

    def "GIVEN wizard 'Time 1:1' is opened WHEN name has been typed BUT time input is empty THEN 'Publish' menu item should be disabled"()
    {
        given: "new wizard for 'Time 1:1' is opened"
        Content timeContent = buildTime1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( timeContent.getContentTypeName() );

        when: "Display name has been typed"
        wizard.typeDisplayName( timeContent.getDisplayName() );
        wizard.showPublishMenu(  );
        saveScreenshot( "required-time-publish-disabled" );


        then: "'Publish' menu item should be disabled, because required input are not filled"
        !wizard.isPublishMenuItemEnabled(  );
    }

    def "GIVEN wizard for required 'DateTime 1:1' is opened WHEN datetime is not correct THEN 'Publish' button should be disabled, because incorrect datetime typed in the required input"()
    {
        given: "wizard for required 'DateTime 1:1' is opened"
        Content dateTimeContent = buildDateTime1_1_Content( WRONG_DATE_TIME );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() );
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        when: "date-time with wrong format has been typed"
        wizard.typeData( dateTimeContent );
        saveScreenshot( "wrong-date-time" );

        then: "'Publish' button should be disabled, because wrong datetime was typed in the required input"
        !wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );

        and: "time input should be with red border"
        formViewPanel.isDateTimeInvalid();
    }

    def "GIVEN wizard for required 'DateTime 1:1' is opened WHEN correct date-time has been typed THEN 'Publish' menu item should be enabled"()
    {
        given: "start to add a content with type 'DateTime(1:1)'"
        Content dateTimeContent = buildDateTime1_1_Content( CORRECT_DATE_TIME );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() );
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        when: "the name has been typed and dateTime is empty"
        wizard.typeData( dateTimeContent );
        saveScreenshot( "correct-date-time" );

        then: "'Publish..' menu item should be enabled"
        wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );

        and: "DateTime input should be without red border"
        !formViewPanel.isDateTimeInvalid();
    }

    def "GIVEN wizard for 'DateTime 2:4' is opened WHEN name has been typed but dateTime input is empty THEN 'Publish' button should be disabled"()
    {
        given: "wizard for 'DateTime 2:4' is opened"
        Content dateTimeContent = buildDateTime2_4_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() );

        when: "'name' has been typed BUT datetime input is empty"
        wizard.typeDisplayName( dateTimeContent.getDisplayName() );
        saveScreenshot( "required-date-time" );

        then: "'Publish' menu item should be disabled"
        !wizard.showPublishMenu(  ).isPublishMenuItemEnabled();
    }
}
