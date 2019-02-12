package com.enonic.autotests.pages.form;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.SourceCodeDialog;
import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertAnchorModalDialog;
import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertImageModalDialog;
import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertLinkModalDialog;
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroModalDialog;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public abstract class BaseHtmlAreaFormViewPanel
    extends FormViewPanel
{
    private final String TOOLBAR_INSERT_LINK_BUTTON = "//a[contains(@class,'cke_button') and contains(@title,'Link')]";

    private final String TOOLBAR_INSERT_MACRO_BUTTON = "//a[contains(@class,'cke_button') and @title='Insert macro']";

    private final String TOOLBAR_INSERT_IMAGE_BUTTON = "//a[contains(@class,'cke_button') and contains(@title,'Image')]";

    private final String TOOLBAR_INSERT_ANCHOR_BUTTON = "//a[contains(@class,'cke_button') and @title='Anchor']";

    private final String SOURCE_BUTTON = "//a[contains(@class,'cke_button__sourcedialog') and contains(@href,'Source')]";

    public static final String NUMBER_OF_EDITORS = "number-of-editors";

    public static String STRING_PROPERTY = "string";

    public static String STRINGS_PROPERTY = "strings";

    protected final String STEP_XPATH = "//li[contains(@id,'TabBarItem') and child::a[text()='Html Area']]";

    protected final String CKE = FORM_VIEW + "//div[contains(@id,'cke_api.ui.text.TextArea')]//div[contains(@class,'cke_contents')]";


    public BaseHtmlAreaFormViewPanel( final TestSession session )
    {
        super( session );
    }

    public int getNumberOfAreas()
    {
        return findElements( By.xpath( CKE ) ).size();
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

    public InsertAnchorModalDialog showToolbarAndClickOnInsertAnchorButton()
    {
        showToolbar();
        getDisplayedElement( By.xpath( TOOLBAR_INSERT_ANCHOR_BUTTON ) ).click();
        InsertAnchorModalDialog insertAnchorModalDialog = new InsertAnchorModalDialog( getSession() );
        insertAnchorModalDialog.waitForOpened();
        return insertAnchorModalDialog;
    }

    public MacroModalDialog showToolbarAndClickOnInsertMacroButton()
    {
        showToolbar();
        getDisplayedElement( By.xpath( TOOLBAR_INSERT_MACRO_BUTTON ) ).click();
        return new MacroModalDialog( getSession() );
    }

    public void showToolbar()
    {
        Actions builder = new Actions( getDriver() );
        if ( !waitUntilVisibleNoException( By.xpath( CKE_TEXT_AREA ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_textarea" ) );
            throw new TestFrameworkException( "Html Area was not found!" );
        }
        WebElement textArea = getDisplayedElement( By.xpath( CKE_TEXT_AREA ) );
        builder.moveToElement( textArea ).click( textArea ).build().perform();
        sleep(500);
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

    public SourceCodeDialog clickOnSourceButton()
    {
        findElement( By.xpath( SOURCE_BUTTON ) ).click();
        sleep( 500 );
        return new SourceCodeDialog( getSession() );
    }
}