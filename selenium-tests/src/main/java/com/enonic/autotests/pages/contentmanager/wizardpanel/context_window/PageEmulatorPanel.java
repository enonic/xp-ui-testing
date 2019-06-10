package com.enonic.autotests.pages.contentmanager.wizardpanel.context_window;

import java.util.List;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.EmulatorResolution;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 29.08.2016.
 */
public class PageEmulatorPanel
    extends Application
{

    public static final String TITLE = "Emulate different client's physical sizes";

    private final String CONTAINER = "//div[contains(@id,'EmulatorPanel')]";

    private final String GRID_PANEL = "//div[contains(@id,'EmulatorGrid')]";

    private String GRID_AVAILABLE_RESOLUTIONS_NAME = GRID_PANEL + SLICK_ROW + "//h5";

    private String RESOLUTION_ROW = "//div[contains(@class,'slick-row') and descendant::h5[text()='%s']]";

    public PageEmulatorPanel( final TestSession session )
    {
        super( session );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( CONTAINER );
    }

    public List<String> getAvailableResolutions()
    {
        return getDisplayedStrings( By.xpath( GRID_AVAILABLE_RESOLUTIONS_NAME ) );
    }

    public String getTitle()
    {
        return getDisplayedString( CONTAINER + "/p" );
    }

    public PageEmulatorPanel selectResolution( EmulatorResolution resolution )
    {
        String resolutionXpath = String.format( RESOLUTION_ROW, resolution.getValue() );
        if ( !isElementDisplayed( resolutionXpath ) )
        {

        }
        getDisplayedElement( By.xpath( resolutionXpath ) ).click();
        sleep( 500 );
        return this;
    }
}
