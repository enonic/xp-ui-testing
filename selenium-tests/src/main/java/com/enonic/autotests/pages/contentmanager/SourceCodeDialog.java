package com.enonic.autotests.pages.contentmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

/**
 * Created on 20.10.2016.
 */
public class SourceCodeDialog
    extends Application
{
    public static final String WINDOW_TITLE = "Source code";

    private final String WINDOW = "//div[contains(@class,'mce-reset') and descendant::div[contains(.,'Source code')]]";

    private final String TITLE_XPATH = WINDOW + "//div[contains(@class,'mce-title')]";

    private final String TEXT_AREA = WINDOW + "//textarea";

    private final String BUTTON_OK = WINDOW + "//button[contains(@id,'button') and descendant::span[text()='Ok']]";

    private final String BUTTON_CLOSE = WINDOW + "//button[contains(@class,'mce-close')]";

    private final String BUTTON_CANCEL = WINDOW + "//button[contains(@id,'button') and descendant::span[text()='Cancel']]";

    @FindBy(xpath = BUTTON_OK)
    WebElement buttonOk;

    @FindBy(xpath = BUTTON_CANCEL)
    WebElement buttonCancel;

    @FindBy(xpath = BUTTON_CLOSE)
    WebElement buttonClose;

    public SourceCodeDialog( final TestSession session )
    {
        super( session );
    }

    public void waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( WINDOW ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_source_code_dialog" );
            throw new TestFrameworkException( "'Source Code' dialog was not opened!" );
        }
    }

    public boolean isOpened()
    {
        return isElementDisplayed( WINDOW );
    }

    public String getText()
    {
        String id = findElement( By.xpath( TEXT_AREA ) ).getAttribute( "id" );
        String script = "return document.getElementById(arguments[0]).value";
        String result = getJavaScriptExecutor().executeScript( script, id ).toString();
        return result;
    }

    public boolean isTitlePresent()
    {
        return isElementDisplayed( TITLE_XPATH );
    }

    public String getTitle()
    {
        return getDisplayedString( TITLE_XPATH );
    }

    public boolean isButtonOkPresent()
    {
        return buttonOk.isDisplayed();
    }

    public boolean isButtonCancelPresent()
    {
        return buttonCancel.isDisplayed();
    }

    public boolean isButtonClosePresent()
    {
        return buttonClose.isDisplayed();
    }
}
