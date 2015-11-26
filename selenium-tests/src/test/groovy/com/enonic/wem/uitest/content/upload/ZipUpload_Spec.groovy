package com.enonic.wem.uitest.content.upload

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore
import spock.lang.Shared

class ZipUpload_Spec
    extends BaseContentSpec
{
    @Shared
    String pathToZip = "test-data/upload/img.zip";

    @Shared
    String FILE_NAME = "img.zip";

    @Ignore
    def "GIVEN opened a new content dialog WHEN upload button clicked and zip archive selected THEN new content appears in browse panel "()
    {
        given: "opened a new content dialog"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: "click 'New' button and open the 'New Content Dialog' and click on drop zone and select a archive"
        dialog.doUploadFile( pathToZip, FILE_NAME );
        sleep( 1000 )

        then: "new archive content appears in the browse panel"
        TestUtils.saveScreenshot( getSession(), "upload-zip" )
        contentBrowsePanel.exists( FILE_NAME )
    }
}
