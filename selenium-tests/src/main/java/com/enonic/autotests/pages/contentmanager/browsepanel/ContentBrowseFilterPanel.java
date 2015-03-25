package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentFilterException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BaseBrowseFilterPanel;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentBrowseFilterPanel
    extends BaseBrowseFilterPanel
{
//    private final String CLEAR_FILTER_LINK = "Clear filter";
//
//    public static final String SEARCH_INPUT_XPATH =
//        "//input[contains(@id,'api.app.browse.filter.TextSearchField') and contains(@class,'text-search-field')]";
//
//    @FindBy(xpath = SEARCH_INPUT_XPATH)
//    private WebElement searchInput;

    private String CONTENT_TYPE_FILTER_ITEM =
        "//div[@class='aggregation-group-view']/h2[text()='Content Types']/..//div[@class='checkbox form-input' and child::label[contains(.,'%s')]]//label";

    private String LAST_MODIFIED_FILTER_ITEM =
        "//div[@class='aggregation-group-view']/h2[text()='Last Modified']/..//div[@class='checkbox form-input' and child::label[contains(.,'%s')]]//label";

    private String LAST_MODIFIED_FILTER_ENTRY =
        "//div[@class='aggregation-group-view']/h2[text()='Last Modified']/..//div[@class='checkbox form-input' and child::label]//label[contains(.,'%s')]";

    public enum ContentTypeDisplayNames
    {
        FOLDER( "Folder" ), DATA( "Data" ), STRUCTURED( "Structured" ), UNSTRUCTURED( "Unstructured" );

        private String value;

        ContentTypeDisplayNames( String value )
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }

    /**
     * The constructor
     *
     * @param session
     */
    public ContentBrowseFilterPanel( TestSession session )
    {
        super( session );

    }

    public List<String> getAllContentTypesFilterEntries()
    {
        boolean result =
            waitUntilVisibleNoException( By.xpath( "//input[@type='checkbox']/label(contains(.,'Folder'))" ), Application.EXPLICIT_NORMAL );
        // we should wait it because, when BrowsePanel not loaded, all labels on FilterPanel are in lower case.
        if ( !result )
        {
            getLogger().info( "The 'Folder' filter not present in FilterPanel" );
        }
        getLogger().info( "The 'Folder' filter not present in FilterPanel" );

        List<WebElement> elements = getDriver().findElements( By.xpath(
            "//div[@class='aggregation-group-view']/h2[text()='Content Types']/..//div[@class='aggregation-bucket-view' and descendant::label]//label" ) );
        List<String> labels = new ArrayList<>();
        for ( WebElement el : elements )
        {
            if ( el.isDisplayed() )
            {
                labels.add( el.getText() );
            }
        }
        return labels;
    }

    public List<String> getAllLastModifiedFilterEntries()
    {
        List<WebElement> elements = getDriver().findElements( By.xpath(
            "//div[@class='aggregation-group-view']/h2[text()='Last Modified']/..//div[@class='aggregation-bucket-view' and child::label]//label" ) );
        List<String> labels = new ArrayList<>();
        for ( WebElement el : elements )
        {
            labels.add( el.getText() );
        }
        return labels;
    }

    public Integer getContentNumberFilteredByLastModified( FilterPanelLastModified entry )
    {
        TestUtils.saveScreenshot( getSession(), "lastmodified" );
        String xpath = String.format( LAST_MODIFIED_FILTER_ENTRY, entry.getValue() );
        List<WebElement> elements = getDriver().findElements( By.xpath( xpath ) );
        if ( elements.size() == 0 )
        {
            getLogger().info( "entry was not found:: " + entry.getValue() );
            return null;
        }
        if ( !elements.get( 0 ).isDisplayed() )
        {
            return 0;
        }
        else
        {
            return TestUtils.getNumberFromFilterLabel( elements.get( 0 ).getText() );
        }
    }

    /**
     * @param text
     */
    public ContentBrowseFilterPanel typeSearchText( String text )
    {
        getLogger().info( "query will be applied : " + text );
        searchInput.sendKeys( text );
        searchInput.sendKeys( Keys.ENTER );
        sleep( 1000 );
        getLogger().info( "Filtered by : " + text );
        return this;
    }

    public String getSearchText()
    {
        return searchInput.getAttribute( "value" );
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
     * Clicks on link 'Clear Filter', located on the search panel.
     */
    public void clickOnCleanFilter()
    {
        boolean isVisible = waitUntilVisibleNoException( By.linkText( CLEAR_FILTER_LINK ), Application.EXPLICIT_QUICK );
        if ( !isVisible )
        {
            getLogger().info( "The link with name 'Clear Filter' was not found!" );
            throw new TestFrameworkException( "The link with name 'Clear Filter' was not found!" );
        }
        findElements( By.linkText( CLEAR_FILTER_LINK ) ).get( 0 ).click();
        sleep( 2000 );
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
        return waitUntilVisibleNoException( By.linkText( CLEAR_FILTER_LINK ), Application.EXPLICIT_QUICK );
    }

    /**
     * @return true if 'Clear Filter' link is not visible, otherwise return false.
     */
    public boolean waitForClearFilterLinkNotvisible()
    {
        return waitsElementNotVisible( By.linkText( CLEAR_FILTER_LINK ), Application.EXPLICIT_QUICK );
    }

    /**
     * Select a content type on the search panel and filter contents.
     *
     * @param contentTypeDisplayName
     */
    public String selectEntryInContentTypesFilter( String contentTypeDisplayName )
    {
        String itemXpath = String.format( CONTENT_TYPE_FILTER_ITEM, contentTypeDisplayName );
        WebElement element = getDynamicElement( By.xpath( itemXpath ), 2 );
        if ( element == null )
        {
            TestUtils.saveScreenshot( getSession(), contentTypeDisplayName );
            logError( "content type was not found in the search panel:" + contentTypeDisplayName );
            throw new ContentFilterException( "content type was not found in the search panel:" + contentTypeDisplayName );
        }
        else
        {

            waitsForSpinnerNotVisible();
            if ( !isSelectedEntryInFilter( contentTypeDisplayName ) )
            {
                element.click();
                waitsForSpinnerNotVisible();
            }
        }

        return getDynamicElement( By.xpath( itemXpath ), 2 ).getText();
    }

    public String deselectEntryInContentTypesFilter( String contentTypeDisplayName )
    {
        String itemXpath = String.format( CONTENT_TYPE_FILTER_ITEM, contentTypeDisplayName );
        WebElement element = getDynamicElement( By.xpath( itemXpath ), 2 );
        if ( element == null )
        {
            logError( "content type was not found in the search panel:" + contentTypeDisplayName );
            throw new ContentFilterException( "content type was not found in the search panel:" + contentTypeDisplayName );
        }
        else
        {

            element.click();


        }
        waitsForSpinnerNotVisible();
        return getDynamicElement( By.xpath( itemXpath ), 2 ).getText();
    }

    /**
     * @param contentTypeName
     * @return
     */
    public Integer getNumberFilteredByContentType( String contentTypeName )
    {
        String itemXpath = String.format( CONTENT_TYPE_FILTER_ITEM, contentTypeName );
        List<WebElement> elems = getDriver().findElements( By.xpath( itemXpath ) );
        if ( elems.size() == 0 )
        {
            return 0;
        }
        if ( !elems.get( 0 ).isDisplayed() )
        {
            return 0;
        }
        return TestUtils.getNumberFromFilterLabel( elems.get( 0 ).getText() );
    }

    boolean isSelectedEntryInFilter( String contentTypeDisplayName )
    {
        return getSelectedValuesForContentTypesFilter().contains( contentTypeDisplayName.toLowerCase() );
    }

    public List<String> getSelectedValuesForContentTypesFilter()
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
        return TestUtils.getNumberFromFilterLabel( elems.get( 0 ).getText() );
    }

}
