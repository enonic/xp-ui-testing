package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditorToolbar
import com.enonic.autotests.pages.form.ImageFormViewPanel
import spock.lang.Ignore
import spock.lang.Stepwise

/**
 *
 * Created on 30.09.2016.
 * */
@Stepwise
class ImageEditor_Crop_Reset_Spec
    extends BaseContentSpec
{
    def "GIVEN 'Image Editor' dialog is opened WHEN dragHandler moved up AND image is cropped THEN image's height should be reduced"()
    {
        given: "'Image Editor' dialog opened"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEditAndSwitchToWizardTab();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        sleep(1000);
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();
        int before = imageEditor.getCropAreaHeight();

        when: "handler moved up and image was cropped "
        imageEditor.doDragCropButtonAndChangeHeightCropArea( -50 );
        saveScreenshot( "image_cropped" );

        and: "Apply button pressed"
        toolbar.clickOnApplyButton();
        saveScreenshot( "image_cropped_and_applied" )
        int after = imageEditor.getCropAreaHeight();

        then: "the height of the 'crop area' is reduced"
        before - after == 50;

        and: "ImageEditor's toolbar getting hidden"
        !toolbar.isDisplayed();

        and: "'Reset' button appeared"
        formViewPanel.isButtonResetPresent();
    }
    //verifies XP-4167 Impossible to save changes and close the Wizard after an image was cropped
    def "GIVEN 'Image Editor' dialog opened WHEN dragHandler moved up AND image cropped  AND save button pressed AND wizard closed THEN 'save before close' dialog does not appear"()
    {
        given: "'Image Editor' dialog opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEditAndSwitchToWizardTab();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        sleep(1000);

        when: " dragHandler moved up AND image cropped "
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        imageEditor.doDragCropButtonAndChangeHeightCropArea( -50 );
        imageEditor.getToolbar().clickOnApplyButton();

        and: "Save button has been pressed "
        wizard.save();

        and: "wizard has been closed"
        wizard.executeCloseWizardScript();

        then: "'Alert' with warning message should not be displayed"
        !wizard.isAlertPresent();
    }

    def "GIVEN existing cropped image WHEN the image opened THEN 'Reset' button displayed on the page"()
    {
        given: "existing cropped image"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        when: "the image was opened"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        sleep(1000);

        then: "'Reset' button should be displayed on the Image Editor"
        formViewPanel.isButtonResetPresent();
    }

    def "GIVEN existing cropped image opened WHEN 'Reset' button has been pressed THEN 'Reset' button is getting hidden"()
    {
        given: "existing cropped image"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEditAndSwitchToWizardTab();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );
        sleep(1000);
        saveScreenshot( "reset_button_displayed" );

        when: "'Reset' button has been pressed"
        imageFormViewPanel.clickOnResetButton();
        wizard.save();
        saveScreenshot( "reset_button_pressed" );

        then: "'Reset' button is getting hidden"
        !imageFormViewPanel.isButtonResetPresent();
    }


    def "GIVEN existing cropped image opened WHEN  'Reset' button has been pressed AND 'Save' button pressed AND tab with the wizard has been closed THEN Alert dialog should not appear"()
    {
        given: "existing cropped image"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEditAndSwitchToWizardTab();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );
        sleep(1000);

        and: "the image has been cropped"
        ImageEditor imageEditor = imageFormViewPanel.clickOnCropButton();
        imageEditor.doDragCropButtonAndChangeHeightCropArea( -50 );
        and: "changes was applied"
        imageEditor.getToolbar().clickOnApplyButton();

        when: "'Reset' button pressed"
        imageFormViewPanel.clickOnResetButton();

        and: "tab with wizard has been closed"
        wizard.executeCloseWizardScript();

        then: "Alert dialog should not appear"
        !wizard.isAlertPresent();
    }
}
