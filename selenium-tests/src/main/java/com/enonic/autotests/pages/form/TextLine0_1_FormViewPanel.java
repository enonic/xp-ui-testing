package com.enonic.autotests.pages.form;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;

public class TextLine0_1_FormViewPanel
    extends BaseTextLineFormViewPanel
{
    public static String TEXT_INPUT_PROPERTY = "textinput0:1";

    private final String TEXT_INPUT_XPATH = FORM_VIEW + "//input[contains(@name,'TextLine_0_1')]";

    public TextLine0_1_FormViewPanel( final TestSession session )
    {
        super( session );
    }

    @FindBy(xpath = TEXT_INPUT_XPATH)
    private WebElement textInput;


    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        List<WebElement> inputs = findElements( By.xpath( TEXT_INPUT_XPATH ) );
        if ( inputs.size() == 0 )
        {
            throw new TestFrameworkException( "text input was not found" );
        }
        String text = data.getString( TEXT_INPUT_PROPERTY );
        if ( text != null )
        {
            textInput.sendKeys( text );
        }
        else
        {
            getLogger().info( "TextLine1:1 - there are no ane text data for typing in the TexTline" );
        }

        return this;
    }

    public String getTextLineValue()

    {
        return textInput.getAttribute( "value" );
    }
}


