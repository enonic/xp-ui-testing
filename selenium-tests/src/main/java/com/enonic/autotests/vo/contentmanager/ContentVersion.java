package com.enonic.autotests.vo.contentmanager;


public class ContentVersion
{
    private String modifier;

    private String modified;

    private String status;

    public ContentVersion( Builder builder )
    {
        this.modifier = builder.modifier;
        this.status = builder.status;
        this.modified = builder.modified;
    }

    public String getModifier()
    {
        return modifier;
    }

    public String getModified()
    {
        return modified;
    }

    public String getStatus()
    {
        return status;
    }

    public static class Builder
    {
        private String modifier;

        private String modified;

        private String status;

        Builder()
        {
        }

        public Builder( final ContentVersion contentVersion )
        {
            this.modifier = contentVersion.getModifier();
            this.status = contentVersion.getStatus();
            this.modified = contentVersion.getModified();
        }

        public Builder modifier( final String modifier )
        {
            this.modifier = modifier;
            return this;
        }

        public Builder modified( final String modified )
        {
            this.modified = modified;
            return this;
        }


        public Builder status( final String status )
        {
            this.status = status;
            return this;
        }

        public ContentVersion build()
        {
            return new ContentVersion( this );
        }
    }

    public static Builder builder()
    {
        return new Builder();
    }
}
