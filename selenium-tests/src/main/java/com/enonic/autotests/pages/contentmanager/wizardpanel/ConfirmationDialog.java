package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ConfirmationDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'api.ui.dialog.ConfirmationDialog')]";

    public static final String YES_BUTTON_XPATH =
        "//div[contains(@id,'api.ui.dialog.ConfirmationDialog')]//div[@class='dialog-buttons']//button/span[text()='Yes']";

    public static final String NO_BUTTON_XPATH =
        "//div[contains(@id,'api.ui.dialog.ConfirmationDialog')]//div[@class='dialog-buttons']//button/span[text()='No']";


    private final String TITLE_TEXT = DIALOG_CONTAINER + "//div[@class='dialog-header']//h2";

    public static final String QUESTION = "Are you sure you want to delete this content?";

    private final String QUESTION_XPATH = DIALOG_CONTAINER + "//div[@class='question']";

    @FindBy(xpath = YES_BUTTON_XPATH)
    private WebElement yesButton;

    @FindBy(xpath = NO_BUTTON_XPATH)
    private WebElement noButton;

    /**
     * The constructor
     *
     * @param session
     */
    public ConfirmationDialog( TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), 2 );
    }

    public String getTitle()
    {
        return getDisplayedString( TITLE_TEXT );
    }

    public String getQuestion()
    {
        return getDisplayedString( QUESTION_XPATH );
    }

    public boolean waitForClosed()
    {
        return waitElementNotVisible( By.xpath( DIALOG_CONTAINER ), 2 );
    }

    public ContentBrowsePanel pressYesButton()
    {
        yesButton.click();
        boolean isClosed = waitForClosed();
        if ( !isClosed )
        {
            throw new TestFrameworkException( "Confirm 'delete content' dialog was not closed!" );
        }
        ContentBrowsePanel table = new ContentBrowsePanel( getSession() );
        table.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        sleep( 500 );
        return table;
    }

    public ContentBrowsePanel pressNoButton()
    {
        noButton.click();
        boolean isClosed = waitForClosed();
        if ( !isClosed )
        {
            throw new TestFrameworkException( "Confirm 'delete content' dialog was not closed!" );
        }
        ContentBrowsePanel browsePanel = new ContentBrowsePanel( getSession() );
        browsePanel.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        sleep( 500 );
        return browsePanel;
    }
}
