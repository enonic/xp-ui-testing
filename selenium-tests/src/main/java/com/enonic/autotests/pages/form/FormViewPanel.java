package com.enonic.autotests.pages.form;


import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public abstract class FormViewPanel
    extends Application
{
    protected static final String FORM_VIEW = "//div[contains(@id,'api.form.FormView')]";

    protected final String VALIDATION_VIEWER = "//div[contains(@id,'ValidationRecordingViewer')]";

    protected final String CONTENT_SELECTED_OPTION_VIEW = "//div[contains(@id,'ContentSelectedOptionView')]";

    protected final String VALIDATION_MESSAGE = VALIDATION_VIEWER + "//li";

    public static String VALIDATION_MESSAGE_OCCURRENCE = "This field is required";

    protected final String ADD_BUTTON_XPATH = FORM_VIEW + "//div[@class='bottom-button-row']//button[child::span[text()='Add']]";

    public FormViewPanel( final TestSession session )
    {
        super( session );
    }

    public abstract FormViewPanel type( final PropertyTree data );

    public boolean isAddButtonPresent()
    {
        return isElementDisplayed( ADD_BUTTON_XPATH );
    }

    public void clickOnAddButton()
    {
        if ( !isElementDisplayed( ADD_BUTTON_XPATH ) )
        {
            saveScreenshot( "err_add_button" );
            throw new TestFrameworkException( "Add button not present in Form View Panel!" );
        }
        getDisplayedElement( By.xpath( ADD_BUTTON_XPATH ) ).click();
        sleep( 500 );
    }

    protected String executeScriptInHtmlArea( WebElement htmlAreaFrame, String script )
    {
        String wHandle = getDriver().getWindowHandle();
        getDriver().switchTo().frame( htmlAreaFrame );
        Object obj = getJavaScriptExecutor().executeScript( script );
        String text = obj.toString();
        getDriver().switchTo().window( wHandle );
        return text;
    }

    public List<String> getDataFromCKEAreas()
    {
        List<WebElement> editors =
            findElements( By.xpath( "//div[contains(@id,'api.form.FormView')]//textarea[contains(@id,'TextArea')]" ) );
        return editors.stream().map( e -> getCKEData( e.getAttribute( "id" ) ) ).collect( Collectors.toList() );
    }

    protected String getCKEData( String id )
    {
        return ((String) getJavaScriptExecutor().executeScript( SCRIPT_DATA_CKE, id )).trim();
    }

    public boolean isValidationMessagePresent()
    {
        return isElementDisplayed( VALIDATION_VIEWER );
    }

    public String getValidationMessage()
    {
        return getDisplayedString( VALIDATION_MESSAGE );
    }

    public void doScrollPanel( int scrollTop )
    {
        String xpathPanel = "//div[contains(@id,'ui.panel.Panel') and contains(@class,'panel-strip-scrollable')]";
        WebElement panel = getDisplayedElement( By.xpath( xpathPanel ) );
        String id = panel.getAttribute( "id" );
        String script = "document.getElementById(arguments[0]).scrollTop=arguments[1]";
        getJavaScriptExecutor().executeScript( script, id, scrollTop );
    }
}
