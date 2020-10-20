package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ComboBoxFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared


class Restore_ComboBox_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content COMBOBOX_CONTENT;

    def "GIVEN combobox-content 2:4 (two selected options) is saved WHEN one selected option has been removed THEN number of versions increased by one"()
    {
        given: "new COMBOBOX-content (2:4) has been added"
        COMBOBOX_CONTENT = buildComboBox2_4_Content( 2 );
        ContentWizardPanel wizard = selectSitePressNew( COMBOBOX_CONTENT.getContentTypeName() );
        wizard.typeData( COMBOBOX_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "content opened and one option was removed"
        findAndSelectContent( COMBOBOX_CONTENT.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        formViewPanel.clickOnLastRemoveButton();
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "version panel is opened"
        VersionHistoryWidget allContentVersionsView = openVersionPanel();

        then: "number of versions should increase by one"
        allContentVersionsView.getAllVersions().size() == INITIAL_NUMBER_OF_VERSIONS + 1;
    }

    def "GIVEN existing combobox content with 3 versions WHEN valid version with two selected options has been reverted THEN content displayed as valid in the grid"()
    {
        given: "existing combobox content with 3 versions is selected "
        ContentWizardPanel wizard = findAndSelectContent( COMBOBOX_CONTENT.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        wizard.switchToBrowsePanelTab();

        and: "version history panel is expanded"
        VersionHistoryWidget allContentVersionsView = openVersionPanel();

        when: "valid version with two selected options has been reverted"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        versionItem.doRevertVersion();
        sleep(1000);

        then: "content should be displayed as valid in the grid"
        saveScreenshot( "combobox_valid_version" );
        !contentBrowsePanel.isContentInvalid( COMBOBOX_CONTENT.getName() );

        and: "'publish' button on the grid-toolbar should be enabled"
        contentBrowsePanel.isPublishButtonEnabled();

        and: "validation message should not be present on the form"
        contentBrowsePanel.switchToBrowserTabByTitle( COMBOBOX_CONTENT.getDisplayName() );
        !formViewPanel.isValidationMessagePresent();
    }

    def "GIVEN version with two images has been reverted WHEN content has been opened THEN two options should be displayed in the wizard"()
    {
        when: "version of content with two options has been restored and content opened"
        ContentWizardPanel wizard = findAndSelectContent( COMBOBOX_CONTENT.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        then: "two options should be displayed on the form"
        formViewPanel.getSelectedOptionValues().size() == 2;

        and: "'Mark as ready' button is getting visible"
        wizard.isMarAsReadyEnabled();

        and: "red icon should not be present on the wizard"
        !wizard.isContentInvalid();
    }

    def "GIVEN existing combobox content with 3 versions WHEN version of content with one option has been restored THEN red icon should appear on the wizard-tab and in the grid"()
    {
        given: "existing content with 3 versions opened"
        ContentWizardPanel wizard = findAndSelectContent( COMBOBOX_CONTENT.getName() ).clickToolbarEdit();
        wizard.switchToBrowsePanelTab();
        VersionHistoryWidget allContentVersionsView = openVersionPanel();

        when: "not valid version is reverted, one required image is missed"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        versionItem.doRevertVersion();
        saveScreenshot( "combobox_not_valid_version" );

        then: "the content should be displayed as invalid in the grid"
        saveScreenshot( "combobox_not_valid_version" );
        contentBrowsePanel.isContentInvalid( COMBOBOX_CONTENT.getName() );

        and: "red icon should appear on the wizard-tab"
        contentBrowsePanel.switchToBrowserTabByTitle( COMBOBOX_CONTENT.getDisplayName() );
        wizard.isContentInvalid();
    }

    def "GIVEN version with one images has been reverted WHEN content has been opened THEN one image should be displayed in the wizard"()
    {
        when: "version of content with one option has been restored and content is opened "
        findAndSelectContent( COMBOBOX_CONTENT.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        then: "only one option should be displayed in the form"
        formViewPanel.getSelectedOptionValues().size() == 1;
    }
}
