package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.DeleteCMSObjectException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BaseModalDialog;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ConfirmationDialog
    extends BaseModalDialog
{

    public static final String YES_BUTTON_XPATH =
        "//div[contains(@id,'api.ui.dialog.ConfirmationDialog')]//div[@class='dialog-buttons']//button/span[text()='Yes']";

    private final String TITLE_XPATH =
        "//div[@class='modal-dialog confirmation-dialog']//div[@class='dialog-header' and contains(.,'Confirmation')]";

    @FindBy(xpath = YES_BUTTON_XPATH)
    private WebElement yesButton;

    /**
     * The constructor
     *
     * @param session
     */
    public ConfirmationDialog( TestSession session )
    {
        super( session );
    }

    public boolean verifyIsOpened()
    {
        return waitUntilVisibleNoException( By.xpath( TITLE_XPATH ), 2 );
    }

    public boolean waitForClosed()
    {
        return waitElementNotVisible( By.xpath( TITLE_XPATH ), 2 );
    }

    public ContentBrowsePanel doConfirm()
    {
        yesButton.click();

        boolean isClosed = waitForClosed();
        if ( !isClosed )
        {
            throw new DeleteCMSObjectException( "Confirm 'delete content' dialog was not closed!" );
        }
        ContentBrowsePanel table = new ContentBrowsePanel( getSession() );
        table.waituntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        sleep( 500 );
        return table;
    }
}
