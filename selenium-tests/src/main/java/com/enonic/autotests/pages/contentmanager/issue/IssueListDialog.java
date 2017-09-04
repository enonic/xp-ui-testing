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

    private final String OPEN_TAB_ITEM = DIALOG_CONTAINER + "//li[contains(@id,'TabBarItem') and descendant::a[contains(.,'Open')]]";

    private final String CLOSED_TAB_ITEM = DIALOG_CONTAINER + "//li[contains(@id,'TabBarItem') and descendant::a[contains(.,'Closed')]]";

    private final String NEW_ISSUE_BUTTON =
        DIALOG_CONTAINER + "//button[contains(@class,'dialog-button') and child::span[text()='New Issue...']]";

    private final String ASSIGNED_TO_ME_CHECKBOX = "//div[contains(@id,'Checkbox') and descendant::label[contains(.,'Assigned to Me')]]";

    private final String MY_ISSUES_CHECKBOX = "//div[contains(@id,'Checkbox') and descendant::label[contains(.,'My Issues')]]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + APP_CANCEL_BUTTON_TOP;

    @FindBy(xpath = NEW_ISSUE_BUTTON)
    private WebElement newIssueButton;

    @FindBy(xpath = ASSIGNED_TO_ME_CHECKBOX)
    private WebElement assignedToMeCheckbox;

    @FindBy(xpath = MY_ISSUES_CHECKBOX)
    private WebElement myIssuesCheckbox;

    @FindBy(xpath = OPEN_TAB_ITEM)
    private WebElement openTab;

    @FindBy(xpath = CLOSED_TAB_ITEM)
    private WebElement closedTab;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

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

    public boolean isAssignedToMeCheckboxDisabled()
    {
        return waitAndCheckAttrValue( assignedToMeCheckbox, "class", "disabled", 1 );
    }

    public boolean isIssuePresent( String displayName )
    {
        String xpath = String.format( ISSUE_BY_TITLE, displayName );
        return isElementDisplayed( By.xpath( xpath ) );
    }

    public boolean isMyIssuesCheckboxDisabled()
    {
        return waitAndCheckAttrValue( myIssuesCheckbox, "class", "disabled", 1 );
    }

    public boolean isOpenTabPresent()
    {
        return isElementDisplayed( OPEN_TAB_ITEM );
    }

    public boolean isClosedTabPresent()
    {
        return isElementDisplayed( CLOSED_TAB_ITEM );
    }

    public boolean isOpenTabActive()
    {
        return waitAndCheckAttrValue( findElement( By.xpath( OPEN_TAB_ITEM ) ), "class", "active", 1 );
    }

    public boolean isClosedTabActive()
    {
        return waitAndCheckAttrValue( findElement( By.xpath( CLOSED_TAB_ITEM ) ), "class", "active", 1 );
    }

    public IssueListDialog clickOnOpenTab()
    {
        openTab.click();
        return this;
    }

    public IssueListDialog clickOnClosedTab()
    {
        closedTab.click();
        return this;
    }

    public IssueListDialog setAssignedToMeCheckbox( boolean checked )
    {
        String id = assignedToMeCheckbox.getAttribute( "id" );
        String script = String.format( "window.api.dom.ElementRegistry.getElementById('%s')" + ".setChecked(%b)", id, checked );
        getJavaScriptExecutor().executeScript( script );
        sleep( 700 );
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
        sleep( 700 );
        IssueDetailsDialog issueDetailsDialog = new IssueDetailsDialog( getSession() );
        issueDetailsDialog.waitForLoaded();
        return issueDetailsDialog;
    }

    public boolean isNewIssueButtonDisplayed()
    {
        return newIssueButton.isDisplayed();
    }

    public CreateIssueDialog clickOnNewIssueDialog()
    {
        newIssueButton.click();
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
