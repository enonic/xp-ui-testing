package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.SortContentDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.SortMenuItem
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore

class ManualSortContent_Spec
    extends BaseContentSpec
{

    def "GIVEN sort dialog opened  WHEN two contents swapped by drag and drop THEN content sorted correctly in the dialog-grid and 'Manually Sorted' selected in the sort menu"()
    {
        given: "folder with contents selected and 'sort' dialog opened"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort().clickOnTabMenu().selectSortMenuItem(
            SortMenuItem.MODIFIED_DESCENDING.getValue() );
        LinkedList<String> defaultSortingList = sortContentDialog.getContentNames();
        TestUtils.saveScreenshot( getSession(), "manual_sort1" );

        when:
        sortContentDialog.dragAndSwapItems( "nord.jpg", "whale.jpg" )
        LinkedList<String> manuallySortedList = sortContentDialog.getContentNames();
        TestUtils.saveScreenshot( getSession(), "manual_sort-drag2" );

        then: "'SortContent' dialog displayed"
        defaultSortingList.indexOf( "nord.jpg" ) == manuallySortedList.indexOf( "whale.jpg" );
        and:
        sortContentDialog.getCurrentSortingName() == SortMenuItem.MANUALLY_SORTED.getValue();
    }
    //INBOX-95
    @Ignore
    def "GIVEN sort dialog opened and default sorting applied WHEN two contents swapped by drag and drop and 'Save' button pressed THEN content sorted correctly in the dialog-grid"()
    {
        given: "one selected content"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort().clickOnTabMenu().selectSortMenuItem(
            SortMenuItem.MODIFIED_DESCENDING.getValue() );
        LinkedList<String> defaultSortingList = sortContentDialog.getContentNames();

        when:
        sortContentDialog.dragAndSwapItems( "nord.jpg", "whale.jpg" );
        sleep( 1000 );
        sortContentDialog.clickOnSaveButton();
        sortContentDialog = contentBrowsePanel.clickToolbarSort();
        LinkedList<String> manuallySortedList = sortContentDialog.getContentNames();
        TestUtils.saveScreenshot( getSession(), "manual_sort-drag3" );

        then: "'SortContent' dialog displayed"
        defaultSortingList.indexOf( "nord.jpg" ) == manuallySortedList.indexOf( "whale.jpg" );
    }
}

