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
 * Created  on 7/6/2017.
 */
public class IssueListDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'IssueListDialog')]";

    private final String ISSUES_LIST = "//ul[contains(@id,'IssueList')]";

    private final String ISSUES_TITLES = ISSUES_LIST + H6_DISPLAY_NAME;

    private String ISSUE_BY_TITLE = ISSUES_LIST + NAMES_VIEW_BY_DISPLAY_NAME;

    private final String NEW_ISSUE_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@class,'dialog-button') and child::span[text()='New Issue...']]";

    private final String ASSIGNED_TO_ME_CHECKBOX = "//div[contains(@id,'Checkbox') and descendant::label[contains(.,'Assigned to Me')]]";

    @FindBy(xpath = NEW_ISSUE_BUTTON)
    private WebElement newIssueButton;

    @FindBy(xpath = ASSIGNED_TO_ME_CHECKBOX)
    private WebElement assignedToMeCheckbox;

    public IssueListDialog( final TestSession session )
    {
        super( session );
    }

    public void waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_create_issue_list_dialog" );
            throw new TestFrameworkException( "'Issue List' dialog was not opened!" );
        }
    }

    public IssueListDialog setAssignedToMeCheckbox( boolean checked )
    {
        String id = assignedToMeCheckbox.getAttribute( "id" );
        String script = String.format( "window.api.dom.ElementRegistry.getElementById('%s')" + ".setChecked(%b)", id, checked );
        getJavaScriptExecutor().executeScript( script );
        sleep( 500 );
        return this;
    }

    public IssueDetailsDialog clickOnIssue( String tittle )
    {
        String issue = String.format( ISSUE_BY_TITLE, tittle );
        if ( !isElementDisplayed( By.xpath( issue ) ) )
        {
            saveScreenshot( "err_click_issue" );
            throw new TestFrameworkException( "issue was not found: " + tittle );
        }
        getDisplayedElement( By.xpath( issue ) ).click();
        IssueDetailsDialog issueDetailsDialog = new IssueDetailsDialog( getSession() );
        issueDetailsDialog.waitForLoaded();
        return issueDetailsDialog;
    }

    public boolean isNewIssueButtonDisplayed()
    {
        return newIssueButton.isDisplayed();
    }

    public List<String> getIssueTitles()
    {
        return getDisplayedStrings( By.xpath( ISSUES_TITLES ) );
    }
}
