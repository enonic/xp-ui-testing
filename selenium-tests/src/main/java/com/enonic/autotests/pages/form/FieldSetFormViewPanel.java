package com.enonic.autotests.pages.form;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class FieldSetFormViewPanel
    extends FormViewPanel
{
    public static String TEXT_LINE_VALUE = "text_line";

    public static String HTML_AREA_VALUE = "html_area";

    public static String DOUBLES_VALUE = "double_values";

    private final String TEXT_LINE = FORM_VIEW + "//input[contains(@id,'TextInput')]";

    private final String HTML_AREA = FORM_VIEW + "//div[contains(@id,'TextLine')]";

    protected final String TINY_MCE = FORM_VIEW + "//div[contains(@class,'mce-edit-area')]//iframe[contains(@id,'TextArea')]";

    private final String SET_TINY_MCE_INNERHTML = "document.getElementById(arguments[0]).contentDocument.body.innerHTML=arguments[1];";

    @FindBy(xpath = TEXT_LINE)
    private WebElement textLineInput;


    public FieldSetFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        typeTextLine( data.getString( TEXT_LINE_VALUE ) );
        typeTextInHtmlArea( data.getString( HTML_AREA_VALUE ) );
        return this;
    }

    public FieldSetFormViewPanel typeTextLine( String text )
    {
        clearAndType( textLineInput, text );
        return this;
    }

    public FieldSetFormViewPanel typeTextInHtmlArea( String text )
    {
        if ( text != null )
        {
            WebElement areaElement = findElement( By.xpath( TINY_MCE ) );
            Actions builder = new Actions( getDriver() );
            builder.click( findElement( By.xpath( TINY_MCE ) ) ).build().perform();
            setTextIntoArea( areaElement.getAttribute( "id" ), text );
            sleep( 300 );
        }
        return this;
    }

    private void setTextIntoArea( String id, String text )
    {
        ( (JavascriptExecutor) getSession().getDriver() ).executeScript( SET_TINY_MCE_INNERHTML, id, text );
    }

    public String getTextLineValue()
    {
        return textLineInput.getAttribute( "value" );
    }

    public boolean isDoubleInputValid( WebElement doubleInput )
    {
        return !waitAndCheckAttrValue( doubleInput, "class", "invalid", Application.EXPLICIT_NORMAL );
    }
}
