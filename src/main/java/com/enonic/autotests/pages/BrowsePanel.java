package com.enonic.autotests.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class BrowsePanel
    extends Application
{
    protected final String ALL_ROWS_IN_BROWSE_PANEL_XPATH = "//div[contains(@class,'ui-widget-content slick-row')]";

    protected String DIV_NAMES_VIEW = "//div[contains(@id,'api.app.NamesView') and child::p[@title='%s']]";

    protected String TD_CHILDREN_CONTENT_NAMES = "//table[contains(@class,'x-grid-table')]//td[descendant::p[contains(.,'%s')]]";

    private final String CLEAR_SELECTION_LINK_XPATH =
        "//div[contains(@id,'api.ui.treegrid.TreeGridToolbar')]/button/span[text()='Clear Selection']";

    private String BROWSE_PANEL_ITEM_EXPANDER =
        DIV_NAMES_VIEW + "/ancestor::div[contains(@class,'slick-cell')]/span[contains(@class,'collapse') or contains(@class,'expand')]";

    @FindBy(xpath = CLEAR_SELECTION_LINK_XPATH)
    protected WebElement clearSelectionLink;

    private final String SELECT_ALL_LINK_XPATH =
        "//div[contains(@id,'api.ui.treegrid.TreeGridToolbar')]/button/span[text()='Select All' or text()='Select all']";

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

    /**
     * @return the number of rows in Browse Panel.
     */
    public int getRowNumber()
    {
        return findElements( By.xpath( ALL_ROWS_IN_BROWSE_PANEL_XPATH ) ).size();
    }


    /**
     * clicks on 'expand' icon and expands a folder.
     *
     * @param name - the name of content type or contentPath
     * @return true if space is not empty and was expanded, otherwise return
     * false.
     */
    public <T> boolean clickOnExpander( T name )
    {
        boolean isExpanderPresent = isExpanderPresent( name );
        if ( !isExpanderPresent )
        {
            getLogger().info( "This object: " + name.toString() + " has no child" );
            return false;
        }
        String expanderIcon = String.format( BROWSE_PANEL_ITEM_EXPANDER, name.toString() );
        findElement( By.xpath( expanderIcon ) ).click();
        return true;
    }

    /**
     * If content or content type has a child, so expander icon should be present near the item from BrowsePanel.
     *
     * @return true if expander icon is present, otherwise false.
     */
    public <T> boolean isExpanderPresent( T contentPath )
    {
        String expanderElement = String.format( BROWSE_PANEL_ITEM_EXPANDER, contentPath.toString() );
        getLogger().info( "check if present expander for folder:" + contentPath.toString() + " xpath: " + expanderElement );
        boolean isPresent = isDynamicElementPresent( By.xpath( expanderElement ), 2 );
        if ( !isPresent )
        {
            getLogger().info( "expander for folder:" + contentPath.toString() + " was not found! " );
            return false;
        }

        return true;
    }

    /**
     * @return true if row in BrowsePanel is expanded, otherwise false.
     */
    public boolean isRowExpanded( String name )
    {
        String expanderXpath = String.format( BROWSE_PANEL_ITEM_EXPANDER, name );

        WebElement rowElement = getDynamicElement( By.xpath( expanderXpath ), 5 );
        if ( rowElement == null )
        {
            throw new TestFrameworkException(
                "invalid locator  or space with name: " + name + " does not exist! xpath =  " + expanderXpath );
        }

        String attributeName = "class";
        String attributeValue = "collapse";
        return waitAndCheckAttrValue( rowElement, attributeName, attributeValue, 1l );
    }


    /**
     * Clicks by "Select All" and selects all items from the table.
     *
     * @return the number of selected rows.
     */
    public int doSelectAll()
    {
        boolean isVisibleLink = waitUntilVisibleNoException( By.xpath( SELECT_ALL_LINK_XPATH ), 2l );
        if ( !isVisibleLink )
        {
            throw new TestFrameworkException( "The link 'Select All' was not found on the page, probably wrong xpath locator" );
        }
        selectAllLink.click();
        return getSelectedRowsNumber();
    }


    /**
     * Gets a number of selected items in the table.
     *
     * @return a number of selected rows.
     */
    public int getSelectedRowsNumber()
    {
        int number = 0;
        List<WebElement> rows = findElements( By.xpath( ALL_ROWS_IN_BROWSE_PANEL_XPATH + "/div[contains(@class,'checkboxsel')]" ) );
        for ( WebElement row : rows )
        {
            if ( waitAndCheckAttrValue( row, "class", "selected", 1l ) )
            {
                number++;
            }
        }
        return number;
    }

    public boolean isRowSelected( String name )
    {
        List<WebElement> rows =
            findElements( By.xpath( String.format( ( DIV_NAMES_VIEW + "/ancestor::div[contains(@class,'slick-cell')]" ), name ) ) );
        if ( rows.size() == 0 )
        {
            throw new TestFrameworkException( "row with content was not found, content name is " + name );
        }
        return waitAndCheckAttrValue( rows.get( 0 ), "class", "selected", 1 );

    }

    /**
     * Clicks by 'Clear Selection' and removes row-selections.
     */
    public void doClearSelection()
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

}
