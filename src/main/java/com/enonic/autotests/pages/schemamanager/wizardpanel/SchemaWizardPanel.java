package com.enonic.autotests.pages.schemamanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;

public abstract class SchemaWizardPanel<T>
    extends WizardPanel<T>
{
    private final String CODE_AREA_CFG = "window.api.dom.ElementRegistry.getElementById('%s').setValue(arguments[0])";

    private final String CODE_AREA_XPATH = "//div[contains(@id,'api.ui.text.CodeArea')]";

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
        String id = getDriver().findElement( By.xpath( CODE_AREA_XPATH ) ).getAttribute( "id" );
        String js = String.format( CODE_AREA_CFG, id );
        ( (JavascriptExecutor) getSession().getDriver() ).executeScript( js, cfg );

    }

    public boolean waitForTextAreaLoaded()
    {
        return waitUntilVisibleNoException( By.xpath( CODE_AREA_XPATH ), Application.DEFAULT_IMPLICITLY_WAIT );
    }
}
