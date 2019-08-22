package com.enonic.wem.uitest.content.move_publish_sort.issue

import com.enonic.autotests.pages.contentmanager.issue.CreateIssueDialog
import com.enonic.autotests.pages.contentmanager.issue.IssueDetailsDialog
import com.enonic.autotests.pages.contentmanager.issue.IssueListDialog
import com.enonic.autotests.pages.contentmanager.issue.UpdateIssueDialog
import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.Issue
import com.enonic.autotests.vo.usermanager.RoleName
import com.enonic.autotests.vo.usermanager.User
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 7/10/2017.
 * */
@Stepwise
class IssueDetailsDialog_Spec
    extends BaseIssueSpec
{

    @Shared
    User TEST_USER;

    @Shared
    private String USER_NAME = NameHelper.uniqueName( "user" );

    @Shared
    Content CONTENT;

    @Shared
    Issue TEST_ISSUE;

    @Shared
    String NEW_TITLE = "new issue-title";

    def setup()
    {
        go "admin"
    }

    def "setup: add a test user to the system user store"()
    {
        setup: "'Users' app is opened"
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );

        and: "build the new user"
        String[] roles = [RoleName.ADMIN_CONSOLE.getValue(), RoleName.CM_APP.getValue()];
        TEST_USER =
            User.builder().displayName( USER_NAME ).email( USER_NAME + "@gmail.com" ).password( "1q2w3e" ).roles( roles.toList() ).build();
        and: "select the Users-folder"
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder( UserItemName.USERS_FOLDER ).clickOnToolbarNew(
            UserItemName.USERS_FOLDER );

        when: "data was typed and user saved"
        userWizardPanel.typeData( TEST_USER ).save().close( TEST_USER.getDisplayName() );
        userBrowsePanel.getFilterPanel().typeSearchText( TEST_USER.getDisplayName() );

        then: "new user should be present beneath the system store"
        userBrowsePanel.exists( TEST_USER.getDisplayName(), true );
    }

    def "GIVEN create issue dialog is opened WHEN data has been typed AND 'Create' button has been pressed THEN Issue Details dialog should be correctly displayed"()
    {
        setup: "Content Studio is opened"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        filterPanel = contentBrowsePanel.getFilterPanel();

        and: "folder has been added"
        CONTENT = buildFolderContent( "folder", "issue details dialog test" )
        addContent( CONTENT );

        List<String> assigneesList = new ArrayList<>();
        assigneesList.add( TEST_USER.getName() );

        and: "create issue dialog is opened and data has been typed"
        TEST_ISSUE = buildIssue( "issue 1", assigneesList, null );
        CreateIssueDialog createIssueDialog = findAndSelectContent( CONTENT.getName() ).showPublishMenu().selectCreateIssueMenuItem();
        createIssueDialog.typeData( TEST_ISSUE );

        when: "'Create' button has been pressed"
        createIssueDialog.clickOnCreateIssueButton();
        IssueDetailsDialog issueDetailsDialog = new IssueDetailsDialog( getSession() );
        saveScreenshot( "issue_details_dialog" )

        then: "Issue details dialog should be displayed"
        issueDetailsDialog.waitForLoaded();

        and: "'Close Issue' button should be present"
        issueDetailsDialog.isCloseIssueButtonPresent();
        and: "'Back' button should be present"
        issueDetailsDialog.isBackButtonDisplayed();
        and: "status of the issue should be 'Open'"
        issueDetailsDialog.getIssueStatus() == "Open"
    }

    def "GIVEN existing user and an issue was assigned to him WHEN the user is logged in THEN 'You have unclosed Publishing Issues' message should be present on the toolbar"()
    {
        given: "existing assigned user"
        getTestSession().setUser( TEST_USER );

        when: "the user is 'logged in'"
        NavigatorHelper.openContentStudioApp( getTestSession() );
        saveScreenshot( "logged_home" + USER_NAME );

        then: "'You have unclosed Publishing Issues' message should be present on the toolbar"
        contentBrowsePanel.hasAssignedIssues();
    }

    def "GIVEN existing user and an issue was assigned to him WHEN Issue List dialog has been opened AND 'Assigned to Me' checkbox clicked THEN one issue should be listed on the dialog"()
    {
        given: "existing assigned user is logged in"
        getTestSession().setUser( TEST_USER );
        NavigatorHelper.openContentStudioApp( getTestSession() );

        when: "'Assigned to Me' has been checked"
        IssueListDialog issueListDialog = contentBrowsePanel.clickOnToolbarShowIssues();
        //issueListDialog.setAssignedToMeCheckbox( true );
        saveScreenshot( "assigned_issue" + USER_NAME );
        List<String> titles = issueListDialog.getIssueTitles();

        then: "one issue with the correct name should be displayed"
        titles.size() == 1;
        and:
        titles.get( 0 ).contains( TEST_ISSUE.getTitle() );
    }

    def "GIVEN existing user and an issue was assigned to him AND 'Issue List' dialog is opened WHEN issue has been clicked THEN 'Issue details dialog' should be opened"()
    {
        given: "existing assigned user is logged in"
        getTestSession().setUser( TEST_USER );
        NavigatorHelper.openContentStudioApp( getTestSession() );
        and: "'Assigned to Me' has been checked"
        IssueListDialog issueListDialog = contentBrowsePanel.clickOnToolbarShowIssues();
       // issueListDialog.setAssignedToMeCheckbox( true );

        when: "issue has been clicked"
        IssueDetailsDialog detailsDialog = issueListDialog.clickOnIssue( TEST_ISSUE.getTitle() );
        saveScreenshot( "issue_clicked" );

        then: "Issue Details dialog should be loaded"
        detailsDialog.waitForLoaded();

        and: "correct creator should be displayed"
        detailsDialog.getOpenedBy().contains( "user:system:su" );
    }

    def "GIVEN 'Issue details dialog' is opened WHEN Back button has been pressed THEN 'Issue List dialog' should be loaded"()
    {
        given: "existing assigned user is logged in"
        getTestSession().setUser( TEST_USER );
        NavigatorHelper.openContentStudioApp( getTestSession() );
        and: "'Assigned to Me' has been checked"
        IssueListDialog issueListDialog = contentBrowsePanel.clickOnToolbarShowIssues();
       // issueListDialog.setAssignedToMeCheckbox( true );
        IssueDetailsDialog detailsDialog = issueListDialog.clickOnIssue( TEST_ISSUE.getTitle() );

        when: "'Back' button has been pressed"
        IssueListDialog listDialog = detailsDialog.clickOnBackToListButton();
        saveScreenshot( "issue_clicked" );

        then: "Issue List dialog should be loaded"
        listDialog.waitForOpened();
    }
}
