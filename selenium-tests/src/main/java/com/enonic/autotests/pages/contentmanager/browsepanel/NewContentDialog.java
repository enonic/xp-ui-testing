package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.google.common.io.Files;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TextTransfer;
import com.enonic.xp.schema.content.ContentTypeName;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class NewContentDialog
    extends Application
{
    public static final String CONTAINER = "//div[contains(@id,'NewContentDialog')]";

    public static final String ALL_LIST_ITEMS =
        "//div[contains(@id,'NewContentDialog') and not(contains(@class,'mock-modal-dialog'))]//ul[@class='content-types-list']//li[contains(@class,'content-types-list-item')]";

    public static final String LIST_ITEMS_SITES = "//div[contains(@id,'NewContentDialog')]//ul/li[@class='content-types-list-item site']";

    public static final String SEARCH_INPUT = "//div[contains(@id,'api.ui.text.FileInput')]/input";

    private final static String DIALOG_TITLE_XPATH =
        "//div[contains(@class,'modal-dialog')]/div[contains(@class,'dialog-header') and contains(.,'Create Content')]";

    public static String CONTENT_TYPE_NAME =
        CONTAINER + "//li[contains(@class,'content-types-list-item') and descendant::p[@class='sub-name' and text()='%s']]";

    private final String AVAILABLE_CONTENT_TYPES = CONTAINER + "//ul[contains(@id,'FilterableItemsList')]" + "//li" + P_NAME;

    private final String UPLOAD_FILE_BUTTON = CONTAINER + "//div[@class='upload-button']";

    private final String MOST_POPULAR_BLOCK = CONTAINER + "//div[contains(@id,'MostPopularItemsBlock')]";

    private final String MOST_POPULAR_ITEM_LIST = MOST_POPULAR_BLOCK + "//ul[contains(@id,'MostPopularItemsList')]";

    private final String MOST_POPULAR_ITEM_NAME = MOST_POPULAR_ITEM_LIST + "//li" + H6_DISPLAY_NAME;

    @FindBy(xpath = UPLOAD_FILE_BUTTON)
    private WebElement uploadButton;

    @FindBy(xpath = SEARCH_INPUT)
    private WebElement searchInput;

    /**
     * The constructor.
     *
     * @param session {@link TestSession}   instance.
     */
    public NewContentDialog( TestSession session )
    {
        super( session );
    }

    public boolean isMostPopularBlockDisplayed()
    {
        return isElementDisplayed( MOST_POPULAR_BLOCK );
    }

    public List<String> getContentTypesNames()
    {
        return getDisplayedStrings( By.xpath( AVAILABLE_CONTENT_TYPES ) );
    }

    public List<String> getMostPopularItemsNames()
    {
        return getDisplayedStrings( By.xpath( MOST_POPULAR_ITEM_NAME ) );
    }

    public static File createFileInTmp( String resName, final String fileName )
    {
        OutputStream outStream = null;
        File file = null;
        InputStream inputStream = NewContentDialog.class.getClassLoader().getResourceAsStream( resName );
        if ( inputStream == null )
        {
            throw new TestFrameworkException( resName + " not found" );
        }
        try
        {
            file = new File( Files.createTempDir(), fileName );
            outStream = new FileOutputStream( file );
            file.deleteOnExit();
            byte[] buf = new byte[1024];
            for ( int len; ( len = inputStream.read( buf ) ) != -1; )
            {
                outStream.write( buf, 0, len );
            }
            outStream.close();
        }
        catch ( IOException e )
        {
            throw new TestFrameworkException( resName + " " + e.getMessage() );
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch ( IOException e1 )
            {
                throw new TestFrameworkException( resName + " " + e1.getMessage() );
            }
        }
        return file;
    }

    public NewContentDialog clearSearchInput()
    {
        String os = System.getProperty( "os.name" ).toLowerCase();

        if ( os.indexOf( "mac" ) >= 0 )
        {
            searchInput.sendKeys( Keys.chord( Keys.COMMAND, "a" ), Keys.DELETE );
        }
        else
        {
            searchInput.sendKeys( Keys.chord( Keys.CONTROL, "a" ), Keys.DELETE );
        }
        return this;
    }

    /**
     * Checks that 'AddNewContentWizard' is opened.
     *
     * @return true if dialog opened, otherwise false.
     */
    public boolean isOpened()
    {
        return findElements( By.xpath( DIALOG_TITLE_XPATH ) ).size() > 0;
    }

    /**
     * Waits until 'AddNewContentWizard' is opened.
     *
     * @return true if dialog opened, otherwise false.
     */
    public boolean waitUntilDialogShowed( long timeout )
    {
        return waitUntilVisibleNoException( By.xpath( DIALOG_TITLE_XPATH ), timeout );
    }

    public NewContentDialog typeSearchText( String text )
    {
        clearAndType( searchInput, text );
        return this;
    }

    public boolean isUploadButtonEnabled()
    {
        return uploadButton.isEnabled();
    }

    public boolean isSearchInputEnabled()
    {
        return searchInput.isEnabled();
    }

    public void waitUntilSearchInputEnabled()
    {
        waitUntilElementEnabledNoException( By.xpath( SEARCH_INPUT ), Application.EXPLICIT_NORMAL );
    }

    public ContentBrowsePanel doUploadFile( String path, String fileName )
        throws AWTException
    {
        uploadButton.click();
        sleep( 1000 );
        File file = createFileInTmp( path, fileName );
        insertPathToFileToSystemDialog( file.getAbsolutePath() );
        return new ContentBrowsePanel( getSession() );
    }

    public ContentBrowsePanel doUploadFile( String pathToFile )
    {
        String absolutePath = null;
        URL resource = NewContentDialog.class.getResource( pathToFile );
        try
        {
            absolutePath = Paths.get( resource.toURI() ).toString();
        }
        catch ( URISyntaxException e )
        {
            e.printStackTrace();
        }
        uploadButton.click();
        sleep( 1000 );
        TextTransfer transfer = new TextTransfer();
        transfer.typePathToFileToSystemDialog( absolutePath );
        return new ContentBrowsePanel( getSession() );
    }

    private void insertPathToFileToSystemDialog( String absolutePath )
        throws AWTException
    {
        StringSelection ss = new StringSelection( absolutePath );
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents( ss, null );
        sleep( 1500 );
        Robot robot = new Robot();
        robot.waitForIdle();
        robot.keyPress( KeyEvent.VK_CONTROL );
        robot.keyPress( KeyEvent.VK_V );
        sleep( 500 );
        robot.keyRelease( KeyEvent.VK_V );
        robot.keyRelease( KeyEvent.VK_CONTROL );
        sleep( 2000 );
        robot.keyPress( KeyEvent.VK_ENTER );
        robot.keyRelease( KeyEvent.VK_ENTER );
        sleep( 1000 );
    }

    /**
     * Select content type by name.
     *
     * @param contentTypeName the name of a content type.
     */
    public ContentWizardPanel selectContentType( String contentTypeName )
    {
        String searchString = contentTypeName.substring( contentTypeName.indexOf( ":" ) + 1 );
        waitUntilElementEnabled( By.xpath( SEARCH_INPUT ), Application.EXPLICIT_NORMAL );
        clearAndType( searchInput, searchString );
        sleep( 700 );
        String ctypeXpath = String.format( CONTENT_TYPE_NAME, contentTypeName );
        boolean isContentTypePresent = isElementDisplayed( ctypeXpath );
        if ( !isContentTypePresent )
        {
            saveScreenshot( NameHelper.uniqueName( "err_type" ) );
            throw new TestFrameworkException( "content type with name " + contentTypeName + " was not found!" );
        }
        getDisplayedElement( By.xpath( ctypeXpath ) ).click();
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        //switch to the new browser tab
        switchToNewWizardTab();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        return wizard;
    }

    /**
     * Selects a content type from the list of all types and opens ContentWizardPanel
     *
     * @param contentTypeName
     * @return {@link ContentWizardPanel} instance.
     */
    public ContentWizardPanel selectContentType( ContentTypeName contentTypeName )
    {
        return selectContentType( contentTypeName.toString() );
    }

    /**
     * Gets number of content types from the 'list-items'-view
     *
     * @return number of content types
     */
    public int getNumberContentTypesFromList()
    {
        boolean isPresentList = waitUntilVisibleNoException( By.xpath( ALL_LIST_ITEMS ), Application.EXPLICIT_NORMAL );
        if ( !isPresentList )
        {
            getLogger().info( "list of content types is empty" );
        }
        return findElements( By.xpath( ALL_LIST_ITEMS ) ).stream().map( WebElement::isDisplayed ).collect( Collectors.toList() ).size();
    }

    public int getNumberSitesFromList()
    {
        boolean isPresentList = waitUntilVisibleNoException( By.xpath( ALL_LIST_ITEMS ), Application.EXPLICIT_NORMAL );
        if ( !isPresentList )
        {
            getLogger().info( "list of content types is empty" );
        }
        return findElements( By.xpath( LIST_ITEMS_SITES ) ).size();
    }
}
