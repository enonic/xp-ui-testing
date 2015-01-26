package com.enonic.autotests.vo.usermanager;


public class UserStore
{
    private final String name;

    private final String displayName;


    UserStore( final String name, final String displayName )
    {
        this.name = name;
        this.displayName = displayName;

    }

    UserStore( final Builder builder )
    {
        name = builder.name;
        displayName = builder.displayName;
    }

    public String getName()
    {
        return name;
    }

    public String getDisplayName()
    {
        return displayName;
    }


    public static class Builder
    {
        String name;

        String displayName;

        Builder()
        {
        }

        Builder( final UserStore userStore )
        {
            this.displayName = userStore.displayName;
            this.name = userStore.name;

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
    }
}

