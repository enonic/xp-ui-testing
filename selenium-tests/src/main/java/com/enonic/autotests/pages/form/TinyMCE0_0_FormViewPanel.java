package com.enonic.autotests.pages.form;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class TinyMCE0_0_FormViewPanel
    extends BaseTinyMCEFormViewPanel
{

    private final String ADD_BUTTON_XPATH = FORM_VIEW + "//div[@class='bottom-button-row']//button[child::span[text()='Add']]";

    public TinyMCE0_0_FormViewPanel( final TestSession session )
    {
        super( session );
    }

    private final String REMOVE_BUTTON_XPATH = FORM_VIEW + "//div[contains(@id,'InputOccurrenceView')]//a[@class='remove-button']";

    public boolean isOpened()
    {
        return waitUntilVisibleNoException( By.xpath( STEP_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public String getText()
    {
        Object obj =
            ( (JavascriptExecutor) getSession().getDriver() ).executeScript( "return document.getElementById('tinymce').innerHTML" );
        String text = obj.toString();

        return text;
    }

    public boolean isAddButtonPresent()
    {
        return
            findElements( By.xpath( ADD_BUTTON_XPATH ) ).stream().filter( WebElement::isDisplayed ).collect( Collectors.toList() ).size() >
                0;


    }

    public boolean waitUntilAddButtonNotVisible()
    {
        return waitElementNotVisible( By.xpath( ADD_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public TinyMCE0_0_FormViewPanel clickOnAddButton()
    {
        if ( findElements( By.xpath( ADD_BUTTON_XPATH ) ).size() == 0 )
        {
            throw new TestFrameworkException( "Add button not present in Form View Panel!" );
        }
        findElements( By.xpath( ADD_BUTTON_XPATH ) ).get( 0 ).click();
        sleep( 500 );
        return this;
    }

    public TinyMCE0_0_FormViewPanel removeLastTextArea()
    {
        List<WebElement> buttons = findElements( By.xpath( REMOVE_BUTTON_XPATH ) );
        if ( buttons.size() != 0 )
        {
            buttons.get( buttons.size() - 1 ).click();
            sleep( 1000 );
            return this;
        }
        else
        {
            throw new TestFrameworkException( "no one 'close' button was found!" );


        }
    }
}
