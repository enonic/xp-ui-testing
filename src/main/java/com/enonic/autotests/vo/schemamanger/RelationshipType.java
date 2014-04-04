package com.enonic.autotests.vo.schemamanger;

import com.enonic.autotests.pages.schemamanager.SchemaKindUI;


public class RelationshipType
    extends Schema
{

    /**
     * Constructor.
     *
     * @param builder
     */
    private RelationshipType( Builder builder )
    {
        super( builder );
        this.schemaKindUI = SchemaKindUI.RELATIONSHIP_TYPE;
    }

    /**
     * @return {@link Builder} instance.
     */
    public static Builder newRelationshipType()
    {
        return new Builder();
    }

    public static Builder newRelationshipType( final RelationshipType relationshipType )
    {
        return new Builder( relationshipType );
    }


    /**
     * Builder for {@link RelationshipType}
     */
    public static class Builder
        extends Schema.Builder<Builder>
    {
        private Builder()
        {
            super();

        }

        private Builder( final RelationshipType source )
        {
            super( source );
        }

        public RelationshipType build()
        {
            return new RelationshipType( this );
        }
    }
}
