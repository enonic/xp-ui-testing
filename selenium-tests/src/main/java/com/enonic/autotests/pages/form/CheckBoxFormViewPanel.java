package com.enonic.autotests.pages.form;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

    private final String CHECBOX_CONTAINER = FORM_VIEW + "//div[contains(@id,'InputView')]//div[contains(@id,'Checkbox') and contains(@class,'checkbox')]";

    private final String CHECKBOX = CHECBOX_CONTAINER + "//input[@type='checkbox']";
    private final String CHECKBOX_LABEL = CHECBOX_CONTAINER + "//label";


    public CheckBoxFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        boolean checkboxValue = data.getBoolean( CHECKBOX_PROPERTY );
        if ( checkboxValue )
        {
            clickOnCheckBox();
        }
        //setChecked( checkboxValue );
        sleep( 300 );
        return this;
    }

    public FormViewPanel clickOnCheckBox()
    {
        findElement( By.xpath( CHECKBOX_LABEL ) ).click();
        return this;
    }

    public CheckBoxFormViewPanel setChecked( boolean checked )
    {
        WebElement checkbox = findElement( By.xpath( CHECKBOX ) );
        String id = checkbox.getAttribute( "id" );
        String script = String.format( "document.getElementById('%s').checked=%b", id, checked );
        //return libAdmin.store.map.get('ElementRegistry').elements.get(arguments[0]).checked=arguments[0]
        //String script = String.format( "window.api.dom.ElementRegistry.getElementById('%s')" + ".setChecked(%b)", id, checked );
        getJavaScriptExecutor().executeScript( script );
        sleep( 1000 );
        return this;
    }

    public boolean isChecked()
    {
        WebElement checkbox = findElement( By.xpath( CHECKBOX ) );
        String id = checkbox.getAttribute( "id" );
        String script = String.format( "return document.getElementById('%s').checked", id );
        return (Boolean) getJavaScriptExecutor().executeScript( script );
    }
}
