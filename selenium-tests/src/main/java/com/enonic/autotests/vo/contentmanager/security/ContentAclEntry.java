package com.enonic.autotests.vo.contentmanager.security;


import java.util.ArrayList;
import java.util.List;

public class ContentAclEntry
{
    private String principalName;

    private List<String> permissions = new ArrayList<>();

    public List<String> getPermissions()
    {
        return this.permissions;
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
}
