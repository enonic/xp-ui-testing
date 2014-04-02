package com.enonic.autotests.pages.schemamanager.wizardpanel;

import org.openqa.selenium.JavascriptExecutor;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.WizardPanel;

public abstract class SchemaWizardPanel<T>
    extends WizardPanel<T>
{

    /**
     * The constructor.
     *
     * @param session
     */
    public SchemaWizardPanel( TestSession session )
    {
        super( session );
    }


    /**
     * Find by ID the configuration text area and set value to CodeMirror java-script component.
     *
     * @param cfg content type configuration string.
     */
    public void setConfiguration( String cfg )
    {
        ( (JavascriptExecutor) getSession().getDriver() ).executeScript(
            "window.api.dom.ElementRegistry.getElementById('api.ui.CodeArea').setValue(arguments[0])", cfg );
    }

}
