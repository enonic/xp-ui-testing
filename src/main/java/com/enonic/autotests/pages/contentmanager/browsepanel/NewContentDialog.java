package com.enonic.autotests.pages.contentmanager.browsepanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.wem.api.schema.content.ContentTypeName;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Content Manager application/add new content/select content type
 */
public class NewContentDialog
    extends Application
{
    private final static String DIALOG_TITLE_XPATH =
        "//div[contains(@class,'modal-dialog')]/div[contains(@class,'dialog-header') and contains(.,'What do you want to create?')]";

    public static String CONTENT_TYPE_NAME =
        "//div[contains(@id,'app.create.NewContentDialogList')]//li[@class='content-types-list-item' and descendant::p[text()='%s']]";

    public static final String SHOW_ALL_LINK = "//div[@class='content-type-facet']/span[contains(.,'All')]";

    public static final String FILTER_BY_CONTENT_LINK = "//div[@class='content-type-facet']/span[contains(.,'Content')]";

    public static final String FILTER_BY_SITES_LINK = "//div[@class='content-type-facet']/span[contains(.,'Sites')]";

    public static final String ALL_LIST_ITEMS =
        "//div[contains(@id,'app.create.NewContentDialogList')]/ul/li[contains(@class,'content-types-list-item')]";

    public static final String LIST_ITEMS_SITES =
        "//div[contains(@id,'app.create.NewContentDialogList')]/ul/li[@class='content-types-list-item site']";

    /**
     * The constructor.
     *
     * @param session  {@link TestSession}   instance.
     */
    public NewContentDialog( TestSession session )
    {
        super( session );
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
    public boolean waituntilDialogShowed( long timeout )
    {
        return waitUntilVisibleNoException( By.xpath( DIALOG_TITLE_XPATH ), timeout );

    }

    /**
     * Select content type by name.
     *
     * @param contentTypeName the name of a content type.
     */
    public ContentWizardPanel selectContentType( String contentTypeName )
    {
        String ctypeXpath = String.format( CONTENT_TYPE_NAME, contentTypeName );
        boolean isContentNamePresent = waitElementExist( ctypeXpath, Application.EXPLICIT_4 );
        if ( !isContentNamePresent )
        {
            throw new TestFrameworkException( "content type with name " + contentTypeName + " was not found!" );
        }
        getDriver().findElement( By.xpath( ctypeXpath ) ).click();
        //TestUtils.clickByElement( By.xpath( ctypeXpath ), getDriver() );
        waitsForSpinnerNotVisible();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        //TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "contentwizard" ) );
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
     * Clicks on 'Content' link in the FilterPanel that located in the dialog window.
     *
     * @return {@link NewContentDialog} instance.
     */
    public NewContentDialog doFilterByContent()
    {
        waitsElementNotVisible( By.xpath( "//span[contains(.,'All (0)')]" ), Application.EXPLICIT_3 );
        findElements( By.xpath( FILTER_BY_CONTENT_LINK ) ).get( 0 ).click();
        sleep( 500 );
        return this;
    }

    public NewContentDialog doClickShowAll()
    {
        findElements( By.xpath( SHOW_ALL_LINK ) ).get( 0 ).click();
        sleep( 500 );
        return this;
    }

    /**
     * Clicks on 'Sites' link in the FilterPanel that located in the dialog window.
     *
     * @return {@link NewContentDialog} instance.
     */
    public NewContentDialog doFilterBySites()
    {
        waitsElementNotVisible( By.xpath( "//span[contains(.,'All (0)')]" ), Application.EXPLICIT_3 );

        findElements( By.xpath( FILTER_BY_SITES_LINK ) ).get( 0 ).click();
        sleep( 700 );
        return this;
    }

    public int getNumberContentTypesFromFilterLink( FilterName filter )
    {
        String text = null;
        //waits until content will be loaded and 'All (0)' changed to 'All (43)'
        waitsElementNotVisible( By.xpath( "//span[contains(.,'All (0)')]" ), Application.EXPLICIT_3 );
        switch ( filter )
        {
            case CONTENT:
                text = findElement( By.xpath( FILTER_BY_CONTENT_LINK ) ).getText();
                break;
            case SITES:
                text = findElement( By.xpath( FILTER_BY_SITES_LINK ) ).getText();
                break;

            case SHOW_ALL:
                text = findElement( By.xpath( SHOW_ALL_LINK ) ).getText();
                break;

        }
        return parseText( text );
    }

    private int parseText( String text )
    {
        int startIndex = text.indexOf( "(" );
        int endIndex = text.indexOf( ")" );
        int number = Integer.valueOf( text.substring( startIndex + 1, endIndex ) );
        return number;
    }

    /**
     * Gets number of content types from the 'list-items'-view
     *
     * @return number of content types
     */
    public int getNumberContentTypesFromList()
    {
        boolean isPresentList = waitUntilVisibleNoException( By.xpath( ALL_LIST_ITEMS ), Application.EXPLICIT_3 );
        if ( !isPresentList )
        {
            getLogger().info( "list of content types is empty" );
        }
        return findElements( By.xpath( ALL_LIST_ITEMS ) ).size();
    }

    public int getNumberSitesFromList()
    {
        boolean isPresentList = waitUntilVisibleNoException( By.xpath( ALL_LIST_ITEMS ), Application.EXPLICIT_3 );
        if ( !isPresentList )
        {
            getLogger().info( "list of content types is empty" );
        }
        return findElements( By.xpath( LIST_ITEMS_SITES ) ).size();
    }


    public enum FilterName
    {
        SHOW_ALL, CONTENT, SITES
    }

    public boolean isLinkActive( FilterName name )
    {
        WebElement element = null;
        switch ( name )
        {
            case SHOW_ALL:
                element = findElement( By.xpath( SHOW_ALL_LINK ) );
                break;
            case CONTENT:
                element = findElement( By.xpath( FILTER_BY_CONTENT_LINK ) );
                break;
            case SITES:
                element = findElement( By.xpath( FILTER_BY_SITES_LINK ) );
                break;
        }
        String classAttr = getAttribute( element, "class", Application.EXPLICIT_3 );
        return classAttr.contains( "active" );
    }

    public boolean isPresentElement( String xpath )
    {
        return findElements( By.xpath( xpath ) ).size() > 0;
    }

}
