package com.enonic.autotests.pages.form;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
                "//div[contains(@id,'api.form.InputView') and descendant::div[@title='To Check']]//div[contains(@class,'checkbox')]//input" ) );

            Actions actions = new Actions( getDriver() );
            actions.click( ele.get( 0 ) ).build().perform();

            sleep( 1000 );

        }

        sleep( 300 );
        return this;
    }

    public CheckBoxFormViewPanel pressKeyOnRow( WebElement element, Keys key )
    {

        Actions actions = new Actions( getDriver() );
        actions.moveToElement( element ).build().perform();
        sleep( 1000 );
        // actions.click( element );
        actions.sendKeys( key );
        actions.build().perform();
        sleep( 500 );

        return this;
    }

    public boolean isChecked()
    {
        return checkBox.getAttribute( "checked" ) != null;
    }
}
