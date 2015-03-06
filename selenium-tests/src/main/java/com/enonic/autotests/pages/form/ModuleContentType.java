package com.enonic.autotests.pages.form;


public enum ModuleContentType
{
    DATE( ":date" ), DATE_TIME( ":datetime" ), DOUBLE( ":double" ), TIME( ":time" ), LONG( ":long" ), GEO_POINT( ":geopoint" ), CHECKBOX(
    ":checkbox" ), TEXTLINE1_0( ":textline1_0" ), TEXTLINE1_1( ":textline1_1" ), TEXTLINE0_1( ":textline0_1" ), TEXTLINE2_5(
    ":textline2_5" );

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
