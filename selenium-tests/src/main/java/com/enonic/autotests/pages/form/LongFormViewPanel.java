package com.enonic.autotests.pages.form;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class LongFormViewPanel
    extends FormViewPanel
{
    public static String LONG_PROPERTY = "long";

    @FindBy(xpath = FORM_VIEW + "//div[contains(@id,'api.form.InputView')]//input[contains(@id,'TextInput') and contains(@name,'long')]")
    private WebElement longInput;


    public LongFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String longValue = data.getString( LONG_PROPERTY );
        typeLong( longValue );
        sleep( 300 );
        return this;
    }

    public String getLongValue()
    {
        return longInput.getAttribute( "value" );
    }

    LongFormViewPanel typeLong( String longValue )
    {
        clearAndType( longInput, longValue );
        return this;
    }

}
