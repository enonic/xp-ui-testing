package com.enonic.autotests.pages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public abstract class BrowsePanel
    extends Application
{
    protected final String ALL_ROWS_IN_BROWSE_PANEL_XPATH = "//div[contains(@class,'ui-widget-content slick-row')]";

    protected String DIV_NAMES_VIEW = "//div[contains(@id,'api.app.NamesView') and child::p[contains(@title,'%s')]]";

    protected final String ALL_NAMES_FROM_BROWSE_PANEL_XPATH = "//div[contains(@id,'api.app.NamesView')]/p[@class='sub-name']";

    protected String CHECKBOX_ROW_CHECKER =
        DIV_NAMES_VIEW + "/ancestor::div[contains(@class,'slick-row')]/div[contains(@class,'slick-cell-checkboxsel')]/label";

    protected static final String DIV_WITH_NAME =
        "//div[contains(@id,'api.ui.grid.Grid') and not(contains(@style,'display: none'))]//div[contains(@id,'api.app.NamesView')]";

    protected final String DIV_WITH_SCROLL = "//div[contains(@id,'app.browse.ContentTreeGrid')]//div[contains(@class,'slickgrid')]";

    protected final String CLEAR_SELECTION_LINK_XPATH =
        "//div[contains(@id,'api.ui.treegrid.TreeGridToolbar')]/button/span[contains(.,'Clear Selection')]";

    protected String NOT_LOADED_CONTENT_XPATH = "//div[contains(@class,'children-to-load')]";

    private String BROWSE_PANEL_ITEM_EXPANDER =
        DIV_NAMES_VIEW + "/ancestor::div[contains(@class,'slick-cell')]/span[contains(@class,'collapse') or contains(@class,'expand')]";

    @FindBy(xpath = CLEAR_SELECTION_LINK_XPATH)
    protected WebElement clearSelectionLink;

    private final String SELECT_ALL_LINK_XPATH =
        "//div[contains(@id,'api.ui.treegrid.TreeGridToolbar')]/button/span[contains(.,'Select All') or contains(.,'Select all')]";

    @FindBy(xpath = SELECT_ALL_LINK_XPATH)
    protected WebElement selectAllLink;


    /**
     * The Constructor
     *
     * @param session
     */
    public BrowsePanel( TestSession session )
    {
        super( session );
    }

    public abstract BrowsePanel goToAppHome();

    /**
     * clicks on 'expand' icon and expands a folder.
     *
     * @param gridItemName - the name of item in the browse panel.
     * @return true if space is not empty and was expanded, otherwise return
     * false.
     */
    public boolean clickOnExpander( String gridItemName )
    {
        boolean isExpanderPresent = isExpanderPresent( gridItemName );
        if ( !isExpanderPresent )
        {
            getLogger().info( "This object: " + gridItemName + " has no child" );
            return false;
        }
        String expanderIcon = String.format( BROWSE_PANEL_ITEM_EXPANDER, gridItemName );
        findElement( By.xpath( expanderIcon ) ).click();
        return true;
    }

    /**
     * If content or content type has a child, so expander icon should be present near the item from BrowsePanel.
     *
     * @return true if expander icon is present, otherwise false.
     */
    public boolean isExpanderPresent( String contentPath )
    {
        String expanderElement = String.format( BROWSE_PANEL_ITEM_EXPANDER, contentPath );
        getLogger().info( "check if present expander for folder:" + contentPath.toString() + " xpath: " + expanderElement );
        boolean isPresent = isDynamicElementPresent( By.xpath( expanderElement ), 2 );
        if ( !isPresent )
        {
            getLogger().info( "expander for folder:" + contentPath + " was not found! " );
            return false;
        }

        return true;
    }

    /**
     * @return true if row in BrowsePanel is expanded, otherwise false.
     */
    public boolean isRowExpanded( String itemName )
    {
        if ( !doScrollAndFindGridItem( itemName ) )
        {
            throw new TestFrameworkException( "grid item was not found! " + itemName );
        }
        String expanderXpath = String.format( BROWSE_PANEL_ITEM_EXPANDER, itemName );

        WebElement expanderElement = findElements( By.xpath( expanderXpath ) ).get( 0 );
        if ( expanderElement == null )
        {
            throw new TestFrameworkException(
                "invalid locator or content with name: " + itemName + " does not exist! xpath =  " + expanderXpath );
        }

        String attributeName = "class";
        String attributeValue = "collapse";
        return waitAndCheckAttrValue( expanderElement, attributeName, attributeValue, 1l );
    }


    /**
     * Clicks by "Select All" and selects all items from the table.
     *
     * @return the number of selected rows.
     */
    public void clickOnSelectAll()
    {
        boolean isVisibleLink = waitUntilVisibleNoException( By.xpath( SELECT_ALL_LINK_XPATH ), 2l );
        if ( !isVisibleLink )
        {
            throw new TestFrameworkException( "The link 'Select All' was not found on the page, probably wrong xpath locator" );
        }
        selectAllLink.click();
        sleep( 2000 );
    }


    /**
     * Gets a number of selected items in the table.
     *
     * @return a number of selected rows.
     */
    public int getSelectedRowsNumber()
    {
        scrollViewPortToTop();
        Set<String> names = new HashSet<>();
        names.addAll( getSelectedGridItemNames() );
        if ( !isViewportScrollable() )
        {
            return names.size();
        }
        else
        {
            //scroll and count
            long scrollTopBefore;
            long scrollTopAfter;
            long valueForScroll = getViewportHeight();
            for (; ; )
            {
                scrollTopBefore = getViewportScrollTopValue();
                scrollTopAfter = doScrollViewport( valueForScroll );
                names.addAll( getSelectedGridItemNames() );
                if ( scrollTopBefore == scrollTopAfter )
                {
                    break;
                }
                valueForScroll += valueForScroll;
            }
            return names.size();
        }
    }

    private Set<String> getSelectedGridItemNames()
    {
        List<WebElement> rows =
            findElements( By.xpath( ALL_ROWS_IN_BROWSE_PANEL_XPATH + "/div[contains(@class,'selected')]//p[@class='sub-name']" ) );
        Set<String> set = rows.stream().map( WebElement::getText ).collect( Collectors.toSet() );
        return set;
    }


    private Set<String> getGridItemNames()
    {
        List<WebElement> elements = findElements( By.xpath( ALL_ROWS_IN_BROWSE_PANEL_XPATH +
                                                                "//div[contains(@class,'slick-cell l1 r1')]//div[@class='names-and-icon-view small']//p[@class='sub-name']" ) );
        return elements.stream().filter( e -> !e.getText().isEmpty() ).map( WebElement::getText ).collect( Collectors.toSet() );
    }

    public boolean isRowSelected( String name )
    {
        List<WebElement> rows =
            findElements( By.xpath( String.format( ( DIV_NAMES_VIEW + "/ancestor::div[contains(@class,'slick-cell')]" ), name ) ) );
        if ( rows.size() == 0 )
        {
            TestUtils.saveScreenshot( getSession(), "select-error" );
            throw new TestFrameworkException( "row with content was not found, content name is " + name );
        }
        return waitAndCheckAttrValue( rows.get( 0 ), "class", "selected", 1 );

    }

    /**
     * Clicks on 'Clear Selection' link and removes row-selections.
     */
    public void clickOnClearSelection()
    {
        sleep( 500 );
        boolean isLeLinkVisible = waitUntilVisibleNoException( By.xpath( CLEAR_SELECTION_LINK_XPATH ), 2l );
        if ( !isLeLinkVisible )
        {
            throw new TestFrameworkException( "The link 'Clear Selection' was not found on the page, probably wrong xpath locator" );
        }
        clearSelectionLink.click();
        sleep( 1000 );
    }

    /**
     * Finds on page 'Clear selection' link and get text.
     *
     * @return for example : 'Clear selection (2)'
     */
    public String getClearSelectionText()
    {
        List<WebElement> elems = findElements( By.xpath( CLEAR_SELECTION_LINK_XPATH ) );
        if ( elems.size() == 0 )
        {
            throw new TestFrameworkException( "the 'Clear selection' Link was not found, probably wrong xpath locator!" );
        }
        return clearSelectionLink.getText();
    }

    /**
     * Waits until page loaded.
     *
     * @param timeout
     */
    public void waitUntilPageLoaded( long timeout )
    {
        boolean isGridLoaded = waitAndFind( By.xpath( DIV_WITH_NAME ), timeout );
        if ( !isGridLoaded )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "grid_bug" ) );
            throw new TestFrameworkException( "BrowsePanel:  grid was not loaded!" );
        }
    }

    /**
     * scrolls and count number of rows in browse panel.
     */
    public int getRowNumber()
    {
        scrollViewPortToTop();
        Set<String> set = new HashSet<>();
        set.addAll( getGridItemNames() );
        if ( !isViewportScrollable() )
        {
            return set.size();
        }
        // else, do scroll and add values.
        long newScrollTop = getViewportHeight();
        long scrollTopBefore;
        long scrollTopAfter;
        for (; ; )
        {
            scrollTopBefore = getViewportScrollTopValue();
            scrollTopAfter = doScrollViewport( newScrollTop );
            if ( scrollTopBefore == scrollTopAfter )
            {
                break;
            }
            newScrollTop += newScrollTop;
            set.addAll( getGridItemNames() );
        }
        return set.size();
    }

    public Long doScrollViewport( long step )
    {
        WebElement viewportElement = findElements( By.xpath( "//div[@class='slick-viewport']" ) ).get( 0 );
        ( (JavascriptExecutor) getDriver() ).executeScript( "arguments[0].scrollTop=arguments[1]", viewportElement, step );
        sleep( 1000 );
        return getViewportScrollTopValue();
    }

    public boolean isViewportScrollable()
    {
        String styleValue = findElements( By.xpath( "//div[@class='grid-canvas']" ) ).get( 0 ).getAttribute( "style" );
        int gridCanvasHeight = getHeightFromStyleString( styleValue );
        styleValue = findElements( By.xpath( "//div[@class='slick-viewport']" ) ).get( 0 ).getAttribute( "style" );
        int viewportHeight = getHeightFromStyleString( styleValue );
        return gridCanvasHeight - viewportHeight > 50;
    }

    private int getHeightFromStyleString( String styleString )
    {
        int startIndex = styleString.lastIndexOf( "height:" ) + "height:".length();
        int endIndex = styleString.lastIndexOf( "px" );
        return Integer.valueOf( styleString.substring( startIndex, endIndex ).trim() );
    }


    /**
     * When row selected, there ia ability to click on Spacebar or ARROW_DOWN, ARROW_UP
     *
     * @param item the item name.
     * @param key
     * @return {@link BrowsePanel} instance.
     */
    public BrowsePanel pressKeyOnRow( String item, Keys key )
    {
        String contentCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, item );

        getLogger().info( "Xpath of checkbox for content is :" + contentCheckBoxXpath );
        boolean isPresent = waitUntilVisibleNoException( By.xpath( contentCheckBoxXpath ), 3l );
        if ( !isPresent )
        {
            throw new SaveOrUpdateException( "checkbox for item: " + item + "was not found" );
        }
        findElement( By.xpath( contentCheckBoxXpath ) ).sendKeys( key );
        sleep( 500 );
        getLogger().info( "key was typed:" + key.toString() + " ,   name is:" + item );
        return this;
    }

    public BrowsePanel holdShiftAndPressArrow( String itemName, int number, Keys key )
    {
        sleep( 5000 );
        Actions action = new Actions( getDriver() );
        List<CharSequence> list = new ArrayList<>( number );
        for ( int i = 0; i < number; i++ )
        {
            list.add( key );
        }
        action.keyDown( Keys.SHIFT ).sendKeys( list.toArray( new Keys[list.size()] ) ).keyUp( Keys.SHIFT ).build().perform();
        sleep( 1000 );
        return this;
    }


    public BrowsePanel clickAndSelectRow( String itemName )
    {
        String rowXpath = String.format( DIV_NAMES_VIEW, itemName );
        boolean result = waitAndFind( By.xpath( rowXpath ) );
        Actions builder = new Actions( getDriver() );
        builder.click( findElement( By.xpath( rowXpath ) ) ).build().perform();
        sleep( 500 );
        return this;
    }

    protected int getViewportHeight()
    {
        Object viewPortHeight = ( (JavascriptExecutor) getDriver() ).executeScript(
            "return document.getElementsByClassName('slick-viewport')[0].style.height" );
        return Integer.valueOf( viewPortHeight.toString().substring( 0, viewPortHeight.toString().indexOf( "px" ) ) );
    }

    protected int getGridCanvasHeight()
    {
        Object scrollHeight = ( (JavascriptExecutor) getDriver() ).executeScript(
            "return document.getElementsByClassName('slick-viewport')[0].scrollHeight" );
        return Integer.valueOf( scrollHeight.toString() );
    }

    /**
     * each 'slick-row' has a attribute : style="top:90px"  , so we can find a last row.
     * Scrolling will be  finished, when slick row is last.
     */
    protected boolean isScrollingFinished( int scrollHeight )
    {
        List<WebElement> elements = findElements( By.xpath( "//div[contains(@class,'slick-row')]" ) );
        String topOfLastElement = elements.get( elements.size() - 1 ).getAttribute( "style" );
        int top =
            Integer.valueOf( topOfLastElement.substring( topOfLastElement.indexOf( ":" ) + 1, topOfLastElement.indexOf( "px" ) ).trim() );
        return scrollHeight - top < 200;

    }

    public Long getViewportScrollTopValue()
    {
        return (Long) ( (JavascriptExecutor) getDriver() ).executeScript(
            "return document.getElementsByClassName('slick-viewport')[0].scrollTop" );
    }

    public void scrollViewPortToTop()
    {
        ( (JavascriptExecutor) getDriver() ).executeScript( "return document.getElementsByClassName('slick-viewport')[0].scrollTop=0" );
        sleep( 1000 );
    }

    public boolean doScrollAndFindGridItem( String gridItem, int timeout )
    {
        String contentNameXpath = String.format( DIV_NAMES_VIEW, gridItem );
        boolean loaded = waitUntilVisibleNoException( By.xpath( contentNameXpath ), timeout );
        if ( loaded )
        {
            return true;
        }
        int scrollTopValue = getViewportHeight();
        long scrollTopBefore;
        long scrollTopAfter;
        for (; ; )
        {
            scrollTopBefore = getViewportScrollTopValue();
            scrollTopAfter = doScrollViewport( scrollTopValue );

            if ( waitUntilVisibleNoException( By.xpath( contentNameXpath ), timeout ) )
            {
                getLogger().info( "content was found: " + gridItem );
                return true;
            }
            if ( scrollTopBefore == scrollTopAfter )
            {
                break;
            }
            scrollTopValue += scrollTopValue;
        }
        getLogger().info( "slick-grid was scrolled and content was not found!" );
        return false;
    }

    public boolean doScrollAndFindGridItem( String gridItem )
    {
        return doScrollAndFindGridItem( gridItem, Application.DEFAULT_IMPLICITLY_WAIT );
    }

    /**
     * @param itemName
     * @param saveScreenshot if true, screenshot will be saved.
     * @return true if content exists, otherwise false.
     */
    public boolean exists( String itemName, boolean saveScreenshot )
    {
        scrollViewPortToTop();
        boolean result = doScrollAndFindGridItem( itemName );

        if ( saveScreenshot )
        {
            String name;
            if ( itemName.contains( "/" ) )
            {
                name = itemName.substring( itemName.lastIndexOf( "/" ) + 1 );
            }
            else
            {
                name = itemName;
            }
            TestUtils.saveScreenshot( getSession(), name );
        }
        return result;
    }

    /**
     * Gets item names, that are children for parent.
     *
     * @param parentItemName parent item name
     * @return list of item names.
     */
    public List<String> getChildNames( String parentItemName )
    {
        List<String> listNames = new ArrayList<>();
        String pElement =
            String.format( "//div[contains(@id,'api.app.NamesView')and child::p[contains(@title,'%s/')]]/p[@class='sub-name']",
                           parentItemName );
        List<WebElement> elements = findElements( By.xpath( String.format( pElement, parentItemName ) ) );
        return elements.stream().map( element -> {
            return element.getAttribute( "title" );
        } ).collect( Collectors.toList() );
    }

    public <T extends BrowsePanel> T clickCheckboxAndSelectRow( String itemName )
    {
        if ( !doScrollAndFindGridItem( itemName ) )
        {
            throw new TestFrameworkException( "grid item was not found! " + itemName );
        }
        String itemCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, itemName );

        getLogger().info( "Xpath of checkbox for item is :" + itemCheckBoxXpath );
        boolean isPresent = waitUntilVisibleNoException( By.xpath( itemCheckBoxXpath ), 3l );
        if ( !isPresent )
        {
            throw new SaveOrUpdateException( "checkbox for item: " + itemName + "was not found" );
        }
        sleep( 700 );
        findElement( By.xpath( itemCheckBoxXpath ) ).click();
        getLogger().info( "check box was selected, item: " + itemName );

        return (T) this;
    }
}
