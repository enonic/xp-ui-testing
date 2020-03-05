package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.List;
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
import com.enonic.xp.schema.content.ContentTypeName;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class NewContentDialog
    extends Application
{
    public static final String CONTAINER = "//div[contains(@id,'NewContentDialog')]";

    public static final String ALL_LIST_ITEMS =
        "//div[contains(@id,'NewContentDialog') and not(contains(@class,'mock-modal-dialog'))]//ul[@class='content-types-list']//li[contains(@class,'content-types-list-item')]";

    public static final String LIST_ITEMS_SITES = "//div[contains(@id,'NewContentDialog')]//ul/li[@class='content-types-list-item site']";

    public static final String SEARCH_INPUT = CONTAINER + "//div[contains(@id,'FileInput')]/input";

    private final static String DIALOG_TITLE_XPATH =
        "//div[contains(@class,'modal-dialog')]/div[contains(@class,'dialog-header') and contains(.,'Create Content')]";

    public static String CONTENT_TYPE_DISPLAY_NAME =
        CONTAINER + "//li[contains(@class,'content-types-list-item') and descendant::h6[contains(@class,'main-name') and text()='%s']]";

    public static String CONTENT_TYPE_BY_DESCRIPTION =
        CONTAINER + "//li[contains(@class,'content-types-list-item') and descendant::p[contains(@class,'sub-name') and text()='%s']]";

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
        return waitUntilVisibleNoException( By.xpath( MOST_POPULAR_BLOCK ), Application.EXPLICIT_NORMAL );
    }

    public List<String> getContentTypesNames()
    {
        return getDisplayedStrings( By.xpath( AVAILABLE_CONTENT_TYPES ) );
    }

    public List<String> getMostPopularItemsNames()
    {
        return getDisplayedStrings( By.xpath( MOST_POPULAR_ITEM_NAME ) );
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
     *
     * @return true if dialog opened, otherwise false.
     */
    public boolean waitUntilDialogLoaded( long timeout )
    {
        return waitUntilVisibleNoException( By.xpath( DIALOG_TITLE_XPATH ), timeout );
    }

    public NewContentDialog typeSearchText( String text )
    {
        clearAndType( searchInput, text );
        return this;
    }

    public NewContentDialog typeSearchTextInHiddenInput( String text )
    {
        buildActions().sendKeys( text ).build().perform();
        return this;
    }

    public boolean isUploadButtonEnabled()
    {
        return uploadButton.isEnabled();
    }


    public void waitUntilSearchInputEnabled()
    {
        waitUntilElementEnabledNoException( By.xpath( SEARCH_INPUT ), Application.EXPLICIT_NORMAL );
    }

    /**
     * Select content type by name or description.
     *
     * @param type the name of a content type.
     */
    public ContentWizardPanel selectContentType( String type )
    {
        buildActions().sendKeys( type ).build().perform();
        sleep( 500 );
        String ctypeXpath = String.format( CONTENT_TYPE_DISPLAY_NAME, type );
        boolean isContentTypePresent = isElementDisplayed( ctypeXpath );
        if ( !isContentTypePresent )
        {
            saveScreenshot( NameHelper.uniqueName( "err_type" ) );
            throw new TestFrameworkException( "content type with name " + type + " was not found!" );
        }
        getDisplayedElement( By.xpath( ctypeXpath ) ).click();
        sleep( 300 );
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

    public NewContentDialog showSearchInput()
    {
        buildActions().sendKeys( "a" ).build().perform();
        return this;
    }
}
