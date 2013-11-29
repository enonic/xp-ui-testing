package com.enonic.autotests.pages.cm;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.Space;
import com.enonic.autotests.model.cm.BaseAbstractContent;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.utils.TestUtils;

/**
 * 'Content Manager' application, the dashboard page.
 * 
 */
public class CMSpacesPage extends Page
{
	private static final String TITLE_XPATH = "//button[contains(@class,'home-button') and contains(.,'Content Manager')]";
	
	private final String NEW_BUTTON_XPATH = "//div[@class='toolbar']/button[text()='New']";
	@FindBy(xpath = NEW_BUTTON_XPATH)
	private WebElement newButton;
	
	public static final String SPACES_TABLE_CELLS_XPATH = "//table[contains(@class,'x-grid-table')]//td[contains(@class,'x-grid-cell')]";

	@FindBy(xpath = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Edit')]]")
	private WebElement editButton;

	private final String DELETE_BUTTON_XPATH = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Delete')]]";
	@FindBy(xpath = DELETE_BUTTON_XPATH)
	private WebElement deleteButton;

	@FindBy(xpath = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Duplicate')]]")
	private WebElement duplicateButton;

	@FindBy(xpath = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Open')]]")
	private WebElement openButton;

	@FindBy(xpath = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Move')]]")
	private WebElement moveButton;

	private final String SEARCH_INPUT_XPATH = "//input[@class='text-search-field']";
	@FindBy(xpath = SEARCH_INPUT_XPATH)
	private WebElement searchInput;

	private String SPACE_NAME_AND_DISPLAYNAME_IN_TABLE = "//table[contains(@class,'x-grid-table')]//td[contains(@class,'x-grid-cell')]//div[@class='admin-tree-description' and descendant::p[contains(.,'%s')]]";

	private String CHECKBOX_ROW_CHECKER = SPACE_NAME_AND_DISPLAYNAME_IN_TABLE + "//ancestor::td/preceding-sibling::td";

	private String DIV_CONTENT_NAME_IN_TABLE = "//div[contains(@class,'x-grid-cell-inner ')]//div[@class='admin-tree-description' and descendant::p[contains(.,'%s')]]";

	private final String ALL_ROWS_IN_CONTENT_TABLE_XPATH = "//table[contains(@class,'x-grid-table')]//tr[contains(@class,'x-grid-row')]";
	private final String ALL_NAMES_IN_CONTENT_TABLE_XPATH = "//table[contains(@class,'x-grid-table')]//tr[contains(@class,'x-grid-row')]//div[@class='admin-tree-description']/descendant::p";

	private final String SELECT_ALL_LINK_XPATH = "//a[contains(@class,'x-toolbar-item') and (contains(.,'Select All') or contains(.,'Select all'))]";
	@FindBy(xpath = SELECT_ALL_LINK_XPATH)
	private WebElement selectAllLink;

	private final String CLEAR_SELECTION_LINK_XPATH = "//a[contains(@class,'admin-grid-toolbar-btn-clear-selection')]";
	@FindBy(xpath = CLEAR_SELECTION_LINK_XPATH)
	private WebElement clearSelectionLink;

	private final String CONTENT_DETAILS_ALL_NAMES_XPATH = "//div[contains(@id, 'contentDetail')]//div[contains(@class,'admin-selected-item-box')]//p";
	private String CLOSE_CONTENT_DETAILS_ICON_XPATH = "//div[contains(@id, 'contentDetail')]//div[contains(@class,'admin-selected-item-box')]//p[contains(.,'%s')]/../following::div/a";
	
	private static String DIV_SCROLL_XPATH = "//table[contains(@class,'x-grid-table-resizer')]/parent::div[contains(@id,'treeview')]";

	private ContentFilterPanel contentFilter;

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public CMSpacesPage( TestSession session )
	{
		super(session);

	}

	public ContentFilterPanel getContentFilter()
	{
		if (contentFilter == null)
		{
			contentFilter = new ContentFilterPanel(getSession());
		}
		return contentFilter;
	}

	/**
	 * Click by close icon and remove selection from row.
	 * 
	 * @param contentName
	 */
	public void closeContentDetails(String contentName)
	{
		String closeIconXpath = String.format(CLOSE_CONTENT_DETAILS_ICON_XPATH, contentName);
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(closeIconXpath));
		if (elems.size() == 0)
		{
			throw new TestFrameworkException("Impossible to close content details, content or close icon were not found!");
		}
		elems.get(0).click();
	}

	/**
	 * Gets content's names from 'Details panel'.
	 * 
	 * @return list of names, or empty list if there are no seleted items
	 */
	public List<String> getNamesFromContentDetails()
	{
		List<String> contentNames = new ArrayList<>();
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(CONTENT_DETAILS_ALL_NAMES_XPATH));
		for (WebElement el : elems)
		{
			contentNames.add(el.getText());
		}
		return contentNames;
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
	 * Gets all content names, showed in the contents-table.
	 * 
	 * @return list of names.
	 */
	public List<String> getShowedContentNames()
	{
		List<String> allNames = new ArrayList<>();
		List<WebElement> rows = getSession().getDriver().findElements(By.xpath(ALL_NAMES_IN_CONTENT_TABLE_XPATH));
		for (WebElement row : rows)
		{
			allNames.add(row.getText());
		}
		return allNames;

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
	 * Finds content by name. Filtering was applied and content filtered.
	 * 
	 * @param contentName
	 *            the name of the content.
	 * @param timeout
	 * @param filtered
	 * @param parents
	 *            - names of parent spaces for this content, this names should
	 *            be present in the full name of content.
	 * @return true if content was found, otherwise return false.
	 */
	public boolean findContentInTable(String contentName, long timeout, boolean filtered, String... parents)
	{
		if (!filtered)
		{
			for (String parentName : parents)
			{
				if (!doExpand(parentName))
				{
					// if parent was not expanded, therefore parent content has no child.
					return false;
				}
			}
		}

		String fullName = TestUtils.getInstance().buildFullNameOfContent(contentName, parents);
		String contentDescriptionXpath = String.format(DIV_CONTENT_NAME_IN_TABLE, fullName);
		getLogger().info("Check is content present in the table: " + contentName);
		boolean result = TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(contentDescriptionXpath), timeout);
		if (result)
		{
			getLogger().info("The  Content  was found in the Table! name:" + contentName);
		} else
		{
			getLogger().info("Content  was not found in the Table! name: " + contentName);
		}

		return result;
	}

	/**
	 * Check content in the table of contents.
	 * 
	 * @param content
	 * @return true if a content was found in the table, otherwise false.
	 */
	public boolean findContentInTable(String contentName, long timeout, String... parents)
	{
		return findContentInTable(contentName, timeout, false, parents);
	}

	/**
	 * clicks by 'expand' icon and expands a space.
	 * 
	 * @param parentName
	 * @return true if space is not empty and was expanded, otherwise return
	 *         false.
	 */
	public boolean doExpand(String parentName)
	{
		boolean isEmptySpace = isContentOrSpaceHasNoChild(parentName);
		if (isEmptySpace)
		{
			getLogger().info("The space: " + parentName + " has no contents");
			return false;
		}
		if (!isContentOrSpaceExapnded(parentName))
		{
			expandSpace(parentName);
			boolean isExpanded = isContentOrSpaceExapnded(parentName);
			if (!isExpanded)
			{
				throw new TestFrameworkException("space " + parentName + " was not expanded");
			}
			getLogger().info("parentContent:" + parentName + " expanded == " + isExpanded);
		}

		return true;
	}

	private String getTableRowElementXpath(String name)
	{
		String tdWithName = String.format(SPACE_NAME_AND_DISPLAYNAME_IN_TABLE, name);
		String toParentRow = "/parent::div/parent::td/parent::tr";
		String result = tdWithName + toParentRow;
		return result;
	}

	private String buildContentExpanderXpath(String name)
	{
		return String.format(SPACE_NAME_AND_DISPLAYNAME_IN_TABLE, name) + "//ancestor::td//img[contains(@class,'x-tree-expander')]";

	}

	/**
	 * Check if space has child. if the attribute 'class' contains a string
	 * "x-grid-tree-node-leaf", so space has no any child.
	 * 
	 * @param parentSpace
	 * @return true if space has no any children., otherwise true.
	 */
	private boolean isContentOrSpaceHasNoChild(String parentName)
	{
		String trXpath = getTableRowElementXpath(parentName);
		WebElement contentTableRow = null;
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(trXpath));
		if (elems.size() == 0)
		{
			throw new TestFrameworkException("invalid locator for space-expander or space dose not exists! " + trXpath);
		}
		
		if (!elems.get(0).isDisplayed())
		{ 
			contentTableRow = TestUtils.getInstance().scrollTableAndFind(getSession(), trXpath, DIV_SCROLL_XPATH);
			if(contentTableRow == null)
			{
				throw new TestFrameworkException("content scrolled, but the row was not found: "+trXpath+ "probably xpath is wrong!");
			}
			
		} else
		{

			contentTableRow = elems.get(0);
		}
		String attributeName = "class";
		String attributeValue = "x-grid-tree-node-leaf";
		return TestUtils.getInstance().waitAndCheckAttrValue(getSession().getDriver(), contentTableRow, attributeName, attributeValue, 1l);
	}

	private boolean isContentOrSpaceExapnded(String name)
	{
		String trXpath = getTableRowElementXpath(name);
		WebElement contentTableRow = null;
		//WebElement contentTableRow = TestUtils.getInstance().getIfDisplayed(By.xpath(trXpath), getSession().getDriver());
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(trXpath));
		if (elems.size() == 0)
		{
			throw new TestFrameworkException("invalid locator for space-expander or space dose not exists! " + trXpath);
		}
		
		if (!elems.get(0).isDisplayed())
		{
			contentTableRow = TestUtils.getInstance().scrollTableAndFind(getSession(), trXpath, DIV_SCROLL_XPATH);
			if(contentTableRow == null)
			{
				throw new TestFrameworkException("content scrolled, but the row was not found: "+trXpath+ "probably xpath is wrong!");
			}
		}else{
			contentTableRow = elems.get(0);
		}
		String attributeName = "class";
		String attributeValue = "x-grid-tree-node-expanded";
		return TestUtils.getInstance().waitAndCheckAttrValue(getSession().getDriver(), contentTableRow, attributeName, attributeValue, 1l);
	}

	/**
	 * Delete contents from a space.
	 * 
	 * @param parentSpace
	 * @param contents
	 */
	public void doDeleteContent(List<BaseAbstractContent> contents, String... parentsNames)
	{
		String parentName = parentsNames[parentsNames.length - 1];
		// 1. expand all spaces
		for (int i = 0; i < parentsNames.length; i++)
		{
			if (!doExpand(parentsNames[i]))
			{
				throw new TestFrameworkException("Impossible to delete content from  " + parentName + "wrong path to the parent, because "
						+ parentsNames[i] + " , has no child ! ");
			}
		}

		// 2. check for existence and select a content to delete.
		selectContentInTable(contents, parentsNames);

		List<String> displayNamesToDelete = new ArrayList<>();
		for (BaseAbstractContent c : contents)
		{
			displayNamesToDelete.add(c.getDisplayName());
		}
		// 3. check if enabled 'Delete' link.
		boolean isEnabledDeleteButton = TestUtils.getInstance().waitUntilElementEnabledNoException(getSession(), By.xpath(DELETE_BUTTON_XPATH), 2l);
		if (!isEnabledDeleteButton)
		{
			throw new SaveOrUpdateException("CM application, impossible to delete content, because the 'Delete' button is disabled!");
		}
		// 4. click by 'Delete' link and open a confirm dialog.
		deleteButton.click();

		DeleteContentDialog dialog = new DeleteContentDialog(getSession(), displayNamesToDelete);
		boolean isOpened = dialog.verifyIsOpened();
		if (!isOpened)
		{
			throw new TestFrameworkException("Confirm 'delete content' dialog was not opened!");
		}
		// 5. press the button "Delete" on the dialog.
		dialog.doDelete();
		boolean isClosed = dialog.verifyIsClosed();
		if (!isClosed)
		{
			throw new TestFrameworkException("Confirm 'delete content' dialog was not closed!");
		}
		// 6. Verify notification message:
		// TODO to implement
	}

	/**
	 * clicks by expand-icon and expands a space.
	 * 
	 * @param parentSpace
	 */
	private void expandSpace(String parentName)
	{
		String expanderImgXpath = buildContentExpanderXpath(parentName);
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

	/**
	 * Selects a content in a space or folder, throws exception if content was
	 * not found.
	 * 
	 * @param parentSpace
	 * @param contents
	 */
	private void selectContentInTable(List<BaseAbstractContent> contents, String... parentNames)
	{
		waitAndCheckContent(contents, parentNames);
		for (BaseAbstractContent content : contents)
		{
			selectCheckboxInRow(content.getName(), parentNames);
		}

	}

	private void waitAndCheckContent(List<BaseAbstractContent> contents, String... parents)
	{
		for (BaseAbstractContent content : contents)
		{
			boolean isPresent = findContentInTable(content.getName(), 2l, parents);
			if (!isPresent)
			{
				throw new TestFrameworkException("The content with name " + content.getName() + " was not found!");
			}
		}

	}

	/**
	 * Clicks by a checkbox, linked with content and select row in the table.
	 * 
	 * @param parentSpaceName
	 * @param content
	 */
	private void selectCheckboxInRow(String contentName, String... parentNames)
	{
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String fullContentName = TestUtils.getInstance().buildFullNameOfContent(contentName, parentNames);
		String contentCheckBoxXpath = String.format(CHECKBOX_ROW_CHECKER, fullContentName);
		getLogger().info("tries to find the content in a table, fullName of content is :" + fullContentName);
		List<WebElement> checkboxes = getSession().getDriver().findElements(By.xpath(contentCheckBoxXpath));
		getLogger().info("selectCheckboxInRow, checkboxes size: " + checkboxes.size());
		getLogger().info("Xpath of checkbox for content is :" + contentCheckBoxXpath);
		if (checkboxes.size() > 1)
		{
			getLogger().warning("more than one checkbox were found, but should be only one");
		}

		getLogger().info("tries to select the content in a table, fullName of content is :" + fullContentName);
		checkboxes.get(0).click();

	}

	private void selectContentInContextMenu()
	{
		// TODO to implement.
	}

	/**
	 * Adds the content to a space or folder.
	 * 
	 * @param space
	 * @param content
	 * @param isCloseWizard
	 */
	public void doAddContent(BaseAbstractContent content, boolean isCloseWizard, String... parents)
	{
		AddNewContentWizard wizard = openAddContentWizard(content.getType(), parents);
		if (isCloseWizard)
		{
			wizard.doTypeDataSaveAndClose(content);
		} else
		{
			wizard.doTypeDataAndSave(content);
		}

	}

	public void doUpdateContent(BaseAbstractContent content, BaseAbstractContent newcontent, String... parentNames)
	{
		boolean isPresent = findContentInTable(content.getName(), 2l, parentNames);
		if (!isPresent)
		{
			throw new TestFrameworkException("The content with name " + content.getName() + " was not found!");
		}

		// 2. check for existence of content in a parent space and select a
		// content to delete.
		selectCheckboxInRow(content.getName(), parentNames);

		editButton.click();
		AddNewContentWizard wizard = new AddNewContentWizard(getSession());
		wizard.waitUntilWizardOpened(content.getDisplayName(), 1);
		wizard.doTypeDataSaveAndClose(newcontent);

	}

	public AddNewContentWizard openToEdit(Space space, String ctype)
	{
		AddNewContentWizard wizard = null;
		return wizard;
	}

	/**
	 * Select a content type and opens "Add new Content Wizard".
	 * 
	 * @param space
	 * @param ctype
	 * @return
	 */
	public AddNewContentWizard openAddContentWizard(String ctype, String... parentNames)
	{
		String parentName = parentNames[parentNames.length - 1];
		// if parentNames.length == 0, so no need to expand space, new content will be added to the root folder
		if (parentNames.length > 1)
		{
			for (int i = 0; i < parentNames.length - 1; i++)
			{
				if (!doExpand(parentNames[i]))
				{
					throw new TestFrameworkException("Impossible to add content to the  " + parentName + "wrong path to the parent, because "
							+ parentNames[i] + " , has no child ! ");
				}
			}
		}

		// 1. select a checkbox and press the 'New' from toolbar.
		String spaceCheckBoxXpath = String.format(CHECKBOX_ROW_CHECKER, parentName);
		boolean isPresentCheckbox = TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(spaceCheckBoxXpath), 3l);
		if (!isPresentCheckbox)
		{
			throw new TestFrameworkException("wrong xpath:" + spaceCheckBoxXpath + " or Space with name " + parentName + " was not found!");
		}
		List<WebElement> checkboxes = getSession().getDriver().findElements(By.xpath(spaceCheckBoxXpath));
		if (checkboxes.size() > 1)
		{
			getLogger().warning("more than one checkbox were found, but should be only one");
		}
		checkboxes.get(0).click();
		boolean isEnabled = TestUtils.getInstance().waitUntilElementEnabledNoException(getSession(), By.xpath(NEW_BUTTON_XPATH), 2l);
		if (!isEnabled)
		{
			throw new SaveOrUpdateException("CM application, impossible to open SelectContentTypeDialog, because the 'New' button is disabled!");
		}
		newButton.click();
		SelectContentTypeDialog selectDialog = new SelectContentTypeDialog(getSession());
		boolean isOpened = selectDialog.verifyIsOpened();
		if (!isOpened)
		{
			getLogger().error("SelectContentTypeDialog was not opened!", getSession());
			throw new TestFrameworkException(String.format("Error during add content to space %s, dialog was not opened!", parentName));
		}
		getLogger().info("SelectContentTypeDialog, content type should be selected:" + ctype);
		AddNewContentWizard wizard = selectDialog.selectContentType(ctype);
		return wizard;
	}

	public ContentInfoPage doOpenContent(BaseAbstractContent content, String... parentNames)
	{
		boolean isPresent = findContentInTable(content.getName(), 2l, parentNames);
		if (!isPresent)
		{
			throw new TestFrameworkException("The content with name " + content.getName() + " and displayName:" + content.getDisplayName()
					+ " was not found!");
		} else
		{
			getLogger().info("doOpenContent::: content with name equals " + content.getDisplayName() + " was found");
		}
		// 2. check for existence of content in a parent space and select a
		// content to delete.
		selectCheckboxInRow(content.getName(), parentNames);
		if (!openButton.isEnabled())
		{
			getLogger().info("'Open' link is disabled!");
		}
		openButton.click();
		ContentInfoPage cinfo = new ContentInfoPage(getSession());
		cinfo.waitUntilOpened(getSession(), content.getDisplayName(), 1);
		return cinfo;
	}

	/**
	 * Click by checkboxes and select contents in the table.
	 * 
	 * @param contentNames, list of names, contents with these names will be selected in the table.
	 * @param parentNames
	 */
	public void selectContentsInTable(List<String> contentNames, String... parentNames)
	{

		if (parentNames.length != 0)
		{
			String parentName = parentNames[parentNames.length - 1];
			// 1. expand all spaces
			for (int i = 0; i < parentNames.length; i++)
			{
				if (!doExpand(parentNames[i]))
				{
					throw new TestFrameworkException("Impossible to delete content from  " + parentName + "wrong path to the parent, because "
							+ parentNames[i] + " , has no child ! ");
				}
			}

		}

		// 2. check for existence and select a content to delete.
		for (String name : contentNames)
		{
			selectCheckboxInRow(name, parentNames);

		}

	}


	/**
	 * Waits until page loaded.
	 * @param timeout
	 */
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SPACES_TABLE_CELLS_XPATH)));
	}

	/**
	 * 
	 * @param session
	 * @return true if 'Content Manager' opened and CMSpacesPage showed, otherwise false.
	 */
	public static boolean isOpened(TestSession session)
	{
		List<WebElement> elems = session.getDriver().findElements(By.xpath(TITLE_XPATH));
		List<WebElement> queryElement = session.getDriver().findElements(By.name("text-search-field"));
		if (elems.size() == 0 && queryElement.size() == 0 )
		{
			return false;
		} else
		{
			return true;
		}
	}

	public boolean verifyTitle()
	{
		return TestUtils.getInstance().waitAndFind(By.xpath(TITLE_XPATH), getDriver());
	}
	public boolean verifyAllControls()
	{
		boolean result = true;
		result &= verifyTollbar();
		result &= verifySearchPannel();
		return result;
	}

	private boolean verifyTollbar()
	{
		boolean result = true;
		result &= newButton.isDisplayed() && !newButton.isEnabled();
		result &= editButton.isDisplayed() && !editButton.isEnabled();
		result &= deleteButton.isDisplayed() && !deleteButton.isEnabled();
		result &= duplicateButton.isDisplayed() && !duplicateButton.isEnabled();
		result &= openButton.isDisplayed() && !openButton.isEnabled();
		result &= moveButton.isDisplayed() && !moveButton.isEnabled();
		return result;
	}

	private boolean verifySearchPannel()
	{
		boolean result = true;
		result &= searchInput.isDisplayed() && !searchInput.isEnabled();
		return result;
	}

}
