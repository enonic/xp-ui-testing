package com.enonic.autotests.vo.contentmanager;


public class PageComponent
{
    private final String name;

    private final String type;


    protected PageComponent( final Builder builder )
    {
        this.name = builder.name;
        this.type = builder.type;

    }

    public static Builder builder()
    {
        return new Builder();
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public static class Builder
    {
        private String name;

        private String type;


        public Builder name( final String name )
        {
            this.name = name;
            return this;
        }

        Builder()
        {
        }

        public Builder( Content content )
        {
            this.name = content.getName();
            this.type = content.getDisplayName();

        }

        public Builder type( String type )
        {
            this.type = type;
            return this;
        }


        public PageComponent build()
        {
            return new PageComponent( this );
        }
    }
}

