package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ConfirmContentDeleteDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
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

    def "GIVEN existing is selected WHEN 'Delete' button has been pressed AND 'Delete' on the dialog clicked THEN 'Confirm delete' dialog appears"()
    {
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

    def "GIVEN existing site with child content WHEN 'Confirm delete' dialog has been opened THEN number of content to delete should be increased"()
    {
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

    def "GIVEN existing site with 2 children AND 'Confirm delete' has been opened WHEN incorrect number has been typed THEN 'Confirm' button should be disabled"()
    {
        given: "existing site selected AND delete button pressed"
        ConfirmContentDeleteDialog confirmContentDeleteDialog = openConfirmDeleteDialog(SITE.getName());

        when: "incorrect number of resources typed"
        confirmContentDeleteDialog.typeNumber(INCORRECT_NUMBER_OF_CONTENT);
        saveScreenshot("test_site_number_to_delete_incorrect")

        then: "'confirm' button on the dialog disabled"
        !confirmContentDeleteDialog.isConfirmButtonButtonEnabled();
    }

    def "GIVEN existing site is selected AND 'Confirm delete' has been opened WHEN required number of content has been typed THEN 'Confirm' button gets enabled"()
    {
        given: "existing site selected AND delete button pressed"
        ConfirmContentDeleteDialog confirmContentDeleteDialog = openConfirmDeleteDialog(SITE.getName());

        when: "required number of resources has been typed"
        confirmContentDeleteDialog.typeNumber("3");
        saveScreenshot("test_site_number_to_delete_correct");

        then: "'confirm' button gets enabled"
        confirmContentDeleteDialog.waitUntilConfirmButtonEnabled();
    }

    def "GIVEN existing site is selected AND 'Confirm delete' has been opened WHEN required number has been typed AND 'Confirm' pressed THEN site should be deleted"()
    {
        given: "existing site selected AND delete button pressed"
        ConfirmContentDeleteDialog confirmContentDeleteDialog = openConfirmDeleteDialog(SITE.getName());

        when:
        confirmContentDeleteDialog.typeNumber("3");
        confirmContentDeleteDialog.clickOnConfirmButton();

        then:
        !contentBrowsePanel.exists(SITE.getName());
    }

    def "GIVEN new site has been added then published WHEN the site has been selected and Delete pressed AND 'Delete now' button has been pressed THEN site should be deleted"()
    {
        given: "new site has been added"
        Content site = buildSiteWithNameAndDispalyNameAndDescription("site", "confirm delete online", "description");
        addContent(site);

        and:"the site has been published"
        findAndSelectContent( site.getName() );
        contentBrowsePanel.showPublishMenu().clickOnMarkAsReadyMenuItem();
        contentBrowsePanel.clickToolbarPublish().clickOnPublishButton();

        when: "site selected and Delete pressed AND 'Delete now' button has been clicked"
        contentBrowsePanel.clickToolbarDelete().clickOnDeleteNowButton();
        ConfirmContentDeleteDialog confirmDialog = new ConfirmContentDeleteDialog(getSession());

        and: "required number of resources has been typed"
        confirmDialog.typeNumber("2").clickOnConfirmButton();
        saveScreenshot("test_confirm_delete_site_online");

        then: "expected notification message should appears"
        contentBrowsePanel.waitExpectedNotificationMessage("Item \"" + site.getName() + "\" is deleted.", 1)


        and: "site should be deleted"
        !contentBrowsePanel.exists(site.getName());
    }

    private ConfirmContentDeleteDialog openConfirmDeleteDialog(String siteName) {
        DeleteContentDialog deleteContentDialog = findAndSelectContent(siteName).clickToolbarDelete()
        deleteContentDialog.waitForOpened();
        deleteContentDialog.clickOnDeleteNowButton();
        return new ConfirmContentDeleteDialog(getSession());
    }
}
