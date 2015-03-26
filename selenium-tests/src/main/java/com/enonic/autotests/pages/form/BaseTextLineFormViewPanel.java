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

    protected String VALIDATION_VIEWER = FORM_VIEW + "//div[contains(@id, 'ValidationRecordingViewer')]";

    protected String ADD_BUTTON_XPATH = FORM_VIEW +
        "//button[contains(@id,'api.ui.button.Button') and child::span[text()='Add'] and not(contains(@style,'display: none'))]";

    public BaseTextLineFormViewPanel( final TestSession session )
    {
        super( session );
    }

    public int getNumberOfTextInputs()
    {
        return findElements( By.xpath( FORM_VIEW + "//div[contains(@id,'TextLine')]//input[contains(@id,'TextInput')]" ) ).size();
    }

    public boolean isAddButtonPresent()
    {
        return findElements( By.xpath( ADD_BUTTON_XPATH ) ).size() > 0;
    }

    public int getNumberOfDisplayedRemoveButtons()
    {
        List<WebElement> allElements = findElements( By.xpath( FORM_VIEW + "//div[contains(@id,'TextLine')]//a[@class='remove-button']" ) );
        return allElements.stream().filter( WebElement::isDisplayed ).collect( Collectors.toList() ).size();
    }

    public boolean isValidationMessagePresent()
    {
        List<WebElement> result = findElements( By.xpath( VALIDATION_VIEWER ) );
        if ( result.size() == 0 )
        {
            return false;
        }
        else
        {
            return result.get( 0 ).isDisplayed();
        }
    }

    public String getValidationMessage()
    {
        return findElements( By.xpath( VALIDATION_VIEWER + "//li" ) ).get( 0 ).getText();
    }

    public BaseTextLineFormViewPanel clickOnAddButton( int times )
    {
        for ( int i = 0; i < times; i++ )
        {
            clickOnAddButton();
        }
        return this;
    }

    public BaseTextLineFormViewPanel clickOnAddButton()
    {
        if ( findElements( By.xpath( ADD_BUTTON_XPATH ) ).size() == 0 )
        {
            throw new TestFrameworkException( "Add button not present in Form View Panel!" );
        }
        findElements( By.xpath( ADD_BUTTON_XPATH ) ).get( 0 ).click();
        sleep( 500 );
        return this;
    }

    public BaseTextLineFormViewPanel clickOnLastRemoveButton()
    {
        List<WebElement> allElements = findElements( By.xpath( FORM_VIEW + "//div[contains(@id,'TextLine')]//a[@class='remove-button']" ) );

        List<WebElement> list = allElements.stream().filter( WebElement::isDisplayed ).collect( Collectors.toList() );
        if ( list.size() == 0 )
        {
            throw new TestFrameworkException( "Remove button was not found" );
        }
        list.get( list.size() - 1 ).click();
        sleep( 500 );
        return this;

    }


}
