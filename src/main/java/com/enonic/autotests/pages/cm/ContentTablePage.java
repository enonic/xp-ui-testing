package com.enonic.autotests.pages.cm;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.Space;
import com.enonic.autotests.model.cm.BaseAbstractContent;
import com.enonic.autotests.pages.AbstractTablePage;
import com.enonic.autotests.utils.TestUtils;

/**
 * 'Content Manager' application, the dashboard page.
 * 
 */
public class ContentTablePage extends AbstractTablePage
{
	private static final String TITLE_XPATH = "//button[contains(@class,'home-button') and contains(.,'Content Manager')]";
	
	public static final String SPACES_TABLE_CELLS_XPATH = "//table[contains(@class,'x-grid-table')]//td[contains(@class,'x-grid-cell')]";

	@FindBy(xpath = "//div[@class='toolbar']/button[text()='Duplicate']")
	private WebElement duplicateButton;

	@FindBy(xpath = "//div[@class='toolbar']/button[text()='Open']")
	private WebElement openButton;

	@FindBy(xpath = "//div[@class='toolbar']/button[text()='Move']")
	private WebElement moveButton;

	private static final String SEARCH_INPUT_XPATH = "//input[@class='text-search-field']";
	@FindBy(xpath = SEARCH_INPUT_XPATH)
	private WebElement searchInput;

	

	private String CHECKBOX_ROW_CHECKER = TD_FOLDER_NAME + "/../td[contains(@class,'x-grid-cell-row-checker')]/div";

	private String DIV_CONTENT_NAME_IN_TABLE = "//div[contains(@class,'x-grid-cell-inner ')]//div[@class='admin-tree-description' and descendant::p[contains(.,'%s')]]";
	
	private final String ALL_NAMES_IN_CONTENT_TABLE_XPATH = "//table[contains(@class,'x-grid-table')]//tr[contains(@class,'x-grid-row')]//div[@class='admin-tree-description']/descendant::p";
	
	private final String CONTENT_DETAILS_ALL_NAMES_XPATH = "//div[contains(@id, 'contentDetail')]//div[contains(@class,'admin-selected-item-box')]//p";
	
	private ContentFilterPanel contentFilter;

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public ContentTablePage( TestSession session )
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
	public boolean findContentInTable(BaseAbstractContent content, long timeout, boolean filtered)
	{
		
		 String[] parents = content.getParentNames();
		if (!filtered)
		{
			for (String parentName : parents )
			{
				if (!doExpandFolder(parentName))
				{
					// if parent was not expanded, therefore parent content has no child.
					return false;
				}
			}
		}

		String fullName = TestUtils.getInstance().buildFullNameOfContent(content.getName(), parents);
		getLogger().info("Full name of content: "+ fullName);
		String contentDescriptionXpath = String.format(DIV_CONTENT_NAME_IN_TABLE, fullName);
		boolean result = TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(contentDescriptionXpath), timeout);
		if (result)
		{
			getLogger().info("The  Content  was found in the Table! name:" + content.getDisplayName());
		} else
		{
			getLogger().info("Content  was not found in the Table! name: " + content.getDisplayName());
		}

