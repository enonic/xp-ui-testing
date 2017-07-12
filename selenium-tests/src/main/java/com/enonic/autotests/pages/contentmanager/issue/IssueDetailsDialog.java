package com.enonic.autotests.pages.contentmanager.issue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

/**
 * Created on 7/6/2017.
 */
public class IssueDetailsDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'IssueDetailsDialog')]";

    private final String EDIT_ISSUE_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@class,'dialog-button') and child::span[text()='Edit Issue']]";

    private final String ISSUE_STATUS_SELECTOR =
        DIALOG_CONTAINER + "//div[contains(@id,'IssueStatusSelector')]//div[contains(@id,'TabMenuButton')]/a";

    private final String PUBLISH_BUTTON = DIALOG_CONTAINER + "//button[contains(@class,'publish-action')]";

    private final String BACK_BUTTON = DIALOG_CONTAINER + "//a[@class='back-button']";

    private final String OPENED_BY = DIALOG_CONTAINER + "//span[@class='creator']";

    @FindBy(xpath = ISSUE_STATUS_SELECTOR)
    private WebElement issueStatusSelector;

    @FindBy(xpath = EDIT_ISSUE_BUTTON)
    private WebElement editIssueButton;

    @FindBy(xpath = PUBLISH_BUTTON)
    private WebElement publishButton;

    @FindBy(xpath = BACK_BUTTON)
    private WebElement backButton;

    public IssueDetailsDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isEditIssueButtonDisplayed()
    {
        return editIssueButton.isDisplayed();
    }

    public void waitForLoaded()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_issue_details_dialog" );
            throw new TestFrameworkException( "'Issue Details' dialog was not opened!" );
        }
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

    public String getIssueStatus()
    {
        return getDisplayedString( ISSUE_STATUS_SELECTOR );
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

    public boolean isPublishButtonPresent()
    {
        return publishButton.isDisplayed();
    }

}
