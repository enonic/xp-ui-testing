package com.enonic.autotests.pages.form;


import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.Property;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class TextLine2_5_FormViewPanel
    extends BaseTextLineFormViewPanel
{
    private final String TEXT_INPUTS_XPATH = FORM_VIEW + "//input[contains(@name,'TextLine_2_5')]";

    public TextLine2_5_FormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        List<WebElement> inputs = findElements( By.xpath( TEXT_INPUTS_XPATH ) );
        if ( inputs.size() == 0 )
        {
            throw new TestFrameworkException( "no one text input was not found" );
        }
        if ( inputs.size() > 5 )
        {
            throw new TestFrameworkException( "number of text inputs can not be more than 5" );
        }
        int i = 0;
        for ( final Property sourceProperty : data.getProperties() )
        {
            if ( i >= 5 )
            {
                throw new TestFrameworkException( "number of text inputs can not be more than 5" );
            }
            if ( i >= inputs.size() )
            {
                break;
            }
            clearAndType( inputs.get( i ), sourceProperty.getString() );
            sleep( 300 );
            i++;
        }

        return this;
    }

    public List<String> getTextLineValues()
    {
        List<WebElement> inputs = findElements( By.xpath( TEXT_INPUTS_XPATH ) );
        List<String> result = inputs.stream().map( element -> {
            return element.getAttribute( "value" );
        } ).collect( Collectors.toList() );

        return result;
    }

    public void clearAllInputs()
    {
        List<WebElement> inputs = findElements( By.xpath( TEXT_INPUTS_XPATH ) );
        if ( inputs.size() == 0 )
        {
            throw new TestFrameworkException( "no one text input was not found" );
        }
        if ( inputs.size() > 5 )
        {
            throw new TestFrameworkException( "number of text inputs can not be more than 5" );
        }

        for ( final WebElement input : inputs )
        {
            input.clear();
        }
    }
}

