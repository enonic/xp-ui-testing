package com.enonic.autotests.pages.contentmanager;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ContentUnpublishDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'ContentUnpublishDialog')]";

    public static final String HEADER_TEXT = "Unpublish item";

    public static final String SUBHEADER_PART_TEXT = "Take offline? - Unpublishing selected item(s) will set status back to offline";

    private final String DIALOG_HEADER_H2 = DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]//h2[@class='title']";

    private final String DIALOG_HEADER_H6 = DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]//h6[@class='sub-title']";

    private final String UNPUBLISH_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and descendant::span[contains(.,'Unpublish')]]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + APP_CANCEL_BUTTON_TOP;

    private final String CANCEL_BUTTON_BOTTOM = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-bottom')]";

    private String CONTENT_STATUS_BY_DISPLAY_NAME = DIALOG_CONTAINER +
        "//div[contains(@id,'StatusSelectionItem') and descendant::h6[text()='%s']]//div[contains(@class,'status') and not(contains(@class,'summary'))]";

    private String DEPENDANTS_BLOCK = DIALOG_CONTAINER + "//div[@class='dependants']";

    private String DEPENDANT_LIST = DEPENDANTS_BLOCK + "//ul[@class='dependant-list']";

    private String DEPENDANT_ITEMS = DEPENDANT_LIST + "//div[contains(@id,'StatusSelectionItem')]";


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
            saveScreenshot( "err_unpublish_dialog_not_opened" );
            throw new TestFrameworkException( "Content unpublish dialog was not shown!" );
        }
        return this;
    }

    public List<String> getDependantNames()
    {
        return getDisplayedStrings( By.xpath( DEPENDANT_ITEMS + H6_DISPLAY_NAME ) );
    }

    public boolean isDependantsDisplayed()
    {
        return isElementDisplayed( By.xpath( DEPENDANT_LIST ) );
    }

    public boolean waitForClosed()
    {
        boolean result = waitsElementNotVisible( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            saveScreenshot( "content_unpublish_not_closed" );
        }
        return result;
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
        sleep( 800 );
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
