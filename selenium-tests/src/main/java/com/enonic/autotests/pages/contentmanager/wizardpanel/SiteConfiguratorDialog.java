package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.LoaderComboBox;
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroModalDialog;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class SiteConfiguratorDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'SiteConfiguratorDialog')]";

    protected final String POSTS_OPTION_FILTER_INPUT =
        DIALOG_CONTAINER + "//div[contains(@id,'LoaderComboBox') and @name='postsFolder']" + COMBOBOX_OPTION_FILTER_INPUT;

    private final String INSERT_LINK_BUTTON = "//div[@aria-label='Insert/edit link']";

    private final String TOOLBAR_INSERT_MACRO_BUTTON = "//div[contains(@class,'mce-btn') and @aria-label='Insert macro']";

    public final String CANCEL_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Cancel']]";

    public final String APPLY_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Apply']]";

    public final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + APP_CANCEL_BUTTON_TOP;

    @FindBy(xpath = CANCEL_BUTTON)
    WebElement cancelButton;

    @FindBy(xpath = APPLY_BUTTON)
    WebElement applyButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    WebElement closeButton;

    @FindBy(xpath = POSTS_OPTION_FILTER_INPUT)
    protected WebElement postsOptionFilterInput;

    public boolean isCancelTopButtonPresent()
    {
        return isElementDisplayed( CANCEL_BUTTON_TOP );
    }

    public boolean isCancelBottomButtonPresent()
    {
        return isElementDisplayed( CANCEL_BUTTON );
    }

    public boolean isApplyButtonPresent()
    {
        return isElementDisplayed( APPLY_BUTTON );
    }

    public SiteConfiguratorDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( DIALOG_CONTAINER );
    }

    public SiteConfiguratorDialog selectPostsFolder( String targetName )
    {
        typePostsFilter( targetName );
        if ( isNoMatchingItemsForPostsFolder() )
        {
            return this;
        }
        LoaderComboBox loaderComboBox = new LoaderComboBox( getSession() );
        loaderComboBox.selectOption( targetName );
        sleep( 400 );
        return this;
    }

    public void typePostsFilter( String targetName )
    {
        clearAndType( postsOptionFilterInput, targetName );
        sleep( 300 );
    }

    public boolean isNoMatchingItemsForPostsFolder()
    {
        String message = DIALOG_CONTAINER + "//div[@class='empty-options' and text()='No matching items']";
        return isElementDisplayed( message );
    }

    public void clickOnCancelButton()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( CANCEL_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            saveScreenshot( "err_cancel-site-config" );
            throw new TestFrameworkException( "button 'cancel' on 'site-config-dialog' was not found!" );
        }
        cancelButton.click();
        sleep( 500 );
    }

    public void doApply()
    {
        boolean isApplyButtonPresent = waitUntilVisibleNoException( By.xpath( APPLY_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !isApplyButtonPresent )
        {
            saveScreenshot( "err_apply-site-config" );
            throw new TestFrameworkException( "button 'apply' on 'site-config-dialog' was not found!" );
        }
        buildActions().moveToElement( applyButton ).click().build().perform();
        waitForDialogClosed();
    }

    public boolean waitForDialogClosed()
    {
        boolean isDialogInvisible = waitInvisibilityOfElement( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL );
        if ( !isDialogInvisible )
        {
            saveScreenshot( "err_site_configurator_not_closed!" );
            throw new TestFrameworkException( "site config was not closed" );
        }
        return isDialogInvisible;
    }

    public SiteConfiguratorDialog waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), EXPLICIT_LONG ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err-site-cfg-dialog" ) );
            throw new TestFrameworkException( "Site-Configurator Dialog was not opened!" );
        }
        return this;
    }

    public String getTitle()
    {
        return getDisplayedString( DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]" + H6_MAIN_NAME );
    }

    public SiteConfiguratorDialog selectBackgroundColor( String color )
    {
        String radioButton =
            String.format( DIALOG_CONTAINER + "//input[contains(@name,'backgroundColor') and @value='%s']", color.toLowerCase() );
        Actions builder = new Actions( getDriver() );
        if ( !isElementDisplayed( radioButton ) )
        {
            saveScreenshot( "err_" + NameHelper.uniqueName( "radioBtn" ) );
            throw new TestFrameworkException( "radio button was not found!" );
        }
        builder.click( findElement( By.xpath( radioButton ) ) ).build().perform();
        sleep( 500 );
        return this;
    }

    public SiteConfiguratorDialog selectBackGroundImage( String imageName )
    {
        String optionFilter = DIALOG_CONTAINER + "//div[contains(@id,'ImageContentComboBox')]" + COMBOBOX_OPTION_FILTER_INPUT;
        if ( !isElementDisplayed( optionFilter ) )
        {
            saveScreenshot( "err_" + NameHelper.uniqueName( "optionFilter" ) );
            throw new TestFrameworkException( "ImageContentComboBox option filter was not found!" );
        }
        getDisplayedElement( By.xpath( optionFilter ) ).sendKeys( imageName );
        String imageXpath = DIALOG_CONTAINER + String.format( OPTION_IMAGE_SELECTOR_VIEW_BY_NAME, imageName );
        if ( !isElementDisplayed( imageXpath ) )
        {
            saveScreenshot( "img_file_not_found" );
            throw new TestFrameworkException( "wrong xpath:  " + imageName + "  " + imageXpath );
        }
        getDisplayedElement( By.xpath( imageXpath ) ).click();
        return this;
    }

    public InsertLinkModalDialog clickOnHtmlAreaInsertLinkButton()
    {
        WebElement textArea = getDisplayedElement( By.xpath( TEXT_AREA ) );
        buildActions().moveToElement( textArea ).click( textArea ).build().perform();
        textArea.sendKeys( "  " );
        if ( !isElementDisplayed( INSERT_LINK_BUTTON ) )
        {
            saveScreenshot( "err_insert_link" );
            throw new TestFrameworkException( "insert-link menu item not present!" );
        }
        getDisplayedElement( By.xpath( INSERT_LINK_BUTTON ) ).click();
        return new InsertLinkModalDialog( getSession() );
    }

    public MacroModalDialog showToolbarAndClickOnInsertMacroButton()
    {
        showToolbar();
        getDisplayedElement( By.xpath( TOOLBAR_INSERT_MACRO_BUTTON ) ).click();
        return new MacroModalDialog( getSession() );
    }

    private void showToolbar()
    {
        WebElement textArea = getDisplayedElement( By.xpath( TEXT_AREA ) );
        buildActions().moveToElement( textArea ).click( textArea ).build().perform();
        textArea.sendKeys( "  " );
    }

    public String getTextFromArea()
    {
        WebElement frame = findElement( By.xpath( TEXT_AREA ) );
        String TEXT_IN_AREA_SCRIPT = "return document.getElementById('tinymce').innerHTML";
        String contentStudioWHandle = getDriver().getWindowHandle();
        getDriver().switchTo().frame( frame );
        Object obj = ( (JavascriptExecutor) getSession().getDriver() ).executeScript( TEXT_IN_AREA_SCRIPT );
        String text = obj.toString();
        getDriver().switchTo().window( contentStudioWHandle );
        return text;
    }

    public void clickOnCancelButtonTop()
    {
        closeButton.click();
        sleep( 200 );
    }

    public void setTextIntoArea( String text )
    {
        String script = "document.getElementById('tinymce').contentDocument.body.innerHTML=arguments[0];";
        ( (JavascriptExecutor) getSession().getDriver() ).executeScript( script, text );
    }
}
