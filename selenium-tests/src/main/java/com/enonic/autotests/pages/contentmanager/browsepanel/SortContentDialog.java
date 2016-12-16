package com.enonic.autotests.pages.contentmanager.browsepanel;


import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class SortContentDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'SortContentDialog')]";

    public static final String TITLE = "Sort items";

    private final String SAVE_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Save']]";

    private final String CANCEL_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Cancel']]";

    private final String CANCEL_TOP_BUTTON = DIALOG_CONTAINER + "//div[contains(@class,'cancel-button-top') ]";

    private final String SORT_CONTENT_MENU_BUTTON = DIALOG_CONTAINER + "//div[contains(@id,'TabMenuButton')]";

    private String GRID_ITEM =
        DIALOG_CONTAINER + "//div[contains(@class,'slick-row') and descendant::p[contains(@class,'sub-name') and contains(.,'%s')]]";

    private String VIEWPORT_XPATH = "//div[contains(@id,'SortContentTreeGrid')]//div[@class='slick-viewport']";

    @FindBy(xpath = SAVE_BUTTON)
    WebElement saveButton;

    @FindBy(xpath = CANCEL_BUTTON)
    WebElement cancelButton;

    @FindBy(xpath = SORT_CONTENT_MENU_BUTTON)
    WebElement sortMenuButton;

    @FindBy(xpath = CANCEL_TOP_BUTTON)
    WebElement cancelTopButton;

    public SortContentDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return findElements( By.xpath( DIALOG_CONTAINER ) ).size() > 0;
    }

    public SortContentDialog waitForLoaded( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout ) )
        {
            throw new TestFrameworkException( "SortContentDialog was not showed!" );
        }
        return this;
    }

    public boolean isSaveButtonEnabled()
    {
        return saveButton.isEnabled();
    }

    public boolean isSortMenuButtonEnabled()
    {
        return sortMenuButton.isEnabled();
    }

    public boolean isCancelButtonEnabled()
    {
        return cancelButton.isEnabled();
    }

    public boolean isPresent()
    {
        return findElements( By.xpath( DIALOG_CONTAINER ) ).size() > 0;
    }

    public String getTitle()
    {
        return findElements( By.xpath( DIALOG_CONTAINER + "//h2[@class='title']" ) ).get( 0 ).getText();
    }

    public SortContentDialog clickOnTabMenu()
    {
        sortMenuButton.click();
        sleep( 500 );
        return this;
    }

    public SortContentDialog dragAndSwapItems( String sourceName, String targetName )
    {
        scrollViewPortToTop( getViewportElement() );
        String sourceItem = String.format( GRID_ITEM, sourceName );
        String targetItem = String.format( GRID_ITEM, targetName );
        if ( findElements( By.xpath( sourceItem ) ).size() == 0 || findElements( By.xpath( targetItem ) ).size() == 0 )
        {
            throw new TestFrameworkException(
                "Sort Content Dialog: drag and drop failed. items were not found: " + sourceName + " " + targetName );
        }
        WebElement element = findElements( By.xpath( sourceItem ) ).get( 0 );
        WebElement target = findElements( By.xpath( targetItem ) ).get( 0 );

        Actions builder = new Actions( getDriver() );
        builder.clickAndHold( element ).build().perform();
        // builder.moveToElement( target, 0, -20 ).build().perform();;
        builder.release( target );
        builder.build().perform();
        sleep( 2000 );
        return this;
    }

    public SortContentDialog clickOnCancelButton()
    {
        cancelButton.click();
        waitsElementNotVisible( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL );
        return this;
    }

    public SortContentDialog clickOnCancelOnTop()
    {
        cancelTopButton.click();
        waitsElementNotVisible( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL );
        return this;
    }

    public String getCurrentSortingName()
    {
        return getDisplayedString( SORT_CONTENT_MENU_BUTTON + "//span[@class='label']" );
    }

    public SortContentDialog clickOnSaveButton()
    {
        saveButton.click();
        waitsElementNotVisible( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL );
        sleep( 1000 );
        return this;
    }

    public List<String> getMenuItems()
    {
        return getDisplayedStrings( By.xpath( DIALOG_CONTAINER + "//li[contains(@id,'SortContentTabMenuItem')]//a" ) );
    }

    public void scrollViewPortToTop( WebElement viewport )
    {
        getJavaScriptExecutor().executeScript( "return arguments[0].scrollTop=0", viewport );
        sleep( 1000 );
    }

    WebElement getViewportElement()
    {
        return findElements( By.xpath( VIEWPORT_XPATH ) ).get( 0 );
    }

    public LinkedList<String> getContentNames()
    {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        scrollViewPortToTop( getViewportElement() );
        set.addAll( getGridItemNames() );
        long newScrollTop = 400;
        long scrollTopBefore;
        long scrollTopAfter;
        for ( ; ; )
        {
            scrollTopBefore = getViewportScrollTopValue( getViewportElement() );
            scrollTopAfter = doScrollViewport( newScrollTop );
            if ( scrollTopBefore == scrollTopAfter )
            {
                break;
            }
            newScrollTop += newScrollTop;
            set.addAll( getGridItemNames() );
        }
        return new LinkedList<>( set );
    }

    public Long getViewportScrollTopValue( WebElement viewport )
    {
        return (Long) getJavaScriptExecutor().executeScript( "return arguments[0].scrollTop", viewport );
    }

    private LinkedList<String> getGridItemNames()
    {
        LinkedList list = findElements( By.xpath( DIALOG_CONTAINER + H6_DISPLAY_NAME ) ).stream().filter( WebElement::isDisplayed ).map(
            WebElement::getText ).collect( Collectors.toCollection( LinkedList::new ) );
        return list;
    }


    public Long doScrollViewport( long step )
    {
        if ( findElements( By.xpath( VIEWPORT_XPATH ) ).size() != 0 )
        {
            WebElement viewportElement = findElements( By.xpath( VIEWPORT_XPATH ) ).get( 0 );
            getJavaScriptExecutor().executeScript( "arguments[0].scrollTop=arguments[1]", viewportElement, step );
        }

        sleep( 1000 );
        return getViewportScrollTopValue( findElements( By.xpath( VIEWPORT_XPATH ) ).get( 0 ) );
    }

    public SortContentDialog selectSortMenuItem( String itemName )
    {
        String menuItem =
            DIALOG_CONTAINER + String.format( "//li[contains(@id,'SortContentTabMenuItem') and child::a[text()='%s']]", itemName );
        if ( !isElementDisplayed( menuItem ) )
        {
            saveScreenshot( "err_sort_menu" );
            throw new TestFrameworkException( "sort menu item was not found!" );
        }
        getDisplayedElement( By.xpath( menuItem ) ).click();
        sleep( 1000 );
        return this;
    }
}
