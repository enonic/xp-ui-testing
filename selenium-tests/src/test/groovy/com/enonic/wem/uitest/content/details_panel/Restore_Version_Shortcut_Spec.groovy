package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ShortcutFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.ContentSettings
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
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


    def "GIVEN existing shortcut is opened WHEN display name has been updated THEN new 'version history item' should appear"()
    {
        given:
        ContentSettings settings = ContentSettings.builder().language( NORSK_LANGUAGE ).build();
        SHORTCUT_CONTENT = buildShortcutWithSettingsAndTarget( "shortcut", null, INITIAL_DISPLAY_NAME, TARGET_1, settings );
        addContent( SHORTCUT_CONTENT );
        findAndSelectContent( SHORTCUT_CONTENT.getName() );
        VersionHistoryWidget allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_before_changing_shortcut" );

        when: "display name has been updated"
        contentBrowsePanel.clickToolbarEdit().typeDisplayName( NEW_DISPLAY_NAME ).save().closeBrowserTab().switchToBrowsePanelTab();
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_after_changing_shortcut" );

        then: "new 'version history item' should appear"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN existing shortcut is selected WHEN previous version has been reverted THEN expected display name appears in the grid"()
    {
        given: "existing shortcut is selected"
        findAndSelectContent( SHORTCUT_CONTENT.getName() );

        and: "version panel is opened"
        VersionHistoryWidget allContentVersionsView = openVersionPanel();

        when: "previous version has been reverted"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        versionItem.doRevertVersion();
        saveScreenshot( "shortcut_display_name_restored" );

        then: "correct display name appears in the grid"
        filterPanel.typeSearchText( INITIAL_DISPLAY_NAME );
        contentBrowsePanel.exists( SHORTCUT_CONTENT.getName() );
    }

    def "GIVEN existing shortcut is opened WHEN target has been changed THEN number of versions should be increased by 1"()
    {
        given: "existing shortcut is opened"
        findAndSelectContent( SHORTCUT_CONTENT.getName() );
        VersionHistoryWidget allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_before_changing_target" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        ShortcutFormViewPanel formViewPanel = new ShortcutFormViewPanel( getSession() );

        when: "target has been changed"
        formViewPanel.removeTarget().selectTarget( TARGET_2 );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();

        then: "number of versions should be increased"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN existing shortcut is opened WHEN version has been reverted THEN expected target should appear in the wizard"()
    {
        given: "existing shortcut with several versions is opened"
        findAndSelectContent( SHORTCUT_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        wizard.switchToBrowsePanelTab();

        and: "version panel is opened"
        VersionHistoryWidget allContentVersionsView = openVersionPanel();

        when: "previous version has been reverted"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        versionItem.doRevertVersion();
        contentBrowsePanel.switchToContentWizardTabBySelectedContent();
        saveScreenshot( "shortcut_target_restored" );
        ShortcutFormViewPanel formViewPanel = new ShortcutFormViewPanel( getSession() );

        then: "expected target should be displayed in the wizard"
        formViewPanel.getTargetDisplayName() == TARGET_1;
    }
}
