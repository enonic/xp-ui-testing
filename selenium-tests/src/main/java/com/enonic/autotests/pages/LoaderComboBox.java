package com.enonic.autotests.pages;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class LoaderComboBox
    extends Application
{
    public String CONTAINER = "//div[contains(@id,'LoaderComboBox')]";

    public String LOADER_COMBOBOX_OPTIONS_ITEM_BY_DISPLAY_NAME = CONTAINER + SLICK_ROW_BY_DISPLAY_NAME;

    public String LOADER_COMBOBOX_OPTION_DISPLAY_NAMES = "//div[@class='slick-viewport']" + H6_DISPLAY_NAME;

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
            throw new TestFrameworkException( "option was not found! " + optionXpath );
        }
        getDisplayedElement( By.xpath( optionXpath ) ).click();
    }

    public void selectOption( String option )
    {
        selectOption( option, null );
    }

    public List<String> getOptionDisplayNames()
    {
        return getDisplayedStrings( By.xpath( LOADER_COMBOBOX_OPTION_DISPLAY_NAMES ) );
    }
}
