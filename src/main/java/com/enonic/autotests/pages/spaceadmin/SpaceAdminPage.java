package com.enonic.autotests.pages.spaceadmin;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.Space;

/**
 * 'Space Admin' application, main page.
 * 
 */
public class SpaceAdminPage extends Page
{
	public static final String SPACES_TABLE_CELLS_XPATH = "//table[contains(@class,'x-grid-table')]//td[contains(@class,'x-grid-cell')]";
	private String SPACE_NAME_AND_DISPLAYNAME_IN_TABLE = "//table[contains(@class,'x-grid-table')]//div[@class='admin-grid-description'  and descendant::h6[contains(.,'%s')] and descendant::p[contains(.,'%s')]]";

	private String CHECKBOX_ROW_CHECKER = SPACE_NAME_AND_DISPLAYNAME_IN_TABLE + "//ancestor::td/preceding-sibling::td";

	@FindBy(xpath = "//span[@class = 'x-btn-inner' and text()='Space Admin']")
	private WebElement titleElement;

	private final String NEW_BUTTON_XPATH = "//div[@class='toolbar']/button[text()='New']";
	@FindBy(xpath = NEW_BUTTON_XPATH)
	private WebElement newButton;

	@FindBy(xpath = "//span[@class='x-btn-inner' and contains(., 'Edit')]")
	private WebElement editButton;

	private final String DELETE_BUTTON_XPATH = "//div[@class='toolbar']/button[text()='Delete']";
	@FindBy(xpath = DELETE_BUTTON_XPATH)
	private WebElement deleteButton;

	@FindBy(xpath = "//td[@class='x-trigger-cell']")
	private WebElement comboboxShowItems;


	private static String DIV_SCROLL_XPATH = "//table[contains(@class,'x-grid-table-resizer')]/parent::div[contains(@id,'gridview')]";

	/**
	 * The constructor.
	 * 
	 * @param session
	 *            {@link TestSession} instance.
	 */
	public SpaceAdminPage( TestSession session )
	{
		super(session);

	}

