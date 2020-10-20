package com.enonic.autotests.vo.contentmanager;


public class ContentVersion
{
    private String modifier;

    private String modified;

    private String action;

    public ContentVersion( Builder builder )
    {
        this.modifier = builder.modifier;
        this.action = builder.action;
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

    public String getAction()
    {
        return action;
    }

    public static class Builder
    {
        private String modifier;

        private String modified;

        private String action;

        Builder()
        {
        }

        public Builder( final ContentVersion contentVersion )
        {
            this.modifier = contentVersion.getModifier();
            this.action = contentVersion.getAction();
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


        public Builder action( final String action )
        {
            this.action = action;
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
