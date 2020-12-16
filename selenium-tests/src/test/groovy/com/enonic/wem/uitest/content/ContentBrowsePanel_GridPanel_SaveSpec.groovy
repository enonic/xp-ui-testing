package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.BaseContentType
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_SaveSpec
    extends BaseContentSpec
{
    @Shared
    Content PARENT_FOLDER;

    def "Preconditions: parent folder should be created"()
    {
        given: "folder-wizard is opened and data typed"
        PARENT_FOLDER = buildFolderContent( "parent-folder", "test folder" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName() );
        wizard.typeData( PARENT_FOLDER );

        when: "the content has been saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        then: "new content should be listed"
        contentBrowsePanel.exists( PARENT_FOLDER.getName() );
    }

    def "WHEN existing folder is collapsed and a child folder has been added THEN parent folder remains collapsed"()
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

    def "WHEN existing folder is unexpanded and a child folder has been saved THEN parent folder remains unexpanded"()
    {
        given: "creating new Content beneath an existing unexpanded folder"
        Content childContent = buildFolderContentWithParent( "folder", "child-folder2", PARENT_FOLDER.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( PARENT_FOLDER.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( childContent.getContentTypeName() ).
            typeData( childContent );

        when: "child folder has been saved"
        wizard.save();
        and: "go to browse panel"
        wizard.switchToBrowsePanelTab();

        then: "parent should be unexpanded"
        !contentBrowsePanel.isRowExpanded( PARENT_FOLDER.getName() );
    }

    def "GIVEN existing expanded folder is selected WHEN child content has been added THEN parent folder should be expanded"()
    {
        given: "select the parent folder and open new wizard"
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

    def "GIVEN existing child content is opened WHEN the name has been updated THEN the content should be listed with its new name"()
    {
        given: "new child content has been added"
        Content contentToEdit = buildFolderContentWithParent( "folder", "child-folder5", PARENT_FOLDER.getName() );
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

    def "GIVEN existing child content is opened WHEN display name has been changed THEN the content should be listed with its new displayName"()
    {
        given: "new child folder has been added"
        Content contentToEdit = buildFolderContentWithParent( "folder", "child-folder6", PARENT_FOLDER.getName() );
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
