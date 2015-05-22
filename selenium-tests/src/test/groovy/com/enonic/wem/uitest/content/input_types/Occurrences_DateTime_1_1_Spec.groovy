package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.pages.form.FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
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

        and: "content should be invalid, because required field- datetime:1 not typed"
        wizard.isContentInvalid( dateTimeContent.getDisplayName() );

        and: " and no options selected on the page"
        formViewPanel.getDateTimeValue().isEmpty();
    }

    def "GIVEN wizard for adding a DateTime(1:1) opened WHEN name typed and dateTime not typed AND 'Publish' button pressed THEN validation message and warning message appears"()
    {
        given: "start to add a content with type 'DateTime(1:1)'"
        Content dateTimeContent = buildDateTime1_1_Content( TEST_DATE_TIME1 );
        ContentWizardPanel wizard = selectSiteOpenWizard( dateTimeContent.getContentTypeName() );
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        when: "only the name typed and dateTime not typed"
        wizard.typeDisplayName( dateTimeContent.getDisplayName() );
        String warning = wizard.clickOnPublishButton().waitNotificationWarning( Application.EXPLICIT_NORMAL );


        then: "option filter input is present and enabled"
        warning == PUBLISH_NOTIFICATION_WARNING;
        and:
        formViewPanel.getValidationMessage() == FormViewPanel.VALIDATION_MESSAGE_1_1;
    }


    private Content buildDateTime1_1_Content( String dateTime )
    {
        String name = "datetime";
        PropertyTree contentData = new PropertyTree();
        contentData.addStrings( DateTimeFormViewPanel.DATE_TIME_PROPERTY, dateTime );

        Content dateTimeContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "date time content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":datetime1_1" ).data( contentData ).
            build();
        return dateTimeContent;
    }
}
