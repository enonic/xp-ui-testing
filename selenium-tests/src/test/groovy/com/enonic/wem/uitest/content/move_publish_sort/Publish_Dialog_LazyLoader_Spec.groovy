package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.wem.uitest.content.BaseContentSpec

/**
 * Created  on 23.12.2016.
 *
 * Verifies:
 *   XP-4754 Publish Dialog - Lazy loader stops loading items after turning "Include child items" off and on again
 * */
class Publish_Dialog_LazyLoader_Spec
    extends BaseContentSpec
{
    def "GIVEN existing folder with children WHEN the folder has been selected AND 'Publish Tree' menu item clicked AND 'Include child items' is unchecked AND dialog closed WHEN Publish Tree menu item clicked again THEN children should be visible on the modal dialog"()
    {
        given: "existing folder with children is selected and 'Publish Tree' menu item was clicked"
        ContentPublishDialog contentPublishDialog = findAndSelectContent( IMPORTED_FOLDER_NAME ).selectPublishTreeMenuItem();
        contentPublishDialog.clickOnShowDependentItemsLink();
        def numberOfChild = contentPublishDialog.getDependantList().size();

        when: "'Include child items' is unchecked and ContentPublishDialog has been closed"
        contentPublishDialog.includeChildren( false ).clickOnCancelTopButton();

        and: "Publish Tree menu item has been clicked  and the dialog is opened again"
        contentBrowsePanel.selectPublishTreeMenuItem();
        contentPublishDialog.clickOnShowDependentItemsLink();

        then: "children should be loaded on the modal dialog"
        contentPublishDialog.getDependantList().size() == numberOfChild;
    }
}
