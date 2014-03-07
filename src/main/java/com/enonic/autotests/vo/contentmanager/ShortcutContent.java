package com.enonic.autotests.vo.contentmanager;

import com.enonic.wem.api.schema.content.ContentTypeName;


public class ShortcutContent
    extends BaseAbstractContent
{

    protected ShortcutContent( Builder<?> builder )
    {
        super( builder );
        setContentTypeName( ContentTypeName.shortcut().toString() );

    }

    public static abstract class Builder<T extends ShortcutContent>
        extends BaseAbstractContent.Builder<T>
    {

        public abstract T build();
    }

    public static Builder<?> builder()
    {
        return new Builder<ShortcutContent>()
        {
            @Override
            public ShortcutContent build()
            {
                return new ShortcutContent( this );
            }
        };
    }


}
