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

    def "GIVEN wizard is opened and data typed WHEN saved and wizard closed THEN new content should be listed"()
    {
        given: "creating new Content on root"
        PARENT_FOLDER = buildFolderContent( "parent-folder", "test folder" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        wizard.typeData( PARENT_FOLDER );

        when: "saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        then: "new content should be listed"
        contentBrowsePanel.exists( PARENT_FOLDER.getName() );
    }

    def "GIVEN wizard for folder is opened WHEN data has been saved THEN the folder should be present in the grid"()
    {
        given: "wizard for folder is opened"
        Content rootContent = buildFolderContent( "folder", "test folder" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( rootContent.getContentTypeName() ).
            typeData( rootContent );

        when: "the data has been saved"
        wizard.save();
        wizard.switchToBrowsePanelTab();
        and: "the name of the content is typed on the search input"
        filterPanel.typeSearchText( rootContent.getName() );

        then: "the folder should be present in the grid"
        contentBrowsePanel.exists( rootContent.getName() );
    }

    def "GIVEN creating new Content beneath an existing unexpanded folder WHEN saved and wizard closed THEN parent folder should be collapsed"()
    {
        given: "creating new Content beneath an existing unexpanded folder"
        Content childContent = buildFolderContentWithParent( "folder", "child-folder1", PARENT_FOLDER.getName() );

        contentBrowsePanel.clickCheckboxAndSelectRow( PARENT_FOLDER.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( childContent.getContentTypeName() );
        wizard.typeData( childContent );

        when: "content has been saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        then: "parent folder should be collapsed"
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

    def "GIVEN existing expanded folder is selected WHEN new content was added and wizard closed THEN parent folder should be expanded"()
    {
        given: "creating of new content beneath the existing expanded folder"
        Content childContent = buildFolderContentWithParent( "folder", "child-folder3", PARENT_FOLDER.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( PARENT_FOLDER.getName() );
        contentBrowsePanel.expandContent( PARENT_FOLDER.getPath() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( childContent.getContentTypeName() );
        wizard.typeData( childContent );

        when: "child content has been saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        then: "the child should be listed beneath the parent"
        contentBrowsePanel.exists( childContent.getName() );
        and: "parent folder should be expanded"
        contentBrowsePanel.isRowExpanded( PARENT_FOLDER.getName() );
    }

    def "GIVEN existing child content is opened WHEN the name has been changed THEN the content should be listed with its new name"()
    {
        given: "new child content has been added"
        Content contentToEdit = buildFolderContentWithParent( "edit-name", "child-folder5", PARENT_FOLDER.getName() );
        findAndSelectContent( PARENT_FOLDER.getName() );
        addContent( contentToEdit );
        contentBrowsePanel.doClearSelection();
        and: "the child is opened"
        ContentWizardPanel contentWizard = findAndSelectContent( contentToEdit.getName() ).clickToolbarEditAndSwitchToWizardTab();
        String newName = NameHelper.uniqueName( "newname" );

        when: "new name has been saved and wizard closed"
        contentWizard.typeName( newName ).save().closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( newName )

        then: "the content should be listed with its new name"
        saveScreenshot( "test_content_edit_name" );
        contentBrowsePanel.exists( newName, true );
    }

    def "GIVEN existing child content is opened WHEN saved and wizard closed THEN Content is listed with its new displayName"()
    {
        given: "existing child content is opened"
        Content contentToEdit = buildFolderContentWithParent( "edit-displayname", "child-folder6", PARENT_FOLDER.getName() );
        findAndSelectContent( PARENT_FOLDER.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( contentToEdit.getContentTypeName() );
        wizard.typeData( contentToEdit ).save().closeBrowserTab().switchToBrowsePanelTab();

        contentBrowsePanel.doClearSelection();
        String newDisplayName = NameHelper.uniqueName( "display-name" );
        and: "the content is opened and new display name is typed"
        findAndSelectContent( contentToEdit.getName() ).clickToolbarEditAndSwitchToWizardTab().typeDisplayName( newDisplayName );

        when: "new display-name has been saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        then: "the content should be listed with its new displayName"
        filterPanel.typeSearchText( newDisplayName );
        contentBrowsePanel.exists( contentToEdit.getName() );
    }
}
