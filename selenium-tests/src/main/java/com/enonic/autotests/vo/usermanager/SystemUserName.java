package com.enonic.autotests.vo.usermanager;

public enum SystemUserName
{

    SYSTEM_ANONYMOUS( "anonymous" ), SYSTEM_SU( "su" );

    private String role;

    private SystemUserName( String role )
    {
        this.role = role;
    }

    public String getValue()
    {
        return role;
    }
}