package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_ImageSelector_0_1_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String TEST_IMAGE_NAME = "nord.jpg";

    @Shared
    Content TEST_IMAGE_SELECTOR_CONTENT;

    def "WHEN wizard for adding a 'Image Selector'-content(0:1) opened THEN option filter input is present, no one images selected and upload button is enabled "()
    {
        when: "start to add a content with type 'Image Selector 0:1'"
        Content imageSelectorContent = buildImageSelector0_1_Content( TEST_IMAGE_NAME );
        selectSiteOpenWizard( imageSelectorContent.getContentTypeName() );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        then: "option filter input is present"
        formViewPanel.isOptionFilterIsDisplayed();
        and: "no one option selected"
        formViewPanel.getSelectedImages().size() == 0;
        and:
        formViewPanel.isUploaderButtonEnabled();
    }

    def "GIVEN saving of 'Image Selector-content' (0:1) without options WHEN content opened for edit THEN image not present on the page and content is valid "()
    {
        given: "new content with type Image Selector added'"
        Content imageSelectorContent = buildImageSelector0_1_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( imageSelectorContent.getContentTypeName() )
        wizard.typeData( imageSelectorContent ).save().close(
            imageSelectorContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( imageSelectorContent );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> imagesNames = formViewPanel.getSelectedImages();

        then: "image not present on the wizard page"
        imagesNames.size() == 0;

        and: "options filter input is displayed"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "just created content without image is valid"
        !wizard.isContentInvalid( imageSelectorContent.getDisplayName() );
    }

    def "GIVEN saving of not required 'Image Selector' content without selected option WHEN 'Publish' button pressed THEN valid content with 'Online' status listed"()
    {
        given: "new content with type 'Image Selector'"
        Content imageSelectorContent = buildImageSelector0_1_Content( null );
        selectSiteOpenWizard( imageSelectorContent.getContentTypeName() ).typeData(
            imageSelectorContent ).save().clickOnPublishButton().close( imageSelectorContent.getDisplayName() );

        when: "content was found in the grid"
        filterPanel.typeSearchText( imageSelectorContent.getName() );

        then: "content has online status in the browse panel"
        contentBrowsePanel.getContentStatus( imageSelectorContent.getPath() ).equals( ContentStatus.ONLINE.getValue() );
        and: "content is valid"
        !contentBrowsePanel.isContentInvalid( imageSelectorContent.getPath().toString() );
    }

    def "GIVEN saving of Image Selector-content (0:1) and one image selected WHEN content opened for edit THEN correct image present on page and option filter not displayed"()
    {
        given: "new content with type 'Image Selector0_1' added"
        TEST_IMAGE_SELECTOR_CONTENT = buildImageSelector0_1_Content( TEST_IMAGE_NAME );
        selectSiteOpenWizard( TEST_IMAGE_SELECTOR_CONTENT.getContentTypeName() ).typeData( TEST_IMAGE_SELECTOR_CONTENT ).save().close(
            TEST_IMAGE_SELECTOR_CONTENT.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> images = formViewPanel.getSelectedImages();

        then: "one image present on the page"
        images.size() == 1;
        and:
        !formViewPanel.isOptionFilterIsDisplayed();

        and: "image with correct name present on the page"
        images.get( 0 ) == TEST_IMAGE_NAME;

        and: "options filter input is not shown"
        !formViewPanel.isOptionFilterIsDisplayed();
    }

    def "GIVEN content opened for edit WHEN images clicked THEN buttons 'Edit' and 'Remove' appears"()
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

    def "GIVEN content opened for edit WHEN images clicked  AND 'Remove' button clicked THEN image removed and 'options filter' input displayed"()
    {
        given: "'Image Selector' content with selected image opened for edit "
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
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
    }
}
