package com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel;


public enum ContentInfoTerms
{
    TYPE( "Type" ), APPLICATION( "Application" ), OWNER( "Owner" ), CREATED( "Created" ), MODIFIED( "Modified" ), ID(
    "Id" ), PUBLISHED_FROM( "Publish From" ), FIRST_PUBLISHED( "First Published" ), PUBLISHED_TO( "Publish To" );


    private String value;

    private ContentInfoTerms( String value )
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }
}
