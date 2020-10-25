package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_ImageSelector_0_1_Spec
        extends Base_InputFields_Occurrences {
    @Shared
    Content TEST_IMAGE_SELECTOR_CONTENT;

    def "WHEN new wizard for 'Image Selector 0:1' is opened THEN there are no selected options and upload button should be enabled"() {
        when: "wizard has been opened:"
        Content imageSelectorContent = buildImageSelector0_1_Content(NORD_IMAGE_DISPLAY_NAME);
        ContentWizardPanel wizard = selectSitePressNew(imageSelectorContent.getContentTypeName());
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel(getSession());
        wizard.typeDisplayName(imageSelectorContent.getDisplayName());

        then: "'options filter' input should be displayed"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "no one option should be selected"
        formViewPanel.getSelectedImages().size() == 0;

        and: "upload button should be enabled"
        formViewPanel.isUploaderButtonEnabled();

        and: "the content should be valid, because an image is not required"
        !wizard.isContentInvalid();
    }

    def "GIVEN new 'Image Selector-content' (0:1) is saved (no images were selected) WHEN content has been reopened THEN options are not selected in the page and content should be valid"() {
        given: "new content is saved 'Image Selector-content' (0:1) AND options were not selected"
        Content imageSelectorContent = buildImageSelector0_1_Content(null);
        ContentWizardPanel wizard = selectSitePressNew(imageSelectorContent.getContentTypeName())
        wizard.typeData(imageSelectorContent).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu(imageSelectorContent);
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel(getSession());
        List<String> imagesNames = formViewPanel.getSelectedImages();

        then: "image should not be selected in the page"
        imagesNames.size() == 0;

        and: "options filter input should be displayed"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "just created content is valid, because an image is not required"
        !formViewPanel.isValidationMessagePresent();

        and: "'Publish' menu item should be enabled"
        wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );

        and: "the content should be valid, because an image is not required"
        !wizard.isContentInvalid();
    }

    def "GIVEN new content with image selector is saved WHEN 'Publish'(in wizard) button pressed THEN the content gets 'Published'"() {
        given: "new content with image selector is saved"
        Content imageSelectorContent = buildImageSelector0_1_Content(null);
        ContentWizardPanel wizard = selectSitePressNew(imageSelectorContent.getContentTypeName());
        and: "data has been typed and the content published"
        wizard.typeData( imageSelectorContent ).clickOnMarkAsReadyAndDoPublish(  );
        sleep(400);
        String publishedMessage = contentBrowsePanel.waitPublishNotificationMessage(Application.EXPLICIT_NORMAL);
        and: "wizard has been closed"
        wizard.close(imageSelectorContent.getDisplayName());

        when: "name of the content is typed in the search input"
        filterPanel.typeSearchText(imageSelectorContent.getName());

        then: "content should be with 'Published' status"
        contentBrowsePanel.getContentStatus(imageSelectorContent.getName()).equalsIgnoreCase(ContentStatus.PUBLISHED.getValue());

        and: "content is valid"
        !contentBrowsePanel.isContentInvalid(imageSelectorContent.getName().toString());

        and: "correct notification message should be displayed"
        publishedMessage == String.format( Application.ITEM_IS_PUBLISHED_NOTIFICATION_MESSAGE, imageSelectorContent.getName() );
    }

    def "GIVEN Image Selector-content (0:1) with selected image is saved WHEN content has been reopened THEN expected image should be present in the page and option filter should not be displayed"() {
        given: "new content with type 'Image Selector0_1' is added"
        TEST_IMAGE_SELECTOR_CONTENT = buildImageSelector0_1_Content(NORD_IMAGE_DISPLAY_NAME);
        selectSitePressNew(TEST_IMAGE_SELECTOR_CONTENT.getContentTypeName()).typeData(
                TEST_IMAGE_SELECTOR_CONTENT).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been reopened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu(TEST_IMAGE_SELECTOR_CONTENT);
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel(getSession());
        List<String> images = formViewPanel.getSelectedImages();

        then: "one image should be present on the page"
        images.size() == 1;

        and: "option filter should not be displayed"
        !formViewPanel.isOptionFilterIsDisplayed();

        and: "image with expected name should be present in the page"
        images.get(0) == NORD_IMAGE_DISPLAY_NAME;
    }

    def "GIVEN content with image selector is opened WHEN image in wizard has been clicked THEN buttons 'Edit' and 'Remove' should appear in the selected option"()
    {
        given: "'Image Selector'-content with selected image is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu(TEST_IMAGE_SELECTOR_CONTENT);
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel(getSession());

        when: "image has been clicked"
        formViewPanel.clickOnImage( NORD_IMAGE_DISPLAY_NAME );
        saveScreenshot( "img_sel_0_1_edit_button" );

        then: "buttons 'Edit' and 'Remove' should appear"
        formViewPanel.isRemoveButtonDisplayed();

        and: "Edit button should be displayed"
        formViewPanel.isEditButtonDisplayed();
    }

    def "GIVEN content with image selector is opened WHEN image has been removed in selected option THEN image should be removed and 'options filter' input should appear"()
    {
        given: "'Image Selector' content with selected image is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu(TEST_IMAGE_SELECTOR_CONTENT);
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel(getSession());

        when: "image was clicked and 'Remove' button pressed"
        formViewPanel.clickOnImage(NORD_IMAGE_DISPLAY_NAME);
        formViewPanel.clickOnRemoveButton();

        then: "buttons 'Edit' and 'Remove' should not be displayed"
        !formViewPanel.isRemoveButtonDisplayed();
        and:
        !formViewPanel.isEditButtonDisplayed();
        and: "option filter should appear"
        formViewPanel.isOptionFilterIsDisplayed();
    }
}
