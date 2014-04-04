package com.enonic.autotests.vo.schemamanger;

import com.enonic.autotests.pages.schemamanager.SchemaKindUI;


public class Mixin
    extends Schema
{

    /**
     * Constructor
     *
     * @param builder
     */
    private Mixin( Builder builder )
    {
        super( builder );
        this.schemaKindUI = SchemaKindUI.MIXIN;
    }

    public static Builder newMixin()
    {
        return new Builder();
    }

    public static Builder newMixin( final Mixin mixin )
    {
        return new Builder( mixin );
    }

    /**
     * Builder for {@link Mixin}}
     */
    public static class Builder
        extends Schema.Builder<Builder>
    {
        private Builder()
        {
            super();

        }

        private Builder( final Mixin source )
        {
            super( source );
        }

        public Mixin build()
        {
            return new Mixin( this );
        }
    }

}
