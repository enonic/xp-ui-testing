package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
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

    def "GIVEN creating new DateTime1:1 on root WHEN data typed and 'Save' and  'Publish' are pressed THEN new content with status equals 'Online' listed"()
    {
        given: "start to add a content with type 'DateTime1 1:1'"
        Content dateTimeContent = buildDateTime1_1_Content( TEST_DATE_TIME1 );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( dateTimeContent.getContentTypeName() );

        when:
        String publishMessage = contentWizardPanel.typeData(
            dateTimeContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton().waitPublishNotificationMessage(
            Application.EXPLICIT_NORMAL );
        contentWizardPanel.close( dateTimeContent.getDisplayName() );
        filterPanel.typeSearchText( dateTimeContent.getName() );

        then:
        contentBrowsePanel.getContentStatus( dateTimeContent.getName() ).equals( ContentStatus.ONLINE.getValue() );
        and:
        publishMessage == String.format( Application.CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, dateTimeContent.getDisplayName() );
    }
}
