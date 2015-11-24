package com.enonic.wem.uitest.content.upload

import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentUploadSpec
    extends BaseContentSpec
{

    @Shared
    String pathToTxt = "test-data/upload/text.txt";


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

    def "GIVEN text file that have been uploaded WHEN the content opened for edit THEN correct name displayed on wizard-page"()
    {
        when: "text file was selected and opened"
        filterPanel.typeSearchText( "text.txt" );
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow( "text.txt" ).clickToolbarEdit();
        TestUtils.saveScreenshot( getSession(), "txt-content-opened" )

        then: "correct name is displayed"
        wizardPanel.getNameInputValue().equalsIgnoreCase( "text.txt" );
    }


}