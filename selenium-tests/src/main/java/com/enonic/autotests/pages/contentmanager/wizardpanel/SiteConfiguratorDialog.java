package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class SiteConfiguratorDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'SiteConfiguratorDialog')]";

    private final String SPEC_CHARS_BUTTON = "";

    private final String INSERT_LINK_BUTTON = "//div[@aria-label='Insert/edit link']";

    public final String CANCEL_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Cancel']";

    public final String APPLY_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Apply']]";

    public final String CANCEL_BUTTON_TOP =
        DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and contains(@calss,'cancel-button-top')]";

    @FindBy(xpath = CANCEL_BUTTON)
    WebElement cancelButton;

    @FindBy(xpath = APPLY_BUTTON)
    WebElement applyButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    WebElement closeButton;


    public SiteConfiguratorDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( DIALOG_CONTAINER );
    }

    public void doCancelDialog()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( CANCEL_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            TestUtils.saveScreenshot( getSession(), "err_cancel-site-config" );
            throw new TestFrameworkException( "button 'cancel' on 'site-config-dialog' was not found!" );
        }
        cancelButton.click();
        sleep( 500 );
    }

    public void doApply()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( APPLY_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            TestUtils.saveScreenshot( getSession(), "err_apply-site-config" );
            throw new TestFrameworkException( "button 'apply' on 'site-config-dialog' was not found!" );
        }
        applyButton.click();
        sleep( 1500 );
    }

    public void doClose()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( CANCEL_BUTTON_TOP ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            TestUtils.saveScreenshot( getSession(), "err_close-site-config" );
            throw new TestFrameworkException( "button 'close' on 'site-config-dialog' was not found!" );
        }
        closeButton.click();
        sleep( 500 );
    }

    public SiteConfiguratorDialog waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), EXPLICIT_LONG ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err-site-configurator-dialog" ) );
            throw new TestFrameworkException( "Site-Configurator Dialog was not opened!" );
        }
        return this;
    }

    public String getTitle()
    {
        return getDisplayedString( DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]//h6[@class='main-name']" );
    }

    public SiteConfiguratorDialog selectBackgroundColor( String color )
    {
        String radioButton = String.format( DIALOG_CONTAINER + "//input[@name='backgroundColor' and @value='%s']", color.toLowerCase() );
        Actions builder = new Actions( getDriver() );
        builder.click( findElement( By.xpath( radioButton ) ) ).build().perform();
        sleep( 500 );
        return this;
    }

    public InsertLinkModalDialog clickOnHtmlAreaInsertLinkButton()
    {
        Actions builder = new Actions( getDriver() );
        String textAreaXpath = "//iframe[contains(@id,'api.ui.text.TextArea')]";
        WebElement textArea = getDisplayedElement( By.xpath( textAreaXpath ) );
        builder.moveToElement( textArea ).click( textArea ).build().perform();
        getDisplayedElement( By.xpath( INSERT_LINK_BUTTON ) ).click();
        return new InsertLinkModalDialog( getSession() );
    }
}