		return result;
	}

	/**
	 * Check content in the table of contents.
	 * 
	 * @param content
	 * @return true if a content was found in the table, otherwise false.
	 */
	public boolean findContentInTable(BaseAbstractContent content, long timeout)
	{
		return findContentInTable(content, timeout, false);
	}

	/**
	 * Delete contents from a space.
	 * 
	 * @param parentSpace
	 * @param contents
	 */
	public void doDeleteContent(List<BaseAbstractContent> contents)
	{
		String[] parents = contents.get(0).getParentNames();
		// 1. expand all spaces
		for (int i = 0; i < parents.length; i++)
		{
			if (!doExpandFolder(parents[i]))
			{
				throw new TestFrameworkException("Impossible to delete content from folder! Wrong path to the parent folder, "
						+ parents[i] + " , has no child ! ");
			}
		}

		// 2. check for existence and select a content to delete.
		selectContentInTable(contents, parents);

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
		// TODO this should be verified, When it will be finished in wem-web
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
			selectCheckbox(content);
		}

	}

	private void waitAndCheckContent(List<BaseAbstractContent> contents, String... parents)
	{
		for (BaseAbstractContent content : contents)
		{
			boolean isPresent = findContentInTable(content, 2l);
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
	private void selectCheckbox(BaseAbstractContent content)
	{
		//String fullContentName = TestUtils.getInstance().buildFullNameOfContent(contentName, content.getParentNames());
		String fullName = buildFullName(content);
		String contentCheckBoxXpath = String.format(CHECKBOX_ROW_CHECKER, fullName);
		getLogger().info("tries to find the content in a table, fullName of content is :" + fullName);
	
		getLogger().info("Xpath of checkbox for content is :" + contentCheckBoxXpath);
		boolean isPresent = TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(contentCheckBoxXpath), 3l);
		if(!isPresent)
		{
			throw new SaveOrUpdateException("checkbox for content with name : "+ content.getName() + "was not found");
		}
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			
		}
		findElement(By.xpath(contentCheckBoxXpath)).click();


	}
	
	private String buildFullName(BaseAbstractContent content)
	{
       StringBuilder sb = new StringBuilder();
       String[] names = content.getParentNames();
       for(String name:names)
       {
    	   if(!name.startsWith("/"))
    	   {
    		   sb.append("/");
    	   }
    	   sb.append(name);
       }
       sb.append("/").append(content.getName());
       return sb.toString();
       
	}

	/**
	 * Adds the content to a space or folder.
	 * 
	 * @param space
	 * @param content
	 * @param isCloseWizard
	 */
	public void doAddContent(BaseAbstractContent content, boolean isCloseWizard)
	{
		String[] parents = content.getParentNames();
		
		AddNewContentWizard wizard = openAddContentWizard(content.getContentTypeName(), parents);
		
		if (isCloseWizard)
		{
			wizard.doTypeDataSaveAndClose(content);
		} else
		{
			wizard.doTypeDataAndSave(content);
		}

	}

	public void doUpdateContent(BaseAbstractContent content, BaseAbstractContent newcontent)
	{
		boolean isPresent = findContentInTable(content, 2l);
		if (!isPresent)
		{
			throw new TestFrameworkException("The content with name " + content.getName() + " was not found!");
		}

		// 2. check for existence of content in a parent space and select a content fo edit.
		selectCheckbox(content);

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
	public AddNewContentWizard openAddContentWizard(String contentTypeName, String... parentNames)
	{
		String parentName = parentNames[parentNames.length - 1];
		// if parentNames.length == 0, so no need to expand space, new content will be added to the root folder
		if (parentNames.length > 1)
		{
			for (int i = 0; i < parentNames.length - 1; i++)
			{
				if (!doExpandFolder(parentNames[i]))
				{
					throw new TestFrameworkException("Impossible to add content to the  " + parentName + "wrong path to the parent, because "
							+ parentNames[i] + " , has no child ! ");
				}
			}
		}

		// 1. select a checkbox and press the 'New' from toolbar.
		String spaceCheckBoxXpath = String.format(CHECKBOX_ROW_CHECKER, parentName);
		//boolean isPresentCheckbox = TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(spaceCheckBoxXpath), 3l);
		boolean isPresentCheckbox = TestUtils.getInstance().waitAndFind( By.xpath(spaceCheckBoxXpath), getDriver());
		if (!isPresentCheckbox)
		{
			throw new TestFrameworkException("wrong xpath:" + spaceCheckBoxXpath + " or Space with name " + parentName + " was not found!");
		}
		WebElement checkboxElement = getDriver().findElement(By.xpath(spaceCheckBoxXpath));
		
		checkboxElement.click();
		boolean isNewEnabled = TestUtils.getInstance().waitUntilElementEnabledNoException(getSession(), By.xpath(NEW_BUTTON_XPATH), 2l);
		if (!isNewEnabled)
		{
			throw new SaveOrUpdateException("CM application, impossible to open SelectContentTypeDialog, because the 'New' button is disabled!");
		}
		newButton.click();
		SelectContentTypeDialog selectDialog = new SelectContentTypeDialog(getSession());
		boolean isOpened = selectDialog.isOpened();
		if (!isOpened)
		{
			getLogger().error("SelectContentTypeDialog was not opened!", getSession());
			throw new TestFrameworkException(String.format("Error during add content to space %s, dialog was not opened!", parentName));
		}
		getLogger().info("SelectContentTypeDialog, content type should be selected:" + contentTypeName);
		AddNewContentWizard wizard = selectDialog.selectContentType(contentTypeName);
		return wizard;
	}

	public ContentInfoPage doOpenContent(BaseAbstractContent content)
	{
		boolean isPresent = findContentInTable(content, 7l);
		if (!isPresent)
		{
			throw new TestFrameworkException("The content with name " + content.getName() + " and displayName:" + content.getDisplayName()
					+ " was not found!");
		} else
		{
			getLogger().info("doOpenContent::: content with name equals " + content.getDisplayName() + " was found");
		}
		// 2. check for existence of content in a parent space and select a content to open.
		selectCheckbox(content);		
		if (!openButton.isEnabled())
		{
			getLogger().info("'Open' link is disabled!");
			new WebDriverWait(getSession().getDriver(), 2).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='toolbar']/button[text()='Open']")));
		}
		
		openButton.click();
		
		ContentInfoPage cinfo = new ContentInfoPage(getSession());
		int expectedNumberOfPage = 1;
		cinfo.waitUntilOpened(getSession(), content.getDisplayName(), expectedNumberOfPage);
		return cinfo;
	}



	/**
	 * Waits until page loaded.
	 * @param timeout
	 */
	public void waituntilPageLoaded(long timeout)
	{

		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TITLE_XPATH)));
	}

	/**
	 * 
	 * @param session
	 * @return true if 'Content Manager' opened and CMSpacesPage showed, otherwise false.
	 */
	public static boolean isOpened(TestSession session)
	{
		List<WebElement> title = session.getDriver().findElements(By.xpath(TITLE_XPATH));
		List<WebElement> searchInput = session.getDriver().findElements(By.xpath(SEARCH_INPUT_XPATH));
		if (title.size() > 0 && (searchInput.size() > 0  && searchInput.get(0).isDisplayed()))
		{
			return true;
		} else
		{
			return false;
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
