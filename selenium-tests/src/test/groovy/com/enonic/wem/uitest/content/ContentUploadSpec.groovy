package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Ignore
import spock.lang.Shared

class ContentUploadSpec
    extends BaseGebSpec
{
    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    String path = "test-data/upload/ea.png";

    @Shared
    String pathToZip = "test-data/upload/img.zip";

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "GIVEN opened a new content dialog WHEN upload button clicked and file selected THEN new content present in browse panel "()
    {
        given: "opened a new content dialog"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: " click on drop zone and select a archive"
        dialog.doUploadFile( path );
        sleep( 1000 )

        then: "new png file content appears in the browse panel"
        TestUtils.saveScreenshot( getSession(), "upload-png" )
        contentBrowsePanel.exists( ContentPath.from( "ea.png" ) )
    }

    @Ignore
    def "GIVEN opened a new content dialog WHEN upload button clicked and zip archive selected THEN new content appears in browse panel "()
    {
        given: "opened a new content dialog"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: "click 'New' button and open the 'New Content Dialog' and click on drop zone and select a archive"
        dialog.doUploadFile( pathToZip );
        sleep( 1000 )

        then: "new archive content appears in the browse panel"
        TestUtils.saveScreenshot( getSession(), "upload-zip" )
        contentBrowsePanel.exists( ContentPath.from( "img.zip" ) )
    }
}