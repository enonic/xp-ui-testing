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
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.utils.SleepWaitHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.schemamanger.ContentType;

/**
 * 'Schema Manager' application, the dashboard page.
 * 
 */
public class SchemaBrowsePanel extends BrowsePanel
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
	public SchemaBrowsePanel(TestSession session)
	{
		super(session);
	}
	
	public void doEditContentType(ContentType contentTypeToEdit, ContentType newContentType)
	{
		String superTypeName = contentTypeToEdit.getSuperTypeNameFromConfig();
		if(superTypeName != null)
		{
		 //1. expand a supertype folder:
		 doExpandFolder(superTypeName);
		}
		//2.  select a content type in a grid
		String contentTypeXpath = String.format(CONTENTTYPE_NAME_AND_DISPLAYNAME_IN_TABLE, contentTypeToEdit.getDisplayNameFromConfig(), contentTypeToEdit.getName());
		getLogger().info("Check that a Content Type to edit is present in the table: " + contentTypeToEdit.getName());
		
		//3. click by 'Edit' button on toolbar
		WebElement elem = getDynamicElement(By.xpath(contentTypeXpath), 3);
	    if(elem==null)
	    {
	    	throw new TestFrameworkException("element was not found:"+ contentTypeXpath);
	    }
			
	    SleepWaitHelper.sleep(500);
		elem.click();
		getLogger().info("content type with name:" +contentTypeToEdit.getName() +" was selected in the table!");
		SleepWaitHelper.waitUntilElementEnabled(getSession(), By.xpath(EDIT_BUTTON_XPATH));
		editButton.click();
		ContentTypeWizardPanel wizard = new ContentTypeWizardPanel(getSession());
		getLogger().info("## ContentTypeWizardPanel  should be opened, waits title: " + contentTypeToEdit.getName());
		wizard.waitUntilWizardOpened( 1);
		wizard.doTypeDataSaveAndClose(newContentType);	
	}
	
	public ContentTypeWizardPanel doOpenContentTypeForEdit(ContentType contentTypeToEdit)
	{
		String superTypeName = contentTypeToEdit.getSuperTypeNameFromConfig();
		if(superTypeName != null)
		{
		  //1. expand a supertype folder:
		  doExpandFolder(superTypeName);
		}
		//2.  select a content type in a grid
		String contentTypeXpath = String.format(CONTENTTYPE_NAME_AND_DISPLAYNAME_IN_TABLE, contentTypeToEdit.getDisplayNameFromConfig(), contentTypeToEdit.getName());
		getLogger().info("Check that a Content Type to edit is present in the table: " + contentTypeToEdit.getName());
		
		//3. click by 'Edit' button on toolbar
		WebElement elem = getDynamicElement(By.xpath(contentTypeXpath), 3);
	    if(elem==null)
	    {
	    	throw new TestFrameworkException("element was not found:"+ contentTypeXpath);
	    }
			
	    SleepWaitHelper.sleep(500);
		elem.click();
		getLogger().info("content type with name:" +contentTypeToEdit.getName() +" was selected in the table!");
		SleepWaitHelper.waitUntilElementEnabled(getSession(), By.xpath(EDIT_BUTTON_XPATH));
		editButton.click();
		ContentTypeWizardPanel wizard = new ContentTypeWizardPanel(getSession());
		wizard.waitUntilWizardOpened( 1);
		return wizard;
	}
	
	
	/**
	 * Select a contentype or mixin or relationship and click by 'Delete' button in toolbar.
	 * 
	 * @param contentTypeToDelete
	 */
	public void doDeleteContentType(ContentType contentTypeToDelete)
	{
		String supertype = contentTypeToDelete.getSuperTypeNameFromConfig();		
		if(supertype != null)
		{
		  //1.expand a super type folder:
		  doExpandFolder(supertype);
		}
		String ctypeXpath = String.format(CONTENTTYPE_TABLE_ROW, contentTypeToDelete.getDisplayNameFromConfig(), contentTypeToDelete.getName());
        boolean isContentTypePresent = SleepWaitHelper.waitElementExist(getDriver(), ctypeXpath, 3);
		
		if(!isContentTypePresent)
		{
			throw new TestFrameworkException("content type with name "+isContentTypePresent +" was not found!");
		}
		
		SleepWaitHelper.sleep(1000);
		//2. click by a contenttype
		TestUtils.clickByElement(By.xpath(ctypeXpath), getDriver());		
		//3. wait for deleteButton(in toolbar) is enabled
		SleepWaitHelper.waitUntilElementEnabled(getSession(), By.xpath(DELETE_BUTTON_XPATH));
		//4. click by 'delete' button
		deleteButton.click();
		
		List<String> names = new ArrayList<>();
		names.add(contentTypeToDelete.getName());
		DeleteContentTypeDialog dialog = new DeleteContentTypeDialog(getSession());
		boolean result = dialog.isOpened();
		if (!result)
		{
			throw new TestFrameworkException("Confirm delete space dialog was not opened!");
		}
		//5. confirm deleting
		dialog.doDelete();
		boolean isClosed =  dialog.verifyIsClosed();
		if(!isClosed)
		{
			throw new TestFrameworkException("Confirm delete space dialog was not closed!");
		}
		getLogger().info("The Contentent Type  with name: " + contentTypeToDelete.getName() + " was deleted!");
	}

	public void doAddContentType(ContentType contentType, boolean isCloseWizard)
	{
		ContentTypeWizardPanel wizard = doOpenAddNewTypeWizard(contentType.getKind().getValue());
		if (isCloseWizard)
		{
			wizard.doTypeDataSaveAndClose(contentType);
		} else
		{
			wizard.doTypeDataAndSave(contentType);
		}
	}

	public ContentTypeWizardPanel doOpenAddNewTypeWizard(String kind)
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
		ContentTypeWizardPanel wizard = selectDialog.doSelectKind(kind);
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
		if(superTypeName != null)
		{
		  //1. expand a supertype folder:
		  doExpandFolder(superTypeName);
		}		
		String contentTypeXpath = String.format(CONTENTTYPE_NAME_AND_DISPLAYNAME_IN_TABLE, contentType.getDisplayNameFromConfig(), contentType.getName());
		getLogger().info("Check is Space present in table: " + contentTypeXpath);
	
		List<WebElement> elems = findElements(By.xpath(contentTypeXpath));

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
