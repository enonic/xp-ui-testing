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
            WebElement htmlArea =
                findElement( By.xpath( "//div[contains(@id,'api.form.FormView')]//textarea[contains(@id,'api.ui.text.TextArea')]" ) );
            sleep( 500 );
            setTextInCKE( htmlArea.getAttribute( "id" ), text );
            sleep( 500 );
        }
        return this;
    }

    public boolean isEditorToolbarVisible()
    {
        return isElementDisplayed( "//span[@class='cke_toolbar']" );
    }

    public boolean isOpened()
    {
        return waitUntilVisibleNoException( By.xpath( STEP_XPATH ), Application.EXPLICIT_NORMAL );
    }


    public String getTextInCKE()
    {
        WebElement editor =
            findElement( By.xpath( "//div[contains(@id,'api.form.FormView')]"+TEXT_AREA_INPUT ) );
        return getCKEData( editor.getAttribute( "id" ) );
    }
}
