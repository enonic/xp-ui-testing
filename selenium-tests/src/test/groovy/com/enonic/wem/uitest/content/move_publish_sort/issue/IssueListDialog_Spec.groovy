package com.enonic.wem.uitest.content.move_publish_sort.issue

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
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
 * Created on 7/12/2017.
 * */
@Stepwise
class IssueListDialog_Spec
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

    def "GIVEN create issue dialog is opened WHEN data has been typed AND 'Create' button has been pressed THEN Issue Details dialog should be correctly displayed"()
    {
        setup: "Content Studio is opened"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        filterPanel = contentBrowsePanel.getFilterPanel();

        and: "folder has been added"
        CONTENT = buildFolderContent( "folder", "issue list dialog test" )
        addReadyContent( CONTENT );

        List<String> assigneesList = new ArrayList<>();
        assigneesList.add( TEST_USER.getName() );

        and: "create issue dialog is opened and data has been typed"
        TEST_ISSUE = buildIssue( "issue 1", assigneesList, null );
        CreateIssueDialog createIssueDialog = findAndSelectContent( CONTENT.getName() ).showPublishMenu().selectCreateIssueMenuItem();
        createIssueDialog.typeData( TEST_ISSUE );
        createIssueDialog.clickOnCreateIssueButton();
        String message = contentBrowsePanel.waitForNotificationMessage();
        saveScreenshot( "issue_created_issue_list" )

        when: "'Create' button has been pressed"
        IssueDetailsDialog issueDetailsDialog = new IssueDetailsDialog( getSession() );
        issueDetailsDialog.clickOnCancelButtonTop();

        then: "Issue details dialog should be closed"
        issueDetailsDialog.waitForClosed();

        and: "'New issue created successfully.' message should be displayed"
        message == Application.ISSUE_IS_CREATED_MESSAGE;
    }

    def "GIVEN issue is assigned to the user WHEN 'show Issues Dialog' toolbar-button has been pressed THEN Issues List dialog should appear"()
    {
        given: "Content Studio is opened"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );

        when:
        IssueListDialog issueListDialog = contentBrowsePanel.clickOnToolbarShowIssues();

        then: "Show Closed Issues button should be present"
        issueListDialog.isShowClosedIssuesButtonDisplayed();

        and: "'All' option should be selected by default"
        issueListDialog.getAssignedSelectedOption().contains( "All" );

        and: "'All issues' tab should be activate by default"
        issueListDialog.isAllIssuesTabActive(  );

        and: "'New Issue' button should be present"
        issueListDialog.clickOnIssuesTab(  ).isNewIssueButtonDisplayed();

        and: "just added issue should be present in the list"
        issueListDialog.isIssuePresent( TEST_ISSUE.getTitle() );
    }

    def "GIVEN Issue List dialog is opened WHEN Cancel-button-top has been pressed THEN the dialog should be closed"()
    {
        given: "Issue List dialog is opened"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        IssueListDialog issueListDialog = contentBrowsePanel.clickOnToolbarShowIssues();

        when: "Cancel button top has been clicked"
        issueListDialog.clickOnCancelButtonTop();

        then: "the dialog should be closed"
        issueListDialog.waitForClosed();
    }

    def "GIVEN existing user is logged in WHEN the user has opened 'Issue List' dialog AND clicked on the 'Assigned to Me' checkbox THEN one issue should be present in the list"()
    {
        given: "existing assigned user is logged in"
        getTestSession().setUser( TEST_USER );
        NavigatorHelper.openContentStudioApp( getTestSession() );
        IssueListDialog issueListDialog = contentBrowsePanel.clickOnToolbarShowIssues();

        when: "'Assigned to Me' checkbox has been checked"
        List<String> titles = issueListDialog.getIssueTitles();

        then: "one issue should be present in the list"
        titles.size() == 1;

        and: ""
        titles.get( 0 ).contains( TEST_ISSUE.getTitle() );

        and: "'Assigned to Me' option should be selected"
        issueListDialog.getAssignedSelectedOption().contains( "Assigned to Me" );
    }

    def "GIVEN existing user is logged in WHEN assigned issue has been clicked AND it has been published THEN the issue should be 'closed' AND 'closed'-tab should be opened"()
    {
        given: "existing user(assigned to the issue) is logged in"
        getTestSession().setUser( TEST_USER );
        NavigatorHelper.openContentStudioApp( getTestSession() );
        IssueListDialog issueListDialog = contentBrowsePanel.clickOnToolbarShowIssues();


        when: "the issue has been clicked"
        issueListDialog.clickOnIssue( TEST_ISSUE.getTitle() );
        IssueDetailsDialog details = new IssueDetailsDialog( getSession() );
        and: "'Items' button has been pressed"
        details.clickOnItemsTabBarItem();

        and: "Publish & Close issue button has been pressed"
        details.clickOnPublishButton();
        ContentPublishDialog contentPublishDialog = new ContentPublishDialog(getSession(  ));
        contentPublishDialog.clickOnPublishButton(  )
        saveScreenshot( "issue_published" );

        then: "Issue Details Dialog should be loaded"
        details.waitForOpened();

        and: "'Close Issue' button should be present"
        details.isCloseIssueButtonPresent();
        println "expected is:" + String.format( Application.ITEM_IS_PUBLISHED_NOTIFICATION_MESSAGE, TEST_ISSUE.getTitle() );
        //"Item is published" message should appear
        contentBrowsePanel.waitExpectedNotificationMessage(
            String.format( Application.ITEM_IS_PUBLISHED_NOTIFICATION_MESSAGE, CONTENT.getName() ), 1 );
    }
}
