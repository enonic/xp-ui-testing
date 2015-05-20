package com.enonic.autotests.pages.form;


import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class TinyMCE0_1_FormViewPanel
    extends BaseTinyMCEFormViewPanel
{
    public TinyMCE0_1_FormViewPanel( final TestSession session )
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
        List<WebElement> toolbars = findElements(
            By.xpath( "//div[contains(@id,'TinyMCE')]//div[contains(@class,'mce-toolbar') and @role='toolbar']" ) ).stream().filter(
            WebElement::isDisplayed ).collect( Collectors.toList() );
        return toolbars.size() > 0;
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
        List<WebElement> frames = findElements( By.xpath( TEXT_AREA ) );
        getDriver().switchTo().frame( frames.get( 0 ) );
        Object obj = ( (JavascriptExecutor) getSession().getDriver() ).executeScript( TEXT_IN_AREA_SCRIPT );
        String text = obj.toString();
        return text;
    }

    public boolean isTextAreaEmpty()
    {
        String actual = getText();
        return actual.equals( EMPTY_TEXT_AREA_CONTENT );
    }
}
