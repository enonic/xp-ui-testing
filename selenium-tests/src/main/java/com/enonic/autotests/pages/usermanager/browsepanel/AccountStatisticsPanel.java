package com.enonic.autotests.pages.usermanager.browsepanel;


import java.util.List;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;

public class AccountStatisticsPanel
    extends UserItemStatisticsPanel
{
    private final String ROLE_DISPLAY_NAMES =
        ROLES_AND_GROUPS_DATA_GROUP + "//ul[@class='data-list' and child::li[text()='Roles']]" + H6_DISPLAY_NAME;

    private final String ROLE_NAMES = ROLES_AND_GROUPS_DATA_GROUP + "//ul[@class='data-list' and child::li[text()='Roles']]" + P_NAME;

    private final String GROUP_NAMES = ROLES_AND_GROUPS_DATA_GROUP + "//ul[@class='data-list' and child::li[text()='Groups']]" + P_NAME;


    private final String EMAIL = USER_INFO + "//ul[@class='data-list' and child::li[text()='E-mail']]//li[2]";

    public AccountStatisticsPanel( final TestSession session )
    {
        super( session );
    }

    public String getEmail()
    {
        if ( isElementDisplayed( EMAIL ) )
        {
            return getDisplayedString( EMAIL );
        }
        else
        {
            return "";
        }
    }

    public List<String> getRoleDisplayNames()
    {
        List<String> roles = getDisplayedStrings( By.xpath( ROLE_DISPLAY_NAMES ) );
        return roles;
    }

    public List<String> getRoleNames()
    {
        List<String> roleNames = getDisplayedStrings( By.xpath( ROLE_NAMES ) );
        return roleNames;
    }

    public List<String> getGroupNames()
    {
        List<String> groupNames = getDisplayedStrings( By.xpath( GROUP_NAMES ) );
        return groupNames;
    }
}
