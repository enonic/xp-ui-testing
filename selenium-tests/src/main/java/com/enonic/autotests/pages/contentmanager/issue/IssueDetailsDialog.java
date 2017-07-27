package com.enonic.autotests.pages.contentmanager.issue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 7/6/2017.
 */
public class IssueDetailsDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'IssueDetailsDialog')]";

    private final String REQ_TITLE = DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]//h2[@class='title' and contains(.,'%s')]";

    private final String TITLE = DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]//h2[@class='title']";

    private final String EDIT_ISSUE_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@class,'dialog-button') and child::span[text()='Edit Issue']]";

    private final String ISSUE_STATUS_SELECTOR = DIALOG_CONTAINER + "//div[contains(@id,'IssueStatusSelector')]";

    private final String ISSUE_STATUS = ISSUE_STATUS_SELECTOR + "//div[contains(@id,'TabMenuButton')]/a";

    private final String PUBLISH_BUTTON = DIALOG_CONTAINER + "//button[contains(@class,'publish-action')]";

    private final String BACK_BUTTON = DIALOG_CONTAINER + "//a[@class='back-button']";

    private final String OPENED_BY = DIALOG_CONTAINER + "//span[@class='creator']";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + "//div[@class='cancel-button-top']";

    private final String STOP_ISSUE_BUTTON = "//li[contains(@id,'TabMenuItem') and child::a[text()='Closed']]";

    private final String START_ISSUE_BUTTON = "//li[contains(@id,'TabMenuItem') and child::a[text()='Open']]";

    private final String STATUS_INFO = DIALOG_CONTAINER + "//span[@class='status-info']";

    private final String CREATOR = DIALOG_CONTAINER + "//span[@class='creator']";

    private final String DEPENDANT_LIST = DIALOG_CONTAINER + "//ul[contains(@id,'PublishDialogDependantList')]";

    private final String DEPENDANT_LIST_NAMES = DEPENDANT_LIST + H6_MAIN_NAME;

    @FindBy(xpath = ISSUE_STATUS_SELECTOR)
    private WebElement issueStatusSelector;

    @FindBy(xpath = EDIT_ISSUE_BUTTON)
    private WebElement editIssueButton;

    @FindBy(xpath = PUBLISH_BUTTON)
    private WebElement publishButton;

    @FindBy(xpath = BACK_BUTTON)
    private WebElement backButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    public IssueDetailsDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isEditIssueButtonDisplayed()
    {
        return editIssueButton.isDisplayed();
    }

    public void clickOnCancelButtonTop()
    {
        cancelButtonTop.click();
        sleep( 300 );
    }

    public List<String> getDependantNames()
    {
        return getDisplayedStrings( By.xpath( DEPENDANT_LIST_NAMES ) );
    }

    public void waitForLoaded()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_issue_details_dialog" );
            throw new TestFrameworkException( "'Issue Details' dialog was not opened!" );
        }
    }

    public boolean waitForTitle( String text )
    {
        String reqTitleXpath = String.format( REQ_TITLE, text );
        return waitUntilVisibleNoException( By.xpath( reqTitleXpath ), 3 );
    }

    public String getTitle()
    {
        return getDisplayedString( TITLE );
    }

    public String getOpenedBy()
    {
        return getDisplayedString( OPENED_BY );
    }

    public IssueDetailsDialog clickOnStatusSelectorMenu()
    {
        issueStatusSelector.click();
        return this;
    }

    public boolean isStopButtonActive()
    {
        WebElement stopButton = findElement( By.xpath( STOP_ISSUE_BUTTON ) );
        return waitAndCheckAttrValue( stopButton, "class", "active", 1 );
    }

    public IssueDetailsDialog doStopIssue()
    {
        if ( isStopButtonActive() )
        {
            saveScreenshot( "err_stop_issue" );
            throw new TestFrameworkException( "stop button should be not 'active'" );
        }
        getDisplayedElement( By.xpath( STOP_ISSUE_BUTTON ) ).click();
        return this;
    }

    public boolean waitForClosedStatus()
    {
        return waitAndCheckAttrValue( issueStatusSelector, "class", "closed", 1 );
    }

    public String getStatusInfo()
    {
        return getDisplayedString( STATUS_INFO );
    }

    public String getCreator()
    {
        return getDisplayedString( CREATOR );
    }

    public String getIssueStatus()
    {
        return getDisplayedString( ISSUE_STATUS );
    }

    public boolean isBackButtonDisplayed()
    {
        return backButton.isDisplayed();
    }

    public IssueListDialog clickOnBackToListButton()
    {
        backButton.click();
        IssueListDialog dialog = new IssueListDialog( getSession() );
        dialog.waitForOpened();
        return dialog;
    }

    public boolean isEditIssueButtonPresent()
    {
        return editIssueButton.isDisplayed();
    }

    public UpdateIssueDialog clickOnEditButton()
    {
        editIssueButton.click();
        sleep( 1000 );
        UpdateIssueDialog updateIssueDialog = new UpdateIssueDialog( getSession() );
        updateIssueDialog.waitForOpened();
        return updateIssueDialog;
    }


    public boolean isPublishButtonPresent()
    {
        return publishButton.isDisplayed();
    }

    public IssueListDialog clickOnPublishButton()
    {
        publishButton.click();
        IssueListDialog issueListDialog = new IssueListDialog( getSession() );
        issueListDialog.waitForOpened();
        return issueListDialog;
    }

    public boolean waitForClosed()
    {
        boolean result = waitsElementNotVisible( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            saveScreenshot( "issue_details_not_closed" );
        }
        return result;
    }
}
