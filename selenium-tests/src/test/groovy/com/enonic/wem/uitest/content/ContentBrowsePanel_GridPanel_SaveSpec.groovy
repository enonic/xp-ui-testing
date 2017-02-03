package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
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
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        then: "new Content should be listed"
        contentBrowsePanel.exists( PARENT_FOLDER.getName() );
    }

    def "GIVEN creating new Content on root WHEN content saved and tab with the grid is switched THEN new Content should be listed"()
    {
        given: "creating new Content on root"
        Content rootContent = buildFolderContent( "folder", "test folder" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( rootContent.getContentTypeName() ).
            typeData( rootContent );

        when: "content saved and tab with the grid is switched"
        wizard.save();
        wizard.switchToBrowsePanelTab();
        and: "the name of the content was typed"
        filterPanel.typeSearchText( rootContent.getName() );

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
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        then: "parent should still be unexpanded"
        !contentBrowsePanel.isRowExpanded( PARENT_FOLDER.getName() );
    }

    def "GIVEN creating new Content beneath an existing unexpanded WHEN content saved and tab with the grid is switched THEN parent should still be unexpanded"()
    {
        given: "creating new Content beneath an existing unexpanded folder"
        Content childContent = buildFolderContentWithParent( "folder", "child-folder2", PARENT_FOLDER.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( PARENT_FOLDER.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( childContent.getContentTypeName() ).
            typeData( childContent );

        when: "child content saved and tab with the grid is switched"
        wizard.save();
        wizard.switchToBrowsePanelTab();

        then: "parent should still be unexpanded"
        !contentBrowsePanel.isRowExpanded( PARENT_FOLDER.getName() );
    }

    def "GIVEN creating new Content beneath an existing expanded WHEN saved and wizard closed THEN new Content should be listed beneath parent"()
    {
        given: "creating new Content beneath an existing expanded content"
        Content childContent = buildFolderContentWithParent( "folder", "child-folder3", PARENT_FOLDER.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( PARENT_FOLDER.getName() );
        contentBrowsePanel.expandContent( PARENT_FOLDER.getPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( childContent.getContentTypeName() );
        wizard.typeData( childContent );

        when: "child content saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        then: "new Content should be listed beneath parent"
        contentBrowsePanel.exists( childContent.getName() );
        and: "parent folder is expanded"
        contentBrowsePanel.isRowExpanded( PARENT_FOLDER.getName() );
    }

    def "GIVEN creating new Content beneath an existing expanded WHEN content saved and tab with the grid is opened THEN new Content should be listed beneath parent"()
    {
        given: "creating new Content beneath an existing expanded folder"
        Content childContent = buildFolderContentWithParent( "folder", "child-folder4", PARENT_FOLDER.getName() );
        contentBrowsePanel.expandContent( PARENT_FOLDER.getPath() );
        contentBrowsePanel.clickCheckboxAndSelectRow( PARENT_FOLDER.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( childContent.getContentTypeName() );
        wizard.typeData( childContent );

        when: "content saved and tab with the grid is opened"
        wizard.save();
        wizard.switchToBrowsePanelTab();

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
        ContentWizardPanel contentWizard = findAndSelectContent( contentToEdit.getName() ).clickToolbarEditAndSwitchToWizardTab();
        String newName = NameHelper.uniqueName( "newname" );

        when: "new name saved and wizard closed"
        contentWizard.typeName( newName ).save().closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( newName )

        then: "Content is listed with it's new name"
        saveScreenshot( "test_content_edit_name" );
        contentBrowsePanel.exists( newName, true );
    }

    def "GIVEN changing displayName of an existing Content WHEN saved and wizard closed THEN Content is listed with its new displayName"()
    {
        given: "changing of displayName of an existing Content"
        Content contentToEdit = buildFolderContentWithParent( "edit-displayname", "child-folder6", PARENT_FOLDER.getName() );
        findAndSelectContent( PARENT_FOLDER.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( contentToEdit.getContentTypeName() );
        wizard.typeData( contentToEdit ).save().closeBrowserTab().switchToBrowsePanelTab();

        contentBrowsePanel.clickOnClearSelection();
        String newDisplayName = NameHelper.uniqueName( "display-name" );
        findAndSelectContent( contentToEdit.getName() ).clickToolbarEditAndSwitchToWizardTab().typeDisplayName( newDisplayName );


        when: "new display-name saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        then: "content is listed with its new displayName"
        filterPanel.typeSearchText( newDisplayName );
        contentBrowsePanel.exists( contentToEdit.getName() );
    }
}
