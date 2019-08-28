package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

/**
 * Created  on 3/1/2017.
 *
 * */
class PublishingWizard_Excluding_NotParent_Spec
    extends BaseContentSpec
{
    @Shared
    Content SHORTCUT_CONTENT;

    @Shared
    String TEST_IMAGE_DISPLAY_NAME = "seng"

    //XP-4890 Publishing Wizard - Enable excluding any item from the dependants list if it's not a parent to other items in the list
    def "GIVEN new shortcut to an image has been added WHEN the shortcut has been selected AND publishing wizard opened THEN the image should be displayed with the 'remove' icon"()
    {
        given: " shortcut to an image was added in the root WHEN the shortcut is selected"
        SHORTCUT_CONTENT = buildShortcutWithTarget( "shortcut", null, "shortcut display name", TEST_IMAGE_DISPLAY_NAME );
        addReadyContent( SHORTCUT_CONTENT );
        findAndSelectContent( SHORTCUT_CONTENT.getName() );

        when: "the shortcut has been selected AND publishing wizard is opened"
        ContentPublishDialog dialog = contentBrowsePanel.clickToolbarPublish();
        dialog.clickOnShowDependentItemsLink();

        then: "the image should be displayed with the 'remove' icon"
        dialog.isDependantItemRemovable( TEST_IMAGE_DISPLAY_NAME )
    }
}
