package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ContentWizardPanel_TabMenuSpec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    String FOLDER_TAB_MENU_ITEM = "<New Folder>"

    @Shared
    String UNSTRUCTURED_TAB_MENU_ITEM = "<New Unstructured>"

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }


    def "WHEN started adding a 'Folder' and Wizard opened  THEN new tab with name 'New Folder' is present"()
    {
        when:
        WizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            waitUntilWizardOpened();

        then: "item menu with title should appears"
        wizard.isTabMenuItemPresent( FOLDER_TAB_MENU_ITEM );

    }


    def "WHEN started adding a 'Unstructured' and 'Folder' two Wizards is opened  THEN two tabs with are present"()
    {
        when:
        WizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.unstructured().toString() ).
            waitUntilWizardOpened();
        contentBrowsePanel.goToAppHome();
        wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            waitUntilWizardOpened();


        then:
        wizard.isTabMenuItemPresent( UNSTRUCTURED_TAB_MENU_ITEM ) && wizard.isTabMenuItemPresent( FOLDER_TAB_MENU_ITEM );

    }


    def "GIVEN content Wizard opened, no any data typed WHEN TabmenuItem(close) clicked THEN wizard closed and BrowsePanel showed"()
    {
        given: "content wizard was opened ad AppBarTabMenu clicked"
        WizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            waitUntilWizardOpened();

        when: "no any data typed and 'close' button pressed"
        SaveBeforeCloseDialog dialog = wizard.closeTabMenuItem( FOLDER_TAB_MENU_ITEM );

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
        SaveBeforeCloseDialog dialog = wizard.closeTabMenuItem( displayName );

        then:
        dialog != null;

    }


    def "GIVEN ContentWizard opened, data is typed, tabmenuItem(close) pressed WHEN Yes is chosen THEN new Content is listed in BrowsePanel"()
    {
        given:
        Content content = Content.builder().
            name( NameHelper.uniqueName( "folder" ) ).
            displayName( "folder" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.folder() ).
            build();
        WizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            waitUntilWizardOpened().typeData( content );

        when: "'close' button in the tabMenu pressed and 'Yes' button on confirm dialog chosen "
        SaveBeforeCloseDialog dialog = wizard.closeTabMenuItem( content.getDisplayName() );
        dialog.clickYesButton();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then: "new content listed in the browse panel"
        contentBrowsePanel.exists( ContentPath.from( content.getName() ) );

    }

}
