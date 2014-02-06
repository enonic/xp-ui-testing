package com.enonic.autotests.pages.contentmanager.browsepanel;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.utils.SleepWaitHelper;
import com.enonic.autotests.utils.TestUtils;
import org.openqa.selenium.By;

/**
 * Content Manager application/add new content/select content type
 *
 */
public class NewContentDialog
{
	private final static String DIALOG_TITLE_XPATH = "//div[contains(@class,'modal-dialog')]/div[contains(@class,'dialog-header') and contains(.,'What do you want to create?')]";
	
	public static  String CONTENTTYPE_NAME = "//li[contains(@class,'content-type-list-item')]//p[text()='%s']";
	
	private String INPUT_SEARCH= "//div[contains(@class,'column-right')]/input";
	
	private TestSession session;
	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public NewContentDialog(TestSession session)
	{
		this.session = session;
	}
	
	/**
	 * Checks that 'AddNewContentWizard' is opened.
	 * 
	 * @return true if dialog opened, otherwise false.
	 */
	public boolean isOpened() {
		return SleepWaitHelper.waitUntilVisibleNoException(session.getDriver(), By.xpath(DIALOG_TITLE_XPATH), 1);
	}
	/**
	 * Select content type by name.
	 * 
	 * @param contentTypeName the name of a content type.
	 */
	public ContentWizardPanel selectContentType(String contentTypeName)
	{
		
		String ctypeXpath = String.format(CONTENTTYPE_NAME, contentTypeName);
		boolean isContentNamePresent = SleepWaitHelper.waitElementExist(session.getDriver(), ctypeXpath, 3);
		
		if(!isContentNamePresent)
		{
			throw new TestFrameworkException("content type with name "+contentTypeName +" was not found!");
		}
//		boolean isDisplayed  = session.getDriver().findElement(By.xpath(ctypeXpath)).isDisplayed();
//		if (!isDisplayed)
//		{
//			WebElement searchinput = session.getDriver().findElement(By.xpath(INPUT_SEARCH));
//			searchinput.sendKeys(contentTypeName);	
//		} 
		TestUtils.getInstance().clickByElement(By.xpath(ctypeXpath), session.getDriver());
		ContentWizardPanel wizard = new ContentWizardPanel(session);
		
		//String title = "New " + type.getValue().toLowerCase();
		//wizard.waitUntilWizardOpened(title, 1);
		
		return wizard;

	}
	
	/**
	 * Predefined names of content type used.
	 *
	 */
	public enum ContentTypeName
	{
		PAGE("page"),
		DATA("data"),
		TEXT("text"), 
		MEDIA("media"), 
		UNSTRUCTURED("unstructured"), 
		STRUCTURED("structured"), 
		MIXIN("mixin"), 
		CITATION("citation"), 
		LINK("link"), 
		SPACE("space"), 
		SHORTCUT("shortcut"),
		FOLDER("folder"),
		ARCHIVE("archive");

		private String value;

		/**
		 * Constructor.
		 * 
		 * @param name
		 */
		private ContentTypeName( String value )
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}

		public void setValue(String value)
		{
			this.value = value;
		}
	}
}
