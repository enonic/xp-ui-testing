package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.date.DateTimePickerPopup
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Occurrences_DateTime_1_1_Spec
        extends Base_InputFields_Occurrences {

    @Shared
    String TEST_DATE_TIME1 = "2015-02-28 19:01";

    def "GIVEN wizard for DateTime(1:1) with timezone is opened WHEN date time input was clicked THEN date time picker popup dialog with timezone is displayed"() {
        given: "wizard for DateTime(1:1) with timezone is opened"
        Content dateTimeContent = buildDateTime1_1_Content(TEST_DATE_TIME1);
        selectSitePressNew(dateTimeContent.getContentTypeName());

        when: "DateTime input has been clicked"
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel(getSession());
        DateTimePickerPopup picker = formViewPanel.showPicker();
        saveScreenshot("date-time-picker-with-timezone");

        then: "'date time picker' popup dialog is displayed"
        picker.isDisplayed();
        and: "time zone should be displayed"
        picker.getTimePickerPopup().isTimeZoneDisplayed();
    }

    def "GIVEN wizard for DateTime(1:1) with timezone is opened WHEN name was typed but dateTime is empty THEN wizard should be with red icon"() {
        given: "wizard for DateTime(1:1) with timezone is opened"
        Content dateTimeContent = buildDateTime1_1_Content(TEST_DATE_TIME1);
        ContentWizardPanel wizard = selectSitePressNew(dateTimeContent.getContentTypeName());

        when: "name has been typed but dateTime is empty and the content is not saved yet"
        wizard.typeDisplayName(dateTimeContent.getDisplayName());
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel(getSession());

        then: "red icon should be present on the wizard-page, because required input is empty"
        wizard.isContentInvalid();

        and: "and date time input is empty"
        formViewPanel.getDateTimeValue().isEmpty();
    }

    def "GIVEN 'date time'-wizard is opened WHEN content with empty required 'date time' saved THEN wizard should be with red icon"() {
        given: "new content with type 'date time' was added'"
        Content dateTimeContent = buildDateTime1_1_Content(null);
        ContentWizardPanel wizard = selectSitePressNew(dateTimeContent.getContentTypeName()).typeData(dateTimeContent);
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel(getSession());

        when: "content without required 'date time' has been saved"
        wizard.save();

        then: "content should be invalid, because required field is empty"
        formViewPanel.isValidationMessagePresent();
    }

    def "WHEN content with empty required 'date time ' saved and wizard closed THEN the content should be with red-icon"() {
        given: "new content with type date time added'"
        Content dateTimeContent = buildDateTime1_1_Content(null);
        ContentWizardPanel wizard = selectSitePressNew(dateTimeContent.getContentTypeName()).typeData(dateTimeContent);

        when: "content saved and the wizard has been closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText(dateTimeContent.getName());
        saveScreenshot("date-time-not-valid-grid")

        then: "content should be invalid, because required input was not filled"
        contentBrowsePanel.isContentInvalid(dateTimeContent.getName());
    }

    def "GIVEN wizard for new DateTime1:1  WHEN date has been typed and 'Save' and 'Publish' are pressed THEN the content is getting 'Published'"() {
        given: "start to add a content with type 'DateTime1 1:1'"
        Content dateTimeContent = buildDateTime1_1_Content(TEST_DATE_TIME1);
        ContentWizardPanel contentWizardPanel = selectSitePressNew(dateTimeContent.getContentTypeName());

        when: "data typed and 'Save' and  'Publish' has been pressed"
        contentWizardPanel.typeData( dateTimeContent ).save().clickOnWizardPublishButton().clickOnPublishButton();
        String publishMessage = contentBrowsePanel.waitPublishNotificationMessage(Application.EXPLICIT_NORMAL);
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        and: "the name of the content has been typed in the search input"
        filterPanel.typeSearchText(dateTimeContent.getName());

        then: "status of the content should be 'Published'"
        contentBrowsePanel.getContentStatus(dateTimeContent.getName()).equalsIgnoreCase(ContentStatus.PUBLISHED.getValue());

        and: "expected notification should appear"
        publishMessage == String.format(Application.ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE_TMP, dateTimeContent.getName());
    }
}
