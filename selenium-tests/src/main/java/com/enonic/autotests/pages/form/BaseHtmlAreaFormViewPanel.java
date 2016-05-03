package com.enonic.autotests.pages.form;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertLinkModalDialog;
import com.enonic.autotests.utils.TestUtils;

public abstract class BaseHtmlAreaFormViewPanel
    extends FormViewPanel
{
    private final String TOOLBAR_INSERT_LINK_BUTTON = "//div[@aria-label='Insert/edit link']";

    public static final String EMPTY_TEXT_AREA_CONTENT = "<p><br data-mce-bogus=\"1\"></p>";

    public static final String NUMBER_OF_EDITORS = "number-of-editors";

    public static String STRING_PROPERTY = "string";

    public static String STRINGS_PROPERTY = "strings";

    protected final String STEP_XPATH = "//li[contains(@id,'api.ui.tab.TabBarItem')]//span[text()='Html Area']";

    protected final String TEXT_IN_AREA_SCRIPT = "return document.getElementById('tinymce').innerHTML";

    protected final String TINY_MCE = FORM_VIEW + "//div[contains(@class,'mce-edit-area')]//iframe[contains(@id,'api.ui.text.TextArea')]";

    protected final String TEXT_AREA = "//iframe[contains(@id,'api.ui.text.TextArea')]";

    public BaseHtmlAreaFormViewPanel( final TestSession session )
    {
        super( session );
    }

    public int getNumberOfAreas()
    {
        return findElements( By.xpath( TINY_MCE ) ).size();
    }

    public String getTextFromArea( WebElement htmlAreaFrame )
    {
        String wHandle = getDriver().getWindowHandle();
        getDriver().switchTo().frame( htmlAreaFrame );
        Object obj = ( (JavascriptExecutor) getSession().getDriver() ).executeScript( TEXT_IN_AREA_SCRIPT );
        String text = obj.toString();
        getDriver().switchTo().window( wHandle );
        return text;
    }

    public InsertLinkModalDialog showToolbarAndClickOnInsertLinkButton()
    {
        Actions builder = new Actions( getDriver() );
        String textAreaXpath = "//iframe[contains(@id,'api.ui.text.TextArea')]";
        WebElement textArea = getDisplayedElement( By.xpath( textAreaXpath ) );
        builder.moveToElement( textArea ).click( textArea ).build().perform();
        textArea.sendKeys( "  " );
        if ( !isElementDisplayed( TOOLBAR_INSERT_LINK_BUTTON ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_insert_link" );
            throw new TestFrameworkException( "insert-link menu item not present!" );
        }
        getDisplayedElement( By.xpath( TOOLBAR_INSERT_LINK_BUTTON ) ).click();
        return new InsertLinkModalDialog( getSession() );
    }
}
