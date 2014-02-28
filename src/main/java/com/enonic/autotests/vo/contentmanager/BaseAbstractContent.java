package com.enonic.autotests.vo.contentmanager;


import com.enonic.wem.api.content.ContentPath;

/**
 * Base class for all types of content.
 */
public abstract class BaseAbstractContent
{
    private ContentPath parent;

    private String name;

    private ContentPath path;

    private String displayName;

    private String contentTypeName;

    protected BaseAbstractContent( Builder<?> builder )
    {
        this.name = builder.name;
        this.parent = builder.parent;
        this.path = ContentPath.from( this.parent, this.name );

        this.displayName = builder.displayName;
        this.contentTypeName = builder.contentTypeName;
    }

    public ContentPath getParent()
    {
        return parent;
    }

    public void setParent( ContentPath parent )
    {
        this.parent = parent;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public ContentPath getPath()
    {
        return path;
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

        private ContentPath parent;

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

        public Builder<T> withParent( ContentPath contentPath )
        {
            this.parent = contentPath;
            return this;
        }

        public abstract T build();
    }
}
