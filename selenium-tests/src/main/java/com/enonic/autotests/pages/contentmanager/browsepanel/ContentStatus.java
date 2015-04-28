package com.enonic.autotests.pages.contentmanager.browsepanel;


public enum ContentStatus
{
    NEW( "New" ), ONLINE( "Online" ), MODIFIED( "Modified" );

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
