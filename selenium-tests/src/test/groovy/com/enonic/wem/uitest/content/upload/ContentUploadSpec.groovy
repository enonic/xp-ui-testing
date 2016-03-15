package com.enonic.wem.uitest.content.upload

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentUploadSpec
    extends BaseContentSpec
{

    @Shared
    String pathToTxt = "test-data/upload/text.txt";

    @Shared
    String FILE_NAME = "text.txt";

    @Shared
    String path = "test-data/upload/ea.png";

    @Shared
    String IMAGE_FILE_NAME = "ea.png";


    @Ignore
    def "GIVEN opened a new content dialog WHEN upload button clicked and file selected THEN new content present in browse panel "()
    {
        given: "opened a new content dialog"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: " click on drop zone and select a archive"
        TestUtils.saveScreenshot( getSession(), "start-upload-png" )
        dialog.doUploadFile( path, IMAGE_FILE_NAME );
        sleep( 1000 )

        then: "new png file content appears in the browse panel"
        TestUtils.saveScreenshot( getSession(), "upload-png" )
        contentBrowsePanel.exists( IMAGE_FILE_NAME )
    }

    def "GIVEN opened a new content dialog WHEN upload button clicked and txt-file selected THEN new content present in browse panel  "()
    {
        given: "opened a new content dialog"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: " click on drop zone and select a archive"
        TestUtils.saveScreenshot( getSession(), "start-upload-txt" )
        dialog.doUploadFile( pathToTxt, FILE_NAME );
        sleep( 1000 )

        then: "new png file content appears in the browse panel"
        TestUtils.saveScreenshot( getSession(), "upload-txt" )
        contentBrowsePanel.exists( FILE_NAME )
    }

    def "GIVEN text file that have been uploaded WHEN the content opened for edit THEN correct name displayed on wizard-page"()
    {
        when: "text file was selected and opened"
        filterPanel.typeSearchText( FILE_NAME );
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow( FILE_NAME ).clickToolbarEdit();
        TestUtils.saveScreenshot( getSession(), "txt-content-opened" )

        then: "correct name is displayed"
        wizardPanel.getNameInputValue().equalsIgnoreCase( FILE_NAME );
    }


}