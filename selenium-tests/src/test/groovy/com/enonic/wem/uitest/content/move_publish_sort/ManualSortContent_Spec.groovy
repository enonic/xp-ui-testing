package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.SortContentDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.SortMenuItem
import com.enonic.autotests.pages.contentmanager.browsepanel.SortOrder
import com.enonic.wem.uitest.content.BaseContentSpec

class ManualSortContent_Spec
    extends BaseContentSpec
{

    def "GIVEN sort dialog is opened WHEN two contents has been swapped (by drag and drop) THEN contents should be correctly sorted in the dialog-grid and 'Manually Sorted' displayed in the sort menu"()
    {
        given: "folder with contents selected and 'sort' dialog opened"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort().clickOnTabMenu().doSort(
            SortMenuItem.MODIFIED_DATE.getValue() ,SortOrder.DESCENDING);
        LinkedList<String> defaultSortingList = sortContentDialog.getContentNames();
        saveScreenshot( "manual_sort_before" );

        when: "two contents has been swapped"
        sortContentDialog.dragAndSwapItems( "renault.jpg", "cape.jpg" )
        LinkedList<String> manuallySortedList = sortContentDialog.getContentNames();
        saveScreenshot( "manual_sort_swapped" );

        then: "contents should be correctly sorted in the dialog-grid"
        defaultSortingList.indexOf( "renault.jpg" ) == manuallySortedList.indexOf( "cape.jpg" );
        and: "'Manually Sorted' should be current sorting name"
        sortContentDialog.getCurrentSortingName() == SortMenuItem.MANUALLY_SORTED.getValue();
    }

    def "GIVEN sort dialog opened and default sorting applied WHEN two contents swapped by drag and drop and 'Save' button pressed THEN content sorted correctly in the dialog-grid"()
    {
        given: "one selected content"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort().clickOnTabMenu().doSort(
            SortMenuItem.MODIFIED_DATE.getValue() ,SortOrder.DESCENDING);
        LinkedList<String> defaultSortingList = sortContentDialog.getContentNames();
        sleep(1000);

        when:
        sortContentDialog.dragAndSwapItems( "nord.jpg", "whale.jpg" );
        sleep( 1000 );
        sortContentDialog.clickOnSaveButton();
        sortContentDialog = contentBrowsePanel.clickToolbarSort();
        LinkedList<String> manuallySortedList = sortContentDialog.getContentNames();
        saveScreenshot( "manual_sort-drag3" );

        then: "'SortContent' dialog displayed"
        defaultSortingList.indexOf( "nord.jpg" ) == manuallySortedList.indexOf( "whale.jpg" );
    }
}

