package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditorToolbar
import com.enonic.autotests.pages.form.ImageFormViewPanel
import spock.lang.Ignore
import spock.lang.Stepwise

/**
 * Created on 30.09.2016.*/
@Stepwise
class ImageEditor_Crop_Reset_Spec
    extends BaseContentSpec
{
    def "GIVEN 'Image Editor' dialog opened WHEN dragHandler moved up AND image cropped THEN image's height was reduced"()
    {
        given: "'Image Editor' dialog opened"
        findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar()
        int before = imageEditor.getImageHeight();

        when: "handler moved up and image cropped "
        imageEditor.doDragAndChangeSizeOfImage( -50 );
        saveScreenshot( "image_cropped" );

        and: "Apply button pressed"
        toolbar.clickOnApplyButton();
        saveScreenshot( "image_cropped_and_applied" )
        int after = imageEditor.getImageHeight();

        then: "the height of the image is reduced"
        before - after == 50;

        and: "ImageEditor's toolbar getting hidden"
        !toolbar.isDisplayed();

        and: "'Reset' button appeared"
        formViewPanel.isButtonResetPresent();
    }
    //XP-4167
    @Ignore
    def "GIVEN 'Image Editor' dialog opened WHEN dragHandler moved up AND image cropped  AND save  button pressed THEN image's height was reduced"()
    {
        given: "'Image Editor' dialog opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        when: " dragHandler moved up AND image cropped "
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        imageEditor.doDragAndChangeSizeOfImage( -50 );
        imageEditor.getToolbar().clickOnApplyButton();

        and: "Save button pressed "
        wizard.save();

        and: "wizard closed"
        def result = wizard.close( IMPORTED_BOOK_IMAGE );
        saveScreenshot( "cropped_image_saved_and_closed" );

        then: "wizard closed and save before close dialog does not appear"
        result == null;
    }
    //XP-4167
    @Ignore
    def "GIVEN existing cropped image WHEN the image opened THEN 'Reset' button displayed on the page"()
    {
        given: "existing cropped image"
        findAndSelectContent( IMPORTED_BOOK_IMAGE )
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        when: "the image opened"
        contentBrowsePanel.clickToolbarEdit().waitUntilWizardOpened();

        then: "'Reset' button displayed on the Image Editor"
        formViewPanel.isButtonResetPresent();
    }
    //XP-4167
    @Ignore
    def "GIVEN existing cropped image opened WHEN  'Reset' button has been pressed THEN 'Reset' button is getting hidden"()
    {
        given: "existing cropped image"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );
        saveScreenshot( "reset_button_displayed" );

        when: "the image opened"
        imageFormViewPanel.clickOnResetButton();
        wizard.save();
        saveScreenshot( "reset_button_pressed" );

        then: "'Reset' button is getting hidden"
        !imageFormViewPanel.isButtonResetPresent();
    }

    //XP-4167
    @Ignore
    def "GIVEN existing cropped image opened WHEN  'Reset' button has been pressed AND 'Save' button pressed AND 'Close' button pressed THEN the wizard closes"()
    {
        given: "existing cropped image"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );

        and: "the image has been cropped"
        ImageEditor imageEditor = imageFormViewPanel.clickOnCropButton();
        imageEditor.doDragAndChangeSizeOfImage( -50 );
        and: "changes was applied"
        imageEditor.getToolbar().clickOnApplyButton();

        when: "'Reset' button pressed"
        imageFormViewPanel.clickOnResetButton();
        and: "'close' wizard button pressed"
        def result = wizard.save().close( IMPORTED_BOOK_IMAGE );
        saveScreenshot( "reset_button_pressed_wizard_closed" );

        then: "Image-wizard closes"
        result == null;
    }
}
