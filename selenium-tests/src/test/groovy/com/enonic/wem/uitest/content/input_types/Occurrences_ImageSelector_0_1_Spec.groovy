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

    def "WHEN wizard for 'Image Selector 0:1' content is opened THEN option filter input should be present, no one images selected and upload button is enabled "() {
        when: "wizard for 'Image Selector 0:1' content is opened"
        Content imageSelectorContent = buildImageSelector0_1_Content(NORD_IMAGE_DISPLAY_NAME);
        ContentWizardPanel wizard = selectSitePressNew(imageSelectorContent.getContentTypeName());
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel(getSession());
        wizard.typeDisplayName(imageSelectorContent.getDisplayName());

        then: "'options filter' input should be displayed"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "no one option is selected"
        formViewPanel.getSelectedImages().size() == 0;

        and: "upload button should be enabled"
        formViewPanel.isUploaderButtonEnabled();

        and: "the content should be valid, because an image is not required"
        !wizard.isContentInvalid();
    }

    def "GIVEN existing 'Image Selector-content' (0:1) AND options were not selected WHEN content opened for edit THEN image not present on the page and content should be valid "() {
        given: "existing 'Image Selector-content' (0:1) AND options were not selected"
        Content imageSelectorContent = buildImageSelector0_1_Content(null);
        ContentWizardPanel wizard = selectSitePressNew(imageSelectorContent.getContentTypeName())
        wizard.typeData(imageSelectorContent).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu(imageSelectorContent);
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel(getSession());
        List<String> imagesNames = formViewPanel.getSelectedImages();

        then: "image not displayed on the wizard page"
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

    def "GIVEN saving of content with not required 'Image Selector' AND option is not selected WHEN 'Publish' button pressed THEN status of the content is getting 'Online'"() {
        given: "creating of content with type 'Image Selector'"
        Content imageSelectorContent = buildImageSelector0_1_Content(null);
        ContentWizardPanel wizard = selectSitePressNew(imageSelectorContent.getContentTypeName());
        and: "data has been typed and the content published"
        wizard.typeData( imageSelectorContent ).clickOnMarkAsReadyAndDoPublish(  );
        String publishedMessage = contentBrowsePanel.waitPublishNotificationMessage(Application.EXPLICIT_NORMAL);
        and: "wizard was closed"
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

    def "GIVEN saving of Image Selector-content (0:1) and one image was selected WHEN content is opened THEN correct image should be present on the page and option filter not displayed"() {
        given: "new content with type 'Image Selector0_1' was added"
        TEST_IMAGE_SELECTOR_CONTENT = buildImageSelector0_1_Content(NORD_IMAGE_DISPLAY_NAME);
        selectSitePressNew(TEST_IMAGE_SELECTOR_CONTENT.getContentTypeName()).typeData(
                TEST_IMAGE_SELECTOR_CONTENT).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content was opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu(TEST_IMAGE_SELECTOR_CONTENT);
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel(getSession());
        List<String> images = formViewPanel.getSelectedImages();

        then: "one image should be present on the page"
        images.size() == 1;

        and: "option filter should not be displayed"
        !formViewPanel.isOptionFilterIsDisplayed();

        and: "image with correct name should be present on the page"
        images.get(0) == NORD_IMAGE_DISPLAY_NAME;
    }

    def "GIVEN content with selected option is opened WHEN images was clicked THEN buttons 'Edit' and 'Remove' should appear"() {
        given: "'Image Selector'-content with selected image is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu(TEST_IMAGE_SELECTOR_CONTENT);
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel(getSession());

        when: "image was clicked"
        formViewPanel.clickOnImage(NORD_IMAGE_DISPLAY_NAME)

        then: "buttons 'Edit' and 'Remove' should appear"
        formViewPanel.isRemoveButtonDisplayed();

        and: "Edit button should be displayed"
        formViewPanel.isEditButtonDisplayed();
    }

    def "GIVEN content with selected option is opened WHEN image was clicked  AND 'Remove' button pressed THEN image should be removed and 'options filter' input should be displayed"() {
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
        and: "option filter should be displayed"
        formViewPanel.isOptionFilterIsDisplayed();
    }
}
