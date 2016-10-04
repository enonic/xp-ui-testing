package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditorToolbar
import com.enonic.autotests.pages.form.ImageFormViewPanel

/**
 * Created on 28.09.2016.*/
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
