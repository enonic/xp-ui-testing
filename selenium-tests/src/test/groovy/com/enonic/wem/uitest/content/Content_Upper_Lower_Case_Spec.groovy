package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 14.10.2016.
 * BUG: XP-4088 Its possible to store more that one node with case difference
 *
 * Task: XP-4093 Create tests to verify that multiple content with same letter name, but different case is illegal name of new content.
 * */
@Stepwise
class Content_Upper_Lower_Case_Spec
    extends BaseContentSpec
{
    @Shared
    String FOLDER_NAME_IN_LOWER_CASE = "folder2016";

    @Shared
    String FOLDER_NAME_IN_UPPER_CASE = "FOLDER2016";

    @Shared
    String FOLDER_NAME_MIXED_CASE = "FOLDer2016";

    @Shared
    String WARNING_MESSAGE = "Content [%s] could not be updated. A content with that name already exists"


    def "GIVEN creating new folder on root with the name in lower cases WHEN saved  THEN correct notification message appears"()
    {
        given: "creating new folder on root"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        wizard.typeDisplayName( FOLDER_NAME_IN_LOWER_CASE );

        when: "'Save' button pressed"
        wizard.save();

        then: "correct notification message appears"
        String message = String.format( Application.CONTENT_SAVED, FOLDER_NAME_IN_LOWER_CASE );
        contentBrowsePanel.waitExpectedNotificationMessage( message, 2 );
    }

    def "GIVEN creating new folder on root with the same name in upper cases WHEN saved THEN warning message appears"()
    {
        given: "creating new folder on root with the name in upper cases"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        wizard.typeDisplayName( FOLDER_NAME_IN_UPPER_CASE );

        when: "'Save' button pressed"
        wizard.save();

        then: "warning message appears"
        String message = String.format( WARNING_MESSAGE, FOLDER_NAME_IN_LOWER_CASE );
        contentBrowsePanel.waitExpectedNotificationMessage( message, 2 );
    }

    def "GIVEN creating new folder on root with the same name in mixed cases WHEN saved THEN warning message appears"()
    {
        given: "creating new folder on root with the name in mixed cases"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        wizard.typeDisplayName( FOLDER_NAME_MIXED_CASE );

        when: "'Save' button pressed"
        wizard.save();

        then: "warning message appears"
        String message = String.format( WARNING_MESSAGE, FOLDER_NAME_IN_LOWER_CASE );
        contentBrowsePanel.waitExpectedNotificationMessage( message, 2 );
    }
    //only one folder was created:
    def "WHEN name of folder typed in the search input typed THEN only one content displayed in the grid"()
    {
        when:
        filterPanel.typeSearchText( FOLDER_NAME_IN_LOWER_CASE );

        then: ""
        contentBrowsePanel.getContentNamesFromGrid().size() == 1;
    }
}
