package com.enonic.autotests.pages.form;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.xp.data.PropertyTree;

public abstract class FormViewPanel
    extends Application
{
    protected static final String FORM_VIEW = "//div[contains(@id,'api.form.FormView')]";

    protected static String VALUE_INPUT =
        "//div[contains(@id,'api.form.InputView') and descendant::div[@title='%s']]//input[contains(@id,'TextInput')]";

    public FormViewPanel( final TestSession session )
    {
        super( session );
    }

    public abstract FormViewPanel type( final PropertyTree data );

}
