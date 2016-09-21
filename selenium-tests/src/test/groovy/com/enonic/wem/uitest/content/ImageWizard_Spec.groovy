package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImageFormViewPanel
import spock.lang.Shared

/**
 * Created on 21.09.2016.*/
class ImageWizard_Spec
    extends BaseContentSpec
{
    @Shared
    String CAPTION_TEXT = "test text";

    @Shared
    String COPYRIGHT_TEXT = "copyright text";

    def "WHEN image content opened THEN all control elemnts are present"()
    {
        when: "content wizard opened"
        findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );

        then: "Caption text area is present"
        imageFormViewPanel.isCaptionTextAreaPresent();

        and: "'Gps Info' tab bar item is present"
        imageFormViewPanel.isGpsInfoTabBarItemPresent();

        and: "'Image Info' tab bar item is present"
        imageFormViewPanel.isImageInfoTabBarItemPresent();

        and: "'Photo Info' tab bar item is present"
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

    def "GIVEN existing image content WHEN the content opened and caption was typed AND content saved THEN correct text present in the text area"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );

        when: "text in the caption input typed"
        imageFormViewPanel.typeInCaptionTextArea( CAPTION_TEXT );

        and: "wizard saved and closed"
        wizard.save().close( IMPORTED_BOOK_IMAGE );

        and: "image opened again"
        contentBrowsePanel.clickToolbarEdit();
        saveScreenshot( "image_caption_text" );

        then:
        imageFormViewPanel.getCaptionText() == CAPTION_TEXT;
    }

    def "GIVEN existing image content WHEN the content opened and 'copyright' text was typed AND content saved THEN correct text present in the input"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );

        when: "text in the 'copyright' input typed"
        imageFormViewPanel.typeInCopyRightInput( COPYRIGHT_TEXT );

        and: "wizard saved and closed"
        wizard.save().close( IMPORTED_BOOK_IMAGE );

        and: "image opened again"
        contentBrowsePanel.clickToolbarEdit();
        saveScreenshot( "image_copyright_text" );

        then:
        imageFormViewPanel.getTextFromCopyright() == COPYRIGHT_TEXT;
    }
}
