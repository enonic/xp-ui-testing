package com.enonic.autotests.pages.form;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class DoubleFormViewPanel
    extends FormViewPanel
{
    public static String DOUBLE_PROPERTY = "double";

    @FindBy(xpath = FORM_VIEW +
        "//div[contains(@id,'api.form.InputView') and descendant::div[@title='Double']]//input[contains(@id,'TextInput')]")
    private WebElement doubleInput;


    public DoubleFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String doubleValue = data.getString( DOUBLE_PROPERTY );
        // type a double
        doubleInput.sendKeys( doubleValue );
        sleep( 300 );
        return this;
    }

    public String getDoubleValue()
    {
        return doubleInput.getAttribute( "value" );
    }
}