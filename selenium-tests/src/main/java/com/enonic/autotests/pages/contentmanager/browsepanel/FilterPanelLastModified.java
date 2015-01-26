package com.enonic.autotests.pages.contentmanager.browsepanel;

public enum FilterPanelLastModified
{

    HOUR( "1 hour" ), DAY( "1 day" ), WEEK( "1 week" );

    private String value;

    public String getValue()
    {
        return value;
    }

    private FilterPanelLastModified( String value )
    {
        this.value = value;
    }
}
