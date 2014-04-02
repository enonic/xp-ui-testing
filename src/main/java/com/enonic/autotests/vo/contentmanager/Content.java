package com.enonic.autotests.vo.contentmanager;


import com.enonic.wem.api.content.ContentPath;
import com.enonic.wem.api.schema.content.ContentTypeName;

public class Content
{
    private ContentPath parent;

    private String name;

    private ContentPath path;

    private String displayName;

    private String contentTypeName;

    protected Content( Builder builder )
    {
        this.name = builder.name;
        this.parent = builder.parent;
        this.path = ContentPath.from( this.parent, this.name );

        this.displayName = builder.displayName;
        this.contentTypeName = builder.contentTypeName;
    }

    public static Builder builder()
    {
        return new Builder();
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

    public static class Builder
    {
        private String name;

        private String displayName;

        private String contentTypeName;

        private ContentPath parent;

        public Builder withName( String name )
        {
            this.name = name;
            return this;
        }

        public Builder withDisplayName( String displayName )
        {
            this.displayName = displayName;
            return this;
        }

        public Builder withType( String ctName )
        {
            this.contentTypeName = ctName;
            return this;
        }

        public Builder withParent( ContentPath contentPath )
        {
            this.parent = contentPath;
            return this;
        }

        public Builder withContentType( String contentTypeName )
        {
            this.contentTypeName = contentTypeName;
            return this;
        }

        public Builder withContentType( ContentTypeName contentTypeName )
        {
            this.contentTypeName = contentTypeName.toString();
            return this;
        }

        public Content build()
        {
            return new Content( this );
        }
    }
}
