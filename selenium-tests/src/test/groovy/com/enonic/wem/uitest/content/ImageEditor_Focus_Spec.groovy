package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditorToolbar
import com.enonic.autotests.pages.form.ImageFormViewPanel

/**
 * Created on 04.10.2016.*/
class ImageEditor_Focus_Spec
    extends BaseContentSpec
{
    def "GIVEN 'Image Editor' dialog opened WHEN dragHandler moved up AND image cropped THEN image's height was reduced"()
    {
        given: "'Image Editor' dialog opened"
        findAndSelectContent( IMPORTED_WHALE_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnFocusButton();
        ImageEditorToolbar imageEditorToolbar = imageEditor.getToolbar();


        when: "handler moved up and image cropped "
        imageEditor.doDragAndChangeFocus( -50 );
        saveScreenshot( "image_focus_changed" );

        and: "Apply button pressed"
        imageEditorToolbar.clickOnApplyButton();
        saveScreenshot( "focus_moved_and_applied" )

        then: "red circle displayed on the image-editor"
        imageEditor.isFocusCircleDisplayed();

        and: "ImageEditor's toolbar is getting hidden"
        !imageEditorToolbar.isDisplayed();

        and: "'Reset' button appeared"
        formViewPanel.isButtonResetPresent();
    }
}
