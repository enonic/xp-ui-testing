package com.enonic.autotests.vo.usermanager;


public enum RoleName
{

    CMS_ADMIN( "cms.admin" ), ADMIN_CONSOLE( "system.admin.login" );

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
