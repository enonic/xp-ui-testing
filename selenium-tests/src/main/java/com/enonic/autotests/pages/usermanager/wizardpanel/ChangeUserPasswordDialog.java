package com.enonic.autotests.pages.usermanager.wizardpanel;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ChangeUserPasswordDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'ChangeUserPasswordDialog')]";

    private final String PASSWORD_INPUT = "//input[contains(@id,'PasswordInput')]";

    private final String CHANGE_PASSWORD_BUTTON = "//button[contains(@id,'DialogButton')]/span[text()='Change Password']";

    private final String CANCEL_BUTTON = "//button[contains(@id,'DialogButton')]/span[text()='Cancel']";

    @FindBy(xpath = DIALOG_CONTAINER + PASSWORD_INPUT)
    WebElement passwordInput;

    @FindBy(xpath = CHANGE_PASSWORD_BUTTON)
    WebElement changeButton;

    @FindBy(xpath = DIALOG_CONTAINER + CANCEL_BUTTON)
    WebElement cancelButton;


    public ChangeUserPasswordDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return findElements( By.xpath( DIALOG_CONTAINER ) ).size() > 0;
    }

    public ChangeUserPasswordDialog waitForLoaded( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout ) )
        {
            throw new TestFrameworkException( "SortContentDialog was not showed!" );
        }
        return this;
    }

    public UserWizardPanel doChangePassword( String newPassword )
    {
        passwordInput.sendKeys( newPassword );
        sleep( 500 );
        changeButton.click();
        return new UserWizardPanel( getSession() );
    }

    public boolean isChangeButtonDisplayed()
    {
        return changeButton.isDisplayed();
    }

    public boolean isCancelButtonDisplayed()
    {
        return cancelButton.isDisplayed();
    }

    public boolean isChangeButtonEnabled()
    {
        return changeButton.isEnabled();
    }

}
