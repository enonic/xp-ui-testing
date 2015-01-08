package com.enonic.autotests.pages.form;


import com.enonic.autotests.TestSession;
import com.enonic.wem.api.data.PropertyTree;

public class PageTemplateFormViewPanel
    extends FormViewPanel
{
    public PageTemplateFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        return null;
    }
}
