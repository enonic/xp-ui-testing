package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImageInfoFormViewPanel
import spock.lang.Shared

/**
 * XP-4136 Add selenium tests for Image Info (Content Wizard)
 * Created  on 22.09.2016.
 * */
class ImageProperties_Wizard_Spec
    extends BaseContentSpec
{
    @Shared
    String TEST_DESCRIPTION = "test description";

    def "WHEN image content opened THEN all control elements are present"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEdit();

        when: "'Image Info' step was clicked"
        wizard.clickOnWizardStep( "Properties" );
        ImageInfoFormViewPanel imageInfoFormViewPanel = new ImageInfoFormViewPanel( getSession() );

        then: "input for size in pixels is present"
        imageInfoFormViewPanel.isInputForSizeInPixelsDisplayed();

        and: "input for height is displayed"
        imageInfoFormViewPanel.isInputForImageHeightDisplayed();

        and: "input for width is displayed"
        imageInfoFormViewPanel.isInputForImageWidthDisplayed();

        and: "input for description is displayed"
        imageInfoFormViewPanel.isInputForImageDescriptionDisplayed();

        and: "input for image type is displayed"
        imageInfoFormViewPanel.isInputForImageTypeDisplayed();

        and: "input for file source is displayed"
        imageInfoFormViewPanel.isInputForFileSourceDisplayed();

        and: "input for color space is displayed"
        imageInfoFormViewPanel.isInputForColorSpaceDisplayed();

        and: "input for size in bytes is displayed"
        imageInfoFormViewPanel.isInputForSizeInBytesDisplayed();
    }

    def "WHEN description text typed AND save button pressed THEN description for image correctly saved"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEdit();

        when: "new description typed"
        wizard.clickOnWizardStep( "Photo" );
        ImageInfoFormViewPanel imageInfoFormViewPanel = new ImageInfoFormViewPanel( getSession() );
        imageInfoFormViewPanel.typeDescription( TEST_DESCRIPTION );

        and: "save button pressed and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: " image opened again"
        contentBrowsePanel.clickToolbarEdit();
        wizard.clickOnWizardStep( "Photo" );

        then: "correct description displayed on the page"
        imageInfoFormViewPanel.getDescription() == TEST_DESCRIPTION;
    }
}
