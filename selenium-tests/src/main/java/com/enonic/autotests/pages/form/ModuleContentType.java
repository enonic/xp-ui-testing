package com.enonic.autotests.pages.form;


public enum ModuleContentType
{
    DATE( ":date" ), DATE_TIME( ":datetime" ), DOUBLE( ":double" ), TIME( ":time" ), LONG( ":long" ), GEO_POINT( ":geopoint" ), CHECKBOX(
    ":checkbox" );

    private String name;

    public String getName()
    {
        return this.name;
    }

    private ModuleContentType( String name )
    {
        this.name = name;
    }
}
