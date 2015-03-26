package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.WizardStepForm;

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

    public EditPermissionsDialog clickOnEditPermissionsButton()
    {
        //findElements( By.xpath( CONTAINER_XPATH + EDIT_PERMISSION_BUTTON ) ).get( 0 ).click();
        Actions builder = new Actions( getDriver() );
        builder.click( findElement( By.xpath( EDIT_PERMISSION_BUTTON ) ) ).build().perform();
        // editPermissionsButton.click();
        sleep( 1000 );
        EditPermissionsDialog modalDialog = new EditPermissionsDialog( getSession() );
        modalDialog.waitForOpened();
        return modalDialog;
    }
}
