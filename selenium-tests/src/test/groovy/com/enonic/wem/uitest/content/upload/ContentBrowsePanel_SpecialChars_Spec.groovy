package com.enonic.wem.uitest.content.upload

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

class ContentBrowsePanel_SpecialChars_Spec
    extends BaseContentSpec
{

    @Shared
    String path = "/test-data/upload/ea[1].png";

    @Shared
    String FILE_NAME = "ea[1].png";

    def "GIVEN new content dialog is opened WHEN upload button clicked and file with special chars in the name is selected THEN new content should be present in the grid "()
    {
        given: "new content dialog is opened"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: "file with special chars in the name has been uploaded"
        dialog.doUploadFile( path );
        sleep( 1000 )

        then: "new png file should be present in the grid"
        saveScreenshot( "upload-special-chars" )
        contentBrowsePanel.exists( FILE_NAME )
    }
}
