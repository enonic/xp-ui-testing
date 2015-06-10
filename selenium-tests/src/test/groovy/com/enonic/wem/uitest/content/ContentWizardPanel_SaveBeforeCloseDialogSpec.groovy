package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared

class ContentWizardPanel_SaveBeforeCloseDialogSpec
    extends BaseContentSpec
{
    @Shared
    String newDisplayName = "changeDisplayName"

    def "GIVEN a unchanged Content WHEN closing THEN SaveBeforeCloseDialog must not appear"()
    {
        given:
        Content content = buildFolderContent( "folder", "save before close" );
        WizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            waitUntilWizardOpened().typeData( content ).save();

        when:
        SaveBeforeCloseDialog dialog = wizard.close( content.getDisplayName() );

        then:
        dialog == null;
    }

    def " GIVEN a changed Content WHEN closing THEN SaveBeforeCloseDialog must appear"()
    {
        given: "wizard opened data typed and 'Save' pressed AND displayName changed"
        Content content = buildFolderContent( "folder", "save before close" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            typeData( content ).save();
        wizard.typeDisplayName( newDisplayName );

        when: "'Close' button pressed"
        SaveBeforeCloseDialog dialog = wizard.close( newDisplayName )
        TestUtils.saveScreenshot( getSession(), "SaveBeforeCloseDialog-appears" );

        then: "modal dialog appears"
        dialog != null;
    }

    def "GIVEN changing name of an existing Content and wizard closing WHEN Yes is chosen THEN Content is listed in BrowsePanel with it's new name"()
    {
        given:
        Content content = buildFolderContent( "folder", "folder" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().
            selectContentType( ContentTypeName.folder().toString() ).typeData( content ).save();
        wizard.typeDisplayName( newDisplayName ).close( newDisplayName );
        SaveBeforeCloseDialog dialog = new SaveBeforeCloseDialog( getSession() );
        dialog.waitForPresent();

        when: "Yes is chosen"
        dialog.clickYesButton();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then: "Content is listed in BrowsePanel with it's new name"
        contentBrowsePanel.exists( content.getName() );
    }

    def "GIVEN changing name of an existing Content and wizard closing WHEN No is chosen THEN Content is listed in BrowsePanel with it's original name"()
    {
        given:
        Content content = buildFolderContent( "folder", "folder" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            typeData( content ).save();
        String newName = NameHelper.uniqueName( "newfolder" );
        SaveBeforeCloseDialog dialog = wizard.typeName( newName ).close( content.getDisplayName() );

        when: "No is chosen"
        dialog.clickNoButton();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then: "Content is listed in BrowsePanel with it's original name"
        contentBrowsePanel.exists( content.getName() ) && !contentBrowsePanel.exists( newName );
    }

    def "GIVEN changing an existing Content and wizard closing WHEN Cancel is chosen THEN wizard is still open"()
    {
        given:
        Content content = buildFolderContent( "folder", "folder" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            typeData( content ).save();
        SaveBeforeCloseDialog dialog = wizard.typeName( "newfolder" ).close( content.getDisplayName() );

        when:
        dialog.clickCancelButton();
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getSession(), "SaveBeforeCloseDialog-cancel" );

        then:
        wizard.isOpened();
    }
}
