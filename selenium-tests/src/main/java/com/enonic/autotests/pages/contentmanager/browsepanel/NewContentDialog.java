package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.xp.schema.content.ContentTypeName;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Content Manager application/add new content/select content type
 */
public class NewContentDialog
    extends Application
{
    public static String CONTAINER = "//div[contains(@id,'app.create.NewContentDialog')]";

    private final static String DIALOG_TITLE_XPATH =
        "//div[contains(@class,'modal-dialog')]/div[contains(@class,'dialog-header') and contains(.,'Create Content')]";

    public static String CONTENT_TYPE_NAME =
        CONTAINER + "//ul[@class='content-types-list']//li[contains(@class,'content-types-list-item') and descendant::p[text()='%s']]";


    public static final String ALL_LIST_ITEMS =
        "//div[contains(@id,'app.create.NewContentDialog') and not(contains(@class,'mock-modal-dialog'))]//ul[@class='content-types-list']//li[contains(@class,'content-types-list-item')]";

    public static final String LIST_ITEMS_SITES =
        "//div[contains(@id,'app.create.NewContentDialog')]//ul/li[@class='content-types-list-item site']";

    public static final String SEARCH_INPUT = "//div[contains(@id,'api.ui.text.FileInput')]/input";

    private final String SEARCH_INPUT_SCRIPT = "window.api.dom.ElementRegistry.getElementById('%s').setValue(arguments[0])";

    private final String UPLOAD_FILE_BUTTON = "//a[contains(@id,'uploader-dropzone') and @class='dropzone']";

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
        // String id = getDriver().findElement( By.xpath( SEARCH_INPUT ) ).getAttribute( "id" );
        //String js = String.format( SEARCH_INPUT_SCRIPT, id );
        //( (JavascriptExecutor) getSession().getDriver() ).executeScript( js, "" );
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

    public ContentBrowsePanel doUploadFile( String resName )
        throws AWTException
    {
        uploadButton.click();
        sleep( 1000 );

        URL dirURL = NewContentDialog.class.getClassLoader().getResource( resName );
        if ( dirURL == null )
        {
            throw new TestFrameworkException( "tests resource for upload tests was not found:" + resName );
        }
        File file = null;
        try
        {
            getLogger().info( "path to resource  is###: " + dirURL.toURI() );
            file = new File( dirURL.toURI() );

        }
        catch ( URISyntaxException e )
        {
            getLogger().error( "wrong uri for file " + resName );
        }

        StringSelection ss = new StringSelection( file.getAbsolutePath() );
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
        sleep( 2000 );
        return new ContentBrowsePanel( getSession() );
    }


    /**
     * Select content type by name.
     *
     * @param contentTypeName the name of a content type.
     */
    public ContentWizardPanel selectContentType( String contentTypeName )
    {
        String searchString = contentTypeName.substring( contentTypeName.indexOf( ":" ) + 1 );
        clearAndType( searchInput, searchString );
        sleep( 500 );
        String ctypeXpath = String.format( CONTENT_TYPE_NAME, contentTypeName );
        boolean isContentNamePresent = waitUntilVisibleNoException( By.xpath( ctypeXpath ), Application.EXPLICIT_LONG );
        if ( !isContentNamePresent )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "no_type" ) );
            throw new TestFrameworkException( "content type with name " + contentTypeName + " was not found!" );
        }

        findElements( By.xpath( ctypeXpath ) ).get( 0 ).click();
        waitsForSpinnerNotVisible();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
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
