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
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 7/26/2017.
 * */
@Stepwise
@Ignore
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

    def "setup: add an user in system provider"()
    {
        setup: "'Users' app is opened"
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );

        and: "build the new user"
        String[] roles = [RoleName.ADMIN_CONSOLE.getValue(), RoleName.CM_APP.getValue(), RoleName.CONTENT_MANAGER_ADMINISTRATOR.getValue()];
        TEST_USER = User.builder().displayName( USER_NAME ).email( USER_NAME + "@gmail.com" ).password( Application.MEDIUM_PASSWORD ).roles(
            roles.toList() ).build();
        and: "select the Users-folder"
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickOnRowByName( "users" ).clickOnToolbarNew( UserItemName.USERS_FOLDER );

        when: "data has been typed and user saved"
        userWizardPanel.typeData( TEST_USER ).save().close( TEST_USER.getDisplayName() );
        userBrowsePanel.getFilterPanel().typeSearchText( TEST_USER.getDisplayName() );

        then: "new user should be present in the system store"
        userBrowsePanel.exists( TEST_USER.getDisplayName(), true );
    }

    def "GIVEN existing user WHEN when new task has been assigned to him THEN new task with expected dependent items should be displayed"()
    {
        setup: "Content Studio is opened"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        filterPanel = contentBrowsePanel.getFilterPanel();

        and: "site has been added"
        SITE = buildSiteWithAllTypes();
        addReadyContent( SITE );

        and: "double content(not valid) has been added"
        DOUBLE_CONTENT = buildDouble1_1_Content( null );
        and: "child content has been added"
        findAndSelectContent( SITE.getName() );

        List<String> assigneesList = new ArrayList<>();
        assigneesList.add( TEST_USER.getName() );

        and: "create task dialog is opened and data has been typed"
        TEST_ISSUE = buildIssue( "description: issue to close", assigneesList, null );
        CreateIssueDialog createIssueDialog = contentBrowsePanel.showPublishMenu().clickOnCreateTaskMenuItem();
        createIssueDialog.typeData( TEST_ISSUE );
        and: "'Include Children' link has been pressed"
        createIssueDialog.clickOnIncludeChildrenToggler();

        when: "'Create' button has been pressed"
        createIssueDialog.clickOnCreateTaskButton();

        and: "Items- tab has been clicked"
        IssueDetailsDialog issueDetailsDialog = new IssueDetailsDialog( getSession() );
        issueDetailsDialog.clickOnItemsTabBarItem();
        sleep( 400 );
        saveScreenshot( "show_dep" );
        List<String> dependantsNames = issueDetailsDialog.getDependantNames();

        then: "the content should be present in the dependants list of the 'Issue Details Dialog'"
        dependantsNames.get( 0 ).contains( DOUBLE_CONTENT.getName() );
    }
}
