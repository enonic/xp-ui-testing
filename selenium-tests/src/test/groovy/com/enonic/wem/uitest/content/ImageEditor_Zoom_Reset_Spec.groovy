package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditorToolbar
import com.enonic.autotests.pages.form.ImageFormViewPanel
import spock.lang.Stepwise

/**
 * Created on 17.10.2016.
 *
 * Task: XP-4272 Add selenium tests for zooming an image
 *
 * */
@Stepwise
class ImageEditor_Zoom_Reset_Spec
    extends BaseContentSpec
{

    def "GIVEN 'Image Editor' opened WHEN image has been zoomed THEN 'Reset' button appears"()
    {
        given: "'Image Editor' dialog opened"
        findAndSelectContent( IMPORTED_MAN2_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();

        when: "the image has been zoomed "
        imageEditor.doZoomImage( 70 );
        saveScreenshot( "image_zoomed_70" );

        and: "Apply button pressed"
        toolbar.clickOnApplyButton();
        saveScreenshot( "zoom_applied" )

        then: "ImageEditor's toolbar getting hidden"
        !toolbar.isDisplayed();

        and: "'Reset' button appeared"
        formViewPanel.isButtonResetPresent();
    }

    def "GIVEN 'Image Editor' opened WHEN image has been zoomed AND Apply button pressed AND 'Close' button pressed THEN 'save before close' modal dialog appears"()
    {
        given: "'Image Editor' dialog opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_MAN2_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();

        when: "the image has been zoomed "
        imageEditor.doZoomImage( 70 );

        and: "Apply button pressed"
        toolbar.clickOnApplyButton();
        saveScreenshot( "image_zoomed_and_applied" );

        and: "'close' button pressed"
        def modalDialog = wizard.close( IMPORTED_MAN2_IMAGE );

        then: "'save before close' dialog appears"
        modalDialog != null
    }

    def "GIVEN 'Image Editor' opened WHEN image has been zoomed AND Apply button pressed AND Save AND 'Close' button pressed THEN 'save before close' modal dialog appears"()
    {
        given: "'Image Editor' dialog opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_MAN2_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();

        when: "the image has been zoomed "
        imageEditor.doZoomImage( 70 );

        and: "Apply button pressed"
        toolbar.clickOnApplyButton();
        saveScreenshot( "image_zoomed_and_applied" );

        and: "'close' button pressed"
        def modalDialog = wizard.save().close( IMPORTED_MAN2_IMAGE );

        then: "'save before close' dialog does not appear"
        modalDialog == null
    }

    def "GIVEN existing zoomed image WHEN it opened THEN 'Reset' button displayed on the wizard"()
    {
        when: "existing zoomed image"
        findAndSelectContent( IMPORTED_MAN2_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        then: "'Reset' button displayed on the wizard"
        formViewPanel.isButtonResetPresent();
    }

    def "GIVEN existing zoomed image WHEN it opened AND 'Reset' button pressed AND 'close' button pressed THEN 'save before close' dialog appears"()
    {
        given: "existing zoomed image"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_MAN2_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        when: "'Reset' button has been pressed"
        formViewPanel.clickOnResetButton();

        and: "'close' button pressed"
        def modalDialog = wizard.close( IMPORTED_MAN2_IMAGE );

        then: "'save before close' dialog appears"
        modalDialog != null
    }

    def "GIVEN existing zoomed image WHEN it opened AND 'Reset' button pressed AND 'close' button pressed AND 'save' pressed THEN 'save before close' dialog appears"()
    {
        given: "existing zoomed image"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_MAN2_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        when: "'Reset' button has been pressed"
        formViewPanel.clickOnResetButton();

        and: "'save' and 'close' buttons have been pressed"
        def modalDialog = wizard.save().close( IMPORTED_MAN2_IMAGE );

        then: "'save before close' dialog does not appear"
        modalDialog == null
    }
}
