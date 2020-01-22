package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditorToolbar
import com.enonic.autotests.pages.form.ImageFormViewPanel
import spock.lang.Ignore
import spock.lang.Stepwise

/**
 * Created on 17.10.2016.
 *
 * */
@Stepwise
class ImageEditor_Zoom_Reset_Spec
    extends BaseContentSpec
{

    def "GIVEN 'Image Editor' opened WHEN image has been zoomed THEN 'Reset' button should appear"()
    {
        given: "'Image Editor' dialog opened"
        findAndSelectContent( IMPORTED_MAN2_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();

        when: "the image has been zoomed "
        imageEditor.doZoomImage( 70 );
        saveScreenshot( "image_zoomed_70" );

        and: "'Apply' button has been pressed"
        toolbar.clickOnApplyButton();
        saveScreenshot( "zoom_applied" )

        then: "ImageEditor's toolbar is getting hidden"
        !toolbar.isDisplayed();

        and: "'Reset' button should be present"
        formViewPanel.isButtonResetPresent();
    }

    def "GIVEN 'Image Editor' opened WHEN image has been zoomed AND Apply button pressed AND 'Close' button pressed THEN 'Alert' dialog with warning messages should appear"()
    {
        given: "'Image Editor' dialog opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_MAN2_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();

        when: "the image has been zoomed "
        imageEditor.doZoomImage( 70 );

        and: "'Apply' button pressed"
        toolbar.clickOnApplyButton();
        saveScreenshot( "image_zoomed_and_applied" );

        and: "wizard-tab is closing, but 'Save' was not pressed"
        wizard.executeCloseWizardScript();

        then: "'Alert' dialog with warning messages should appear"
        wizard.isAlertPresent();
    }

    def "GIVEN 'Image Editor' opened WHEN image has been zoomed AND Apply button pressed AND Save AND 'Close' button pressed THEN Alert modal dialog should not appear"()
    {
        given: "'Image Editor' dialog opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_MAN2_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();

        when: "the image has been zoomed "
        imageEditor.doZoomImage( 70 );

        and: "'Apply' button pressed"
        toolbar.clickOnApplyButton();
        saveScreenshot( "image_zoomed_and_applied" );

        and: "'Save' was pressed and wizard-tab is closing"
        wizard.save().executeCloseWizardScript();

        then: "'Alert' dialog with warning messages should not appear"
        !wizard.isAlertPresent();
    }

    //https://github.com/enonic/app-contentstudio/issues/1365
    //Image Wizard - Save button gets enabled after reverting changes (rotated or flipped) #1365
    def "WHEN existing zoomed image is opened THEN 'Reset' button should be displayed in the wizard"()
    {
        when: "existing zoomed image"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_MAN2_IMAGE ).clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        then: "'Reset' button should be displayed in the wizard"
        formViewPanel.isButtonResetPresent();

        and: "Save button should be disabled"
        !wizard.isSaveButtonEnabled();
    }

    def "GIVEN existing zoomed image is opened WHEN 'Reset' button has been pressed AND 'close' icon pressed THEN 'Alert' dialog with warning messages should appear"()
    {
        given: "existing zoomed image"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_MAN2_IMAGE ).clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        when: "'Reset' button has been pressed"
        formViewPanel.clickOnResetButton();

        and: "'close tab' button was pressed, but Save was not pressed"
        wizard.executeCloseWizardScript();

        then: "'Alert' dialog with warning messages should appear"
        wizard.waitIsAlertDisplayed();
    }

    def "GIVEN existing zoomed image is opened WHEN 'Reset' button has been pressed AND 'Saved' and 'close' buttons pressed THEN 'Alert' dialog should not appear"()
    {
        given: "existing zoomed image"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_MAN2_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        when: "'Reset' button has been pressed"
        formViewPanel.clickOnResetButton();

        and: "'Save' has been pressed and wizard-tab is closing"
        wizard.save().executeCloseWizardScript();

        then: "'Alert' dialog should not appear"
        !wizard.isAlertPresent();
    }
}
