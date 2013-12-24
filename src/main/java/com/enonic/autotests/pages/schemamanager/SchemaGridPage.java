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
import com.enonic.autotests.pages.AbstractGridPage;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.schemamanger.ContentType;

/**
 * 'Schema Manager' application, the dashboard page.
 * 
 */
public class SchemaGridPage extends AbstractGridPage
{
	private static String titleXpath = "//button[contains(@class,'home-button') and contains(.,'Schema Manager')]"; 

	private final String NEW_BUTTON_XPATH = "//div[@class='toolbar']/button[text()='New']";
	@FindBy(xpath = NEW_BUTTON_XPATH)
	private WebElement newButtonMenu;

	public static final String SCHEMAS_TABLE_CELLS_XPATH = "//table[contains(@class,'x-grid-table')]//td[contains(@class,'x-grid-cell')]";

	private final String EDIT_BUTTON_XPATH = "//div[@class='toolbar']/button[text()='Edit']";
	@FindBy(xpath = EDIT_BUTTON_XPATH)
	private WebElement editButton;

	private final String DELETE_BUTTON_XPATH ="//div[@class='toolbar']/button[text()='Delete']";
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
	
	private String CONTENTTYPE_TABLE_ROW = "//tr[contains(@class,'x-grid-row') and descendant::h6[contains(.,'%s')] and descendant::p[text()='%s']]";
    
	//private static String DIV_SCROLL_XPATH = "//table[contains(@class,'x-grid-table-resizer')]/parent::div[contains(@id,'treeview')]";

	/**
	 * 
	 * The constructor
	 * 
	 * @param session
	 */
	public SchemaGridPage( TestSession session )
	{
		super(session);

	}
	
	public void doEditContentType(ContentType contentTypeToEdit, ContentType newContentType)
	{
		String superTypeName = contentTypeToEdit.getSuperTypeNameFromConfig();
		//1. expand a supertype folder:
		doExpandFolder(superTypeName);
		//2.  select a content type in a grid
		String contentTypeXpath = String.format(CONTENTTYPE_NAME_AND_DISPLAYNAME_IN_TABLE, contentTypeToEdit.getDisplayNameFromConfig(), contentTypeToEdit.getName());
		getLogger().info("Check is Space present in table: " + contentTypeXpath);
		
		//3. click by 'Edit' button on toolbar
		WebElement elem = TestUtils.getInstance().getDynamicElement(getDriver(), By.xpath(contentTypeXpath), 3);
	    if(elem==null)
	    {
	    	throw new TestFrameworkException("element was not found:"+ contentTypeXpath);
	    }
			
		try
		{
			Thread.sleep(500);
		} catch (InterruptedException e)
		{
			
			e.printStackTrace();
		}
		elem.click();
		getLogger().info("content type with name:" +contentTypeToEdit.getName() +" was selected in the table!");
		TestUtils.getInstance().waitUntilElementEnabled(getSession(), By.xpath(EDIT_BUTTON_XPATH));
		editButton.click();
		AddNewContentTypeWizard wizard = new AddNewContentTypeWizard(getSession());
		getLogger().info("## AddNewContentTypeWizard  should be opened, waits title: " + contentTypeToEdit.getName());
		wizard.waitUntilWizardOpened(contentTypeToEdit.getName(), 1);
		wizard.doTypeDataSaveAndClose(newContentType);
		
	}
	
	public void doDeleteContentType(ContentType contentTypeToDelete)
	{
		//1.expand a super type folder:
		String supertype = contentTypeToDelete.getSuperTypeNameFromConfig();
		doExpandFolder(supertype);
		String ctypeXpath = String.format(CONTENTTYPE_TABLE_ROW, contentTypeToDelete.getDisplayNameFromConfig(), contentTypeToDelete.getName());
        boolean isContentTypePresent = TestUtils.getInstance().waitElementExist(getDriver(), ctypeXpath, 3);
		
		if(!isContentTypePresent)
		{
			throw new TestFrameworkException("content type with name "+isContentTypePresent +" was not found!");
		}
		
		
		TestUtils.getInstance().clickByElement(By.xpath(ctypeXpath), getDriver());		
		deleteButton.click();
		List<String> names = new ArrayList<>();
		names.add(contentTypeToDelete.getName());
		DeleteContentTypeDialog dialog = new DeleteContentTypeDialog(getSession());
		boolean result = dialog.isOpened();
		if (!result)
		{
			throw new TestFrameworkException("Confirm delete space dialog was not opened!");
		}
		dialog.doDelete();
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
	
	public boolean isContentTypePresentInTable(ContentType contentType)
	{
		String superTypeName = contentType.getSuperTypeNameFromConfig();
		//1. expand a supertype folder:
		doExpandFolder(superTypeName);
		
		String contentTypeXpath = String.format(CONTENTTYPE_NAME_AND_DISPLAYNAME_IN_TABLE, contentType.getDisplayNameFromConfig(), contentType.getName());
		getLogger().info("Check is Space present in table: " + contentTypeXpath);
	
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(contentTypeXpath));

		if (elems.size() > 0)
		{
			getLogger().info("Content type  was found in the Table! " +   "Name:" +contentType.getName());
			return true;
		} else
		{
			getLogger().info("Content type  was not found in the Table!  " +   "Name:" + contentType.getName());
			return false;
		}

	}

}
