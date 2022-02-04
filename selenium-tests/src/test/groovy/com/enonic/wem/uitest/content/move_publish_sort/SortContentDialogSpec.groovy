package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.SortContentDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.SortMenuItem
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore
import spock.lang.Shared

@Ignore
class SortContentDialogSpec
    extends BaseContentSpec
{

    @Shared
    Content PARENT_FOLDER;

    def "GIVEN existing folder with children is selected WHEN 'Sort' button has been clicked THEN 'Sort Content' appears with expected control elements"()
    {
        given: "existing folder with children is selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME )

        when: "'Sort' button has been clicked"
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        then: "'SortContent' dialog should be displayed"
        sortContentDialog.isDisplayed();
        and: "dialog has a correct title"
        sortContentDialog.getTitle() == SortContentDialog.TITLE;
        and: "has 'save, close' buttons should be displayed"
        sortContentDialog.isCancelButtonEnabled();
        and:
        !sortContentDialog.isSaveButtonEnabled();
        and:
        sortContentDialog.isSortMenuButtonEnabled();
    }

    def "GIVEN sort dialog is opened WHEN 'Cancel' button has been clicked THEN the modal dialog should be closed"()
    {
        given: "sort dialog is opened"
        findAndSelectContent( IMPORTED_FOLDER_NAME )
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        when: "'Cancel' button has been clicked"
        sortContentDialog.clickOnCancelButton();

        then: "the modal dialog should be closed"
        !sortContentDialog.isDisplayed();
    }

    def "GIVEN sort dialog is opened WHEN 'Cancel' on top button has been clicked THEN the modal dialog should be closed"()
    {
        given: "sort dialog is opened"
        findAndSelectContent( IMPORTED_FOLDER_NAME )
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        when: "'Cancel' -top button has been clicked"
        sortContentDialog.clickOnCancelOnTop();

        then: "the modal dialog should be closed"
        !sortContentDialog.isDisplayed();
    }

    def "GIVEN folder was selected and 'Sort' button clicked WHEN TabMenuButton has been clicked THEN five menu items get visible"()
    {
        given: "sort dialog is opened"
        findAndSelectContent( IMPORTED_FOLDER_NAME )
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        when: "TabMenuButton has been clicked"
        sortContentDialog.clickOnTabMenu();
        List<String> items = sortContentDialog.getMenuItems();

        then: "five menu items should be displayed"
        items.size() == 5;
        and:
        items.contains( SortMenuItem.DISPLAY_NAME.getValue() );
        and:
        items.contains( SortMenuItem.MODIFIED_DATE.getValue() );
        and:
        items.contains( SortMenuItem.PUBLISHED_DATE.getValue() );
        and:
        items.contains( SortMenuItem.MANUALLY_SORTED.getValue() );
        and:
        items.contains( SortMenuItem.CREATED_DATE.getValue() );
    }

    def "GIVEN sort dialog is opened WHEN 'cancel'- top button has been clicked THEN dialog should be closed"()
    {
        given: "content selected and 'Sort' pressed"
        findAndSelectContent( IMPORTED_FOLDER_NAME )
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        when: "'Cancel'- top has been clicked"
        sortContentDialog.clickOnCancelOnTop();

        then: "dialog should be closed"
        !sortContentDialog.isDisplayed();
    }

    def "GIVEN existing folder without children WHEN the folder has been selected  THEN 'Sort'button should be disabled in the browse toolbar"()
    {
        given: "existing folder without children"
        PARENT_FOLDER = buildFolderContent( "folder", "sort-test" );
        addContent( PARENT_FOLDER );

        when: "content was selected and 'Sort' dialog is opened"
        findAndSelectContent( PARENT_FOLDER.getName() );

        then: "'Sort' button should be disabled"
        !contentBrowsePanel.isSortButtonEnabled()
        saveScreenshot( "default_sorting" );
    }

    def "GIVEN parent folder is selected WHEN sort dialog is opened THEN default sorting should be present in the dialog"()
    {
        given: "the parent folder is selected"
        findAndSelectContent( PARENT_FOLDER.getName() );
        Content childFolder = buildFolderContent( "child-folder", "child folder" );

        and: "child content has been added"
        addContent( childFolder );

        when: "parent folder is selected and 'Sort' dialog has been opened"
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();
        saveScreenshot( "sort_child_folder_deleted" );

        then: "default sorting should be present in the dialog"
        sortContentDialog.getCurrentSortingName() == SortMenuItem.MODIFIED_DATE.getValue();
    }

}
