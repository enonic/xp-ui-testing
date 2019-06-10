package com.enonic.wem.uitest.content.move_publish_sort.issue

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.issue.CreateIssueDialog
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

/**
 * Created on 7/6/2017.
 *
 * */
class CreateIssueDialog_Spec
    extends BaseContentSpec
{
    @Shared
    Content CONTENT;

    def "GIVEN existing folder WHEN the folder has been published THEN 'Online' status should be displayed"()
    {
        given: "existing folder"
        CONTENT = buildFolderContent( "folder", "create issue dialog test" )
        addContent( CONTENT );

        when: "the folder has been published"
        CreateIssueDialog dialog = findAndSelectContent( CONTENT.getName() ).showPublishMenu().selectCreateIssueMenuItem();
        saveScreenshot( "create_issue_dialog" );

        then: "Input for a title should be displayed"
        dialog.isTitleInputDisplayed();

        and: "Cancel-bottom button should be displayed"
        dialog.isCancelButtonBottomDisplayed();

        and: "Cancel-top button should be displayed"
        dialog.isCancelTopButtonDisplayed();

        and: "'Create Issue' button should be displayed"
        dialog.isCreateIssueButtonDisplayed();

        and: "Description-html area should be displayed"
        dialog.isDescriptionTextAreaDisplayed();

        and: "Assignees option filter should be displayed"
        dialog.isAssigneesOptionFilterDisplayed();

        and: "Items options filter should be displayed"
        dialog.isItemsOptionFilterDisplayed();

        and: "correct display name should be presented in the 'Item List'"
        dialog.getDisplayNameOfSelectedItems().get( 0 ) == CONTENT.getDisplayName();
    }

    def "GIVEN Create Issue dialog is opened WHEN Required inputs are empty and Create Issue button has been pressed THEN validation messages should be displayed"()
    {
        given: "Create Issue dialog is opened"
        CreateIssueDialog dialog = findAndSelectContent( CONTENT.getName() ).showPublishMenu().selectCreateIssueMenuItem();

        when: "Required inputs are empty and Create Issue button has been pressed"
        dialog.clickOnCreateIssueButton();

        then: "correct notification message should be present"
        dialog.getValidationMessageForTitleInput() == Application.REQUIRED_MESSAGE;
    }

    def "GIVEN Create Issue dialog is opened WHEN 'Cancel' bottom-button has been pressed THEN the dialog should be closed"()
    {
        given:
        CreateIssueDialog dialog = findAndSelectContent( CONTENT.getName() ).showPublishMenu().selectCreateIssueMenuItem();
        when: "'Cancel Bottom' button has been pressed"
        dialog.clickOnCancelBottomButton();

        then: "dialog should be closed"
        dialog.waitForClosed();
    }

    def "GIVEN Create Issue dialog is opened WHEN 'Cancel' bottom-top has been pressed THEN the dialog should be closed"()
    {
        given:
        CreateIssueDialog dialog = findAndSelectContent( CONTENT.getName() ).showPublishMenu().selectCreateIssueMenuItem();
        when: "'Cancel Top' button has been pressed"
        dialog.clickOnCancelTopButton();

        then: "dialog should be closed"
        dialog.waitForClosed();
    }
}