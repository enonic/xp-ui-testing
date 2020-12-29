package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.ConfirmValueDialog
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

/**
 * Created on 3/28/2017.
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
        given: "parent folder has been created"
        PARENT_FOLDER = buildFolderContent( "folder", "delete parent with child" );
        CHILD_FOLDER = buildFolderContent( "folder", "child content" );
        addContent( PARENT_FOLDER );
        findAndSelectContent( PARENT_FOLDER.getName() );
        and: "child folder has been added"
        addContent( CHILD_FOLDER );

        when: "Delete button has been pressed and deleting confirmed"
        contentBrowsePanel.clickToolbarDelete().clickOnDeleteNowButton();

        then: "confirmation dialog should be displayed"
        ConfirmValueDialog confirmContentDeleteDialog = new ConfirmValueDialog( getSession() );
        confirmContentDeleteDialog.isOpened();
    }

    def "GIVEN existing parent folder is selected WHEN ConfirmContentDelete Dialog has been opened AND correct number typed THEN parent folder with children should be deleted"()
    {
        given: "existing parent folder is selected"
        findAndSelectContent( PARENT_FOLDER.getName() );

        when: "Delete button has been pressed and it confirmed"
        contentBrowsePanel.clickToolbarDelete().clickOnDeleteNowButton();
        ConfirmValueDialog confirmContentDeleteDialog = new ConfirmValueDialog( getSession() );
        confirmContentDeleteDialog.typeNumber( "2" ).clickOnConfirmButton();
        saveScreenshot( "parent_deleted" );

        then: "the folder should not be listed"
        !contentBrowsePanel.exists( PARENT_FOLDER.getName() );

        and: "Delete button should be disabled"
        !contentBrowsePanel.isDeleteButtonEnabled();

        and: "Move button should be disabled"
        !contentBrowsePanel.isMoveButtonEnabled();

        and: "Publish button should be disabled"
        !contentBrowsePanel.isPublishButtonEnabled(); ;
    }
}
