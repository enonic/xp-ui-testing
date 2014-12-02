package com.enonic.autotests.pages.form;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Page;
import com.enonic.wem.api.data.PropertyTree;

public class FormViewPanel
    extends Page
{
    public FormViewPanel( final TestSession session )
    {
        super( session );
    }

    public FormViewPanel type( final PropertyTree propertyTree )
    {

        // TODO: Type given data into form

        return this;
    }
}
