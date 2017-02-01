package com.enonic.autotests.vo.contentmanager.security;

public enum PermissionSuite
{
    CAN_READ( "Can Read" ), CAN_WRITE( "Can Write" ), CAN_PUBLISH( "Can Publish" ), FULL_ACCESS( "Full Access" ), CUSTOM( "Custom..." );

    private PermissionSuite( String value )
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }

    private String value;

    public static PermissionSuite getSuite( String suiteString )
    {
        switch ( suiteString )
        {
            case "Full Access":
                return PermissionSuite.FULL_ACCESS;
            case "Can Read":
                return PermissionSuite.CAN_READ;
            default:
                return PermissionSuite.CUSTOM;
        }
    }

}
