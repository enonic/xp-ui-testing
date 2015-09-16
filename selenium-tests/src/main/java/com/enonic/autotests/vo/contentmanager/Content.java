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

    private final ContentSettings contentSettings;

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
        this.contentSettings = builder.contentSettings;
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

    public ContentSettings getContentSettings()
    {
        return contentSettings;
    }

    public List<ContentAclEntry> getAclEntries()
    {
        return this.aclEntries;
    }

    public PropertyTree getData()
    {
        return data;
    }

    public static class Builder<BUILDER extends Builder, C extends Content>
    {
        private String name;

        private String displayName;

        private String contentTypeName;

        private ContentPath parent;

        private PropertyTree data;

        private ContentSettings contentSettings;

        private List<ContentAclEntry> aclEntries;

        public Builder<BUILDER, C> name( final String name )
        {
            this.name = name;
            return this;
        }

        public Builder<BUILDER, C> settings( final ContentSettings settings )
        {
            this.contentSettings = settings;
            return this;
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
            this.contentSettings = content.getContentSettings();
        }

        public Builder<BUILDER, C> displayName( String displayName )
        {
            this.displayName = displayName;
            return this;
        }

        public Builder<BUILDER, C> aclEntries( List<ContentAclEntry> aclEntries )
        {
            this.aclEntries = aclEntries;
            return this;
        }

        public Builder<BUILDER, C> parent( ContentPath contentPath )
        {
            this.parent = contentPath;
            return this;
        }

        public Builder<BUILDER, C> contentType( String contentTypeName )
        {
            this.contentTypeName = contentTypeName;
            return this;
        }

        public Builder<BUILDER, C> contentType( ContentTypeName contentTypeName )
        {
            this.contentTypeName = contentTypeName.toString();
            return this;
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
