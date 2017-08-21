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
 *
 * Tasks:
 *
 * Verifies:
 * https://github.com/enonic/xp/issues/5023
 * Mobile(480x800) ImageEditor dialog- buttons 'Apply' and 'Close' are not available #5023
 * */
@Stepwise
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
        expect: "'More' button should be present on the toolbar"
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

    def "GIVEN wizard for creating of folder is opened WHEN data has been typed and saved THEN new folder should be listed"()
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

    def "GIVEN existing folder WHEN the folder has been clicked THEN Item Preview Panel should be displayed"()
    {
        when: "existing folder has been clicked"
        contentBrowsePanel.clickOnRowByName( TEST_FOLDER.getName() );

        then: "Item Preview Panel should be displayed"
        contentItemPreviewPanel.isDisplayed();
    }

    //TODO remove it  when https://github.com/enonic/xp/issues/5023 will be fixed
    @Ignore
    def "GIVEN image content opened AND 'Crop' button was pressed THEN image was cropped THEN 'Reset Mask' link appears on the toolbar "()
    {
        given: "content wizard opened"
        ( (ContentBrowseFilterPanel) filterPanel.typeSearchText( IMPORTED_IMAGE_BOOK_NAME ) ).clickOnShowResultsLink();
        selectContentByName( IMPORTED_IMAGE_BOOK_NAME );
        contentBrowsePanel.clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel imageFormViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = new ImageEditor( getSession() );

        when: "'Crop' button was pressed"
        imageFormViewPanel.clickOnCropButton();
        ImageEditorToolbar toolbar = imageEditor.getToolbar();
        and: "image was cropped"
        imageEditor.doDragCropButtonAndChangeHeightCropArea( -40 );

        then: "'Reset Mask' link is displayed"
        toolbar.isResetMaskDisplayed();

        and: "'Apply' button is present"
        toolbar.isApplyButtonDisplayed();

        and: "'Close' button is present"
        toolbar.isCloseButtonDisplayed();
    }

}


