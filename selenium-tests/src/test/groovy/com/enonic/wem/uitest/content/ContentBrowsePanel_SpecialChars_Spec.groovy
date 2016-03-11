package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog
import com.enonic.autotests.utils.TestUtils
import spock.lang.Shared

class ContentBrowsePanel_SpecialChars_Spec
    extends BaseContentSpec
{

    @Shared
    String path = "/test-data/upload/ea[1].png";

    @Shared
    String FILE_NAME = "ea[1].png";

    def "GIVEN opened a new content dialog WHEN upload button clicked and file selected THEN new content present in browse panel "()
    {
        given: "opened a new content dialog"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: "click on drop zone and select a file"
        dialog.doUploadFile( path );
        sleep( 1000 )

        then: "new png file content appears in the browse panel"
        TestUtils.saveScreenshot( getSession(), "upload-png-spec" )
        contentBrowsePanel.exists( FILE_NAME )
    }
}
