package com.enonic.autotests.pages.form;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class TextAreaFormViewPanel
    extends FormViewPanel
{
    @FindBy(xpath = FORM_VIEW + "//textarea[@class='text-area form-input']")
    private WebElement textArea;

    public TextAreaFormViewPanel( final TestSession session )
    {
        super( session );
    }


    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String text = data.getString( "text" );
        if ( text != null )
        {
            textArea.sendKeys( text );
            sleep( 300 );
        }

        return this;
    }

    public boolean isTextAreaDisplayed()
    {
        return textArea.isDisplayed() && textArea.isEnabled();
    }

    public String getTextAreaValue()
    {
        String id = textArea.getAttribute( "id" );
        JavascriptExecutor executor = (JavascriptExecutor) getSession().getDriver();
        String text = (String) executor.executeScript( String.format( ELEMENT_BY_ID, id ) + ".getValue()" );
        return text;
    }

}