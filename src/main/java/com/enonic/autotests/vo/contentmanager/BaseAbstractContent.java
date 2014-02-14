package com.enonic.autotests.vo.contentmanager;


/**
 * Base class for all types of content.
 */
public abstract class BaseAbstractContent
{
    private String name;

    private String displayName;

    private String contentTypeName;

    private String[] contentPath;

    protected BaseAbstractContent( Builder<?> builder )
    {
        this.name = builder.name;
        this.displayName = builder.displayName;
        this.contentPath = builder.contentPath;
        this.contentTypeName = builder.contentTypeName;
    }

    public String[] getContentPath()
    {
        return contentPath;
    }

    public void setContentPath( String[] contentPath )
    {
        this.contentPath = contentPath;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    public String getContentTypeName()
    {
        return contentTypeName;
    }

    public void setContentTypeName( String contentTypeName )
    {
        this.contentTypeName = contentTypeName;
    }

    public static abstract class Builder<T extends BaseAbstractContent>
    {
        private String name;

        private String displayName;

        private String contentTypeName;

        private String[] contentPath;

        public Builder<T> withName( String name )
        {
            this.name = name;
            return this;
        }

        public Builder<T> withDisplayName( String displayName )
        {
            this.displayName = displayName;
            return this;
        }

        public Builder<T> withType( String ctName )
        {
            this.contentTypeName = ctName;
            return this;
        }

        public Builder<T> withContentPath( String[] contentPath )
        {
            this.contentPath = contentPath;
            return this;
        }

        public abstract T build();
    }
}
