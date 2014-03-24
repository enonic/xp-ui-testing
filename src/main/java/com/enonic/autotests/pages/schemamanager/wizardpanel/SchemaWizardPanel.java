package com.enonic.autotests.pages.schemamanager.wizardpanel;

import org.openqa.selenium.JavascriptExecutor;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.vo.schemamanger.ContentType;

public abstract class SchemaWizardPanel
    extends WizardPanel
{

    public SchemaWizardPanel( TestSession session )
    {
        super( session );
    }


    public WizardPanel typeData( ContentType contentType )
    {
        // 1. type a data: 'name' and 'Display Name'.
        clearAndType( nameInput, contentType.getName() );
        //2. type the XML-config data:
        getLogger().info( "set contenttype configuration " );
        setConfiguration( contentType.getConfigData().trim() );
        return this;
    }


    private void setConfiguration( String cfg )
    {
        ( (JavascriptExecutor) getSession().getDriver() ).executeScript(
            "window.api.dom.ElementRegistry.getElementById('api.ui.CodeArea').setValue(arguments[0])", cfg );
    }

}
