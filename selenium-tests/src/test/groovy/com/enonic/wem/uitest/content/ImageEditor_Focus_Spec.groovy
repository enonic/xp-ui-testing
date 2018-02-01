package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.HomePage
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditorToolbar
import com.enonic.autotests.pages.form.ImageFormViewPanel
import spock.lang.Ignore
import spock.lang.Stepwise

/**
 * Created on 04.10.2016.
 * XP-4328 Add selenium tests for changing of focus in the Image Wizard
 * */
@Stepwise
class ImageEditor_Focus_Spec
    extends BaseContentSpec
{
    def "GIVEN 'Image Editor' dialog opened WHEN focus has been moved THEN red circle appears"()
    {
        given: "'Image Editor' dialog opened"
        findAndSelectContent( WHALE_IMAGE_NAME ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnFocusButton();
        ImageEditorToolbar imageEditorToolbar = imageEditor.getToolbar();


        when: "focus has been moved"
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

    def "GIVEN 'Image Editor' dialog is opened WHEN focus was changed AND 'Save' button has been pressed THEN 'save before close dialog' does not appear"()
    {
        given: "'Image Editor' dialog opened"
        ContentWizardPanel wizard = findAndSelectContent( WHALE_IMAGE_NAME ).clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        when: "dragHandler was moved up AND the image is cropped "
        ImageEditor imageEditor = formViewPanel.clickOnFocusButton();
        imageEditor.doDragAndChangeFocus( -50 );
        imageEditor.getToolbar().clickOnApplyButton();

        and: "'Save' button has been pressed "
        wizard.save();

        and: "wizard has been closed"
        wizard.executeCloseWizardScript();

        then: "Alert dialog should not appear"
        !wizard.isAlertPresent();
    }

    def "GIVEN existing image with changed focus WHEN the image opened THEN 'Reset' button displayed on the page"()
    {
        given: "existing image with changed focus"
        findAndSelectContent( WHALE_IMAGE_NAME )
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        when: "the image opened"
        contentBrowsePanel.clickToolbarEdit().waitUntilWizardOpened();

        then: "'Reset' button should be displayed on the Image Editor"
        formViewPanel.isButtonResetPresent();
    }

    def "GIVEN existing image with changed focus WHEN 'Reset' button has been pressed THEN 'Reset' button is getting hidden"()
    {
        given: "existing image with changed focus"
        ContentWizardPanel wizard = findAndSelectContent( WHALE_IMAGE_NAME ).clickToolbarEdit();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );

        when: "'Reset' button has been pressed"
        imageFormViewPanel.clickOnResetButton();
        wizard.save();
        saveScreenshot( "focus_reset_button_pressed" );

        then: "'Reset' button is getting hidden"
        !imageFormViewPanel.isButtonResetPresent();
    }

    def "GIVEN existing image with changed focus WHEN 'Reset' button has been pressed AND 'Save' button pressed AND 'Close' button pressed THEN Alert dialog should not be present"()
    {
        given: "existing image"
        ContentWizardPanel wizard = findAndSelectContent( WHALE_IMAGE_NAME ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );

        and: "focus was moved"
        ImageEditor imageEditor = imageFormViewPanel.clickOnFocusButton();
        imageEditor.doDragAndChangeFocus( -50 );

        and: "changes was applied"
        imageEditor.getToolbar().clickOnApplyButton();

        when: "'Reset' button pressed"
        imageFormViewPanel.clickOnResetButton();

        and: "'close' wizard button pressed"
        wizard.executeCloseWizardScript();

        then: "Alert dialog should not appear"
        !wizard.isAlertPresent();
    }

    def cleanup()
    {
        acceptAlerts();
    }
}
