package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.date.DateTimePickerPopup
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.utils.TestUtils
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
        selectSiteOpenWizard( dateTimeContent.getContentTypeName() );

        when: "DateTime input has been clicked"
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );
        DateTimePickerPopup picker = formViewPanel.showPicker();
        TestUtils.saveScreenshot( getSession(), "date-time-picker-with-timezone" );

        then: "'date time picker' popup dialog is displayed"
        picker.isDisplayed();

        and: "time zone not displayed"
        !picker.getTimePickerPopup().isTimeZoneDisplayed();
    }

    def "GIVEN wizard for adding a Local DateTime(0:1) opened WHEN name typed and dateTime was not typed THEN dateTime input is empty and content has a valid status"()
    {
        given: "start to add a content with type local 'DateTime(0:1)'"
        Content dateTimeContent = buildDateTime0_1_Content( VALID_DATE_TIME1 );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateTimeContent.getContentTypeName() );

        when: "only a name typed and dateTime was not typed"
        wizard.typeDisplayName( dateTimeContent.getDisplayName() );
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "date time input is present and enabled"
        formViewPanel.isDateTimeInputDisplayed();

        and: "Add button not displayed on the form"
        !formViewPanel.isAddButtonPresent();

        and: "content should be valid, because 'datetime' is not required"
        !wizard.isContentInvalid( dateTimeContent.getDisplayName() );

        and: "and date time input is empty"
        formViewPanel.getDateTimeValue().isEmpty();
    }

    def "GIVEN wizard for adding a Local DateTime(0:1) opened WHEN not valid dateTime typed THEN content has a invalid status"()
    {
        given: "start to add a content with type local 'DateTime(0:1)'"
        Content dateTimeNotValid = buildDateTime0_1_Content( NOT_VALID_DATE_TIME );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateTimeNotValid.getContentTypeName() );

        when: "all data typed"
        wizard.typeData( dateTimeNotValid );

        then: "content should be invalid, because 'datetime' is not valid"
        !wizard.isContentInvalid( dateTimeNotValid.getDisplayName() );

    }

    def "GIVEN 'date time' wizard is opened WHEN content saved without a 'date time' THEN wizard has no a red icon"()
    {
        given: "new content with type date time added'"
        Content dateTimeContent = buildDateTime0_1_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateTimeContent.getContentTypeName() ).typeData( dateTimeContent );

        when: "content opened for edit"
        wizard.save();
        TestUtils.saveScreenshot( getSession(), "wizard-datetime01-valid" );

        then: "content is valid"
        !wizard.isContentInvalid( dateTimeContent.getDisplayName() );

        and: "'Publish' button is enabled"
        wizard.isPublishButtonEnabled();
    }

    def "GIVEN 'date time' wizard is opened WHEN content  saved and wizard closed without a 'date time' THEN grid row with this content has no a red icon"()
    {
        given: "new content with type 'DateTime(0:1)' added'"
        Content dateTimeContent = buildDateTime0_1_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateTimeContent.getContentTypeName() ).typeData( dateTimeContent );

        when: "content opened for edit"
        wizard.save().close( dateTimeContent.getDisplayName() );
        filterPanel.typeSearchText( dateTimeContent.getName() );
        TestUtils.saveScreenshot( getSession(), "date-time01-without-date" );

        then: "content should is valid, because the 'date time' field not required"
        !contentBrowsePanel.isContentInvalid( dateTimeContent.getName() );
    }
}
