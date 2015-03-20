package com.enonic.autotests.vo.contentmanager.security;

public enum PermissionSuite
{
    CAN_READ( "Can Read" ), CAN_WRITE( "Can Write" ), CAN_PUBLISH( "Can Publish" ), FULL_ACCESS( "Full Access" );

    private PermissionSuite( String value )
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }

    private String value;

}
