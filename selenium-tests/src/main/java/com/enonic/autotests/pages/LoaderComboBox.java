package com.enonic.autotests.pages;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class LoaderComboBox
    extends Application
{
    public String LOADER_COMBOBOX_OPTIONS_ITEM_BY_DISPLAY_NAME =
        "//div[@class='slick-viewport']//div[contains(@id,'NamesView')]//h6[text()='%s']";

    public LoaderComboBox( final TestSession session )
    {
        super( session );
    }

    public void selectOption( String option, String div )
    {
        String optionXpath = null;
        if ( StringUtils.isEmpty( div ) )
        {
            optionXpath = String.format( LOADER_COMBOBOX_OPTIONS_ITEM_BY_DISPLAY_NAME, option );
        }
        else
        {
            optionXpath = String.format( div + LOADER_COMBOBOX_OPTIONS_ITEM_BY_DISPLAY_NAME, option );
        }

        boolean isVisible = waitUntilVisibleNoException( By.xpath( optionXpath ), Application.EXPLICIT_NORMAL );
        sleep( 400 );
        if ( !isVisible )
        {
            saveScreenshot( NameHelper.uniqueName( "err_option" ) );
            throw new TestFrameworkException( "option was not found! " + option );
        }
        getDisplayedElement( By.xpath( optionXpath ) ).click();
    }

    public void selectOption( String option )
    {
        selectOption( option, null );
    }
}
