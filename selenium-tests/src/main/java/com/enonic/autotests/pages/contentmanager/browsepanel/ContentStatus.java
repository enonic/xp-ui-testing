package com.enonic.autotests.pages.contentmanager.browsepanel;


public enum ContentStatus
{
    NEW( "New" ), ONLINE( "Online" );

    private String value;

    private ContentStatus( String value )
    {
        this.value = value;

    }

    public String getValue()
    {
        return value;
    }

}
