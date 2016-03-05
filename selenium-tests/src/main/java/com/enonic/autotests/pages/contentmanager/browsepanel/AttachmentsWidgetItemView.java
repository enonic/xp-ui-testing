package com.enonic.autotests.pages.contentmanager.browsepanel;


import java.util.List;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

public class AttachmentsWidgetItemView
    extends Application

{
    private final static String NO_ATTACHMENTS_MESSAGE = "This item has no attachments";

    private final String DIV_CONTAINER = "//div[contains(@id,'AttachmentsWidgetItemView')]";

    private final String EMPTY_ATTACHMENTS_XPATH = DIV_CONTAINER + "//span[@class='att-placeholder']";

    private final String ATTACHMENTS = DIV_CONTAINER + "//ul[@class='attachment-list']/li/a";

    public AttachmentsWidgetItemView( final TestSession session )
    {
        super( session );
    }

    public List<String> getAttachmentNames()
    {
        return getDisplayedStrings( By.xpath( ATTACHMENTS ) );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( DIV_CONTAINER );
    }

    public boolean isHasAttachments()
    {
        return !isElementDisplayed( EMPTY_ATTACHMENTS_XPATH );
    }

    public String getMessage()
    {
        return getDisplayedString( EMPTY_ATTACHMENTS_XPATH );
    }
}
