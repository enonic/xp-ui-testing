package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class InputTypesNegative_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    String WRONG_TIME = "191:01";

    def "GIVEN saving of content with type 'Time' value of time is wrong WHEN time typed and content published THEN warning message appears"()
    {
        given: "add a content with type 'Time'"
        Content timeContent = buildTimeContent( WRONG_TIME );
        ContentWizardPanel wizard = selectSiteOpenWizard( timeContent.getContentTypeName() );

        when: "site expanded and just created content selected and 'Edit' button clicked"
        String message = wizard.typeData( timeContent ).clickOnPublishButton().waitNotificationWarning( Application.EXPLICIT_NORMAL );

        then: "actual value in the form view and expected should be equals"
        message == PUBLISH_NOTIFICATION_WARNING;

    }

}
