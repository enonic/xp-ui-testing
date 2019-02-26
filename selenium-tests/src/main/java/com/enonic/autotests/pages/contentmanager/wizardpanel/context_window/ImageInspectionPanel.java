package com.enonic.autotests.pages.contentmanager.wizardpanel.context_window;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 8/11/2017.
 */
public class ImageInspectionPanel
    extends Application
{
    private final String PANEL_CONTAINER = "//div[contains(@id,'ImageInspectionPanel')]";

    private final String FRAGMENT_DROPDOWN_HANDLE = PANEL_CONTAINER + DROP_DOWN_HANDLE_BUTTON;

    private final String IMAGE_SELECTED_OPTION_DISPLAY_NAME =
        PANEL_CONTAINER + "//div[contains(@id,'SelectedOptionView')]" + H6_DISPLAY_NAME;

    private final String IMAGE_DROPDOWN_OPTIONS = PANEL_CONTAINER + SLICK_ROW + H6_DISPLAY_NAME;

    private String FRAGMENT_DROPDOWN_OPTION_BY_DISPLAY_NAME = PANEL_CONTAINER + SLICK_ROW_BY_DISPLAY_NAME;

    private final String REMOVE_SELECTED_OPTION_BUTTON = PANEL_CONTAINER+"//a[@class='remove']";

    private final String IMAGE_OPTION_FILTER_INPUT = PANEL_CONTAINER + COMBOBOX_OPTION_FILTER_INPUT;

    @FindBy(xpath = FRAGMENT_DROPDOWN_HANDLE)
    protected WebElement imageDropdownHandle;

    public ImageInspectionPanel( final TestSession session )
    {
        super( session );
    }

    public String getSelectedImageDisplayName()
    {
        return getDisplayedString( IMAGE_SELECTED_OPTION_DISPLAY_NAME );
    }

    public List<String> getDropdownOptions()
    {
        return getDisplayedStrings( By.xpath( IMAGE_SELECTED_OPTION_DISPLAY_NAME ) );
    }

    public boolean isDropdownListExpanded()
    {
        return imageDropdownHandle.getAttribute( "class" ).contains( "down" );
    }

    public ImageInspectionPanel clickOnImageDropdownHandle()
    {
        imageDropdownHandle.click();
        sleep( 400 );
        return this;
    }

    public boolean isRemoveSelectedOptionButtonDisplayed()
    {
        return isElementDisplayed( REMOVE_SELECTED_OPTION_BUTTON );
    }

    public ImageInspectionPanel clickOnRemoveSelectedOptionButton()
    {
        getDisplayedElement( By.xpath( REMOVE_SELECTED_OPTION_BUTTON ) ).click();
        sleep( 500 );
        return this;
    }

    public ImageInspectionPanel clickOnDropdownOption( String fragmentDisplayName )
    {
        if ( !isDropdownListExpanded() )
        {
            saveScreenshot( "err_fragment_options" );
            throw new TestFrameworkException( "Drop Down options are not expanded!" );
        }
        String xpath = String.format( FRAGMENT_DROPDOWN_OPTION_BY_DISPLAY_NAME, fragmentDisplayName );
        getDisplayedElement( By.xpath( xpath ) ).click();
        return this;
    }

    public boolean isOptionFilterInputDisplayed()
    {
        return isElementDisplayed( By.xpath( IMAGE_OPTION_FILTER_INPUT ) );
    }

    public ImageInspectionPanel waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( PANEL_CONTAINER ), EXPLICIT_NORMAL ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err-open-inspection" ) );
            throw new TestFrameworkException( "Fragment Inspection Panel was was not opened!" );
        }
        return this;
    }
}