	/**
	 * waits until iframe will be loaded.
	 * 
	 * @param timeout
	 */
	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(AppConstants.APP_SPACE_ADMIN_FRAME_XPATH)));

	}

	public void waituntilToolbarLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(NEW_BUTTON_XPATH)));

	}

	//TODO waiting for implementation in wem-webapp
	private void selectTemplate(String template)
	{
		// comboboxShowItems
	}

	/**
	 * Creates new space, if Alert dialog will appears, so exception will be thrown.
	 * 
	 * @param space
	 * @param isCloseWizard
	 */
	public void addNewSpace(Space space, boolean isCloseWizard)
	{
		AddNewSpaceWizard wizardPage = openNewSpaceWizard();
		//waits until red circle with number '1' appears.
		wizardPage.waitUntilWizardOpened(AddNewSpaceWizard.START_TITLE, 1);
		getLogger().info("AddNewSpaceWizard  was successfully opened, new space name is : " + space.getName());
		if (isCloseWizard)
		{
			wizardPage.doTypeDataSaveAndClose(getSession(), space);
		} else
		{
			wizardPage.doTypeDataAndSave(getSession(), space);
		}

		boolean isAlertDialogPresent = checkAlerts(By.xpath(AppConstants.DIALOG_CLOSE_BUTTON_XPATH));
		if (isAlertDialogPresent)
		{
			throw new SaveOrUpdateException("Modal Dialog is present after closing the AddNewSpaceWizard, but table with spaces expected");
		}
		getLogger().info("new Space  with name: " + space.getName() + " was created!");
	}

	/**
	 * Clicks "New" button on the 'Space Admin' page.
	 * 
	 * @return {@link AddNewSpaceWizard} instance.
	 */
	public AddNewSpaceWizard openNewSpaceWizard()
	{
		TestUtils.getInstance().waitUntilElementEnabledNoException(getSession(), By.xpath(NEW_BUTTON_XPATH), 3l);
		newButton.click();
		return new AddNewSpaceWizard(getSession());
	}

	/**
	 * Clicks "Edit" button on the 'Space Admin' page.
	 * 
	 * @return {@link AddNewSpaceWizard} instance.
	 */
	public AddNewSpaceWizard openToUpdateSpaceWizard(Space space)
	{
		// 1. select the Space in the table.
		String spaceXpath = String.format(CHECKBOX_ROW_CHECKER, space.getDisplayName(), space.getName());
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(spaceXpath));
		if (elems.size() == 0)
		{
			throw new SaveOrUpdateException("Space with name:" + space.getName() + "was not found!");
		}

		if (!elems.get(0).isDisplayed())
		{ // execute scroll script: $0.scrollHeight =630;
		// need to increase the scrollTop :::: $0(div).scrollTop += 10
			WebElement element = TestUtils.getInstance().scrollTableAndFind(getSession(),spaceXpath, DIV_SCROLL_XPATH);
			element.click();
		} else
		{

			elems.get(0).click();
		}

		// 2. click the 'Edit' button.
		editButton.click();
		AddNewSpaceWizard wizard = new AddNewSpaceWizard(getSession());
		wizard.waitUntilWizardOpened(space.getDisplayName(), 1);
		return wizard;
	}

	public void doUpdateSpace(Space spaceToEdit, Space space, boolean isCloseWizard)
	{
		AddNewSpaceWizard wizardPage = openToUpdateSpaceWizard(spaceToEdit);
		wizardPage.waitUntilWizardOpened(spaceToEdit.getDisplayName(), 1);
		getLogger().info("SpaceWizard  was successfully opened, space with name : " + spaceToEdit.getDisplayName() + " can be editable");
		if (isCloseWizard)
		{
			wizardPage.doTypeDataSaveAndClose(getSession(), space);
		} else
		{
			wizardPage.doTypeDataAndSave(getSession(), space);
		}

		getLogger().info("new Space  with name: " + space.getName() + " was updated!");
	}

	public void doDeleteSpace(Space spaceToDelete)
	{
		String spaceXpath = String.format(CHECKBOX_ROW_CHECKER, spaceToDelete.getDisplayName(), spaceToDelete.getName());
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(spaceXpath));
		if(elems.size() == 0)
		{
			throw new TestFrameworkException("Space to delete was not present in the table or wron xpath! name:" +spaceToDelete.getDisplayName());
		}
		//elems.get(0).click();
		if (!elems.get(0).isDisplayed())
		{ 
			WebElement scrolled = TestUtils.getInstance().scrollTableAndFind(getSession(),spaceXpath,DIV_SCROLL_XPATH);
			scrolled.click();
		} else
		{

			elems.get(0).click();
		}
		
		deleteButton.click();
		List<String> names = new ArrayList<>();
		names.add(spaceToDelete.getDisplayName());
		DeleteSpaceDialogPage dialog = new DeleteSpaceDialogPage(getSession(), names);
		boolean result = dialog.isOpened();
		if (!result)
		{
			throw new TestFrameworkException("Confirm delete space dialog was not opened!");
		}
		dialog.doDelete();
		dialog.verifyIsClosed();
		getLogger().info("The Space  with name: " + spaceToDelete.getName() + " was deleted!");
	}

	public boolean checkIsSpacePresentInTable(Space space)
	{
		String spaceDescriptionXpath = String.format(SPACE_NAME_AND_DISPLAYNAME_IN_TABLE, space.getDisplayName(), space.getName());
		getLogger().info("Check is Space present in table: " + spaceDescriptionXpath);
	
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(spaceDescriptionXpath));

		if (elems.size() > 0)
		{
			getLogger().info("new Space  was found in the Table! name:" + space.getName() + " displayName:" + space.getDisplayName());
			return true;
		} else
		{
			getLogger().info("new Space  was not found in the Table! name: " + space.getName() + " displayName:" + space.getDisplayName());
			return false;
		}

	}

	/**
	 * Checks errors messages and alert, when 'Save' button was pressed.
	 * 
	 * @param session
	 *            {@link TestSession} instance.
	 * @param by
	 */
	public boolean checkAlerts(By by)
	{
		return TestUtils.getInstance().waitUntilVisibleNoException(getSession(), by, 2l);

	}

}
