package com.enonic.autotests.vo.contentmanager;

public class ContentSettings
{
    private final String language;

    private final String owner;


    protected ContentSettings( final Builder builder )
    {
        this.language = builder.language;
        this.owner = builder.owner;

    }

    public static Builder builder()
    {
        return new Builder();
    }

    public String getOwner()
    {
        return owner;
    }

    public String getLanguage()
    {
        return language;
    }


    public static class Builder
    {
        private String language;

        private String owner;


        public Builder owner( final String owner )
        {
            this.owner = owner;
            return this;
        }

        Builder()
        {
        }

        public Builder( Content content )
        {
            this.owner = content.getName();
            this.language = content.getDisplayName();

        }

        public Builder language( String language )
        {
            this.language = language;
            return this;
        }


        public ContentSettings build()
        {
            return new ContentSettings( this );
        }
    }
}
