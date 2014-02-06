package com.enonic.autotests.pages.schemamanager;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.SleepWaitHelper;

/**
 * 
 *
 */
public class SelectKindDialog 
{

	private String KIND = "//div[@class='modal-dialog new-schema-dialog']//h6[text()='%s']";
	private TestSession session;
	
	/**
	 * The Constructor.
	 * 
	 * @param session
	 */
	public SelectKindDialog( TestSession session )
	{
		this.session = session;
		PageFactory.initElements(session.getDriver(), this);
	}

	/**
	 * @param kind
	 * @return
	 */
	public ContentTypeWizardPanel doSelectKind(String kind)
	{
		String kindXpath = String.format(KIND, kind);
		boolean isPpresent = SleepWaitHelper.waitAndFind(By.xpath(kindXpath), session.getDriver());
		
		if (!isPpresent)
		{
			throw new TestFrameworkException("The kind of contentype" + kind + " was not found!!!");
		}
		session.getDriver().findElement(By.xpath(kindXpath)).click();
		ContentTypeWizardPanel wizard = new ContentTypeWizardPanel(session);

		wizard.waitUntilWizardOpened( 1);
		return wizard;
	}

	/**
	 * @return
	 */
	public boolean verifyIsOpened()
	{
		String title ="//div[@class='modal-dialog new-schema-dialog']/div[contains(.,'Select Kind')]";
		return SleepWaitHelper.waitUntilVisibleNoException(session.getDriver(), By.xpath(title), 2);
	}

}
