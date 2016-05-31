package com.enonic.autotests.pages.form;


import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class HtmlArea0_1_FormViewPanel
    extends BaseHtmlAreaFormViewPanel
{
    public HtmlArea0_1_FormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String text = data.getString( STRING_PROPERTY );
        if ( text != null )
        {
            Actions builder = new Actions( getDriver() );
            builder.click( findElement( By.xpath( TINY_MCE ) ) ).build().perform();
            findElements( By.xpath( TINY_MCE ) ).get( 0 ).sendKeys( text );
            sleep( 300 );
        }
        return this;
    }

    public boolean isEditorToolbarVisible()
    {
        return isElementDisplayed( "//div[contains(@id,'HtmlArea')]//div[contains(@class,'mce-toolbar')]" );
    }

    public boolean isEditorPresent()
    {
        return findElements( By.xpath( TINY_MCE ) ).size() > 0;
    }

    public boolean isOpened()
    {
        return waitUntilVisibleNoException( By.xpath( STEP_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public String getText()
    {
        return getTextFromArea( findElement( By.xpath( TEXT_AREA ) ) );
    }

    public boolean isTextAreaEmpty()
    {
        String actual = getTextFromArea( findElement( By.xpath( TEXT_AREA ) ) );
        return actual.equals( EMPTY_TEXT_AREA_CONTENT );
    }
}
