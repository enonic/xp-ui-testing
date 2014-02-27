package com.enonic.autotests.pages.schemamanager;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.CreateContentTypeException;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.TextTransfer;
import com.enonic.autotests.vo.schemamanger.ContentType;

/**
 * 'Schema Manager' application, Add new Content Type Wizard page.
 */
public class ContentTypeWizardPanel
    extends WizardPanel
{
    @FindBy(xpath = "//div[@class='CodeMirror']//textarea")
    protected WebElement configXMLTextArea;

    /**
     * The constructor.
     *
     * @param session
     */
    public ContentTypeWizardPanel( TestSession session )
    {
        super( session );
    }

    /**
     * calculates a width of input field.
     *
     * @return
     */
    public int getInputNameWidth()
    {
        String width = nameInput.getAttribute( "style" );
        //input Style should be like as: "width: 300px";
        int start = width.indexOf( ":" );
        int end = width.indexOf( "px" );
        int inputWidth = Integer.valueOf( width.substring( start + 1, end ).trim() );
        return inputWidth;
    }

    /**
     * Types a name to the name-input field
     *
     * @param name
     */
    public void doTypeName( String name )
    {
        clearAndType( nameInput, name );
    }

    /**
     * Types a data and close wizard.
     *
     * @param contentType
     */
    public void doTypeDataSaveAndClose( ContentType contentType )
    {
        doTypeDataAndSave( contentType );
        closeButton.click();
        if ( checkoutErrorDialog() )
        {
            throw new CreateContentTypeException( "New Content type was not created, Erorr dialog with error message appeared " );
        }
        SchemaBrowsePanel page = new SchemaBrowsePanel( getSession() );
        page.waituntilPageLoaded( Application.PAGELOAD_TIMEOUT );

    }

    private boolean checkoutErrorDialog()
    {
        List<WebElement> elems =
            getSession().getDriver().findElements( By.xpath( "//div[contains(@class,'x-window-closable')]//span[contains(.,'Error')]" ) );
        if ( elems.size() == 0 )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private void clearConfig( WebElement configElement )
    {
        final Actions builder = new Actions( getSession().getDriver() );
        builder.click( configElement ).sendKeys( Keys.chord( Keys.CONTROL, "a" ), " " ).build().perform();
        getLogger().info( "method fifnished :clearConfig " );

    }

    /**
     * Types data and press the "Save" button from the toolbar.
     *
     * @param contentType
     */
    public void doTypeDataAndSave( ContentType contentType )
    {
        doTypeData( contentType );

        TestUtils.saveScreenshot( getSession() );
        // 3. check if enabled and press "Save".
        doSaveFromToolbar();
        boolean isSaveEnabled = isEnabledSaveButton();
        if ( !isSaveEnabled )
        {
            throw new SaveOrUpdateException(
                "the content with name" + contentType.getName() + " was not correctly saved, button 'Save' still disabled!" );
        }
    }

    public void doTypeData( ContentType contentType )
    {
        // 1. type a data: 'name' and 'Display Name'.
        clearAndType( nameInput, contentType.getName() );
        //2. type the XMLconfig data:
        List<WebElement> elems = getSession().getDriver().findElements(
            By.xpath( "//div[contains(@class,'CodeMirror')]//div[contains(@class,'CodeMirror-lines')]" ) );
        if ( getSession().getIsRemote() != null && !getSession().getIsRemote() )
        {
            clearConfig( elems.get( 0 ) );
            getLogger().info( "set configuration from a Clipboard:" );
           // setConfigFromClipboard( contentType, elems.get( 0 ) );
            setConfiguration( contentType.getConfigData().trim());
           

        }
        else
        {

            final Actions builder = new Actions( getSession().getDriver() );
            builder.click( elems.get( 0 ) ).sendKeys( contentType.getConfigData() );
            final Action paste = builder.build();
            paste.perform();
        }
    }

    /**
     * @param content
     */
    private void setClipboardContents( String content )
    {
        TextTransfer textTransfer = new TextTransfer();
        textTransfer.setClipboardContents( content );
    }

	private void setConfiguration(String cfg)
	{
		((JavascriptExecutor) getSession().getDriver()).executeScript("window.api.dom.ElementRegistry.getElementById('api.ui.CodeArea').setValue(arguments[0])", cfg);
	}
    private void setConfigFromClipboard( ContentType ctype, WebElement configElement )
    {
        setClipboardContents( ctype.getConfigData().trim() );
        final Actions act = new Actions( getDriver() );
        String os = System.getProperty( "os.name" ).toLowerCase();
        if ( os.indexOf( "mac" ) >= 0 )
        {
           // act.click( configElement ).keyDown( Keys.COMMAND ).sendKeys( "v" ).keyUp( Keys.COMMAND ).build().perform();
            act.click( configElement ).sendKeys( Keys.chord( Keys.COMMAND, "v" )).build().perform();
        }
        else
        {
            //act.click( configElement ).keyDown( Keys.CONTROL ).sendKeys( "v" ).keyUp( Keys.CONTROL ).build().perform();
        	 act.click( configElement ).sendKeys( Keys.chord( Keys.CONTROL, "v" )).build().perform();
            getLogger().info( "copy paste from clipboard, os:windows" );
        }
    }
}
