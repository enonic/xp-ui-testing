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

    def "GIVEN existing parent folder is selected WHEN ConfirmContentDelete Dialog has been opened AND correct number of items has been typed THEN parent folder with children should be deleted"()
    {
        given: "parent folder has been created"
        PARENT_FOLDER = buildFolderContent( "folder", "parent folder" );
        CHILD_FOLDER = buildFolderContent( "folder", "child content" );
        addContent( PARENT_FOLDER );
        findAndSelectContent( PARENT_FOLDER.getName() );
        and: "child folder has been added"
        addContent( CHILD_FOLDER );

        when: "Delete button has been pressed and it confirmed"
        contentBrowsePanel.clickToolbarArchive().clickOnDeleteMenuItem();
        ConfirmValueDialog confirmContentDeleteDialog = new ConfirmValueDialog( getSession() );
        confirmContentDeleteDialog.typeNumber( "2" ).clickOnConfirmButton();
        saveScreenshot( "parent_deleted" );

        then: "the folder should not be listed"
        !contentBrowsePanel.exists( PARENT_FOLDER.getName() );

        and: "'Archive...' button gets disabled"
        !contentBrowsePanel.isArchiveButtonEnabled();

        and: "Move button gets disabled"
        !contentBrowsePanel.isMoveButtonEnabled();

        and: "Publish button gets disabled"
        !contentBrowsePanel.isPublishButtonEnabled(); ;
    }
}
