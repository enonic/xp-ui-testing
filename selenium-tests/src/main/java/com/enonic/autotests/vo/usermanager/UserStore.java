package com.enonic.autotests.vo.usermanager;


public class UserStore
{
    private final String name;

    private final String displayName;

    private final String description;

    public UserStore( final Builder builder )
    {
        this.name = builder.name;
        this.displayName = builder.displayName;
        this.description = builder.description;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDisplayName()
    {
        return this.displayName;
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

        private Builder()
        {
        }

        private Builder( final UserStore userStore )
        {
            this.displayName = userStore.displayName;
            this.name = userStore.name;
            this.description = userStore.description;

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

        public UserStore build()
        {
            return new UserStore( this );
        }
    }
}

