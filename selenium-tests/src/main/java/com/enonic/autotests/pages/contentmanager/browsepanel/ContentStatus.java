package com.enonic.autotests.pages.contentmanager.browsepanel;


public enum ContentStatus
{
    NEW( "New" ), MODIFIED( "Modified" ), MARKED_FOR_DELETION( "Marked for deletion" ), OUT_OF_DATE( "Out-of-date" ), PUBLISHED_SCHEDULED(
    "Publishing Scheduled" ), MODIFIED_PENDING( "Modified (Pending)" ), PENDING_DELETE_EXPIRED( "Pending delete (Expired)" ), PUBLISHED(
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
