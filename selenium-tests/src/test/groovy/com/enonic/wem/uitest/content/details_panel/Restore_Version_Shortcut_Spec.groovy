package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ShortcutFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentSettings
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Ignore
@Stepwise
class Restore_Version_Shortcut_Spec
    extends BaseVersionHistorySpec
{
    @Shared
    Content SHORTCUT_CONTENT;

    @Shared
    String INITIAL_DISPLAY_NAME = "shortcut-restore-version";

    @Shared
    String NEW_DISPLAY_NAME = "sh-new-display-name";

    @Shared
    String TARGET_1 = "server";

    @Shared
    String TARGET_2 = "whale";


    def "GIVEN existing shortcut WHEN display name of the shortcut changed THEN new 'version history item' appeared in the version-view"()
    {
        given:
        ContentSettings settings = ContentSettings.builder().language( NORSK_LANGUAGE ).build();
        SHORTCUT_CONTENT = buildShortcutWithSettingsAndTarget( "shortcut", null, INITIAL_DISPLAY_NAME, TARGET_1, settings );
        addContent( SHORTCUT_CONTENT );
        findAndSelectContent( SHORTCUT_CONTENT.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_before_changing_shortcut" );


        when: "display name of the folder changed"
        contentBrowsePanel.clickToolbarEdit().typeDisplayName( NEW_DISPLAY_NAME ).save().closeBrowserTab().switchToBrowsePanelTab();
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_after_changing_shortcut" );

        then: "new 'version history item' appeared in the version-view"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN existing shortcut and 'display name' has been updated WHEN the shortcut selected AND previous version restored THEN correct display name appears in the grid"()
    {
        given: "existing shortcut and 'display name' has been updated"
        findAndSelectContent( SHORTCUT_CONTENT.getName() );

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "the shortcut selected AND previous version restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        saveScreenshot( "shortcut_display_name_restored" );

        then: "correct display name appears in the grid"
        filterPanel.typeSearchText( INITIAL_DISPLAY_NAME );
        contentBrowsePanel.exists( SHORTCUT_CONTENT.getName() );
    }

    def "GIVEN existing shortcut WHEN target changed THEN new target displayed on wizard AND number of versions is increased"()
    {
        given: "existing shortcut"
        findAndSelectContent( SHORTCUT_CONTENT.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_before_changing_target" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        ShortcutFormViewPanel formViewPanel = new ShortcutFormViewPanel( getSession() );

        when: "target changed"
        formViewPanel.removeTarget().selectTarget( TARGET_2 );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();

        then: "number of versions increased"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN existing shortcut with two versions for target AND wizard opened WHEN version of shortcut changed THEN correct target present on the wizard"()
    {
        given: "existing folder with updated 'display name'"
        findAndSelectContent( SHORTCUT_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        wizard.switchToBrowsePanelTab();

        and: "version panel is opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "the shortcut selected AND previous version restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        contentBrowsePanel.switchToContentWizardTabBySelectedContent();
        saveScreenshot( "shortcut_target_restored" );
        ShortcutFormViewPanel formViewPanel = new ShortcutFormViewPanel( getSession() );

        then: "expected target should be displayed in the wizard"
        formViewPanel.getTargetDisplayName() == TARGET_1
    }
}
