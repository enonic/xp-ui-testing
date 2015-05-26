package com.enonic.autotests.vo.contentmanager;


public enum WorkspaceName
{
    DRAFT( "draft" ), MASTER( "master" );

    private String value;

    private WorkspaceName( String value )
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
