package com.enonic.autotests.pages.contentmanager.wizardpanel.image;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 28.09.2016.
 */
public class ImageEditorDialog
    extends Application
{
    private final String CONTAINER_DIV = "//div[contains(@id,'ImageEditor')]";

    private final String APPLY_BUTTON = CONTAINER_DIV + "//button//span[text()='Apply']";

    private final String CLOSE_BUTTON = CONTAINER_DIV + "//button[contains(@id,'CloseButton')]";

    private final String FOCUS_CIRCLE = CONTAINER_DIV + "//*[name()='svg']/*[name()='g' and contains(@class,'focus-group')]";

    @FindBy(xpath = CLOSE_BUTTON)
    WebElement closeButton;

    public ImageEditorDialog( TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( CONTAINER_DIV );
    }

    public ImageEditorDialog waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( CONTAINER_DIV ), EXPLICIT_NORMAL ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err-image-editor-dialog" ) );
            throw new TestFrameworkException( "Image Editor Dialog was not opened!" );
        }
        return this;
    }

    public boolean isApplyButtonDisplayed()
    {
        return isElementDisplayed( APPLY_BUTTON );
    }

    public boolean isCloseButtonPresent()
    {
        return isElementDisplayed( CLOSE_BUTTON );
    }

    public void clickOnCloseButton()
    {
        closeButton.click();
        sleep( 300 );
    }

    public boolean isFocusCircleDisplayed()
    {
        return isElementDisplayed( FOCUS_CIRCLE );
    }

}
