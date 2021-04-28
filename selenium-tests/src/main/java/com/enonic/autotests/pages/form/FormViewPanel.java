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
    protected static final String FORM_VIEW = "//div[contains(@id,'FormView')]";

    protected final String CONTENT_SELECTED_OPTION_VIEW = "//div[contains(@id,'ContentSelectedOptionView')]";

    protected final String FORM_VALIDATION_VEW = "//div[contains(@id,'InputViewValidationViewer')]";

    protected final String OCCURRENCE_ERROR_BLOCK = "//div[contains(@id,'InputOccurrenceView')]//div[contains(@class,'error-block')]";

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
        List<WebElement> editors = findElements( By.xpath( FORM_VIEW + "//textarea[contains(@id,'TextArea')]" ) );
        return editors.stream().map( e -> getCKEData( e.getAttribute( "id" ) ) ).collect( Collectors.toList() );
    }

    protected String getCKEData( String id )
    {
        return ( (String) getJavaScriptExecutor().executeScript( SCRIPT_DATA_CKE, id ) ).trim();
    }

    protected String getOccurrenceValidationRecording( int index )
    {
        String locator = FORM_VIEW + OCCURRENCE_ERROR_BLOCK;
        List<WebElement> elements = findElements( By.xpath( locator ) );
        if ( elements.size() == 0 )
        {
            throw new Error( "Occurrence Element was not found:" + locator );
        }
        return elements.get( index ).getText();
    }

    protected boolean isFormValidationMessageDisplayed()
    {
        String locator = FORM_VIEW + FORM_VALIDATION_VEW;
        List<WebElement> elements = getDisplayedElements( By.xpath( locator ) );
        return elements.size() > 0;
    }

    protected String getFormValidationRecording( int index )
    {
        String locator = FORM_VIEW + FORM_VALIDATION_VEW;
        List<WebElement> elements = getDisplayedElements( By.xpath( locator ) );
        if ( elements.size() == 0 )
        {
            throw new TestFrameworkException( "Form validation recording is not visible" );
        }
        elements = getDisplayedElements( By.xpath( locator ) );
        return elements.get( index ).getText();
    }

    public void doScrollPanel( int scrollTop )
    {
        String xpathPanel = "//div[contains(@id,'Panel') and contains(@class,'panel-strip-scrollable')]";
        WebElement panel = getDisplayedElement( By.xpath( xpathPanel ) );
        String id = panel.getAttribute( "id" );
        String script = "document.getElementById(arguments[0]).scrollTop=arguments[1]";
        getJavaScriptExecutor().executeScript( script, id, scrollTop );
    }

    public void expandFormByLabel( String label )
    {
        String locator = String.format( "//div[contains(@id,'FormOccurrenceDraggableLabel') and text()='%s']", label );
        findElement( By.xpath( locator ) ).click();
        sleep( 500 );
    }
}
