package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ComboBoxFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

/**
 * Tasks:
 * XP-4948 Add Selenium tests for checking of 'red icon' (invalid content) in wizards
 */
class Restore_ComboBox_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content COMBOBOX_CONTENT;

    def "GIVEN existing combobox-content 2:4 with two selected options WHEN content opened and one option was removed THEN number of versions increased by one"()
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

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        then: "number of versions increased by one"
        allContentVersionsView.getAllVersions().size() == INITIAL_NUMBER_OF_VERSIONS + 1;
    }

    def "GIVEN existing combobox content with 3 versions WHEN valid version with two selected options is restored THEN content displayed as valid in the grid"()
    {
        given: "existing combobox content with 3 versions is selected "
        ContentWizardPanel wizard = findAndSelectContent( COMBOBOX_CONTENT.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        wizard.switchToBrowsePanelTab();

        and: "version history panel is expanded"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "valid version with two selected options is restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );

        then: "content displayed as valid in the grid"
        saveScreenshot( "combobox_valid_version" );
        !contentBrowsePanel.isContentInvalid( COMBOBOX_CONTENT.getName() );

        and: "'publish' button on the grid-toolbar should be enabled"
        contentBrowsePanel.isPublishButtonEnabled();

        and: "validation message should not be present on the form"
        contentBrowsePanel.switchToBrowserTabByTitle(COMBOBOX_CONTENT.getDisplayName(  ));
        !formViewPanel.isValidationMessagePresent();

    }

    def "GIVEN version of content with two images has been restored WHEN content opened THEN two options are displayed on the wizard"()
    {
        when: "version of content with two options has been restored and content opened"
        ContentWizardPanel wizard = findAndSelectContent( COMBOBOX_CONTENT.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        then: "two options are displayed in the form"
        formViewPanel.getSelectedOptionValues().size() == 2;

        and: "'publish' button is enabled"
        wizard.isPublishButtonEnabled();
    }

    def "GIVEN existing combobox content with 3 versions WHEN version of content with one option has been restored THEN red icon should appear on the wizard-tab and in the grid"()
    {
        given: "existing content with 3 versions opened"
        ContentWizardPanel wizard = findAndSelectContent( COMBOBOX_CONTENT.getName() ).clickToolbarEdit();
        wizard.switchToBrowsePanelTab();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "not valid version of content is restored, one required image missed"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        versionItem.doRestoreVersion( versionItem.getId() );
        saveScreenshot( "combobox_not_valid_version" );

        and: "switch to wizard tab"
        def isRedIconInGrid = contentBrowsePanel.isContentInvalid( COMBOBOX_CONTENT.getName() );
        contentBrowsePanel.switchToBrowserTabByTitle( COMBOBOX_CONTENT.getDisplayName(  ) )

        then: "the content is invalid in the grid as well"
        saveScreenshot( "combobox_not_valid_version" );
        contentBrowsePanel.isContentInvalid( COMBOBOX_CONTENT.getName() );

        and: "red icon should appear on the wizard-tab"
        contentBrowsePanel.switchToBrowserTabByTitle( COMBOBOX_CONTENT.getDisplayName(  ) );
        wizard.isContentInvalid();

        then: "red icon should be present on the wizard-tab"
        wizard.isContentInvalid();

        and: "the content should be displayed as invalid in the grid"
        isRedIconInGrid;
    }

    def "GIVEN version of content with one images is restored WHEN content opened THEN one image should be displayed on the wizard"()
    {
        when: "version of content with one option has been restored and content is opened "
        findAndSelectContent( COMBOBOX_CONTENT.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        then: "only one option should be displayed in the form"
        formViewPanel.getSelectedOptionValues().size() == 1;
    }
}
