package com.enonic.autotests.pages.contentmanager.wizardpanel;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.WizardStepForm;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry;

import static com.enonic.autotests.utils.SleepHelper.sleep;

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

    public SecurityWizardStepForm editPermissions( List<ContentAclEntry> aclEntries )
    {
        sleep( 1000 );
        TestUtils.saveScreenshot( getSession(), "editPermButton" );
        editPermissionsButton.click();
        EditPermissionsDialog dialog = new EditPermissionsDialog( getSession() );
        dialog.waitForOpened().clickOnInheritCheckbox().updatePermissions( aclEntries );
        return this;
    }
}
