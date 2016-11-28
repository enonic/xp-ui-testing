package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.SortContentDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.SortMenuItem
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Stepwise

@Stepwise
class DisplayNameSortContent_Spec
    extends BaseContentSpec
{

    def "GIVEN sort content dialog opened with default sorting WHEN 'DisplayName - Ascending' selected THEN content sorted correctly in the dialog-grid"()
    {
        given: "folder with contents selected and 'Sort' button clicked"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort().clickOnTabMenu().selectSortMenuItem(
            SortMenuItem.MODIFIED_DESCENDING.getValue() );
        List<String> defaultSortingList = sortContentDialog.getContentNames();
        TestUtils.saveScreenshot( getSession(), "modified_descending" );
        sortContentDialog.clickOnSaveButton();

        when:
        List<String> nameAscendingList = contentBrowsePanel.clickToolbarSort().clickOnTabMenu().selectSortMenuItem(
            SortMenuItem.DNAME_ASCENDING.getValue() ).getContentNames();
        TestUtils.saveScreenshot( getSession(), "dname_ascending" );

        then: "content sorted correctly in the dialog-grid"
        Collections.sort( defaultSortingList );
        defaultSortingList.equals( nameAscendingList );
    }

    def "GIVEN sort content dialog opened WHEN 'DisplayName - Descending' selected THEN content sorted correctly in the dialog-grid"()
    {
        given: "folder with contents selected and 'Sort' button clicked"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();
        List<String> defaultSortingList = sortContentDialog.getContentNames();

        when: "'DisplayName - Descending' selected "
        List<String> nameDescendingList = sortContentDialog.clickOnTabMenu().selectSortMenuItem(
            SortMenuItem.DNAME_DESCENDING.getValue() ).getContentNames();

        then: "content sorted correctly in the dialog-grid"
        Collections.sort( defaultSortingList, Collections.reverseOrder() );
        defaultSortingList.equals( nameDescendingList );
    }

    def "GIVEN sort content dialog opened WHEN 'DisplayName - Descending' selected and 'Save' button clicked THEN content sorted correctly in the dialog-grid"()
    {
        given: "folder with contents selected and 'Sort' button clicked"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();
        List<String> defaultSortingList = sortContentDialog.getContentNames();

        when: "DisplayName - Descending' selected and 'Save' button clicked"
        sortContentDialog.clickOnTabMenu().selectSortMenuItem( SortMenuItem.DNAME_DESCENDING.getValue() ).clickOnSaveButton();
        sortContentDialog = contentBrowsePanel.clickToolbarSort();
        List<String> newList = sortContentDialog.getContentNames();

        then: "content sorted correctly in the dialog-grid"
        Collections.sort( defaultSortingList, Collections.reverseOrder() );
        defaultSortingList.equals( newList );

        and: "'DisplayName - Descending' shown in the menu"
        sortContentDialog.getCurrentSortingName() == SortMenuItem.DNAME_DESCENDING.getValue();
    }

    def "GIVEN sort content opened WHEN 'DisplayName - Ascending' selected and 'Save' button clicked THEN content sorted correctly in the browse panel"()
    {
        given: "folder with content selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        when: "ascending by display name item selected "
        List<String> contentsInDialog = sortContentDialog.clickOnTabMenu().selectSortMenuItem(
            SortMenuItem.DNAME_ASCENDING.getValue() ).getContentNames();
        sortContentDialog.clickOnSaveButton();
        contentBrowsePanel.expandContent( ContentPath.from( IMPORTED_FOLDER_NAME ) );

        then: "contents sorted in the browse panel the same order as in the dialog"
        List<String> names = contentBrowsePanel.getChildContentDisplayNamesFromTreeGrid( IMAGES_FOLDER_DISPLAY_NAME );
        contentsInDialog.equals( names );
    }
}
