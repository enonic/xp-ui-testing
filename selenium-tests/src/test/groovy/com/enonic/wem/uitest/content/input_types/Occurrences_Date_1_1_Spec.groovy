package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.date.DatePickerPopup
import com.enonic.autotests.pages.form.DateFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Occurrences_Date_1_1_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    String TEST_DATE = "2016-01-11";

    @Shared
    Content TEST_CONTENT;

    def "GIVEN wizard for new Date(1:1) is opened WHEN date input was clicked THEN 'date picker popup' dialog should appear"()
    {
        given: "wizard for new Date(1:1) is opened"
        Content dateContent = buildDate1_1_Content( TEST_DATE );
        selectSitePressNew( dateContent.getContentTypeName() );

        when: "Date input has been clicked"
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );
        DatePickerPopup picker = formViewPanel.showPicker();
        saveScreenshot( "date-picker-popup" );

        then: "'date picker' popup dialog should appear"
        picker.isDisplayed();
    }

    def "GIVEN wizard for new 'Date(1:1)' is opened WHEN name has been typed AND date input is empty THEN red icon should be present on the wizard-page(date input is required)"()
    {
        given: "wizard for new 'Date(1:1)' is opened"
        Content dateContent = buildDate1_1_Content( TEST_DATE );
        ContentWizardPanel wizard = selectSitePressNew( dateContent.getContentTypeName() );

        when: "only a name was typed and date input is empty(the content is not saved)"
        wizard.typeDisplayName( dateContent.getDisplayName() );

        then: "red icon should be present on the wizard-page, because required input is empty"
        wizard.isContentInvalid();
    }

    def "GIVEN wizard for Date(1:1) is opened WHEN required 'date' is empty AND the content has been saved THEN red icon should be present on the wizard"()
    {
        given: "wizard for Date(1:1) is opened"
        TEST_CONTENT = buildDate1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( TEST_CONTENT.getContentTypeName() ).typeData( TEST_CONTENT );
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );

        when: "content without required 'date' has been saved"
        wizard.save();

        then: "validation message should be displayed"
        formViewPanel.isValidationMessagePresent();

        and: "red icon should be present on the wizard-page, because required input is empty"
        wizard.isContentInvalid();
    }

    def "GIVEN existing Date(1:1) AND date is not selected WHEN content has been selected THEN red icon should be present near the content in the grid"()
    {
        when: "existing Date(1:1) has been selected(date is not selected)"
        filterPanel.typeSearchText( TEST_CONTENT.getName() );
        saveScreenshot( "date-not-valid" )

        then: "content should be invalid, because required field is empty"
        contentBrowsePanel.isContentInvalid( TEST_CONTENT.getName() );
    }

    def "WHEN new Date(1:1) content has been published THEN content's status should be 'Published'"()
    {
        given: "start to add a content with type 'Date'"
        Content dateContent = buildDate1_1_Content( TEST_DATE );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "data was typed and saved and the content has been published"
        contentWizardPanel.typeData( dateContent ).save().clickOnWizardPublishButton().clickOnPublishButton();
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( dateContent.getName() );

        then: "content should be 'Published'"
        contentBrowsePanel.getContentStatus( dateContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }
}
