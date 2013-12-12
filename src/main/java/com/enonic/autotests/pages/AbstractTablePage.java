package com.enonic.autotests.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.TestUtils;

public class AbstractTablePage extends Page
{

	protected  final String NEW_BUTTON_XPATH = "//div[@class='toolbar']/button[text()='New']";
	@FindBy(xpath = NEW_BUTTON_XPATH)
	protected WebElement newButton;
	
	private final String CLEAR_SELECTION_LINK_XPATH = "//a[contains(@class,'admin-grid-toolbar-btn-clear-selection')]";
	@FindBy(xpath = CLEAR_SELECTION_LINK_XPATH)
	protected WebElement clearSelectionLink;
	

	private final String SELECT_ALL_LINK_XPATH = "//a[contains(@class,'x-toolbar-item') and (contains(.,'Select All') or contains(.,'Select all'))]";
	@FindBy(xpath = SELECT_ALL_LINK_XPATH)
	protected WebElement selectAllLink;
	
	@FindBy(xpath = "//div[@class='toolbar']/button[text()='Edit']")
	protected WebElement editButton;

	protected final String DELETE_BUTTON_XPATH = "//div[@class='toolbar']/button[text()='Delete']";
	@FindBy(xpath = DELETE_BUTTON_XPATH)
	protected WebElement deleteButton;
	
	private final String ALL_ROWS_IN_CONTENT_TABLE_XPATH = "//table[contains(@class,'x-grid-table')]//tr[contains(@class,'x-grid-row')]";
	
    protected String TD_FOLDER_DISPLAYNAME = "//table[contains(@class,'x-grid-table')]//td[descendant::h6[text()='%s']]";
	
	protected String TD_FOLDER_NAME = "//table[contains(@class,'x-grid-table')]//td[descendant::p[text()='%s']]";
	
	private static String DIV_SCROLL_XPATH = "//table[contains(@class,'x-grid-table-resizer')]/parent::div[contains(@id,'treeview')]";

	/**
	 * The Constructor 
	 * 
	 * @param session
	 */
	public AbstractTablePage( TestSession session )
	{
		super(session);
	}
	
	
	/**
	 * clicks by 'expand' icon and expands a folder.
	 * 
	 * @param parentName
	 * @return true if space is not empty and was expanded, otherwise return
	 *         false.
	 */
	public boolean doExpandFolder(String parentName)
	{
		boolean isExpanderPresent = isExpanderPresent(parentName);
		if (!isExpanderPresent)
		{
			getLogger().info("The folder: " + parentName + " has no contents");
			TestUtils.getInstance().saveScreenshot(getSession());
			return false;
		}
		if (!isRowExapnded(parentName))
		{
			clickByExpanderIcon(parentName);
			boolean isExpanded = isRowExapnded(parentName);
			if (!isExpanded)
			{
				throw new TestFrameworkException("folder " + parentName + " was not expanded");
			}
			getLogger().info("parentContent:" + parentName + " expanded == " + isExpanded);
		}

		return true;
	}
	
	/**
	 * Check if space has child. if the attribute 'class' contains a string
	 * "x-grid-tree-node-leaf", so space has no any child.
	 * 
	 * @param parentSpace
	 * @return true if space has no any children., otherwise true.
	 */
	private boolean isExpanderPresent(String parentName)
	{
		String expanderElement = String.format(TD_FOLDER_NAME +"/div/img[contains(@class,'x-tree-expander')]", parentName);
		getLogger().info("check if present expander for folder:"+ parentName +" xpath: " + expanderElement);
		//boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(expanderElement), getDriver());
		//findElement(By.xpath(expanderElement));
		//boolean isPresent = TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(expanderElement), 3l);
		boolean isPresent = TestUtils.getInstance().findDinamicElement(getSession().getDriver(),  By.xpath(expanderElement), 5);
		if (!isPresent)
		{
			getLogger().info("expander for folder:"+ parentName +" was not found! ");
			return false;
		}
		
		//check if dispalyed:
//		if (!findElement(By.xpath(expanderElement)).isDisplayed())
//		{ 
//			TestUtils.getInstance().scrollTableAndFind(getSession(), expanderElement, DIV_SCROLL_XPATH);
//			
//		}
		return true;
	}

