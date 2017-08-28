package com.enonic.autotests.pages.usermanager.browsepanel;

import java.util.List;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;

public class RoleStatisticsPanel
    extends UserItemStatisticsPanel
{
    private final String MEMBER_DISPLAY_NAMES = MEMBERS_DATA_GROUP + "//ul[@class='data-list' ]//li" + H6_DISPLAY_NAME;

    private final String MEMBER_NAMES = MEMBERS_DATA_GROUP + "//ul[@class='data-list' and child::li[text()='Members']]" + P_NAME;

    public RoleStatisticsPanel( final TestSession session )
    {
        super( session );
    }

    public List<String> getMemberDisplayNames()
    {
        List<String> roles = getDisplayedStrings( By.xpath( MEMBER_DISPLAY_NAMES ) );
        return roles;
    }

    public List<String> getMemberNames()
    {
        List<String> roles = getDisplayedStrings( By.xpath( MEMBER_NAMES ) );
        return roles;
    }
}
