package com.enonic.autotests.vo.contentmanager.security;

import java.util.Objects;


public class UserStoreAclEntry
{
    private String principalDisplayName;

    private UserStoreAccess userStoreAccesses;

    public UserStoreAccess getUserStoreAccess()
    {
        return this.userStoreAccesses;
    }

    public void setAccess( final UserStoreAccess userStoreAccesses )
    {
        this.userStoreAccesses = userStoreAccesses;
    }

    public String getPrincipalDisplayName()
    {
        return principalDisplayName;
    }

    public void setPrincipalName( String principalName )
    {
        this.principalDisplayName = principalName;
    }

    private UserStoreAclEntry( final Builder builder )
    {
        this.userStoreAccesses = builder.userStoreAccesses;
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

        return Objects.equals( principalDisplayName, other.principalDisplayName ) &&
            Objects.equals( userStoreAccesses, other.userStoreAccesses );
    }

    public static class Builder
    {

        private String principalDisplayName;

        private UserStoreAccess userStoreAccesses;

        Builder()
        {
        }

        public Builder( final UserStoreAclEntry aclEntry )
        {
            this.principalDisplayName = aclEntry.getPrincipalDisplayName();
            this.userStoreAccesses = aclEntry.getUserStoreAccess();
        }

        public Builder principalName( final String principalName )
        {
            this.principalDisplayName = principalName;
            return this;
        }

        public Builder access( final UserStoreAccess userStoreAccesses )
        {
            this.userStoreAccesses = userStoreAccesses;
            return this;
        }

        public UserStoreAclEntry build()
        {
            return new UserStoreAclEntry( this );
        }
    }

    public static Builder builder()
    {
        return new Builder();
    }

}
