package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.ArchiveContent
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
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
        BaseAbstractContent content = ArchiveContent.builder().
            withName( NameHelper.uniqueName( "archive" ) ).
            withDisplayName( "archive" ).
            withParent( ContentPath.ROOT ).build();
        WizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType(
            ContentTypeName.archiveMedia().toString() ).waitUntilWizardOpened().typeData( content ).save()

        when:
        wizard.close()

        then:
        SaveBeforeCloseDialog dialog = new SaveBeforeCloseDialog( getSession() )
        !dialog.waitForPresent()

    }

    def " GIVEN a changed Content WHEN closing THEN SaveBeforeCloseDialog must appear"()
    {
        given:
        BaseAbstractContent content = ArchiveContent.builder().
            withName( NameHelper.uniqueName( "archive" ) ).
            withDisplayName( "archive" ).
            withParent( ContentPath.ROOT ).build();
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType(
            ContentTypeName.archiveMedia().toString() ).typeData( content ).save()
        content.setDisplayName( "chngedname" )
        wizard.typeData( content )

        when:
        wizard.close()
        TestUtils.saveScreenshot( getSession(), "SaveBeforeCloseDialog-appears" )

        then:
        SaveBeforeCloseDialog dialog = new SaveBeforeCloseDialog( getSession() )
        dialog.waitForPresent()
    }

    def "GIVEN changing name of an existing Content and wizard closing WHEN Yes is chosen THEN Content is listed in BrowsePanel with it's new name"()
    {
        given:
        BaseAbstractContent content = ArchiveContent.builder().
            withName( NameHelper.uniqueName( "archive" ) ).withDisplayName( "archive" ).withParent( ContentPath.ROOT ).build()
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType(
            ContentTypeName.archiveMedia().toString() ).typeData( content ).save()
        content.setName( NameHelper.uniqueName( "newarchive" ) )
        wizard.typeData( content ).close()
        SaveBeforeCloseDialog dialog = new SaveBeforeCloseDialog( getSession() )
        dialog.waitForPresent()

        when:
        dialog.clickYesButton()
        contentBrowsePanel.waitsForSpinnerNotVisible()

        then:
        contentBrowsePanel.exists( ContentPath.from( content.getName() ) )
    }

    def "GIVEN changing name of an existing Content and wizard closing WHEN No is chosen THEN Content is listed in BrowsePanel with it's original name"()
    {
        given:
        BaseAbstractContent content = ArchiveContent.builder().
            withName( NameHelper.uniqueName( "archive" ) ).withDisplayName( "archive" ).withParent( ContentPath.ROOT ).build()
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType(
            ContentTypeName.archiveMedia().toString() ).typeData( content ).save()
        content.setName( NameHelper.uniqueName( "newarchive" ) )
        wizard.typeData( content ).close()
        SaveBeforeCloseDialog dialog = new SaveBeforeCloseDialog( getSession() )
        dialog.waitForPresent()

        when:
        dialog.clickNoButton()
        contentBrowsePanel.waitsForSpinnerNotVisible()

        then:
        !contentBrowsePanel.exists( ContentPath.from( content.getName() ) )
    }

    def "GIVEN changing an existing Content and wizard closing WHEN Cancel is chosen THEN wizard is still open"()
    {
        given:
        BaseAbstractContent content = ArchiveContent.builder().
            withName( NameHelper.uniqueName( "archive" ) ).withDisplayName( "archive" ).withParent( ContentPath.ROOT ).build()
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType(
            ContentTypeName.archiveMedia().toString() ).typeData( content ).save()
        content.setName( NameHelper.uniqueName( "newarchive" ) )
        wizard.typeData( content ).close()
        SaveBeforeCloseDialog dialog = new SaveBeforeCloseDialog( getSession() )
        dialog.waitForPresent()

        when:
        dialog.clickCancelButton()
        contentBrowsePanel.waitsForSpinnerNotVisible()
        TestUtils.saveScreenshot( getSession(), "SaveBeforeCloseDialog-cancel" )
        then:
        wizard.waitUntilWizardOpened()
    }
}
