package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.*
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_SaveSpec
    extends BaseGebSpec
{
    @Shared
    String REPONAME = "test-folder";

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "GIVEN creating new Content on root WHEN saved and wizard closed THEN new Content should be listed"()
    {
        given:
        BaseAbstractContent rootContent = FolderContent.builder().
            withParent( ContentPath.ROOT ).
            withName( REPONAME ).
            withDisplayName( REPONAME ).
            build();

        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() );
        wizard.typeData( rootContent );

        when:
        wizard.save().close();

        then:
        contentBrowsePanel.exists( rootContent.getPath() );
    }

    def "GIVEN creating new Content on root WHEN saved and HomeButton clicked THEN new Content should be listed"()
    {
        given:
        BaseAbstractContent rootContent = DataContent.builder().
            withParent( ContentPath.ROOT ).
            withName( NameHelper.unqiueName( "datacontent" ) ).
            withDisplayName( "datacontent" ).
            build();

        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( rootContent.getContentTypeName() )
        wizard.typeData( rootContent );

        when:
        wizard.save();
        contentBrowsePanel.goToAppHome();

        then:
        contentBrowsePanel.exists( rootContent.getPath() );
    }


    def "GIVEN creating new Content beneath an existing unexpanded WHEN saved and wizard closed THEN parent should still be unexpanded"()
    {
        given:
        BaseAbstractContent content = FolderContent.builder().
            withParent( ContentPath.from( REPONAME ) ).
            withName( NameHelper.unqiueName( "folder" ) ).
            withDisplayName( "folder" ).
            build();

        contentBrowsePanel.clickByParentCheckbox( content.getPath().getParentPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() );
        wizard.typeData( content );

        when:
        wizard.save().close()

        then:
        !contentBrowsePanel.isRowExapnded( content.getParent().toString() )
    }

    def "GIVEN creating new Content beneath an existing unexpanded WHEN saved and HomeButton clicked THEN parent should still be unexpanded"()
    {
        given:
        BaseAbstractContent content = FolderContent.builder().
            withParent( ContentPath.from( REPONAME ) ).
            withName( NameHelper.unqiueName( "folder" ) ).
            withDisplayName( "folder" ).
            build();

        contentBrowsePanel.clickByParentCheckbox( content.getPath().getParentPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData(
            content );

        when:
        wizard.save();
        contentBrowsePanel.goToAppHome();

        then:
        !contentBrowsePanel.isRowExapnded( content.getParent().toString() );
    }

    def "GIVEN creating new Content beneath an existing expanded WHEN saved and wizard closed THEN new Content should be listed beneath parent"()
    {
        given:
        String name = NameHelper.unqiueName( "archive" );
        BaseAbstractContent content = ArchiveContent.builder().
            withName( name ).
            withDisplayName( "archive" ).
            withParent( ContentPath.from( REPONAME ) ).build();

        contentBrowsePanel.expandContent( content.getParent() );
        contentBrowsePanel.clickByParentCheckbox( content.getPath().getParentPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() );
        wizard.typeData( content );

        when:
        wizard.save().close();

        then:
        contentBrowsePanel.exists( content.getPath() );
    }

    def "GIVEN creating new Content beneath an existing expanded WHEN saved and HomeButton clicked THEN new Content should be listed beneath parent"()
    {
        given:
        String name = NameHelper.unqiueName( "folder" );
        BaseAbstractContent content = FolderContent.builder().
            withName( name ).
            withDisplayName( "folder" ).
            withParent( ContentPath.from( REPONAME ) ).build()

        contentBrowsePanel.expandContent( content.getParent() )
        contentBrowsePanel.clickByParentCheckbox( content.getPath().getParentPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() )
        wizard.typeData( content );

        when:
        wizard.save();
        contentBrowsePanel.goToAppHome();

        then:
        contentBrowsePanel.exists( content.getPath() ) && contentBrowsePanel.isRowExapnded( content.getParent().toString() )
    }

    def "GIVEN changing name of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new name"()
    {
        given:
        StructuredContent contentToEdit = StructuredContent.builder().
            withParent( ContentPath.from( REPONAME ) ).
            withName( "editname" ).
            withDisplayName( "editnametest" ).
            build();
        contentBrowsePanel.clickByParentCheckbox( contentToEdit.getPath().getParentPath() );

        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( contentToEdit.getContentTypeName() );
        wizard.typeData( contentToEdit ).save().close();
        StructuredContent newcontent = cloneContentWithNewName( contentToEdit )
        wizard = contentBrowsePanel.expandContent( contentToEdit.getParent() ).
            selectRowByCheckbox( contentToEdit.getPath() ).
            clickToolbarEdit();
        wizard.typeData( newcontent );

        when:
        wizard.save().close();

        then:
        contentBrowsePanel.waitsForSpinnerNotVisible();
        contentBrowsePanel.waituntilPageLoaded( Application.PAGELOAD_TIMEOUT );
        contentBrowsePanel.expandContent( newcontent.getParent() );
        TestUtils.saveScreenshot( getTestSession(), "editnametest1" );
        contentBrowsePanel.exists( newcontent.getPath() );

    }

    def "GIVEN changing displayName of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new displayName"()
    {
        given:
        StructuredContent contentToEdit = StructuredContent.builder().
            withParent( ContentPath.from( REPONAME ) ).
            withName( "editdisplayname" ).
            withDisplayName( "editdisplayname" ).
            build()
        contentBrowsePanel.clickByParentCheckbox( contentToEdit.getPath().getParentPath() )
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( contentToEdit.getContentTypeName() );
        wizard.typeData( contentToEdit ).save().close();

        StructuredContent newcontent = cloneContentWithNewDispalyName( contentToEdit );
        wizard = contentBrowsePanel.expandContent( contentToEdit.getParent() ).
            selectRowByCheckbox( contentToEdit.getPath() ).clickToolbarEdit();
        wizard.typeData( newcontent );

        when:
        wizard.save().close();

        then:
        contentBrowsePanel.waitsForSpinnerNotVisible()
        contentBrowsePanel.waituntilPageLoaded( Application.PAGELOAD_TIMEOUT )
        contentBrowsePanel.expandContent( newcontent.getParent() )
        contentBrowsePanel.exists( newcontent.getPath() )
    }

    StructuredContent cloneContentWithNewDispalyName( StructuredContent contentToedit )
    {
        String newDisplayName = NameHelper.unqiueName( "displaynamechanged" )
        return StructuredContent.builder().
            withName( contentToedit.getName() ).
            withDisplayName( newDisplayName ).
            withParent( contentToedit.getParent() ).
            build()
    }

    StructuredContent cloneContentWithNewName( StructuredContent source )
    {
        String newName = NameHelper.unqiueName( "newname" )
        return StructuredContent.builder().
            withName( newName ).
            withDisplayName( source.getDisplayName() ).
            withParent( source.getParent() ).
            build()

    }
}
