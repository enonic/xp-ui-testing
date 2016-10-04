package com.enonic.autotests.pages.contentmanager.wizardpanel.image;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 30.09.2016.
 */
public class ImageEditorToolbar
    extends Application
{

    private final String TOOLBAR_CONTAINER = "//div[contains(@id,'ImageEditor')]" + "//div[contains(@class,'edit-container')]";

    private final String BUTTON_APPLY = TOOLBAR_CONTAINER + "//button/span[text()='Apply']";

    private final String BUTTON_CLOSE = TOOLBAR_CONTAINER + "//button[contains(@class,'close-button')]";

    @FindBy(xpath = BUTTON_CLOSE)
    WebElement closeButton;

    @FindBy(xpath = BUTTON_APPLY)
    WebElement applyButton;

    public ImageEditorToolbar( TestSession session )
    {
        super( session );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( TOOLBAR_CONTAINER );
    }

    public boolean isApplyButtonDisplayed()
    {
        return isElementDisplayed( BUTTON_APPLY );
    }

    public boolean isCloseButtonDisplayed()
    {
        return isElementDisplayed( BUTTON_CLOSE );
    }

    public void clickOnApplyButton()
    {
        applyButton.click();
        sleep( 200 );
    }

    public void clickOnCloseButton()
    {
        closeButton.click();
        sleep( 200 );
    }

}
