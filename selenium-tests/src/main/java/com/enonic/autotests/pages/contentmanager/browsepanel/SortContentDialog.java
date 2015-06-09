package com.enonic.autotests.pages.contentmanager.browsepanel;


import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class SortContentDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'SortContentDialog')]";

    public static final String TITLE = "Default sorting";

    private final String SAVE_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Save']]";

    private final String CANCEL_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Cancel']]";

    private final String CANCEL_TOP_BUTTON = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-top') ]";

    private final String SORT_CONTENT_MENU_BUTTON = DIALOG_CONTAINER + "//div[contains(@id,'TabMenuButton')]";

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

    public SortContentDialog clickOnCancelButton()
    {
        cancelButton.click();
        waitElementNotVisible( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL );
        return this;
    }

    public SortContentDialog clickOnCancelOnTop()
    {
        cancelTopButton.click();
        waitElementNotVisible( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL );
        return this;
    }

    public String getCurrentSortingName()
    {
        return findElements( By.xpath( SORT_CONTENT_MENU_BUTTON + "//span[@class='label']" ) ).stream().filter(
            WebElement::isDisplayed ).map( WebElement::getText ).collect( Collectors.toList() ).get( 0 );
    }

    public SortContentDialog clickOnSaveButton()
    {
        saveButton.click();
        waitElementNotVisible( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL );
        return this;
    }

    public List<String> getMenuItems()
    {
        List<String> items =
            findElements( By.xpath( DIALOG_CONTAINER + "//li[contains(@id,'SortContentTabMenuItem')]//span" ) ).stream().filter(
                WebElement::isDisplayed ).map( WebElement::getText ).collect( Collectors.toList() );
        return items;
    }

    public LinkedList<String> getContentNames()
    {
        return findElements( By.xpath( "//div[contains(@id,'NamesView')]//h6[contains(@class,'main-name')]" ) ).stream().filter(
            WebElement::isDisplayed ).map( WebElement::getText ).collect( Collectors.toCollection( LinkedList::new ) );
    }

    public SortContentDialog selectSortMenuItem( String itemName )
    {
        findElements( By.xpath(
            DIALOG_CONTAINER + String.format( "//li[contains(@id,'SortContentTabMenuItem')]//span[text()='%s']", itemName ) ) ).get(
            0 ).click();
        sleep( 400 );
        return this;
    }
}
