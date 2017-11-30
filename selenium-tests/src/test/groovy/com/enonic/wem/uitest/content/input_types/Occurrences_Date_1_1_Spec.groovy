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

    def "GIVEN wizard for adding a Date opened WHEN date input was clicked THEN 'date picker popup' dialog is displayed"()
    {
        given: "wizard for adding a Date opened"
        Content dateContent = buildDate1_1_Content( TEST_DATE );
        selectSitePressNew( dateContent.getContentTypeName() );

        when: "Date input has been clicked"
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );
        DatePickerPopup picker = formViewPanel.showPicker();
        saveScreenshot( "date-picker-popup" );

        then: "'date picker' popup dialog is displayed"
        picker.isDisplayed();
    }

    def "GIVEN wizard for adding a Date(1:1) opened WHEN name typed and date was not typed THEN red icon should be present on the wizard-page"()
    {
        given: "start to add a content with type 'Date(1:1)'"
        Content dateContent = buildDate1_1_Content( TEST_DATE );
        ContentWizardPanel wizard = selectSitePressNew( dateContent.getContentTypeName() );

        when: "only a name was typed and date was not typed(the content is not saved)"
        wizard.typeDisplayName( dateContent.getDisplayName() );

        then: "red icon should be present on the wizard-page, because required input is empty"
        wizard.isContentInvalid();
    }

    def "GIVEN opened content wizard WHEN content without required 'date' was saved THEN wizard has a red icon on wizard-tab"()
    {
        given: "new content with type date added'"
        Content dateContent = buildDate1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( dateContent.getContentTypeName() ).typeData( dateContent );
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );

        when: "content without required 'date' was saved"
        wizard.save();

        then: "validation message should be displayed"
        formViewPanel.isValidationMessagePresent();

        and: "red icon should be present on the wizard-page, because required input is empty"
        wizard.isContentInvalid();
    }

    def "GIVEN opened content wizard WHEN content saved without required 'date' and wizard closed THEN content displayed in a grid with a invalid status"()
    {
        given: "new content with type date time added'"
        Content dateContent = buildDate1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( dateContent.getContentTypeName() ).typeData( dateContent );

        when: "content saved without required 'date' and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( dateContent.getName() );
        saveScreenshot( "date-not-valid" )

        then: "content should be invalid, because required field is empty"
        contentBrowsePanel.isContentInvalid( dateContent.getName() );
    }

    def "GIVEN creating new Date 1:1 on root WHEN data was typed and saved and the content has been published THEN new content with status equals 'Online' should be listed in the grid"()
    {
        given: "start to add a content with type 'Date'"
        Content dateContent = buildDate1_1_Content( TEST_DATE );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "data was typed and saved and the content has been published"
        contentWizardPanel.typeData( dateContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton();
        String publishMessage = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( dateContent.getName() );

        then: "content should be 'Published'"
        contentBrowsePanel.getContentStatus( dateContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );

        and: "correct notification message should be shown "
        publishMessage == String.format( Application.ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, dateContent.getDisplayName() );
    }
}
