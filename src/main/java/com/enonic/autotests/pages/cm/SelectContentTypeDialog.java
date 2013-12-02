package com.enonic.autotests.pages.cm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.TestUtils;

/**
 * Content Manager application/add new content/select content type
 *
 */
public class SelectContentTypeDialog
{
	private final static String DIALOG_TITLE_XPATH = "//div/h2[text()='Select Content Type']";
	
	public static  String ITEM_CONTENTTYPE = "//li[@class='content-type-list-item']//h6[text()='%s']";
	
	private String INPUT_SEARCH= "//input[@placeholder='Content Type Search']";
	//private String DIV_SEARCH_INPUT="//div[contains(@class,'x-container')] and descendant::input[@placeholder='Content Type Search']]//div[@class='admin-data-view-description']//h6[text()='%s']";
	private TestSession session;
	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public SelectContentTypeDialog(TestSession session)
	{
		this.session = session;
	}
	
	/**
	 * Checks that 'AddNewContentWizard' is opened.
	 * 
	 * @return true if dialog opened, otherwise false.
	 */
	public boolean verifyIsOpened() {
		return TestUtils.getInstance().waitUntilVisibleNoException(session, By.xpath(DIALOG_TITLE_XPATH), 1);
	}
	/**
	 * @param ctype
	 */
	public AddNewContentWizard selectContentType(ContentTypes type)
	{
		
		String ctypeXpath = String.format(ITEM_CONTENTTYPE, type.getValue());
		TestUtils.getInstance().waitUntilVisibleNoException(session, By.xpath(ctypeXpath), 3l);
		WebElement ctypeElement = session.getDriver().findElement(By.xpath(ctypeXpath));
		boolean isDisplayed = ctypeElement.isDisplayed();
		if (!isDisplayed)
		{
			WebElement searchinput = session.getDriver().findElement(By.xpath(INPUT_SEARCH));
			searchinput.sendKeys(type.getValue());	
			ctypeElement = session.getDriver().findElement(By.xpath(ctypeXpath));
		} 
		ctypeElement.click();
		AddNewContentWizard wizard = new AddNewContentWizard(session);
		//TODO currently wrong title present in the CMS
		//String title = "New " + type.getValue().toLowerCase();
		//wizard.waitUntilWizardOpened(title, 1);
		
		return wizard;

	}
	public enum ContentTypes
	{
		PAGE("Page"), MEDIA("Media"), UNSTRUCTURED("Unstructured"), STUCTURED("Structured"), MIXIN("Mixin"), CITATION("Citation"), LINK("Link"), SPACE(
				"Space"), SHORTCUT("Shortcut"), FOLDER("Folder"), ARCHIVE("Archive");

		private String value;

		/**
		 * Constructor.
		 * 
		 * @param name
		 */
		private ContentTypes( String value )
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
