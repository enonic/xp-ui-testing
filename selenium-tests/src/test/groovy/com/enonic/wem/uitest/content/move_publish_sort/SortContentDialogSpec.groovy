package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.SortContentDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.SortMenuItem
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec

class SortContentDialogSpec
    extends BaseContentSpec
{

    def "GIVEN Content BrowsePanel WHEN one content selected and 'Sort' button clicked THEN 'Sort Content' appears with correct control elements"()
    {
        given: "one selected content"
        findAndSelectContent( IMPORTED_FOLDER_NAME )

        when:
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        then: "'SortContent' dialog should be displayed"
        sortContentDialog.isDisplayed();
        and: "has a correct title"
        sortContentDialog.getTitle() == SortContentDialog.TITLE;
        and: "has 'save, close' buttons"
        sortContentDialog.isCancelButtonEnabled();
        and:
        sortContentDialog.isSaveButtonEnabled();
        and:
        sortContentDialog.isSortMenuButtonEnabled();
    }

    def "GIVEN sort dialog is opened WHEN 'Save' button was clicked THEN dialog should not be displayed"()
    {
        given: "one selected content"
        findAndSelectContent( IMPORTED_FOLDER_NAME )
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        when: "'Save' clicked"
        sortContentDialog.clickOnSaveButton();

        then:
        !sortContentDialog.isDisplayed();
    }

    def "GIVEN sort dialog is opened WHEN 'Cancel' button was clicked THEN dialog should not be displayed"()
    {
        given: "one selected content"
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

    def "WHEN sort dialog is opened THEN default sorting should be set"()
    {
        given: "folder added at root"
        Content folderContent = buildFolderContent( "folder", "sort_test" );
        addContent( folderContent );

        when: "content was selected and 'Sort' dialog is opened"
        findAndSelectContent( folderContent.getName() );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();
        saveScreenshot( "default_sorting" );

        then: "default sorting should be set for the dialog"
        sortContentDialog.getCurrentSortingName() == SortMenuItem.MODIFIED_DESCENDING.getValue();
    }

}
