package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_ImageSelector_1_1_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content TEST_IMAGE_SELECTOR_CONTENT;

    def "WHEN wizard for 'Image Selector'-content(1:1) is opened THEN option filter input should be present, there no selected options and upload button should be enabled"()
    {
        when: "wizard is opened:"
        Content imageSelectorContent = buildImageSelector1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( imageSelectorContent.getContentTypeName() );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        wizard.typeDisplayName( imageSelectorContent.getDisplayName() );

        then: "'option filter' input should be present"
        formViewPanel.isOptionFilterIsDisplayed();
        and: "no one option is selected"
        formViewPanel.getSelectedImages().size() == 0;
        and: "upload button should be enabled"
        formViewPanel.isUploaderButtonEnabled();

        and: "the content is not valid, because an image is required"
        wizard.isContentInvalid();
    }

    def "GIVEN 'Image Selector-content' (1:1) has been added (without image) WHEN content has been reopened THEN selected image should not present in the page and the content is invalid"()
    {
        given: "'Image Selector-content' (1:1) has been added (image was not selected)"
        Content imageSelectorContent = buildImageSelector1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( imageSelectorContent.getContentTypeName() )
        and: "required image was not selected and the content was saved"
        wizard.typeData( imageSelectorContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the content is reopened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( imageSelectorContent );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> imagesNames = formViewPanel.getSelectedImages();
        saveScreenshot( "img1_1_invalid" )

        then: "selected options should not be present on the form view"
        imagesNames.size() == 0;

        and: "'options filter' input should be displayed"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "validation warning should be displayed, because the required image was not selcted"
        formViewPanel.isValidationMessagePresent();

        and: "'Publish' button on the wizard-toolbar should be disabled"
        !wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );

        and: "the content is not valid, because an image is required"
        wizard.isContentInvalid();
    }

    def "GIVEN saving of 'Image Selector (1:1)' without required image WHEN content saved  THEN content should be displayed with the red circle"()
    {
        when: "saving of 'Image Selector-content' (1:1) without a selected image"
        Content imageSelectorContent = buildImageSelector1_1_Content( null );
        selectSitePressNew( imageSelectorContent.getContentTypeName() ).typeData(
            imageSelectorContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        then: "content should be displayed with the red circle"
        filterPanel.typeSearchText( imageSelectorContent.getDisplayName() );
        contentBrowsePanel.isContentInvalid( imageSelectorContent.getName() );
    }

    def "GIVEN Image Selector-content (1:1) with an image has been added WHEN the content is opened THEN correct image should be present on the page and option filter should not be displayed"()
    {
        given: "Image Selector-content (1:1) content has been added(without an image)"
        TEST_IMAGE_SELECTOR_CONTENT = buildImageSelector1_1_Content( NORD_IMAGE_DISPLAY_NAME );
        selectSitePressNew( TEST_IMAGE_SELECTOR_CONTENT.getContentTypeName() ).typeData(
            TEST_IMAGE_SELECTOR_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> images = formViewPanel.getSelectedImages();

        then: "one image should be present"
        images.size() == 1;

        and: "option filter should not be displayed"
        !formViewPanel.isOptionFilterIsDisplayed();

        and: "correct image is present on the page"
        images.get( 0 ) == NORD_IMAGE_DISPLAY_NAME;
    }

    def "GIVEN 'Image Selector' content (with an image) is opened WHEN the image has been clicked THEN buttons 'Edit' and 'Remove' should appear"()
    {
        given: "'Image Selector' content (with an image) is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "the image has been clicked"
        formViewPanel.clickOnImage( NORD_IMAGE_DISPLAY_NAME )

        then: "buttons 'Edit' and 'Remove' should appear"
        formViewPanel.isRemoveButtonDisplayed();
        and:
        formViewPanel.isEditButtonDisplayed();
    }

    def "GIVEN 'Image Selector' content (with an image) is opened WHEN the image was clicked  AND 'Remove' button clicked THEN image removed and 'options filter' input should appear"()
    {
        given: "'Image Selector' content (with an image) is opened"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "image was clicked and 'Remove' button pressed"
        formViewPanel.clickOnImage( NORD_IMAGE_DISPLAY_NAME );
        formViewPanel.clickOnRemoveButton();

        then: "buttons 'Edit' and 'Remove' is getting hidden"
        !formViewPanel.isRemoveButtonDisplayed();
        and:
        !formViewPanel.isEditButtonDisplayed();

        and: "option filter should appear"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "validation warning should appear, because the image was removed and the input is required"
        formViewPanel.isValidationMessagePresent();
        and: "red icon should be displayed on the wizard page"
        wizard.isContentInvalid();

    }
}
