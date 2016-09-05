package com.enonic.autotests.pages;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;


public class RichComboBoxInput
    extends Application
{
    public String RICH_COMBOBOX_OPTIONS_ITEM_BY_DISPLAY_NAME =
        "//div[@class='slick-viewport']//div[contains(@id,'NamesView')]//h6[text()='%s']";

    public RichComboBoxInput( final TestSession session )
    {
        super( session );
    }

    public void selectOption( String option )
    {
        String optionXpath = String.format( RICH_COMBOBOX_OPTIONS_ITEM_BY_DISPLAY_NAME, option );
        boolean isVisible = waitUntilVisibleNoException( By.xpath( optionXpath ), Application.EXPLICIT_NORMAL );

        if ( !isVisible )
        {
            throw new TestFrameworkException( "option was not found! " + option );
        }
        getDisplayedElement( By.xpath( optionXpath ) ).click();
    }
}
