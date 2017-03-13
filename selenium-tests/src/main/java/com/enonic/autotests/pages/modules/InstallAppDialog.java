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
import com.enonic.autotests.utils.TextTransfer;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 *
 */
public class InstallAppDialog
    extends Application
{
    public static final String HEADER = "Install Application";

    public static final String INSTALL_DIALOG_DIV = "//div[contains(@id,'InstallAppDialog')]";

    private final String HEADER_XPATH = INSTALL_DIALOG_DIV + "//div[contains(@id,'ModalDialogHeader')]//h2[@class='title']";

    private final String CANCEL_BUTTON = INSTALL_DIALOG_DIV + "//div[contains(@class,'cancel-button-top')]";

    private final String APPLICATION_INPUT = INSTALL_DIALOG_DIV + "//div[contains(@id,'ApplicationInput')]/input";

    private final String APPLICATION_UPLOADER = INSTALL_DIALOG_DIV + "//div[contains(@id,'ApplicationUploaderEl')]";


    private final String VALIDATION_MESSAGE = INSTALL_DIALOG_DIV +
        "//div[contains(@id,'ModalDialogContentPanel')]//div[contains(@class,'status-message') and contains(@class,'failed')]";

    private final String APP_VIEWER_DIV = INSTALL_DIALOG_DIV + "//div[contains(@id,'MarketAppViewer')]";

    @FindBy(xpath = CANCEL_BUTTON)
    private WebElement cancelButton;

    @FindBy(xpath = APPLICATION_INPUT)
    private WebElement applicationInput;

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

    public InstallAppDialog typeInApplicationInput( String url )
    {
        clearAndType( applicationInput, url );
        sleep( 2000 );
        return this;
    }

    public String getValidationMessage( long timeout )
    {
        WebDriverWait wait = new WebDriverWait( getDriver(), timeout );
        WebElement element = wait.until( ExpectedConditions.visibilityOfElementLocated( By.xpath( VALIDATION_MESSAGE ) ) );
        return element.getText();
    }

    public boolean waitUntilValidationMessageAppears( long timeout )
    {
        return waitUntilVisibleNoException( By.xpath( VALIDATION_MESSAGE ), timeout );
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

    public boolean isAppUploaderButtonPresent()
    {
        return isElementDisplayed( APPLICATION_UPLOADER );
    }

    public void waitUntilDialogLoaded()
    {
        boolean isLoaded = waitUntilVisibleNoException( By.xpath( HEADER_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isLoaded )
        {
            saveScreenshot( NameHelper.uniqueName( "err_install-dialog" ) );
            throw new TestFrameworkException( "Install app dialog not loaded!" );
        }
        waitInvisibilityOfSpinner( 9 );
        waitUntilVisible( By.xpath( APP_VIEWER_DIV ) );
    }

    public void waitUntilDialogClosed()
    {
        boolean isPresent = waitsElementNotVisible( By.xpath( HEADER_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isPresent )
        {
            saveScreenshot( NameHelper.uniqueName( "err_dialog-not-closed" ) );
            throw new TestFrameworkException( "Install app dialog not closed!" );
        }
    }


    public InstallAppDialog clickOnCancelButton()
    {
        cancelButton.click();
        sleep( 400 );
        return this;
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( HEADER_XPATH );
    }


    public boolean isEnonicMarketPanelPresent()
    {
        return getMarketAppPanel().isDisplayed();
    }


    public boolean isApplicationInputDisplayed( String path )
    {
        return isElementDisplayed( APPLICATION_INPUT );
    }

    public boolean isCancelButtonDisplayed( String path )
    {
        return isElementDisplayed( CANCEL_BUTTON );
    }

    public Long countDisplayedApplications()
    {
        return getNumberOfElements( By.xpath( APP_VIEWER_DIV + H6_DISPLAY_NAME ) );
    }
}
