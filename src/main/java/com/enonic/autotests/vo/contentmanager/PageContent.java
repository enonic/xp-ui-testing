package com.enonic.autotests.vo.contentmanager;

import com.enonic.wem.api.schema.content.ContentTypeName;


public class PageContent
    extends BaseAbstractContent
{

    public PageContent( Builder<?> builder )
    {
        super( builder );
        setContentTypeName( ContentTypeName.page().toString() );
    }

    public static abstract class Builder<T extends PageContent>
        extends BaseAbstractContent.Builder<T>
    {

        public abstract T build();
    }

    public static Builder<?> builder()
    {
        return new Builder<PageContent>()
        {
            @Override
            public PageContent build()
            {
                return new PageContent( this );
            }
        };
    }

}
