package com.enonic.wem.uitest.content

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
        given: "creating new Content on root"
        PARENT_FOLDER = buildFolderContent( "parent-folder", "test folder" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        wizard.typeData( PARENT_FOLDER );

        when: "saved and wizard closed"
        wizard.save().close( PARENT_FOLDER.getDisplayName() );

        then: "new Content should be listed"
        contentBrowsePanel.exists( PARENT_FOLDER.getName() );
    }

    def "GIVEN creating new Content on root WHEN saved and HomeButton clicked THEN new Content should be listed"()
    {
        given: "creating new Content on root"
        Content rootContent = buildFolderContent( "folder", "test folder" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( rootContent.getContentTypeName() ).
            typeData( rootContent );

        when: "content saved and HomeButton clicked"
        wizard.save();
        contentBrowsePanel.pressAppHomeButton();

        then: "new Content should be listed"
        contentBrowsePanel.exists( rootContent.getName() );
    }

    def "GIVEN creating new Content beneath an existing unexpanded folder WHEN saved and wizard closed THEN parent should still be unexpanded"()
    {
        given: "creating new Content beneath an existing unexpanded folder"
        Content childContent = buildFolderContentWithParent( "folder", "child-folder1", PARENT_FOLDER.getName() );

        contentBrowsePanel.clickCheckboxAndSelectRow( PARENT_FOLDER.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( childContent.getContentTypeName() );
        wizard.typeData( childContent );

        when: "content saved and wizard closed"
        wizard.save().close( childContent.getDisplayName() );

        then: "parent should still be unexpanded"
        !contentBrowsePanel.isRowExpanded( PARENT_FOLDER.getName() );
    }

    def "GIVEN creating new Content beneath an existing unexpanded WHEN saved and HomeButton clicked THEN parent should still be unexpanded"()
    {
        given: "creating new Content beneath an existing unexpanded folder"
        Content childContent = buildFolderContentWithParent( "folder", "child-folder2", PARENT_FOLDER.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( PARENT_FOLDER.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( childContent.getContentTypeName() ).
            typeData( childContent );

        when: "child content saved and HomeButton clicked"
        wizard.save();
        contentBrowsePanel.pressAppHomeButton();

        then: "parent should still be unexpanded"
        !contentBrowsePanel.isRowExpanded( PARENT_FOLDER.getName() );
    }

    def "GIVEN creating new Content beneath an existing expanded WHEN saved and wizard closed THEN new Content should be listed beneath parent"()
    {
        given: "creating new Content beneath an existing expanded content"
        String name = NameHelper.uniqueName( "folder" );
        Content childContent = buildFolderContentWithParent( name, "child-folder3", PARENT_FOLDER.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( PARENT_FOLDER.getName() );
        contentBrowsePanel.expandContent( PARENT_FOLDER.getPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( childContent.getContentTypeName() );
        wizard.typeData( childContent );

        when: "child content saved and wizard closed"
        wizard.save().close( childContent.getDisplayName() );
        TestUtils.saveScreenshot( getTestSession(), name );

        then: "new Content should be listed beneath parent"
        contentBrowsePanel.exists( childContent.getName() );
        and: "parent folder is expanded"
        contentBrowsePanel.isRowExpanded( PARENT_FOLDER.getName() );
    }

    def "GIVEN creating new Content beneath an existing expanded WHEN saved and HomeButton clicked THEN new Content should be listed beneath parent"()
    {
        given: "creating new Content beneath an existing expanded folder"
        String name = NameHelper.uniqueName( "folder" );
        Content childContent = buildFolderContentWithParent( name, "child-folder4", PARENT_FOLDER.getName() );
        contentBrowsePanel.expandContent( PARENT_FOLDER.getPath() );
        contentBrowsePanel.clickCheckboxAndSelectRow( PARENT_FOLDER.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( childContent.getContentTypeName() );
        wizard.typeData( childContent );

        when: "saved and HomeButton clicked"
        wizard.save();
        contentBrowsePanel.pressAppHomeButton();

        then: "new Content should be listed beneath parent"
        contentBrowsePanel.exists( childContent.getName() )
        and: "and parent folder is expanded"
        contentBrowsePanel.isRowExpanded( PARENT_FOLDER.getName() );
    }

    def "GIVEN changing name of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new name"()
    {
        given: "changing name of an existing Content"
        Content contentToEdit = buildFolderContentWithParent( "edit-name", "child-folder5", PARENT_FOLDER.getName() );
        findAndSelectContent( PARENT_FOLDER.getName() );
        addContent( contentToEdit );
        contentBrowsePanel.clickOnClearSelection();
        filterPanel.typeSearchText( contentToEdit.getName() )
        ContentWizardPanel contentWizard = contentBrowsePanel.clickCheckboxAndSelectRow( contentToEdit.getName() ).clickToolbarEdit();
        String newName = NameHelper.uniqueName( "newname" );

        when: "new name saved and wizard closed"
        contentWizard.typeName( newName ).save().close( contentToEdit.getDisplayName() );
        filterPanel.typeSearchText( newName )

        then: "Content is listed with it's new name"
        TestUtils.saveScreenshot( getTestSession(), "editnametest" );
        contentBrowsePanel.exists( newName, true );
    }

    def "GIVEN changing displayName of an existing Content WHEN saved and wizard closed THEN Content is listed with it's new displayName"()
    {
        given: "changing of displayName of an existing Content"
        Content contentToEdit = buildFolderContentWithParent( "edit-displayname", "child-folder6", PARENT_FOLDER.getName() );
        findAndSelectContent( PARENT_FOLDER.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( contentToEdit.getContentTypeName() );
        wizard.typeData( contentToEdit ).save().close( contentToEdit.getDisplayName() );

        contentBrowsePanel.clickOnClearSelection();
        filterPanel.typeSearchText( contentToEdit.getName() );
        String newDisplayName = NameHelper.uniqueName( "display-name" );
        wizard =
            contentBrowsePanel.clickCheckboxAndSelectRow( contentToEdit.getName() ).clickToolbarEdit().typeDisplayName( newDisplayName );

        when: "new display-name saved and wizard closed"
        wizard.save().close( newDisplayName );

        then: "content is listed with it's new displayName"
        filterPanel.typeSearchText( newDisplayName );
        contentBrowsePanel.exists( contentToEdit.getName() );
    }
}
