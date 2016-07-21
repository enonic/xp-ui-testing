package com.enonic.autotests.vo.usermanager;


import java.util.List;

public class UserStore
{
    private final String name;

    private final String displayName;

    private final String description;

    private final String idProviderDisplayName;


    private List<String> aclEntryDisplayNames;

    public UserStore( final Builder builder )
    {
        this.name = builder.name;
        this.displayName = builder.displayName;
        this.description = builder.description;
        this.idProviderDisplayName = builder.idProviderDisplayName;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDisplayName()
    {
        return this.displayName;
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

        private Builder()
        {
        }

        private Builder( final UserStore userStore )
        {
            this.displayName = userStore.displayName;
            this.name = userStore.name;
            this.description = userStore.description;
            this.idProviderDisplayName = userStore.idProviderDisplayName;
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

        public UserStore build()
        {
            return new UserStore( this );
        }
    }
}

