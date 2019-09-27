package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.BaseContentType
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
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


    def "GIVEN wizard for new folder is opened WHEN name in lower cases has been typed AND saved THEN expected notification message appears"()
    {
        given: "creating new folder in root"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName() );
        wizard.typeDisplayName( FOLDER_NAME_IN_LOWER_CASE );

        when: "'Save' button pressed"
        wizard.save();

        then: "expected notification message appears"
        String message = String.format( Application.CONTENT_SAVED, FOLDER_NAME_IN_LOWER_CASE );
        contentBrowsePanel.waitExpectedNotificationMessage( message, 2 );
    }

    def "GIVEN wizard for new folder is opened WHEN existing name in upper cases has been typed AND saved THEN expected warning message appears"()
    {
        given: "creating new folder on root with the name in upper cases"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName() );
        wizard.typeDisplayName( FOLDER_NAME_IN_UPPER_CASE );

        when: "'Save' button has been pressed"
        wizard.save();

        then: "expected warning message should appear - 'Content [%s] could not be updated. A content with that name already exists'"
        String message = String.format( WARNING_MESSAGE, FOLDER_NAME_IN_LOWER_CASE );
        contentBrowsePanel.waitExpectedNotificationMessage( message, 2 );
    }

    def "GIVEN wizard for new folder is opened WHEN existing name but is in mixed cases has been typed AND saved THEN expected warning message should appear"()
    {
        given: "wizard for new folder is opened and existing name but is in mixed cases has been typed"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName() );
        wizard.typeDisplayName( FOLDER_NAME_MIXED_CASE );

        when: "'Save' button has been pressed"
        wizard.save();

        then: "warning message should appear - 'Content [%s] could not be updated. A content with that name already exists'"
        String message = String.format( WARNING_MESSAGE, FOLDER_NAME_IN_LOWER_CASE );
        contentBrowsePanel.waitExpectedNotificationMessage( message, 2 );
    }

    def "WHEN name of existing folder has been typed in the search THEN only one content should be present in the grid"()
    {
        when: "unique name of a folder has been typed"
        filterPanel.typeSearchText( FOLDER_NAME_IN_LOWER_CASE );

        then: "only one content should be present in the grid"
        contentBrowsePanel.getContentNamesFromGrid().size() == 1;
    }
}
