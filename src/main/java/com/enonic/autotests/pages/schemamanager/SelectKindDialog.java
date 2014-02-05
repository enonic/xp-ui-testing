package com.enonic.autotests.pages.schemamanager;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.utils.TestUtils;
import org.openqa.selenium.By;

/**
 * 
 *
 */
public class SelectKindDialog extends Page
{

	private String KIND = "//div[@class='modal-dialog new-schema-dialog']//h6[text()='%s']";
	
	/**
	 * The Constructor.
	 * 
	 * @param session
	 */
	public SelectKindDialog( TestSession session )
	{
		super(session);
	}

	/**
	 * @param kind
	 * @return
	 */
	public ContentTypeWizardPanel doSelectKind(String kind)
	{
		String kindXpath = String.format(KIND, kind);
		boolean isPpresent = TestUtils.getInstance().waitAndFind(By.xpath(kindXpath), getDriver());
		
		if (!isPpresent)
		{
			throw new TestFrameworkException("The kind of contentype" + kind + " was not found!!!");
		}
		findElement(By.xpath(kindXpath)).click();
		ContentTypeWizardPanel wizard = new ContentTypeWizardPanel(getSession());
		String displayName = null;
		if (kind.equals(KindOfContentTypes.RELATIONSHIP_TYPE.getValue()))
		{
			displayName = "New Relationship Type";
			
		} else if (kind.equals(KindOfContentTypes.MIXIN.getValue()))
		{
			displayName = "New Mixin";
			
		}else if(kind.equals(KindOfContentTypes.CONTENT_TYPE.getValue()))
		{
			displayName = "New Content Type";
		}
		wizard.waitUntilWizardOpened( 1);
		return wizard;
	}

	/**
	 * @return
	 */
	public boolean verifyIsOpened()
	{
		String title ="//div[@class='modal-dialog new-schema-dialog']/div[contains(.,'Select Kind')]";
		return TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(title), 2);
	}

}
