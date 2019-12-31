package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUtils
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ComboBoxFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_ComboBox_0_0_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    Content COMBOBOX_0_0_CONTENT;

    def "WHEN wizard for Combo box-content(0:0) is opened THEN option filter input should be present"()
    {
        when: "wizard is opened: 'ComboBox 0:0'"
        Content comboBoxContent = buildComboBox0_0_Content( 1 );
        selectSitePressNew( comboBoxContent.getContentTypeName() );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        then: "option filter input should be present and enabled"
        formViewPanel.isOptionFilterInputEnabled();
        and:
        formViewPanel.getSelectedOptionValues().size() == 0;
    }

    def "GIVEN new Combobox-content (0:0) without options is added WHEN content has been opened THEN options should not be selected"()
    {
        given: "new Combobbox0_0 is added'"
        Content comboBoxContent = buildComboBox0_0_Content( 0 );
        selectSitePressNew( comboBoxContent.getContentTypeName() ).typeData(
            comboBoxContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "combobox content has been reopened"
        findAndSelectContent( comboBoxContent.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "options should not be selected:"
        optValues.size() == 0;

        and: "options filter input should be enabled"
        formViewPanel.isOptionFilterInputEnabled();
    }

    def "GIVEN new Combobox-content(0:0) (one option is selected) WHEN the content is reopened THEN one selected option and button 'Remove' should be displayed"()
    {
        given: "new content with type ComboBox 0_0 was added"
        Content comboBoxContent = buildComboBox0_0_Content( 1 );
        selectSitePressNew( comboBoxContent.getContentTypeName() ).typeData(
            comboBoxContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "content has been reopened"
        findAndSelectContent( comboBoxContent.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "one option should be present in form view"
        optValues.size() == 1;

        and: "correct text should be present in the option"
        optValues.get( 0 ) == "option A";

        and: "options filter should be enabled, because new options can be selected"
        formViewPanel.isOptionFilterInputEnabled();
    }

    def "GIVEN new ComboBox-content (0:0) with three options is added WHEN content has been opened THEN three options should be displayed and button 'Remove' should be present on page "()
    {
        given: "new content with type ComboBox0_0 is added'"
        COMBOBOX_0_0_CONTENT = buildComboBox0_0_Content( 3 );
        selectSitePressNew( COMBOBOX_0_0_CONTENT.getContentTypeName() ).typeData(
            COMBOBOX_0_0_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "content has been opened"
        findAndSelectContent( COMBOBOX_0_0_CONTENT.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "three options should be present on the form view"
        optValues.size() == 3;

        and: "and options have a correct text"
        String[] options = ["option A", "option B", "option C"];
        optValues.containsAll( options.toList() );

        and: "options filter input should be enabled"
        formViewPanel.isOptionFilterInputEnabled();
    }

    def "GIVEN existing ComboBox-content (0:0) with three options is opened AND one option has been removed and content saved WHEN content has been reopened THEN two selected options and buttons 'Remove' should be present on the page "()
    {
        given: "content with tree options is opened AND one option has been removed"
        ContentWizardPanel wizard = findAndSelectContent( COMBOBOX_0_0_CONTENT.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        formViewPanel.clickOnLastRemoveButton();
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been reopened"
        contentBrowsePanel.clickToolbarEdit();

        then: "only two options should be present on the form view"
        List<String> optValues = formViewPanel.getSelectedOptionValues();
        saveScreenshot( "combo_removed_option" );
        optValues.size() == 2;

        and: "expected text should be displayed in both options"
        String[] options = ["option A", "option B"];
        optValues.containsAll( options.toList() );
    }

    def "GIVEN existing ComboBox 0:0 (options are selected) is opened WHEN the content has been published THEN content's  status is getting 'Published'"()
    {
        given: "existing new ComboBox 0:0 is opened'"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( COMBOBOX_0_0_CONTENT );

        when: "the content has been published"
        wizard.showPublishMenu().clickOnMarkAsReadyMenuItem();
        wizard.clickOnWizardPublishButton().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL ).clickOnPublishButton().waitForDialogClosed();
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.clickOnCleanFilter().typeSearchText( COMBOBOX_0_0_CONTENT.getName() );

        then: "content's  status is getting 'Published'"
        contentBrowsePanel.getContentStatus( COMBOBOX_0_0_CONTENT.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }

    def "GIVEN new ComboBox 0:0 without options is added WHEN the content has been published THEN the content should be displayed with 'Published' status"()
    {
        given: "ComboBox 0:0 content is added"
        Content comboBoxContent = buildComboBox0_0_Content( 0 );
        filterPanel.typeSearchText( SITE_NAME );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() ).typeData( comboBoxContent );
        wizard.showPublishMenu().clickOnMarkAsReadyMenuItem();

        when: "the content has been published in the wizard"
        wizard.clickOnWizardPublishButton().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL ).clickOnPublishButton().waitForDialogClosed();
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( comboBoxContent.getName() );

        then: "the content should be displayed with 'Published' status"
        contentBrowsePanel.getContentStatus( comboBoxContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }

    private Content buildComboBox0_0_Content( int numberOptions )
    {
        PropertyTree data = ContentUtils.buildComboBoxData( numberOptions );
        Content comboboxContent = Content.builder().
            name( NameHelper.uniqueName( "cbox0_0_" ) ).
            displayName( "combobox0_0 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "combobox0_0" ).data( data ).
            build();
        return comboboxContent;
    }
}