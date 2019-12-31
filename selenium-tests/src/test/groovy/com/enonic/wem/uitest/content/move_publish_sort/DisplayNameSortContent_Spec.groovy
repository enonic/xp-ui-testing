package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.SortContentDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.SortMenuItem
import com.enonic.autotests.pages.contentmanager.browsepanel.SortOrder
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Stepwise

@Stepwise
class DisplayNameSortContent_Spec
        extends BaseContentSpec {

    def "GIVEN sort content dialog is opened (default sorting) WHEN 'DisplayName - Ascending order' has been selected THEN content in the folder should be sorted correctly"() {
        given: "sort dialog is opened:"
        findAndSelectContent(IMPORTED_FOLDER_NAME);
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort().clickOnTabMenu().doSort(
                SortMenuItem.DISPLAY_NAME.getValue(), SortOrder.ASCENDING);
        List<String> defaultSortingList = sortContentDialog.getContentNames();
        saveScreenshot("modified_descending");
        sortContentDialog.clickOnSaveButton();

        when: "'DisplayName - Ascending' has been selected"
        List<String> nameAscendingList = contentBrowsePanel.clickToolbarSort().clickOnTabMenu().doSort(
                SortMenuItem.DISPLAY_NAME.getValue(), SortOrder.ASCENDING).getContentNames();
        saveScreenshot("dname_ascending");

        then: "content in the dialog should be correctly sorted"
        Collections.sort(defaultSortingList);
        defaultSortingList.equals(nameAscendingList);
    }

    def "GIVEN sort content dialog is opened WHEN 'DisplayName - Descending' has been selected THEN items should be correctly sorted in the dialog-grid"() {

        given: "folder with contents is selected and 'Sort' button clicked"
        findAndSelectContent(IMPORTED_FOLDER_NAME);
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();
        List<String> defaultSortingList = sortContentDialog.getContentNames();

        when: "'DisplayName - Descending' has been selected "
        List<String> nameDescendingList = sortContentDialog.clickOnTabMenu().doSort(
                SortMenuItem.DISPLAY_NAME.getValue(), SortOrder.DESCENDING).getContentNames();

        then: "content in the dialog should be correctly sorted "
        Collections.sort(defaultSortingList, Collections.reverseOrder());
        defaultSortingList.equals(nameDescendingList);
    }

    def "GIVEN sort content dialog is opened WHEN 'DisplayName - Descending' has been selected and 'Save' button clicked THEN items in the folder should be correctly sorted"() {
        given: "folder with contents selected and 'Sort' button clicked"
        findAndSelectContent(IMPORTED_FOLDER_NAME);
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();
        List<String> defaultSortingList = sortContentDialog.getContentNames();

        when: "DisplayName - Descending' selected and 'Save' button clicked"
        sortContentDialog.clickOnTabMenu().doSort(SortMenuItem.DISPLAY_NAME.getValue(),SortOrder.DESCENDING).clickOnSaveButton();
        sortContentDialog = contentBrowsePanel.clickToolbarSort();
        List<String> newList = sortContentDialog.getContentNames();

        then: "content sorted correctly in the dialog-grid"
        Collections.sort(defaultSortingList, Collections.reverseOrder());
        defaultSortingList.equals(newList);

        and: "'DisplayName - Descending' should be shown in the menu"
        sortContentDialog.getCurrentSortingName() == SortMenuItem.DISPLAY_NAME.getValue();
    }

    def "GIVEN sort content dialog is opened WHEN 'DisplayName - Ascending' has been selected and 'Save' button clicked THEN contents in the browse panel should be correctly sorted"() {
        given: "folder with contents is selected"
        findAndSelectContent(IMPORTED_FOLDER_NAME);
        SortContentDialog sortContentDialog = contentBrowsePanel.clickToolbarSort();

        when: "'ascending by display name' has been selected "
        List<String> contentsInDialog = sortContentDialog.clickOnTabMenu().doSort(
                SortMenuItem.DISPLAY_NAME.getValue(),SortOrder.ASCENDING).getContentNames();
        sortContentDialog.clickOnSaveButton();
        contentBrowsePanel.expandContent(ContentPath.from(IMPORTED_FOLDER_NAME));

        then: "contents in the browse panel should be correctly sorted"
        List<String> names = contentBrowsePanel.getChildContentDisplayNamesFromTreeGrid(IMAGES_FOLDER_DISPLAY_NAME);
        contentsInDialog.equals(names);
    }
}
