package com.enonic.autotests.pages.contentmanager.wizardpanel;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class PageComponentsViewDialog
    extends Application
{
    public static final String DIALOG_HEADER = "Page Components";

    public final String DIALOG_CONTAINER = "//div[contains(@id,'PageComponentsView')]";

    public final String CLOSE_BUTTON = DIALOG_CONTAINER + "//button[contains(@id,'CloseButton')]";

    private final String SLICK_VIEW_PORT = "//div[@class='slick-viewport']";

    @FindBy(xpath = CLOSE_BUTTON)
    WebElement closeButton;

    public PageComponentsViewDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return findElements( By.xpath( DIALOG_CONTAINER ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public String getTextFromHeader()
    {
        return getDisplayedElement( By.xpath( DIALOG_CONTAINER + "//h2[@class='header']" ) ).getText();
    }

    public String getContentName()
    {
        return getDisplayedElement( By.xpath( DIALOG_CONTAINER + "//h6[@class='main-name']" ) ).getText();
    }

    public PageComponentsViewDialog waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), EXPLICIT_LONG ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err-page-comp-dialog" ) );
            throw new TestFrameworkException( "Page Components Dialog was not opened!" );
        }
        return this;
    }

    public void doCloseDialog()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( CLOSE_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            TestUtils.saveScreenshot( getSession(), "err_close-button" );
            throw new TestFrameworkException( "close button was not found!" );
        }
        closeButton.click();
        sleep( 500 );
    }

    public boolean isCloseButtonPresent()
    {
        return waitUntilVisibleNoException( By.xpath( CLOSE_BUTTON ), Application.EXPLICIT_NORMAL );
    }

//    public List<PageComponent> getPageComponents()
//    {
//        findElements( By.xpath( DIALOG_CONTAINER + SLICK_VIEW_PORT +
//                                    "//div[contains(@id,'PageComponentsItemViewer')]//div[contains(@id,'api.app.NamesView')]" ) ).stream().filter(
//            WebElement::isDisplayed )
//        return null;
//    }
}
