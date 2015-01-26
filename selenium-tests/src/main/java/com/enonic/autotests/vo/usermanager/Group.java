package com.enonic.autotests.vo.usermanager;


public class Group
    extends Principal
{
    private String description;

    public static Builder create( final Group group )
    {
        return new Builder( group );
    }

    public static Builder builder()
    {
        return new Builder();
    }

    private Group( final Builder builder )
    {
        super( builder );
        this.description = builder.description;
    }

    public String getDescription()
    {
        return description;
    }

    public static class Builder
        extends Principal.Builder<Builder>
    {
        private String description;

        private Builder()
        {
            super();
        }

        private Builder( final Group group )
        {
            super( group );
            this.description = group.getDescription();
        }

        public Builder description( String description )
        {
            this.description = description;
            return this;
        }

        public Group build()
        {
            return new Group( this );
        }
    }
}
