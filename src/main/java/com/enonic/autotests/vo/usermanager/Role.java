package com.enonic.autotests.vo.usermanager;

public class Role
    extends Principal
{
    public static Builder create( final Role role )
    {
        return new Builder( role );
    }

    private Role( final Builder builder )
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


        public Role build()
        {
            return new Role( this );
        }
    }
}
