package com.enonic.autotests.vo.contentmanager;

import com.enonic.wem.api.schema.content.ContentTypeName;


public class ArchiveContent
    extends BaseAbstractContent
{

    protected ArchiveContent( Builder<?> builder )
    {
        super( builder );
        setContentTypeName( ContentTypeName.archiveMedia().toString() );
    }

    public static abstract class Builder<T extends ArchiveContent>
        extends BaseAbstractContent.Builder<T>
    {

        public abstract T build();
    }

    public static Builder<?> builder()
    {
        return new Builder<ArchiveContent>()
        {
            @Override
            public ArchiveContent build()
            {
                return new ArchiveContent( this );
            }
        };
    }

}
