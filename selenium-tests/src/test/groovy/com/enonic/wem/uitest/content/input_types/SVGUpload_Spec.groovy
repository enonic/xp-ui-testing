package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

class SVGUpload_Spec
    extends BaseContentSpec
{
    @Shared
    String path = "test-data/upload/cirkles.svg";

    @Shared
    String FILE_NAME = "cirkles.svg";

    def "GIVEN opened a new content dialog WHEN upload button clicked and SVG file selected THEN new content present in browse panel "()
    {
        given: "opened a new content dialog"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: " click on drop zone and select a svg-file"
        TestUtils.saveScreenshot( getSession(), "start-upload-svg" )
        dialog.doUploadFile( path, FILE_NAME );
        sleep( 1000 )

        then: "new svg file content appears in the browse panel"
        TestUtils.saveScreenshot( getSession(), "upload-svg" )
        contentBrowsePanel.exists( FILE_NAME )
    }
}
