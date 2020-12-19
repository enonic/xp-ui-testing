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
 * */
@Stepwise
class Image_Change_And_Publish_Spec
    extends BaseContentSpec
{

    def "WHEN existing 'Published' image has been zoomed THEN image status gets 'Modified'"()
    {
        given: "existing 'online' image"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_BRO_IMAGE ).clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        formViewPanel.waitUntilImageLoaded();
        wizard.clickOnPublishButton().clickOnPublishNowButton();
        sleep( 1000 );
        formViewPanel.waitUntilImageLoaded();

        when: "the image has been zoomed"
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        imageEditor.doZoomImage( 70 );

        and: "changes have been applied"
        imageEditor.getToolbar().clickOnApplyButton();

        and: "content has been saved"
        wizard.save();
        saveScreenshot( "published_image_zoomed" );

        then: "status should be 'Modified'"
        wizard.waitStatus( ContentStatus.MODIFIED, Application.EXPLICIT_NORMAL );
    }
    //verifies status in the Grid
    def "WHEN the modified image has been selected THEN 'Modified' status should be displayed in the grid"()
    {
        when: "the modified image has been selected"
        findAndSelectContent( IMPORTED_BRO_IMAGE );

        then: "Modified status should be displayed in the grid"
        contentBrowsePanel.getContentStatus( IMPORTED_BRO_IMAGE ) == ContentStatus.MODIFIED.getValue();
    }
}
