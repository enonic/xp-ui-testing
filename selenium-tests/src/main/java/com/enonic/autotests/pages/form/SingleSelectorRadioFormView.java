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
    private String RADIO_INPUTS = "//div[contains(@class,'radio-group')]//input[@type='radio']";

    public SingleSelectorRadioFormView( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String option = data.getString( "option" );
        if ( option == null )
        {
            return this;
        }

        selectOption( option );
        sleep( 300 );
        return this;
    }

    public SingleSelectorRadioFormView selectOption( String option )
    {
        String radioInputXpath = String.format( "//span[contains(@class,'radio-button') and child::label[text()='%s']]//input", option );
        if ( findElements( By.xpath( radioInputXpath ) ).size() == 0 )
        {
            throw new TestFrameworkException( "option was not found! " + option );
        }
        findElements( By.xpath( radioInputXpath ) ).get( 0 ).click();

        return this;
    }

    public String getSelectedOption()
    {
        List<WebElement> elements = findElements( By.xpath( FORM_VIEW + RADIO_INPUTS ) );
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