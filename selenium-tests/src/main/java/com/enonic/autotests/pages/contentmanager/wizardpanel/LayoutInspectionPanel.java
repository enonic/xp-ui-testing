package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created  on 13.01.2017.
 */
public class LayoutInspectionPanel
    extends Application
{

    private final String CONTAINER = "//div[contains(@id,'LayoutInspectionPanel')]";

    private final String LAYOUT_DESCRIPTOR_DROPDOWN = CONTAINER + "//div[contains(@id,'LayoutDescriptorDropdown')]";

    private final String LAYOUT_DROPDOWN_HANDLER = LAYOUT_DESCRIPTOR_DROPDOWN + "//button[contains(@id,'DropdownHandle')]";

    public LayoutInspectionPanel( final TestSession session )
    {
        super( session );
    }

    public LayoutInspectionPanel selectNewLayout( String layoutDisplayName )
    {
        if ( !isElementDisplayed( LAYOUT_DROPDOWN_HANDLER ) )
        {
            saveScreenshot( "err_dropdown_layout_selector" );
            throw new TestFrameworkException( "dropdown handler was not found!  " + layoutDisplayName );
        }
        getDisplayedElement( By.xpath( LAYOUT_DROPDOWN_HANDLER ) ).click();
        sleep( 300 );
        String optionItemXpath = LAYOUT_DESCRIPTOR_DROPDOWN + SLICK_CELL + String.format( NAMES_VIEW_BY_DISPLAY_NAME, layoutDisplayName );
        if ( !isElementDisplayed( optionItemXpath ) )
        {
            saveScreenshot( "err_inspection_layout" );
            throw new TestFrameworkException( "option was not found!  " + layoutDisplayName );
        }
        getDisplayedElement( By.xpath( optionItemXpath ) ).click();
        sleep( 1000 );
        return this;
    }

}
