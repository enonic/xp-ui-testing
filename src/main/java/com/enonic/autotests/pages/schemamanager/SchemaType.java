package com.enonic.autotests.pages.schemamanager;

public enum SchemaType
{
    MIXIN( "Mixin" ), CONTENT_TYPE( "Content Type" ), RELATIONSHIP_TYPE( "Relationship Type" );

    private String value;

    public String getValue()
    {
        return value;
    }

    private SchemaType( String value )
    {
        this.value = value;
    }
}
