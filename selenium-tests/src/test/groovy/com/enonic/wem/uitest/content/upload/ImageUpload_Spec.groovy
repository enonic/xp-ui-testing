package com.enonic.wem.uitest.content.upload

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

class ImageUpload_Spec
    extends BaseContentSpec
{
    @Shared
    String path = "/test-data/upload/ea.png";

    @Shared
    String FILE_NAME = "ea.png";


    def "GIVEN opened a new content dialog WHEN upload button clicked and file selected THEN new content present in browse panel "()
    {
        given: "'new content dialog' is opened"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: "upload button was clicked and file selected"
        saveScreenshot( "start-upload-png" )
        dialog.doUploadFile( path );
        sleep( 1000 )

        then: "new png file content should appear in the browse panel"
        contentBrowsePanel.exists( FILE_NAME )
    }
}
