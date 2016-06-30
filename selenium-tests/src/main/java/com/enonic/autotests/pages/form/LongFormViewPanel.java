package com.enonic.autotests.pages.form;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class LongFormViewPanel
    extends FormViewPanel
{
    public static String LONG_VALUE = "long_value";

    private final String LONG_INPUT =
        FORM_VIEW + "//div[contains(@id,'InputView')]//input[contains(@id,'TextInput') and contains(@name,'long')]";

    @FindBy(xpath = LONG_INPUT)
    private WebElement longInput;


    public LongFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String longValue = data.getString( LONG_VALUE );
        if ( longValue != null )
        {
            typeLongValue( longValue );
        }
        sleep( 300 );
        return this;
    }

    public String getLongValue()
    {
        return longInput.getAttribute( "value" );
    }

    public LongFormViewPanel typeLongValue( String longValue )
    {
        clearAndType( longInput, longValue );
        return this;
    }

    public boolean isValueInInputValid( int index )
    {
        List<WebElement> actualInputs = getDisplayedElements( By.xpath( LONG_INPUT ) );
        return !waitAndCheckAttrValue( actualInputs.get( index ), "class", "invalid", Application.EXPLICIT_NORMAL );
    }

}
