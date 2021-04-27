package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ComboBoxFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Occurrences_ComboBox_2_4_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    Content content_with_opt;

    def "GIVEN wizard for ComboBox-content(2:4) is opened WHEN name has been typed but options are not selected THEN content should be invalid and publish button should be disabled"()
    {
        given: "wizard for ComboBox-content(2:4) is opened"
        Content comboBoxContent = buildComboBox2_4_Content( 0 );
        ContentWizardPanel wizard = selectSitePressNew( comboBoxContent.getContentTypeName() );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );

        when: "only the name was typed and no option were selected"
        wizard.typeDisplayName( comboBoxContent.getDisplayName() );

        then: "'option filter' input should be present and enabled"
        formViewPanel.isOptionFilterInputEnabled();

        and: "no one option is selected on the page"
        formViewPanel.getSelectedOptionValues().size() == 0;

        and: "content should be invalid, because required option was not selected"
        !wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );
    }

    def "GIVEN new ComboBox2_4 is added (options are not selected) WHEN content has been opened THEN no one selected options should be present on the page"()
    {
        given: "new ComboBox2_4 is added"
        Content comboBoxContent = buildComboBox2_4_Content( 0 );
        selectSitePressNew( comboBoxContent.getContentTypeName() ).typeData(
            comboBoxContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( comboBoxContent );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "no one options is selected on the page"
        optValues.size() == 0;

        and: "options filter input should be enabled"
        formViewPanel.isOptionFilterInputEnabled();

        and: "content should be invalid, because required options were not selected"
        formViewPanel.getFormValidationRecording( 0 ) == "Min 2 occurrences required";
    }

    def "GIVEN new ComboBox-content(2:4) with two option is created WHEN content has been opened THEN two selected options should be present on the page"()
    {
        given: "new ComboBox2_4 is added'"
        content_with_opt = buildComboBox2_4_Content( 2 );
        selectSitePressNew( content_with_opt.getContentTypeName() ).typeData(
            content_with_opt ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "two options should be present in the form view"
        optValues.size() == 2;

        and: "options with correct text should be present"
        String[] options = ["option A", "option B"];
        optValues.containsAll( options.toList() );

        and: "options filter input is enabled, because 2 less than 4"
        formViewPanel.isOptionFilterInputEnabled();
    }

    def "GIVEN existing ComboBox-content(2:4) with two selected options is opened AND one option has been removed,the  content has been saved WHEN content has been opened THEN one option selected on the page "()
    {
        given: "content with one required option opened for edit' and one option removed"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        formViewPanel.clickOnLastRemoveButton();
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "when content selected in the grid and opened for edit again"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( content_with_opt );

        then: "one option selected in the form "
        saveScreenshot( "combobox_remove_option" )
        List<String> optValues = formViewPanel.getSelectedOptionValues();
        optValues.size() == 1;

        and: "content is invalid, because required fields- combobox2:4 not selected"
        formViewPanel.getFormValidationRecording( 0 ) == "Min 2 occurrences required";
    }

    def "GIVEN new ComboBox-content(2:4) with four options is created WHEN content has been opened THEN four selected options should be displayed and 'filter input' is disabled"()
    {
        given: "new content ComboBox2_4 is added'"
        Content comboBoxContent = buildComboBox2_4_Content( 4 );
        selectSitePressNew( comboBoxContent.getContentTypeName() ).typeData(
            comboBoxContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( comboBoxContent );
        ComboBoxFormViewPanel formViewPanel = new ComboBoxFormViewPanel( getSession() );
        List<String> optValues = formViewPanel.getSelectedOptionValues();

        then: "four options should be present in the form view"
        optValues.size() == 4;

        and: "expected options should be displayed"
        String[] options = ["option A", "option B", "option C", "option D"];
        optValues.containsAll( options.toList() );

        and: "options filter input should not be displayed, because 4 options are selected"
        !formViewPanel.isOptionFilterInputDisplayed();
    }

    def "WHEN content with 2 selected option has been published THEN the content should be with 'Published'-status"()
    {
        when: "content without options was saved and published"
        Content comboBoxContent = buildComboBox2_4_Content( 2 );
        ContentWizardPanel wizard = selectSitePressNew( comboBoxContent.getContentTypeName() ).typeData( comboBoxContent );
        wizard.showPublishMenu(  ).clickOnMarkAsReadyMenuItem(  );
        wizard.clickOnPublishButton().clickOnPublishNowButton();
        String publishedMessage = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );

        and: "wizard has been closed"
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( comboBoxContent.getName() );

        then: "the content should be with 'Published'-status"
        contentBrowsePanel.getContentStatus( comboBoxContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
        and:
        publishedMessage == String.format( Application.ITEM_IS_PUBLISHED_NOTIFICATION_MESSAGE, comboBoxContent.getName() );
    }
}
