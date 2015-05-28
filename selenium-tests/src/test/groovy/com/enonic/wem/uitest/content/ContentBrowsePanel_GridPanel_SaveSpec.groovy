package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_SaveSpec
    extends BaseContentSpec
{
    @Shared
    Content PARENT_FOLDER;

    def "GIVEN creating new Content on root WHEN saved and wizard closed THEN new Content should be listed"()
    {
        given:
        PARENT_FOLDER = buildFolderContent( "testfolder", "test folder" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        wizard.typeData( PARENT_FOLDER );

        when:
        wizard.save().close( PARENT_FOLDER.getDisplayName() );

        then:
        contentBrowsePanel.exists( PARENT_FOLDER.getPath() );
    }

    def "GIVEN creating new Content on root WHEN saved and HomeButton clicked THEN new Content should be listed"()
    {
        given:
        Content rootContent = buildFolderContent( "folder", "test folder" );
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
            parent( PARENT_FOLDER.getPath() ).
            name( NameHelper.uniqueName( "folder" ) ).
            displayName( "folder" ).
            contentType( ContentTypeName.folder() ).
            build();

        contentBrowsePanel.clickOnParentCheckbox( content.getPath().getParentPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() );
        wizard.typeData( content );

        when:
        wizard.save().close( content.getDisplayName() );

        then:
        !contentBrowsePanel.isRowExpanded( content.getParent().toString() );
    }

    def "GIVEN creating new Content beneath an existing unexpanded WHEN saved and HomeButton clicked THEN parent should still be unexpanded"()
    {
        given:
        Content content = Content.builder().
            parent( PARENT_FOLDER.getPath() ).
            name( NameHelper.uniqueName( "folder" ) ).
            displayName( "folder" ).
            contentType( ContentTypeName.folder() ).
            build();

        contentBrowsePanel.clickOnParentCheckbox( content.getPath().getParentPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).
            typeData( content );

        when:
        wizard.save();
        contentBrowsePanel.goToAppHome();

        then:
        !contentBrowsePanel.isRowExpanded( content.getParent().toString() );
    }

    def "GIVEN creating new Content beneath an existing expanded WHEN saved and wizard closed THEN new Content should be listed beneath parent"()
    {
        given:
        String name = NameHelper.uniqueName( "folder" );
        Content content = Content.builder().
            name( name ).
            displayName( "folder-child" ).
            contentType( ContentTypeName.folder() ).
            parent( PARENT_FOLDER.getPath() ).
            build();

        contentBrowsePanel.expandContent( content.getParent() );
        contentBrowsePanel.clickOnParentCheckbox( content.getPath().getParentPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() );
        wizard.typeData( content );

        when:
        wizard.save().close( content.getDisplayName() );
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
            parent( PARENT_FOLDER.getPath() ).
            build()

        contentBrowsePanel.expandContent( content.getParent() );
        contentBrowsePanel.clickOnParentCheckbox( content.getPath().getParentPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() );
        wizard.typeData( content );

        when:
        wizard.save();
        contentBrowsePanel.goToAppHome();

        then:
        contentBrowsePanel.exists( content.getPath() ) && contentBrowsePanel.isRowExpanded( content.getParent().toString() );
    }

    def "GIVEN changing name of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new name"()
    {
        given:
        Content contentToEdit = Content.builder().
            parent( PARENT_FOLDER.getPath() ).
            name( "editname" ).
            displayName( "editnametest" ).
            contentType( ContentTypeName.unstructured() ).
            build();
        contentBrowsePanel.clickOnParentCheckbox( contentToEdit.getPath().getParentPath() );
        addContent( contentToEdit );

        Content newContent = cloneContentWithNewName( contentToEdit )
        contentBrowsePanel.expandContent( contentToEdit.getParent() );
        TestUtils.saveScreenshot( getTestSession(), "editnametest" );
        contentBrowsePanel.deSelectContentInTable( PARENT_FOLDER.getPath() );
        ContentWizardPanel contentWizard = contentBrowsePanel.clickCheckboxAndSelectRow( contentToEdit.getPath() ).clickToolbarEdit();
        contentWizard.typeData( newContent );

        when:
        contentWizard.save().close( newContent.getDisplayName() );

        then:
        contentBrowsePanel.waitsForSpinnerNotVisible();
        contentBrowsePanel.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );

        TestUtils.saveScreenshot( getTestSession(), "editnametest1" );
        contentBrowsePanel.exists( newContent.getName(), true );

    }

    def "GIVEN changing displayName of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new displayName"()
    {
        given:
        Content contentToEdit = Content.builder().
            parent( PARENT_FOLDER.getPath() ).
            name( "editdisplayname" ).
            displayName( "editdisplayname" ).
            contentType( ContentTypeName.unstructured() ).
            build();
        contentBrowsePanel.clickOnParentCheckbox( PARENT_FOLDER.getPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( contentToEdit.getContentTypeName() );
        wizard.typeData( contentToEdit ).save().close( contentToEdit.getDisplayName() );

        Content newContent = cloneContentWithNewDisplayName( contentToEdit );
        contentBrowsePanel.deSelectContentInTable( PARENT_FOLDER.getPath() );
        wizard = contentBrowsePanel.expandContent( contentToEdit.getParent() ).clickCheckboxAndSelectRow( contentToEdit.getPath() ).
            clickToolbarEdit();
        wizard.typeData( newContent );

        when:
        wizard.save().close( newContent.getDisplayName() );

        then:
        contentBrowsePanel.waitsForSpinnerNotVisible();
        contentBrowsePanel.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );

        contentBrowsePanel.exists( newContent.getPath() );
    }

    Content cloneContentWithNewDisplayName( Content source )
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
