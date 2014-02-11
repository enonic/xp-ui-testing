package com.enonic.autotests.pages.contentmanager.browsepanel;

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
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.pages.HomePage;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.SleepWaitHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;

/**
 * 'Content Manager' application, the dashboard page.
 * 
 */
public class ContentBrowsePanel extends BrowsePanel
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

	private String CHECKBOX_ROW_CHECKER = TD_CONTENT_NAME + "/..//div[@class='x-grid-row-checker']";

	private String DIV_CONTENT_NAME_IN_TABLE = "//div[contains(@class,'x-grid-cell-inner ')]//div[@class='admin-tree-description' and descendant::p[contains(.,'%s')]]";
	
	private final String ALL_NAMES_IN_CONTENT_TABLE_XPATH = "//table[contains(@class,'x-grid-table')]//tr[contains(@class,'x-grid-row')]//div[@class='admin-tree-description']/descendant::p";
	
	private final String CONTENT_DETAILS_ALL_NAMES_XPATH = "//div[contains(@id, 'contentDetail')]//div[contains(@class,'admin-selected-item-box')]//p";
	
	private FilterContentPanel contentFilter;

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public ContentBrowsePanel(TestSession session)
	{
		super(session);
	}

	public FilterContentPanel getContentFilter()
	{
		if (contentFilter == null)
		{
			contentFilter = new FilterContentPanel(getSession());
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
		List<WebElement> elems = getDriver().findElements(By.xpath(CONTENT_DETAILS_ALL_NAMES_XPATH));
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
		List<WebElement> rows = getDriver().findElements(By.xpath(ALL_NAMES_IN_CONTENT_TABLE_XPATH));
		for (WebElement row : rows)
		{
			allNames.add(row.getText());
		}
		return allNames;
	}

	/**
	 * Finds content by name. Filtering was applied and content filtered.
	 * 
	 * @param contentName the name of the content.
	 * @param timeout
	 * @param filtered
	 
	 * @return true if content was found, otherwise return false.
	 */
	public boolean findContentInTable(BaseAbstractContent content, long timeout, boolean filtered)
	{		
		String[] contentPath = content.getContentPath();
		if (!filtered && contentPath != null)
		{
			for (String parentName : contentPath )
			{
				if (!doExpandFolder(parentName))
				{
					// if parent was not expanded, therefore parent content has no child.
					return false;
				}
			}
		}

		String fullName = TestUtils.buildFullNameOfContent(content.getName(), contentPath);
		getLogger().info("Full name of content: "+ fullName);
		String contentDescriptionXpath = String.format(DIV_CONTENT_NAME_IN_TABLE, fullName);
		boolean result = SleepWaitHelper.waitUntilVisibleNoException(getDriver(), By.xpath(contentDescriptionXpath), timeout);
		if (result)
		{
			getLogger().info("The Content  was found in the Table! name:" + content.getDisplayName());
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
	 * @param contentPath
	 */
	private void doExpandAllFoldersFromContentPath(String[] contentPath)
	{
		
		if(contentPath != null)
		{
			for (int i = 0; i < contentPath.length; i++)
			{
				if (!doExpandFolder(contentPath[i]))
					{
						throw new TestFrameworkException("Impossible to expand a folder! Wrong path to the parent folder, "
						+ contentPath[i] + " , has no child ! ");
					}
			}
		}	
	}

	/**
	 * Delete contents from a space.
	 * 
	 * @param parentSpace
	 * @param contents
	 */
	public void doDeleteContent(List<BaseAbstractContent> contents)
	{
		DeleteContentDialog dialog = openDeleteContentDialog(contents);
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
 
	public DeleteContentDialog openDeleteContentDialog(List<BaseAbstractContent> contents)
	{
		String[] contentPath = contents.get(0).getContentPath();
        // 1. expand all folders
		doExpandAllFoldersFromContentPath(contentPath);
	
		// 2. check for existence and select a content to delete.
		selectContentInTable(contents, contentPath);
		
		// 3. check if enabled 'Delete' link.
		boolean isEnabledDeleteButton = SleepWaitHelper.waitUntilElementEnabledNoException(getDriver(), By.xpath(DELETE_BUTTON_XPATH), 2l);
		if (!isEnabledDeleteButton)
		{
			throw new SaveOrUpdateException("CM application, impossible to delete content, because the 'Delete' button is disabled!");
		}
		// 4. click by 'Delete' link and open a confirm dialog.
		deleteButton.click();
		DeleteContentDialog dialog = new DeleteContentDialog(getSession());
		return dialog;
	}
	

	/**
	 * Selects a content in a space or folder, throws exception if content was
	 * not found.
	 * 
	 * @param parentSpace
	 * @param contents
	 */
	private void selectContentInTable(List<BaseAbstractContent> contents, String... contentPath)
	{
		waitAndCheckContent(contents, contentPath);
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
		String fullName = TestUtils.buildFullNameOfContent(content.getName(), content.getContentPath());
		String contentCheckBoxXpath = String.format(CHECKBOX_ROW_CHECKER, fullName);
		getLogger().info("tries to find the content in a table, fullName of content is :" + fullName);
	
		getLogger().info("Xpath of checkbox for content is :" + contentCheckBoxXpath);
		boolean isPresent = SleepWaitHelper.waitUntilVisibleNoException(getDriver(), By.xpath(contentCheckBoxXpath), 3l);
		if(!isPresent)
		{
			throw new SaveOrUpdateException("checkbox for content with name : "+ content.getName() + "was not found");
		}
		SleepWaitHelper.sleep(1000);
		findElement(By.xpath(contentCheckBoxXpath)).click();
	}

	/**
	 * Adds the content to a space or folder.
	 * 
	 * @param space
	 * @param content
	 * @param isCloseWizard
	 */
	public void doAddContent(BaseAbstractContent content, boolean isWizardShouldBeClosed)
	{
		String[] contentPath = content.getContentPath();	
		ContentWizardPanel wizard = openContentWizardPanel(content.getContentTypeName(), contentPath);		
		if (isWizardShouldBeClosed)
		{
			wizard.doTypeDataSaveAndClose(content);
			ContentBrowsePanel panel = new ContentBrowsePanel(getSession());
			panel.waituntilPageLoaded(Application.PAGELOAD_TIMEOUT);
		} else
		{
			wizard.doTypeDataAndSave(content);
		}

	}

	public void doUpdateContent(BaseAbstractContent content, BaseAbstractContent newcontent)
	{
		ContentWizardPanel wizard = openEditWizardPage(content);
		wizard.doTypeDataSaveAndClose(newcontent);
		ContentBrowsePanel panel = new ContentBrowsePanel(getSession());
		panel.waituntilPageLoaded(Application.PAGELOAD_TIMEOUT);
	}
	
	public ContentWizardPanel openEditWizardPage(BaseAbstractContent content)
	{
		boolean isPresent = findContentInTable(content, 2l);
		if (!isPresent)
		{
			throw new TestFrameworkException("The content with name " + content.getName() + " was not found!");
		}
		// 2. check out is content present  in a parent space and select it to edit.
		selectCheckbox(content);
		editButton.click();
		ContentWizardPanel wizard = new ContentWizardPanel(getSession());
		wizard.waitUntilWizardOpened( 1);
		return wizard;
	}

	/**
	 * Select a content type and opens "Add new Content Wizard".
	 * 
	 * @param space
	 * @param ctype
	 * @return
	 */
	public ContentWizardPanel openContentWizardPanel(String contentTypeName, String... contentPath)
	{
		//1. click by a checkbox and select a parent folder 
		selectParentFolderForContent(contentPath);
		return selectKindOfContentAndOpenWizardPanel(contentTypeName);
	}
	
	/**
	 * Clicks by 'New' button from toolbar and open a dialog with title: "What do you want to create?"
	 * 
	 * @param contentTypeName the kind of content
	 * @return {@ContentWizardPanel} instance.
	 */
	private ContentWizardPanel selectKindOfContentAndOpenWizardPanel(String contentTypeName)
	{
		// click by 'New' button from the toolbar
		newButton.click();
		NewContentDialog newContentDialog = new NewContentDialog(getSession());
		boolean isOpened = newContentDialog.isOpened();
		if (!isOpened)
		{
			//getLogger().error("NewContentDialog was not opened!", getSession());
			throw new TestFrameworkException("Error during add content, NewContentDialog dialog was not opened!");
		}
		getLogger().info("NewContentDialog, content type should be selected:" + contentTypeName);
		ContentWizardPanel wizard = newContentDialog.selectContentType(contentTypeName);
		return wizard;
	}
	/**
	 * Expands all folders, that present in the 'content path' and  clicks by a checkbox related to parent folder for new content.
	 * 
	 * @param contentPath
	 */
	private void selectParentFolderForContent(String[] contentPath)
	{
		String parentContent = null;
		if (contentPath != null)
		{
			parentContent = contentPath[contentPath.length - 1];

			if (contentPath.length > 1)
			 {
                doExpandAllFoldersFromContentPath(contentPath);
			 }
			// 1. select a checkbox and press the 'New' from toolbar.
			String spaceCheckBoxXpath = String.format(CHECKBOX_ROW_CHECKER, parentContent);
			boolean isPresentCheckbox = isDynamicElementPresent(By.xpath(spaceCheckBoxXpath), 3);
			
			//TODO workaround: issue with empty grid(this is a application issue, it  will be fixed some later )
			if (!isPresentCheckbox)
			{
				TestUtils.saveScreenshot(getSession());
				openHomePage();
				HomePage homepage = new HomePage(getSession());
				homepage.openContentManagerApplication();
				TestUtils.saveScreenshot(getSession());
				//getDriver().navigate().refresh();
				SleepWaitHelper.sleep(1000);
			}
			isPresentCheckbox = isDynamicElementPresent(By.xpath(spaceCheckBoxXpath), 3);
			if (!isPresentCheckbox)
			{
				TestUtils.saveScreenshot(getSession());
				throw new TestFrameworkException("Time: "+TestUtils.timeNow()+ "  wrong xpath:" + spaceCheckBoxXpath + " or Space with name " + parentContent + " was not found!");
			}
			WebElement checkboxElement = getDriver().findElement(By.xpath(spaceCheckBoxXpath));

			checkboxElement.click();
			//selectRowByContentDisplayName(parentName);
			boolean isNewEnabled = SleepWaitHelper.waitUntilElementEnabledNoException(getDriver(), By.xpath(NEW_BUTTON_XPATH), 2l);
			if (!isNewEnabled)
			{
				throw new SaveOrUpdateException("CM application, impossible to open NewContentDialog, because the 'New' button is disabled!");
			}
		}
	}

	/**
	 * Clicks by row with content and  clicks by 'Open' button.
	 * 
	 * @param content
	 * @return {@ItemViewPanelPage} instance
	 */
	public ItemViewPanelPage doOpenContent(BaseAbstractContent content)
	{
		boolean isPresent = findContentInTable(content, IMPLICITLY_WAIT);
		if (!isPresent)
		{
			throw new TestFrameworkException("The content with name " + content.getName() + " was not found!");
		} else
		{
			getLogger().info("doOpenContent::: content with name equals " + content.getDisplayName() + " was found");
		}
		String fullName = TestUtils.buildFullNameOfContent(content.getName(), content.getContentPath());;	
		SleepWaitHelper.sleep(1000);		
		//1. select a content
		selectRowByContentFullName(fullName );
		if (!openButton.isEnabled())
		{
			getLogger().info("'Open' link is disabled!");
			new WebDriverWait(getSession().getDriver(), 2).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='toolbar']/button[text()='Open']")));
		}
		//2. click by 'Open' button
		openButton.click();		
		ItemViewPanelPage cinfo = new ItemViewPanelPage(getSession());
		int expectedNumberOfPage = 1;
		cinfo.waitUntilOpened(getSession(), content.getDisplayName(), expectedNumberOfPage);
		return cinfo;
	}
	
	/**
	 * Select a content and right click on  mouse, opens a Item view panel.
	 * 
	 * @param content
	 * @return {@ItemViewPanelPage} instance.
	 */
	public ItemViewPanelPage doOpenContentFromContextMenu(BaseAbstractContent content)
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
		String fullName = TestUtils.buildFullNameOfContent(content.getName(), content.getContentPath());
		getLogger().info("Full name of content: "+ fullName);
		String contentDescriptionXpath = String.format(DIV_CONTENT_NAME_IN_TABLE, fullName);	
		WebElement element = findElement(By.xpath(contentDescriptionXpath));
		Actions action = new Actions(getDriver());
		//action.contextClick(element).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
		action.contextClick(element).click().build().perform();
		
		ItemViewPanelPage cinfo = new ItemViewPanelPage(getSession());
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
		return SleepWaitHelper.waitAndFind(By.xpath(TITLE_XPATH), getDriver());
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
