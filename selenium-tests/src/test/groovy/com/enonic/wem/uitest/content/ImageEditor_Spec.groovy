package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditorToolbar
import com.enonic.autotests.pages.form.ImageFormViewPanel

/**
 * Created on 28.09.2016.
 * */
class ImageEditor_Spec
    extends BaseContentSpec
{
    def "GIVEN image content is opened WHEN 'Crop' button has been pressed THEN 'Image Editor' dialog appears with required control elements"()
    {
        given: "an image is opened"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = new ImageEditor( getSession() );

        when: "'Crop' button has been pressed"
        imageFormViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();
        saveScreenshot( "image_editor_dialog_opened" );

        then: "toolbar appears on the top right"
        toolbar.isDisplayed();

        and: "'Apply' button is present"
        toolbar.isApplyButtonDisplayed();

        and: "'Close' button is present"
        toolbar.isCloseButtonDisplayed();

        and: "'zoom knob' is displayed"
        imageEditor.isZoomKnobPresent();

        and: "'zoom knob' button present"
        imageEditor.getZoomKnobValue() == 0;

        and: "'Focus Circle' should not be displayed"
        !imageEditor.isFocusCircleDisplayed();

        and: "'Reset Mask' link should not be displayed"
        !toolbar.isResetMaskDisplayed();
    }

    def "GIVEN image content is opened WHEN 'Crop' button has been pressed THEN image should be cropped AND 'Reset Mask' link appears in the toolbar "()
    {
        given: "an image is opened"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = new ImageEditor( getSession() );

        when: "'Crop' button has been pressed"
        imageFormViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();
        and: "image was cropped"
        imageEditor.doDragCropButtonAndChangeHeightCropArea( -40 );

        then: "'Reset Mask' link should appear"
        toolbar.isResetMaskDisplayed();

        and: "'Apply' button should be present"
        toolbar.isApplyButtonDisplayed();

        and: "'Close' button should be present"
        toolbar.isCloseButtonDisplayed();
    }

    def "GIVEN a cropped image WHEN 'Reset Mask' has been pressed THEN the button gets hidden AND initial size should be restored"()
    {
        given: "an image is opened"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = new ImageEditor( getSession() );

        and: "crop button has been pressed:"
        imageFormViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();
        def cropAreaBefore = imageEditor.getCropAreaHeight();

        and: "the image has been cropped:"
        imageEditor.doDragCropButtonAndChangeHeightCropArea( -40 );

        when: "'Reset Mask' button has been pressed"
        toolbar.clickOnResetMaskButton();
        def cropAreaHeightAfter = imageEditor.getCropAreaHeight();

        then: "toolbar should be displayed"
        toolbar.isDisplayed();

        and: "'Reset Mask' gets hidden"
        !toolbar.isResetMaskDisplayed();

        and: "initial crop area is restored"
        cropAreaBefore == cropAreaHeightAfter;
    }

    def "GIVEN 'Image Editor' dialog is opened WHEN 'Close' button has been pressed THEN the dialog closes"()
    {
        given: "'Image Editor' dialog opened"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();

        when: "'Close' button was pressed"
        toolbar.clickOnCloseButton();
        saveScreenshot( "image_editor_close_pressed" );

        then: "toolbar getting hidden"
        !toolbar.isDisplayed();
    }


    def "GIVEN image content opened AND 'Focus' button was pressed WHEN focus circle moved THEN 'Reset Autofocus' link appears on the toolbar"()
    {
        given: "'Image Editor' dialog opened"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnFocusButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();

        when: "'Autofocus' has been moved"
        imageEditor.doDragAndChangeFocus( -40 );

        then: "red circle is displayed on the Image Editor"
        imageEditor.isFocusCircleDisplayed();

        and: "'Reset Autofocus' link appears on the toolbar"
        toolbar.isResetAutoFocusDisplayed();
    }

    def "GIVEN image  opened AND 'AutoFocus' was moved WHEN 'Reset Autofocus' link pressed THEN 'Reset Autofocus' link is getting hidden"()
    {
        given: "'Image Editor' dialog opened"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = imageFormViewPanel.clickOnFocusButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();
        and: "'Autofocus' was moved"
        imageEditor.doDragAndChangeFocus( -40 );

        when: "'Reset Autofocus' link was pressed"
        toolbar.clickOnResetAutofocusButton();
        saveScreenshot( "reset_focus_pressed" );

        then: "red circle is  displayed on the Image Editor"
        imageEditor.isFocusCircleDisplayed();

        and: "'Reset Autofocus' is getting hidden on the toolbar"
        !toolbar.isResetAutoFocusDisplayed();
    }

    def "GIVEN existing an image opened AND 'AutoFocus' was moved  AND all were saved WHEN image opened THEN 'Reset' button is displayed"()
    {
        given: "image opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = imageFormViewPanel.clickOnFocusButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();

        and: "focus was moved"
        imageEditor.doDragAndChangeFocus( -40 );

        and: "'Apply' button was clicked"
        toolbar.clickOnApplyButton();

        and: "image saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "image opened again"
        contentBrowsePanel.clickToolbarEdit().waitUntilWizardOpened();

        then: "'Reset' red-button displayed on the page"
        imageFormViewPanel.isButtonResetPresent();

        and: "red circle is  displayed on the Image Editor"
        imageEditor.isFocusCircleDisplayed();
    }
}
