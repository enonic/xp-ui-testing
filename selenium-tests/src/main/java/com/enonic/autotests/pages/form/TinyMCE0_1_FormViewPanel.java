package com.enonic.autotests.pages.form;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class TinyMCE0_1_FormViewPanel
    extends BaseTinyMCEFormViewPanel
{
    private final String TINY_MCE_INNERHTML = "return document.getElementsByTagName('iframe')[0].contentDocument.body.innerHTML;";

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
            findElements( By.xpath( TINY_MCE ) ).get( 0 ).sendKeys( text );
            sleep( 300 );
        }

        return this;
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
        //getDriver().switchTo().window( getSession().getWindowHandle() );
        //NavigatorHelper.switchToIframe( getSession(), "//iframe[contains(@id,'api.ui.text.TextArea')]" );
        List<WebElement> frames = findElements( By.xpath( "//iframe[contains(@id,'api.ui.text.TextArea')]" ) );
        getDriver().switchTo().frame( frames.get( 0 ) );
//        boolean result = true;
        //Object obj = ( (JavascriptExecutor) getSession().getDriver() ).executeScript( TINY_MCE_INNERHTML );
        // String text = obj.toString();
        Object obj =
            ( (JavascriptExecutor) getSession().getDriver() ).executeScript( "return document.getElementById('tinymce').innerHTML" );
        String text = obj.toString();

        return text;
    }

    public String getTextFromEmptyTextArea()
    {
        return "<p><br data-mce-bogus=\"1\"></p>";
    }
}
