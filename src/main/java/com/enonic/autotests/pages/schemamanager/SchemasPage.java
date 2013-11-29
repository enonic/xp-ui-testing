package com.enonic.autotests.pages.schemamanager;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.schemamanger.ContentType;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.utils.TestUtils;

/**
 * 'Schema Manager' application, the dashboard page.
 * 
 */
public class SchemasPage extends Page
{
	private static String titleXpath = "//span[contains(@class,'x-btn-inner') and contains(.,'Schema Manager')]";

	//private final String NEW_BUTTON_XPATH = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'New')]]";
	private final String NEW_BUTTON_XPATH = "//div[@class='toolbar']/button[text()='New']";
	@FindBy(xpath = NEW_BUTTON_XPATH)
	private WebElement newButtonMenu;

	public static final String SCHEMAS_TABLE_CELLS_XPATH = "//table[contains(@class,'x-grid-table')]//td[contains(@class,'x-grid-cell')]";

	@FindBy(xpath = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Edit')]]")
	private WebElement editButton;

	private final String DELETE_BUTTON_XPATH = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Delete')]]";
	@FindBy(xpath = DELETE_BUTTON_XPATH)
	private WebElement deleteButton;

	private final String REINDEX_BUTTON_XPATH = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Re-index')]]";
	@FindBy(xpath = REINDEX_BUTTON_XPATH)
	private WebElement reindexButton;

	private final String EXPORT_BUTTON_XPATH = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Export')]]";
	@FindBy(xpath = EXPORT_BUTTON_XPATH)
	private WebElement exportButton;

	private final String SELECT_ALL_LINK_XPATH = "//a[contains(@class,'x-toolbar-item') and (contains(.,'Select All') or contains(.,'Select all'))]";
	@FindBy(xpath = SELECT_ALL_LINK_XPATH)
	private WebElement selectAllLink;

	private final String CLEAR_SELECTION_LINK_XPATH = "//a[contains(@class,'admin-grid-toolbar-btn-clear-selection')]";
	@FindBy(xpath = CLEAR_SELECTION_LINK_XPATH)
	private WebElement clearSelectionLink;
	
	private String CONTENTTYPE_NAME_AND_DISPLAYNAME_IN_TABLE = "//table[contains(@class,'x-grid-table')]//div[@class='admin-tree-description' and descendant::h6[contains(.,'%s')] and descendant::p[contains(.,'%s')]]";
	
	private String CONTENTTYPE_TABLE_ROW = "//tr[contains(@class,'x-grid-row') and descendant::h6[contains(.,'%s')] and descendant::p[text()='system:%s']]";
    
	private static String DIV_SCROLL_XPATH = "//table[contains(@class,'x-grid-table-resizer')]/parent::div[contains(@id,'treeview')]";

	/**
	 * 
	 * The constructor
	 * 
	 * @param session
	 */
	public SchemasPage( TestSession session )
	{
		super(session);

	}
	
	public void doDeleteContentType(ContentType contentTypeToDelete)
	{
		
		String spaceXpath = String.format(CONTENTTYPE_TABLE_ROW, contentTypeToDelete.getDisplayName(), contentTypeToDelete.getName());
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(spaceXpath));
		if(elems.size() == 0)
		{
			throw new TestFrameworkException("Space to delete was not present in the table or wron xpath! name:" +contentTypeToDelete.getDisplayName());
		}

		if (!elems.get(0).isDisplayed())
		{ 
			WebElement scrolled = TestUtils.getInstance().scrollTableAndFind(getSession(),spaceXpath, DIV_SCROLL_XPATH);
			scrolled.click();
		} else
		{

			elems.get(0).click();
		}
		
		deleteButton.click();
		List<String> names = new ArrayList<>();
		names.add(contentTypeToDelete.getDisplayName());
		DeleteContentTypeDialog dialog = new DeleteContentTypeDialog(getSession(), names);
		boolean result = dialog.verifyIsOpened();
		if (!result)
		{
			throw new TestFrameworkException("Confirm delete space dialog was not opened!");
		}
		dialog.doDelete();
		dialog.verifyIsClosed();
		getLogger().info("The Space  with name: " + contentTypeToDelete.getName() + " was deleted!");
	}

	public void doAddContentType(ContentType contentType, boolean isCloseWizard)
	{
		AddNewContentTypeWizard wizard = openAddNewTypeWizard(contentType.getKind().getValue());
		if (isCloseWizard)
		{
			wizard.doTypeDataSaveAndClose(contentType);
		} else
		{
			wizard.doTypeDataAndSave(contentType);
		}
	}

	private AddNewContentTypeWizard openAddNewTypeWizard(String kind)
	{
		newButtonMenu.click();
		SelectKindDialog selectDialog = new SelectKindDialog(getSession());
		boolean isOpened = selectDialog.verifyIsOpened();
		if (!isOpened)
		{
			getLogger().error("SelectKindDialog was not opened!", getSession());
			throw new TestFrameworkException(String.format("Error during add new content type  %s, dialog was not opened!",kind));
		}
		getLogger().info("SelectKindDialog, content type should be selected:" + kind);
		AddNewContentTypeWizard wizard = selectDialog.doSelectKind(kind);
		return wizard;
		
	}

	/**
	 * Waits until page loaded.
	 * 
	 * @param timeout
	 */
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SCHEMAS_TABLE_CELLS_XPATH)));
	}

	/**
	 * 
	 * @param session
	 * @return true if 'Content Manager' opened and CMSpacesPage showed,
	 *         otherwise false.
	 */
	public static boolean isOpened(TestSession session)
	{
		List<WebElement> elems = session.getDriver().findElements(By.xpath(titleXpath));
		if (elems.size() == 0)
		{
			return false;
		} else
		{
			return true;
		}
	}
	
	public boolean verifyIsContentTypePresentInTable(String displayName,String name)
	{
		String spaceDescriptionXpath = String.format(CONTENTTYPE_NAME_AND_DISPLAYNAME_IN_TABLE, displayName, name);
		getLogger().info("Check is Space present in table: " + spaceDescriptionXpath);
	
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(spaceDescriptionXpath));

		if (elems.size() > 0)
		{
			getLogger().info("new Space  was found in the Table! name:" +   " displayName:" +displayName);
			return true;
		} else
		{
			getLogger().info("new Space  was not found in the Table! name: " +   " displayName:" + displayName);
			return false;
		}

	}

}
