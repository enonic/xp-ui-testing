package com.enonic.autotests.vo.usermanager;

import java.time.Instant;

public abstract class Principal
{
    private final String key;

    private final String displayName;

    private final String name;

    private final Instant modifiedTime;


    protected Principal( final Builder builder )
    {
        this.key = builder.key;
        this.displayName = builder.displayName;
        this.modifiedTime = builder.modifiedTime;
        this.name = builder.name;

    }

    public String getKey()
    {
        return key;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getName()
    {
        return name;
    }

    public Instant getModifiedTime()
    {
        return modifiedTime;
    }

    public static abstract class Builder<B>
    {
        String key;

        String displayName;

        Instant modifiedTime;

        String name;

        Builder()
        {
        }

        Builder( final Principal principal )
        {
            this.displayName = principal.displayName;
            this.key = principal.key;
            this.modifiedTime = principal.getModifiedTime();
            this.name = principal.getName();
        }

        @SuppressWarnings("unchecked")
        public B key( final String key )
        {
            this.key = key;
            return (B) this;
        }


        @SuppressWarnings("unchecked")
        public B displayName( final String displayName )
        {
            this.displayName = displayName;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B name( final String name )
        {
            this.name = name;
            return (B) this;
        }


        public B modifiedTime( final Instant modifiedTime )
        {
            this.modifiedTime = modifiedTime;
            return (B) this;
        }
    }
}

