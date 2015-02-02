package com.enonic.autotests.pages.contentmanager.browsepanel;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

public class SortContentDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'SortContentDialog')]";

    public SortContentDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return findElements( By.xpath( DIALOG_CONTAINER ) ).size() > 0;
    }

    public SortContentDialog waitForLoaded( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout ) )
        {
            throw new TestFrameworkException( "SortContentDialog was not showed!" );
        }
        return this;
    }

    public boolean isPresent()
    {
        return findElements( By.xpath( DIALOG_CONTAINER ) ).size() > 0;
    }
}
