package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImageFormViewPanel
import spock.lang.Ignore
import spock.lang.Shared

/**
 * Created on 21.09.2016.*/
@Ignore
class ImageWizard_Spec
    extends BaseContentSpec
{
    @Shared
    String CAPTION_TEXT = "test text";

    @Shared
    String COPYRIGHT_TEXT = "copyright text";

    @Shared
    String ARTIST_TAG1 = "artist1";

    @Shared
    String ARTIST_TAG2 = "artist2";

    @Shared
    String TAG1 = "tag1";

    @Shared
    String TAG2 = "tag2";

    def "WHEN image content is opened THEN expected elements should be present"()
    {
        when: "content wizard opened"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEditAndSwitchToWizardTab().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );

        then: "Caption text area is present"
        imageFormViewPanel.isCaptionTextAreaPresent();

        and: "'Location' tab bar item is present"
        imageFormViewPanel.isGpsInfoTabBarItemPresent();

        and: "'Properties' tab bar item is present"
        imageFormViewPanel.isImageInfoTabBarItemPresent();

        and: "'Photo' tab bar item is present"
        imageFormViewPanel.isPhotoInfoTabBarItemPresent();

        and: "Image uploader is present"
        imageFormViewPanel.isImageUploaderPresent();

        and: "Copyright input is present"
        imageFormViewPanel.isCopyrightInputPresent();

        and: "button Crop is present"
        imageFormViewPanel.isButtonCropPresent();

        and: "button Focus is present"
        imageFormViewPanel.isButtonFocusPresent();

        and: "Artists input is present"
        imageFormViewPanel.isArtistTagInputPresent();
    }

    def "WHEN existing image is opened and caption has been typed AND content saved THEN caption should be saved"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEditAndSwitchToWizardTab();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );

        when: "text in the caption input typed"
        imageFormViewPanel.typeInCaptionTextArea( CAPTION_TEXT );

        and: "wizard saved and closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "image opened again"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        saveScreenshot( "image_caption_text" );

        then:
        imageFormViewPanel.getCaptionText() == CAPTION_TEXT;
    }

    def "WHEN existing image is opened and 'copyright' text has been typed AND content saved THEN 'copyright' should be present in the input"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEditAndSwitchToWizardTab();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );

        when: "text in the 'copyright' input typed"
        imageFormViewPanel.typeInCopyRightInput( COPYRIGHT_TEXT );

        and: "wizard saved and closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "image opened again"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        saveScreenshot( "image_copyright_text" );

        then:
        imageFormViewPanel.getTextFromCopyright() == COPYRIGHT_TEXT;
    }

    def "WHEN existing image is opened and 'Artists' tags added AND content saved THEN tags should be present in the input"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEditAndSwitchToWizardTab();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );

        when: "text in the 'Artists' input typed"
        imageFormViewPanel.typeInArtistsInput( ARTIST_TAG1, ARTIST_TAG2 );

        and: "wizard saved and closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "image opened again"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        saveScreenshot( "image_artists_tags" );

        then: "artist tags are present on the page "
        imageFormViewPanel.getArtistsTagsText().contains( ARTIST_TAG1 );

        and: ""
        imageFormViewPanel.getArtistsTagsText().contains( ARTIST_TAG2 );
    }

    def "WHEN existing image is opened AND two tags added AND content saved THEN tags are present on the page"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEditAndSwitchToWizardTab();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );

        when: "two tags added"
        imageFormViewPanel.typeTags( TAG1, TAG2 );

        and: "wizard saved and closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "image opened again"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        saveScreenshot( "image_two_tags_added" );

        then: "added tags are present on the page "
        imageFormViewPanel.getTagsText().contains( TAG1 );

        and: ""
        imageFormViewPanel.getTagsText().contains( TAG2 );
    }
}
