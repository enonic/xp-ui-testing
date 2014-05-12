package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.schemamanger.ContentType
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.content.data.ContentData
import com.enonic.wem.api.data.DataSet
import com.enonic.wem.api.data.Value
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.TwoTextLineContentTypeCfg
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_SaveSpec
    extends BaseGebSpec
{
    @Shared
    String REPO_NAME = "test-folder";

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    @Ignore
    def "experiment"()
    {
        given:

        String twoTextlineCFG = TwoTextLineContentTypeCfg.CFG;
        ContentType contentType = ContentType.newContentType().
            name( "person" ).
            configData( twoTextlineCFG ).
            build();

        ContentData data = new ContentData();
        data.setProperty( "firstName", Value.newString( "Jorund" ) );
        data.addProperty( "firstName", Value.newString( "Vier" ) );
        data.addProperty( "surname", Value.newString( "Skriubakken" ) );
        DataSet mySet = new DataSet( "address" );
        mySet.setProperty( "street", Value.newString( "Sonstlei" ) )
        mySet.setProperty( "postalCode", Value.newString( "1763" ) )
        mySet.setProperty( "postalPlace", Value.newString( "HALDEN" ) )
        data.add( mySet );

        Content rootContent = Content.builder().
            parent( ContentPath.ROOT ).
            name( REPO_NAME ).
            displayName( REPO_NAME ).
            contentType( ContentTypeName.structured() ).
            data( data ).
            build();

        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( rootContent.getContentTypeName() );
        wizard.typeData( rootContent );
        wizard.clickDataStep().type( rootContent.data );

        when:
        wizard.save().close();

        then:
        contentBrowsePanel.exists( rootContent.getPath() );
    }

    def "GIVEN creating new Content on root WHEN saved and wizard closed THEN new Content should be listed"()
    {
        given:
        Content rootContent = Content.builder().
            parent( ContentPath.ROOT ).
            name( REPO_NAME ).
            displayName( REPO_NAME ).
            contentType( ContentTypeName.folder() ).
            build();

        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        wizard.typeData( rootContent );

        when:
        wizard.save().close();
		contentBrowsePanel.waituntilPageLoaded(2);

        then:
        contentBrowsePanel.exists( rootContent.getPath() );
    }

    def "GIVEN creating new Content on root WHEN saved and HomeButton clicked THEN new Content should be listed"()
    {
        given:
        Content rootContent = Content.builder().
            parent( ContentPath.ROOT ).
            name( NameHelper.uniqueName( "datacontent" ) ).
            displayName( "datacontent" ).
            contentType( ContentTypeName.dataMedia() ).
            build();

        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( rootContent.getContentTypeName() ).
            typeData( rootContent );


        when:
        wizard.save();
        contentBrowsePanel.goToAppHome();

        then:
        contentBrowsePanel.exists( rootContent.getPath() );
    }

    def "GIVEN creating new Content beneath an existing unexpanded WHEN saved and wizard closed THEN parent should still be unexpanded"()
    {
        given:
        Content content = Content.builder().
            parent( ContentPath.from( REPO_NAME ) ).
            name( NameHelper.uniqueName( "folder" ) ).
            displayName( "folder" ).
            contentType( ContentTypeName.folder() ).
            build();

        contentBrowsePanel.clickByParentCheckbox( content.getPath().getParentPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() );
        wizard.typeData( content );

        when:
        wizard.save().close();
		contentBrowsePanel.waituntilPageLoaded(2);

        then:
        !contentBrowsePanel.isRowExapnded( content.getParent().toString() );
    }

    def "GIVEN creating new Content beneath an existing unexpanded WHEN saved and HomeButton clicked THEN parent should still be unexpanded"()
    {
        given:
        Content content = Content.builder().
            parent( ContentPath.from( REPO_NAME ) ).
            name( NameHelper.uniqueName( "folder" ) ).
            displayName( "folder" ).
            contentType( ContentTypeName.folder() ).
            build();

        contentBrowsePanel.clickByParentCheckbox( content.getPath().getParentPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).
            typeData( content );

        when:
        wizard.save();
        contentBrowsePanel.goToAppHome();

        then:
        !contentBrowsePanel.isRowExapnded( content.getParent().toString() );
    }

    def "GIVEN creating new Content beneath an existing expanded WHEN saved and wizard closed THEN new Content should be listed beneath parent"()
    {
        given:
        String name = NameHelper.uniqueName( "archive" );
        Content content = Content.builder().
            name( name ).
            displayName( "archive" ).
            contentType( ContentTypeName.archiveMedia() ).
            parent( ContentPath.from( REPO_NAME ) ).
            build();

        contentBrowsePanel.expandContent( content.getParent() );
        contentBrowsePanel.clickByParentCheckbox( content.getPath().getParentPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() );
        wizard.typeData( content );

        when:
        wizard.save().close();
		contentBrowsePanel.waituntilPageLoaded(2);
        TestUtils.saveScreenshot( getTestSession(), name );

        then:
        contentBrowsePanel.exists( content.getPath() );
    }

    def "GIVEN creating new Content beneath an existing expanded WHEN saved and HomeButton clicked THEN new Content should be listed beneath parent"()
    {
        given:
        String name = NameHelper.uniqueName( "folder" );
        Content content = Content.builder().
            name( name ).
            displayName( "folder" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.from( REPO_NAME ) ).
            build()

        contentBrowsePanel.expandContent( content.getParent() );
        contentBrowsePanel.clickByParentCheckbox( content.getPath().getParentPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() );
        wizard.typeData( content );

        when:
        wizard.save();
        contentBrowsePanel.goToAppHome();

        then:
        contentBrowsePanel.exists( content.getPath() ) && contentBrowsePanel.isRowExapnded( content.getParent().toString() );
    }

    def "GIVEN changing name of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new name"()
    {
        given:
        Content contentToEdit = Content.builder().
            parent( ContentPath.from( REPO_NAME ) ).
            name( "editname" ).
            displayName( "editnametest" ).
            contentType( ContentTypeName.structured() ).
            build();
        contentBrowsePanel.clickByParentCheckbox( contentToEdit.getPath().getParentPath() );

        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( contentToEdit.getContentTypeName() );
        wizard.typeData( contentToEdit ).save().close();
		contentBrowsePanel.waituntilPageLoaded(2);
        Content newcontent = cloneContentWithNewName( contentToEdit )
        wizard = contentBrowsePanel.expandContent( contentToEdit.getParent() ).
            clickCheckboxAndSelectRow( contentToEdit.getPath() ).clickToolbarEdit();
        wizard.typeData( newcontent );

        when:
        wizard.save().close();

        then:
        contentBrowsePanel.waitsForSpinnerNotVisible();
        contentBrowsePanel.waituntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        TestUtils.saveScreenshot( getTestSession(), "editnametest1" );
        contentBrowsePanel.exists( newcontent.getPath() );

    }

    def "GIVEN changing displayName of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new displayName"()
    {
        given:
        Content contentToEdit = Content.builder().
            parent( ContentPath.from( REPO_NAME ) ).
            name( "editdisplayname" ).
            displayName( "editdisplayname" ).
            contentType( ContentTypeName.structured() ).
            build();
        contentBrowsePanel.clickByParentCheckbox( contentToEdit.getPath().getParentPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( contentToEdit.getContentTypeName() );
        wizard.typeData( contentToEdit ).save().close();
		contentBrowsePanel.waituntilPageLoaded(2);

        Content newcontent = cloneContentWithNewDispalyName( contentToEdit );
        wizard = contentBrowsePanel.expandContent( contentToEdit.getParent() ).clickCheckboxAndSelectRow( contentToEdit.getPath() ).
            clickToolbarEdit();
        wizard.typeData( newcontent );

        when:
        wizard.save().close();

        then:
        contentBrowsePanel.waitsForSpinnerNotVisible();
        contentBrowsePanel.waituntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        contentBrowsePanel.exists( newcontent.getPath() );
    }

    Content cloneContentWithNewDispalyName( Content source )
    {
        String newDisplayName = NameHelper.uniqueName( "displaynamechanged" );
        return Content.builder().
            name( source.getName() ).
            displayName( newDisplayName ).
            parent( source.getParent() ).
            contentType( source.getContentTypeName() ).
            build();
    }

    Content cloneContentWithNewName( Content source )
    {
        String newName = NameHelper.uniqueName( "newname" );
        return Content.builder().
            name( newName ).
            displayName( source.getDisplayName() ).
            parent( source.getParent() ).
            contentType( source.getContentTypeName() ).
            build();

    }
}
