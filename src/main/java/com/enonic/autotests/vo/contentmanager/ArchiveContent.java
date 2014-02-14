package com.enonic.autotests.vo.contentmanager;

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog.ContentTypeName;


public class ArchiveContent
    extends BaseAbstractContent
{

    protected ArchiveContent( Builder<?> builder )
    {
        super( builder );
        setContentTypeName( ContentTypeName.ARCHIVE.getValue() );
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
