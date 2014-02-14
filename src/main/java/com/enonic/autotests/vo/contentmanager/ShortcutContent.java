package com.enonic.autotests.vo.contentmanager;

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog.ContentTypeName;


public class ShortcutContent
    extends BaseAbstractContent
{

    protected ShortcutContent( Builder<?> builder )
    {
        super( builder );
        setContentTypeName( ContentTypeName.SHORTCUT.getValue() );

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
