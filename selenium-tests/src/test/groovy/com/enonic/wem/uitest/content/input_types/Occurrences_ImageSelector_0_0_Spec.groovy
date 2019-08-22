package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.SettingsWizardStepForm
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Verifies:
 * -XP-4863 Content Wizard - Mod+S won't save content when image selector has focus
 * -Path-search in selectors doesn't work #4786
 * */
@Stepwise
class Occurrences_ImageSelector_0_0_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content TEST_IMAGE_SELECTOR_CONTENT;

    //verifies the "Path-search in selectors doesn't work #4786'
    def "GIVEN wizard for Image Selector-content (0:0) is opened WHEN path to an image has been typed THEN the image should be filtered "()
    {
        given: "wizard for Image Selector-content (0:0) is opened"
        Content imageSelectorContent = buildImageSelector0_0_Content( NORD_IMAGE_DISPLAY_NAME );
        selectSitePressNew( imageSelectorContent.getContentTypeName() );
        String pathToImage = "/all-content-types-images/" + NORD_IMAGE_NAME;

        when: "path to an image has been typed"
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        formViewPanel.selectOptionByPath( pathToImage, NORD_IMAGE_DISPLAY_NAME );

        then: "two images should be present on the wizard page"
        List<String> images = formViewPanel.getSelectedImages();
        images.size() == 1;

        and: "display name of the image should be present on the form panel"
        images.get( 0 ) == NORD_IMAGE_DISPLAY_NAME;
    }

    def "GIVEN wizard for Image Selector-content (0:0) is opened WHEN display name has been typed THEN red icon should not be displayed on the wizard page"()
    {
        given: "wizard for Image Selector-content (0:0) is opened"
        Content imageSelectorContent = buildImageSelector0_0_Content( NORD_IMAGE_DISPLAY_NAME );
        ContentWizardPanel wizard = selectSitePressNew( imageSelectorContent.getContentTypeName() );

        when: "display name has been typed"
        wizard.typeDisplayName( imageSelectorContent.getDisplayName() );

        then: "red icon should not be displayed on the wizard page, because the input is not required"
        !wizard.isContentInvalid();
    }

    def "GIVEN Image Selector-content (0:0) with two images has been added WHEN content is opened THEN correct images should be present on the page "()
    {
        given: "Image Selector-content (0:0) with two images has been added"
        Content imageSelectorContent = buildImageSelector0_0_Content( NORD_IMAGE_DISPLAY_NAME, BOOK_IMAGE_DISPLAY_NAME );
        selectSitePressNew( imageSelectorContent.getContentTypeName() ).typeData(
            imageSelectorContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( imageSelectorContent );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> images = formViewPanel.getSelectedImages();

        then: "two images should be present on the wizard page"
        images.size() == 2;
        and:
        formViewPanel.isOptionFilterIsDisplayed();

        and: "expected images should be present"
        images.get( 0 ) == NORD_IMAGE_DISPLAY_NAME;
        and:
        images.get( 1 ) == BOOK_IMAGE_DISPLAY_NAME;
    }

    def "GIVEN Image Selector-content (0:0) with four images has been added WHEN content opened for edit THEN correct images should be present on the page"()
    {
        given: "Image Selector-content (0:0) with four images has been added"
        TEST_IMAGE_SELECTOR_CONTENT =
            buildImageSelector0_0_Content( NORD_IMAGE_DISPLAY_NAME, BOOK_IMAGE_DISPLAY_NAME, MAN_IMAGE_DISPLAY_NAME,
                                           FL_IMAGE_DISPLAY_NAME );
        selectSitePressNew( TEST_IMAGE_SELECTOR_CONTENT.getContentTypeName() ).typeData(
            TEST_IMAGE_SELECTOR_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> images = formViewPanel.getSelectedImages();
        saveScreenshot( "img_sel_content0_0_4" )

        then: "four images should be present on the wizard page"
        images.size() == 4;
        and:
        formViewPanel.isOptionFilterIsDisplayed();

        and: "expected images are present"
        images.get( 0 ) == NORD_IMAGE_DISPLAY_NAME;
        and:
        images.get( 1 ) == BOOK_IMAGE_DISPLAY_NAME;
        and:
        images.get( 2 ) == MAN_IMAGE_DISPLAY_NAME;
        and:
        images.get( 3 ) == FL_IMAGE_DISPLAY_NAME;
    }

    def "GIVEN content with an image-selector is opened WHEN one of the images clicked THEN buttons 'Edit' and 'Remove' appears"()
    {
        given: "content with an image-selector is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "image has been clicked"
        formViewPanel.clickOnImage( MAN_IMAGE_DISPLAY_NAME );
        saveScreenshot( "img_selector_image_clicked" );

        then: "buttons 'Edit' and 'Remove' should be displayed"
        formViewPanel.isRemoveButtonDisplayed();
        and:
        formViewPanel.isEditButtonDisplayed();
    }
    //Verifies XP-4863 Content Wizard - Mod+S won't save content when image selector has focus
    def "GIVEN existing content is opened WHEN image has been clicked AND keyboard shortcut to 'Save' is pressed THEN expected notification message should appear"()
    {
        given: "content with a image-selector is opened(4 images is selected)"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        SettingsWizardStepForm settingsWizardStepForm = new SettingsWizardStepForm( getSession() );

        when: "the image has been clicked"
        settingsWizardStepForm.selectLanguage( "English (en)" );
        formViewPanel.clickOnImage( MAN_IMAGE_DISPLAY_NAME );
        and: "keyboard shortcut to 'Save' is pressed"
        wizard.pressSaveKeyboardShortcut();
        String expectedMessage = String.format( Application.CONTENT_SAVED, TEST_IMAGE_SELECTOR_CONTENT.getName() );

        then: "expected notification message should appear"
        wizard.waitExpectedNotificationMessage( expectedMessage, Application.EXPLICIT_NORMAL );
    }

    def "GIVEN content with an image-selector is opened WHEN checkbox for one of the images was clicked THEN label for button 'Remove' has a correct number"()
    {
        given: "content with an image-selector is opened(4 images is selected)"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "the checkbox near the image has been clicked"
        formViewPanel.clickOnCheckboxAndSelectImage( MAN_IMAGE_DISPLAY_NAME );

        then: "label for button 'Remove' has a correct number"
        formViewPanel.getNumberFromRemoveButton() == 1;
    }

    def "GIVEN content with 4 images is opened WHEN one of the images was selected and 'Remove' button pressed THEN number of images should be reduced"()
    {
        given: "content with 4 images is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "one of the images was selected and 'Remove' button pressed"
        formViewPanel.clickOnImage( MAN_IMAGE_DISPLAY_NAME );
        formViewPanel.clickOnRemoveButton();
        saveScreenshot( "img_sel_0_0_one_removed" );

        then: "number of images should be reduced"
        formViewPanel.getSelectedImages().size() == 3;
    }

    def "GIVEN saving of 'Image Selector 0:0' and content without selected image WHEN data typed AND image not selected THEN 'Publish' button on the wizard-toolbar is enabled"()
    {
        given: "new content with type 'Image Selector'"
        Content imageSelectorContent = buildImageSelector0_0_Content( null );

        when: "data typed AND image not selected"
        ContentWizardPanel wizard = selectSitePressNew( imageSelectorContent.getContentTypeName() ).typeData( imageSelectorContent );

        then: "'Publish' menu item should be enabled, because image-inputs are not required"
        wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );

        and: "red icon should not be displayed on the wizard page"
        !wizard.isContentInvalid();
    }

    def "GIVEN creating of new content(0,0) AND no one image was selected WHEN 'Publish' button was pressed THEN the content should be with 'online' status"()
    {
        given: "creating of new content AND no one image was selected"
        Content imageSelectorContent = buildImageSelector0_0_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( imageSelectorContent.getContentTypeName() ).typeData( imageSelectorContent ).save();
        and: "Publish button has been pressed"
        wizard.clickOnWizardPublishButton().clickOnPublishButton();
        String publishedMessage = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        and: "wizard tab was closed"
        wizard.closeBrowserTab().switchToBrowsePanelTab();

        when: "the name of the content was typed in the search input"
        filterPanel.typeSearchText( imageSelectorContent.getName() );

        then: "the content should be with 'Published' status"
        contentBrowsePanel.getContentStatus( imageSelectorContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
        and: "the content is valid, because images are not required for this content"
        !contentBrowsePanel.isContentInvalid( imageSelectorContent.getName().toString() );
        and: "correct notification message was displayed"
        publishedMessage == String.format( Application.ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE_TMP, imageSelectorContent.getName() );
    }

    def "GIVEN 'Image Selector 0:0' content with one image was saved WHEN 'Publish' button pressed THEN the content with 'Online' status should be listed"()
    {
        given: "new content with type 'Image Selector 0:0'"
        Content imageSelectorContent = buildImageSelector0_0_Content( BOOK_IMAGE_DISPLAY_NAME );
        ContentWizardPanel wizard = selectSitePressNew( imageSelectorContent.getContentTypeName() ).typeData( imageSelectorContent ).save();

        when: "the content has been published(from the wizard)"
        wizard.clickOnWizardPublishButton().clickOnPublishButton();
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        and: "name of the content is typed in the search input"
        filterPanel.typeSearchText( imageSelectorContent.getName() );

        then: "the content with 'Published' status should be listed"
        contentBrowsePanel.getContentStatus( imageSelectorContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );

    }
}
