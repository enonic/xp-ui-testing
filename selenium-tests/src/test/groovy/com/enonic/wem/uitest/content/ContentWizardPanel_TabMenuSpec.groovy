package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared

class ContentWizardPanel_TabMenuSpec
    extends BaseContentSpec
{
    @Shared
    String FOLDER_TAB_TITLE = "<Unnamed Folder>";

    @Shared
    String UNSTRUCTURED_TAB_MENU_ITEM = "<Unnamed Unstructured>";

    def "WHEN started adding a 'Folder' and Wizard opened  THEN new tab with name 'New Folder' is present"()
    {
        when:
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            waitUntilWizardOpened();

        then: "item menu with title should appears"
        contentBrowsePanel.isTabMenuItemPresent( FOLDER_TAB_TITLE );
    }

    def "WHEN started adding a 'Unstructured' and 'Folder' two Wizards is opened  THEN two tabs with are present"()
    {
        when:
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.unstructured().toString() ).
            waitUntilWizardOpened();
        contentBrowsePanel.pressAppHomeButton();
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            waitUntilWizardOpened();

        then:
        contentBrowsePanel.isTabMenuItemPresent( UNSTRUCTURED_TAB_MENU_ITEM ) &&
            contentBrowsePanel.isTabMenuItemPresent( FOLDER_TAB_TITLE );
    }

    def "GIVEN content Wizard opened, no any data typed WHEN TabmenuItem(close) clicked THEN wizard closed and BrowsePanel showed"()
    {
        given: "content wizard was opened ad AppBarTabMenu clicked"
        WizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            waitUntilWizardOpened();

        when: "no any data typed and 'close' button pressed"
        SaveBeforeCloseDialog dialog = wizard.close( FOLDER_TAB_TITLE );

        then: "close dialog should not showed"
        dialog == null;
    }

    def "GIVEN content Wizard opened and name is typed WHEN TabmenuItem(close) clicked THEN 'SaveBeforeClose' dialog showed"()
    {
        given:
        String displayName = "testname";
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            waitUntilWizardOpened().typeDisplayName( displayName );

        when:
        SaveBeforeCloseDialog dialog = wizard.close( displayName );

        then:
        dialog != null;
    }

    def "GIVEN ContentWizard opened, data is typed, tabmenuItem(close) pressed WHEN Yes is chosen THEN new Content is listed in BrowsePanel"()
    {
        given:
        Content content = buildFolderContent( "folder", "folder" );
        WizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            waitUntilWizardOpened().typeData( content );

        when: "'close' button in the tabMenu pressed and 'Yes' button on confirm dialog chosen "
        SaveBeforeCloseDialog dialog = wizard.close( content.getDisplayName() );
        dialog.clickYesButton();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then: "new content listed in the browse panel"
        contentBrowsePanel.exists( content.getName() );
    }
}
