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

    private final String CHANGE_PASSWORD_BUTTON = "//button[contains(@id,'DialogButton') and child::span[text()='Change Password']]";

    private final String CANCEL_BUTTON = "//button[contains(@id,'DialogButton')]/span[text()='Cancel']";

    private final String SHOW_PASSWORD_LINK = DIALOG_CONTAINER + "//a[contains(@class,'show-link')]";

    private final String GENERATE_LINK = DIALOG_CONTAINER + "//a[text()='Generate']";


    @FindBy(xpath = DIALOG_CONTAINER + PASSWORD_INPUT)
    WebElement passwordInput;

    @FindBy(xpath = SHOW_PASSWORD_LINK)
    WebElement showPasswordLink;

    @FindBy(xpath = GENERATE_LINK)
    WebElement generatePasswordLink;

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

    public ChangeUserPasswordDialog typePassword( String newPassword )
    {
        passwordInput.sendKeys( newPassword );
        sleep( 300 );
        return this;
    }

    public boolean isPasswordInputDisplayed()
    {
        return passwordInput.isDisplayed();
    }

    public String getPasswordInputValue()
    {
        return passwordInput.getAttribute( "value" );
    }

    public boolean isShowPasswordLinkDisplayed()
    {
        return showPasswordLink.isDisplayed();
    }

    public ChangeUserPasswordDialog clickOnShowLink()
    {
        showPasswordLink.click();
        sleep( 300 );
        return this;
    }

    public ChangeUserPasswordDialog clickOnGenerateLink()
    {
        generatePasswordLink.click();
        sleep( 300 );
        return this;
    }

    public boolean isHideTextDisplayed()
    {
        return waitAndCheckAttrValue( showPasswordLink, "data-i18n", "Hide", 1 );
    }

    public boolean isShowTextDisplayed()
    {
        return waitAndCheckAttrValue( showPasswordLink, "data-i18n", "Show", 1 );
    }

    public boolean isGenerateLinkDisplayed()
    {
        return generatePasswordLink.isDisplayed();
    }

    public UserWizardPanel doChangePassword( String newPassword )
    {
        typePassword( newPassword );
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

    public boolean isChangeButtonDisabled()
    {
        return isAttributePresent( changeButton, "disabled", 1 );
    }

}
