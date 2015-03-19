package com.enonic.autotests.vo.contentmanager;


import java.util.List;

import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry;
import com.enonic.xp.content.ContentPath;
import com.enonic.xp.data.PropertyTree;
import com.enonic.xp.schema.content.ContentTypeName;

public class Content
{
    private final ContentPath parent;

    private final String name;

    private final ContentPath path;

    private final String displayName;

    private final String contentTypeName;

    private final List<ContentAclEntry> aclEntries;

    private final PropertyTree data;

    protected Content( final Builder builder )
    {
        this.name = builder.name;
        this.parent = builder.parent;
        this.path = ContentPath.from( this.parent, this.name );

        this.displayName = builder.displayName;
        this.contentTypeName = builder.contentTypeName;

        this.data = builder.data;
        this.aclEntries = builder.aclEntries;
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

    public List<ContentAclEntry> getAclEntries()
    {
        return this.aclEntries;
    }

    public PropertyTree getData()
    {
        return data;
    }

    public static class Builder<B>
    {
        private String name;

        private String displayName;

        private String contentTypeName;

        private ContentPath parent;

        private PropertyTree data;

        private List<ContentAclEntry> aclEntries;

        public B name( String name )
        {
            this.name = name;
            return (B) this;
        }

        Builder()
        {
        }

        public Builder( Content content )
        {
            this.name = content.getName();
            this.displayName = content.getDisplayName();
            this.data = content.getData();
            this.parent = content.getParent();
            this.contentTypeName = content.getContentTypeName();
            this.aclEntries = content.getAclEntries();
        }

        public B displayName( String displayName )
        {
            this.displayName = displayName;
            return (B) this;
        }

        public B aclEntries( List<ContentAclEntry> aclEntries )
        {
            this.aclEntries = aclEntries;
            return (B) this;
        }

        public B parent( ContentPath contentPath )
        {
            this.parent = contentPath;
            return (B) this;
        }

        public B contentType( String contentTypeName )
        {
            this.contentTypeName = contentTypeName;
            return (B) this;
        }

        public B contentType( ContentTypeName contentTypeName )
        {
            this.contentTypeName = contentTypeName.toString();
            return (B) this;
        }

        public Builder data( final PropertyTree value )
        {
            this.data = value;
            return this;
        }

        public Content build()
        {
            return new Content( this );
        }
    }
}
