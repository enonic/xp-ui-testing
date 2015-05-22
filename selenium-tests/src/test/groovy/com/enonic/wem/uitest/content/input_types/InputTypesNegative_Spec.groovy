package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.pages.form.FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class InputTypesNegative_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    String WRONG_TIME = "191:01";

    @Shared
    String TEST_DATE_TIME1 = "2015-02-28 19:01";

    @Shared
    String WRONG_DATE_TIME = "015-02-28 19:01";

    def "GIVEN saving of content with type 'Time 0:0' and value of time is wrong WHEN time typed and content published THEN warning message appears"()
    {
        given: "add a content with type 'Time'"
        Content timeContent = buildTime0_0_Content( WRONG_TIME );
        ContentWizardPanel wizard = selectSiteOpenWizard( timeContent.getContentTypeName() );

        when: "site expanded and just created content selected and 'Edit' button clicked"
        String message = wizard.typeData( timeContent ).clickOnPublishButton().waitNotificationWarning( Application.EXPLICIT_NORMAL );

        then: "actual value in the form view and expected should be equals"
        message == PUBLISH_NOTIFICATION_WARNING;

    }

    def "GIVEN wizard for adding a required DateTime(1:1) opened WHEN name typed and dateTime not typed AND 'Publish' button pressed THEN validation message and warning message appears"()
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

}
