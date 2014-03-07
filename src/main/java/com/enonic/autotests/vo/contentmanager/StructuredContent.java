package com.enonic.autotests.vo.contentmanager;

import com.enonic.wem.api.schema.content.ContentTypeName;

public class StructuredContent
    extends BaseAbstractContent
{

    public StructuredContent( Builder<?> builder )
    {
        super( builder );
        setContentTypeName( ContentTypeName.structured().toString() );


    }

    public static abstract class Builder<T extends StructuredContent>
        extends BaseAbstractContent.Builder<T>
    {

        public abstract T build();
    }

    public static Builder<?> builder()
    {
        return new Builder<StructuredContent>()
        {
            @Override
            public StructuredContent build()
            {
                return new StructuredContent( this );
            }
        };
    }
}
