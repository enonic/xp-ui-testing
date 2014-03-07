package com.enonic.autotests.vo.contentmanager;

import com.enonic.wem.api.schema.content.ContentTypeName;


public class UnstructuredContent
    extends BaseAbstractContent
{

    protected UnstructuredContent( Builder<?> builder )
    {
        super( builder );
        setContentTypeName( ContentTypeName.unstructured().toString() );

    }

    public static abstract class Builder<T extends UnstructuredContent>
        extends BaseAbstractContent.Builder<T>
    {
        public abstract T build();

    }

    public static Builder<?> builder()
    {
        return new Builder<UnstructuredContent>()
        {
            @Override
            public UnstructuredContent build()
            {
                return new UnstructuredContent( this );
            }
        };
    }
}
