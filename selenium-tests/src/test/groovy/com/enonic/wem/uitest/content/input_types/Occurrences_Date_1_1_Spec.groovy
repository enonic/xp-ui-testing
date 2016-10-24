package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.exceptions.TestFrameworkException
import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.date.DatePickerPopup
import com.enonic.autotests.pages.form.DateFormViewPanel
import com.enonic.autotests.utils.NameHelper
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
        selectSiteOpenWizard( dateContent.getContentTypeName() );

        when: "Date input has been clicked"
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );
        DatePickerPopup picker = formViewPanel.showPicker();
        saveScreenshot( "date-picker-popup" );

        then: "'date picker' popup dialog is displayed"
        picker.isDisplayed();
    }

    def "GIVEN wizard for adding a Date(1:1) opened WHEN name typed and date was not typed THEN date input is empty and content has a invalid status"()
    {
        given: "start to add a content with type 'Date(1:1)'"
        Content dateContent = buildDate1_1_Content( TEST_DATE );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateContent.getContentTypeName() );

        when: "only a name typed and date was not typed"
        wizard.typeDisplayName( dateContent.getDisplayName() );
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );

        then: "option filter input is present and enabled"
        formViewPanel.isDateInputDisplayed();

        and: "content should be invalid, because required field- date not typed"
        wizard.isContentInvalid( dateContent.getDisplayName() );

        and: "date input is empty"
        formViewPanel.getDateValue().isEmpty();
    }

    def "GIVEN opened content wizard WHEN content without required 'date' saved THEN wizard has a red icon on wizard-tab"()
    {
        given: "new content with type date added'"
        Content dateContent = buildDate1_1_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateContent.getContentTypeName() ).typeData( dateContent );

        when: "content without required 'date' saved"
        wizard.save();

        then: "content should be invalid, because required field not filled"
        wizard.isContentInvalid( dateContent.getDisplayName() );
    }

    def "GIVEN opened content wizard WHEN content saved without required 'date' and wizard closed THEN content displayed in a grid with a invalid status"()
    {
        given: "new content with type date time added'"
        Content dateContent = buildDate1_1_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateContent.getContentTypeName() ).typeData( dateContent );

        when: "content saved without required 'date' and wizard closed"
        wizard.save().close( dateContent.getDisplayName() );
        filterPanel.typeSearchText( dateContent.getName() );
        saveScreenshot( "date-not-valid" )

        then: "content should be invalid, because required field not filled"
        contentBrowsePanel.isContentInvalid( dateContent.getName() );
    }

    def "GIVEN creating new Date 1:1 on root WHEN data typed and 'Save' and  'Publish' are pressed THEN new content with status equals 'Online' listed"()
    {
        given: "start to add a content with type 'Date'"
        Content dateContent = buildDate1_1_Content( TEST_DATE );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( dateContent.getContentTypeName() );

        when: "data typed and 'Save' and  'Publish' are pressed"
        contentWizardPanel.typeData( dateContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton();
        String publishMessage = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        SaveBeforeCloseDialog modalDialog = contentWizardPanel.close( dateContent.getDisplayName() );
        if ( modalDialog != null )
        {
            saveScreenshot( NameHelper.uniqueName( "err-close-wizard" ) );
            throw new TestFrameworkException( "'save before closing' modal dialog present but all changes were saved! " )
        }
        filterPanel.typeSearchText( dateContent.getName() );

        then: "content is 'online now'"
        contentBrowsePanel.getContentStatus( dateContent.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );

        and: "correct notification message was shown "
        publishMessage == String.format( Application.ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, dateContent.getDisplayName() );
    }
}
