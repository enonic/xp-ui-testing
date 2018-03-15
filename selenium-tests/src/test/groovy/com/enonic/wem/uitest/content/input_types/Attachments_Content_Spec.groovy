package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.AttachmentsFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content

/**
 * Created on 26.12.2016.
 *
 * Tasks:
 * XP-4780 Add selenium test for Attachments-content
 *
 * Verifies: XP-4779 Broken uploader in attachment content type
 * */
class Attachments_Content_Spec
    extends Base_InputFields_Occurrences
{

    def "GIVEN creating of content with the required 'attachment' WHEN wizard is opened THEN Attachment-uploader is present AND 'Upload' button is displayed"()
    {
        given: "creating of content with the required 'attachment'"
        Content dateContent = buildAttachment1_1_Content( NORD_IMAGE_NAME );

        when: "wizard is opened"
        selectSitePressNew( dateContent.getContentTypeName() );
        AttachmentsFormView attachmentsFormView = new AttachmentsFormView( getSession() );

        then: "Attachment-uploader is present"
        attachmentsFormView.isUploaderPresent();

        and: "Upload' button should be displayed"
        attachmentsFormView.isUploadButtonDisplayed();

        and: "validation message should not be displayed"
        !attachmentsFormView.isValidationMessagePresent();
    }

    def "GIVEN attachments wizard is opened WHEN 'Save' button has been pressed AND the required attachment is not selected THEN validation message should be present"()
    {
        given: "attachments wizard is opened"
        Content dateContent = buildAttachment1_1_Content( NORD_IMAGE_NAME );
        ContentWizardPanel wizard = selectSitePressNew( dateContent.getContentTypeName() );
        AttachmentsFormView attachmentsFormView = new AttachmentsFormView( getSession() );

        when: "'Save' button has been pressed but the required attachment is not selected"
        wizard.typeName( NameHelper.uniqueName( "attachment" ) ).save();

        then: "validation message should be displayed"
        attachmentsFormView.isValidationMessagePresent();

        and: "correct validation message is displayed"
        attachmentsFormView.getValidationMessage() == Application.REQUIRED_MESSAGE;

        and: "'Publish' button should be disabled"
        !wizard.isPublishButtonEnabled();

    }

    def "GIVEN wizard for adding an attachments-content with 2 required occurrences is opened WHEN 'Save' button has been pressed AND the required attachment is not selected THEN validation message is present"()
    {
        given: "attachments wizard is opened"
        Content dateContent = buildAttachment2_4_Content( NORD_IMAGE_NAME );
        ContentWizardPanel wizard = selectSitePressNew( dateContent.getContentTypeName() );
        AttachmentsFormView attachmentsFormView = new AttachmentsFormView( getSession() );

        when: "'Save draft' button has been pressed but the required attachment not selected"
        wizard.typeName( NameHelper.uniqueName( "attachments" ) ).save();

        then: "validation message should be displayed"
        attachmentsFormView.isValidationMessagePresent();

        and: "correct validation message is displayed"
        attachmentsFormView.getValidationMessage() == String.format( Application.MIN_OCCURRENCES_REQUIRED_MESSAGE, 2 );

        and: "'Publish' button should be disabled"
        !wizard.isPublishButtonEnabled();
    }

}
