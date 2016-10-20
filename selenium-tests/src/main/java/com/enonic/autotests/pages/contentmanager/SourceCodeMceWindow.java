package com.enonic.autotests.pages.contentmanager;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

/**
 * Created on 20.10.2016.
 */
public class SourceCodeMceWindow
    extends Application
{
    private final String WINDOW = "//div[contains(@class,'mce-window')]";

    private final String WINDOW_TITLE = "Source code";

    private final String TITLE_XPATH = WINDOW + "//div[@class='mce-title']";

    public SourceCodeMceWindow( final TestSession session )
    {
        super( session );
    }

    public void waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( WINDOW ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_source_code_dialog" );
            throw new TestFrameworkException( "'Source Code' dialog was not opened!" );
        }
    }

    public boolean isOpened()
    {
        return isElementDisplayed( WINDOW );
    }
}
