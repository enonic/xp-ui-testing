package com.enonic.autotests.vo.usermanager;

public class User
    extends Principal
{
    public static Builder create( final Role role )
    {
        return new Builder( role );
    }

    private User( final Builder builder )
    {
        super( builder );
    }

    public static class Builder
        extends Principal.Builder<Builder>
    {
        private Builder()
        {
            super();
        }

        private Builder( final Role role )
        {
            super( role );
        }


        public User build()
        {
            return new User( this );
        }
    }
}

