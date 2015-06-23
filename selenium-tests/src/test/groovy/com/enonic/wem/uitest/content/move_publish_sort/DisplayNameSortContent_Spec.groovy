package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.SortContentDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.SortMenuItem
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DisplayNameSortContent_Spec
    extends BaseContentSpec
{
    @Shared
    String IMPORTED_FOLDER_NAME = "all-content-types-images";


    def "GIVEN sort content dialog opened WHEN the item with name 'DisplayName - Ascending' selected THEN content sorted correctly in the dialog-grid"()
    {
        given: "folder with contents selected and 'Sort' button clicked"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();
        List<String> defaultSortingList = sortContentDialog.getContentNames();

        when:
        List<String> nameAscendingList = sortContentDialog.clickOnTabMenu().selectSortMenuItem(
            SortMenuItem.DNAME_ASCENDING.getValue() ).getContentNames();

        then: "'SortContent' dialog displayed"
        Collections.sort( defaultSortingList );
        defaultSortingList.equals( nameAscendingList );
    }


    def "GIVEN sort content dialog opened WHEN 'DisplayName - Descending' selected THEN content sorted correctly in the dialog-grid"()
    {
        given: "folder with contents selected and 'Sort' button clicked"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();
        List<String> defaultSortingList = sortContentDialog.getContentNames();

        when:
        List<String> nameDescendingList = sortContentDialog.clickOnTabMenu().selectSortMenuItem(
            SortMenuItem.DNAME_DESCENDING.getValue() ).getContentNames();

        then: "'SortContent' dialog displayed"
        Collections.sort( defaultSortingList, Collections.reverseOrder() );
        defaultSortingList.equals( nameDescendingList );
    }

    def "GIVEN sort content dialog opened WHEN 'DisplayName - Descending' selected and 'Save' button clicked THEN content sorted correctly in the dialog-grid"()
    {
        given: "folder with contents selected and 'Sort' button clicked"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();
        List<String> defaultSortingList = sortContentDialog.getContentNames();

        when: "order saved and dialog opened again"
        sortContentDialog.clickOnTabMenu().selectSortMenuItem( SortMenuItem.DNAME_DESCENDING.getValue() ).clickOnSaveButton();
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        sortContentDialog = contentBrowsePanel.clickToolbarSort();
        List<String> newList = sortContentDialog.getContentNames();

        then: "'SortContent' dialog displayed"
        Collections.sort( defaultSortingList, Collections.reverseOrder() );
        defaultSortingList.equals( newList );
        and: "'DisplayName - Descending' shown in the menu"
        sortContentDialog.getCurrentSortingName() == SortMenuItem.DNAME_DESCENDING.getValue();
    }

    def "GIVEN sort content opened WHEN 'DisplayName - Descending' selected and 'Save' button clicked THEN content sorted correctly in the browse panel"()
    {
        given: "folder with content selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        when: "ascending by display name item selected "
        List<String> contentsInDialog = sortContentDialog.clickOnTabMenu().selectSortMenuItem(
            SortMenuItem.DNAME_ASCENDING.getValue() ).getContentNames();
        sortContentDialog.clickOnSaveButton();
        contentBrowsePanel.expandContent( ContentPath.from( IMPORTED_FOLDER_NAME ) )

        then: "contents sorted in the browse panel the same order as in the dialog"
        List<String> names = contentBrowsePanel.getChildContentNamesFromBrowsePanel( IMPORTED_FOLDER_NAME );
        contentsInDialog.equals( names );
    }
}