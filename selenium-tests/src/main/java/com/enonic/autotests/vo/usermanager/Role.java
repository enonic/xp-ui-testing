package com.enonic.autotests.vo.usermanager;

public class Role
    extends Principal
{
    private String name;

    private String description;

    public String getDescription()
    {
        return description;
    }

    public static Builder create( final Role role )
    {
        return new Builder( role );
    }

    public static Builder builder()
    {
        return new Builder();
    }

    private Role( final Builder builder )
    {
        super( builder );
    }

    public static class Builder
        extends Principal.Builder<Builder>
    {
        private String description;

        private Builder()
        {
            super();
        }

        private Builder( final Role role )
        {
            super( role );
            this.description = role.getDescription();
        }

        public Builder description( String description )
        {
            this.description = description;
            return this;
        }

        public Role build()
        {
            return new Role( this );
        }
    }
}
