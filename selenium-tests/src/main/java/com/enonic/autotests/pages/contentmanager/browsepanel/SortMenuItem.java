package com.enonic.autotests.pages.contentmanager.browsepanel;


public enum SortMenuItem
{
    DNAME_ASCENDING( "DisplayName - Ascending" ), DNAME_DESCENDING( "DisplayName - Descending" ), MODIFIED_ASCENDING(
    "Modified - Ascending" ), MODIFIED_DESCENDING( "Modified - Descending (default)" ), MANUALLY_SORTED( "Manually Sorted" );

    private String value;

    public String getValue()
    {
        return value;
    }

    private SortMenuItem( String value )
    {
        this.value = value;
    }
}
