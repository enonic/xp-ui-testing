package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.SortContentDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.SortMenuItem
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Ignore
import spock.lang.Stepwise

@Stepwise
class DisplayNameSortContent_Spec
    extends BaseContentSpec
{

    @Ignore
    def "GIVEN sort content dialog opened WHEN the item with name 'DisplayName - Ascending' selected THEN content sorted correctly in the dialog-grid"()
    {
        given: "folder with contents selected and 'Sort' button clicked"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort().clickOnTabMenu().selectSortMenuItem(
            SortMenuItem.MODIFIED_DESCENDING.getValue() );
        List<String> defaultSortingList = sortContentDialog.getContentNames();
        TestUtils.saveScreenshot( getSession(), "dn1" );
        sortContentDialog.clickOnSaveButton();

        when:
        List<String> nameAscendingList = contentBrowsePanel.clickToolbarSort().clickOnTabMenu().selectSortMenuItem(
            SortMenuItem.DNAME_ASCENDING.getValue() ).getContentNames();
        TestUtils.saveScreenshot( getSession(), "dn2" );

        then: "'SortContent' dialog displayed"
        Collections.sort( defaultSortingList );
        defaultSortingList.equals( nameAscendingList );
    }

    @Ignore
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

    @Ignore
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

    @Ignore
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
