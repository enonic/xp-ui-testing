package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.ConfirmValueDialog
import com.enonic.autotests.pages.contentmanager.DuplicateContentDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content

/**
 * Created  on 17.11.2016.
 *
 * */
class Delete_Duplicated_Moved_Content_Spec
    extends BaseContentSpec
{

    def "GIVEN existing site and its copy WHEN the original site has been deleted THEN duplicated site should not be deleted"()
    {
        given: "existing site and its copy"
        String name = NameHelper.uniqueName( "site" );
        Content site = buildSiteWithNameAndDispalyNameAndDescription( name, "test-site", "delete duplicated content" );
        addSite( site );
        findAndSelectContent( site.getName() ).clickToolbarDuplicate();
        DuplicateContentDialog dialog = new DuplicateContentDialog(getSession(  ));
        dialog.waitForOpened(  );
        dialog.clickOnDuplicateButton(  );
        dialog.waitForClosed(  );

        when: "the original site has been deleted"
        ConfirmValueDialog confirmationDialog = openConfirmDeleteDialog( site.getName() );
        confirmationDialog.typeNumber( DEFAULT_NUMBER_OF_CONTENT_IN_SITE ).clickOnConfirmButton();
        saveScreenshot( "original_site_deleted" );
        filterPanel.clickOnCleanFilter();

        and: "name of the duplicated site has been typed"
        findAndSelectContent( site.getName() + "-copy" );

        then: "duplicated site should not be deleted (expander-icon should be displayed)"
        contentBrowsePanel.isExpanderPresent( site.getName() + "-copy" );
    }

    def "GIVEN first-folder with a child AND the empty folder WHEN the child content moved to the empty folder AND first folder has been deleted THEN moved content should not be deleted"()
    {
        given: "parent folder has been added"
        Content parentFolder = buildFolderContent( "parent", "parent folder" );
        addContent( parentFolder );
        Content childFolder = buildFolderContent( "child", "child folder" );
        findAndSelectContent( parentFolder.getName() );

        and: "child folder has been added"
        addContent( childFolder );

        and: "one more empty folder has been added(in root)"
        Content emptyFolder = buildFolderContent( "empty", "empty folder" );
        contentBrowsePanel.doClearSelection();
        addContent( emptyFolder );

        when: "the child content moved to the empty folder"
        findAndSelectContent( childFolder.getName() ).clickToolbarMove().typeSearchText(
            emptyFolder.getName() ).selectDestinationAndClickOnMove( emptyFolder.getName() );
        contentBrowsePanel.doClearSelection();

        and: "the parent folder has been deleted"
        findAndSelectContent( parentFolder.getName() ).clickToolbarDelete().doDelete();
        filterPanel.typeSearchText( childFolder.getName() );
        saveScreenshot( "moved_content_was_not_deleted" )

        then: "moved content should not be deleted"
        contentBrowsePanel.exists( childFolder.getName() );
    }

    private ConfirmValueDialog openConfirmDeleteDialog( String siteName )
    {
        DeleteContentDialog deleteContentDialog = findAndSelectContent( siteName ).clickToolbarDelete()
        deleteContentDialog.waitForOpened();
        deleteContentDialog.clickOnDeleteNowButton();
        return new ConfirmValueDialog( getSession() );
    }
}
