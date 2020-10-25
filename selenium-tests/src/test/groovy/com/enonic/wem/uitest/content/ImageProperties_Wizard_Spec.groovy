package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImagePropertiesFormViewPanel
import spock.lang.Shared

/**
 * Created  on 22.09.2016.
 * */
class ImageProperties_Wizard_Spec
    extends BaseContentSpec
{
    @Shared
    String TEST_DESCRIPTION = "test description";

    def "WHEN image content is opened THEN expected inputs should be present in Properties form"()
    {
        given: "image content is opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEdit();

        when: "'Image Info' step has been clicked"
        wizard.clickOnWizardStep( "Properties" );
        ImagePropertiesFormViewPanel imagePropertiesFormViewPanel = new ImagePropertiesFormViewPanel( getSession() );

        then: "input for size in pixels should be present"
        imagePropertiesFormViewPanel.isInputForSizeInPixelsDisplayed();

        and: "input for height is displayed"
        imagePropertiesFormViewPanel.isInputForImageHeightDisplayed();

        and: "input for width is displayed"
        imagePropertiesFormViewPanel.isInputForImageWidthDisplayed();

        and: "input for description is displayed"
        imagePropertiesFormViewPanel.isInputForImageDescriptionDisplayed();

        and: "input for image type is displayed"
        imagePropertiesFormViewPanel.isInputForImageTypeDisplayed();

        and: "input for file source is displayed"
        imagePropertiesFormViewPanel.isInputForFileSourceDisplayed();

        and: "input for color space is displayed"
        imagePropertiesFormViewPanel.isInputForColorSpaceDisplayed();

        and: "input for size in bytes is displayed"
        imagePropertiesFormViewPanel.isInputForSizeInBytesDisplayed();
    }

    def "WHEN description(properties) text has been saved THEN expected description should be displayed"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEdit();

        when: "new description typed"
        wizard.clickOnWizardStep( "Properties" );
        ImagePropertiesFormViewPanel imagePropertiesFormViewPanel = new ImagePropertiesFormViewPanel( getSession() );
        imagePropertiesFormViewPanel.typeDescription( TEST_DESCRIPTION );

        and: "save button pressed and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: " image opened again"
        contentBrowsePanel.clickToolbarEdit();
        wizard.clickOnWizardStep( "Properties" );

        then: "correct description displayed on the page"
        imagePropertiesFormViewPanel.getDescription() == TEST_DESCRIPTION;
    }
}
