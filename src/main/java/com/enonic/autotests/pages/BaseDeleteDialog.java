package com.enonic.autotests.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.utils.TestUtils;

/**
 * Base confirm dialog for deleting spaces, contents, accounts
 *
 */
public abstract class BaseDeleteDialog
{
	protected Logger logger = Logger.getLogger();
	
	private final String ITEMS_TO_DELETE = "//div[contains(@class,'modal-dialog delete-dialog')]//div[@class='item-list']//h4";
	
	public static final String DELETE_BUTTON_XPATH = "//div[@class='modal-dialog delete-dialog']//div[@class='button-row']//button[text()='Delete']";
	@FindBy(xpath = DELETE_BUTTON_XPATH)
	private WebElement deleteButton;

	private TestSession session;
	//private List<String> displayNamesToDelete = new ArrayList<>();

	/**
	 * The constructor
	 * 
	 * @param session
	 * @param spacesToDelete
	 */
	public BaseDeleteDialog( TestSession session )
	{
		this.session = session;
		//this.displayNamesToDelete = displayNamesToDelete;
		PageFactory.initElements(session.getDriver(), this);
	}

	/**
	 * Checks items to delete and click "Delete" button. throws
	 * DeleteSpaceException, if list of names for deleting does not contains
	 * expected names.
	 */
	public void doDelete()
	{
		// TODO this part is postponed
		
//		List<WebElement> itemsTodelete = session.getDriver().findElements(By.xpath(ITEMS_TO_DELETE));
//		List<String> actual = new ArrayList<>();
//		for (WebElement el : itemsTodelete)
//		{
//			actual.add(el.getText());
//			logger.info("this item present in the confirm-delete dialog and will be deleted:" + el.getText());
//		}
//		boolean result = actual.equals(displayNamesToDelete);
//		if (!result)
//		{
//			logger.error("list of names in the dialog-window are not as expected!", session);
//			throw new DeleteCMSObjectException("list of names in the dialog-window are not equals with expected list of names!");
//		}
		deleteButton.click();
	}
	
	public List<String> getContentNameToDelete()
	{
		List<String> names = new ArrayList<>();
		List<WebElement> itemsTodelete = session.getDriver().findElements(By.xpath(ITEMS_TO_DELETE));
		
		for (WebElement el : itemsTodelete)
		{
			names.add(el.getText());
			logger.info("this item present in the confirm-delete dialog and will be deleted:" + el.getText());
		}
		return names;
	}

	/**
	 * Checks that 'DeleteSpaceDialog' is opened.
	 * 
	 * @return true if dialog opened, otherwise false.
	 */
	public boolean isOpened()
	{
		return TestUtils.getInstance().waitUntilVisibleNoException(session, By.xpath(getTitleXpath()), AppConstants.IMPLICITLY_WAIT);
	}

	public boolean verifyIsClosed()
	{
		return TestUtils.getInstance().waitsElementNotVisible(session, By.xpath(getTitleXpath()), AppConstants.IMPLICITLY_WAIT);
	}
	
	public abstract String getTitleXpath();
	

	
}
