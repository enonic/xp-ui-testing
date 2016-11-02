package com.enonic.autotests.pages.contentmanager.browsepanel;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class MoveContentDialog
    extends Application
{
    public static String DIALOG_TITLE = "Move item with children";

    public static String NO_MATCHING_ITEMS = "No matching items";

    private final String DIALOG_CONTAINER = "//div[contains(@id,'MoveContentDialog')]";

    private final String TITLE_XPATH = DIALOG_CONTAINER + "//h2[@class='title']";

    private final String FILTER_INPUT = DIALOG_CONTAINER + COMBOBOX_OPTION_FILTER_INPUT;

    private final String MOVE_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Move']]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + "//div[contains(@class,'cancel-button-top')]";

    private final String CANCEL_BUTTON_BOTTOM = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-bottom')]";

    @FindBy(xpath = MOVE_BUTTON)
    private WebElement moveButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    @FindBy(xpath = CANCEL_BUTTON_BOTTOM)
    private WebElement cancelButtonBottom;


    @FindBy(xpath = FILTER_INPUT)
    private WebElement optionFilterInput;

    /**
     * The constructor.
     *
     * @param session {@link TestSession}   instance.
     */
    public MoveContentDialog( TestSession session )
    {
        super( session );
    }

    public MoveContentDialog selectDestinationAndClickOnMove( String destinationName )
    {
        selectDestination( destinationName );
        sleep( 500 );
        moveButton.click();
        waitForClosed();
        return this;
    }

    private MoveContentDialog selectDestination( String name )
    {
        String destinationXpath = DIALOG_CONTAINER + String.format( NAMES_VIEW_BY_NAME, name );
        if ( !waitUntilVisibleNoException( By.xpath( destinationXpath ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_move_" + name );
            throw new TestFrameworkException( "destination folder was not found! " + name );
        }
        getDisplayedElement( By.xpath( destinationXpath ) ).click();
        return this;
    }

    public void clickOnCancelTopButton()
    {
        cancelButtonTop.click();
        sleep( 200 );
    }

    public void closeByClickingOnEsc()
    {
        Actions actions = new Actions( getDriver() );
        actions.sendKeys( Keys.ESCAPE );
        actions.build().perform();
        sleep( 200 );
    }

    public void clickOnCancelBottomButton()
    {
        cancelButtonBottom.click();
        sleep( 200 );
    }

    public boolean waitForClosed()
    {
        return waitsElementNotVisible( By.xpath( TITLE_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public String getTitle()
    {
        if ( findElements( By.xpath( TITLE_XPATH ) ).size() == 0 )
        {
            throw new TestFrameworkException( "dialog's title was not found!" );
        }
        return findElements( By.xpath( DIALOG_CONTAINER + "//h2[@class='title']" ) ).get( 0 ).getText();
    }

    public MoveContentDialog typeSearchText( String text )
    {
        clearAndType( optionFilterInput, text );
        sleep( 500 );
        return this;
    }

    public boolean isDestinationMatches( String name )
    {
        String destinationXpath = DIALOG_CONTAINER + String.format( NAMES_VIEW_BY_NAME, name );
        return isElementDisplayed( destinationXpath );
    }

    public boolean isNoMatchingItemsMessageDisplayed()
    {
        String message = DIALOG_CONTAINER + "//div[@class='empty-options']";
        return isElementDisplayed( message );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( DIALOG_CONTAINER );
    }

    /**
     * Waits until 'MoveContentDialog' is opened.
     *
     * @return true if dialog opened, otherwise false.
     */
    public boolean waitUntilDialogShown( long timeout )
    {
        return waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout );
    }
}
