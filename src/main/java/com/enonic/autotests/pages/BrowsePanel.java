package com.enonic.autotests.pages;

import static com.enonic.autotests.utils.SleepHelper.sleep;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

public class BrowsePanel
    extends Application
{
    private final String ALL_ROWS_IN_CONTENT_TABLE_XPATH = "//table[contains(@class,'x-grid-table')]//tr[contains(@class,'x-grid-row')]";

    protected String TD_CONTENT_DISPLAY_NAME = "//table[contains(@class,'x-grid-table')]//td[descendant::h6[text()='%s']]";

    protected String TD_CONTENT_NAME = "//table[contains(@class,'x-grid-table')]//td[descendant::p[text()='%s']]";

    protected String TD_CHILDREN_CONTENT_NAMES = "//table[contains(@class,'x-grid-table')]//td[descendant::p[contains(.,'%s')]]";

    private static String DIV_SCROLL_XPATH = "//table[contains(@class,'x-grid-table-resizer')]/parent::div[contains(@id,'treeview')]";


    private final String CLEAR_SELECTION_LINK_XPATH = "//a[contains(@class,' x-box-item x-toolbar-item') and contains(.,'Clear')]";

    @FindBy(xpath = CLEAR_SELECTION_LINK_XPATH)
    protected WebElement clearSelectionLink;

    private final String SELECT_ALL_LINK_XPATH =
        "//a[contains(@class,'x-toolbar-item') and (contains(.,'Select All') or contains(.,'Select all'))]";

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
     * clicks by 'expand' icon and expands a folder.
     *
     * @param name - the name of content type or contentPath
     * @return true if space is not empty and was expanded, otherwise return
     * false.
     */
    public <T> boolean clickByExpander( T name )
    {
        boolean isExpanderPresent = isExpanderPresent( name );
        if ( !isExpanderPresent )
        {
            getLogger().info( "This object: " + name.toString() + " has no child" );
            return false;
        }
        clickOnExpanderImage( name.toString() );
//        if ( isRowExapnded( element.toString() ) )
//        {
//        	clickOnExpanderImage( element.toString() );
//        }

        return true;
    }

    /**
     * Check if space has child. if the attribute 'class' contains a string
     * "x-grid-tree-node-leaf", so space has no any child.
     *
     * @return true if space has no any children., otherwise true.
     */
    public <T>boolean isExpanderPresent( T contentPath )
    {
        String expanderElement = String.format( TD_CONTENT_NAME + "/div/img[contains(@class,'x-tree-expander')]", contentPath.toString() );
        getLogger().info( "check if present expander for folder:" + contentPath.toString() + " xpath: " + expanderElement );
        boolean isPresent = isDynamicElementPresent( By.xpath( expanderElement ), 2 );
        if ( !isPresent )
        {
            getLogger().info( "expander for folder:" + contentPath.toString() + " was not found! " );
            return false;
        }

        return true;
    }

    public boolean isRowExapnded( String name )
    {
        String trXpath = String.format( TD_CONTENT_NAME + "/parent::tr", name );

        WebElement rowElement = getDynamicElement( By.xpath( trXpath ), 5 );
        if ( rowElement == null )
        {
            throw new TestFrameworkException( "invalid locator  or space with name: " + name + " dose not exists! xpath =  " + trXpath );
        }

        String attributeName = "class";
        String attributeValue = "x-grid-tree-node-expanded";
        return waitAndCheckAttrValue( rowElement, attributeName, attributeValue, 1l );
    }

    /**
     * clicks by expand-icon and expands a space.
     */
    private void clickOnExpanderImage( String name )
    {
        String expanderImgXpath = buildExpanderXpath( name );
        List<WebElement> elems = getSession().getDriver().findElements( By.xpath( expanderImgXpath ) );
        if ( elems.size() == 0 )
        {
            throw new TestFrameworkException( "invalid locator for content-expander or space dose not exist! " + expanderImgXpath );
        }

        if ( !elems.get( 0 ).isDisplayed() )
        {
            WebElement scrolled = scrollTableAndFind( expanderImgXpath, DIV_SCROLL_XPATH );
            if ( scrolled != null )
            {
                scrolled.click();
            }

        }
        else
        {
            elems.get( 0 ).click();
        }
    }

    protected String buildExpanderXpath( String name )
    {
        return String.format( TD_CONTENT_NAME, name ) + "//ancestor::td//img[contains(@class,'x-tree-expander')]";

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
     * @return number of rows in the table of content. The row with header is
     * excluded.
     */
    public int getTableRowNumber()
    {
        List<WebElement> rows = getDriver().findElements( By.xpath( ALL_ROWS_IN_CONTENT_TABLE_XPATH ) );
        return rows.size();
    }

    /**
     * Gets a number of selected items in the table.
     *
     * @return a number of selected rows.
     */
    public int getSelectedRowsNumber()
    {
        int number = 0;
        List<WebElement> rows = getSession().getDriver().findElements( By.xpath( ALL_ROWS_IN_CONTENT_TABLE_XPATH ) );
        for ( WebElement row : rows )
        {
            if ( waitAndCheckAttrValue( row, "class", "x-grid-row-selected", 1l ) )
            {
                number++;
            }
        }
        return number;
    }

    public boolean isRowSelected( String name )
    {
        List<WebElement> rows =
            getSession().getDriver().findElements( By.xpath( String.format( ( TD_CONTENT_NAME + "/parent::tr" ), name ) ) );
        if ( rows.size() == 0 )
        {
            throw new TestFrameworkException( "row with content was not found, content name is " + name );
        }
        return waitAndCheckAttrValue( rows.get( 0 ), "class", "x-grid-row-selected", 1 );

    }

    /**
     * Clicks by 'Clear Selection' and removes row-selections.
     */
    public void doClearSelection()
    {
        sleep( 500 );//mac mimi issue
        boolean isLeLinkVisible = waitUntilVisibleNoException( By.xpath( CLEAR_SELECTION_LINK_XPATH ), 2l );
        if ( !isLeLinkVisible )
        {
            throw new TestFrameworkException( "The link 'Clear Selection' was not found on the page, probably wrong xpath locator" );
        }
        clearSelectionLink.click();
    }

    /**
     * Finds on page 'Clear selection' link and get text.
     *
     * @return for example : 'Clear selection (2)'
     */
    public String getClearSelectionText()
    {
        List<WebElement> elems = getSession().getDriver().findElements( By.xpath( CLEAR_SELECTION_LINK_XPATH ) );
        if ( elems.size() == 0 )
        {
            throw new TestFrameworkException( "the 'Clear selection' Link was not found, probably wrong xpath locator!" );
        }
        return clearSelectionLink.getText();
    }

}
