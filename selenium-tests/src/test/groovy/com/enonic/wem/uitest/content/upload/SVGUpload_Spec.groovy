package com.enonic.wem.uitest.content.upload

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

class SVGUpload_Spec
    extends BaseContentSpec
{
    @Shared
    String path = "test-data/upload/cirkles.svg";

    def "GIVEN opened a new content dialog WHEN upload button clicked and SVG file selected THEN new content present in browse panel "()
    {
        given: "opened a new content dialog"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: " click on drop zone and select a archive"
        TestUtils.saveScreenshot( getSession(), "start-upload-svg" )
        dialog.doUploadFile( path );
        sleep( 1000 )

        then: "new png file content appears in the browse panel"
        TestUtils.saveScreenshot( getSession(), "upload-svg" )
        contentBrowsePanel.exists( "cirkles.svg" )
    }
}
