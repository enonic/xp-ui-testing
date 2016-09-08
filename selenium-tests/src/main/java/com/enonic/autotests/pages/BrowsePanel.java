package com.enonic.autotests.pages;

import java.util.ArrayList;
import java.util.List;
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
    protected final String TREE_GREED = "//div[contains(@id,'ContentTreeGrid')]";

    protected final String BROWSE_TOOLBAR_XPATH = "//div[contains(@id,'BrowseToolbar')]";

    protected final String TREEGRID_TOOLBAR_XPATH = "//div[contains(@id,'TreeGridToolbar')]";

    protected final String SHOW_FILTER_PANEL_BUTTON = BROWSE_TOOLBAR_XPATH + "//button[contains(@class, 'icon-search')]";

    protected final String HIDE_FILTER_PANEL_BUTTON =
        "//div[contains(@id,'ContentBrowseFilterPanel')]//span[contains(@class, 'icon-search')]";

    public static String NAME_OF_CHILD_ROW =
        "//div[contains(@class,'ui-widget-content slick-row') and descendant::span[contains(@class,'toggle icon') and contains(@style,'margin-left: 16')]]//div[contains(@id,'api.app.NamesView')]/p[@class='sub-name']";

    protected final String ALL_ROWS_IN_BROWSE_PANEL_XPATH = "//div[contains(@class,'ui-widget-content slick-row')]";

    protected String CONTENT_SUMMARY_VIEWER =
        "//div[contains(@id,'ContentSummaryAndCompareStatusViewer') and descendant::p[@class='sub-name' and contains(.,'%s')]]";

    protected String ROW_CHECKBOX_BY_NAME =
        NAMES_VIEW_BY_NAME + "/ancestor::div[contains(@class,'slick-row')]/div[contains(@class,'slick-cell-checkboxsel')]/label";

    protected String ROW_CHECKBOX_BY_DISPLAY_NAME =
        NAMES_VIEW_BY_DISPLAY_NAME + "/ancestor::div[contains(@class,'slick-row')]/div[contains(@class,'slick-cell-checkboxsel')]/label";

    protected static final String DIV_WITH_NAME =
        "//div[contains(@id,'api.ui.grid.Grid') and not(contains(@style,'display: none'))]//div[contains(@id,'api.app.NamesView')]";

    protected final String CLEAR_SELECTION_LINK_XPATH = TREEGRID_TOOLBAR_XPATH + "/button/span[contains(.,'Clear Selection')]";

    protected final String REFRESH_BUTTON = TREEGRID_TOOLBAR_XPATH + "//button[contains(@class,'icon-loop2')]";

    private String BROWSE_PANEL_ITEM_EXPANDER =
        NAMES_VIEW_BY_NAME + "/ancestor::div[contains(@class,'slick-cell')]/span[contains(@class,'collapse') or contains(@class,'expand')]";

    public final String APP_BAR_TAB_MENU = "//div[contains(@id,'AppBarTabMenu')]";

    public String APP_BAR_TAB_MENU_ITEM =
        APP_BAR_TAB_MENU + "//li[contains(@id,'AppBarTabMenuItem') and descendant::span[contains(.,'%s')]]";


    protected String CONTEXT_MENU_ITEM = "//li[contains(@id,'api.ui.menu.MenuItem') and text()='%s']";

    @FindBy(xpath = SHOW_FILTER_PANEL_BUTTON)
    protected WebElement showFilterPanelButton;

    @FindBy(xpath = HIDE_FILTER_PANEL_BUTTON)
    protected WebElement hideFilterPanelButton;

    @FindBy(xpath = CLEAR_SELECTION_LINK_XPATH)
    protected WebElement clearSelectionLink;

    private final String SELECT_ALL_LINK_XPATH =
        TREEGRID_TOOLBAR_XPATH + "/button/span[contains(.,'Select All') or contains(.,'Select all')]";

    @FindBy(xpath = SELECT_ALL_LINK_XPATH)
    protected WebElement selectAllLink;

    @FindBy(xpath = REFRESH_BUTTON)
    protected WebElement refreshButton;


    /**
     * The Constructor
     *
     * @param session
     */
    public BrowsePanel( TestSession session )
    {
        super( session );
    }

    public abstract BrowsePanel pressAppHomeButton();

    public abstract <T extends WizardPanel> T clickToolbarEdit();

    public abstract <T extends Application> T clickToolbarNew();

    public abstract <T extends Application> T clickToolbarDelete();

    public abstract BaseBrowseFilterPanel getFilterPanel();

    private BrowsePanel clickOnShowFilterPanelButton()
    {
        boolean isClickable = waitUntilClickableNoException( By.xpath( SHOW_FILTER_PANEL_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !isClickable )
        {
            saveScreenshot( NameHelper.uniqueName( "err_show_filter" ) );
            throw new TestFrameworkException( "button 'show filter panel' not clickable" );
        }
        showFilterPanelButton.click();
        sleep( 700 );
        return this;
    }

    public boolean isTabMenuItemPresent( String itemText )
    {
        List<WebElement> elems = findElements( By.xpath( APP_BAR_TAB_MENU + "//li[contains(@id,'AppBarTabMenuItem')]//span" ) );

        for ( WebElement element : elems )
        {
            if ( element.getText().contains( itemText ) )
            {
                return true;
            }
        }
        return false;
    }

    public boolean isRefreshButtonDisplayed()
    {
        return isElementDisplayed( REFRESH_BUTTON );
    }

    public BrowsePanel clickOnRefreshButton()
    {
        if ( !isRefreshButtonDisplayed() )
        {
            TestUtils.saveScreenshot( getSession(), "err_refresh_button" );
            throw new TestFrameworkException( "refresh button was not found!" );
        }
        refreshButton.click();
        sleep( 2000 );
        return this;
    }

    private BrowsePanel clickOnHideFilterPanelButton()
    {
        if ( !isElementDisplayed( HIDE_FILTER_PANEL_BUTTON ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_hide_filter" ) );
            throw new TestFrameworkException( "button 'hide filter panel' not displayed or probably bad locator for web element" );
        }
        hideFilterPanelButton.click();
        sleep( 700 );
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

    public BrowsePanel clickOnTab( String displayName )
    {
        String tabXpath = String.format( APP_BAR_TAB_MENU_ITEM, displayName );
        if ( !isElementDisplayed( tabXpath ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_tab_" + displayName ) );
            throw new TestFrameworkException( "tab menu item was not found: " + displayName );
        }
        getDisplayedElement( By.xpath( tabXpath ) ).click();
        return this;
    }

    public BrowsePanel doHideFilterPanel()
    {
        if ( getFilterPanel().isFilterPanelDisplayed() )
        {
            clickOnHideFilterPanelButton();
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
        String rowXpath = String.format( NAMES_VIEW_BY_NAME, itemName );
        boolean result = waitAndFind( By.xpath( rowXpath ) );
        Actions builder = new Actions( getDriver() );
        builder.doubleClick( findElement( By.xpath( rowXpath ) ) ).build().perform();
        sleep( 500 );
    }

    public BrowsePanel expandItem( String gritItemName )
    {
        if ( !isRowSelected( gritItemName ) )
        {
            selectRowByName( gritItemName );
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
    public BrowsePanel clickOnSelectAll()
    {
        boolean isVisibleLink = waitUntilVisibleNoException( By.xpath( SELECT_ALL_LINK_XPATH ), 2l );
        if ( !isVisibleLink )
        {
            throw new TestFrameworkException( "The link 'Select All' was not found on the page, probably wrong xpath locator" );
        }
        selectAllLink.click();
        sleep( 2000 );
        return this;
    }

    /**
     * Gets a number of selected items in the table.
     *
     * @return a number of selected rows.
     */
    public int getSelectedRowsNumber()
    {
        scrollViewPortToTop();
        // Set<String> names = new HashSet<>();
        List<String> names = new ArrayList<>();
        names.addAll( getNamesOfSelectedGridItem() );
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
            for ( ; ; )
            {
                scrollTopBefore = getViewportScrollTopValue();
                scrollTopAfter = doScrollViewport( valueForScroll );
                names.addAll( getNamesOfSelectedGridItem() );
                if ( scrollTopBefore == scrollTopAfter )
                {
                    break;
                }
                valueForScroll += valueForScroll;
            }
            return names.size();
        }
    }

    public List<String> getNamesOfSelectedGridItem()
    {
        List<WebElement> rows =
            findElements( By.xpath( ALL_ROWS_IN_BROWSE_PANEL_XPATH + "/div[contains(@class,'selected')]//p[@class='sub-name']" ) );
        return rows.stream().map( WebElement::getText ).collect( Collectors.toList() );
    }

    public List<String> getDisplayNamesOfSelectedGridItems()
    {
        List<WebElement> rows =
            findElements( By.xpath( ALL_ROWS_IN_BROWSE_PANEL_XPATH + "/div[contains(@class,'selected')]" + H6_MAIN_NAME ) );
        return rows.stream().map( WebElement::getText ).collect( Collectors.toList() );
    }


    private List<String> getGridItemNames()
    {
        List<WebElement> elements = findElements( By.xpath( ALL_ROWS_IN_BROWSE_PANEL_XPATH +
                                                                "//div[contains(@class,'slick-cell l1 r1')]//div[@class='names-and-icon-view small']//p[@class='sub-name']" ) );
        return elements.stream().filter( e -> !e.getText().isEmpty() ).map( WebElement::getText ).collect( Collectors.toList() );
    }

    public boolean isRowSelected( String name )
    {
        String rowByName = String.format( ( NAMES_VIEW_BY_NAME + "/ancestor::div[contains(@class,'slick-cell')]" ), name );
        List<WebElement> rows = findElements( By.xpath( rowByName ) );
        if ( rows.size() == 0 )
        {
            getFilterPanel().typeSearchText( name );
            rows = findElements( By.xpath( rowByName ) );
            if ( rows.size() == 0 )
            {
                TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_select_" + name ) );
                throw new TestFrameworkException( "row with content was not found, content name is " + name );
            }
            getFilterPanel().clickOnCleanFilter();
        }
        return rows.get( 0 ).getAttribute( "class" ).contains( "selected" );
    }

    public boolean isRowByDisplayNameSelected( String displayName )
    {
        String rowByDisplayName =
            String.format( ( NAMES_VIEW_BY_DISPLAY_NAME + "/ancestor::div[contains(@class,'slick-cell')]" ), displayName );
        List<WebElement> rows = findElements( By.xpath( rowByDisplayName ) );
        if ( rows.size() == 0 )
        {
            getFilterPanel().typeSearchText( displayName );
            rows = findElements( By.xpath( rowByDisplayName ) );
            if ( rows.size() == 0 )
            {
                TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_select_" + displayName ) );
                throw new TestFrameworkException( "row with grid-item was not found, display name is " + displayName );
            }
            getFilterPanel().clickOnCleanFilter();
        }
        return rows.get( 0 ).getAttribute( "class" ).contains( "selected" );
    }


    /**
     * Clicks on 'Clear Selection' link and removes row-selections.
     */
    public BrowsePanel clickOnClearSelection()
    {
        sleep( 500 );
        boolean isLeLinkVisible = waitUntilVisibleNoException( By.xpath( CLEAR_SELECTION_LINK_XPATH ), 2l );
        if ( !isLeLinkVisible && getFilterPanel().isFilterPanelDisplayed() )
        {
            doHideFilterPanel();
            isLeLinkVisible = waitUntilVisibleNoException( By.xpath( CLEAR_SELECTION_LINK_XPATH ), 2l );
        }
        if ( !isLeLinkVisible )
        {
            saveScreenshot( NameHelper.uniqueName( "err_clear_sel" ) );
            throw new TestFrameworkException( "The link 'Clear Selection' was not found on the page, probably wrong xpath locator" );
        }
        clearSelectionLink.click();
        sleep( 1000 );
        return this;
    }

    /**
     * Finds on page 'Clear selection' link and get text.
     *
     * @return for example : 'Clear selection (2)'
     */
    public Integer getNumberFromClearSelectionLink()
    {
        if ( !isElementDisplayed( CLEAR_SELECTION_LINK_XPATH ) )
        {
            saveScreenshot( "err_clear_selection" );
            throw new TestFrameworkException( "the 'Clear selection' Link was not found, probably wrong xpath locator!" );
        }
        String text = clearSelectionLink.getText();
        if ( text.indexOf( "(" ) == -1 )
        {
            return 0;
        }
        String numberOfSelectedItems = text.substring( text.indexOf( "(" ) + 1, text.indexOf( ")" ) );
        return Integer.valueOf( numberOfSelectedItems );
    }


    public void isGridEmpty( long timeout )
    {
        boolean isGridLoaded = waitAndFind( By.xpath( DIV_WITH_NAME ), timeout );
        if ( !isGridLoaded )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "grid_is_empty" ) );
        }
    }

    /**
     * Waits until page loaded.
     *
     * @param timeout
     */
    public void waitUntilPageLoaded( long timeout )
    {
        boolean isLoaded = waitUntilVisibleNoException( By.xpath( GRID_CANVAS ), timeout );
        if ( !isLoaded )
        {
            saveScreenshot( NameHelper.uniqueName( "err_browse_panel" ) );
            throw new TestFrameworkException( "browse panel not loaded" );
        }
    }

    /**
     * scrolls and count number of rows in browse panel.
     */
    public int getRowsCount()
    {
        scrollViewPortToTop();
        //Set<String> set = new HashSet<>();
        List<String> names = new ArrayList<>();
        names.addAll( getGridItemNames() );
        if ( !isViewportScrollable() )
        {
            return names.size();
        }
        // else, do scroll and add values.
        long newScrollTop = getViewportHeight();
        long scrollTopBefore;
        long scrollTopAfter;
        for ( ; ; )
        {
            scrollTopBefore = getViewportScrollTopValue();
            scrollTopAfter = doScrollViewport( newScrollTop );
            if ( scrollTopBefore == scrollTopAfter )
            {
                break;
            }
            newScrollTop += newScrollTop;
            names.addAll( getGridItemNames() );
        }
        return names.size();
    }

    protected Long doScrollViewport( long step )
    {
        if ( findElements( By.xpath( TREE_GREED + "//div[@class='slick-viewport']" ) ).size() != 0 )
        {
            WebElement viewportElement = findElements( By.xpath( TREE_GREED + "//div[@class='slick-viewport']" ) ).get( 0 );
            ( (JavascriptExecutor) getDriver() ).executeScript( "arguments[0].scrollTop=arguments[1]", viewportElement, step );
        }
        sleep( 1000 );
        return getViewportScrollTopValue();
    }

    public boolean isViewportScrollable()
    {
        String styleValue = findElements( By.xpath( GRID_CANVAS ) ).get( 0 ).getAttribute( "style" );
        int gridCanvasHeight = getHeightFromStyleString( styleValue );
        if ( gridCanvasHeight == 0 )
        {
            return false;
        }
        styleValue = findElements( By.xpath( "//div[@class='slick-viewport']" ) ).get( 0 ).getAttribute( "style" );
        int viewportHeight = getHeightFromStyleString( styleValue );
        if ( viewportHeight == 0 )
        {
            return false;
        }
        return gridCanvasHeight - viewportHeight > 50;
    }

    private int getHeightFromStyleString( String styleString )
    {
        int startIndex = styleString.lastIndexOf( "height:" ) + "height:".length();
        int endIndex = styleString.lastIndexOf( "px" );
        int height = 0;
        try
        {
            height = Integer.valueOf( styleString.substring( startIndex, endIndex ).trim() );
        }
        catch ( NumberFormatException ex )
        {
            getLogger().info( "style does not contain a number value for height" );
            return 0;
        }
        return height;
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
        String rowXpath = String.format( SLICK_ROW_BY_NAME, item );

        boolean isPresent = waitUntilVisibleNoException( By.xpath( rowXpath ), 3l );
        if ( !isPresent )
        {
            throw new SaveOrUpdateException( "checkbox for item: " + item + "was not found" );
        }
        Actions actions = new Actions( getDriver() );
        actions.moveToElement( findElement( By.xpath( rowXpath ) ) );
        actions.sendKeys( key );
        actions.build().perform();
        sleep( 500 );
        getLogger().info( "key was typed:" + key.toString() + " ,   name is:" + item );
        return this;
    }

    public BrowsePanel findRowByDisplayNameAndSendKey( String itemDisplayName, Keys key )
    {
        WebElement row = findRowElementByDisplayName( itemDisplayName );
        pressKeyOnRowElement( row, key );
        return this;
    }

    protected WebElement findRowElementByDisplayName( String item )
    {
        String row = String.format( SLICK_ROW_BY_DISPLAY_NAME, item );
        boolean isPresent = waitUntilVisibleNoException( By.xpath( row ), 3l );
        if ( !isPresent )
        {
            throw new SaveOrUpdateException( "checkbox for item: " + item + "was not found" );
        }
        return findElement( By.xpath( row ) );
    }

    protected BrowsePanel pressKeyOnRowElement( WebElement element, Keys key )
    {
        Actions actions = new Actions( getDriver() );
        actions.moveToElement( element );
        actions.sendKeys( key );
        actions.build().perform();
        sleep( 500 );
        getLogger().info( "key was typed:" + key.toString() );
        return this;
    }

    public BrowsePanel holdShiftAndPressArrow( int number, Keys key )
    {
        sleep( 500 );
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

    public BrowsePanel selectRowByItemDisplayName( String displayName )
    {
        String rowXpath = String.format( NAMES_VIEW_BY_DISPLAY_NAME, displayName );
        boolean result = waitAndFind( By.xpath( rowXpath ) );
        if ( !result )
        {
            saveScreenshot( "err_" + displayName );
            throw new TestFrameworkException( "item was not found:" + displayName );
        }
        Actions builder = new Actions( getDriver() );
        builder.click( waitAndFindElement( By.xpath( rowXpath ) ) ).build().perform();
        sleep( 500 );
        return this;
    }

    public BrowsePanel selectRowByName( String itemName )
    {
        String rowXpath = String.format( NAMES_VIEW_BY_NAME, itemName );
        boolean result = waitAndFind( By.xpath( rowXpath ) );
        if ( !result )
        {
            TestUtils.saveScreenshot( getSession(), "err_" + itemName );
            throw new TestFrameworkException( "item was not found:" + itemName );
        }
        Actions builder = new Actions( getDriver() );
        builder.click( waitAndFindElement( By.xpath( rowXpath ) ) ).build().perform();
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
        scrollViewPortToTop();
        String contentNameXpath = String.format( NAMES_VIEW_BY_NAME, gridItemName );
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
        for ( ; ; )
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

    public boolean exists( String itemName )
    {
        return exists( itemName, false );
    }

    public boolean isGridItemPresent( String displayName )
    {
        String itemXpath = String.format( NAMES_VIEW_BY_DISPLAY_NAME, displayName );
        return isElementDisplayed( itemXpath );
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
            saveScreenshot( "err_find_" + itemName );
            throw new TestFrameworkException( "grid item was not found! " + itemName );
        }
        String itemCheckBoxXpath = String.format( ROW_CHECKBOX_BY_NAME, itemName );
        getLogger().info( "Xpath of checkbox for item is :" + itemCheckBoxXpath );
        boolean isPresent = waitUntilVisibleNoException( By.xpath( itemCheckBoxXpath ), 3l );
        if ( !isPresent )
        {
            saveScreenshot( "err_checkbox_" + itemName );
            throw new SaveOrUpdateException( "checkbox for item: " + itemName + "was not found" );
        }
        waitAndFindElement( By.xpath( itemCheckBoxXpath ) ).click();
        sleep( 1000 );
        getLogger().info( "check box was selected, item: " + itemName );
        return (T) this;
    }

    public <T extends BrowsePanel> T clickCheckboxAndSelectRowByDisplayName( String itemDisplayName )
    {
        String itemCheckBoxXpath = String.format( ROW_CHECKBOX_BY_DISPLAY_NAME, itemDisplayName );
        getLogger().info( "Xpath of checkbox for item is :" + itemCheckBoxXpath );
        boolean isPresent = waitUntilVisibleNoException( By.xpath( itemCheckBoxXpath ), 3l );
        if ( !isPresent )
        {
            TestUtils.saveScreenshot( getSession(), "err_checkbox_" + itemDisplayName );
            throw new SaveOrUpdateException( "checkbox for item: " + itemDisplayName + "was not found" );
        }
        waitAndFindElement( By.xpath( itemCheckBoxXpath ) ).click();
        sleep( 1000 );
        getLogger().info( "check box was selected, item: " + itemDisplayName );
        return (T) this;
    }

    public <T extends BrowsePanel> T clickCheckboxAndSelectRow( int number )
    {
        long actualNumberOfRows = getNumberOfElements( By.xpath( GRID_CANVAS + SLICK_ROW ) );
        if ( actualNumberOfRows == 0 || actualNumberOfRows < number )
        {
            saveScreenshot( NameHelper.uniqueName( "grid_empty" ) );
            throw new TestFrameworkException( "BrowsePanel, the grid is empty" );
        }
        findElements( By.xpath( GRID_CANVAS + SLICK_ROW + "//div[contains(@class,'slick-cell-checkboxsel')]/label" ) ).get(
            number ).click();
        sleep( 200 );
        return (T) this;
    }

    public BrowsePanel pressKeyOnRow( int number, Keys key )
    {
        List<WebElement> elements = findElements( By.xpath( SLICK_ROW ) );
        if ( elements.size() == 0 )
        {
            throw new TestFrameworkException( "BrowsePanel, the grid is empty" );
        }
        WebElement element = findElements( By.xpath( GRID_CANVAS + SLICK_ROW ) ).get( number );
        Actions actions = new Actions( getDriver() );
        actions.moveToElement( element );
        actions.sendKeys( key );
        actions.build().perform();
        sleep( 500 );
        return this;
    }

    public void openContextMenu( String gridItemName )
    {
        getLogger().info( "opening a context menu, content path of content: " + gridItemName );
        String contentDescriptionXpath = String.format( NAMES_VIEW_BY_NAME, gridItemName );
        WebElement element = findElement( By.xpath( contentDescriptionXpath ) );
        Actions action = new Actions( getDriver() );
        action.contextClick( element ).build().perform();
        sleep( 300 );
    }

    public void selectItemByDisplayNameOnOpenContextMenu( String gridItemDisplayName )
    {
        getLogger().info( "select a item and opening a context menu, : " + gridItemDisplayName );
        String contentDescriptionXpath = String.format( NAMES_VIEW_BY_DISPLAY_NAME, gridItemDisplayName );
        WebElement element = findElement( By.xpath( contentDescriptionXpath ) );
        Actions action = new Actions( getDriver() );
        action.contextClick( element ).build().perform();
        sleep( 300 );
    }

    public boolean isContextMenuItemEnabled( String action )
    {
        if ( findElements( By.xpath( String.format( CONTEXT_MENU_ITEM, action ) ) ).size() == 0 )
        {
            throw new TestFrameworkException( "menu item was not found!  " + action );
        }
        String styleClass = findElement( By.xpath( String.format( CONTEXT_MENU_ITEM, action ) ) ).getAttribute( "class" );
        return !styleClass.contains( "disabled" );
    }

    public boolean isContextMenuItemDisplayed( String menuItem )
    {
        return isElementDisplayed( String.format( CONTEXT_MENU_ITEM, menuItem ) );
    }

    public BrowsePanel refreshPanelInBrowser()
    {
        getDriver().navigate().refresh();
        sleep( 2000 );
        return this;
    }

    public String waitNotificationMessage( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( NOTIFICATION_MESSAGE_XPATH ), timeout ) )
        {
            return null;
        }
        String message = findElement( By.xpath( NOTIFICATION_MESSAGE_XPATH ) ).getText();
        getLogger().info( "Notification message " + message );
        return message.trim();
    }

    public String waitErrorNotificationMessage( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( ERROR_NOTIFICATION_MESSAGE_XPATH ), timeout ) )
        {
            return null;
        }
        String message = findElement( By.xpath( ERROR_NOTIFICATION_MESSAGE_XPATH ) ).getText();
        getLogger().info( "Notification message " + message );
        return message.trim();
    }

}
