package com.enonic.autotests.pages.contentmanager.browsepanel;


public enum ContentInfoTerms
{
    TYPE( "Type" ), APPLICATION( "Application" ), OWNER( "Owner" ), CREATED( "Created" ), MODIFIED( "Modified" ), ID( "Id" );


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
