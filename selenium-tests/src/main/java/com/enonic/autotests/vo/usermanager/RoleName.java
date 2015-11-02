package com.enonic.autotests.vo.usermanager;


public enum RoleName
{

    CMS_ADMIN( "cms.admin" ), ADMIN_CONSOLE( "system.admin.login" ), CM_APP( "cms.cm.app" ), SYSTEM_ADMIN(
    "system.admin" ), SYSTEM_USER_MANAGER( "system.user.app" ), SYSTEM_AUTHENTICATED( "system.authenticated" );

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
