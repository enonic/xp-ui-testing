package com.enonic.autotests.pages.contentmanager.browsepanel;


public enum ContentStatus
{
    OFFLINE( "Offline" ), ONLINE( "Online" ), MODIFIED( "Modified" ), PENDING_DELETE( "Pending delete" );

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
