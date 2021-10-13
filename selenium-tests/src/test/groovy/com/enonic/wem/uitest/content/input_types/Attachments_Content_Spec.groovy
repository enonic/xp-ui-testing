package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.AttachmentsFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore

/**
 * Created on 26.12.2016.
 *
 * */
@Ignore
class Attachments_Content_Spec
    extends Base_InputFields_Occurrences
{

    def "WHEN new wizard required 'attachment' is opened THEN Attachment-uploader with 'Upload' button should be displayed"()
    {
        when: "new wizard with required 'attachment' is opened"
        selectSitePressNew( "attachment1_1" );
        AttachmentsFormView attachmentsFormView = new AttachmentsFormView( getSession() );

        then: "Attachment-uploader should be displayed"
        attachmentsFormView.isUploaderPresent();

        and: "Upload' button should be displayed"
        attachmentsFormView.isUploadButtonDisplayed();

        and: "validation message should not be displayed"
        !attachmentsFormView.isFormValidationMessageDisplayed();
    }

    def "GIVEN attachments wizard is opened WHEN 'Save' button has been pressed AND the required attachment is not selected THEN validation message should appear"()
    {
        given: "attachments wizard is opened"
        ContentWizardPanel wizard = selectSitePressNew( "attachment1_1" );
        AttachmentsFormView attachmentsFormView = new AttachmentsFormView( getSession() );

        when: "Display name has been typed and 'Save' button has been pressed"
        wizard.typeDisplayName( NameHelper.uniqueName( "attachment" ) );
        wizard.save();

        then: "expected validation message is displayed"
        attachmentsFormView.getFormValidationRecording( 0 ) == Application.REQUIRED_MESSAGE;

        and: "'Publish' menu item should be disabled"
        !wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );
    }

    def "GIVEN wizard content with 2 required attachment is opened WHEN 'Save' button has been pressed THEN validation message should appear"()
    {
        given: "wizard for attachments content is opened"
        ContentWizardPanel wizard = selectSitePressNew( "attachment2_4" );
        AttachmentsFormView attachmentsFormView = new AttachmentsFormView( getSession() );

        when: "required attachment is not selected and 'Save' button has been pressed"
        wizard.typeDisplayName( NameHelper.uniqueName( "attachments" ) ).save();

        then: "expected validation message is displayed"
        attachmentsFormView.getFormValidationRecording( 0 ) == String.format( Application.MIN_VALID_OCCURRENCE_REQUIRED, 2 );

        and: "'Publish' button should be disabled"
        !wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );
    }
}
