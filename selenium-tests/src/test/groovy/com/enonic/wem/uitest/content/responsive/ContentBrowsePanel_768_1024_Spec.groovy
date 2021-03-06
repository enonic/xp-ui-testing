package com.enonic.wem.uitest.content.responsive

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared

/**
 * Created on 5/30/2017.*/
@Ignore
class ContentBrowsePanel_768_1024_Spec
    extends BaseResponsiveSpec
{
    @Shared
    Content TEST_FOLDER

    def setup()
    {
        setBrowserDimension( 768, 1024 );
    }

    def "GIVEN browser dimension is 768x1024 WHEN content grid is opened THEN required buttons should be present on the toolbar"()
    {
        expect: "'More' button should be present on the toolbar"
        !contentBrowsePanel.isMoreButtonPresent();
        saveScreenshot( "768_1024_1" );

        and: "'New' button should be displayed and enabled"
        contentBrowsePanel.isNewButtonEnabled();

        and: "'Delete' button should be displayed and disabled"
        !contentBrowsePanel.isDeleteButtonEnabled();

        and: "'Edit' button should be displayed and disabled"
        !contentBrowsePanel.isEditButtonEnabled();

        and: "'New' button should be displayed and enabled"
        contentBrowsePanel.isRefreshButtonDisplayed();

        and: "'Move' button should be displayed and disabled"
        !contentBrowsePanel.isMoveButtonEnabled();
    }

    def "GIVEN wizard for new folder is opened WHEN test data has been saved THEN new folder should be listed"()
    {
        given:
        TEST_FOLDER = buildFolderContent( "folder", "768-1024" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_FOLDER.getContentTypeName() );

        when: "data has been saved"
        wizard.typeData( TEST_FOLDER ).save().close( TEST_FOLDER.getDisplayName() );
        saveScreenshot( "768_1024_2" );

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
}
