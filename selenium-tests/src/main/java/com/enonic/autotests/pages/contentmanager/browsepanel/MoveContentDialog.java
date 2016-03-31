package com.enonic.autotests.pages.contentmanager.browsepanel;


import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class MoveContentDialog
    extends Application
{
    public static String DIALOG_TITLE = "Move item with children";

    private final String DIALOG_CONTAINER = "//div[contains(@id,'app.browse.MoveContentDialog')]";

    private final String TITLE_XPATH = DIALOG_CONTAINER + "//h2[@class='title']";

    private final String FILTER_INPUT = DIALOG_CONTAINER + "//input[contains(@id,'ComboBoxOptionFilterInput')]";

    private final String MOVE_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@id,'api.ui.dialog.DialogButton') and child::span[text()='Move']]";

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
     * @param session {@link com.enonic.autotests.TestSession}   instance.
     */
    public MoveContentDialog( TestSession session )
    {
        super( session );
    }

    public MoveContentDialog doMove( String destinationName )
    {
        selectDestination( destinationName );
        sleep( 500 );
        moveButton.click();
        waitForClosed();
        return this;
    }

    private MoveContentDialog selectDestination( String name )
    {
        String xpath = String.format( "//div[contains(@id,'api.app.NamesView') and child::p[contains(.,'%s')] ]", name );
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER + xpath ), Application.EXPLICIT_NORMAL ) )
        {
            throw new TestFrameworkException( "destination folder was not found!" );
        }
        findElements( By.xpath( xpath ) ).get( 0 ).click();
        return this;
    }

    public void clickOnCancelTopButton()
    {
        cancelButtonTop.click();
        sleep( 200 );

    }

    public void clickOnCancelBottomButton()
    {
        cancelButtonBottom.click();
        sleep( 200 );
    }

    public boolean waitForClosed()
    {
        return waitElementNotVisible( By.xpath( TITLE_XPATH ), Application.EXPLICIT_NORMAL );
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
        //optionFilterInput.sendKeys( text );
        sleep( 500 );
        return this;
    }

    public boolean isOpened()
    {
        List<WebElement> elements = findElements( By.xpath( DIALOG_CONTAINER ) );
        return elements.stream().filter( WebElement::isDisplayed ).collect( Collectors.toList() ).size() > 0;
    }

    /**
     * Waits until 'MoveContentDialog' is opened.
     *
     * @return true if dialog opened, otherwise false.
     */
    public boolean waitUntilDialogShowed( long timeout )
    {
        return waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout );

    }

}
