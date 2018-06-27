package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ConfirmContentDeleteDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DeleteSite_ConfirmDelete_Spec
        extends BaseContentSpec {
    @Shared
    Content SITE;

    @Shared
    int INITIAL_NUMBER_TO_DELETE = 2;

    @Shared
    String INCORRECT_NUMBER_OF_CONTENT = "100";

    def "GIVEN existing site WHEN site selected AND 'Delete'-toolbar button pressed AND 'Delete' on the dialog clicked THEN 'Confirm delete' dialog appears"() {
        given: "existing site is selected AND delete button pressed"
        SITE = buildSiteWithNameAndDispalyNameAndDescription("site", "confirm delete", "description");
        addContent(SITE);

        when: "'confirm delete site' dialog has been opened"
        ConfirmContentDeleteDialog confirmDialog = openConfirmDeleteDialog(SITE.getName());
        saveScreenshot("test_confirm_delete_site");

        then:
        confirmDialog.isCancelButtonBottomDisplayed();
        and:
        confirmDialog.isCancelButtonTopDisplayed();

        and:
        !confirmDialog.isConfirmButtonButtonEnabled();

        and:
        confirmDialog.getNumberOfContentToDelete() == INITIAL_NUMBER_TO_DELETE;
    }

    def "GIVEN existing site WHEN 'Confirm delete' has been opened AND 'Cancel' button clicked  THEN modal dialog closed AND site listed in the grid"() {
        given: "existing site selected AND delete button pressed"
        ConfirmContentDeleteDialog confirmDialog = openConfirmDeleteDialog(SITE.getName());

        when: "'Cancel' on the confirmation dialog has been clicked"
        confirmDialog.clickOnCancelBottomButton();
        saveScreenshot("test_confirm_delete_dialog_cancel_clicked");

        then: "modal dialog should be closed"
        confirmDialog.waitUntilDialogClosed(Application.EXPLICIT_NORMAL);

        and: "site should be listed in the grid"
        contentBrowsePanel.exists(SITE.getName());
    }

    def "GIVEN existing site WHEN new child content for the site has been added AND 'Confirm delete' has been opened THEN number of content to delete should be increased"() {
        given: "existing site selected AND delete button pressed"
        Content child = buildFolderContentWithParent("child", "child content", SITE.getName());
        findAndSelectContent(SITE.getName());
        addContent(child);

        when: "'Confirm delete' has been opened"
        ConfirmContentDeleteDialog confirmDialog = openConfirmDeleteDialog(SITE.getName());
        saveScreenshot("test_number_to_delete_increased");

        then: "number of content to delete should be increased"
        confirmDialog.getNumberOfContentToDelete() == INITIAL_NUMBER_TO_DELETE + 1;
    }

    def "GIVEN existing site with 2 children AND 'Confirm delete' has been opened WHEN incorrect number of content typed THEN 'Confirm' button is disabled"() {
        given: "existing site selected AND delete button pressed"
        ConfirmContentDeleteDialog confirmContentDeleteDialog = openConfirmDeleteDialog(SITE.getName());

        when: "incorrect number of resources typed"
        confirmContentDeleteDialog.typeNumber(INCORRECT_NUMBER_OF_CONTENT);
        saveScreenshot("test_site_number_to_delete_incorrect")

        then: "'confirm' button on the dialog disabled"
        !confirmContentDeleteDialog.isConfirmButtonButtonEnabled();
    }

    def "GIVEN existing site with 2 children AND 'Confirm delete' has been opened WHEN correct number of content typed THEN 'Confirm' button is disabled"() {
        given: "existing site selected AND delete button pressed"
        ConfirmContentDeleteDialog confirmContentDeleteDialog = openConfirmDeleteDialog(SITE.getName());

        when: "correct number of resources typed"
        confirmContentDeleteDialog.typeNumber("3");
        saveScreenshot("test_site_number_to_delete_correct");

        then: "'confirm' button on the dialog enabled"
        confirmContentDeleteDialog.waitUntilConfirmButtonEnabled();
    }

    def "GIVEN existing site with 2 children AND 'Confirm delete' has been opened WHEN correct number of content typed AND 'Confirm' pressed THEN site not listed"() {
        given: "existing site selected AND delete button pressed"
        ConfirmContentDeleteDialog confirmContentDeleteDialog = openConfirmDeleteDialog(SITE.getName());

        when:
        confirmContentDeleteDialog.typeNumber("3");
        confirmContentDeleteDialog.clickOnConfirmButton();

        then:
        !contentBrowsePanel.exists(SITE.getName());
    }
    // this test verifies the XP-3428
    def "GIVEN existing site with 'online' status WHEN site selected and Delete pressed AND 'Instantly delete' checkbox checked THEN site not listed"() {
        given: "existing site selected AND delete button pressed"
        Content onlineSite = buildSiteWithNameAndDispalyNameAndDescription("site", "confirm delete online", "description");
        addContent(onlineSite);
        findAndSelectContent(onlineSite.getName()).clickToolbarPublish().clickOnPublishNowButton();

        when: "site selected and Delete pressed AND 'Instantly delete' checkbox checked"
        contentBrowsePanel.clickToolbarDelete().clickOnInstantlyCheckbox().clickOnDeleteButton();
        ConfirmContentDeleteDialog confirmDialog = new ConfirmContentDeleteDialog(getSession());

        and: "correct number of resources was typed"
        confirmDialog.typeNumber("2").clickOnConfirmButton();
        saveScreenshot("test_confirm_delete_site_online");

        then: "correct notification message should appears"
        contentBrowsePanel.waitExpectedNotificationMessage("Item \"" + onlineSite.getName() + "\" is deleted.", 1)


        and: "site successfully deleted"
        !contentBrowsePanel.exists(onlineSite.getName());
    }

    private ConfirmContentDeleteDialog openConfirmDeleteDialog(String siteName) {
        DeleteContentDialog deleteContentDialog = findAndSelectContent(siteName).clickToolbarDelete()
        deleteContentDialog.waitForOpened();
        deleteContentDialog.clickOnDeleteButton();
        return new ConfirmContentDeleteDialog(getSession());
    }
}
