package com.enonic.autotests.pages.usermanager.browsepanel;


import java.util.List;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;

public class GroupStatisticsPanel
    extends UserItemStatisticsPanel
{
    private final String MEMBER_DISPLAY_NAMES =
        MEMBERS_DATA_GROUP + "//ul[@class='data-list' and child::li[text()='Members']]" + H6_DISPLAY_NAME;

    private final String MEMBER_NAMES = MEMBERS_DATA_GROUP + "//ul[@class='data-list' and child::li[text()='Members']]" + P_NAME;

    public GroupStatisticsPanel( final TestSession session )
    {
        super( session );
    }

    public List<String> getMemberDisplayNames()
    {
        List<String> membersDisplayNames = getDisplayedStrings( By.xpath( MEMBER_DISPLAY_NAMES ) );
        return membersDisplayNames;
    }

    public List<String> getMemberNames()
    {
        List<String> membersNames = getDisplayedStrings( By.xpath( MEMBER_NAMES ) );
        return membersNames;
    }
}
