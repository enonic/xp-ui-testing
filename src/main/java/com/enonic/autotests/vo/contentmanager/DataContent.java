package com.enonic.autotests.vo.contentmanager;

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog.ContentTypeName;


public class DataContent
    extends BaseAbstractContent
{

    protected DataContent( Builder<?> builder )
    {
        super( builder );
        setContentTypeName( ContentTypeName.DATA.getValue() );
    }

    public static abstract class Builder<T extends DataContent>
        extends BaseAbstractContent.Builder<T>
    {

        public abstract T build();
    }

    public static Builder<?> builder()
    {
        return new Builder<DataContent>()
        {
            @Override
            public DataContent build()
            {
                return new DataContent( this );
            }
        };
    }

}