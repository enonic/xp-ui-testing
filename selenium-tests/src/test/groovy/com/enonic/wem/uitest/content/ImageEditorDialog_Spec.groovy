package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditorDialog
import com.enonic.autotests.pages.form.ImageFormViewPanel

/**
 * Created on 28.09.2016.*/
class ImageEditorDialog_Spec
    extends BaseContentSpec
{

    def "GIVEN image content opened WHEN 'Crop' button was pressed THEN 'Image Editor' dialog appears with required control elements"()
    {
        given: "content wizard opened"
        findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        when: "'Crop' button was pressed"
        ImageEditorDialog imageEditorDialog = formViewPanel.clickOnCropButton();
        saveScreenshot( "image_editor_dialog_opened" );

        then: "dialog opened"
        imageEditorDialog.isOpened();

        and: "'Apply' button present"
        imageEditorDialog.isApplyButtonDisplayed();

        and: "'Close' button present"
        imageEditorDialog.isCloseButtonPresent();

        and: "'Focus Circle' not displayed"
        !imageEditorDialog.isFocusCircleDisplayed()
    }

    def "GIVEN 'Image Editor' dialog opened WHEN 'Close' button was pressed THEN the dialog closes"()
    {
        given: "'Image Editor' dialog opened"
        findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditorDialog imageEditorDialog = formViewPanel.clickOnCropButton();

        when: "'Close' button was pressed"
        imageEditorDialog.clickOnCloseButton();
        saveScreenshot( "image_editor_dialog_closed" );

        then: "dialog closes"
        imageEditorDialog.isOpened();
    }

    def "GIVEN image content opened WHEN 'Focus' button was pressed THEN 'Image Editor' dialog appears and focus circle is present on it"()
    {
        given: "'Image Editor' dialog opened"
        findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );


        when: "'Close' button was pressed"
        ImageEditorDialog imageEditorDialog = formViewPanel.clickOnFocusButton();
        saveScreenshot( "image_editor_focus_circle" );

        then: "dialog opened"
        imageEditorDialog.isOpened();

        and: "Focus Circle is displayed"
        imageEditorDialog.isFocusCircleDisplayed();
    }
}
