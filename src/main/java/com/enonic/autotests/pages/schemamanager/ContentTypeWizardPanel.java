package com.enonic.autotests.pages.schemamanager;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.CreateContentTypeException;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.schemamanger.ContentType;

/**
 * 'Schema Manager' application, Add new Content Type Wizard page.
 */
public class ContentTypeWizardPanel
    extends WizardPanel
{

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

    /**
     * Types data and press the "Save" button from the toolbar.
     *
     * @param contentType
     */
    public void doTypeDataAndSave( ContentType contentType )
    {
        typeData( contentType );

        TestUtils.saveScreenshot( getSession() );
        // 3. check if enabled and press "Save".
        save();
        boolean isSaveEnabled = isEnabledSaveButton();
        if ( !isSaveEnabled )
        {
            throw new SaveOrUpdateException(
                "the content with name" + contentType.getName() + " was not correctly saved, button 'Save' still disabled!" );
        }
    }

    public ContentTypeWizardPanel typeData( ContentType contentType )
    {
        // 1. type a data: 'name' and 'Display Name'.
        clearAndType( nameInput, contentType.getName() );
        //2. type the XMLconfig data:
        getLogger().info( "set contenttype configuration " );
        setConfiguration( contentType.getConfigData().trim() );
        return this;
    }


    private void setConfiguration( String cfg )
    {
        ( (JavascriptExecutor) getSession().getDriver() ).executeScript(
            "window.api.dom.ElementRegistry.getElementById('api.ui.CodeArea').setValue(arguments[0])", cfg );
    }

}
