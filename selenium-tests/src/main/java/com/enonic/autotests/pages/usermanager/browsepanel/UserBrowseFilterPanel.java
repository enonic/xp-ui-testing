package com.enonic.autotests.pages.usermanager.browsepanel;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BaseBrowseFilterPanel;
import com.enonic.autotests.pages.BrowsePanel;

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
}
