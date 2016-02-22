package com.enonic.autotests.pages.modules;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class InstallAppDialog
    extends Application
{
    public static final String HEADER = "Install Application";

    private final String DIALOG_DIV = "//div[contains(@id,'InstallAppDialog')]";

    private final String HEADER_XPATH = DIALOG_DIV + "//div[contains(@id,'ModalDialogHeader')]//h2[@class='title']";

    private final String CANCEL_BUTTON = DIALOG_DIV + "//button[contains(@class,'cancel-button-top')]";

    private final String UPLOAD_TAB = DIALOG_DIV + "//li[contains(@id,'TabBarItem') and child::span[text()='Upload']]";

    private final String ENONIC_MARKET_TAB = DIALOG_DIV + "//li[contains(@id,'TabBarItem') and child::span[text()='Enonic Market']]";

    private final String APPLICATION_INPUT = DIALOG_DIV + "//div[contains(@id,'ApplicationInput')]/input";

    @FindBy(xpath = CANCEL_BUTTON)
    private WebElement cancelButton;

    @FindBy(xpath = UPLOAD_TAB)
    private WebElement uploadTab;

    @FindBy(xpath = ENONIC_MARKET_TAB)
    private WebElement enonicMarketTab;

    @FindBy(xpath = APPLICATION_INPUT)
    private WebElement applicationInput;

    /**
     * The Constructor
     *
     * @param session
     */
    public InstallAppDialog( final TestSession session )
    {
        super( session );
    }

    public String getHeader()
    {
        return getDisplayedString( HEADER_XPATH );
    }

    public void waitUntilDialogLoaded()
    {
        boolean isLoaded = waitUntilVisibleNoException( By.xpath( HEADER_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isLoaded )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_install-dialog" ) );
            throw new TestFrameworkException( "Install app dialog not loaded!" );
        }
    }

    public void waitUntilDialogClosed()
    {
        boolean isPresent = waitsElementNotVisible( By.xpath( HEADER_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isPresent )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_dialog-not-closed" ) );
            throw new TestFrameworkException( "Install app dialog not closed!" );
        }
    }

    public InstallAppDialog clickOnEnonicMarketTab()
    {
        enonicMarketTab.click();
        sleep( 500 );
        return this;
    }

    public InstallAppDialog clickOnCancelButton()
    {
        cancelButton.click();
        return this;
    }

    public boolean isUploadTabActivated()
    {
        return waitAndCheckAttrValue( uploadTab, "class", "active", Application.EXPLICIT_NORMAL );
    }

    public boolean isEnonicMarketTabActivated()
    {
        return waitAndCheckAttrValue( enonicMarketTab, "class", "active", Application.EXPLICIT_NORMAL );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( HEADER_XPATH );
    }

    public boolean isUploadTabPresent()
    {
        return isElementDisplayed( UPLOAD_TAB );
    }

    public boolean isEnonicMarketTabPresent()
    {
        return isElementDisplayed( ENONIC_MARKET_TAB );
    }

    public InstallAppDialog typePathToApplication( String path )
    {
        clearAndType( applicationInput, path );
        return this;
    }

    public boolean isApplicationInputDisplayed( String path )
    {
        return isElementDisplayed( APPLICATION_INPUT );
    }

    public boolean isCancelButtonDisplayed( String path )
    {
        return isElementDisplayed( CANCEL_BUTTON );
    }

    public boolean isEnonicMarketTableDisplayed()
    {
        return isElementDisplayed( DIALOG_DIV + GRID_CANVAS );
    }
}