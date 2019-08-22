package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.form.ImageFormViewPanel
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Stepwise

/**
 * Created  on 28.10.2016.
 *
 * Task:XP-4352 Add selenium test to verify the XP-4351
 * Bug: XP-4351 Status of published content changes from "Modified" to "Online" after save
 * */
@Stepwise
class Image_Change_And_Publish_Spec
    extends BaseContentSpec
{
    //verifies status on the wizard page
    def "GIVEN existing 'Published' image WHEN the image has been zoomed AND changes were applied THEN status is getting 'Modified'"()
    {
        given: "existing 'online' image"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_ELEPHANT_IMAGE ).clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        formViewPanel.waitUntilImageLoaded();
        wizard.clickOnMarkAsReadyAndDoPublish(  );
        formViewPanel.waitUntilImageLoaded();

        when: "the image has been zoomed "
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        imageEditor.doZoomImage( 70 );

        and: "changes were applied"
        imageEditor.getToolbar().clickOnApplyButton();

        and: "content saved in the wizard"
        wizard.save();
        saveScreenshot( "online_image_zoomed" );

        then: "status should be 'Modified'"
        wizard.waitStatus( ContentStatus.MODIFIED, Application.EXPLICIT_NORMAL );
    }
    //verifies status in the Grid
    def "GIVEN 'Published' image that has been zoomed WHEN the image has been selected in the grid THEN 'Modified' status should be displayed in grid"()
    {
        when: "online image that has been zoomed is selected"
        findAndSelectContent( IMPORTED_ELEPHANT_IMAGE );

        then: "Modified status is displayed in the grid for this content"
        contentBrowsePanel.getContentStatus( IMPORTED_ELEPHANT_IMAGE ) == ContentStatus.MODIFIED.getValue();
    }
}
