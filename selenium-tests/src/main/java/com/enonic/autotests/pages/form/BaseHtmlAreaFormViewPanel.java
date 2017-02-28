package com.enonic.autotests.pages.form;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertImageModalDialog;
import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertLinkModalDialog;
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroModalDialog;
import com.enonic.autotests.utils.NameHelper;

public abstract class BaseHtmlAreaFormViewPanel
    extends FormViewPanel
{
    private final String TOOLBAR_INSERT_LINK_BUTTON = "//div[contains(@class,'mce-btn') and @aria-label='Insert/edit link']";

    private final String TOOLBAR_INSERT_MACRO_BUTTON = "//div[contains(@class,'mce-btn') and @aria-label='Insert macro']";

    private final String TOOLBAR_INSERT_IMAGE_BUTTON = "//div[contains(@class,'mce-btn') and @aria-label='Insert/edit image']";

    private final String SOURCE_BUTTON = "//div[contains(@class,'mce-btn') and @aria-label='Source code']";

    public static final String EMPTY_TEXT_AREA_CONTENT = "<div><br></div>";

    public static final String NUMBER_OF_EDITORS = "number-of-editors";

    public static String STRING_PROPERTY = "string";

    public static String STRINGS_PROPERTY = "strings";

    protected final String STEP_XPATH = "//li[contains(@id,'TabBarItem') and child::a[text()='Html Area']]";

    protected final String TINY_MCE = FORM_VIEW + "//div[contains(@class,'mce-edit-area')]" + TEXT_AREA;

    public BaseHtmlAreaFormViewPanel( final TestSession session )
    {
        super( session );
    }

    public int getNumberOfAreas()
    {
        return findElements( By.xpath( TINY_MCE ) ).size();
    }

    public InsertLinkModalDialog showToolbarAndClickOnInsertLinkButton()
    {
        showToolbar();
        getDisplayedElement( By.xpath( TOOLBAR_INSERT_LINK_BUTTON ) ).click();
        return new InsertLinkModalDialog( getSession() );
    }

    public InsertImageModalDialog showToolbarAndClickOnInsertImageButton()
    {
        showToolbar();
        getDisplayedElement( By.xpath( TOOLBAR_INSERT_IMAGE_BUTTON ) ).click();
        InsertImageModalDialog insertImageModalDialog = new InsertImageModalDialog( getSession() );
        insertImageModalDialog.waitForOpened();
        return insertImageModalDialog;
    }


    public MacroModalDialog showToolbarAndClickOnInsertMacroButton()
    {
        showToolbar();
        getDisplayedElement( By.xpath( TOOLBAR_INSERT_MACRO_BUTTON ) ).click();
        return new MacroModalDialog( getSession() );
    }

    private void showToolbar()
    {
        Actions builder = new Actions( getDriver() );
        if ( !waitUntilVisibleNoException( By.xpath( TEXT_AREA ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_textarea" ) );
            throw new TestFrameworkException( "Html Area was not found!" );
        }
        WebElement textArea = getDisplayedElement( By.xpath( TEXT_AREA ) );
        builder.moveToElement( textArea ).click( textArea ).build().perform();
        textArea.sendKeys( " " );
        if ( !isElementDisplayed( TOOLBAR_INSERT_LINK_BUTTON ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_html_toolbar" ) );
            throw new TestFrameworkException( "toolbar button was not found!" );
        }
    }

    public boolean isSourceCodeButtonDisplayed()
    {
        return isElementDisplayed( SOURCE_BUTTON );
    }
}
