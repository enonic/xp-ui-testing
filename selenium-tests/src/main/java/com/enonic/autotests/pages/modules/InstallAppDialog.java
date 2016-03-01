package com.enonic.autotests.pages.modules;


import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.TextTransfer;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class InstallAppDialog
    extends Application
{
    public static final String HEADER = "Install Application";

    public static final String INSTALL_DIALOG_DIV = "//div[contains(@id,'InstallAppDialog')]";

    private final String HEADER_XPATH = INSTALL_DIALOG_DIV + "//div[contains(@id,'ModalDialogHeader')]//h2[@class='title']";

    private final String CANCEL_BUTTON = INSTALL_DIALOG_DIV + "//button[contains(@class,'cancel-button-top')]";

    private final String UPLOAD_TAB = INSTALL_DIALOG_DIV + "//li[contains(@id,'TabBarItem') and child::span[text()='Upload']]";

    private final String ENONIC_MARKET_TAB =
        INSTALL_DIALOG_DIV + "//li[contains(@id,'TabBarItem') and child::span[text()='Enonic Market']]";

    private final String APPLICATION_INPUT = INSTALL_DIALOG_DIV + "//div[contains(@id,'ApplicationInput')]/input";

    private final String APPLICATION_UPLOADER = INSTALL_DIALOG_DIV + "//div[contains(@id,'ApplicationUploaderEl')]";

    private final String VALIDATION_VIEWER = INSTALL_DIALOG_DIV + "//div[contains(@id,'ValidationRecordingViewer')]";

    private final String VALIDATION_VIEWER_TEXT = VALIDATION_VIEWER + "//li";

    @FindBy(xpath = CANCEL_BUTTON)
    private WebElement cancelButton;

    @FindBy(xpath = UPLOAD_TAB)
    private WebElement uploadTab;

    @FindBy(xpath = ENONIC_MARKET_TAB)
    private WebElement enonicMarketTab;

    @FindBy(xpath = APPLICATION_INPUT)
    private WebElement applicationURLInput;

    @FindBy(xpath = APPLICATION_UPLOADER)
    private WebElement applicationUploaderButton;

    private InstallAppDialog_MarketAppPanel marketAppPanel;

    public InstallAppDialog_MarketAppPanel getMarketAppPanel()
    {
        if ( marketAppPanel == null )
        {
            marketAppPanel = new InstallAppDialog_MarketAppPanel( getSession() );
        }
        return marketAppPanel;
    }

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

    public InstallAppDialog typeApplicationURL( String url )
    {
        clearAndType( applicationURLInput, url );
        sleep( 3000 );
        return this;
    }

    public String waitValidationViewerText( long timeout )
    {
        WebDriverWait wait = new WebDriverWait( getDriver(), timeout );
        WebElement element = wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( VALIDATION_VIEWER_TEXT ) ) );
        return element.getText();
    }

    public boolean waitUntilValidationViewerAppears( long timeout )
    {
        return waitUntilVisibleNoException( By.xpath( VALIDATION_VIEWER ), timeout );
    }

    public InstallAppDialog duUploadApplication( String pathToApp )
    {
        String absolutePath = null;
        URL resource = InstallAppDialog.class.getResource( pathToApp );
        try
        {
            absolutePath = Paths.get( resource.toURI() ).toString();
        }
        catch ( URISyntaxException e )
        {
            e.printStackTrace();
        }
        applicationUploaderButton.click();
        sleep( 300 );
        TextTransfer transfer = new TextTransfer();
        transfer.typePathToFileToSystemDialog( absolutePath );
        sleep( 3000 );
        return this;
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

    public InstallAppDialog_MarketAppPanel clickOnEnonicMarketTab()
    {
        enonicMarketTab.click();
        sleep( 500 );
        return getMarketAppPanel();
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
        return isElementDisplayed( INSTALL_DIALOG_DIV + GRID_CANVAS );
    }
}
