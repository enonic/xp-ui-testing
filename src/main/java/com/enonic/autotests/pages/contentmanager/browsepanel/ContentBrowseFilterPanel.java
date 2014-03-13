package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentFilterException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentBrowseFilterPanel
    extends Application
{
    private final String CLEAR_FILTER_LINK = "Clear filter";

    public static final String SEARCH_INPUT_XPATH = "//input[@class='text-search-field']";

    @FindBy(xpath = SEARCH_INPUT_XPATH)
    private WebElement searchInput;

    private String CONTENT_TYPE_FILTER_ITEM =
        "//div[@class='aggregation-group-view']/h2[text()='Content Types']/..//div[@class='aggregation-bucket-view' and child::label[contains(.,'%s')]]//label";

    private String LAST_MODIFIED_FILTER_ITEM =
        "//div[@class='aggregation-group-view']/h2[text()='Last Modified']/..//div[@class='aggregation-bucket-view' and child::label[contains(.,'%s')]]//label";


    /**
     * The constructor
     *
     * @param session
     */
    public ContentBrowseFilterPanel( TestSession session )
    {
        super( session );

    }

    /**
     * @param text
     */
    public void typeSearchText( String text )
    {
        getLogger().info( "query will be applied : " + text );
        searchInput.sendKeys( text );
        searchInput.sendKeys( Keys.ENTER );
        sleep( 1000 );
        getLogger().info( "Filtered by : " + text );
    }

    /**
     * Clicks by range of date: '1 hour' or  '1 day' or '1 week'.
     *
     * @param date
     */
    public void selectEntryInLastModifiedFilter( FilterPanelLastModified date )
    {
        String rangeXpath = String.format( LAST_MODIFIED_FILTER_ITEM, date.getValue() );
        boolean isVisible = waitUntilVisibleNoException( By.xpath( rangeXpath ), 1l );
        if ( !isVisible )
        {
            getLogger().info( "range was not found: " + date.getValue() );
            throw new TestFrameworkException( "The link with name 'Clear Filter' was not found!" );
        }
        getDriver().findElement( By.xpath( rangeXpath ) ).click();
        sleep( 500 );
        getLogger().info( "Filtered by : " + date.getValue() );
    }

    /**
     * Clicks by link 'Clear Filter', located on the search panel.
     */
    public void clickByCleanFilter()
    {
        boolean isVisible = waitUntilVisibleNoException( By.linkText( CLEAR_FILTER_LINK ), Application.REFRESH_TIMEOUT );
        if ( !isVisible )
        {
            getLogger().info( "The link with name 'Clear Filter' was not found!" );
            throw new TestFrameworkException( "The link with name 'Clear Filter' was not found!" );
        }
        getDriver().findElement( By.linkText( CLEAR_FILTER_LINK ) ).click();
        sleep( 500 );
    }

    /**
     * @return true if there any any selected entries in the ContentBrowseFilterPanel, otherwise false.
     */
    public boolean isAnySelectionPresent()
    {
        JavascriptExecutor executor = (JavascriptExecutor) getSession().getDriver();
        return (Boolean) executor.executeScript(
            "return window.api.dom.ElementRegistry.getElementById('app.browse.filter.ContentBrowseFilterPanel').hasFilterSet()" );

    }

    /**
     * Waits until link is visible.
     *
     * @return true if 'Clear Filter' link is present and visible, otherwise return false.
     */
    public boolean waitForClearFilterLinkVisible()
    {
        return waitUntilVisibleNoException( By.linkText( CLEAR_FILTER_LINK ), Application.REFRESH_TIMEOUT );
    }

    /**
     * @return true if 'Clear Filter' link is not visible, otherwise return false.
     */
    public boolean waitForClearFilterLinkNotvisible()
    {
        return waitsElementNotVisible( By.linkText( CLEAR_FILTER_LINK ), Application.REFRESH_TIMEOUT );
    }

    /**
     * Select a content type on the search panel and filter contents.
     *
     * @param contentTypeName
     */
    public String selectEntryInContentTypesFilter( String contentTypeName )
    {
        String itemXpath = String.format( CONTENT_TYPE_FILTER_ITEM, contentTypeName );
        List<WebElement> elems = getDriver().findElements( By.xpath( itemXpath ) );
        if ( elems.size() == 0 )
        {
            logError( "content type was not found in the search panel:" + contentTypeName );
            throw new ContentFilterException( "content type was not found in the search panel:" + contentTypeName );
        }
        else
        {
            elems.get( 0 ).click();
        }
        waitsForSpinnerNotVisible();
        return elems.get( 0 ).getText();
    }

    /**
     * @param contentTypeName
     * @return
     */
    public Integer getContentTypeFilterCount( String contentTypeName )
    {
        String itemXpath = String.format( CONTENT_TYPE_FILTER_ITEM, contentTypeName );
        List<WebElement> elems = getDriver().findElements( By.xpath( itemXpath ) );
        if ( elems.size() == 0 )
        {
            return null;
        }
        if ( !elems.get( 0 ).isDisplayed() )
        {
            return 0;
        }
        return TestUtils.parseFilterLabel( elems.get( 0 ).getText() );
    }

    public boolean isFilterEntryDisplayed()
    {
        return true;
    }

    public List<String> getContentTypeSelectedValues()
    {
        JavascriptExecutor executor = (JavascriptExecutor) getSession().getDriver();
        List list = (ArrayList) executor.executeScript(
            "return window.api.dom.ElementRegistry.getElementById('app.browse.filter.ContentBrowseFilterPanel').getSearchInputValues().getSelectedValuesForAggregationName('contentTypes')" );
        Iterator it = list.iterator();
        String value = null;
        ArrayList<String> result = new ArrayList<>();
        while ( it.hasNext() )
        {
            Map element = (Map) it.next();
            value = (String) ( element ).get( "key" );
            result.add( value );
        }
        return result;
    }

    /**
     * @param filterItem
     * @return
     */
    public Integer getLastModifiedCount( String filterItem )
    {
        String itemXpath = String.format( LAST_MODIFIED_FILTER_ITEM, filterItem );
        List<WebElement> elems = getDriver().findElements( By.xpath( itemXpath ) );
        if ( elems.size() == 0 )
        {
            return null;
        }
        if ( !elems.get( 0 ).isDisplayed() )
        {
            return 0;
        }
        return TestUtils.parseFilterLabel( elems.get( 0 ).getText() );
    }

    public List<String> getSpaceNames()
    {
        return null;
    }

}
