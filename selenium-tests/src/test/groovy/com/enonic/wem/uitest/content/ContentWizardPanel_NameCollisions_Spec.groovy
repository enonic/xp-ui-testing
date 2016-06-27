package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class ContentWizardPanel_NameCollisions_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_FOLDER;


    def "GIVEN added folder in the ROOT WHEN wizard opened and folder with the same name typed AND 'Save' button pressed  THEN correct warning-message appears "()
    {
        given:
        TEST_FOLDER = buildFolderContent( "folder", "name already in use" );
        addContent( TEST_FOLDER );

        when:
        String warning = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_FOLDER.getContentTypeName() ).typeData(
            TEST_FOLDER ).save().waitNotificationWarning( Application.EXPLICIT_NORMAL );
        TestUtils.saveScreenshot( getSession(), "test_save_content_name_in_use" )
        then:
        warning == String.format( Application.CONTENT_ALREADY_IN_USE_WARNING, TEST_FOLDER.getName() );
    }
    //this test verifies the  "XP-3037 Impossible to add a content with name that is no longer used"
    def "GIVEN deleting of existing folder WHEN folder was deleted AND new folder with the same name adding AND 'save' button pressed THEN new content successfully added"()
    {
        given:
        findAndSelectContent( TEST_FOLDER.getName() ).clickToolbarDelete().doDelete();
        String expectedMessage = String.format( Application.CONTENT_SAVED, TEST_FOLDER.getDisplayName() );

        when:
        boolean isMessageCorrect = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_FOLDER.getContentTypeName() ).typeData(
            TEST_FOLDER ).save().waitExpectedNotificationMessage( expectedMessage, Application.EXPLICIT_NORMAL );
        TestUtils.saveScreenshot( getSession(), "test_save_content_name_no_longer_used" )

        then: "'content saved' notification message appeared"
        isMessageCorrect;
    }
}
