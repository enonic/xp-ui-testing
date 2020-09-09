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
    def "WHEN 'Publish Wizard' has been opened AND 'Include child items' unchecked WHEN 'Publish Wizard' has been reopened THEN children should be visible on the modal dialog"()
    {
        given: "existing folder is selected and 'Publish Wizard' has been opened"
        ContentPublishDialog contentPublishDialog = findAndSelectContent( IMPORTED_FOLDER_NAME ).selectPublishTreeMenuItem();
        contentPublishDialog.clickOnShowDependentItemsLink();
        def numberOfChild = contentPublishDialog.getDependantList().size();

        when: "'Include child items' has been unchecked and ContentPublishDialog has been closed"
        contentPublishDialog.includeChildren( false ).clickOnCancelTopButton();

        and: "'Publish Wizard' has been reopened"
        contentBrowsePanel.selectPublishTreeMenuItem();
        contentPublishDialog.clickOnShowDependentItemsLink();

        then: "children should be loaded on the modal dialog"
        contentPublishDialog.getDependantList().size() == numberOfChild;
    }
}
