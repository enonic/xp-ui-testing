package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared

class ContentWizardPanel_SaveAlreadyInUse_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_FOLDER;
    //issue for this test is XP-1530
    @Ignore
    def "GIVEN added folder in the ROOT WHEN wizard opened and folder with the same name typed AND 'Save' button pressed  THEN correct warning-message appears "()
    {
        given:
        TEST_FOLDER = buildFolderContent( "folder", "name already in use" );
        addContent( TEST_FOLDER );

        when:
        String warning = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_FOLDER.getContentTypeName() ).typeData(
            TEST_FOLDER ).save().waitNotificationWarning( Application.EXPLICIT_NORMAL );
        then: true;
        warning == Application.CONTENT_ALREADY_IN_USE_WARNING;

    }
    // any tests will be added some later
}
