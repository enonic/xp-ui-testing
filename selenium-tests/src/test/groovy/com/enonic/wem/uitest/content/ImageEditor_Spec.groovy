package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditorToolbar
import com.enonic.autotests.pages.form.ImageFormViewPanel

/**
 * Created on 28.09.2016.
 * XP-4201 Add selenium tests for ImageEditor dialog
 * XP-4226 Add selenium tests for 'Reset Mask' and 'Reset Autofocus' links on the ImageEditor's tollbar
 * */
class ImageEditor_Spec
    extends BaseContentSpec
{
    def "GIVEN image content opened WHEN 'Crop' button was pressed THEN 'Image Editor' dialog appears with required control elements"()
    {
        given: "content wizard opened"
        findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = new ImageEditor( getSession() );

        when: "'Crop' button was pressed"
        imageFormViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();
        saveScreenshot( "image_editor_dialog_opened" );

        then: "toolbar appears on the top right"
        toolbar.isDisplayed();

        and: "'Apply' button is present"
        toolbar.isApplyButtonDisplayed();

        and: "'Close' button is present"
        toolbar.isCloseButtonDisplayed();

        and: "'Focus Circle' not displayed"
        !imageEditor.isFocusCircleDisplayed();

        and: "'Reset Mask' link is not displayed"
        !toolbar.isResetMaskDisplayed();
    }

    def "GIVEN image content opened AND 'Crop' button was pressed THEN image was cropped THEN 'Reset Mask' link appears on the toolbar "()
    {
        given: "content wizard opened"
        findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = new ImageEditor( getSession() );

        when: "'Crop' button was pressed"
        imageFormViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();
        and: "image was cropped"
        imageEditor.doDragAndChangeSizeOfImage( -40 );

        then: "'Reset Mask' link is displayed"
        toolbar.isResetMaskDisplayed();

        and: "'Apply' button is present"
        toolbar.isApplyButtonDisplayed();

        and: "'Close' button is present"
        toolbar.isCloseButtonDisplayed();
    }

    def "GIVEN a cropped image WHEN 'Reset Mask' was pressed THEN the button is getting hidden AND image's size is not changed"()
    {
        given: "content wizard opened"
        findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = new ImageEditor( getSession() );

        and: "crop button pressed"
        imageFormViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();
        def imageHeightBefore = imageEditor.getImageHeight();

        and: "the image has been cropped"
        imageEditor.doDragAndChangeSizeOfImage( -40 );

        when: "'Reset Mask' button was pressed"
        toolbar.clickOnResetMaskButton();
        def imageHeightAfter = imageEditor.getImageHeight();

        then: "toolbar still displayed"
        toolbar.isDisplayed();

        and: "'Reset Mask' is getting hidden"
        !toolbar.isResetMaskDisplayed();

        and: "image's size is not changed"
        imageHeightBefore == imageHeightAfter;
    }

    def "GIVEN 'Image Editor' dialog opened WHEN 'Close' button was pressed THEN the dialog closes"()
    {
        given: "'Image Editor' dialog opened"
        findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();

        when: "'Close' button was pressed"
        toolbar.clickOnCloseButton();
        saveScreenshot( "image_editor_close_pressed" );

        then: "toolbar getting hidden"
        !toolbar.isDisplayed();
    }

    def "GIVEN image content opened WHEN 'Focus' button was pressed THEN 'Image Editor' dialog appears and focus circle is present on it"()
    {
        given: "'Image Editor' dialog opened"
        findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        when: "'Close' button was pressed"
        ImageEditor imageEditor = formViewPanel.clickOnFocusButton();
        saveScreenshot( "image_editor_focus_circle" );

        then: "dialog opened"
        imageEditor.isFocusCircleDisplayed();
    }

}
