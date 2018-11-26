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
 * Created on 8/10/2017.
 */
public class FragmentInspectionPanel
    extends Application
{
    private final String PANEL_CONTAINER = "//div[contains(@id,'FragmentInspectionPanel')]";

    private final String FRAGMENT_DROPDOWN_HANDLE = PANEL_CONTAINER + DROP_DOWN_HANDLE_BUTTON;

    private final String FRAGMENT_SELECTED_OPTION_DISPLAY_NAME =
        PANEL_CONTAINER + "//div[contains(@id,'SelectedOptionView')]" + H6_DISPLAY_NAME;

    private final String FRAGMENT_DROPDOWN_OPTIONS = PANEL_CONTAINER + SLICK_ROW + H6_DISPLAY_NAME;

    private String FRAGMENT_DROPDOWN_OPTION_BY_DISPLAY_NAME = PANEL_CONTAINER + SLICK_ROW_BY_DISPLAY_NAME;

    @FindBy(xpath = FRAGMENT_DROPDOWN_HANDLE)
    protected WebElement fragmentDropdownHandle;

    public FragmentInspectionPanel( final TestSession session )
    {
        super( session );
    }

    public String getSelectedFragmentDisplayName()
    {
        return getDisplayedString( FRAGMENT_SELECTED_OPTION_DISPLAY_NAME );
    }

    public List<String> getDropdownOptions()
    {
        return getDisplayedStrings( By.xpath( FRAGMENT_DROPDOWN_OPTIONS ) );
    }

    public boolean isDropdownListExpanded()
    {
        return fragmentDropdownHandle.getAttribute( "class" ).contains( "down" );
    }

    public FragmentInspectionPanel clickOnFragmentDropdownHandle()
    {
        waitUntilVisibleNoException( By.xpath( FRAGMENT_DROPDOWN_HANDLE ), Application.EXPLICIT_NORMAL );
        fragmentDropdownHandle.click();
        sleep( 900 );
        return this;
    }

    public FragmentInspectionPanel clickOnDropdownOption( String fragmentDisplayName )
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

    public FragmentInspectionPanel waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( PANEL_CONTAINER ), EXPLICIT_NORMAL ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err-open-inspection" ) );
            throw new TestFrameworkException( "Fragment Inspection Panel was was not opened!" );
        }
        return this;
    }
}
