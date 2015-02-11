package com.enonic.autotests.pages.form;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.wem.api.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class DoubleFormViewPanel
    extends FormViewPanel
{
    public static String REQUIRED_DOUBLE_PROPERTY = "req_double";

    private final String FORM_VIEW = "//div[contains(@id,'api.form.FormView')]";

    @FindBy(xpath = FORM_VIEW +
        "//div[contains(@id,'api.form.InputView') and descendant::div[@title='Required Double']]//input[contains(@id,'TextInput')]")
    private WebElement reqDoubleInput;


    public DoubleFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String doubleValue = data.getString( REQUIRED_DOUBLE_PROPERTY );
        // type a date
        reqDoubleInput.sendKeys( doubleValue );
        sleep( 300 );
        return this;
    }

    public String getRequiredDoubleValue()
    {
        return reqDoubleInput.getAttribute( "value" );
    }
}