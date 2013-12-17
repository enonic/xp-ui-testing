package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.TestUtils;

public class CloseWizardDialog extends BaseModalDialog
{
	private final String YES_BUTTON_XPATH = "//div[@class='modal-dialog']//button[contains(.,'es')]";
	private final String NO_BUTTON_XPATH = "//div[@class='modal-dialog']//button[contains(.,'o')]";
	private final String CANCEL_BUTTON_XPATH = "//div[@class='modal-dialog']//button[contains(.,'Cancel')]";
	public final String TITLE_XPATH = "//h2[text()='Close wizard']";


	public CloseWizardDialog( TestSession session )
	{
		super(session);
	}

	public void doCloseNoSave()
	{
		boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(NO_BUTTON_XPATH), getDriver());
		if(!isPresent)
		{
			throw new TestFrameworkException("'No' button was not found on modal dialog!");
		}
		findElement(By.xpath(NO_BUTTON_XPATH)).click();
	}

	public void doCloseAndSave()
	{

	}
	public void waituntilPageLoaded(long timeout)
	{
		
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TITLE_XPATH)));
	}

	public boolean isDialogPresent()
	{
		return TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(TITLE_XPATH), 1l);
	}
	

}
