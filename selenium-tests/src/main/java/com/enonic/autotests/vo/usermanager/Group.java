package com.enonic.autotests.vo.usermanager;


import java.util.List;

public class Group
    extends Principal
{
    private String description;

    private List<String> memberDisplayNames;

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
        this.memberDisplayNames = builder.memberDisplayNames;
    }

    public String getDescription()
    {
        return description;
    }

    public List<String> getMemberDisplayNames()
    {
        return memberDisplayNames;
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

        private Builder( final Group group )
        {
            super( group );
            this.description = group.getDescription();
            this.memberDisplayNames = group.getMemberDisplayNames();
        }

        public Builder description( String description )
        {
            this.description = description;
            return this;
        }

        public Builder memberDisplayNames( String description )
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
