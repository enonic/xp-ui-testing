package com.enonic.autotests.pages.contentmanager.wizardpanel.macro;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;


public class YoutubeConfigPanel
    extends MacroConfigPanel
{
    public static final String URL_VALUE = "url_value";

    private final String URL_INPUT = CONFIG_PANEL + "//input[contains(@name,'url')]";

    private String URL_INPUT_VIEW = CONFIG_PANEL + "//div[contains(@id,'InputView')][1]";

    private final String VALIDATION_MESSAGE = CONFIG_PANEL + "//div[contains(@id,'ValidationRecordingViewer')]//li";

    @FindBy(xpath = URL_INPUT)
    private WebElement urlInput;

    public YoutubeConfigPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public void typeData( final PropertyTree data )
    {
        String url = data.getString( URL_VALUE );
        if ( url != null )
        {
            clearAndType( urlInput, url );
        }
    }

    public boolean isUrlInputInvalid()
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
            return getDisplayedString( VALIDATION_MESSAGE );
        }
        return null;
    }

    public boolean isValidationMessagePresent()
    {
        return isElementDisplayed( VALIDATION_MESSAGE );
    }
}
