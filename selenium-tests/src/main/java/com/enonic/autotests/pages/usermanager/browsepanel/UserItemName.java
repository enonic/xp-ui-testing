package com.enonic.autotests.pages.usermanager.browsepanel;

/**
 * Created on 01.09.2017.
 */
public enum UserItemName
{
    USERS_FOLDER( "users" ), USER( "user" ), GROUPS_FOLDER( "groups" ), GROUP( "group" ), ROLES_FOLDER( "roles" ), ROLE( "role" ), SYSTEM(
    "system" ), USER_STORE( "user_store" );

    private UserItemName( String type )
    {
        this.value = type;
    }

    private String value;

    public String getValue()
    {
        return this.value;
    }
}
