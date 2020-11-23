package com.enonic.wem.uitest.content.responsive

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditorToolbar
import com.enonic.autotests.pages.form.ImageFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 5/29/2017.
 * */
@Stepwise
@Ignore
class ContentBrowsePanel_480_800_Spec
    extends BaseResponsiveSpec
{
    @Shared
    Content TEST_FOLDER

    def setup()
    {
        setBrowserDimension( 480, 800 );
    }

    def "GIVEN browser dimension is 480x800 WHEN content grid is opened THEN required buttons should be present on the toolbar"()
    {
        expect: "'More' button should be present in the toolbar"
        contentBrowsePanel.isMoreButtonPresent();
        saveScreenshot( "480_800_1" );

        and: "'New' button should be displayed and enabled"
        contentBrowsePanel.isNewButtonEnabled();

        and: "'Delete' button should be displayed and disabled"
        !contentBrowsePanel.isDeleteButtonEnabled();

        and: "'Edit' button should be displayed and disabled"
        !contentBrowsePanel.isEditButtonEnabled();

        and: "'New' button should be displayed and enabled"
        contentBrowsePanel.isRefreshButtonDisplayed();

        and: "'Move' button should not be displayed"
        !contentBrowsePanel.isMoveButtonDisplayed();
    }

    def "GIVEN wizard for new folder is opened WHEN test data has been saved THEN new folder should be listed"()
    {
        given:
        TEST_FOLDER = buildFolderContent( "folder", "480-800" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_FOLDER.getContentTypeName() );

        when: "data has been saved"
        wizard.typeData( TEST_FOLDER ).save().close( TEST_FOLDER.getDisplayName() );
        saveScreenshot( "480_800_2" );

        then: "new created folder should be displayed in the grid"
        contentBrowsePanel.exists( TEST_FOLDER.getName() );
    }

    def "WHEN existing folder has been clicked THEN Item Preview Panel should be loaded"()
    {
        when: "existing folder has been clicked"
        contentBrowsePanel.clickOnRowByName( TEST_FOLDER.getName() );
        sleep( 3000 );
        saveScreenshot( "480_800_preview_panel" );

        then: "Item Preview Panel should be displayed"
        contentItemPreviewPanel.isDisplayed();
    }

    def "GIVEN image content is opened AND 'Crop' button has been pressed THEN image should be cropped AND 'Reset Mask' link appears on the toolbar"()
    {
        given: "content wizard is opened"
        ( (ContentBrowseFilterPanel) filterPanel.typeSearchText( IMPORTED_IMAGE_BOOK_NAME ) ).clickOnShowResultsLink();
        selectContentByName( IMPORTED_IMAGE_BOOK_NAME );
        contentBrowsePanel.clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = new ImageEditor( getSession() );

        when: "'Crop' button has been pressed"
        imageFormViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();
        and: "image was cropped"
        imageEditor.doDragCropButtonAndChangeHeightCropArea( -40 );

        then: "'Reset Mask' link should appear"
        toolbar.isResetMaskDisplayed();

        and: "'Apply' button should be displayed"
        toolbar.isApplyButtonDisplayed();

        and: "'Close' button should be displayed"
        toolbar.isCloseButtonDisplayed();
    }
}


