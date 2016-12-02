package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_ImageSelector_0_0_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content TEST_IMAGE_SELECTOR_CONTENT;

    def "GIVEN saving of Image Selector-content (0:0) and two image selected WHEN content opened for edit THEN correct images present on page "()
    {
        given: "new content with type Image Selector 0:0 added'"
        Content imageSelectorContent = buildImageSelector0_0_Content( NORD_IMAGE_DISPLAY_NAME, BOOK_IMAGE_DISPLAY_NAME );
        selectSitePressNew( imageSelectorContent.getContentTypeName() ).typeData(
            imageSelectorContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( imageSelectorContent );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> images = formViewPanel.getSelectedImages();

        then: "two images are present on wizard page"
        images.size() == 2;
        and:
        formViewPanel.isOptionFilterIsDisplayed();

        and: "images present with correct names"
        images.get( 0 ) == NORD_IMAGE_NAME;
        and:
        images.get( 1 ) == BOOK_IMAGE_NAME;
    }

    def "GIVEN saving of 'Image Selector-content' (0:0) and four images selected WHEN content opened for edit THEN correct images present on page "()
    {
        given: "new content with type Image Selector 0_0 added'"
        TEST_IMAGE_SELECTOR_CONTENT =
            buildImageSelector0_0_Content( NORD_IMAGE_DISPLAY_NAME, BOOK_IMAGE_DISPLAY_NAME, MAN_IMAGE_DISPLAY_NAME,
                                           FL_IMAGE_DISPLAY_NAME );
        selectSitePressNew( TEST_IMAGE_SELECTOR_CONTENT.getContentTypeName() ).typeData(
            TEST_IMAGE_SELECTOR_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> images = formViewPanel.getSelectedImages();
        saveScreenshot( "img_sel_content0_0_4" )

        then: "four images are present on wizard page"
        images.size() == 4;
        and:
        formViewPanel.isOptionFilterIsDisplayed();

        and: "images present with correct names"
        images.get( 0 ) == NORD_IMAGE_NAME;
        and:
        images.get( 1 ) == BOOK_IMAGE_NAME;
        and:
        images.get( 2 ) == MAN_IMAGE_NAME;
        and:
        images.get( 3 ) == FL_IMAGE_NAME;
    }

    def "GIVEN content opened for edit WHEN one of the images clicked THEN buttons 'Edit' and 'Remove' appears"()
    {
        given: "new content with type Image Selector 0_0 added'"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "content opened for edit"
        formViewPanel.clickOnImage( MAN_IMAGE_NAME )
        then: "buttons 'Edit' and 'Remove' appears"
        formViewPanel.isRemoveButtonDisplayed();

        and:
        formViewPanel.isEditButtonDisplayed();
    }

    def "GIVEN content opened for edit WHEN checkbox for one of the images clicked THEN label for button 'Remove' has a correct number"()
    {
        given: "new content with type Image Selector 0_0 added'"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "content opened for edit"
        formViewPanel.clickOnCheckboxAndSelectImage( MAN_IMAGE_NAME );

        then: "label for button 'Remove' has a correct number"
        formViewPanel.getNumberFromRemoveButton() == 1;
    }

    def "GIVEN content with 4 images opened for edit WHEN one of the images selected and 'Remove' button pressed THEN number of images reduced"()
    {
        given: "new content with type Image Selector 0_0 added'"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "content opened for edit"
        formViewPanel.clickOnImage( MAN_IMAGE_NAME );
        formViewPanel.clickOnRemoveButton();
        saveScreenshot( "img_sel_0_0_one_removed" );

        then: "number of images reduced"
        formViewPanel.getSelectedImages().size() == 3;
    }

    def "GIVEN saving of 'Image Selector 0:0' and content without selected image WHEN data typed AND image not selected THEN 'Publish' button on the wizard-toolbar is enabled"()
    {
        given: "new content with type 'Image Selector'"
        Content imageSelectorContent = buildImageSelector0_0_Content( null );

        when: "data typed AND image not selected"
        ContentWizardPanel wizard = selectSitePressNew( imageSelectorContent.getContentTypeName() ).typeData( imageSelectorContent );

        then: "'Publish' button on the wizard-toolbar is enabled"
        wizard.isPublishButtonEnabled();
    }

    def "GIVEN saving of not required 'Image Selector 0:0' content without selected image WHEN 'Publish' button pressed THEN valid content with 'Online' status listed"()
    {
        given: "new content with type 'Image Selector 0:0'"
        Content imageSelectorContent = buildImageSelector0_0_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( imageSelectorContent.getContentTypeName() ).typeData( imageSelectorContent ).save();
        wizard.clickOnWizardPublishButton().clickOnPublishNowButton();
        String publishedMessage = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        wizard.closeBrowserTab().switchToBrowsePanelTab();

        when: "content was found in the grid"
        filterPanel.typeSearchText( imageSelectorContent.getName() );

        then:
        contentBrowsePanel.getContentStatus( imageSelectorContent.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
        and:
        !contentBrowsePanel.isContentInvalid( imageSelectorContent.getName().toString() );
        and:
        publishedMessage == String.format( Application.ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, imageSelectorContent.getDisplayName() );
    }

    def "GIVEN saving 'Image Selector 0:0' content with selected image WHEN 'Publish' button pressed THEN valid content with 'Online' status listed"()
    {
        given: "new content with type 'Image Selector 0:0'"
        Content imageSelectorContent = buildImageSelector0_0_Content( BOOK_IMAGE_DISPLAY_NAME );
        ContentWizardPanel wizard = selectSitePressNew( imageSelectorContent.getContentTypeName() ).typeData( imageSelectorContent ).save();
        wizard.clickOnWizardPublishButton().clickOnPublishNowButton();
        String publishedMessage = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        wizard.closeBrowserTab().switchToBrowsePanelTab();

        when: "content was found in the grid"
        filterPanel.typeSearchText( imageSelectorContent.getName() );

        then: "valid content with 'Online' status listed"
        contentBrowsePanel.getContentStatus( imageSelectorContent.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );

        and:
        !contentBrowsePanel.isContentInvalid( imageSelectorContent.getName().toString() );

        and: "correct notification message appeared"
        publishedMessage == String.format( Application.ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, imageSelectorContent.getDisplayName() );
    }
}
