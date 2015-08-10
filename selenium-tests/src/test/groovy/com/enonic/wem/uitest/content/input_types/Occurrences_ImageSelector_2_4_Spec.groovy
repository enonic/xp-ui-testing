package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Occurrences_ImageSelector_2_4_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String TEST_IMG_1 = "nord.jpg";

    @Shared
    String TEST_IMG_2 = "book.jpg";

    @Shared
    String TEST_IMG_3 = "man.jpg";

    @Shared
    String TEST_IMG_4 = "fl.jpg";

    @Shared
    Content TEST_IMAGE_SELECTOR_CONTENT;

    @Shared
    Content IMAGE_SELECTOR_CONTENT_4_IMAGES;

    def "WHEN wizard for adding a 'Image Selector'-content(2:4) opened THEN option filter input is present, there no selected image and upload button is enabled"()
    {
        when: "start to add a content with type 'Image Selector 2:4'"
        Content imageSelectorContent = buildImageSelector2_4_Content( null );
        selectSiteOpenWizard( imageSelectorContent.getContentTypeName() );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        then: "option filter input is present"
        formViewPanel.isOptionFilterIsDisplayed();
        and: "no one option selected"
        formViewPanel.getSelectedImages().size() == 0;
        and:
        formViewPanel.isUploaderButtonEnabled();
    }

    def "GIVEN saving of 'Image Selector-content' (2:4) without selected image WHEN content opened for edit THEN selected image not present on page and content is invalid"()
    {
        given: "new content with type Image Selector added'"
        Content imageSelectorContent = buildImageSelector2_4_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( imageSelectorContent.getContentTypeName() )
        wizard.typeData( imageSelectorContent ).save().close( imageSelectorContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( imageSelectorContent );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> imagesNames = formViewPanel.getSelectedImages();
        TestUtils.saveScreenshot( getSession(), "img2_4_invalid" )

        then: "no one options present in form view"
        imagesNames.size() == 0;

        and: "options filter input is displayed"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "just created content without images is invalid"
        wizard.isContentInvalid( imageSelectorContent.getDisplayName() );
    }

    def "GIVEN saving of 'Image Selector (2:4)' without required image WHEN content saved  THEN invalid content listed"()
    {
        when: "content without required image saved"
        Content imageSelectorContent = buildImageSelector2_4_Content( null );
        selectSiteOpenWizard( imageSelectorContent.getContentTypeName() ).typeData( imageSelectorContent ).save().close(
            imageSelectorContent.getDisplayName() );

        then:
        filterPanel.typeSearchText( imageSelectorContent.getDisplayName() );
        contentBrowsePanel.isContentInvalid( imageSelectorContent.getName() );
    }

    def "GIVEN saving of Image Selector-content (2:4) and 2 image selected WHEN content opened for edit THEN correct images present on page and option filter displayed"()
    {
        given: "new content with type 'Image Selector2_4' added"
        TEST_IMAGE_SELECTOR_CONTENT = buildImageSelector2_4_Content( TEST_IMG_1, TEST_IMG_2 );
        selectSiteOpenWizard( TEST_IMAGE_SELECTOR_CONTENT.getContentTypeName() ).typeData( TEST_IMAGE_SELECTOR_CONTENT ).save().close(
            TEST_IMAGE_SELECTOR_CONTENT.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> images = formViewPanel.getSelectedImages();

        then: "two images present on the page"
        images.size() == 2;
        and:
        formViewPanel.isOptionFilterIsDisplayed();

        and: "correct image present on the page"
        images.get( 0 ) == TEST_IMG_1;

        and: "correct image present on the page"
        images.get( 1 ) == TEST_IMG_2;
    }

    def "GIVEN Image Selector-content (2:4) with two selected images and one image removed and content saved WHEN content opened for edit THEN one image present on the page"()
    {
        given: "content with one required option opened for edit' and one option removed"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        formViewPanel.clickOnImage( TEST_IMG_1 ).clickOnRemoveButton();
        wizard.save().close( TEST_IMAGE_SELECTOR_CONTENT.getDisplayName() );

        when: "when content selected in the grid and opened for edit again"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );

        then: "one image present on the page "
        TestUtils.saveScreenshot( getSession(), "24remove_img" )
        formViewPanel.getSelectedImages().size() == 1;

        and: "content is invalid, because only one image present on page"
        wizard.isContentInvalid( TEST_IMAGE_SELECTOR_CONTENT.getDisplayName() );

        and: "'Publish button' is disabled now"
        !wizard.isPublishButtonEnabled();
    }

    def "WHEN content with 4 selected images saved and published THEN it content with 'Online'-status listed"()
    {
        when: "content with 4 selected images saved and published"
        IMAGE_SELECTOR_CONTENT_4_IMAGES = buildImageSelector2_4_Content( TEST_IMG_1, TEST_IMG_2, TEST_IMG_3, TEST_IMG_4 );
        selectSiteOpenWizard( IMAGE_SELECTOR_CONTENT_4_IMAGES.getContentTypeName() ).typeData(
            IMAGE_SELECTOR_CONTENT_4_IMAGES ).save().clickOnWizardPublishButton().clickOnPublishNowButton().waitPublishNotificationMessage(
            Application.EXPLICIT_NORMAL );
        ContentWizardPanel.getWizard( getSession() ).close( IMAGE_SELECTOR_CONTENT_4_IMAGES.getDisplayName() );
        filterPanel.typeSearchText( IMAGE_SELECTOR_CONTENT_4_IMAGES.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( IMAGE_SELECTOR_CONTENT_4_IMAGES.getName() ).equals( ContentStatus.ONLINE.getValue() );
    }

    def "WHEN content with 4 selected images opened THEN option filter should not be displayed"()
    {
        when: "content with four selected images opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( IMAGE_SELECTOR_CONTENT_4_IMAGES );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        TestUtils.saveScreenshot( getSession(), "img_sel_2_4" )

        then: "option filter should not be displayed"
        !formViewPanel.isOptionFilterIsDisplayed();
    }

    def "GIVEN content with 4 selected images opened WHEN one image removed THEN option filter appears"()
    {
        given: "content with four selected images opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( IMAGE_SELECTOR_CONTENT_4_IMAGES );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when:
        formViewPanel.clickOnImage( TEST_IMG_1 ).clickOnRemoveButton();
        TestUtils.saveScreenshot( getSession(), "img_sel_2_4_remove" )

        then: "option filter appears"
        formViewPanel.isOptionFilterIsDisplayed();
    }
}
