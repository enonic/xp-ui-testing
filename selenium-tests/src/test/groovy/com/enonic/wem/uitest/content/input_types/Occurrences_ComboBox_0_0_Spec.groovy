package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUtils
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ComboBoxFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_ComboBox_0_0_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    Content COMBOBOX_0_0_CONTENT;

    def "WHEN wizard for adding a Combo box-content(0:0) opened THEN option filter input is present "()
    {
        when: "start to add a content with type 'ComboBox 0:0'"
        Content comboBoxContent = buildComboBox0_0_Content( 1 );
        selectSitePressNew( comboBoxContent.getContentTypeName() );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        then: "option filter input is present and enabled"
        formViewPanel.isOptionFilterInputEnabled();
        and:
        formViewPanel.getSelectedOptionValues().size() == 0;
    }

    def "GIVEN saving of Combobox-content (0:0) without options WHEN content opened for edit THEN no one selected options present in form view"()
    {
        given: "new content with type Combobbox0_0 was added'"
        Content comboBoxContent = buildComboBox0_0_Content( 0 );
        selectSitePressNew( comboBoxContent.getContentTypeName() ).typeData(
            comboBoxContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "combobox content is opened"
        findAndSelectContent( comboBoxContent.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "no one options should be present in form view"
        optValues.size() == 0;

        and: "options filter input should be enabled"
        formViewPanel.isOptionFilterInputEnabled();
    }

    def "GIVEN saving of  Combobox-content (0:0) with one option WHEN content is opened THEN one selected option and button 'Remove' should be present in the form view"()
    {
        given: "new content with type ComboBox 0_0 was added"
        Content comboBoxContent = buildComboBox0_0_Content( 1 );
        selectSitePressNew( comboBoxContent.getContentTypeName() ).typeData(
            comboBoxContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "content is opened"
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

    def "GIVEN saving of ComboBox-content (0:0) with three options WHEN content opened for edit THEN three options should be displayed  and button 'Remove' should be present on page "()
    {
        given: "new content with type ComboBox0_0 was added'"
        COMBOBOX_0_0_CONTENT = buildComboBox0_0_Content( 3 );
        selectSitePressNew( COMBOBOX_0_0_CONTENT.getContentTypeName() ).typeData(
            COMBOBOX_0_0_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "content is opened"
        findAndSelectContent( COMBOBOX_0_0_CONTENT.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "three options should be present on the form view"
        optValues.size() == 3;

        and: "and options have a correct text"
        String[] options = ["option A", "option B", "option C"];
        optValues.containsAll( options.toList() );

        and: "options filter input is enabled"
        formViewPanel.isOptionFilterInputEnabled();
    }

    def "GIVEN ComboBox-content (0:0) with three options and one option was removed and content saved WHEN content is opened THEN two selected options and buttons 'Remove' should be present on the page "()
    {
        given: "content with tree options opened for edit' and one option removed"
        ContentWizardPanel wizard = findAndSelectContent( COMBOBOX_0_0_CONTENT.getName() ).clickToolbarEdit();
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        formViewPanel.clickOnLastRemoveButton();
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been opened again"
        contentBrowsePanel.clickToolbarEdit();

        then: "only two options should be present on the form view"
        List<String> optValues = formViewPanel.getSelectedOptionValues();
        saveScreenshot( "combo_removed_option" );
        optValues.size() == 2;

        and: "correct text should be displayed in both options"
        String[] options = ["option A", "option B"];
        optValues.containsAll( options.toList() );
    }

    def "GIVEN existing ComboBox 0:0 with options  WHEN content is opened and 'Publish' on toolbar has been pressed THEN content's  status is getting 'Online'"()
    {
        given: "existing new ComboBox 0:0 with options'"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( COMBOBOX_0_0_CONTENT );

        when: "the content has been published"
        wizard.clickOnWizardPublishButton().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL ).clickOnPublishNowButton().waitForDialogClosed();
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.clickOnCleanFilter().typeSearchText( COMBOBOX_0_0_CONTENT.getName() );

        then: "content's  status is getting 'Online'"
        contentBrowsePanel.getContentStatus( COMBOBOX_0_0_CONTENT.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
    }

    def "GIVEN creating of a new ComboBox 0:0 without options WHEN content was saved and 'Publish' on toolbar pressed THEN the content should be displayed with 'Online' status"()
    {
        given: "ComboBox 0:0 content was added"
        Content comboBoxContent = buildComboBox0_0_Content( 0 );
        filterPanel.typeSearchText( SITE_NAME );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            comboBoxContent.getContentTypeName() ).typeData( comboBoxContent ).save();

        when: "the content has been published in the wizard"
        wizard.clickOnWizardPublishButton().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL ).clickOnPublishNowButton().waitForDialogClosed();
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( comboBoxContent.getName() );

        then: "the content should be displayed with 'Online' status"
        contentBrowsePanel.getContentStatus( comboBoxContent.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
    }

    private Content buildComboBox0_0_Content( int numberOptions )
    {
        PropertyTree data = ContentUtils.buildComboBoxData( numberOptions );
        Content comboboxContent = Content.builder().
            name( NameHelper.uniqueName( "cbox0_0_" ) ).
            displayName( "combobox0_0 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":combobox0_0" ).data( data ).
            build();
        return comboboxContent;
    }
}