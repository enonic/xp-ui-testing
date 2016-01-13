package com.enonic.autotests.vo.usermanager;

import java.util.List;

public class Role
    extends Principal
{
    private String description;

    private List<String> memberDisplayNames;

    public String getDescription()
    {
        return description;
    }

    public List<String> getMemberDisplayNames()
    {
        return this.memberDisplayNames;
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
        this.description = builder.description;
        this.memberDisplayNames = builder.memberDisplayNames;
    }

    public static class Builder
        extends Principal.Builder<Builder>
    {
        private String description;

        private List<String> memberDisplayNames;

        private Builder()
        {
            super();
        }

        private Builder( final Role role )
        {
            super( role );
            this.description = role.getDescription();
            this.memberDisplayNames = role.getMemberDisplayNames();
        }

        public Builder description( String description )
        {
            this.description = description;
            return this;
        }

        public Builder roles( List<String> memberDisplayNames )
        {
            this.memberDisplayNames = memberDisplayNames;
            return this;
        }

        public Role build()
        {
            return new Role( this );
        }
    }
}
