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

    public static String VALIDATION_MESSAGE_OCCURRENCE = "This field is required";

    protected final String INNER_HTML_IN_AREA_SCRIPT = "return document.getElementById('tinymce').innerHTML";

    protected final String INNER_TEXT_IN_AREA_SCRIPT = "return document.getElementById('tinymce').innerText";

    protected final String TEXT_AREA = "//iframe[contains(@id,'api.ui.text.TextArea')]";

    protected final String ADD_BUTTON_XPATH = FORM_VIEW + "//div[@class='bottom-button-row']//button[child::span[text()='Add']]";

    protected final String SCRIPT_SET_INNERHTML = "document.getElementById(arguments[0]).contentDocument.body.innerHTML=arguments[1];";

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

    protected String getInnerHtmlFromArea( WebElement htmlAreaFrame )
    {
        return executeScriptInHtmlArea( htmlAreaFrame, INNER_HTML_IN_AREA_SCRIPT );
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

    protected String getInnerTextFromArea( WebElement htmlAreaFrame )
    {
        return executeScriptInHtmlArea( htmlAreaFrame, INNER_TEXT_IN_AREA_SCRIPT );
    }

    public List<String> getInnerHtmlFromAreas()
    {
        List<WebElement> frames = findElements( By.xpath( TEXT_AREA ) );
        return frames.stream().map( e -> getInnerHtmlFromArea( e ) ).collect( Collectors.toList() );
    }

    protected void setTextIntoArea( String id, String text )
    {
        getJavaScriptExecutor().executeScript( SCRIPT_SET_INNERHTML, id, text );
    }
}
