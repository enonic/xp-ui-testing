package com.enonic.autotests.vo.contentmanager;


import com.enonic.wem.api.content.ContentPath;
import com.enonic.wem.api.data.PropertyTree;
import com.enonic.wem.api.schema.content.ContentTypeName;

public class Content
{
    private final ContentPath parent;

    private final String name;

    private final ContentPath path;

    private final String displayName;

    private final String contentTypeName;

    private final PropertyTree propertyTree;

    protected Content( final Builder builder )
    {
        this.name = builder.name;
        this.parent = builder.parent;
        this.path = ContentPath.from( this.parent, this.name );

        this.displayName = builder.displayName;
        this.contentTypeName = builder.contentTypeName;

        this.propertyTree = builder.propertyTree;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public ContentPath getParent()
    {
        return parent;
    }

    public String getName()
    {
        return name;
    }

    public ContentPath getPath()
    {
        return path;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getContentTypeName()
    {
        return contentTypeName;
    }

    public PropertyTree getPropertyTree()
    {
        return propertyTree;
    }

    public static class Builder
    {
        private String name;

        private String displayName;

        private String contentTypeName;

        private ContentPath parent;

        private PropertyTree propertyTree;

        public Builder name( String name )
        {
            this.name = name;
            return this;
        }

        public Builder displayName( String displayName )
        {
            this.displayName = displayName;
            return this;
        }

        public Builder parent( ContentPath contentPath )
        {
            this.parent = contentPath;
            return this;
        }

        public Builder contentType( String contentTypeName )
        {
            this.contentTypeName = contentTypeName;
            return this;
        }

        public Builder contentType( ContentTypeName contentTypeName )
        {
            this.contentTypeName = contentTypeName.toString();
            return this;
        }

        public Builder propertyTree( final PropertyTree value )
        {
            this.propertyTree = value;
            return this;
        }

        public Content build()
        {
            return new Content( this );
        }
    }
}
