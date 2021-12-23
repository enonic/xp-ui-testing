package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class ContentWizardPanel_NameCollisions_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_FOLDER;

    def "GIVEN new folder is added (in the ROOT) WHEN new wizard has been opened and the same name has been typed THEN Save button gets disabled in the wizard"()
    {
        given: "preconditions - test folder should be added"
        TEST_FOLDER = buildFolderContent( "folder", "name already in use" );
        addContent( TEST_FOLDER );

        when: "new wizard has been opened and the same name has been typed"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_FOLDER.getContentTypeName() ).typeData(
            TEST_FOLDER );
        sleep( 700 );
        saveScreenshot( "test_save_content_name_in_use" )
        then: "'Save' button should be disabled"
        !wizard.isSaveButtonEnabled();
    }
    //this test verifies the  "XP-3037 Impossible to add a content with name that is no longer used"
    def "GIVEN deleting of existing folder WHEN folder was deleted AND new folder with the same name adding AND 'save' button pressed THEN new content successfully added"()
    {
        given:
        findAndSelectContent( TEST_FOLDER.getName() ).clickToolbarArchive().clickOnDeleteAndWaitForClosed(  );
        String expectedMessage = String.format( Application.CONTENT_SAVED, TEST_FOLDER.getName() );

        when:
        boolean isMessageCorrect = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_FOLDER.getContentTypeName() ).typeData(
            TEST_FOLDER ).save().waitExpectedNotificationMessage( expectedMessage, Application.EXPLICIT_NORMAL );
        saveScreenshot( "test_save_content_name_no_longer_used" )

        then: "'content saved' notification message appeared"
        isMessageCorrect;
    }
}