	private boolean isRowExapnded(String name)
	{
		String trXpath = String.format(TD_FOLDER_NAME +"/parent::tr", name);
		
		//List<WebElement> elems = getSession().getDriver().findElements(By.xpath(trXpath));
		//boolean isRowPresent = TestUtils.getInstance().waitAndFind(By.xpath(trXpath), getDriver());
		boolean isRowPresent = TestUtils.getInstance().findDinamicElement(getSession().getDriver(), By.xpath(trXpath), 5);
		if (!isRowPresent)
		{
			throw new TestFrameworkException("invalid locator  or space with name: "+ name+ " dose not exists! xpath =  " + trXpath);
		}
		
//		if (!findElement(By.xpath(trXpath)).isDisplayed())
//		{
//			 TestUtils.getInstance().scrollTableAndFind(getSession(), trXpath, DIV_SCROLL_XPATH);
//			
//		}
		String attributeName = "class";
		String attributeValue = "x-grid-tree-node-expanded";
		return TestUtils.getInstance().waitAndCheckAttrValue(getDriver(), findElement(By.xpath(trXpath)), attributeName, attributeValue, 1l);
	}

	/**
	 * clicks by expand-icon and expands a space.
	 * 
	 * @param parentSpace
	 */
	private void clickByExpanderIcon(String parentName)
	{
		String expanderImgXpath = buildFolderExpanderXpath(parentName);
	    List<WebElement> elems = getSession().getDriver().findElements(By.xpath(expanderImgXpath));
		if (elems.size() == 0)
		{
			throw new TestFrameworkException("invalid locator for content-expander or space dose not exist! " + expanderImgXpath);
		}
				
		if (!elems.get(0).isDisplayed())
		{ 
			WebElement scrolled = TestUtils.getInstance().scrollTableAndFind(getSession(),expanderImgXpath ,DIV_SCROLL_XPATH);
			scrolled.click();
		} else
		{

			elems.get(0).click();
		}
	}

	protected String buildFolderExpanderXpath(String name)
	{
		return String.format(TD_FOLDER_NAME, name) + "//ancestor::td//img[contains(@class,'x-tree-expander')]";

	}
	/**
	 * Clicks by "Select All" and selects all items from the table.
	 * 
	 * @return the number of selected rows.
	 */
	public int doSelectAll()
	{
		boolean isVisibleLink = TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(SELECT_ALL_LINK_XPATH), 2l);
		if (!isVisibleLink)
		{
			throw new TestFrameworkException("The link 'Select All' was not found on the page, probably wrong xpath locator");
		}
		selectAllLink.click();
		return getSelectedRowNumber();
	}
	

	/**
	 * @return number of rows in the table of content. The row with header is
	 *         excluded.
	 */
	public int getTableRowNumber()
	{
		List<WebElement> rows = getSession().getDriver().findElements(By.xpath(ALL_ROWS_IN_CONTENT_TABLE_XPATH));
		return rows.size();
	}
	
	/**
	 * Gets a number of selected items in the table.
	 * 
	 * @return a number of selected rows.
	 */
	public int getSelectedRowNumber()
	{
		int number = 0;
		List<WebElement> rows = getSession().getDriver().findElements(By.xpath(ALL_ROWS_IN_CONTENT_TABLE_XPATH));
		for (WebElement row : rows)
		{
			if (TestUtils.getInstance().waitAndCheckAttrValue(getSession().getDriver(), row, "class", "x-grid-row-selected", 1l))
			{
				number++;
			}
		}
		return number;
	}
	
	/**
	 * Clicks by 'Clear Selection' and removes row-selections.
	 */
	public void doClearSelection()
	{
		boolean isVisibleLink = TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(CLEAR_SELECTION_LINK_XPATH), 2l);
		if (!isVisibleLink)
		{
			throw new TestFrameworkException("The link 'Select All' was not found on the page, probably wrong xpath locator");
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
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(CLEAR_SELECTION_LINK_XPATH));
		if (elems.size() == 0)
		{
			throw new TestFrameworkException("the 'Clear selection' Link was not found, probably wrong xpath locator!");
		}
		return clearSelectionLink.getText();
	}
	
	public void selectRow(String displayName)
	{
		String rowXpath = String.format(TD_FOLDER_DISPLAYNAME, displayName);
		TestUtils.getInstance().waitAndFind(By.xpath(rowXpath), getDriver());
		//findElement(By.xpath(rowXpath)).click();
		
		Actions builder = new Actions(getDriver()); 
		builder.click(findElement(By.xpath(rowXpath))).build().perform();
	}

}
