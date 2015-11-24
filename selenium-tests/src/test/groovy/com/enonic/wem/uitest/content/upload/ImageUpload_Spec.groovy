package com.enonic.wem.uitest.content.upload

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

class ImageUpload_Spec
    extends BaseContentSpec
{
    @Shared
    String path = "test-data/upload/ea.png";

    def "GIVEN opened a new content dialog WHEN upload button clicked and file selected THEN new content present in browse panel "()
    {
        given: "opened a new content dialog"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: " click on drop zone and select a archive"
        TestUtils.saveScreenshot( getSession(), "start-upload-png" )
        dialog.doUploadFile( path );
        sleep( 1000 )

        then: "new png file content appears in the browse panel"
        TestUtils.saveScreenshot( getSession(), "upload-png" )
        contentBrowsePanel.exists( "ea.png" )
    }
}
