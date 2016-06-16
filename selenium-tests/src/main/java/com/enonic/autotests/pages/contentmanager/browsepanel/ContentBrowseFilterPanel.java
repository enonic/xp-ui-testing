package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentFilterException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.BaseBrowseFilterPanel;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentBrowseFilterPanel
    extends BaseBrowseFilterPanel
{
    private String FILTER_PANEL_DIV = "//div[contains(@class,'filter-panel')]";

    private final String CONTENT_TYPE_AGGREGATION_VIEW = "//div[contains(@id,'ContentTypeAggregationGroupView')]";

    private String CONTENT_TYPE_AGGREGATION_ITEM_BY_NAME =
        CONTENT_TYPE_AGGREGATION_VIEW + "//div[@class='checkbox form-input' and child::label[contains(.,'%s')]]//label";

    private String CONTENT_TYPE_AGGREGATION_ITEMS = CONTENT_TYPE_AGGREGATION_VIEW + "//div[@class='checkbox form-input']//label";

    private String LAST_MODIFIED_AGGREGATION_ENTRY_BY_NAME =
        "//div[@class='aggregation-group-view']/h2[text()='Last Modified']/..//div[@class='checkbox form-input' and child::label]//label[contains(.,'%s')]";

    public enum ContentTypeDisplayNames
    {
        FOLDER( "Folder" ), SITE( "Site" ), SHORTCUT( "Shortcut" ), STRUCTURED( "Structured" ), UNSTRUCTURED( "Unstructured" );

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

    @Override
    public BrowsePanel getBrowsePanel()
    {
        return new ContentBrowsePanel( getSession() );
    }

    public List<String> getContentTypesAggregationValues()
    {
        sleep( 500 );
        return getDisplayedStrings( By.xpath( CONTENT_TYPE_AGGREGATION_ITEMS ) );
    }

    /**
     * @param contentTypeName
     * @return
     */
    public Integer getNumberAggregatedByContentType( String contentTypeName )
    {
        String itemXpath = String.format( CONTENT_TYPE_AGGREGATION_ITEM_BY_NAME, contentTypeName );
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

    public Integer getNumberAggregatedByLastModified( FilterPanelLastModified aggregation )
    {
        String xpath = String.format( LAST_MODIFIED_AGGREGATION_ENTRY_BY_NAME, aggregation.getValue() );
        List<WebElement> elements = findElements( By.xpath( xpath ) );
        if ( elements.size() == 0 )
        {
            getLogger().info( "entry was not found:: " + aggregation.getValue() );
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
        String rangeXpath = String.format( LAST_MODIFIED_AGGREGATION_ENTRY_BY_NAME, date.getValue() );
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
     * @return true if there any any selected entries in the ContentBrowseFilterPanel, otherwise false.
     */
    public boolean isAnySelectionPresent()
    {
        JavascriptExecutor executor = (JavascriptExecutor) getSession().getDriver();
        WebElement filterPanel = getDisplayedElement( By.xpath( FILTER_PANEL_DIV ) );
        return (Boolean) executor.executeScript( "return window.api.dom.ElementRegistry.getElementById(arguments[0]).hasFilterSet()",
                                                 filterPanel.getAttribute( "id" ) );
    }

    /**
     * Select a content type on the search panel and filter contents.
     *
     * @param contentTypeDisplayName
     */
    public String selectContentTypeInAggregationView( String contentTypeDisplayName )
    {
        if ( !isFilterPanelDisplayed() )
        {
            TestUtils.saveScreenshot( getSession(), "err_show_filter_panel" );
            throw new TestFrameworkException( "FilterPanel not shown!" );
        }
        String itemXpath = String.format( CONTENT_TYPE_AGGREGATION_ITEM_BY_NAME, contentTypeDisplayName );
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
            if ( !isEntrySelected( contentTypeDisplayName ) )
            {
                getDynamicElement( By.xpath( itemXpath ), 2 ).click();
                waitsForSpinnerNotVisible();
            }
        }
        return getDynamicElement( By.xpath( itemXpath ), 2 ).getText();
    }

    public String deselectContentTypeInAggregationView( String contentTypeDisplayName )
    {
        String itemXpath = String.format( CONTENT_TYPE_AGGREGATION_ITEM_BY_NAME, contentTypeDisplayName );
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


    boolean isEntrySelected( String contentTypeDisplayName )
    {
        return getSelectedValuesFromContentTypesAggregationView().contains( contentTypeDisplayName.toLowerCase() );
    }

    public List<String> getSelectedValuesFromContentTypesAggregationView()
    {
        JavascriptExecutor executor = (JavascriptExecutor) getSession().getDriver();
        WebElement filterPanel = getDisplayedElement( By.xpath( FILTER_PANEL_DIV ) );
        List list = (ArrayList) executor.executeScript(
            "return window.api.dom.ElementRegistry.getElementById(arguments[0]).getSearchInputValues().getSelectedValuesForAggregationName('contentTypes')",
            filterPanel.getAttribute( "id" ) );
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
        String itemXpath = String.format( LAST_MODIFIED_AGGREGATION_ENTRY_BY_NAME, filterItem );
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
