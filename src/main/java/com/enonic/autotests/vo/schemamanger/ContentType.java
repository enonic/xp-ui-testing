package com.enonic.autotests.vo.schemamanger;

import com.enonic.autotests.pages.schemamanager.SchemaKindUI;


/**
 * Model of ContentType.
 */
public class ContentType
    extends Schema
{

    /**
     * The constructor.
     *
     * @param builder
     */
    private ContentType( Builder builder )
    {
        super( builder );
        this.schemaKindUI = SchemaKindUI.CONTENT_TYPE;
    }

    public static Builder newContentType()
    {
        return new Builder();
    }

    public static Builder newContentType( final ContentType contentType )
    {
        return new Builder( contentType );
    }

    /**
     * Builder for {@link ContentType }
     */
    public static class Builder
        extends Schema.Builder<Builder>
    {
        /**
         * Constructor
         */
        private Builder()
        {
            super();

        }

        private Builder( final ContentType source )
        {
            super( source );
        }

        public ContentType build()
        {
            return new ContentType( this );
        }
    }

}
