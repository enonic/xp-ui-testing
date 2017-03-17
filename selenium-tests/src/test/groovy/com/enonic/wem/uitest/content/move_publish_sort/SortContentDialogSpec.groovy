package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.SortContentDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.SortMenuItem
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

class SortContentDialogSpec
    extends BaseContentSpec
{

    @Shared
    Content PARENT_FOLDER;

    def "GIVEN existing folder with children is selected WHEN 'Sort' button has been clicked THEN 'Sort Content' appears with correct control elements"()
    {
        given: "existing folder with children is selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME )

        when:"'Sort' button has been clicked"
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        then: "'SortContent' dialog should be displayed"
        sortContentDialog.isDisplayed();
        and: "dialog has a correct title"
        sortContentDialog.getTitle() == SortContentDialog.TITLE;
        and: "has 'save, close' buttons should be displayed"
        sortContentDialog.isCancelButtonEnabled();
        and:
        sortContentDialog.isSaveButtonEnabled();
        and:
        sortContentDialog.isSortMenuButtonEnabled();
    }

    def "GIVEN sort dialog is opened WHEN 'Save' button was clicked THEN dialog should not be displayed"()
    {
        given: "sort dialog is opened"
        findAndSelectContent( IMPORTED_FOLDER_NAME )
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        when: "'Save' has been clicked"
        sortContentDialog.clickOnSaveButton();

        then:"the dialog should not be displayed"
        !sortContentDialog.isDisplayed();
    }

    def "GIVEN sort dialog is opened WHEN 'Cancel' button was clicked THEN dialog should not be displayed"()
    {
        given: "sort dialog is opened"
        findAndSelectContent( IMPORTED_FOLDER_NAME )
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        when: "'Cancel' button was clicked"
        sortContentDialog.clickOnCancelButton();

        then: "dialog should not be displayed"
        !sortContentDialog.isDisplayed();
    }

    def "GIVEN sort dialog is opened WHEN 'Cancel' on top button was clicked THEN dialog should not be displayed"()
    {
        given: "sort dialog is opened"
        findAndSelectContent( IMPORTED_FOLDER_NAME )
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        when: "'Cancel' on top was clicked"
        sortContentDialog.clickOnCancelOnTop();

        then: "dialog should not be displayed"
        !sortContentDialog.isDisplayed();
    }

    def "GIVEN folder was selected and 'Sort' button clicked WHEN TabMenuButton on the Sort dialog was clicked THEN five menu items should be displayed"()
    {
        given: "sort dialog is opened"
        findAndSelectContent( IMPORTED_FOLDER_NAME )
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        when: "TabMenuButton on the Sort dialog was clicked"
        sortContentDialog.clickOnTabMenu();
        List<String> items = sortContentDialog.getMenuItems();

        then: "five menu items should be displayed"
        items.size() == 5;
        and:
        items.contains( SortMenuItem.DNAME_ASCENDING.getValue() );
        and:
        items.contains( SortMenuItem.DNAME_DESCENDING.getValue() );
        and:
        items.contains( SortMenuItem.MODIFIED_ASCENDING.getValue() );
        and:
        items.contains( SortMenuItem.MODIFIED_DESCENDING.getValue() );
        and:
        items.contains( SortMenuItem.MANUALLY_SORTED.getValue() );
    }

    def "GIVEN sort dialog is opened WHEN 'cancel' button on the top was clicked THEN dialog should not be displayed"()
    {
        given: "content selected and 'Sort' pressed"
        findAndSelectContent( IMPORTED_FOLDER_NAME )
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        when: "'Cancel' on top was clicked"
        sortContentDialog.clickOnCancelOnTop();

        then: "dialog should not be displayed"
        !sortContentDialog.isDisplayed();
    }

    def "GIVEN existing folder without children WHEN the folder has been selected  THEN 'Sort' button should be disabled"()
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

    def "GIVEN parent folder is selected WHEN sort dialog is opened THEN default sorting should be present on the dialog"()
    {
        given: "the parent folder is selected"
        findAndSelectContent( PARENT_FOLDER.getName(  ) );
        Content childFolder = buildFolderContent( "child-folder", "child folder" );

        and:"child content has been added"
        addContent( childFolder );
        contentBrowsePanel.doClearSelection(  );

        when: "parent folder has been selected and 'Sort' dialog is opened"
        findAndSelectContent( PARENT_FOLDER.getName() );
        and:"and 'Sort' dialog is opened"
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();
        saveScreenshot( "default_sorting" );

        then: "default sorting should be present on the dialog"
        sortContentDialog.getCurrentSortingName() == SortMenuItem.MODIFIED_DESCENDING.getValue();
    }

}
