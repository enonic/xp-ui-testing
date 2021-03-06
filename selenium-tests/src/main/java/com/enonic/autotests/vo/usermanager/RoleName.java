package com.enonic.autotests.vo.usermanager;


public enum RoleName
{

    CONTENT_MANAGER_ADMINISTRATOR( "cms.admin" ), ADMIN_CONSOLE( "system.admin.login" ), CM_APP( "cms.cm.app" ), CM_EXPERT(
    "cms.expert" ), SYSTEM_ADMIN( "system.admin" ), SYSTEM_USER_MANAGER( "system.user.app" ), SYSTEM_AUTHENTICATED(
    "system.authenticated" ), SYSTEM_EVERYONE( "system.everyone" ), USER_ADMINISTRATOR( "system.user.admin" );

    private String role;

    private RoleName( String role )
    {
        this.role = role;
    }

    public String getValue()
    {
        return role;
    }
}
