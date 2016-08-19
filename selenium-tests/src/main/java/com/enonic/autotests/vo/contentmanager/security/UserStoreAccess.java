package com.enonic.autotests.vo.contentmanager.security;


public enum UserStoreAccess
{
    READ( "Read" ), CREATE_USERS( "Create Users" ), WRITE_USERS( "Write Users" ), USER_STORE_MANAGER( "User Store Manager" ), ADMINISTRATOR(
    "Administrator" );

    private UserStoreAccess( String value )
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }

    private String value;

    public static UserStoreAccess findByValue( String value )
    {
        for ( UserStoreAccess v : values() )
        {
            if ( v.getValue().equals( value ) )
            {
                return v;
            }
        }
        return null;
    }
}
