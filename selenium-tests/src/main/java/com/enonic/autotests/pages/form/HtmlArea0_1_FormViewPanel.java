package com.enonic.autotests.pages.form;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
            WebElement htmlArea = findElement( By.xpath( TINY_MCE ) );
            buildActions().click( htmlArea ).build().perform();
            sleep( 500 );
            setTextIntoArea( htmlArea.getAttribute( "id" ), text );
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

    public String getInnerHtml()
    {
        return getInnerHtmlFromArea( findElement( By.xpath( TEXT_AREA ) ) );
    }

    public String getInnerText()
    {
        return getInnerTextFromArea( findElement( By.xpath( TEXT_AREA ) ) );
    }
}
