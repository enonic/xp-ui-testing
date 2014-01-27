package com.enonic.autotests.pages.schemamanager;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.CreateContentTypeException;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.pages.BaseWizardPage;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.TextTransfer;
import com.enonic.autotests.vo.schemamanger.ContentType;

/**
 * 'Schema Manager' application, Add new Content Type Wizard page.
 * 
 */
public class AddNewContentTypeWizard extends BaseWizardPage
{

	private String NOTIF_MESSAGE = "\"%s\" was saved";

	@FindBy(xpath = "//div[@class='CodeMirror']//textarea")
	protected WebElement configXMLTextArea;

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public AddNewContentTypeWizard( TestSession session )
	{
		super(session);

	}

	/**
	 * Tapes a name and calculate a width of input field.
	 * @param longName
	 * @return
	 */
	public int doTypeLongNameAndGetInputWidth(String longName)
	{
		TestUtils.getInstance().clearAndType(getSession(), nameInput, longName);
		String width = findElement(By.xpath("//input[@name='name']")).getAttribute("style");

		String aa = "width: 300px";
		int start = aa.indexOf(":");
		int end = aa.indexOf("px");
		int value = Integer.valueOf(width.substring(start+1, end).trim());
		
		return value;
	}

	/**
	 * Types a data and close wizard.
	 * 
	 * @param session
	 * @param content
	 */
	public void doTypeDataSaveAndClose(ContentType contentType)
	{
		doTypeDataAndSave(contentType);
		closeButton.click();
		if(checkoutErrorDialog())
		{
			throw new CreateContentTypeException("New Content type was not created, Erorr dialog with error message appeared ");
		}
		SchemaGridPage page = new SchemaGridPage(getSession());
		page.waituntilPageLoaded(TestUtils.TIMEOUT_IMPLICIT);

	}
	
	private boolean checkoutErrorDialog()
	{
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath("//div[contains(@class,'x-window-closable')]//span[contains(.,'Error')]"));
		if(elems.size() == 0)
		{
			return false;
		}else return true;
	}

	private void clearConfig(WebElement configElement)
	{
		final Actions builder = new Actions( getSession().getDriver() );
		builder.click( configElement ).sendKeys(Keys.chord(Keys.CONTROL, "a"), " " ).build().perform();
		
		getLogger().info("method fifnished :clearConfig ");
		
	}
	/**
	 * Types data and press the "Save" button from the toolbar.
	 * 
	 * @param session
	 * @param content
	 */
	public void doTypeDataAndSave(ContentType contentType)
	{
		// 1. type a data: 'name' and 'Display Name'.
		TestUtils.getInstance().clearAndType(getSession(), nameInput, contentType.getName());
		//2. type the XMLconfig data:
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath("//div[contains(@class,'CodeMirror')]//div[contains(@class,'CodeMirror-lines')]"));
		if(getSession().getIsRemote()!=null && !getSession().getIsRemote())
		{
			clearConfig(elems.get(0));
			getLogger().info("set configuration from a Clipboard:");
			setConfigFromClipboard(contentType,  elems.get(0));
			
		}else{
			
			final Actions builder = new Actions( getSession().getDriver() );
			builder.click( elems.get(0) ).sendKeys( contentType.getConfigData() );
			final Action paste = builder.build();
			paste.perform();
		}
		
		TestUtils.getInstance().saveScreenshot(getSession());
		// 3. check if enabled and press "Save".
		doSaveFromToolbar();
		
		// 4. TODO check ERROR DIALOG

		// 5. check notification message.
		String mess = getNotificationMessage(getSession(), AppConstants.APP_SCHEMA_MANAGER_FRAME_XPATH);
		if (mess == null)
		{
			throw new SaveOrUpdateException("A notification, that the content type with name" + contentType.getName() + " is saved - was not showed");
		}
		String expectedNotificationMessage = String.format(NOTIF_MESSAGE, contentType.getName());
		if (!mess.contains(expectedNotificationMessage))
		{
		//	getLogger().error(
		//			"the actual notification and expected are not equals!  actual message:" + mess + " but expected:" + expectedNotificationMessage,
		//			getSession());
		//	throw new SaveOrUpdateException("the actual notification, that the content type with name" + contentType.getName()
		//			+ " was saved - and expected are not equals!");
		}

	}
	/**
	 * @param content
	 */
	private void setClipboardContents(String content)
	{
		TextTransfer textTransfer = new TextTransfer();
		textTransfer.setClipboardContents(content);
	}
	
	private void setConfigFromClipboard(ContentType ctype, WebElement configElement)
	{
		setClipboardContents(ctype.getConfigData().trim());
		final Actions act = new Actions( getSession().getDriver());
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("mac") >= 0)
		{
			act.click(configElement).keyDown(Keys.COMMAND).sendKeys("v").keyUp(Keys.CONTROL).build().perform();
		} else
		{
			act.click(configElement).keyDown(Keys.CONTROL).sendKeys("v").keyUp(Keys.CONTROL).build().perform();
			getLogger().info("copy paste from clipboard, os:windows");

		}
	}
}
