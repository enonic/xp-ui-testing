package com.enonic.autotests.pages.form;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class CheckBoxFormViewPanel
    extends FormViewPanel
{
    public static String CHECKBOX_PROPERTY = "checkbox";


    @FindBy(xpath = FORM_VIEW +
        "//div[contains(@id,'api.form.InputView') and descendant::div[@title='To Check']]//div[contains(@id,'Checkbox')]//input[@type='checkbox']")
    private WebElement checkBox;


    public CheckBoxFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        boolean checkboxValue = data.getBoolean( CHECKBOX_PROPERTY );
        if ( checkBox.getAttribute( "checked" ) == null && checkboxValue )
        {
            List<WebElement> ele = findElements( By.xpath(
                "//div[contains(@id,'api.form.InputView') and descendant::div[@title='To Check']]//div[contains(@id,'Checkbox')]//div[contains(@class,'checkbox']" ) );
            ele.get( 0 ).click();

            //checkBox.click();
        }

        sleep( 300 );
        return this;
    }

    public boolean isChecked()
    {
        return checkBox.getAttribute( "checked" ) != null;
    }
}
