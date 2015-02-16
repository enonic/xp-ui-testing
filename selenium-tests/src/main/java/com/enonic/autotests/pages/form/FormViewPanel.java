package com.enonic.autotests.pages.form;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Page;
import com.enonic.xp.data.PropertyTree;

public abstract class FormViewPanel
    extends Page
{
    public FormViewPanel( final TestSession session )
    {
        super( session );
    }

    public abstract FormViewPanel type( final PropertyTree data );

}
