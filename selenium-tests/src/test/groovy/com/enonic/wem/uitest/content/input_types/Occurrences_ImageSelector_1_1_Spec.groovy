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
    String TEST_IMAGE_NAME = "nord.jpg";

    @Shared
    Content TEST_IMAGE_SELECTOR_CONTENT;

    def "WHEN wizard for adding a 'Image Selector'-content(1:1) opened THEN option filter input is present, there no selected image and upload button is enabled "()
    {
        when: "start to add a content with type 'Image Selector 1:1'"
        Content imageSelectorContent = buildImageSelector1_1_Content( null );
        selectSitePressNew( imageSelectorContent.getContentTypeName() );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        then: "option filter input is present"
        formViewPanel.isOptionFilterIsDisplayed();
        and: "no one option selected"
        formViewPanel.getSelectedImages().size() == 0;
        and:
        formViewPanel.isUploaderButtonEnabled();
    }

    def "GIVEN saving of 'Image Selector-content' (1:1) without selected image WHEN content opened for edit THEN selected image not present on page and content is invalid"()
    {
        given: "new content with type Image Selector added'"
        Content imageSelectorContent = buildImageSelector1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( imageSelectorContent.getContentTypeName() )
        wizard.typeData( imageSelectorContent ).save().close( imageSelectorContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( imageSelectorContent );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> imagesNames = formViewPanel.getSelectedImages();
        saveScreenshot( "img1_1_invalid" )

        then: "no one options present in form view"
        imagesNames.size() == 0;

        and: "options filter input is displayed"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "just created content without images is invalid"
        wizard.isContentInvalid( imageSelectorContent.getDisplayName() );
    }

    def "GIVEN saving of 'Image Selector (1:1)' without required image WHEN content saved  THEN invalid content listed"()
    {
        when: "content without required image saved"
        Content imageSelectorContent = buildImageSelector1_1_Content( null );
        selectSitePressNew( imageSelectorContent.getContentTypeName() ).typeData( imageSelectorContent ).save().close(
            imageSelectorContent.getDisplayName() );

        then:
        filterPanel.typeSearchText( imageSelectorContent.getDisplayName() );
        contentBrowsePanel.isContentInvalid( imageSelectorContent.getName() );
    }

    def "GIVEN saving of Image Selector-content (1:1) and image selected WHEN content opened for edit THEN correct image present on page and option filter not displayed"()
    {
        given: "new content with type 'Image Selector0_1' added"
        TEST_IMAGE_SELECTOR_CONTENT = buildImageSelector1_1_Content( TEST_IMAGE_NAME );
        selectSitePressNew( TEST_IMAGE_SELECTOR_CONTENT.getContentTypeName() ).typeData( TEST_IMAGE_SELECTOR_CONTENT ).save().close(
            TEST_IMAGE_SELECTOR_CONTENT.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> images = formViewPanel.getSelectedImages();

        then: "one image present on the page"
        images.size() == 1;
        and:
        !formViewPanel.isOptionFilterIsDisplayed();

        and: "correct image present on the page"
        images.get( 0 ) == TEST_IMAGE_NAME;

        and: "options filter input is not shown"
        !formViewPanel.isOptionFilterIsDisplayed();
    }

    def "GIVEN content opened for edit WHEN image clicked THEN buttons 'Edit' and 'Remove' appears"()
    {
        given: "'Image Selector' content with selected image opened for edit "
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "image clicked"
        formViewPanel.clickOnImage( TEST_IMAGE_NAME )
        then: "buttons 'Edit' and 'Remove' appears"
        formViewPanel.isRemoveButtonDisplayed();
        and:
        formViewPanel.isEditButtonDisplayed();
    }

    def "GIVEN content opened for edit WHEN image clicked  AND 'Remove' button clicked THEN image removed and 'options filter' input appears"()
    {
        given: "'Image Selector' content with selected image opened for edit "
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "image clicked and 'Remove' button pressed"
        formViewPanel.clickOnImage( TEST_IMAGE_NAME );
        formViewPanel.clickOnRemoveButton();

        then: "buttons 'Edit' and 'Remove' disappears"
        !formViewPanel.isRemoveButtonDisplayed();
        and:
        !formViewPanel.isEditButtonDisplayed();
        and: "option filter displayed"
        formViewPanel.isOptionFilterIsDisplayed();
        and:
        wizard.isContentInvalid( TEST_IMAGE_SELECTOR_CONTENT.getDisplayName() );
    }

    def "GIVEN saving of 'Image Selector 1:1' and content without selected image WHEN data typed AND image not selected THEN 'Publish' button on the wizard-toolbar is disabled"()
    {
        given: "new content with type 'Image Selector'"
        Content imageSelectorContent = buildImageSelector1_1_Content( null );

        when: "data typed AND image not selected"
        ContentWizardPanel wizard = selectSitePressNew( imageSelectorContent.getContentTypeName() ).typeData( imageSelectorContent );

        then: "'Publish' button on the wizard-toolbar is disabled"
        !wizard.isPublishButtonEnabled();
    }

}
