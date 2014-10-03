package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ContentWizardPanel_SaveBeforeCloseDialogSpec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel


    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() )
    }

    def "GIVEN a unchanged Content WHEN closing THEN SaveBeforeCloseDialog must not appear"()
    {
        given:
        Content content = Content.builder().
            name( NameHelper.uniqueName( "archive" ) ).
            displayName( "archive" ).
            contentType( ContentTypeName.archiveMedia() ).
            parent( ContentPath.ROOT ).build();
        WizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.archiveMedia().toString() ).
            waitUntilWizardOpened().typeData( content ).save();

        when:
        SaveBeforeCloseDialog dialog = wizard.close();

        then:
        dialog == null;

    }

    def " GIVEN a changed Content WHEN closing THEN SaveBeforeCloseDialog must appear"()
    {
        given:
        Content content = Content.builder().
            name( NameHelper.uniqueName( "archive" ) ).
            displayName( "archive" ).
            contentType( ContentTypeName.archiveMedia() ).
            parent( ContentPath.ROOT ).
            build();

        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.archiveMedia().toString() ).
            typeData( content ).save();
        wizard.typeDisplayName( "changedname" );

        when:
        SaveBeforeCloseDialog dialog = wizard.close()
        TestUtils.saveScreenshot( getSession(), "SaveBeforeCloseDialog-appears" );

        then:
        dialog != null;
    }

    def "GIVEN changing name of an existing Content and wizard closing WHEN Yes is chosen THEN Content is listed in BrowsePanel with it's new name"()
    {
        given:
        Content content = Content.builder().
            name( NameHelper.uniqueName( "archive" ) ).
            displayName( "archive" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.archiveMedia() ).
            build();

        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().
            selectContentType( ContentTypeName.archiveMedia().toString() ).typeData( content ).save();
        wizard.typeDisplayName( "changedname" ).close();
        SaveBeforeCloseDialog dialog = new SaveBeforeCloseDialog( getSession() );
        dialog.waitForPresent();

        when:
        dialog.clickYesButton();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then:
        contentBrowsePanel.exists( ContentPath.from( content.getName() ) );
    }

    def "GIVEN changing name of an existing Content and wizard closing WHEN No is chosen THEN Content is listed in BrowsePanel with it's original name"()
    {
        given:
        Content content = Content.builder().
            name( NameHelper.uniqueName( "archive" ) ).
            displayName( "archive" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.archiveMedia() ).
            build();

        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.archiveMedia().toString() ).
            typeData( content ).save();
        String newName = NameHelper.uniqueName( "newarchive" );
        SaveBeforeCloseDialog dialog = wizard.typeName( newName ).close();


        when:
        dialog.clickNoButton();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then:
        contentBrowsePanel.exists( ContentPath.from( content.getName() ) ) && !contentBrowsePanel.exists( ContentPath.from( newName ) );
    }

    def "GIVEN changing an existing Content and wizard closing WHEN Cancel is chosen THEN wizard is still open"()
    {
        given:
        Content content = Content.builder().
            name( NameHelper.uniqueName( "archive" ) ).
            displayName( "archive" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.archiveMedia() ).
            build();

        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.archiveMedia().toString() ).
            typeData( content ).save();
        SaveBeforeCloseDialog dialog = wizard.typeName( "newarchive" ).close();

        when:
        dialog.clickCancelButton();
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getSession(), "SaveBeforeCloseDialog-cancel" );

        then:
        wizard.isOpened();
    }
}
