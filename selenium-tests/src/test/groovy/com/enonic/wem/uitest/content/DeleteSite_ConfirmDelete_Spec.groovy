package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ConfirmValueDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
class DeleteSite_ConfirmDelete_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    int INITIAL_NUMBER_TO_DELETE = 2;

    @Shared
    String INCORRECT_NUMBER_OF_CONTENT = "100";

    def "GIVEN existing site is selected WHEN 'Delete' button has been pressed AND 'Delete' in the dialog clicked THEN 'Confirm delete' dialog appears"()
    {
        given: "existing site is selected AND delete button pressed"
        SITE = buildSiteWithNameAndDispalyNameAndDescription( "site", "confirm delete", "description" );
        addContent( SITE );

        when: "'confirm delete site' dialog has been opened"
        ConfirmValueDialog confirmDialog = openConfirmDeleteDialog( SITE.getName() );
        saveScreenshot( "test_confirm_delete_site" );

        then:
        confirmDialog.isCancelButtonBottomDisplayed();
        and:
        confirmDialog.isCancelButtonTopDisplayed();

        and:
        !confirmDialog.isConfirmButtonButtonEnabled();

        and:
        confirmDialog.getNumberOfContentToDelete() == INITIAL_NUMBER_TO_DELETE;
    }

    def "GIVEN existing site WHEN 'Confirm delete' has been opened AND 'Cancel' button clicked  THEN modal dialog closed AND site listed in the grid"()
    {
        given: "existing site selected AND delete button pressed"
        ConfirmValueDialog confirmDialog = openConfirmDeleteDialog( SITE.getName() );

        when: "'Cancel' on the confirmation dialog has been clicked"
        confirmDialog.clickOnCancelBottomButton();
        saveScreenshot( "test_confirm_delete_dialog_cancel_clicked" );

        then: "modal dialog should be closed"
        confirmDialog.waitUntilDialogClosed( Application.EXPLICIT_NORMAL );

        and: "site should be listed in the grid"
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN existing site with child content WHEN 'Confirm delete' dialog has been opened THEN number of content to delete should be increased"()
    {
        given: "existing site selected AND delete button pressed"
        Content child = buildFolderContentWithParent( "child", "child content", SITE.getName() );
        findAndSelectContent( SITE.getName() );
        addContent( child );

        when: "'Confirm delete' has been opened"
        ConfirmValueDialog confirmDialog = openConfirmDeleteDialog( SITE.getName() );
        saveScreenshot( "test_number_to_delete_increased" );

        then: "number of content to delete should be increased"
        confirmDialog.getNumberOfContentToDelete() == INITIAL_NUMBER_TO_DELETE + 1;
    }

    def "GIVEN existing site with 2 children AND 'Confirm delete' has been opened WHEN incorrect number has been typed THEN 'Confirm' button should be disabled"()
    {
        given: "existing site selected AND delete button pressed"
        ConfirmValueDialog confirmContentDeleteDialog = openConfirmDeleteDialog( SITE.getName() );

        when: "incorrect number of resources typed"
        confirmContentDeleteDialog.typeNumber( INCORRECT_NUMBER_OF_CONTENT );
        saveScreenshot( "test_site_number_to_delete_incorrect" )

        then: "'confirm' button on the dialog disabled"
        !confirmContentDeleteDialog.isConfirmButtonButtonEnabled();
    }

    def "GIVEN existing site is selected AND 'Confirm delete' has been opened WHEN required number of content has been typed THEN 'Confirm' button gets enabled"()
    {
        given: "existing site selected AND delete button pressed"
        ConfirmValueDialog confirmContentDeleteDialog = openConfirmDeleteDialog( SITE.getName() );

        when: "required number of resources has been typed"
        confirmContentDeleteDialog.typeNumber( "3" );
        saveScreenshot( "test_site_number_to_delete_correct" );

        then: "'confirm' button gets enabled"
        confirmContentDeleteDialog.waitUntilConfirmButtonEnabled();
    }

    def "GIVEN existing site is selected AND 'Confirm delete' has been opened WHEN required number has been typed AND 'Confirm' pressed THEN site should be deleted"()
    {
        given: "existing site selected AND delete button pressed"
        ConfirmValueDialog confirmContentDeleteDialog = openConfirmDeleteDialog( SITE.getName() );

        when:
        confirmContentDeleteDialog.typeNumber( "3" );
        confirmContentDeleteDialog.clickOnConfirmButton();

        then:
        !contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN new site has been added then published WHEN the site has been selected and Delete pressed AND 'Delete' button has been pressed THEN site should be deleted"()
    {
        given: "new site has been added"
        Content site = buildSiteWithNameAndDispalyNameAndDescription( "site", "confirm delete online", "description" );
        addContent( site );

        and: "the site has been published"
        findAndSelectContent( site.getName() );
        contentBrowsePanel.showPublishMenu().clickOnMarkAsReadyMenuItem();
        sleep( 1000 );
        contentBrowsePanel.clickToolbarPublish().clickOnPublishNowButton();

        when: "site selected and Delete pressed AND 'Delete' button has been clicked"
        contentBrowsePanel.clickToolbarArchive().clickOnDeleteMenuItem(  );
        ConfirmValueDialog confirmDialog = new ConfirmValueDialog( getSession() );

        and: "required number of resources has been typed"
        confirmDialog.typeNumber( "2" ).clickOnConfirmButton();
        saveScreenshot( "test_confirm_delete_site_online" );

        then: "expected notification message should appears"
        contentBrowsePanel.waitExpectedNotificationMessage( "2 items are deleted.", 1 )

        and: "site should be deleted"
        !contentBrowsePanel.exists( site.getName() );
    }

    private ConfirmValueDialog openConfirmDeleteDialog( String siteName )
    {
        DeleteContentDialog deleteContentDialog = findAndSelectContent( siteName ).clickToolbarArchive();
        deleteContentDialog.waitForOpened();
        deleteContentDialog.clickOnDeleteMenuItem();
        return new ConfirmValueDialog( getSession() );
    }
}
