package com.enonic.autotests.pages.contentmanager.wizardpanel.macro;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;


public class YoutubeConfigPanel
    extends MacroConfigPanel
{
    public static final String URL_VALUE = "url_value";

    private final String URL_INPUT = CONFIG_PANEL + "//input[contains(@name,'url')]";

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
}
