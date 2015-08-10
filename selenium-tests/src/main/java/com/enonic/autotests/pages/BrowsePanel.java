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
    protected final String BASE_TOOLBAR_XPATH = "//div[contains(@id,'BrowseToolbar')]";

    protected final String SHOW_FILTER_PANEL_BUTTON = BASE_TOOLBAR_XPATH + "//button[contains(@class, 'icon-search')]";

    public static String GRID_ROW =
        "//div[@class='slick-viewport']//div[contains(@class,'slick-row') and descendant::p[@class='sub-name' and text()='%s']]";

    public static String NAME_OF_CHILD_ROW =
        "//div[contains(@class,'ui-widget-content slick-row') and descendant::span[contains(@class,'toggle icon') and contains(@style,'margin-left: 16')]]//div[contains(@id,'api.app.NamesView')]/p[@class='sub-name']";

    protected final String ALL_ROWS_IN_BROWSE_PANEL_XPATH = "//div[contains(@class,'ui-widget-content slick-row')]";


    protected String CONTENT_SUMMARY_VIEWER =
        "//div[contains(@id,'ContentSummaryAndCompareStatusViewer') and descendant::p[@class='sub-name' and contains(.,'%s')]]";

    protected final String ALL_NAMES_FROM_BROWSE_PANEL_XPATH = "//div[contains(@id,'NamesView')]/p[@class='sub-name']";

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

    @FindBy(xpath = SHOW_FILTER_PANEL_BUTTON)
    protected WebElement showFilterPanelButton;

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

    public abstract <T extends WizardPanel> T clickToolbarEdit();

    public abstract <T extends Application> T clickToolbarNew();

    public abstract <T extends BaseDeleteDialog> T clickToolbarDelete();

    public abstract BaseBrowseFilterPanel getFilterPanel();

    private BrowsePanel clickOnShowFilterPanelButton()
    {
        if ( findElements( By.xpath( SHOW_FILTER_PANEL_BUTTON ) ).stream().filter( WebElement::isDisplayed ).count() == 0 )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_show_filter" ) );
            throw new TestFrameworkException( "button 'show filter panel' not displayed or probably bad locator for web element" );
        }
        showFilterPanelButton.click();
        return this;
    }

    public BrowsePanel doShowFilterPanel()
    {
        if ( !getFilterPanel().isFilterPanelDisplayed() )
        {
            clickOnShowFilterPanelButton();
        }
        return this;
    }

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
        findElements( By.xpath( expanderIcon ) ).get( 0 ).click();
        sleep( 1000 );
        return true;
    }

    public void doubleClickOnItem( String itemName )
    {
        sleep( 500 );
        String rowXpath = String.format( DIV_NAMES_VIEW, itemName );
        boolean result = waitAndFind( By.xpath( rowXpath ) );
        Actions builder = new Actions( getDriver() );
        builder.doubleClick( findElement( By.xpath( rowXpath ) ) ).build().perform();
        sleep( 500 );
    }

    public BrowsePanel expandItem( String gritItemName )
    {
        if ( !isRowSelected( gritItemName ) )
        {
            clickAndSelectRow( gritItemName );
            sleep( 700 );
        }

        pressKeyOnRow( gritItemName, Keys.ARROW_RIGHT );
        return this;
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
    public Boolean isRowExpanded( String itemName )
    {
        if ( !doScrollAndFindGridItem( itemName ) )
        {
            throw new TestFrameworkException( "grid item was not found! " + itemName );
        }
        String expanderXpath = String.format( BROWSE_PANEL_ITEM_EXPANDER, itemName );
        boolean result = waitUntilVisibleNoException( By.xpath( expanderXpath ), 3 );
        if ( !result )
        {
            getLogger().info( "invalid locator or expander for content with name: " + itemName + " does not exist! xpath =  " );
            return null;
        }
        List<WebElement> elements = findElements( By.xpath( expanderXpath ) );

        String attributeName = "class";
        String attributeValue = "collapse";
        return waitAndCheckAttrValue( elements.get( 0 ), attributeName, attributeValue, 1l );
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

    public Set<String> getSelectedGridItemNames()
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
            // throw new TestFrameworkException( "BrowsePanel:  grid was not loaded!" );
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
        if ( findElements( By.xpath( "//div[contains(@id,'app.browse.ContentTreeGrid')]//div[@class='slick-viewport']" ) ).size() != 0 )
        {
            WebElement viewportElement =
                findElements( By.xpath( "//div[contains(@id,'app.browse.ContentTreeGrid')]//div[@class='slick-viewport']" ) ).get( 0 );
            ( (JavascriptExecutor) getDriver() ).executeScript( "arguments[0].scrollTop=arguments[1]", viewportElement, step );
        }

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
        // findElement( By.xpath( contentCheckBoxXpath ) ).sendKeys( key );
        Actions actions = new Actions( getDriver() );
        actions.moveToElement( findElement( By.xpath( contentCheckBoxXpath ) ) );
        actions.sendKeys( key );
        actions.build().perform();
        sleep( 500 );
        getLogger().info( "key was typed:" + key.toString() + " ,   name is:" + item );
        return this;
    }


    public BrowsePanel holdShiftAndPressArrow( int number, Keys key )
    {
        sleep( 1000 );
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
        if ( !result )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_item" ) );
            throw new TestFrameworkException( "item was not found:" + itemName );
        }
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

    public boolean doScrollAndFindGridItem( String gridItemName, long timeout )
    {
        getLogger().info( "doScrollAndFindGridItem :  Item Name:" + gridItemName );
        String contentNameXpath = String.format( DIV_NAMES_VIEW, gridItemName );
        boolean loaded = waitUntilVisibleNoException( By.xpath( contentNameXpath ), timeout );
        if ( loaded )
        {
            return true;
        }
        if ( !isViewportScrollable() )
        {
            return false;
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
                getLogger().info( "content was found: " + gridItemName );
                return true;
            }
            if ( scrollTopBefore == scrollTopAfter )
            {
                break;
            }
            scrollTopValue += scrollTopValue;
        }
        getLogger().info( "slick-grid was scrolled and content was not found!" );
        TestUtils.saveScreenshot( getSession(), "scrolled_" + NameHelper.resolveScreenshotName( gridItemName ) );
        return false;
    }

    public boolean doScrollAndFindGridItem( String gridItemName )
    {
        return doScrollAndFindGridItem( gridItemName, Application.EXPLICIT_QUICK );
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
     * Gets item names, that have a margin-left, that means items are children.
     * *
     *
     * @return list of item names.
     */
    public List<String> getChildNames()
    {
        List<WebElement> elements = findElements( By.xpath( NAME_OF_CHILD_ROW ) );
        List<String> names = elements.stream().map( WebElement::getText ).collect( Collectors.toList() );
        return names;
    }


    public <T extends BrowsePanel> T clickCheckboxAndSelectRow( String itemName )
    {

        if ( !doScrollAndFindGridItem( itemName ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "grid_empty" ) );
            throw new TestFrameworkException( "grid item was not found! " + itemName );
        }
        String itemCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, itemName );

        getLogger().info( "Xpath of checkbox for item is :" + itemCheckBoxXpath );
        boolean isPresent = waitUntilVisibleNoException( By.xpath( itemCheckBoxXpath ), 3l );
        if ( !isPresent )
        {
            throw new SaveOrUpdateException( "checkbox for item: " + itemName + "was not found" );
        }
        findElement( By.xpath( itemCheckBoxXpath ) ).click();
        sleep( 1000 );
        getLogger().info( "check box was selected, item: " + itemName );

        return (T) this;
    }

    public <T extends BrowsePanel> T clickCheckboxAndSelectRow( int number )
    {

        List<WebElement> elements = findElements( By.xpath( "//div[contains(@class,'slick-row')]" ) );
        if ( elements.size() == 0 )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "grid_empty" ) );
            throw new TestFrameworkException( "BrowsePanel, the grid is empty" );
        }

        findElements( By.xpath(
            "//div[@class='grid-canvas']//div[contains(@class,'slick-row')]//div[contains(@class,'slick-cell-checkboxsel')]/label" ) ).get(
            number ).click();
        sleep( 200 );

        return (T) this;
    }

    public BrowsePanel pressKeyOnRow( int number, Keys key )
    {
        List<WebElement> elements = findElements( By.xpath( "//div[contains(@class,'slick-row')]" ) );
        if ( elements.size() == 0 )
        {
            throw new TestFrameworkException( "BrowsePanel, the grid is empty" );
        }

        WebElement element = findElements( By.xpath( "//div[@class='grid-canvas']//div[contains(@class,'slick-row')]" ) ).get( number );

        // findElement( By.xpath( contentCheckBoxXpath ) ).sendKeys( key );
        ////div[contains(@class,'slick-cell-checkboxsel')]/label
        Actions actions = new Actions( getDriver() );
        actions.moveToElement( element );
        actions.sendKeys( key );
        actions.build().perform();
        sleep( 500 );
        return this;
    }
}
