package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.WizardStepForm;

public class SecurityWizardStepForm
    extends WizardStepForm
{
    private final String CONTAINER_XPATH = "//div[contains(@id,'SecurityWizardStepForm')]";

    private final String EDIT_PERMISSION_BUTTON =
        "//button[contains(@id,'api.ui.button.Button') and child::span[text()='Edit Permissions']]";


    @FindBy(xpath = CONTAINER_XPATH + EDIT_PERMISSION_BUTTON)
    WebElement editPermissionsButton;


    public SecurityWizardStepForm( final TestSession session )
    {
        super( session );
        waitUntilVisible( By.xpath( CONTAINER_XPATH ) );
    }

    public EditPermissionsDialog clickOnEditPermissionsButton()
    {
        findElements( By.xpath( CONTAINER_XPATH + EDIT_PERMISSION_BUTTON ) ).get( 0 ).click();
        EditPermissionsDialog modalDialog = new EditPermissionsDialog( getSession() );
        modalDialog.waitForOpened();
        return modalDialog;
    }
}
