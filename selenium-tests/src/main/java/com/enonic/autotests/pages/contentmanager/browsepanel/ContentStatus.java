package com.enonic.autotests.pages.contentmanager.browsepanel;


public enum ContentStatus
{
    OFFLINE( "Offline" ), ONLINE( "Online" ), MODIFIED( "Modified" ), PENDING_DELETE( "Pending delete" ), OUT_OF_DATE( "Out-of-date" );

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
