package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardStepForm;
import com.enonic.autotests.utils.WaitHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class SecurityWizardStepForm
    extends WizardStepForm
{
    private final String CONTAINER_XPATH = "//div[contains(@id,'SecurityWizardStepForm')]";

    private final String EDIT_PERMISSION_BUTTON =
        "//button[contains(@class,'edit-permissions') and child::span[text()='Edit Permissions']]";


    @FindBy(xpath = CONTAINER_XPATH + EDIT_PERMISSION_BUTTON)
    WebElement editPermissionsButton;


    public SecurityWizardStepForm( final TestSession session )
    {
        super( session );
        waitUntilVisible( By.xpath( CONTAINER_XPATH ) );
    }

    public SecurityWizardStepForm waitUntilButtonEditPermissionsClickable()
    {
        boolean result = WaitHelper.waitUntilClickableNoException( getDriver(), By.xpath( CONTAINER_XPATH + EDIT_PERMISSION_BUTTON ),
                                                                   Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            throw new TestFrameworkException( "Button 'Edit Permissions' not clickable!" );
        }
        return this;
    }

    public EditPermissionsDialog clickOnEditPermissionsButton()
    {
        findElements( By.xpath( CONTAINER_XPATH + EDIT_PERMISSION_BUTTON ) ).get( 0 ).click();
        // Actions builder = new Actions( getDriver() );
        //builder.click( findElement( By.xpath( CONTAINER_XPATH+ EDIT_PERMISSION_BUTTON ) ) ).build().perform();
        // editPermissionsButton.click();
        //click();
        sleep( 1000 );
        EditPermissionsDialog modalDialog = new EditPermissionsDialog( getSession() );
        modalDialog.waitForOpened();
        return modalDialog;
    }

    private void click()
    {
        JavascriptExecutor executor = (JavascriptExecutor) getSession().getDriver();
        String id = editPermissionsButton.getAttribute( "id" );
        String script = String.format( "window.api.dom.ElementRegistry.getElementById('%s').getHTMLElement().click()", id );
        executor.executeScript( script );
    }
}
