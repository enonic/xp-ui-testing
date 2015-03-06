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

public class TextLine1_0_FormViewPanel
    extends BaseTextLineFormViewPanel
{
    private final String TEXT_INPUTS_XPATH = FORM_VIEW + "//input[contains(@name,'TextLine_1_0')]";

    public TextLine1_0_FormViewPanel( final TestSession session )
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
        int i = 0;
        for ( final Property sourceProperty : data.getProperties() )
        {

            inputs.get( i ).sendKeys( sourceProperty.getValue().toString() );
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
}
