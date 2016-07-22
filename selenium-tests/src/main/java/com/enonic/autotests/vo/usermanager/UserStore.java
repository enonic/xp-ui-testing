package com.enonic.autotests.vo.usermanager;


import java.util.List;

import com.enonic.autotests.vo.contentmanager.security.UserStoreAclEntry;

public class UserStore
{
    private final String name;

    private final String displayName;

    private final String description;

    private final String idProviderDisplayName;

    private List<UserStoreAclEntry> aclEntries;

    public UserStore( final Builder builder )
    {
        this.name = builder.name;
        this.displayName = builder.displayName;
        this.description = builder.description;
        this.idProviderDisplayName = builder.idProviderDisplayName;
        this.aclEntries = builder.aclEntries;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public List<UserStoreAclEntry> getAclEntries()
    {
        return this.aclEntries;
    }

    public String getIdProviderDisplayName()
    {
        return this.idProviderDisplayName;
    }

    public String getDescription()
    {
        return this.description;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        String name;

        String displayName;

        String description;

        String idProviderDisplayName;

        List<UserStoreAclEntry> aclEntries;

        private Builder()
        {
        }

        private Builder( final UserStore userStore )
        {
            this.displayName = userStore.displayName;
            this.name = userStore.name;
            this.description = userStore.description;
            this.idProviderDisplayName = userStore.idProviderDisplayName;
            this.aclEntries = userStore.aclEntries;
        }

        public Builder name( final String name )
        {
            this.name = name;
            return this;
        }

        public Builder displayName( final String displayName )
        {
            this.displayName = displayName;
            return this;
        }

        public Builder description( final String description )
        {
            this.description = description;
            return this;
        }

        public Builder idProviderDisplayName( final String idProviderDisplayName )
        {
            this.idProviderDisplayName = idProviderDisplayName;
            return this;
        }

        public Builder aclEntries( List<UserStoreAclEntry> aclEntries )
        {
            this.aclEntries = aclEntries;
            return this;
        }

        public UserStore build()
        {
            return new UserStore( this );
        }
    }
}

