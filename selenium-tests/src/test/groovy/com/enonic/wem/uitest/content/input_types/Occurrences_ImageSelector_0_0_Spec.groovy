package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_ImageSelector_0_0_Spec
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

    def "GIVEN saving of Image Selector-content (0:0) and two image selected WHEN content opened for edit THEN correct images present on page "()
    {
        given: "new content with type Image Selector 0:0 added'"
        Content imageSelectorContent = buildImageSelector0_0_Content( TEST_IMG_1, TEST_IMG_2 );
        selectSiteOpenWizard( imageSelectorContent.getContentTypeName() ).typeData( imageSelectorContent ).save().close(
            imageSelectorContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( imageSelectorContent );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> images = formViewPanel.getSelectedImages();

        then: "two images are present on wizard page"
        images.size() == 2;
        and:
        formViewPanel.isOptionFilterIsDisplayed();

        and: "images present with correct names"
        images.get( 0 ) == TEST_IMG_1;
        and:
        images.get( 1 ) == TEST_IMG_2;
    }

    def "GIVEN saving of 'Image Selector-content' (0:0) and four images selected WHEN content opened for edit THEN correct images present on page "()
    {
        given: "new content with type Image Selector 0_0 added'"
        TEST_IMAGE_SELECTOR_CONTENT = buildImageSelector0_0_Content( TEST_IMG_1, TEST_IMG_2, TEST_IMG_3, TEST_IMG_4 );
        selectSiteOpenWizard( TEST_IMAGE_SELECTOR_CONTENT.getContentTypeName() ).typeData( TEST_IMAGE_SELECTOR_CONTENT ).save().close(
            TEST_IMAGE_SELECTOR_CONTENT.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        List<String> images = formViewPanel.getSelectedImages();
        TestUtils.saveScreenshot( getSession(), "imgs0_0_4" )

        then: "four images are present on wizard page"
        images.size() == 4;
        and:
        formViewPanel.isOptionFilterIsDisplayed();

        and: "images present with correct names"
        images.get( 0 ) == TEST_IMG_1;
        and:
        images.get( 1 ) == TEST_IMG_2;
        and:
        images.get( 2 ) == TEST_IMG_3;
        and:
        images.get( 3 ) == TEST_IMG_4;
    }

    def "GIVEN content opened for edit WHEN one of the images clicked THEN buttons 'Edit' and 'Remove' appears"()
    {
        given: "new content with type Image Selector 0_0 added'"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "content opened for edit"
        formViewPanel.clickOnImage( TEST_IMG_3 )
        then: "buttons 'Edit' and 'Remove' appears"
        formViewPanel.isRemoveButtonDisplayed();
        and:
        formViewPanel.isEditButtonDisplayed();
    }

    def "GIVEN content with 4 images opened for edit WHEN one of the images selected and 'Remove' button pressed THEN number of images reduced"()
    {
        given: "new content with type Image Selector 0_0 added'"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_IMAGE_SELECTOR_CONTENT );
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        when: "content opened for edit"
        formViewPanel.clickOnImage( TEST_IMG_3 );
        formViewPanel.clickOnRemoveButton();
        TestUtils.saveScreenshot( getSession(), "imgs0_0_remove" );

        then: "number of images reduced"
        formViewPanel.getSelectedImages().size() == 3;
    }

    def "GIVEN saving of not required 'Image Selector 0:0' content without selected image WHEN 'Publish' button pressed THEN valid content with 'Online' status listed"()
    {
        given: "new content with type 'Image Selector 0:0'"
        Content imageSelectorContent = buildImageSelector0_0_Content( null );
        selectSiteOpenWizard( imageSelectorContent.getContentTypeName() ).typeData(
            imageSelectorContent ).save().clickOnPublishButton().close( imageSelectorContent.getDisplayName() );

        when: "content was found in the grid"
        filterPanel.typeSearchText( imageSelectorContent.getName() );

        then:
        contentBrowsePanel.getContentStatus( imageSelectorContent.getPath() ).equals( ContentStatus.ONLINE.getValue() );
        and:
        !contentBrowsePanel.isContentInvalid( imageSelectorContent.getPath().toString() );
    }

    def "GIVEN saving  'Image Selector 0:0' content with selected image WHEN 'Publish' button pressed THEN valid content with 'Online' status listed"()
    {
        given: "new content with type 'Image Selector 0:0'"
        Content imageSelectorContent = buildImageSelector0_0_Content( TEST_IMG_2 );
        selectSiteOpenWizard( imageSelectorContent.getContentTypeName() ).typeData(
            imageSelectorContent ).save().clickOnPublishButton().close( imageSelectorContent.getDisplayName() );

        when: "content was found in the grid"
        filterPanel.typeSearchText( imageSelectorContent.getName() );

        then:
        contentBrowsePanel.getContentStatus( imageSelectorContent.getPath() ).equals( ContentStatus.ONLINE.getValue() );
        and:
        !contentBrowsePanel.isContentInvalid( imageSelectorContent.getPath().toString() );
    }
}
