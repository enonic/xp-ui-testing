package com.enonic.autotests.pages.contentmanager.issue;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;

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

    private final String TYPE_FILTER = "//div[contains(@id,'TypeFilter')]";

    private final String TYPE_FILTER_DROPDOWN_HANDLE = DIALOG_CONTAINER + "//div[contains(@id,'TypeFilter')]" + DROP_DOWN_HANDLE_BUTTON;

    private final String CLOSED_TASKS_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@id,'StatusFilterButton') and child::span[contains(.,'Closed')]]";

    private final String NEW_TASK_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@class,'dialog-button') and child::span[text()='New Task']]";


    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + APP_CANCEL_BUTTON_TOP;

    @FindBy(xpath = NEW_TASK_BUTTON)
    private WebElement newTaskButton;

    @FindBy(xpath = CLOSED_TASKS_BUTTON)
    private WebElement closedTasksButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    @FindBy(xpath = TYPE_FILTER_DROPDOWN_HANDLE)
    private WebElement typeFilterDropDownHandle;

    public IssueListDialog( final TestSession session )
    {
        super( session );
    }

    public void clickOnCancelButtonTop()
    {
        cancelButtonTop.click();
        sleep( 300 );
    }

    public void waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_create_issue_list_dialog" );
            throw new TestFrameworkException( "'Issue List' dialog was not opened!" );
        }
    }

    //Type Filter:
    public String getTypeFilterSelectedOption()
    {
        String selector = DIALOG_CONTAINER + TYPE_FILTER + "//button/span";
        return this.getDisplayedString( selector );
    }

    public boolean isIssuePresent( String displayName )
    {
        String xpath = String.format( ISSUE_BY_TITLE, displayName );
        return isElementDisplayed( By.xpath( xpath ) );
    }


    public boolean isClosedTasksButtonDisplayed()
    {
        return isElementDisplayed( CLOSED_TASKS_BUTTON );
    }

    public IssueListDialog clickOnClosedTasks()
    {
        closedTasksButton.click();
        sleep( 500 );
        return this;
    }

    public void clickOnAssignedToMeOption()
    {
        typeFilterDropDownHandle.click();
        String optionXpath =
            "//div[contains(@class,'slick-row') and descendant::div[contains(@id,'RowOptionDisplayValueViewer') and contains(.,'Assigned to Me')]]";
        boolean isVisible = waitUntilVisibleNoException( By.xpath( optionXpath ), Application.EXPLICIT_NORMAL );
        sleep( 400 );
        if ( !isVisible )
        {
            saveScreenshot( NameHelper.uniqueName( "err_option" ) );
            throw new TestFrameworkException( "option was not found! " + optionXpath );
        }
        getDisplayedElement( By.xpath( optionXpath ) ).click();
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
        sleep( 700 );
        IssueDetailsDialog issueDetailsDialog = new IssueDetailsDialog( getSession() );
        issueDetailsDialog.waitForLoaded();
        return issueDetailsDialog;
    }

    public boolean isNewTaskButtonDisplayed()
    {
        return newTaskButton.isDisplayed();
    }

    public CreateIssueDialog clickOnNewTaskButton()
    {
        newTaskButton.click();
        CreateIssueDialog createIssueDialog = new CreateIssueDialog( getSession() );
        createIssueDialog.waitForOpened();
        return createIssueDialog;
    }

    public List<String> getIssueTitles()
    {
        return getDisplayedStrings( By.xpath( ISSUES_TITLES ) );
    }

    public boolean waitForClosed()
    {
        boolean result = waitsElementNotVisible( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            saveScreenshot( "issue_list_not_closed" );
        }
        return result;
    }
}
