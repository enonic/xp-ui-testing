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
        String[] roles = [RoleName.ADMIN_CONSOLE.getValue(), RoleName.CM_APP.getValue()];
        TEST_USER = User.builder().displayName( USER_NAME ).email( USER_NAME + "@gmail.com" ).password( Application.MEDIUM_PASSWORD ).roles(
            roles.toList() ).build();
        and: "select the Users-folder"
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickOnRowByName( "users" ).clickOnToolbarNew(
            UserItemName.USERS_FOLDER );

        when: "data was typed and user saved"
        userWizardPanel.typeData( TEST_USER ).save().close( TEST_USER.getDisplayName() );
        userBrowsePanel.getFilterPanel().typeSearchText( TEST_USER.getDisplayName() );

        then: "new user should be present beneath the system store"
        userBrowsePanel.exists( TEST_USER.getDisplayName(), true );
    }

    def "GIVEN 'Create Task' dialog is opened WHEN data has been typed AND 'Create Task' button has been pressed THEN Issue Details dialog should be correctly displayed"()
    {
        setup: "Content Studio is opened"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        filterPanel = contentBrowsePanel.getFilterPanel();

        and: "folder has been added"
        CONTENT = buildFolderContent( "folder", "issue details dialog test" )
        addReadyContent( CONTENT );

        List<String> assigneesList = new ArrayList<>();
        assigneesList.add( TEST_USER.getName() );

        and: "Create task dialog is opened and data has been typed"
        TEST_TASK = buildIssue( "task 1", assigneesList, null );
        CreateIssueDialog createIssueDialog = findAndSelectContent( CONTENT.getName() ).showPublishMenu().clickOnCreateTaskMenuItem();
        createIssueDialog.typeData( TEST_TASK );

        when: "'Create Task' button has been pressed"
        createIssueDialog.clickOnCreateTaskButton();
        IssueDetailsDialog taskDetailsDialog = new IssueDetailsDialog( getSession() );
        saveScreenshot( "issue_details_dialog" )

        then: "Task details dialog should be displayed"
        taskDetailsDialog.waitForLoaded();

        and: "'Back' button should be present"
        taskDetailsDialog.isBackButtonDisplayed();
        and: "status of the issue should be 'Open'"
        taskDetailsDialog.getIssueStatus() == "Open"
        resetBrowser();
    }

    def "GIVEN existing user and an issue was assigned to him WHEN the user is logged in THEN 'Assigned to Me' button should be present in the toolbar"()
    {
        given: "existing assigned user"
        getTestSession().setUser( TEST_USER );

        when: "the user is 'logged in'"
        NavigatorHelper.openContentStudioApp( getTestSession() );
        saveScreenshot( "logged_home" + USER_NAME );

        then: "'Assigned to Me' button should be present in the toolbar"
        contentBrowsePanel.hasAssignedIssues();
    }

    def "GIVEN existing user and an issue was assigned to him WHEN Issue List dialog has been opened AND 'Assigned to Me' checkbox clicked THEN one issue should be listed on the dialog"()
    {
        given: "existing assigned user is logged in"
        getTestSession().setUser( TEST_USER );
        NavigatorHelper.openContentStudioApp( getTestSession() );

        when: "'Assigned to Me' has been checked"
        IssueListDialog issueListDialog = contentBrowsePanel.clickOnToolbarShowIssues();
        saveScreenshot( "assigned_issue" + USER_NAME );
        List<String> titles = issueListDialog.getIssueTitles();

        then: "one issue with expected name should be displayed"
        titles.size() == 1;
        and:
        titles.get( 0 ).contains( TEST_TASK.getTitle() );
    }

    def "GIVEN existing user and an issue was assigned to him AND 'Issue List' dialog is opened WHEN issue has been clicked THEN 'Issue details dialog' should be opened"()
    {
        given: "existing assigned user is logged in"
        getTestSession().setUser( TEST_USER );
        NavigatorHelper.openContentStudioApp( getTestSession() );
        and: "'Assigned to Me' has been checked"
        IssueListDialog issueListDialog = contentBrowsePanel.clickOnToolbarShowIssues();

        when: "the task has been clicked"
        IssueDetailsDialog detailsDialog = issueListDialog.clickOnIssue( TEST_TASK.getTitle() );
        saveScreenshot( "issue_clicked" );

        then: "Issue Details dialog should be loaded"
        detailsDialog.waitForLoaded();

        and: "expected creator should be present"
        detailsDialog.getOpenedBy().contains( "user:system:su" );
    }
}
