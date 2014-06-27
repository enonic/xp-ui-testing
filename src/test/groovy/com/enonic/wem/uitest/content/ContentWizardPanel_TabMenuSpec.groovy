package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Ignore
import spock.lang.Shared

class ContentWizardPanel_TabMenuSpec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }


    def "GIVEN started adding a 'Archive' and Wizard opened WHEN tab-menu button clicked THEN list of items with one name 'New Archive' is present"()
    {
        given:
        WizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.archiveMedia().toString() ).
            waitUntilWizardOpened();

        when:
        wizard.showTabMenuItems();

        then:
        wizard.isTabMenuItemPresent( "New Archive" );

    }

    def "GIVEN started adding a 'Archive' and 'Folder' two Wizards is opened WHEN tab-menu button clicked THEN list of items with two names is present"()
    {
        given:
        WizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.archiveMedia().toString() ).
            waitUntilWizardOpened();
        contentBrowsePanel.goToAppHome();
        wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            waitUntilWizardOpened();
        when:
        wizard.showTabMenuItems();

        then:
        wizard.isTabMenuItemPresent( "New Archive" ) && wizard.isTabMenuItemPresent( "New Folder" );

    }

    def "GIVEN content Wizard opened, no any data typed WHEN TabmenuItem(close) clicked THEN wizard closed and BrowsePanel showed"()
    {
        given:
        WizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.archiveMedia().toString() ).
            waitUntilWizardOpened().showTabMenuItems();

        when:
        SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( "New Archive" );

        then:
        dialog == null;

    }

   
    def "GIVEN content Wizard opened and name is typed WHEN TabmenuItem(close) clicked THEN 'SaveBeforeClose' dialog showed"()
    {
        given:
        String dispalyName = "testname";
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.archiveMedia().toString() ).
            waitUntilWizardOpened().typeDisplayName( dispalyName ).showTabMenuItems();

        when:
        SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( dispalyName );

        then:
        dialog != null;

    }

   
    def "GIVEN ContentWizard opened, data is typed, tabmenuItem(close) pressed WHEN Yes is chosen THEN new Content is listed in BrowsePanel"()
    {
        given:
        Content content = Content.builder().
            name( NameHelper.uniqueName( "archive" ) ).
            displayName( "archive" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.archiveMedia() ).
            build();
        WizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.archiveMedia().toString() ).
            waitUntilWizardOpened().typeData( content ).showTabMenuItems();

        when:
        SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( content.getDisplayName() );
        dialog.clickYesButton();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then:
        contentBrowsePanel.exists( ContentPath.from( content.getName() ) );

    }

}
