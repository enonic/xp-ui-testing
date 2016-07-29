package com.enonic.autotests.vo.usermanager;


public enum RoleDisplayName
{
    CMS_ADMIN( "Content Manager Administrator" ), ADMIN_CONSOLE( "Administration Console Login" ), CM_APP(
    "Content Manager App" ), SYSTEM_ADMIN( "Administrator" ), SYSTEM_USER_MANAGER( "Users App" ), SYSTEM_AUTHENTICATED(
    "Authenticated" ), USERS_ADMINISTRATOR( "Users Administrator" );

    private String role;

    private RoleDisplayName( String role )
    {
        this.role = role;

    }

    public String getValue()
    {
        return role;
    }
}
