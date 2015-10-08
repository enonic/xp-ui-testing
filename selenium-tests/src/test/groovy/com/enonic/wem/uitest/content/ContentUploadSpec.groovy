package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog
import com.enonic.autotests.utils.TestUtils
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentUploadSpec
    extends BaseContentSpec
{
    @Shared
    String path = "test-data/upload/ea.png";

    @Shared
    String pathToZip = "test-data/upload/img.zip";

    @Shared
    String pathToTxt = "test-data/upload/text.txt";


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
        contentBrowsePanel.exists( "img.zip" )
    }

    def "GIVEN opened a new content dialog WHEN upload button clicked and txt-file selected THEN new content present in browse panel  "()
    {
        given: "opened a new content dialog"
        NewContentDialog dialog = contentBrowsePanel.clickToolbarNew();

        when: " click on drop zone and select a archive"
        TestUtils.saveScreenshot( getSession(), "start-upload-txt" )
        dialog.doUploadFile( pathToTxt );
        sleep( 1000 )

        then: "new png file content appears in the browse panel"
        TestUtils.saveScreenshot( getSession(), "upload-txt" )
        contentBrowsePanel.exists( "text.txt" )
    }

    //XP-1727
    @Ignore
    def "GIVEN text file was uploaded WHEN content opened for edit THEN correct text present on page"()
    {
        when: ""
        filterPanel.typeSearchText( "text.txt" );
        contentBrowsePanel.clickCheckboxAndSelectRow( "text.txt" ).clickToolbarEdit();

        then: true;
    }


}