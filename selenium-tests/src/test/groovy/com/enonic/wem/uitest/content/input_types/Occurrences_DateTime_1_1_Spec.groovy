package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.exceptions.TestFrameworkException
import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
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


    def "GIVEN wizard for adding a DateTime(1:1) opened WHEN name typed and dateTime not typed THEN dateTime input is empty and content has a invalid status"()
    {
        given: "start to add a content with type 'DateTime(1:1)'"
        Content dateTimeContent = buildDateTime1_1_Content( TEST_DATE_TIME1 );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateTimeContent.getContentTypeName() );

        when: "only the name typed and dateTime not typed"
        wizard.typeDisplayName( dateTimeContent.getDisplayName() );
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "option filter input is present and enabled"
        formViewPanel.isDateTimeInputDisplayed();

        and: "content should be invalid, because required field- datetime1:1 not typed"
        wizard.isContentInvalid( dateTimeContent.getDisplayName() );

        and: " and no options selected on the page"
        formViewPanel.getDateTimeValue().isEmpty();
    }

    def "GIVEN opened content wizard WHEN content without required 'date time ' saved THEN wizard has a red icon"()
    {
        given: "new content with type date time added'"
        Content dateTimeContent = buildDateTime1_1_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateTimeContent.getContentTypeName() ).typeData( dateTimeContent );

        when: "content opened for edit"
        wizard.save();

        then: "content should be invalid, because required field not filled"
        wizard.isContentInvalid( dateTimeContent.getDisplayName() );
    }

    def "GIVEN opened content wizard WHEN content without required 'date time ' saved and wizard closed THEN grid row with it content has a red icon"()
    {
        given: "new content with type date time added'"
        Content dateTimeContent = buildDateTime1_1_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateTimeContent.getContentTypeName() ).typeData( dateTimeContent );

        when: "content opened for edit"
        wizard.save().close( dateTimeContent.getDisplayName() );
        filterPanel.typeSearchText( dateTimeContent.getName() );
        TestUtils.saveScreenshot( getSession(), "date-time-not-valid1" )

        then: "content should be invalid, because required field not filled"
        contentBrowsePanel.isContentInvalid( dateTimeContent.getName() );
    }

    def "GIVEN creating new DateTime1:1 on root WHEN data typed and 'Save' and  'Publish' are pressed THEN new content with status equals 'Online' listed"()
    {
        given: "start to add a content with type 'DateTime1 1:1'"
        Content dateTimeContent = buildDateTime1_1_Content( TEST_DATE_TIME1 );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( dateTimeContent.getContentTypeName() );

        when:
        String publishMessage = contentWizardPanel.typeData(
            dateTimeContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton().waitPublishNotificationMessage(
            Application.EXPLICIT_NORMAL );
        SaveBeforeCloseDialog modalDialog = contentWizardPanel.close( dateTimeContent.getDisplayName() );
        if ( modalDialog != null )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err-close-wizard" ) );
            throw new TestFrameworkException( "'save before closing' modal dialog present but all changes were saved! " )
        }
        filterPanel.typeSearchText( dateTimeContent.getName() );

        then:
        contentBrowsePanel.getContentStatus( dateTimeContent.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
        and:
        publishMessage == String.format( Application.CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, dateTimeContent.getDisplayName() );
    }
}
