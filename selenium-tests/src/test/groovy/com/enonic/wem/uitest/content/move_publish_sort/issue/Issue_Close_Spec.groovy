package com.enonic.wem.uitest.content.move_publish_sort.issue

import com.enonic.autotests.pages.contentmanager.issue.CreateIssueDialog
import com.enonic.autotests.pages.contentmanager.issue.IssueDetailsDialog
import com.enonic.autotests.pages.contentmanager.issue.IssueListDialog
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
 * Created on 7/17/2017.
 * */
@Stepwise
class Issue_Close_Spec
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

    def setup()
    {
        go "admin"
    }

    def "setup: add a test user to the system user store"()
    {
        setup: "'Users' app is opened"
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );

        and: "build the new user"
        String[] roles = [RoleName.ADMIN_CONSOLE.getValue(), RoleName.CM_APP.getValue(), RoleName.CONTENT_MANAGER_ADMINISTRATOR.getValue()];
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

    def "GIVEN existing user WHEN when an issue created and assigned to him THEN issue should be listed"()
    {
        setup: "Content Studio is opened"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        filterPanel = contentBrowsePanel.getFilterPanel();

        and: "folder has been added"
        CONTENT = buildFolderContent( "folder", "issue list dialog test" )
        addContent( CONTENT );

        List<String> assigneesList = new ArrayList<>();
        assigneesList.add( TEST_USER.getName() );

        and: "create issue dialog is opened and data has been typed"
        TEST_ISSUE = buildIssue( "issue to close", assigneesList, null );
        CreateIssueDialog createIssueDialog = findAndSelectContent( CONTENT.getName() ).showPublishMenu().selectCreateIssueMenuItem();
        createIssueDialog.typeData( TEST_ISSUE );
        createIssueDialog.clickOnCreateIssueButton();

        when: "'Create' button has been pressed"
        IssueDetailsDialog issueDetailsDialog = new IssueDetailsDialog( getSession() );
        issueDetailsDialog.clickOnCancelButtonTop();

        then: "Issue details dialog should be closed"
        issueDetailsDialog.waitForClosed();
    }

    def "GIVEN the user is logged in WHEN the user has opened 'Issue Details' THEN 'Creator' should be displayed correctly"()
    {
        given: "existing assigned user is logged in"
        getTestSession().setUser( TEST_USER );
        NavigatorHelper.openContentStudioApp( getTestSession() );
        IssueListDialog dialog = contentBrowsePanel.clickOnToolbarShowIssues();
        dialog.setAssignedToMeCheckbox( true );

        when: "issue has been clicked"
        IssueDetailsDialog issueDetailsDialog = dialog.clickOnIssue( TEST_ISSUE.getTitle() );
        saveScreenshot( "issue_creator_su" )

        then: "status of the issue should be 'Stopped'"
        issueDetailsDialog.getCreator() == "user:system:su";

        and: ""
        issueDetailsDialog.getStatusInfo().contains( "Opened by user:system:su" );
    }

    //verifies the xp#5300 Notification about unclosed issues doesn't disappear after publishing of the last open issue
    def "GIVEN the user is logged in WHEN the user has opened 'Issue Details' and stopped the issue THEN the issue should be 'Stopped' on the dialog"()
    {
        given: "existing assigned user is logged in"
        getTestSession().setUser( TEST_USER );
        NavigatorHelper.openContentStudioApp( getTestSession() );
        IssueListDialog dialog = contentBrowsePanel.clickOnToolbarShowIssues();
        dialog.setAssignedToMeCheckbox( true );

        when: "issue has been clicked"
        IssueDetailsDialog issueDetailsDialog = dialog.clickOnIssue( TEST_ISSUE.getTitle() );

        and: "issue has been stopped"
        issueDetailsDialog.clickOnStatusSelectorMenu().doStopIssue();

        then: "status of the issue should be 'Stopped'"
        issueDetailsDialog.waitForClosedStatus();
        saveScreenshot( "issue_stopped_by_user" );

        and: "correct notification message should be displayed"
        contentBrowsePanel.waitForNotificationMessage() == "The issue is Closed.";

        and: "'Has Assigned issues' icon should not be displayed"
        contentBrowsePanel.waitForAssignedIssuesIconNotVisible();
        saveScreenshot( "issue_stopped_icon_hidden" );
    }

    def "GIVEN existing issue is closed by the user AND SU is 'logged in' WHEN 'issue details' dialog has been opened THEN correct user-name should be present in the 'Closed by'"()
    {
        given: "SU is logged in"
        getTestSession().setUser( null );
        NavigatorHelper.openContentStudioApp( getTestSession() );
        IssueListDialog issueListDialog = contentBrowsePanel.clickOnToolbarShowIssues();
        issueListDialog.clickOnShowClosedIssues();

        when: "issue details dialog has been opened"
        IssueDetailsDialog issueDetailsDialog = issueListDialog.clickOnIssue( TEST_ISSUE.getTitle() )

        then: "correct user-name should be present in the 'Closed by'"
        issueDetailsDialog.getStatusInfo().contains( String.format( "Closed by user:system:%s", TEST_USER.getName() ) );
    }
}
