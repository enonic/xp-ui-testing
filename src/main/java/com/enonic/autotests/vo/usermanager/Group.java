package com.enonic.autotests.vo.usermanager;


public class Group
    extends Principal
{
    public static Builder create( final Group group )
    {
        return new Builder( group );
    }

    private Group( final Builder builder )
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

        private Builder( final Group group )
        {
            super( group );
        }


        public Group build()
        {
            return new Group( this );
        }
    }
}
