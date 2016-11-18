package com.enonic.autotests.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public abstract class BaseBrowseFilterPanel
    extends Application
{
    public static String FILTER_PANEL_CONTAINER = "//div[contains(@id,'FilterPanel') and contains(@class,'filter-panel')]";

    public static final String CLEAR_FILTER_LINK = "Clear";

    public static final String CLEAR_FILTER_BUTTON = FILTER_PANEL_CONTAINER + "//a[contains(@id,'ClearFilterButton']";

    public static final String SEARCH_INPUT_XPATH =
        "//input[contains(@id,'api.app.browse.filter.TextSearchField') and contains(@class,'text-search-field')]";

    @FindBy(xpath = SEARCH_INPUT_XPATH)
    protected WebElement searchInput;

    public BaseBrowseFilterPanel( final TestSession session )
    {
        super( session );
    }

    public abstract BrowsePanel getBrowsePanel();

    public boolean waitIsFilterPanelDisplayed()
    {
        return waitUntilVisibleNoException( By.xpath( FILTER_PANEL_CONTAINER ), Application.EXPLICIT_NORMAL );
    }

    public boolean isFilterPanelDisplayed()
    {
        return isElementDisplayed( FILTER_PANEL_CONTAINER );
    }

    /**
     * Clicks on link 'Clear Filter', located on the search panel.
     */
    public BaseBrowseFilterPanel clickOnCleanFilter()
    {
        boolean isVisible = waitUntilVisibleNoException( By.linkText( CLEAR_FILTER_LINK ), Application.EXPLICIT_QUICK );
        if ( !isVisible )
        {
            return this;
        }
        findElement( By.linkText( CLEAR_FILTER_LINK ) ).click();
        sleep( 2000 );
        return this;
    }

    /**
     * @param text
     */
    public BaseBrowseFilterPanel typeSearchText( String text )
    {
        if ( !isFilterPanelDisplayed() )
        {
            getBrowsePanel().doShowFilterPanel();
        }
        boolean isVisible = waitUntilVisibleNoException( By.xpath( SEARCH_INPUT_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isVisible )
        {
            saveScreenshot( NameHelper.uniqueName( "err_filter" ) );
            throw new TestFrameworkException( "filter panel or search input not displayed" );
        }
        clearAndType( searchInput, text );
        sleep( 1000 );
        getLogger().info( "Filtered by : " + text );
        return this;
    }

    /**
     * Waits until link is visible.
     *
     * @return true if 'Clear Filter' link is present and visible, otherwise return false.
     */
    public boolean waitForClearFilterLinkVisible()
    {
        return waitUntilVisibleNoException( By.linkText( CLEAR_FILTER_LINK ), Application.EXPLICIT_QUICK );
    }

    /**
     * @return true if 'Clear Filter' link is not visible, otherwise return false.
     */
    public boolean waitForClearFilterLinkNotVisible()
    {
        return waitsElementNotVisible( By.linkText( CLEAR_FILTER_LINK ), Application.EXPLICIT_QUICK );
    }
}
