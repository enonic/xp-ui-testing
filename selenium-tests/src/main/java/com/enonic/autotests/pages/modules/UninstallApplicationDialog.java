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

    private final String HEADER = DIV_CONTAINER + "//div[contains(@id,'ModalDialogHeader']/h2";

    public static final String HEADER_TEXT = "Uninstall Applications";

    private final String YES_BUTTON = DIV_CONTAINER + "//button[contains(@id,'DialogButton')]/span[text()='Yes']";

    private final String NO_BUTTON = DIV_CONTAINER + "//button[contains(@id,'DialogButton')]/span[text()='No']";

    @FindBy(xpath = YES_BUTTON)
    private WebElement yesButton;

    @FindBy(xpath = NO_BUTTON)
    private WebElement noButton;

    public UninstallApplicationDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( HEADER );
    }

    public ApplicationBrowsePanel clickOnYesButton()
    {
        yesButton.click();
        waitUntilDialogClosed();
        return new ApplicationBrowsePanel( getSession() );
    }

    public void waitUntilDialogLoaded()
    {
        boolean isLoaded = waitUntilVisibleNoException( By.xpath( HEADER_TEXT ), Application.EXPLICIT_NORMAL );
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
}
