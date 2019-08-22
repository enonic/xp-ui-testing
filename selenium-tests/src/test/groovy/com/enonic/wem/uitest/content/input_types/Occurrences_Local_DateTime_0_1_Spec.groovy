package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.date.DateTimePickerPopup
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Occurrences_Local_DateTime_0_1_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String VALID_DATE_TIME1 = "2016-03-15 19:01";

    @Shared
    String NOT_VALID_DATE_TIME = "016-03-15 19:01";

    def "GIVEN wizard for adding a Local DateTime without timezone opened WHEN date time input was clicked THEN date time picker popup dialog is displayed"()
    {
        given: "wizard for adding a DateTime with timezone opened"
        Content dateTimeContent = buildDateTime0_1_Content( VALID_DATE_TIME1 );
        selectSitePressNew( dateTimeContent.getContentTypeName() );

        when: "DateTime input has been clicked"
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );
        DateTimePickerPopup picker = formViewPanel.showPicker();
        saveScreenshot( "date-time-picker-with-timezone" );

        then: "'date time picker' popup dialog is displayed"
        picker.isDisplayed();

        and: "time zone not displayed"
        !picker.getTimePickerPopup().isTimeZoneDisplayed();
    }

    def "GIVEN wizard for DateTime(not required) is opened WHEN only name has been typed THEN dateTime input is empty and content has a valid status"()
    {
        given: "start to add a content with type local 'DateTime(0:1)'"
        Content dateTimeContent = buildDateTime0_1_Content( VALID_DATE_TIME1 );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() );

        when: "only a name typed and dateTime was not typed"
        wizard.typeDisplayName( dateTimeContent.getDisplayName() );
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "date time input is present and enabled"
        formViewPanel.isDateTimeInputDisplayed();

        and: "Add button not displayed on the form"
        !formViewPanel.isAddButtonPresent();

        and: "content should be valid, because 'datetime' is not required"
        !wizard.isContentInvalid(  );

        and: "and date time input is empty"
        formViewPanel.getDateTimeValue().isEmpty();
    }

    def "GIVEN wizard for a Local DateTime(not required) is opened WHEN not valid dateTime has been typed THEN the content should be valid, because the input is not required"()
    {
        given: "start to add a content with type local 'DateTime(not required)'"
        Content dateTimeNotValid = buildDateTime0_1_Content( NOT_VALID_DATE_TIME );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeNotValid.getContentTypeName() );

        when: "all data has been typed"
        wizard.typeData( dateTimeNotValid );

        then: "the content should be valid, because the input is not required"
        !wizard.isContentInvalid(  );
    }

    def "GIVEN 'date time' wizard is opened WHEN content saved without a 'date time' THEN wizard has no a red icon"()
    {
        given: "new content with type date time added'"
        Content dateTimeContent = buildDateTime0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() ).typeData( dateTimeContent );
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        when: "content has been opened"
        wizard.save();
        saveScreenshot( "wizard-datetime01-valid" );

        then: "content is valid and validation message is not displayed"
        !formViewPanel.isValidationMessagePresent();

        and: "'Publish' button is enabled"
        wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );
    }

    def "GIVEN 'date time' wizard is opened WHEN 'date time' is empty and content was saved and wizard closed THEN content should be displayed as valid in the grid"()
    {
        given: "new content with type 'DateTime(0:1)' added'"
        Content dateTimeContent = buildDateTime0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() ).typeData( dateTimeContent );

        when: "'date time' is empty and content was saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( dateTimeContent.getName() );
        saveScreenshot( "date-time01-without-date" );

        then: "content should be displayed as valid in the grid, because the 'date time' field not required"
        !contentBrowsePanel.isContentInvalid( dateTimeContent.getName() );
    }
}
