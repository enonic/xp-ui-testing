package com.enonic.autotests.vo.contentmanager.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UserStoreAclEntry
{
    private String principalDisplayName;

    private List<String> permissions = new ArrayList<>();

    public List<String> getPermissions()
    {
        return this.permissions;
    }

    public void setPermissions( final List<String> permissions )
    {
        this.permissions = permissions;
    }

    public String getPrincipalDisplayName()
    {
        return principalDisplayName;
    }

    public void setPrincipalDisplayName( String principalDisplayName )
    {
        this.principalDisplayName = principalDisplayName;
    }

    private UserStoreAclEntry( final UserStoreAclEntry.Builder builder )
    {
        this.permissions = builder.permissions;
        this.principalDisplayName = builder.principalDisplayName;
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( !( obj instanceof UserStoreAclEntry ) )
        {
            return false;
        }

        final UserStoreAclEntry other = (UserStoreAclEntry) obj;

        return Objects.equals( principalDisplayName, other.principalDisplayName ) && Objects.equals( permissions, other.permissions );

    }

    public static class Builder
    {
        private PermissionSuite suite;

        private String principalDisplayName;

        private List<String> permissions;

        Builder()
        {
        }

        public Builder( final UserStoreAclEntry aclEntry )
        {
            this.principalDisplayName = aclEntry.getPrincipalDisplayName();
            this.permissions = aclEntry.getPermissions();
        }

        public UserStoreAclEntry.Builder principalDisplayName( final String principalDisplayName )
        {
            this.principalDisplayName = principalDisplayName;
            return this;
        }

        public UserStoreAclEntry.Builder suite( final PermissionSuite suite )
        {
            this.suite = suite;
            return this;
        }

        public UserStoreAclEntry.Builder permissions( final List<String> permissions )
        {
            this.permissions = permissions;
            return this;
        }

        public UserStoreAclEntry build()
        {
            return new UserStoreAclEntry( this );
        }
    }

    public static ContentAclEntry.Builder builder()
    {
        return new ContentAclEntry.Builder();
    }

}
