package com.enonic.autotests.vo.usermanager;

import java.time.Instant;

public abstract class Principal
{
    private final String key;

    private final String displayName;

    private final Instant modifiedTime;

    Principal( final String principalKey, final String displayName, final Instant modifiedTime )
    {
        this.key = principalKey;
        this.displayName = displayName;
        this.modifiedTime = modifiedTime;
    }

    Principal( final Builder builder )
    {
        key = builder.key;
        displayName = builder.displayName;
        modifiedTime = builder.modifiedTime;
    }

    public String getKey()
    {
        return key;
    }

    public String getDisplayName()
    {
        return displayName;
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

        Builder()
        {
        }

        Builder( final Principal principal )
        {
            this.displayName = principal.displayName;
            this.key = principal.key;
            this.modifiedTime = principal.getModifiedTime();
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

        public B modifiedTime( final Instant modifiedTime )
        {
            this.modifiedTime = modifiedTime;
            return (B) this;
        }
    }
}

