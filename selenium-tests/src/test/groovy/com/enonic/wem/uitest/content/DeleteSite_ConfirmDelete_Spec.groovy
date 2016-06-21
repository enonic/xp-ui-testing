package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ConfirmContentDeleteDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DeleteSite_ConfirmDelete_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    int INITIAL_NUMBER_TO_DELETE = 2;

    @Shared
    String INCORRECT_NUMBER_OF_CONTENT = "100";

    def "GIVEN existing site WHEN site selected AND 'Delete'-toolbar button pressed AND 'Delete' on the dialog clicked THEN 'Confirm delete' dialog appears"()
    {
        given: "existing site selected AND delete button pressed"
        SITE = buildSite( "site", "confirm delete", "description" );
        addContent( SITE );

        when:
        ConfirmContentDeleteDialog confirmDialog = openConfirmDeleteDialog( SITE.getName() );
        TestUtils.saveScreenshot( getSession(), "test_confirm_delete_site" );

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
        ConfirmContentDeleteDialog confirmDialog = openConfirmDeleteDialog( SITE.getName() );

        when: "'Cancel' on the confirmation dialog has been clicked"
        confirmDialog.clickOnCancelBottomButton();
        TestUtils.saveScreenshot( getSession(), "test_confirm_delete_dialog_cancel_clicked" );

        then: "modal dialog closed"
        confirmDialog.waitUntilDialogClosed( Application.EXPLICIT_NORMAL );

        and: "site listed in the grid"
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN existing site WHEN new child content to the site added AND 'Confirm delete' has been opened THEN number of content to delete increased"()
    {
        given: "existing site selected AND delete button pressed"
        Content child = buildFolderContentWithParent( "child", "child content", SITE.getName() );
        findAndSelectContent( SITE.getName() );
        addContent( child );

        when:
        ConfirmContentDeleteDialog confirmDialog = openConfirmDeleteDialog( SITE.getName() );
        TestUtils.saveScreenshot( getSession(), "test_number_to_delete_increased" );

        then:
        confirmDialog.getNumberOfContentToDelete() == INITIAL_NUMBER_TO_DELETE + 1;
    }

    def "GIVEN existing site with 2 children AND 'Confirm delete' has been opened WHEN incorrect number of content typed THEN 'Confirm' button is disabled"()
    {
        given: "existing site selected AND delete button pressed"
        ConfirmContentDeleteDialog confirmContentDeleteDialog = openConfirmDeleteDialog( SITE.getName() );

        when:
        confirmContentDeleteDialog.typeNumber( INCORRECT_NUMBER_OF_CONTENT );
        TestUtils.saveScreenshot( getSession(), "test_site_number_to_delete_incorrect" )

        then:
        !confirmContentDeleteDialog.isConfirmButtonButtonEnabled();
    }

    def "GIVEN existing site with 2 children AND 'Confirm delete' has been opened WHEN correct number of content typed THEN 'Confirm' button is disabled"()
    {
        given: "existing site selected AND delete button pressed"
        ConfirmContentDeleteDialog confirmContentDeleteDialog = openConfirmDeleteDialog( SITE.getName() );

        when:
        confirmContentDeleteDialog.typeNumber( "3" );
        TestUtils.saveScreenshot( getSession(), "test_site_number_to_delete_correct" );

        then:
        confirmContentDeleteDialog.waitUntilConfirmButtonEnabled();
    }

    def "GIVEN existing site with 2 children AND 'Confirm delete' has been opened WHEN correct number of content typed AND 'Confirm' pressed THEN site not listed"()
    {
        given: "existing site selected AND delete button pressed"
        ConfirmContentDeleteDialog confirmContentDeleteDialog = openConfirmDeleteDialog( SITE.getName() );

        when:
        confirmContentDeleteDialog.typeNumber( "3" );
        confirmContentDeleteDialog.clickOnConfirmButton();

        then:
        !contentBrowsePanel.exists( SITE.getName() );
    }

    private ConfirmContentDeleteDialog openConfirmDeleteDialog( String siteName )
    {
        DeleteContentDialog deleteContentDialog = findAndSelectContent( siteName ).clickToolbarDelete()
        deleteContentDialog.waitForOpened();
        deleteContentDialog.clickOnDeleteButton();
        return new ConfirmContentDeleteDialog( getSession() );
    }
}
