package com.enonic.autotests.pages.usermanager.browsepanel;


import org.openqa.selenium.Keys;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BaseBrowseFilterPanel;
import com.enonic.autotests.pages.BrowsePanel;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class UserBrowseFilterPanel
    extends BaseBrowseFilterPanel
{
    /**
     * The constructor
     *
     * @param session
     */
    public UserBrowseFilterPanel( TestSession session )
    {
        super( session );

    }

    @Override
    public BrowsePanel getBrowsePanel()
    {
        return new UserBrowsePanel( getSession() );
    }

    /**
     * @param text
     */
    public UserBrowseFilterPanel typeSearchText( String text )
    {
        getLogger().info( "query will be applied : " + text );
        clearAndType( searchInput, text );
        searchInput.sendKeys( Keys.ENTER );
        sleep( 1000 );
        getLogger().info( "Filtered by : " + text );
        return this;
    }
}
