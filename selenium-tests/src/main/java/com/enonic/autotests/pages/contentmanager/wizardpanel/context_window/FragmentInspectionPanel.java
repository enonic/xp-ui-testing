package com.enonic.autotests.pages.contentmanager.wizardpanel.context_window;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

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
        fragmentDropdownHandle.click();
        sleep( 400 );
        return this;
    }
}
