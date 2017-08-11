package com.enonic.autotests.pages.contentmanager.wizardpanel.context_window;


import java.util.List;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

public class PageInsertablesPanel
    extends Application
{
    public static final String TITLE = "Drag and drop components into the page";

    private final String CONTAINER = "//div[contains(@id,'InsertablesPanel')]";

    private final String GRID_PANEL = "//div[contains(@id,'InsertablesGrid')]";

    private String GRID_AVAILABLE_COMPONENTS = "//div[contains(@class,'grid-row')]//div[contains(@class,'comp ui-draggable')]//h5";

    public PageInsertablesPanel( final TestSession session )
    {
        super( session );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( CONTAINER );
    }

    public List<String> getAvailableComponents()
    {
        return getDisplayedStrings( By.xpath( GRID_AVAILABLE_COMPONENTS ) );
    }

    public String getTitle()
    {
        return getDisplayedString( CONTAINER + "/p" );
    }
}
