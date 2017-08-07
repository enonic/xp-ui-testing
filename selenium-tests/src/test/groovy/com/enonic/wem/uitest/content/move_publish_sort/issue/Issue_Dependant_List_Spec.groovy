package com.enonic.wem.uitest.content.move_publish_sort.issue

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.issue.CreateIssueDialog
import com.enonic.autotests.pages.contentmanager.issue.IssueDetailsDialog
import com.enonic.autotests.pages.contentmanager.issue.UpdateIssueDialog
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
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
 * Created on 7/26/2017.
 *
 * Tasks:
 * xp-ui-testing#73  Add Selenium tests for an Issue with dependants-list
 * xp-ui-testing#68 Add Selenium tests for an issue without an item(deleted)
 *
 * verifies:
 *  Issue Dialog - handle removed assignee #5391
 *  List of dependant items is not refreshed after deletion in the Issue Dialog #5372
 * */
@Stepwise
class Issue_Dependant_List_Spec
    extends BaseIssueSpec
{
    @Shared
    User TEST_USER;

    @Shared
    private String USER_NAME = NameHelper.uniqueName( "user" );

    @Shared
    Content SITE;

    @Shared
    Content DOUBLE_CONTENT;

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
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

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

        and: "site has been added"
        SITE = buildSiteWithAllTypes();
        addContent( SITE );

        and: "double content(not valid) has been added"
        DOUBLE_CONTENT = buildDouble1_1_Content( null );
        findAndSelectContent( SITE.getName() );
        addContent( DOUBLE_CONTENT );

        List<String> assigneesList = new ArrayList<>();
        assigneesList.add( TEST_USER.getName() );

        and: "create issue dialog is opened and data has been typed"
        TEST_ISSUE = buildIssue( "issue to close", assigneesList, null );
        CreateIssueDialog createIssueDialog = contentBrowsePanel.showPublishMenu().selectCreateIssueMenuItem();
        createIssueDialog.typeData( TEST_ISSUE );
        and: "'Include Children' link has been pressed"
        createIssueDialog.clickOnIncludeChildrenToggler()

        when: "'Create' button has been pressed"
        createIssueDialog.clickOnCreateIssueButton();
        IssueDetailsDialog issueDetailsDialog = new IssueDetailsDialog( getSession() );
        List<String> dependantsNames = issueDetailsDialog.getDependantNames();

        then: "the content should be present in the dependants list of the 'Details Dialog'"
        dependantsNames.get( 0 ).contains( DOUBLE_CONTENT.getName() );
    }

    def "GIVEN existing issue AND 'Update issue' dialog is opened WHEN one item has been removed from the dependants list AND Cancel button pressed THEN the changes should not be saved"()
    {
        setup: "existing issue"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        IssueDetailsDialog issueDetailsDialog = contentBrowsePanel.clickOnToolbarShowIssues().clickOnIssue( TEST_ISSUE.getTitle() );
        and: "'Update issue' dialog is opened"
        UpdateIssueDialog updateIssueDialog = issueDetailsDialog.clickOnEditButton();

        when: "one item has been removed from the dependants list"
        updateIssueDialog.removeDependantItem( DOUBLE_CONTENT.getName() );

        and: "Cancel button on the UpdateIssue dialog has been pressed"
        updateIssueDialog.clickOnCancelBottomButton();
        List<String> dependantsNames = issueDetailsDialog.getDependantNames();

        then: "the removed content should be present in the dependants list of the 'Details Dialog', because 'Cancel' button was pressed"
        dependantsNames.get( 0 ).contains( DOUBLE_CONTENT.getName() );
    }

    //verifies: List of dependant items is not refreshed after deletion in the Issue Dialog #5372
    def "GIVEN existing issue AND one item was removed on the 'UpdateIssue dialog' and it did not saved WHEN UpdateIssue dialog has been opened THEN removed content should be present in the dependants"()
    {
        given: "existing issue"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        IssueDetailsDialog issueDetailsDialog = contentBrowsePanel.clickOnToolbarShowIssues().clickOnIssue( TEST_ISSUE.getTitle() );
        UpdateIssueDialog updateIssueDialog = issueDetailsDialog.clickOnEditButton();
        updateIssueDialog.removeDependantItem( DOUBLE_CONTENT.getName() );

        and: "'Cancel' button on the 'UpdateIssue dialog' has been pressed and the dialog closed"
        updateIssueDialog.clickOnCancelBottomButton();

        when: "'Update issue' dialog has been opened"
        issueDetailsDialog.clickOnEditButton();
        saveScreenshot( "update_issue_dlg_removed_item_canceled" )

        then: "the number of dependants should not be changed, because the changes were not saved"
        updateIssueDialog.getDependantNames().size() == 2;

        and: "the removed content should be present in the dependants list of the 'UpdateIssue Dialog', because 'Cancel' button was pressed"
        updateIssueDialog.getDependantNames().get( 0 ).contains( DOUBLE_CONTENT.getName() );
    }

    def "GIVEN existing issue AND one item was removed on the 'UpdateIssue dialog' and Save button has been pressed WHEN UpdateIssue dialog has been opened THEN removed content should be present in the dependants"()
    {
        given: "existing issue"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        IssueDetailsDialog issueDetailsDialog = contentBrowsePanel.clickOnToolbarShowIssues().clickOnIssue( TEST_ISSUE.getTitle() );
        UpdateIssueDialog updateIssueDialog = issueDetailsDialog.clickOnEditButton();
        updateIssueDialog.removeDependantItem( DOUBLE_CONTENT.getName() );

        and: "'Save' button on the 'UpdateIssue dialog' has been pressed and the dialog closed"
        updateIssueDialog.clickOnSaveIssueButton();

        when: "'Update issue' dialog has been opened again"
        issueDetailsDialog.clickOnEditButton();
        saveScreenshot( "update_issue_dlg_removed_item_saved" )

        then: "the number of dependants should be changed, because the changes were saved"
        updateIssueDialog.getDependantNames().size() == 1;

        and: "the removed content should not be present in the 'dependants list' of the 'UpdateIssue Dialog', because 'Save' button was pressed"
        !updateIssueDialog.getDependantNames().get( 0 ).contains( DOUBLE_CONTENT.getName() );
    }

    def "GIVEN existing issue and the item to publish has been deleted WHEN 'UpdateIssue' dialog has been opened THEN no any items to publish should be on the dialog"()
    {
        given: "existing issue and the item to publish has been deleted"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        openConfirmDeleteDialog( SITE.getName() ).typeNumber( "3" ).clickOnConfirmButton();

        when: "'UpdateIssue' dialog has been opened"
        IssueDetailsDialog issueDetailsDialog = contentBrowsePanel.clickOnToolbarShowIssues().clickOnIssue( TEST_ISSUE.getTitle() );
        UpdateIssueDialog updateIssueDialog = issueDetailsDialog.clickOnEditButton();
        saveScreenshot( "item_to_publish_was removed" );

        then: "no any items to publish should be on the dialog"
        updateIssueDialog.getItemNames().size() == 0;

        and: "'One or more items from the issue cannot be found' - notification should be displayed"
        contentBrowsePanel.waitExpectedNotificationMessage( Application.ISSUE_ITEM_DELETED, 1 );
    }
}
