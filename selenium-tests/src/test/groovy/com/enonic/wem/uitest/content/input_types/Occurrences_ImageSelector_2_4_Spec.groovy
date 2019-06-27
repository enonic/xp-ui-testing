package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Occurrences_ImageSelector_2_4_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    Content TEST_IMAGE_SELECTOR_CONTENT;

    @Shared
    Content IMAGE_SELECTOR_CONTENT_4_IMAGES;

    def "WHEN wizard for 'Image Selector'-content(2:4) is opened THEN option filter input should be present, there no selected image and upload button should be enabled"()
    {
        when: "wizard for 'Image Selector'-content(2:4) is opened and image is not selected"
        Content imageSelectorContent = buildImageSelector2_4_Content( null );
        selectSitePressNew( imageSelectorContent.getContentTypeName() );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        then: "option filter input should be present"
        formViewPanel.isOptionFilterIsDisplayed();
        and: "no one option is selected"
        formViewPanel.getSelectedImages().size() == 0;
        and: "upload button should be enabled"
        formViewPanel.isUploaderButtonEnabled();
    }

    def "GIVEN saving of 'Image Selector-content' (2:4) without a selected image WHEN content opened for edit THEN selected image not present on page and content is invalid"()
    {
        given: "new content with type Image Selector added'"
        Content imageSelectorContent = buildImageSelector2_4_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( imageSelectorContent.getContentTypeName() )
        wizard.typeData( imageSelectorContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( imageSelectorContent );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> imagesNames = formViewPanel.getSelectedImages();
        saveScreenshot( "img2_4_invalid" )

        then: "no one options present in form view"
        imagesNames.size() == 0;

        and: "options filter input should be displayed"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "red icon should be present on the wizard page"
        wizard.isContentInvalid();
    }

    def "WHEN 'Image Selector (2:4)' without required image has been added THEN content should be displayed with red circle"()
    {
        when: "content without required images was added"
        Content imageSelectorContent = buildImageSelector2_4_Content( null );
        selectSitePressNew( imageSelectorContent.getContentTypeName() ).typeData(
            imageSelectorContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        then: "content should be displayed with red circle"
        filterPanel.typeSearchText( imageSelectorContent.getDisplayName() );
        contentBrowsePanel.isContentInvalid( imageSelectorContent.getName() );
    }

    def "GIVEN 'Image Selector (2:4)' with 2 images has been added WHEN content is opened THEN correct images present on page and option filter should be displayed"()
    {
        given: "new content with type 'Image Selector2_4' added"
        TEST_IMAGE_SELECTOR_CONTENT = buildImageSelector2_4_Content( NORD_IMAGE_DISPLAY_NAME, BOOK_IMAGE_DISPLAY_NAME );
        selectSitePressNew( TEST_IMAGE_SELECTOR_CONTENT.getContentTypeName() ).typeData(
            TEST_IMAGE_SELECTOR_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        sleep( 1000 );
        saveScreenshot( "image-selector-2-img" )
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> imagesActual = formViewPanel.getSelectedImages();

        then: "two images should be present on the page"
        imagesActual.size() == 2;
        and:
        formViewPanel.isOptionFilterIsDisplayed();

        and: "expected images should be present on the page"
        imagesActual.get( 0 ) == NORD_IMAGE_DISPLAY_NAME;

        and:
        imagesActual.get( 1 ) == BOOK_IMAGE_DISPLAY_NAME;
    }

    def "GIVEN existing Image Selector-content  with two selected images AND one image was removed WHEN content is opened THEN one image should be present on the page"()
    {
        given: "content with one required option opened for edit' and one option removed"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        formViewPanel.clickOnImage( NORD_IMAGE_DISPLAY_NAME ).clickOnRemoveButton();
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "when content selected in the grid and opened for edit again"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );

        then: "one image present on the page "
        saveScreenshot( "24remove_img" )
        formViewPanel.getSelectedImages().size() == 1;

        and: "content is invalid, because only one image present on page"
        formViewPanel.isValidationMessagePresent();

        and: "'Publish button' is disabled now"
        !wizard.isPublishButtonEnabled();

        and: "red circle should be displayed on the wizard page"
        wizard.isContentInvalid();
    }

    def "WHEN content with 4 images has been added and published THEN the content should be 'Online'"()
    {
        when: "content with 4 selected images saved and published"
        IMAGE_SELECTOR_CONTENT_4_IMAGES =
            buildImageSelector2_4_Content( NORD_IMAGE_DISPLAY_NAME, BOOK_IMAGE_DISPLAY_NAME, MAN_IMAGE_DISPLAY_NAME,
                                           FL_IMAGE_DISPLAY_NAME );
        ContentWizardPanel wizard = selectSitePressNew( IMAGE_SELECTOR_CONTENT_4_IMAGES.getContentTypeName() ).typeData(
            IMAGE_SELECTOR_CONTENT_4_IMAGES ).save();
        wizard.clickOnWizardPublishButton().clickOnPublishNowButton();
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );

        and: "wizard has been closed"
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( IMAGE_SELECTOR_CONTENT_4_IMAGES.getName() );

        then: " the content should be 'Published'"
        contentBrowsePanel.getContentStatus( IMAGE_SELECTOR_CONTENT_4_IMAGES.getName() ).equalsIgnoreCase(
            ContentStatus.PUBLISHED.getValue() );
    }

    def "WHEN content with 4 selected images opened THEN option filter should not be displayed"()
    {
        when: "content with four selected images opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( IMAGE_SELECTOR_CONTENT_4_IMAGES );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        saveScreenshot( "img_sel_2_4" )

        then: "option filter should not be displayed"
        !formViewPanel.isOptionFilterIsDisplayed();
    }

    def "GIVEN content with 4 selected images opened WHEN one image removed THEN option filter should appear"()
    {
        given: "content with four selected images opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( IMAGE_SELECTOR_CONTENT_4_IMAGES );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "the image has been removed"
        formViewPanel.clickOnImage( NORD_IMAGE_DISPLAY_NAME ).clickOnRemoveButton();
        saveScreenshot( "img_sel_2_4_remove" )

        then: "option filter should appear"
        formViewPanel.isOptionFilterIsDisplayed();
    }
}
