package com.enonic.autotests.pages.usermanager.browsepanel;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

/**
 * Panel displayed, when an User-item was clicked and selected in the BrowsePanel
 */
public abstract class UserItemStatisticsPanel
    extends Application
{
    protected final String STATISTIC_PANEL = "//div[contains(@id,'UserItemStatisticsPanel')]";

    private final String HEADER = "//div[contains(@id,'ItemStatisticsHeader']";

    private final String HEADER_BY_DISPLAY_NAME = "//div[contains(@id,'ItemStatisticsHeader']//h1[@class='title' and text()='%s']";

    private final String HEADER_DISPLAY_NAME = "//div[contains(@id,'ItemStatisticsHeader')]//h1[@class='title']";

    protected final String ROLES_AND_GROUPS_DATA_GROUP =
        STATISTIC_PANEL + "//div[contains(@id,'ItemDataGroup') and child::h2[text()='Roles & Groups']]";

    protected final String MEMBERS_DATA_GROUP = STATISTIC_PANEL + "//div[contains(@id,'ItemDataGroup') and child::h2[text()='Members']]";

    protected final String USER_INFO = STATISTIC_PANEL + "//div[contains(@id,'ItemDataGroup') and child::h2[text()='User']]";

    public UserItemStatisticsPanel( final TestSession session )
    {
        super( session );
    }

    public UserItemStatisticsPanel waitForLoaded()
    {
        if ( !waitUntilVisibleNoException( By.xpath( HEADER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_userItemStatisticPanel" );
            throw new TestFrameworkException( "UserItemStatisticsPanel was not showed!" );
        }
        return this;
    }

    public boolean isOpened( String displayName )
    {
        return isElementDisplayed( HEADER_BY_DISPLAY_NAME );
    }

    public String getItemDisplayName()
    {
        return getDisplayedString( HEADER_DISPLAY_NAME );
    }
}
