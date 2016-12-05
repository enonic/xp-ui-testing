package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.ConfirmContentDeleteDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content

/**
 * Created  on 17.11.2016.
 *
 * Task:XP-4478 Add selenium tests to verify XP-4476 and INBOX-583
 *
 * */
class Delete_Duplicated_Moved_Content_Spec
    extends BaseContentSpec
{

    def "GIVEN existing site and its copy WHEN the original site was deleted THEN copy of the site should not be deleted"()
    {
        given: "existing site and its copy"
        String name = NameHelper.uniqueName( "site" );
        Content site = buildSiteWithNameAndDispalyNameAndDescription( name, "test-site", "delete duplicated content" );
        addSite( site );
        findAndSelectContent( site.getName() ).clickToolbarDuplicate();

        when: "the original site was deleted"
        ConfirmContentDeleteDialog confirmationDialog = openConfirmDeleteDialog( site.getName() );
        confirmationDialog.typeNumber( DEFAULT_NUMBER_OF_CONTENT_IN_SITE ).clickOnConfirmButton();
        saveScreenshot( "original_site_deleted" );
        filterPanel.clickOnCleanFilter();

        and: "name of copy of the site is typed"
        findAndSelectContent( site.getName() + "-copy" );

        then: "site-copy is exists and expander is displayed"
        contentBrowsePanel.isExpanderPresent( site.getName() + "-copy" );
    }

    def "GIVEN first-folder with a child AND the empty folder WHEN the child content moved to the empty folder AND first folder has been deleted THEN moved content should not be deleted"()
    {
        given: "folder with a child AND the empty folder"
        Content parentFolder = buildFolderContent( "parent", "parent folder" );
        addContent( parentFolder );
        Content childFolder = buildFolderContent( "child", "child folder" );
        findAndSelectContent( parentFolder.getName() );

        and: "parent folder with the child were added"
        addContent( childFolder );

        and: "one empty folder added"
        Content emptyFolder = buildFolderContent( "empty", "empty folder" );
        contentBrowsePanel.clickOnClearSelection();
        addContent( emptyFolder );

        when: "the child content moved to the empty folder"
        findAndSelectContent( childFolder.getName() ).clickToolbarMove().typeSearchText(
            emptyFolder.getName() ).selectDestinationAndClickOnMove( emptyFolder.getName() );
        contentBrowsePanel.clickOnClearSelection();

        and: "first folder has been deleted"
        findAndSelectContent( parentFolder.getName() ).clickToolbarDelete().doDelete();
        filterPanel.typeSearchText( childFolder.getName() );
        saveScreenshot( "moved_content_was_not_deleted" )

        then: "moved content should not be deleted"
        contentBrowsePanel.exists( childFolder.getName() );
    }

    private ConfirmContentDeleteDialog openConfirmDeleteDialog( String siteName )
    {
        DeleteContentDialog deleteContentDialog = findAndSelectContent( siteName ).clickToolbarDelete()
        deleteContentDialog.waitForOpened();
        deleteContentDialog.clickOnDeleteButton();
        return new ConfirmContentDeleteDialog( getSession() );
    }

}