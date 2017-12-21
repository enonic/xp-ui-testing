package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.ConfirmContentDeleteDialog
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

/**
 * Created on 3/28/2017.
 * enonic/xp-ui-testing#13  Update Selenium tests for 'Delete Content Dialog'
 * */
class ContentBrowsePanel_DeleteParent_With_Child_Spec
    extends BaseContentSpec

{
    @Shared
    Content PARENT_FOLDER

    @Shared
    Content CHILD_FOLDER


    def "GIVEN existing parent folder with a child WHEN the parent is selected AND 'Delete' button has been pressed THEN confirmation dialog with input for number of contents to delete should be displayed"()
    {
        given: "existing parent folder"
        PARENT_FOLDER = buildFolderContent( "folder", "delete parent with child" );
        CHILD_FOLDER = buildFolderContent( "folder", "child content" );
        addContent( PARENT_FOLDER );
        findAndSelectContent( PARENT_FOLDER.getName() );
        and: "child folder has been added"
        addContent( CHILD_FOLDER );

        when: "Delete button has been pressed and deleting confirmed"
        contentBrowsePanel.clickToolbarDelete().clickOnDeleteButton();

        then: "confirmation dialog should be displayed"
        ConfirmContentDeleteDialog confirmContentDeleteDialog = new ConfirmContentDeleteDialog( getSession() );
        confirmContentDeleteDialog.isOpened();
    }

    def "GIVEN existing parent folder with a child WHEN ConfirmContentDeleteDialog is opened AND correct number typed THEN confirmation dialog with input for number of contents to delete should be displayed"()
    {
        given: "existing parent folder"
        findAndSelectContent( PARENT_FOLDER.getName() );

        when: "Delete button is pressed and it confirmed"
        contentBrowsePanel.clickToolbarDelete().clickOnDeleteButton();
        ConfirmContentDeleteDialog confirmContentDeleteDialog = new ConfirmContentDeleteDialog( getSession() );
        confirmContentDeleteDialog.typeNumber( "2" ).clickOnConfirmButton();
        saveScreenshot( "parent_deleted" );

        then: "the folder should not be listed"
        !contentBrowsePanel.exists( PARENT_FOLDER.getName() );

        and:"Delete button should be disabled"
        !contentBrowsePanel.isDeleteButtonEnabled(  );

        and:"Move button should be disabled"
        !contentBrowsePanel.isMoveButtonEnabled(  );

        and:"Publish button should be disabled"
        !contentBrowsePanel.isPublishButtonEnabled(  );;

    }
}
