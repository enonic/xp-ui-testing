package com.enonic.autotests.pages.contentmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentUnpublishDialog
    extends Application
{
    public static final String HEADER_TEXT = "Unpublish item";

    public static final String SUBHEADER_PART_TEXT = "Unpublishing selected item(s) will set status back to offline";

    private final String DIALOG_CONTAINER = "//div[contains(@id,'ContentUnpublishDialog')]";

    private final String DIALOG_HEADER_H2 = DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]//h2[@class='title']";

    private final String DIALOG_HEADER_H6 = DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]//h6[@class='sub-title']";

    private final String UNPUBLISH_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and descendant::span[contains(.,'Unpublish')]]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + "//div[contains(@class,'cancel-button-top')]";

    private final String CANCEL_BUTTON_BOTTOM = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-bottom')]";

    private String CONTENT_STATUS_BY_DISPLAY_NAME =
        DIALOG_CONTAINER + "//div[@class='browse-selection-item' and descendant::h6[text()='%s']]//div[contains(@class,'status equal')]";

    @FindBy(xpath = UNPUBLISH_BUTTON)
    private WebElement unPublishButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    @FindBy(xpath = CANCEL_BUTTON_BOTTOM)
    private WebElement cancelButtonBottom;


    public ContentUnpublishDialog( TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( DIALOG_CONTAINER );
    }

    public ContentUnpublishDialog waitUntilDialogShown( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_unpublish_dialog_not_opened" );
            throw new TestFrameworkException( "Content unpublish dialog was not shown!" );
        }
        return this;
    }

    public boolean isUnPublishButtonEnabled()
    {
        return unPublishButton.isEnabled();
    }

    public boolean isCancelButtonTopEnabled()
    {
        return cancelButtonTop.isEnabled();
    }

    public boolean isCancelButtonBottomEnabled()
    {
        return cancelButtonBottom.isEnabled();
    }

    public void clickOnUnpublishButton()
    {
        unPublishButton.click();
        sleep( 500 );
    }

    public void clickOnCancelBottomButton()
    {
        cancelButtonBottom.click();
        sleep( 200 );
    }

    public void clickOnCancelTopButton()
    {
        cancelButtonTop.click();
        sleep( 200 );
    }

    public String getHeader()
    {
        return getDisplayedString( DIALOG_HEADER_H2 );
    }

    public String getSubHeader()
    {
        return getDisplayedString( DIALOG_HEADER_H6 );
    }

    public String getContentStatus( String displayName )
    {
        String status = String.format( CONTENT_STATUS_BY_DISPLAY_NAME, displayName );
        if ( !isElementDisplayed( status ) )
        {
            throw new TestFrameworkException( "status was not found! " + displayName );
        }
        return getDisplayedString( status );
    }
}
