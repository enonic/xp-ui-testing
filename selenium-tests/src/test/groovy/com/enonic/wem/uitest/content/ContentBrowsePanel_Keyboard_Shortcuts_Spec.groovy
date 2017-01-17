package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 17.01.2017.
 *
 * Tasks:
 * XP-4870 Add Selenium tests for testing the main keyboard shortcuts
 *
 * */
@Stepwise
class ContentBrowsePanel_Keyboard_Shortcuts_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_FOLDER;

    def "GIVEN Content browse panel is opened WHEN 'Alt+N' have been pressed THEN 'new content' dialog should appear"()
    {
        when:
        contentBrowsePanel.pressNewContentKeyboardShortcut( "" );
        NewContentDialog dlg = new NewContentDialog( getSession() );
        then:
        saveScreenshot( "keyboard_shortcut_new" )
        dlg.waitUntilDialogLoaded( Application.EXPLICIT_NORMAL );
    }

    def "GIVEN existing folder WHEN the folder is selected AND shortcut to 'Edit selected content' has been pressed THEN new wizard tab with the content should be opened"()
    {
        given: "existing folder"
        TEST_FOLDER = buildFolderContent( "folder", "keyboard shortcuts" );
        addContent( TEST_FOLDER );

        when: "the folder is selected AND shortcut to 'Edit selected content' has been pressed"
        findAndSelectContent( TEST_FOLDER.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.pressEditSelectedContentKeyboardShortcut();
        saveScreenshot( "keyboard_shortcut_edit" )

        then: "new wizard tab with the content should be opened"
        wizard.getNameInputValue() == TEST_FOLDER.getName();
    }

    def "GIVEN existing folder WHEN the folder is selected AND shortcut to 'Delete selected content' has been pressed THEN 'Delete Content' modal dialog should be opened"()
    {
        given: "existing folder"
        findAndSelectContent( TEST_FOLDER.getName() );

        when: "the folder is selected AND shortcut to 'Edit selected content' has been pressed"
        DeleteContentDialog deleteContentDialog = contentBrowsePanel.pressDeleteSelectedContentKeyboardShortcut();
        List<String> items = deleteContentDialog.getDisplayNamesToDelete();
        saveScreenshot( "keyboard_shortcut_delete" )

        then: "'Delete Content' modal dialog should be opened"
        items.size() == 1;

        and: "correct display name should be present on the dialog"
        items.get( 0 ) == TEST_FOLDER.getDisplayName();
    }
}
