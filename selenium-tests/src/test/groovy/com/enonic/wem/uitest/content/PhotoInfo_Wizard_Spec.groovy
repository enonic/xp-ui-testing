package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.PhotoInfoFormViewPanel
import spock.lang.Shared

/**
 * Created on 23.09.2016.*/
class PhotoInfo_Wizard_Spec
    extends BaseContentSpec
{
    @Shared
    String TEST_DESCRIPTION = "test description";

    def "WHEN image content opened THEN all control elements are present"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();

        when: "'Image Info' step was clicked"
        wizard.clickOnWizardStep( "Photo Info" );
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

    }
}
