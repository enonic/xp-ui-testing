package com.enonic.autotests.vo.contentmanager;

import com.enonic.wem.api.schema.content.ContentTypeName;


public class MediaContent
    extends BaseAbstractContent
{

    protected MediaContent( Builder<?> builder )
    {
        super( builder );
        setContentTypeName( ContentTypeName.media().toString() );

    }

    public static abstract class Builder<T extends MediaContent>
        extends BaseAbstractContent.Builder<T>
    {

        public abstract T build();
    }

    public static Builder<?> builder()
    {
        return new Builder<MediaContent>()
        {
            @Override
            public MediaContent build()
            {
                return new MediaContent( this );
            }
        };
    }

}
