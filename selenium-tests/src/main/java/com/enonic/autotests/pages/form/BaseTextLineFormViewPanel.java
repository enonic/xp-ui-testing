package com.enonic.autotests.pages.form;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public abstract class BaseTextLineFormViewPanel
    extends FormViewPanel
{
    protected final String REMOVE_TEXT_INPUT_BUTTON = FORM_VIEW + "//div[contains(@id,'TextLine')]//button[@class='remove-button']";

    public BaseTextLineFormViewPanel( final TestSession session )
    {
        super( session );
    }

    public int getNumberOfTextInputs()
    {
        return findElements( By.xpath( FORM_VIEW + "//div[contains(@id,'TextLine')]//input[contains(@id,'TextInput')]" ) ).size();
    }

    public int getNumberOfDisplayedRemoveButtons()
    {
        List<WebElement> allElements = findElements( By.xpath( REMOVE_TEXT_INPUT_BUTTON ) );
        return allElements.stream().filter( WebElement::isDisplayed ).collect( Collectors.toList() ).size();
    }

    public BaseTextLineFormViewPanel clickOnAddButton( int times )
    {
        for ( int i = 0; i < times; i++ )
        {
            clickOnAddButton();
        }
        return this;
    }

    public BaseTextLineFormViewPanel clickOnLastRemoveButton()
    {
        List<WebElement> removeButtons = getDisplayedElements( By.xpath( REMOVE_TEXT_INPUT_BUTTON ) );
        if ( removeButtons.size() == 0 )
        {
            saveScreenshot( "err_remove_button" );
            throw new TestFrameworkException( "Remove button was not found" );
        }
        removeButtons.get( removeButtons.size() - 1 ).click();
        sleep( 500 );
        return this;

    }

}

