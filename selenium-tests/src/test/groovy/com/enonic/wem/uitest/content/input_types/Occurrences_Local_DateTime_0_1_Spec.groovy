package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.date.DateTimePickerPopup
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.IgnoreIf
import spock.lang.Shared

@Ignore
class Occurrences_Local_DateTime_0_1_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String VALID_DATE_TIME1 = "2016-03-15 19:01";

    @Shared
    String NOT_VALID_DATE_TIME = "016-03-15 19:01";

    def "GIVEN wizard for adding a datetime(0:1) without timezone opened WHEN click in the date time input THEN date time picker popup dialog is displayed"()
    {
        given: "wizard for adding a DateTime with timezone opened"
        Content dateTimeContent = buildDateTime0_1_Content( VALID_DATE_TIME1 );
        selectSitePressNew( dateTimeContent.getContentTypeName() );

        when: "Click in the dateTime input:"
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );
        DateTimePickerPopup picker = formViewPanel.showPicker();
        saveScreenshot( "date-time-picker-with-timezone" );

        then: "'date time picker' popup dialog should appear"
        picker.isDisplayed();

        and: "time zone should not be displayed"
        !picker.getTimePickerPopup().isTimeZoneDisplayed();
    }

    def "GIVEN wizard for DateTime(not required) is opened WHEN a name has been typed THEN the content should be valid, because dateTime input is not required"()
    {
        given: "open new wizard 'DateTime(0:1)'"
        Content dateTimeContent = buildDateTime0_1_Content( VALID_DATE_TIME1 );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() );

        when: "a name has been typed"
        wizard.typeDisplayName( dateTimeContent.getDisplayName() );
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "date time input should be enabled"
        formViewPanel.isDateTimeInputDisplayed();

        and: "'Add' button should not be displayed in the form"
        !formViewPanel.isAddButtonPresent();

        and: "content should be valid, because 'datetime' is not required"
        !wizard.isContentInvalid();

        and: "date time input should be empty"
        formViewPanel.getDateTimeValue().isEmpty();
    }

    def "GIVEN wizard for a Local DateTime(not required) is opened WHEN writing dateTime in wrong format THEN the content should be valid, because the input is not required"()
    {
        given: "start to add a content with type local 'DateTime(not required)'"
        Content dateTimeNotValid = buildDateTime0_1_Content( NOT_VALID_DATE_TIME );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeNotValid.getContentTypeName() );

        when: "writing dateTime in wrong format:"
        wizard.typeData( dateTimeNotValid );

        then: "the content should be valid, because the input is not required"
        !wizard.isContentInvalid();
    }

    def "GIVEN 'date time' wizard is opened AND dateTime input is empty WHEN content has been saved THEN wizard has no red icon"()
    {
        given: "new content with type date time added'"
        Content dateTimeContent = buildDateTime0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() ).typeData( dateTimeContent );
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        when: "content has been saved"
        wizard.save();
        saveScreenshot( "wizard-datetime01-valid" );

        then: "content is valid and validation message should not be displayed"
        !formViewPanel.isFormValidationMessageDisplayed();

        and: "'Publish' menu item gets enabled"
        wizard.showPublishMenu().isPublishMenuItemEnabled();
    }

    def "GIVEN 'date time' wizard is opened AND dateTime input is empty WHEN the content has been saved and wizard closed THEN content should be valid in the browse panel"()
    {
        given: "new content with type 'DateTime(0:1)' added'"
        Content dateTimeContent = buildDateTime0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( dateTimeContent.getContentTypeName() ).typeData( dateTimeContent );

        when: "the content has been saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( dateTimeContent.getName() );
        saveScreenshot( "date-time01-without-date" );

        then: "content should be valid in the browse panel, because the 'date time' field is not required"
        !contentBrowsePanel.isContentInvalid( dateTimeContent.getName() );
    }
}
