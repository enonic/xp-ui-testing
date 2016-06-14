package com.enonic.autotests.pages.contentmanager.wizardpanel.macro;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.TestUtils;

public class MacroPreviewPanel
    extends Application
{
    protected final String PREVIEW_PANEL = MacroModalDialog.DIALOG_CONTAINER + "//div[@class='panel macro-preview-panel']";

    protected final String PREVIEW_MESSAGE_XPATH = PREVIEW_PANEL + "//div[contains(@class,'preview-message')]";

    protected final String PREVIEW_CONTENT_XPATH = PREVIEW_PANEL + "//div[contains(@class,'preview-content')]";

    protected final String IFRAME_EMBEDDED_XPATH = PREVIEW_PANEL + "//div[contains(@class,'preview-content')]/iframe";


    public static final String EXPECTED_IFRAME_MESSAGE_PREVIEW_TAB = "Expected an <iframe> element in Embed macro";


    public MacroPreviewPanel( final TestSession session )
    {
        super( session );
    }

    public String getPreviewMessage()
    {
        if ( !isElementDisplayed( PREVIEW_MESSAGE_XPATH ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_preview_message" );
            throw new TestFrameworkException( "preview message was not shown" );
        }
        return getDisplayedString( PREVIEW_MESSAGE_XPATH );
    }

    public String getPreviewContentMessage()
    {
        if ( !isElementDisplayed( PREVIEW_CONTENT_XPATH ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_preview_content_message" );
            throw new TestFrameworkException( "preview-content message was not shown" );
        }
        return getDisplayedString( PREVIEW_CONTENT_XPATH );
    }

    public boolean isIFrameEmbedded( String src )
    {
        if ( !isElementDisplayed( IFRAME_EMBEDDED_XPATH ) )
        {
            return false;
        }
        return getDisplayedElement( By.xpath( IFRAME_EMBEDDED_XPATH ) ).getAttribute( "src" ).contains( src );
    }
}
