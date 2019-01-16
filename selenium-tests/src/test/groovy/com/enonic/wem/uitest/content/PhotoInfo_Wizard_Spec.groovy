package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.PhotoInfoFormViewPanel

/**
 * Created on 23.09.2016.
 * */
class PhotoInfo_Wizard_Spec
    extends BaseContentSpec
{

    def "WHEN existing image content is opened THEN all control elements are present"()
    {
        given: "existing image content is opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEdit();

        when: "'Photo' step has been clicked"
        wizard.clickOnWizardStep( "Photo" );
        PhotoInfoFormViewPanel photoInfoFormViewPanel = new PhotoInfoFormViewPanel( getSession() );

        then: "input for 'focal length'  is present"
        photoInfoFormViewPanel.isFocalLengthInputPresent();

        and: "input for 'lens' is displayed"
        photoInfoFormViewPanel.isLensInputPresent();

        and: "input for 'make'  is present"
        photoInfoFormViewPanel.isMakeInputPresent();

        and: "input for 'made'  is present"
        photoInfoFormViewPanel.isModelInputPresent();

        and: "input for 'date time' is displayed"
        photoInfoFormViewPanel.isDateTimeInputPresent();

        and: "input for 'iso' is displayed"
        photoInfoFormViewPanel.isISOInputPresent();

        and: "input for 'focalLength35' is displayed"
        photoInfoFormViewPanel.isFocalLength35InputPresent();

        and: "input for 'exposure bias' is displayed"
        photoInfoFormViewPanel.isExposureBiasInputPresent();

        and: "input for 'aperture' is displayed"
        photoInfoFormViewPanel.isApertureInputPresent();


        and: "input for 'auto flash compensation' is displayed"
        photoInfoFormViewPanel.isAutoFlashCompensationPresent();

        and: "input for 'flash' is displayed"
        photoInfoFormViewPanel.isPhotoFlashPresent();

        and: "input for 'shutter time' is displayed"
        photoInfoFormViewPanel.isShutterTimePresent();

        and: "input for 'white balance' is displayed"
        photoInfoFormViewPanel.isWhiteBalancePresent();

        and: "input for 'exposure program' is displayed"
        photoInfoFormViewPanel.isExposureProgramPresent();

        and: "input for 'shooting mode' is displayed"
        photoInfoFormViewPanel.isShootingModePresent();

        and: "input for 'metering mode' is displayed"
        photoInfoFormViewPanel.isMeteringModePresent();

        and: "input for 'exposure mode' is displayed"
        photoInfoFormViewPanel.isExposureModePresent();

        and: "input for 'focus distance' is displayed"
        photoInfoFormViewPanel.isFocusDistancePresent();

        and: "input for 'orientation' is displayed"
        photoInfoFormViewPanel.isOrientationPresent();
    }
}
