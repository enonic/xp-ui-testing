package com.enonic.autotests.pages.modules;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

public class UninstallApplicationDialog
    extends Application
{
    private final String DIV_CONTAINER = "//div[contains(@id,'UninstallApplicationDialog')]";

    private final String HEADER_XPATH = DIV_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]/h2";

    public static final String HEADER_TEXT = "Uninstall Applications";

    public static final String CONTENT_TEXT = "Are you sure you want to uninstall selected application(s)?";

    private final String YES_BUTTON = DIV_CONTAINER + "//button[contains(@id,'DialogButton')]/span[text()='Yes']";

    private final String NO_BUTTON = DIV_CONTAINER + "//button[contains(@id,'DialogButton')]/span[text()='No']";

    private final String CANCEL_BUTTON = DIV_CONTAINER + "//button[contains(@class,'cancel-button-top')]";

    private final String DIALOG_CONTENT = DIV_CONTAINER + "//div[contains(@id,'ModalDialogContentPanel')]/h6";

    @FindBy(xpath = YES_BUTTON)
    private WebElement yesButton;

    @FindBy(xpath = NO_BUTTON)
    private WebElement noButton;

    @FindBy(xpath = CANCEL_BUTTON)
    private WebElement cancelButton;

    public UninstallApplicationDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( HEADER_XPATH );
    }

    public ApplicationBrowsePanel clickOnYesButton()
    {
        yesButton.click();
        waitUntilDialogClosed();
        return new ApplicationBrowsePanel( getSession() );
    }

    public String getContentString()
    {
        return getDisplayedString( DIALOG_CONTENT );
    }

    public void waitUntilDialogLoaded()
    {
        boolean isLoaded = waitUntilVisibleNoException( By.xpath( HEADER_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isLoaded )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_confirm-uninstall" ) );
            throw new TestFrameworkException( "UninstallApplicationDialog was dialog not loaded!" );
        }
    }

    public void waitUntilDialogClosed()
    {
        boolean isPresent = waitsElementNotVisible( By.xpath( HEADER_TEXT ), Application.EXPLICIT_NORMAL );
        if ( !isPresent )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_confirm-uninstall-not-closed" ) );
            throw new TestFrameworkException( "UninstallApplicationDialog dialog not closed!" );
        }
    }

    public String getHeader()
    {
        return getDisplayedString( HEADER_XPATH );
    }

    public boolean isYesButtonDisplayed()
    {
        return yesButton.isDisplayed();
    }

    public boolean isNoButtonDisplayed()
    {
        return noButton.isDisplayed();
    }

    public boolean isCancelButtonDisplayed()
    {
        return cancelButton.isDisplayed();
    }

    public void clickOnCancelButton()
    {
        cancelButton.click();
        waitUntilDialogClosed();
    }

    public void clickOnNoButton()
    {
        noButton.click();
        waitUntilDialogClosed();
    }

}
