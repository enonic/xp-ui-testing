package com.enonic.autotests.vo.contentmanager.security;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContentAclEntry
{
    private PermissionSuite suite;

    private String principalName;

    private List<String> permissions = new ArrayList<>();

    public List<String> getPermissions()
    {
        return this.permissions;
    }

    public void setPermissionSuite( PermissionSuite suite )
    {
        this.suite = suite;
    }

    public PermissionSuite getPermissionSuite()
    {
        return this.suite;
    }

    public void setPermissions( final List<String> permissions )
    {
        this.permissions = permissions;
    }

    public String getPrincipalName()
    {
        return principalName;
    }

    public void setPrincipalName( String principalName )
    {
        this.principalName = principalName;
    }

    private ContentAclEntry( final Builder builder )
    {
        this.suite = builder.suite;
        this.permissions = builder.permissions;
        this.principalName = builder.principalName;
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( !( obj instanceof ContentAclEntry ) )
        {
            return false;
        }

        final ContentAclEntry other = (ContentAclEntry) obj;

        return Objects.equals( suite, other.suite ) &&
            Objects.equals( principalName, other.principalName ) &&
            Objects.equals( permissions, other.permissions );

    }

    public static class Builder
    {
        private PermissionSuite suite;

        private String principalName;

        private List<String> permissions;

        Builder()
        {
        }

        public Builder( final ContentAclEntry aclEntry )
        {
            this.principalName = aclEntry.getPrincipalName();
            this.permissions = aclEntry.getPermissions();
            this.suite = aclEntry.getPermissionSuite();
        }

        public Builder principalName( final String principalName )
        {
            this.principalName = principalName;
            return this;
        }


        public Builder suite( final PermissionSuite suite )
        {
            this.suite = suite;
            return this;
        }

        public Builder permissions( final List<String> permissions )
        {
            this.permissions = permissions;
            return this;
        }

        public ContentAclEntry build()
        {
            return new ContentAclEntry( this );
        }
    }

    public static Builder builder()
    {
        return new Builder();
    }


}
