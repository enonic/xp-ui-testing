package com.enonic.wem.uitest.content.move_publish_sort.issue

import com.enonic.autotests.pages.Application
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
    Issue TEST_TASK;

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

    def "GIVEN existing user WHEN new task has been assigned to him THEN new task should be added in tasks list"()
    {
        setup: "Content Studio is opened"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        filterPanel = contentBrowsePanel.getFilterPanel();

        and: "folder has been added"
        CONTENT = buildFolderContent( "folder", "issue list dialog test" )
        addReadyContent( CONTENT );

        List<String> assigneesList = new ArrayList<>();
        assigneesList.add( TEST_USER.getName() );

        and: "create task dialog is opened and data has been typed"
        TEST_TASK = buildIssue( "task to close", assigneesList, null );
        CreateIssueDialog createIssueDialog = findAndSelectContent( CONTENT.getName() ).showPublishMenu().clickOnCreateTaskMenuItem();
        createIssueDialog.typeData( TEST_TASK );
        createIssueDialog.clickOnCreateTaskButton();

        when: "'Create' button has been pressed"
        IssueDetailsDialog issueDetailsDialog = new IssueDetailsDialog( getSession() );
        issueDetailsDialog.clickOnCancelButtonTop();

        then: "Task details dialog should be closed"
        issueDetailsDialog.waitForClosed();
    }

    //verifies the xp#5300 Notification about unclosed issues doesn't disappear after publishing of the last open issue
    def "GIVEN the user is logged in WHEN the user has opened 'Issue Details' and stopped the issue THEN the issue should be 'Closed' on the dialog"()
    {
        given: "existing assigned user is logged in"
        getTestSession().setUser( TEST_USER );
        NavigatorHelper.openContentStudioApp( getTestSession() );
        IssueListDialog dialog = contentBrowsePanel.clickOnToolbarShowIssues();

        when: "task has been clicked"
        IssueDetailsDialog taskDetailsDialog = dialog.clickOnIssue( TEST_TASK.getTitle() );

        and: "Open status menu and click on `Close` button(task has been closed)"
        taskDetailsDialog.clickOnStatusSelectorMenu().doCloseTask();

        then: "the task gets 'Closed'"
        taskDetailsDialog.waitForClosedStatus();
        saveScreenshot( "task_stopped_by_user" );

        and: "expected notification message should appear"
        contentBrowsePanel.waitForNotificationMessage() == Application.TASK_IS_CLOSED;
        taskDetailsDialog.clickOnCancelButtonTop();
        sleep( 2000 );
        saveScreenshot( "last_task_closed" );
    }

    def "GIVEN existing task is closed by the user AND SU is 'logged in' WHEN 'task details' dialog has been opened THEN correct user-name should be present in the 'Closed by'"()
    {
        given: "SU is logged in"
        getTestSession().setUser( null );
        NavigatorHelper.openContentStudioApp( getTestSession() );
        IssueListDialog issueListDialog = contentBrowsePanel.clickOnToolbarShowIssues();

        and: "Click on Closed button:"
        issueListDialog.clickOnClosedTasks();

        when: "click on the closed task:(Open Task Details dialog)"
        IssueDetailsDialog issueDetailsDialog = issueListDialog.clickOnIssue( TEST_TASK.getTitle() )

        then: "expected user-name should be present in the 'Closed by'"
        issueDetailsDialog.getStatusInfo().contains( String.format( "Closed by user:system:%s", TEST_USER.getName() ) );
    }
}
