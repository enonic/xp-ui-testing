package com.enonic.autotests.pages.contentmanager.wizardpanel.macro;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;


public class TextAreaConfigPanel
    extends MacroConfigPanel
{
    public static final String TEXT_AREA_VALUE = "textarea_value";

    private final String CODE_TEXT_AREA = CONFIG_PANEL + "//textarea[contains(@id,'TextArea')]";

    protected final String FORM_VALIDATION_VEW = "//div[contains(@id,'InputViewValidationViewer')]";

    private String URL_INPUT_VIEW = CONFIG_PANEL + "//div[contains(@id,'InputView')][1]";

    @FindBy(xpath = CODE_TEXT_AREA)
    protected WebElement codeTextArea;

    public TextAreaConfigPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public void typeData( final PropertyTree data )
    {
        String text = data.getString( TEXT_AREA_VALUE );
        if ( text != null )
        {
            clearAndType( codeTextArea, text );
        }
    }

    public boolean isTextAreaInvalid()
    {
        if ( !isElementDisplayed( URL_INPUT_VIEW ) )
        {
            throw new TestFrameworkException( "URL view not found!" );
        }
        return getDisplayedElement( By.xpath( URL_INPUT_VIEW ) ).getAttribute( "class" ).contains( "invalid" );
    }

    public String getValidationMessage()
    {
        if ( isValidationMessagePresent() )
        {
            return getDisplayedString( FORM_VALIDATION_VEW );
        }
        return null;
    }

    public boolean isValidationMessagePresent()
    {
        return isElementDisplayed( FORM_VALIDATION_VEW );
    }

}

