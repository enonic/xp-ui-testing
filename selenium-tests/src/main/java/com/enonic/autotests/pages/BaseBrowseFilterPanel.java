package com.enonic.autotests.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class BaseBrowseFilterPanel
    extends Application
{
    public static final String CLEAR_FILTER_LINK = "Clear"; //ClearFilterButton

    public static final String SEARCH_INPUT_XPATH =
        "//input[contains(@id,'api.app.browse.filter.TextSearchField') and contains(@class,'text-search-field')]";

    @FindBy(xpath = SEARCH_INPUT_XPATH)
    protected WebElement searchInput;

    public BaseBrowseFilterPanel( final TestSession session )
    {
        super( session );
    }

    /**
     * Clicks on link 'Clear Filter', located on the search panel.
     */
    public BaseBrowseFilterPanel clickOnCleanFilter()
    {
        boolean isVisible = waitUntilVisibleNoException( By.linkText( CLEAR_FILTER_LINK ), Application.EXPLICIT_QUICK );
        if ( !isVisible )
        {
            // getLogger().info( "The link with name 'Clear Filter' was not found!" );
            //throw new TestFrameworkException( "The link with name 'Clear Filter' was not found!" );
            return this;
        }
        findElements( By.linkText( CLEAR_FILTER_LINK ) ).get( 0 ).click();
        sleep( 2000 );
        return this;
    }

    /**
     * @param text
     */
    public BaseBrowseFilterPanel typeSearchText( String text )
    {
        boolean isVisible = waitUntilVisibleNoException( By.xpath( SEARCH_INPUT_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isVisible )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "filterinput" ) );
            throw new TestFrameworkException( "browse panel or search input not displayed" );
        }
        getLogger().info( "query will be applied : " + text );
        clearAndType( searchInput, text );
        //searchInput.sendKeys( text );
        searchInput.sendKeys( Keys.ENTER );
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
