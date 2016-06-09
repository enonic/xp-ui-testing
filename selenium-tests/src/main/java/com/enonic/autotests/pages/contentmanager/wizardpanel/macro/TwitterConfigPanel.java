package com.enonic.autotests.pages.contentmanager.wizardpanel.macro;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

public class TwitterConfigPanel
    extends MacroConfigPanel
{
    public static final String URL_VALUE = "url_value";

    public static final String LANG_VALUE = "lang_value";

    private final String URL_INPUT = CONFIG_PANEL + "//input[contains(@name,'url')]";

    private final String LANGUAGE_INPUT = CONFIG_PANEL + "//input[contains(@name,'lang')]";

    @FindBy(xpath = URL_INPUT)
    private WebElement urlInput;

    @FindBy(xpath = LANGUAGE_INPUT)
    private WebElement langInput;

    public TwitterConfigPanel( final TestSession session )
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
        String lang = data.getString( LANG_VALUE );
        if ( lang != null )
        {
            clearAndType( langInput, lang );
        }

    }
}
