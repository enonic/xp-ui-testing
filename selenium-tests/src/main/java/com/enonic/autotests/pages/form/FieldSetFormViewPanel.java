package com.enonic.autotests.pages.form;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class FieldSetFormViewPanel
    extends FormViewPanel
{
    public static String TEXT_LINE_VALUE = "text_line";

    public static String HTML_AREA_VALUE = "html_area";

    public static String DOUBLES_VALUE = "double_values";

    private final String TEXT_LINE = FORM_VIEW + "//input[contains(@id,'TextInput')]";

    @FindBy(xpath = TEXT_LINE)
    private WebElement textLineInput;

    private DoubleFormViewPanel doubleFormViewPanel;

    public FieldSetFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        typeTextLine( data.getString( TEXT_LINE_VALUE ) );
        typeTextInHtmlArea( data.getString( HTML_AREA_VALUE ) );
        doubleFormViewPanel = new DoubleFormViewPanel( getSession() );
        doubleFormViewPanel.type( data );
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
            WebElement areaElement = findElement( By.xpath( CKE_HTML_AREA ) );
            setTextInCKE( areaElement.getAttribute( "id" ), text );
            sleep( 300 );
        }
        return this;
    }

    public String getTextLineValue()
    {
        return textLineInput.getAttribute( "value" );
    }
}
