package com.enonic.autotests.pages.schemamanager;

public enum SchemaKindUI
{
    MIXIN( "Mixin" ), CONTENT_TYPE( "Content Type" ), RELATIONSHIP_TYPE( "Relationship Type" );

    private String value;

    public String getValue()
    {
        return value;
    }

    private SchemaKindUI( String value )
    {
        this.value = value;
    }
}
