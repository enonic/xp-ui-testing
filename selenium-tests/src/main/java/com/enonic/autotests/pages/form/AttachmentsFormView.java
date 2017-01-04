package com.enonic.autotests.pages.form;

import com.google.common.base.Strings;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

/**
 * Created on 26.12.2016.
 */
public class AttachmentsFormView
    extends FormViewPanel
{
    public static String ATTACHMENT_PROPERTY = "attachments";

    private String ATTACHMENT_UPLOADER = "//div[contains(@id,'AttachmentUploader')]";

    private String UPLOAD_BUTTON = "//button[contains(@class,'upload-button')]";

    public AttachmentsFormView( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String attachmentName = data.getString( ATTACHMENT_PROPERTY );
        if ( !Strings.isNullOrEmpty( attachmentName ) )
        {
            //do upload
        }
        return this;
    }

    public boolean isUploaderPresent()
    {
        return isElementDisplayed( ATTACHMENT_UPLOADER );
    }

    public boolean isUploadButtonDisplayed()
    {
        return isElementDisplayed( UPLOAD_BUTTON );
    }
}
