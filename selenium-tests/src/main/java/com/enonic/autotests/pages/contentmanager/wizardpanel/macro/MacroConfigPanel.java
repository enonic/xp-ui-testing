package com.enonic.autotests.pages.contentmanager.wizardpanel.macro;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.xp.data.PropertyTree;

public abstract class MacroConfigPanel
    extends Application
{
    protected final String CONFIG_PANEL = MacroModalDialog.DIALOG_CONTAINER + "//div[@class='panel macro-config-panel']";

    public MacroConfigPanel( final TestSession session )
    {
        super( session );
    }

    public abstract void typeData( final PropertyTree data );

}
