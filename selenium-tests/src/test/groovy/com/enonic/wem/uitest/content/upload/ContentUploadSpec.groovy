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
    def "GIVEN 'new content dialog' is opened WHEN upload button was clicked and file selected THEN new content should be present in the browse panel "()
    {
        given: "new content dialog is opened"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: " click on drop zone and select a archive"
        dialog.doUploadFile( path, IMAGE_FILE_NAME );
        TestUtils.createScreenCaptureWithRobot( "upload_image" );
        sleep( 1000 )

        then: "new png file content appears in the browse panel"
        TestUtils.saveScreenshot( getSession(), "upload-png" )
        contentBrowsePanel.exists( IMAGE_FILE_NAME )
    }

    def "GIVEN 'new content dialog' is opened WHEN upload button was clicked and txt-file selected THEN new content should be listed in the browse panel"()
    {
        given: "new content dialog is opened"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: "upload button was clicked and txt-file selected"
        TestUtils.createScreenCaptureWithRobot( "upload_txt1" );
        dialog.doUploadFile( pathToTxt, FILE_NAME );
        TestUtils.createScreenCaptureWithRobot( "upload_txt2" );
        sleep( 1000 )

        then: "new txt-file should appear in the browse panel"
        contentBrowsePanel.exists( FILE_NAME )
    }

    def "GIVEN text file that have been uploaded WHEN the text file is opened THEN correct name should be displayed on wizard-page"()
    {
        when: "the text file is opened"
        filterPanel.typeSearchText( FILE_NAME );
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow( FILE_NAME ).clickToolbarEdit();
        saveScreenshot( "txt-content-opened" )

        then: "correct name should be displayed on wizard-page"
        wizardPanel.getNameInputValue().equalsIgnoreCase( FILE_NAME );
    }


}