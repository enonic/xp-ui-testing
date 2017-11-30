package com.enonic.autotests.pages.contentmanager.browsepanel;


public enum ContentStatus
{
    NEW( "New" ), MODIFIED( "Modified" ), DELETED( "Deleted" ), OUT_OF_DATE( "Out-of-date" ), PUBLISHED_PENDING(
    "Published (Pending)" ), MODIFIED_PENDING( "Modified (Pending)" ), PENDING_DELETE_EXPIRED( "Pending delete (Expired)" ), PUBLISHED(
    "Published" ), UNPUBLISHED( "Unpublished" );

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
