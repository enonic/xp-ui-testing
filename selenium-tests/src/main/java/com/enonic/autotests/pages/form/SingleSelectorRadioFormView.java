package com.enonic.autotests.pages.form;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class SingleSelectorRadioFormView
    extends FormViewPanel
{
    public static String RADIO_OPTION = "radio-option";

    private String ALL_RADIO_INPUTS = "//div[contains(@id,'RadioButton')]//input[@type='radio']";

    private String RADIO_INPUT = "//span[contains(@class,'radio-button') and child::label[text()='%s']]//input";

    public SingleSelectorRadioFormView( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String option = data.getString( RADIO_OPTION );
        if ( option == null )
        {
            return this;
        }
        selectOption( option );
        sleep( 300 );
        return this;
    }

    public SingleSelectorRadioFormView selectOption( String label )
    {
        String radioInputXpath = String.format( RADIO_INPUT, label );
        if ( !isElementDisplayed( radioInputXpath ) )
        {
            throw new TestFrameworkException( "option was not found! " + label );
        }
        getDisplayedElement( By.xpath( radioInputXpath ) ).click();
        sleep( 1000 );
        return this;
    }

    public String getSelectedOption()
    {
        List<WebElement> elements = findElements( By.xpath( FORM_VIEW + ALL_RADIO_INPUTS ) );
        if ( elements.stream().filter( WebElement::isSelected ).count() == 0 )
        {
            //no selected options
            return "";
        }
        List<WebElement> inputs = findElements( By.xpath( "//span[contains(@class,'radio-button') ]//input" ) );
        List<WebElement> labels = findElements( By.xpath( "//span[contains(@class,'radio-button') ]//label" ) );
        for ( int i = 0; i < inputs.size(); i++ )
        {
            if ( inputs.get( i ).isSelected() )
            {
                return labels.get( i ).getText();
            }
        }
        return "";
    }
}
